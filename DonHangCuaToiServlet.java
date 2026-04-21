package controller.user;

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
import utils.CsrfUtil;

@WebServlet("/don-hang-cua-toi")
public class DonHangCuaToiServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        NguoiDung user = (NguoiDung) session.getAttribute("user");
        String vaiTro = user.getVaiTro() != null ? user.getVaiTro().trim().toUpperCase() : "";

        if (!"USER".equals(vaiTro)) {
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        CsrfUtil.getToken(session);

        DonHangDAO donHangDAO = new DonHangDAO();
        List<DonHang> dsDonHang = donHangDAO.getDonHangCuaNguoiDung(user.getId());

        request.setAttribute("dsDonHang", dsDonHang);
        request.getRequestDispatcher("/jsp/user/don_hang_cua_toi.jsp").forward(request, response);
    }
}