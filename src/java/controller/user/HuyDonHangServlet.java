package controller.user;

import dao.DonHangDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.NguoiDung;
import utils.CsrfUtil;

@WebServlet("/huy-don-hang")
public class HuyDonHangServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        if (!CsrfUtil.isValid(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "CSRF không hợp lệ");
            return;
        }

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

        String idDonHangStr = request.getParameter("idDonHang");
        int idDonHang;

        try {
            idDonHang = Integer.parseInt(idDonHangStr);
            if (idDonHang <= 0) {
                throw new NumberFormatException("idDonHang <= 0");
            }
        } catch (Exception e) {
            session.setAttribute("cartMessage", "Mã đơn hàng không hợp lệ.");
            response.sendRedirect(request.getContextPath() + "/don-hang-cua-toi");
            return;
        }

        DonHangDAO donHangDAO = new DonHangDAO();
        boolean success = donHangDAO.huyDonHangBoiKhach(idDonHang, user.getId());

        if (success) {
            session.setAttribute("cartMessage", "Hủy đơn hàng thành công.");
        } else {
            session.setAttribute("cartMessage", "Không thể hủy đơn. Đơn đã quá 8 tiếng hoặc không còn hợp lệ để hủy.");
        }

        response.sendRedirect(request.getContextPath() + "/don-hang-cua-toi");
    }
}