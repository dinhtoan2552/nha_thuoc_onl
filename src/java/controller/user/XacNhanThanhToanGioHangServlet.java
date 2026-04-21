package controller.user;

import dao.GioHangDAO;
import dao.ThuocDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.GioHangItem;
import model.NguoiDung;
import model.Thuoc;
import utils.DBConnection;
import utils.FileUploadUtil;

@WebServlet("/xac-nhan-thanh-toan-gio-hang")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 10 * 1024 * 1024,
        maxRequestSize = 20 * 1024 * 1024
)
public class XacNhanThanhToanGioHangServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        FileUploadUtil.createUploadDirIfNotExists();

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        NguoiDung user = (NguoiDung) session.getAttribute("user");

        String tenNguoiNhan = request.getParameter("tenNguoiNhan");
        String sdtNhan = request.getParameter("sdtNhan");
        String diaChiNhan = request.getParameter("diaChiNhan");
        String ghiChu = request.getParameter("ghiChu");
        String phuongThucThanhToan = request.getParameter("phuongThucThanhToan");

        if (tenNguoiNhan == null || tenNguoiNhan.trim().isEmpty()
                || sdtNhan == null || sdtNhan.trim().isEmpty()
                || diaChiNhan == null || diaChiNhan.trim().isEmpty()
                || phuongThucThanhToan == null || phuongThucThanhToan.trim().isEmpty()) {
            session.setAttribute("cartMessage", "Vui lòng nhập đầy đủ thông tin nhận hàng.");
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        GioHangDAO gioHangDAO = new GioHangDAO();
        ThuocDAO thuocDAO = new ThuocDAO();
        List<GioHangItem> dsGioHang = gioHangDAO.getDanhSachGioHang(user.getId());

        if (dsGioHang == null || dsGioHang.isEmpty()) {
            session.setAttribute("cartMessage", "Giỏ hàng của bạn đang trống.");
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        String maChuyenKhoan = null;
        String anhBill = null;

        if ("CHUYEN_KHOAN".equals(phuongThucThanhToan) || "THE".equals(phuongThucThanhToan)) {
            Part billPart = request.getPart("anhBill");

            if (billPart == null || billPart.getSize() <= 0) {
                session.setAttribute("cartMessage", "Vui lòng tải lên ảnh bill/chứng từ thanh toán.");
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }

            anhBill = FileUploadUtil.saveImage(billPart);
            maChuyenKhoan = "CK-" + user.getId() + "-" + System.currentTimeMillis();
        }

        Connection conn = null;
        PreparedStatement psDonHang = null;
        PreparedStatement psChiTiet = null;
        PreparedStatement psUpdateThuoc = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            double tongTien = 0;
            double phiVanChuyen = 0;

            List<Thuoc> dsThuocHopLe = new ArrayList<>();
            List<Integer> dsSoLuongHopLe = new ArrayList<>();

            for (GioHangItem item : dsGioHang) {
                Thuoc thuoc = thuocDAO.getThuocById(item.getIdThuoc());

                if (thuoc == null || thuoc.getSoLuong() <= 0 || "HET_HANG".equalsIgnoreCase(thuoc.getTrangThai())) {
                    continue;
                }

                int soLuong = item.getSoLuong();
                if (soLuong < 1) {
                    soLuong = 1;
                }
                if (soLuong > thuoc.getSoLuong()) {
                    soLuong = thuoc.getSoLuong();
                }

                if (soLuong <= 0) {
                    continue;
                }

                dsThuocHopLe.add(thuoc);
                dsSoLuongHopLe.add(soLuong);

                tongTien += thuoc.getDonGia() * soLuong;

                if (thuoc.getPhiShip() > phiVanChuyen) {
                    phiVanChuyen = thuoc.getPhiShip();
                }
            }

            if (dsThuocHopLe.isEmpty()) {
                conn.rollback();
                session.setAttribute("cartMessage", "Không có sản phẩm hợp lệ để thanh toán.");
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }

            String trangThaiThanhToan = "CHUA_THANH_TOAN";
            if ("CHUYEN_KHOAN".equals(phuongThucThanhToan) || "THE".equals(phuongThucThanhToan)) {
                trangThaiThanhToan = "CHO_XAC_NHAN";
            }

            String sqlDonHang = "INSERT INTO donhang "
                    + "(idNguoiDung, ngayDat, tongTien, phiVanChuyen, trangThai, tenNguoiNhan, diaChiNhan, sdtNhan, ghiChu, "
                    + "phuongThucThanhToan, trangThaiThanhToan, maChuyenKhoan, anhBill, nguoiXuLy) "
                    + "VALUES (?, NOW(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            psDonHang = conn.prepareStatement(sqlDonHang, Statement.RETURN_GENERATED_KEYS);
            psDonHang.setInt(1, user.getId());
            psDonHang.setDouble(2, tongTien);
            psDonHang.setDouble(3, phiVanChuyen);
            psDonHang.setString(4, "CHO_XAC_NHAN");
            psDonHang.setString(5, tenNguoiNhan.trim());
            psDonHang.setString(6, diaChiNhan.trim());
            psDonHang.setString(7, sdtNhan.trim());
            psDonHang.setString(8, (ghiChu == null || ghiChu.trim().isEmpty()) ? null : ghiChu.trim());
            psDonHang.setString(9, phuongThucThanhToan.trim());
            psDonHang.setString(10, trangThaiThanhToan);
            psDonHang.setString(11, maChuyenKhoan);
            psDonHang.setString(12, anhBill);
            psDonHang.setNull(13, java.sql.Types.INTEGER);

            if (psDonHang.executeUpdate() <= 0) {
                conn.rollback();
                session.setAttribute("cartMessage", "Đặt hàng thất bại.");
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }

            rs = psDonHang.getGeneratedKeys();
            int idDonHang = -1;
            if (rs.next()) {
                idDonHang = rs.getInt(1);
            }

            if (idDonHang == -1) {
                conn.rollback();
                session.setAttribute("cartMessage", "Không tạo được đơn hàng.");
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }

            String sqlChiTiet = "INSERT INTO chitietdonhang "
                    + "(idDonHang, idThuoc, soLuong, donGia, phiShip, thanhTien) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";
            psChiTiet = conn.prepareStatement(sqlChiTiet);

            String sqlUpdateThuoc = "UPDATE thuoc SET soLuong = soLuong - ? WHERE idThuoc = ? AND soLuong >= ?";
            psUpdateThuoc = conn.prepareStatement(sqlUpdateThuoc);

            for (int i = 0; i < dsThuocHopLe.size(); i++) {
                Thuoc thuoc = dsThuocHopLe.get(i);
                int soLuong = dsSoLuongHopLe.get(i);
                double thanhTien = thuoc.getDonGia() * soLuong;

                psChiTiet.setInt(1, idDonHang);
                psChiTiet.setInt(2, thuoc.getIdThuoc());
                psChiTiet.setInt(3, soLuong);
                psChiTiet.setDouble(4, thuoc.getDonGia());
                psChiTiet.setDouble(5, thuoc.getPhiShip());
                psChiTiet.setDouble(6, thanhTien);
                psChiTiet.addBatch();

                psUpdateThuoc.setInt(1, soLuong);
                psUpdateThuoc.setInt(2, thuoc.getIdThuoc());
                psUpdateThuoc.setInt(3, soLuong);
                psUpdateThuoc.addBatch();
            }

            psChiTiet.executeBatch();
            int[] updateResults = psUpdateThuoc.executeBatch();

            for (int result : updateResults) {
                if (result <= 0) {
                    conn.rollback();
                    session.setAttribute("cartMessage", "Một số sản phẩm không đủ tồn kho.");
                    response.sendRedirect(request.getContextPath() + "/cart");
                    return;
                }
            }

            int idGioHang = gioHangDAO.getOrCreateGioHang(user.getId());
            if (idGioHang != -1) {
                try (PreparedStatement psClear = conn.prepareStatement("DELETE FROM chitietgiohang WHERE idGioHang = ?")) {
                    psClear.setInt(1, idGioHang);
                    psClear.executeUpdate();
                }
            }

            conn.commit();

            if ("CHUYEN_KHOAN".equals(phuongThucThanhToan) || "THE".equals(phuongThucThanhToan)) {
                session.setAttribute("cartMessage",
                        "Đã gửi đơn và ảnh bill thành công. Đơn hàng đang chờ nhân viên xác nhận thanh toán. Mã đơn: #" + idDonHang);
            } else {
                session.setAttribute("cartMessage", "Đặt hàng thành công. Mã đơn: #" + idDonHang);
            }

            response.sendRedirect(request.getContextPath() + "/don-hang-cua-toi");

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            session.setAttribute("cartMessage", "Đặt hàng thất bại.");
            response.sendRedirect(request.getContextPath() + "/cart");
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (psUpdateThuoc != null) psUpdateThuoc.close(); } catch (Exception e) {}
            try { if (psChiTiet != null) psChiTiet.close(); } catch (Exception e) {}
            try { if (psDonHang != null) psDonHang.close(); } catch (Exception e) {}
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (Exception e) {}
        }
    }
}