package controller.user;

import dao.GioHangDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.NguoiDung;

@WebServlet("/xoa-khoi-gio")
public class XoaKhoiGioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        NguoiDung user = (NguoiDung) session.getAttribute("user");

        String idThuocStr = request.getParameter("idThuoc");
        int idThuoc;

        try {
            idThuoc = Integer.parseInt(idThuocStr);
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        GioHangDAO gioHangDAO = new GioHangDAO();
        int idGioHang = gioHangDAO.getOrCreateGioHang(user.getId());

        if (idGioHang != -1) {
            gioHangDAO.xoaKhoiGio(idGioHang, idThuoc);
        }

        response.sendRedirect(request.getContextPath() + "/cart");
    }
}