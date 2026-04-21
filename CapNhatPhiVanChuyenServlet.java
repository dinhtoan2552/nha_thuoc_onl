package controller.admin;

import dao.DonHangDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.NguoiDung;

@WebServlet("/admin/donhang/cap-nhat-ship")
public class CapNhatPhiVanChuyenServlet extends HttpServlet {

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

        int idDonHang = 0;
        double phiVanChuyen = 0;

        try {
            idDonHang = Integer.parseInt(request.getParameter("idDonHang"));
            phiVanChuyen = Double.parseDouble(request.getParameter("phiVanChuyen"));
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/admin/donhang");
            return;
        }

        if (phiVanChuyen < 0) {
            phiVanChuyen = 0;
        }

        DonHangDAO donHangDAO = new DonHangDAO();
        donHangDAO.capNhatPhiVanChuyen(idDonHang, phiVanChuyen, user.getId());

        response.sendRedirect(request.getContextPath() + "/admin/donhang/chitiet?id=" + idDonHang);
    }
}