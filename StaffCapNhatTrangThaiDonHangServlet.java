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

@WebServlet("/staff/cap-nhat-trang-thai-don-hang")
public class StaffCapNhatTrangThaiDonHangServlet extends HttpServlet {

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
        String trangThaiMoi = request.getParameter("trangThaiMoi");

        int idDonHang;
        try {
            idDonHang = Integer.parseInt(idDonHangStr);
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/staff/donhang");
            return;
        }

        if (trangThaiMoi == null || trangThaiMoi.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/staff/donhang");
            return;
        }

        DonHangDAO donHangDAO = new DonHangDAO();
        boolean success = donHangDAO.updateTrangThaiDonHang(idDonHang, trangThaiMoi, staff.getId());

        if (success) {
            session.setAttribute("staffOrderMessage", "Cập nhật trạng thái đơn hàng thành công.");
        } else {
            session.setAttribute("staffOrderMessage", "Cập nhật trạng thái thất bại.");
        }

        response.sendRedirect(request.getContextPath() + "/staff/donhang");
    }
}