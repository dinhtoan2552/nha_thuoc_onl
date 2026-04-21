package controller.staff;

import dao.DonHangDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import model.DonHang;

@WebServlet("/staff/chi-tiet-don-hang")
public class StaffChiTietDonHangServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idDonHangStr = request.getParameter("id");
        int idDonHang;

        try {
            idDonHang = Integer.parseInt(idDonHangStr);
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/staff/donhang");
            return;
        }

        DonHangDAO donHangDAO = new DonHangDAO();
        DonHang donHang = donHangDAO.getDonHangById(idDonHang);

        if (donHang == null) {
            response.sendRedirect(request.getContextPath() + "/staff/donhang");
            return;
        }

        request.setAttribute("donHang", donHang);
        request.getRequestDispatcher("/jsp/staff/chitiet_donhang.jsp").forward(request, response);
    }
}