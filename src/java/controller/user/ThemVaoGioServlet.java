package controller.user;

import dao.GioHangDAO;
import dao.ThuocDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import model.NguoiDung;
import model.Thuoc;
import utils.CsrfUtil;

@WebServlet("/them-vao-gio")
public class ThemVaoGioServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        if (!CsrfUtil.isValid(request)) {
            response.sendError(403);
            return;
        }

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }

        NguoiDung user = (NguoiDung) session.getAttribute("user");

        String idStr = request.getParameter("thuocId");
        String slStr = request.getParameter("soLuong");

        int id, sl;

        try {
            id = Integer.parseInt(idStr);
            sl = Integer.parseInt(slStr);
        } catch (Exception e) {
            response.sendRedirect("user/home");
            return;
        }

        if (sl <= 0) sl = 1;

        ThuocDAO thuocDAO = new ThuocDAO();
        Thuoc t = thuocDAO.getThuocById(id);

        if (t == null || t.getSoLuong() <= 0) {
            response.sendRedirect("user/home");
            return;
        }

        if (sl > t.getSoLuong()) sl = t.getSoLuong();

        GioHangDAO dao = new GioHangDAO();
        int gid = dao.getOrCreateGioHang(user.getId());

        boolean ok;
        if (dao.daTonTaiTrongGio(gid, id)) {
            ok = dao.capNhatSoLuong(gid, id, sl);
        } else {
            ok = dao.themMoiVaoGio(gid, id, sl, t.getDonGia());
        }

        response.sendRedirect(ok ? "cart" : "user/home");
    }
}