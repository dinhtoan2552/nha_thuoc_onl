package controller.admin;

import dao.DanhMucDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.DanhMuc;

@WebServlet(name = "AdminDanhMucServlet", urlPatterns = {"/admin/danhmuc"})
public class AdminDanhMucServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        DanhMucDAO dao = new DanhMucDAO();
        List<DanhMuc> danhSachDanhMuc = dao.getAllDanhMuc();

        request.setAttribute("danhSachDanhMuc", danhSachDanhMuc);
        request.getRequestDispatcher("/jsp/admin/quanly_danhmuc.jsp").forward(request, response);
    }
}