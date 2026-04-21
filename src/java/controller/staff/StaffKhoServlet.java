package controller.staff;

import dao.ThuocDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import model.Thuoc;

@WebServlet("/staff/kho")
public class StaffKhoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String keyword = request.getParameter("keyword");
        String trangThaiKho = request.getParameter("trangThaiKho");

        if (keyword == null) keyword = "";
        if (trangThaiKho == null) trangThaiKho = "";

        ThuocDAO thuocDAO = new ThuocDAO();

        List<Thuoc> listThuoc = thuocDAO.getThuocTrongKho(keyword, trangThaiKho);

        int tongThuoc = thuocDAO.countTatCaThuoc();
        int sapHet = thuocDAO.countThuocSapHet(10);
        int hetHang = thuocDAO.countThuocHetHang();

        request.setAttribute("listThuoc", listThuoc);
        request.setAttribute("keyword", keyword);
        request.setAttribute("trangThaiKho", trangThaiKho);

        request.setAttribute("tongThuoc", tongThuoc);
        request.setAttribute("sapHet", sapHet);
        request.setAttribute("hetHang", hetHang);

        request.getRequestDispatcher("/jsp/staff/kho.jsp").forward(request, response);
    }
}