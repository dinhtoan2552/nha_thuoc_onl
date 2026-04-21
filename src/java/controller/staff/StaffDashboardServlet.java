package controller.staff;

import dao.DonHangDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import model.DonHang;
import model.NguoiDung;

@WebServlet("/staff/dashboard")
public class StaffDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        NguoiDung user = (NguoiDung) session.getAttribute("user");
        if (user == null || user.getVaiTro() == null || !"STAFF".equalsIgnoreCase(user.getVaiTro())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        DonHangDAO donHangDAO = new DonHangDAO();

        int tongDonHang = donHangDAO.countAllDonHang();
        int choXacNhan = donHangDAO.countDonHangTheoTrangThai("CHO_XAC_NHAN");
        int dangGiao = donHangDAO.countDonHangTheoTrangThai("DANG_GIAO");
        int hoanThanh = donHangDAO.countDonHangTheoTrangThai("HOAN_THANH");
        int daHuy = donHangDAO.countDonHangTheoTrangThai("DA_HUY");

        List<DonHang> donHangMoi = donHangDAO.getAllDonHang(null, null);

        request.setAttribute("tongDonHang", tongDonHang);
        request.setAttribute("choXacNhan", choXacNhan);
        request.setAttribute("dangGiao", dangGiao);
        request.setAttribute("hoanThanh", hoanThanh);
        request.setAttribute("daHuy", daHuy);
        request.setAttribute("donHangMoi", donHangMoi);

        request.getRequestDispatcher("/jsp/staff/dashboard.jsp").forward(request, response);
    }
}