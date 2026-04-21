package controller.user;

import dao.CauHinhThanhToanDAO;
import dao.ThuocDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.CauHinhThanhToan;
import model.NguoiDung;
import model.Thuoc;

@WebServlet("/dat-hang-ngay")
public class DatHangNgayServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/user/home");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        NguoiDung user = (NguoiDung) session.getAttribute("user");
        String vaiTro = user.getVaiTro() != null ? user.getVaiTro().trim().toUpperCase() : "";

        if (!"USER".equals(vaiTro)) {
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String idThuocStr = request.getParameter("idThuoc");
        String soLuongStr = request.getParameter("soLuong");
        String nguon = request.getParameter("nguon");

        int idThuoc;
        int soLuong;

        try {
            idThuoc = Integer.parseInt(idThuocStr);
            soLuong = Integer.parseInt(soLuongStr);
        } catch (Exception e) {
            session.setAttribute("cartMessage", "Dữ liệu đặt hàng không hợp lệ.");
            response.sendRedirect(request.getContextPath() + "/user/home");
            return;
        }

        if (soLuong < 1) {
            soLuong = 1;
        }

        ThuocDAO thuocDAO = new ThuocDAO();
        Thuoc thuoc = thuocDAO.getThuocById(idThuoc);

        if (thuoc == null) {
            session.setAttribute("cartMessage", "Không tìm thấy sản phẩm.");
            response.sendRedirect(request.getContextPath() + "/user/home");
            return;
        }

        if (thuoc.getSoLuong() <= 0 || "HET_HANG".equalsIgnoreCase(thuoc.getTrangThai())) {
            session.setAttribute("cartMessage", "Sản phẩm này hiện đã hết hàng.");

            if ("detail".equalsIgnoreCase(nguon)) {
                response.sendRedirect(request.getContextPath() + "/chi-tiet-thuoc?id=" + idThuoc);
            } else {
                response.sendRedirect(request.getContextPath() + "/user/home");
            }
            return;
        }

        if (soLuong > thuoc.getSoLuong()) {
            soLuong = thuoc.getSoLuong();
        }

        CauHinhThanhToanDAO cauHinhDAO = new CauHinhThanhToanDAO();
        CauHinhThanhToan cauHinhThanhToan = cauHinhDAO.getCauHinh();

        double tongTien = thuoc.getDonGia() * soLuong;
        double phiVanChuyen = thuoc.getPhiShip(); // ship chỉ tính 1 lần
        double tongThanhToan = tongTien + phiVanChuyen;

        request.setAttribute("thuoc", thuoc);
        request.setAttribute("soLuong", soLuong);
        request.setAttribute("tongTien", tongTien);
        request.setAttribute("phiVanChuyen", phiVanChuyen);
        request.setAttribute("tongThanhToan", tongThanhToan);

        request.setAttribute("hoTenNhan", user.getHoTen() != null ? user.getHoTen() : "");
        request.setAttribute("sdtNhan", user.getSoDienThoai() != null ? user.getSoDienThoai() : "");
        request.setAttribute("diaChiNhan", user.getDiaChi() != null ? user.getDiaChi() : "");
        request.setAttribute("nguon", nguon != null ? nguon : "home");

        request.setAttribute("cauHinhThanhToan", cauHinhThanhToan);

        if (cauHinhThanhToan != null) {
            request.setAttribute("tenNganHangQr", cauHinhThanhToan.getTenNganHangQr());
            request.setAttribute("soTaiKhoanQr", cauHinhThanhToan.getSoTaiKhoanQr());
            request.setAttribute("chuTaiKhoanQr", cauHinhThanhToan.getChuTaiKhoanQr());
            request.setAttribute("anhQR", cauHinhThanhToan.getAnhQR());

            request.setAttribute("tenNganHangThe", cauHinhThanhToan.getTenNganHangThe());
            request.setAttribute("soTaiKhoanThe", cauHinhThanhToan.getSoTaiKhoanThe());
            request.setAttribute("chiNhanhThe", cauHinhThanhToan.getChiNhanhThe());
            request.setAttribute("chuThe", cauHinhThanhToan.getChuThe());
            request.setAttribute("anhThe", cauHinhThanhToan.getAnhThe());

            request.setAttribute("batCOD", cauHinhThanhToan.isBatCOD());
            request.setAttribute("batChuyenKhoan", cauHinhThanhToan.isBatChuyenKhoan());
            request.setAttribute("batThe", cauHinhThanhToan.isBatThe());
        } else {
            request.setAttribute("batCOD", false);
            request.setAttribute("batChuyenKhoan", false);
            request.setAttribute("batThe", false);
        }

        request.getRequestDispatcher("/jsp/user/thanh_toan_ngay.jsp").forward(request, response);
    }
}