package controller.user;

import dao.GioHangDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import model.GioHangItem;
import model.NguoiDung;
import utils.CsrfUtil;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Object sessionUser = session.getAttribute("user");
        if (!(sessionUser instanceof NguoiDung)) {
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        NguoiDung user = (NguoiDung) sessionUser;
        String vaiTro = user.getVaiTro() != null ? user.getVaiTro().trim().toUpperCase() : "";
        String trangThai = user.getTrangThai() != null ? user.getTrangThai().trim().toUpperCase() : "";

        if (!"USER".equals(vaiTro) || !"HOAT_DONG".equals(trangThai)) {
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        CsrfUtil.getToken(session);

        GioHangDAO gioHangDAO = new GioHangDAO();
        List<GioHangItem> dsGioHang = gioHangDAO.getDanhSachGioHang(user.getId());
        double tongTien = gioHangDAO.tinhTongTienGioHang(user.getId());

        request.setAttribute("dsGioHang", dsGioHang);
        request.setAttribute("tongTien", tongTien);

        request.getRequestDispatcher("/jsp/user/cart.jsp").forward(request, response);
    }
}