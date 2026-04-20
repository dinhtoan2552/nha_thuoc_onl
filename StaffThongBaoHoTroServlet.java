package controller.staff;

import dao.HoTroDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.NguoiDung;

@WebServlet("/staff/thong-bao-ho-tro")
public class StaffThongBaoHoTroServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.getWriter().write("{\"success\":false,\"message\":\"Chưa đăng nhập\"}");
            return;
        }

        NguoiDung user = (NguoiDung) session.getAttribute("user");
        if (user == null || user.getVaiTro() == null || !"STAFF".equalsIgnoreCase(user.getVaiTro())) {
            response.getWriter().write("{\"success\":false,\"message\":\"Không có quyền\"}");
            return;
        }

        HoTroDAO hoTroDAO = new HoTroDAO();

        int soPhienDangMo = hoTroDAO.countPhienDangMo();
        int soHoTroMoi = hoTroDAO.countPhienCoTinNhanMoiTuKhach();

response.getWriter().write(
    "{\"success\":true,\"soPhienDangMo\":" + soPhienDangMo + ",\"soHoTroMoi\":" + soHoTroMoi + "}"
);
    }
}