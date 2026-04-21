package controller.admin;

import dao.DanhMucDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.DanhMuc;

@WebServlet(name = "SuaDanhMucServlet", urlPatterns = {"/admin/danhmuc/sua"})
public class SuaDanhMucServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(request.getParameter("id"));

            DanhMucDAO dao = new DanhMucDAO();
            DanhMuc dm = dao.getDanhMucById(id);

            request.setAttribute("danhMuc", dm);
            request.getRequestDispatcher("/jsp/admin/sua_danhmuc.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/admin/danhmuc");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            int idDanhMuc = Integer.parseInt(request.getParameter("idDanhMuc"));
            String tenDanhMuc = request.getParameter("tenDanhMuc");
            String moTa = request.getParameter("moTa");

            DanhMuc dm = new DanhMuc();
            dm.setIdDanhMuc(idDanhMuc);
            dm.setTenDanhMuc(tenDanhMuc);
            dm.setMoTa(moTa);

            DanhMucDAO dao = new DanhMucDAO();
            dao.updateDanhMuc(dm);
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect(request.getContextPath() + "/admin/danhmuc");
    }
}