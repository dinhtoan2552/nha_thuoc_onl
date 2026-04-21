package controller.admin;

import dao.ThuocDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Thuoc;

@WebServlet(name = "QuanLyThuocServlet", urlPatterns = {"/admin/thuoc"})
public class QuanLyThuocServlet extends HttpServlet {

    private static final int PAGE_SIZE = 10;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String keyword = request.getParameter("keyword");
        String trangThaiKho = request.getParameter("trangThaiKho");
        String pageParam = request.getParameter("page");

        int currentPage = 1;
        try {
            if (pageParam != null && !pageParam.trim().isEmpty()) {
                currentPage = Integer.parseInt(pageParam);
            }
        } catch (NumberFormatException e) {
            currentPage = 1;
        }

        if (currentPage < 1) {
            currentPage = 1;
        }

        ThuocDAO thuocDAO = new ThuocDAO();

        int totalItems = thuocDAO.countThuocTrongKho(keyword, trangThaiKho);
        int totalPages = (int) Math.ceil((double) totalItems / PAGE_SIZE);
        if (totalPages == 0) {
            totalPages = 1;
        }

        if (currentPage > totalPages) {
            currentPage = totalPages;
        }

        int offset = (currentPage - 1) * PAGE_SIZE;
        List<Thuoc> danhSachThuoc = thuocDAO.getThuocTrongKhoPhanTrang(keyword, trangThaiKho, offset, PAGE_SIZE);

        request.setAttribute("danhSachThuoc", danhSachThuoc);
        request.setAttribute("keyword", keyword == null ? "" : keyword);
        request.setAttribute("trangThaiKho", trangThaiKho == null ? "" : trangThaiKho);

        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalItems", totalItems);
        request.setAttribute("pageSize", PAGE_SIZE);

        request.getRequestDispatcher("/jsp/admin/quanly_thuoc.jsp").forward(request, response);
    }
}