package controller.admin;

import dao.ThuocDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "XoaThuocServlet", urlPatterns = {"/admin/thuoc/xoa"})
public class XoaThuocServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int idThuoc = 0;
        try {
            idThuoc = Integer.parseInt(request.getParameter("id"));
        } catch (Exception e) {
            idThuoc = 0;
        }

        ThuocDAO dao = new ThuocDAO();
        dao.deleteThuoc(idThuoc);

        response.sendRedirect(request.getContextPath() + "/admin/thuoc");
    }
}