package controller.admin;

import dao.DonHangDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.DonHang;

@WebServlet(name = "ChiTietDonHangServlet", urlPatterns = {"/admin/donhang/chitiet"})
public class ChiTietDonHangServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int idDonHang = Integer.parseInt(request.getParameter("id"));

            DonHangDAO dao = new DonHangDAO();
            DonHang donHang = dao.getDonHangById(idDonHang);

            if (donHang == null) {
                response.sendRedirect(request.getContextPath() + "/admin/donhang");
                return;
            }

            request.setAttribute("donHang", donHang);
            request.getRequestDispatcher("/jsp/admin/chitiet_donhang.jsp").forward(request, response);

        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/admin/donhang");
        }
    }
}