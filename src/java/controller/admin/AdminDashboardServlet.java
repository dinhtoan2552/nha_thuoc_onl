package controller.admin;

import dao.DashboardDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ThongKeNgay;

@WebServlet(name = "AdminDashboardServlet", urlPatterns = {"/admin/dashboard"})
public class AdminDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        DashboardDAO dao = new DashboardDAO();

        request.setAttribute("tongThuoc", dao.countThuoc());
        request.setAttribute("tongDonHang", dao.countDonHang());
        request.setAttribute("tongNguoiDung", dao.countNguoiDung());
        request.setAttribute("tongNhanVien", dao.countNhanVien());
        request.setAttribute("tongDoanhThu", dao.tongDoanhThu());
        request.setAttribute("tongVonNhapHang", dao.tongVonNhapHang());

        request.setAttribute("donChoXacNhan", dao.countDonChoXacNhan());
        request.setAttribute("thuocSapHet", dao.countThuocSapHet(10));
        request.setAttribute("thuocHetHang", dao.countThuocHetHang());
        request.setAttribute("donDaHuy", dao.countDonDaHuy());

        List<ThongKeNgay> doanhThuTheoThang = dao.getDoanhThuTheo6ThangGanNhat();
        request.setAttribute("doanhThuTheoThang", doanhThuTheoThang);

        request.getRequestDispatcher("/jsp/admin/dashboard.jsp").forward(request, response);
    }
}