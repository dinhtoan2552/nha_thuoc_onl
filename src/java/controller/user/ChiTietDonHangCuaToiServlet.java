package controller.user;

import dao.DonHangDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.DonHang;
import model.NguoiDung;

@WebServlet("/chi-tiet-don-hang-cua-toi")
public class ChiTietDonHangCuaToiServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        NguoiDung user = (NguoiDung) session.getAttribute("user");

        String idDonHangStr = request.getParameter("id");
        int idDonHang;

        try {
            idDonHang = Integer.parseInt(idDonHangStr);
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/don-hang-cua-toi");
            return;
        }

        DonHangDAO donHangDAO = new DonHangDAO();
        DonHang donHang = donHangDAO.getDonHangCuaNguoiDungById(idDonHang, user.getId());

        if (donHang == null) {
            response.sendRedirect(request.getContextPath() + "/don-hang-cua-toi");
            return;
        }

        request.setAttribute("donHang", donHang);
        request.getRequestDispatcher("/jsp/user/chi_tiet_don_hang_cua_toi.jsp").forward(request, response);
    }
}