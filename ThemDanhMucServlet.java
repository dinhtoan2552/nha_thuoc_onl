package controller.admin;

import dao.DanhMucDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.DanhMuc;

@WebServlet(name = "ThemDanhMucServlet", urlPatterns = {"/admin/danhmuc/them"})
public class ThemDanhMucServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/admin/them_danhmuc.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String tenDanhMuc = request.getParameter("tenDanhMuc");
        String moTa = request.getParameter("moTa");

        DanhMuc dm = new DanhMuc();
        dm.setTenDanhMuc(tenDanhMuc);
        dm.setMoTa(moTa);

        DanhMucDAO dao = new DanhMucDAO();
        dao.insertDanhMuc(dm);

        response.sendRedirect(request.getContextPath() + "/admin/danhmuc");
    }
}