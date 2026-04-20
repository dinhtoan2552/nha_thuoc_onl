package controller.user;

import dao.DonHangDAO;
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
import model.NguoiDung;
import model.Thuoc;
import utils.FileUploadUtil;

@WebServlet("/xac-nhan-dat-hang-ngay")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 10 * 1024 * 1024,
        maxRequestSize = 20 * 1024 * 1024
)
public class XacNhanDatHangNgayServlet extends HttpServlet {

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

        String idThuocStr = request.getParameter("idThuoc");
        String soLuongStr = request.getParameter("soLuong");
        String tenNguoiNhan = request.getParameter("tenNguoiNhan");
        String sdtNhan = request.getParameter("sdtNhan");
        String diaChiNhan = request.getParameter("diaChiNhan");
        String ghiChu = request.getParameter("ghiChu");
        String phuongThucThanhToan = request.getParameter("phuongThucThanhToan");
        String nguon = request.getParameter("nguon");

        int idThuoc;
        int soLuong;

        try {
            idThuoc = Integer.parseInt(idThuocStr);
            soLuong = Integer.parseInt(soLuongStr);
        } catch (Exception e) {
            session.setAttribute("cartMessage", "Dữ liệu đặt hàng không hợp lệ.");
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        if (tenNguoiNhan == null || tenNguoiNhan.trim().isEmpty()
                || sdtNhan == null || sdtNhan.trim().isEmpty()
                || diaChiNhan == null || diaChiNhan.trim().isEmpty()
                || phuongThucThanhToan == null || phuongThucThanhToan.trim().isEmpty()) {
            session.setAttribute("cartMessage", "Vui lòng nhập đầy đủ thông tin nhận hàng.");
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        ThuocDAO thuocDAO = new ThuocDAO();
        Thuoc thuoc = thuocDAO.getThuocById(idThuoc);

        if (thuoc == null || thuoc.getSoLuong() <= 0 || "HET_HANG".equalsIgnoreCase(thuoc.getTrangThai())) {
            session.setAttribute("cartMessage", "Sản phẩm không tồn tại hoặc đã hết hàng.");
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        if (soLuong < 1) {
            soLuong = 1;
        }

        if (soLuong > thuoc.getSoLuong()) {
            soLuong = thuoc.getSoLuong();
        }

        double donGia = thuoc.getDonGia();
        double tongTien = donGia * soLuong;
        double phiVanChuyen = thuoc.getPhiShip(); // ship chỉ tính 1 lần
        double tongThanhToan = tongTien + phiVanChuyen;

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

        DonHangDAO donHangDAO = new DonHangDAO();

        int idDonHang = donHangDAO.taoDonHangTheoThongTin(
                user,
                thuoc,
                soLuong,
                tenNguoiNhan.trim(),
                sdtNhan.trim(),
                diaChiNhan.trim(),
                ghiChu,
                phuongThucThanhToan.trim(),
                maChuyenKhoan,
                anhBill,
                phiVanChuyen
        );

        if (idDonHang == -1) {
            session.setAttribute("cartMessage", "Đặt hàng thất bại.");
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        if ("cart".equalsIgnoreCase(nguon)) {
            GioHangDAO gioHangDAO = new GioHangDAO();
            int idGioHang = gioHangDAO.getOrCreateGioHang(user.getId());
            if (idGioHang != -1) {
                gioHangDAO.xoaKhoiGio(idGioHang, idThuoc);
            }
        }

        if ("CHUYEN_KHOAN".equals(phuongThucThanhToan) || "THE".equals(phuongThucThanhToan)) {
            session.setAttribute(
                    "cartMessage",
                    "Đã gửi đơn và ảnh bill thành công. Đơn hàng đang chờ nhân viên xác nhận thanh toán. Mã đơn: #" + idDonHang
            );
        } else {
            session.setAttribute("cartMessage", "Đặt hàng thành công. Mã đơn: #" + idDonHang);
        }

        response.sendRedirect(request.getContextPath() + "/don-hang-cua-toi");
    }
}