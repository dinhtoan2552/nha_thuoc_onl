package controller.staff;

import dao.DonHangDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.NguoiDung;

@WebServlet("/staff/cap-nhat-thanh-toan")
public class StaffCapNhatTrangThaiThanhToanServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        NguoiDung staff = (NguoiDung) session.getAttribute("user");

        String idDonHangStr = request.getParameter("idDonHang");
        String trangThaiThanhToan = request.getParameter("trangThaiThanhToan");
        String ghiChuThanhToan = request.getParameter("ghiChuThanhToan");

        int idDonHang;
        try {
            idDonHang = Integer.parseInt(idDonHangStr);
        } catch (Exception e) {
            session.setAttribute("staffOrderMessage", "Mã đơn không hợp lệ.");
            response.sendRedirect(request.getContextPath() + "/staff/donhang");
            return;
        }

        if (trangThaiThanhToan == null || trangThaiThanhToan.trim().isEmpty()) {
            session.setAttribute("staffOrderMessage", "Thiếu trạng thái thanh toán.");
            response.sendRedirect(request.getContextPath() + "/staff/donhang");
            return;
        }

        DonHangDAO donHangDAO = new DonHangDAO();
        boolean success = donHangDAO.capNhatTrangThaiThanhToan(
                idDonHang,
                trangThaiThanhToan.trim(),
                ghiChuThanhToan,
                staff.getId()
        );

        if (success) {
            session.setAttribute("staffOrderMessage", "Cập nhật trạng thái thanh toán thành công.");
        } else {
            session.setAttribute("staffOrderMessage", "Cập nhật trạng thái thanh toán thất bại.");
        }

        response.sendRedirect(request.getContextPath() + "/staff/donhang");
    }
}