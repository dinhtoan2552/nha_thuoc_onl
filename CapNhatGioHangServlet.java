package controller.user;

import dao.GioHangDAO;
import dao.ThuocDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.NguoiDung;
import model.Thuoc;
import utils.CsrfUtil;

@WebServlet("/cap-nhat-gio-hang")
public class CapNhatGioHangServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // 🔐 CSRF
        if (!CsrfUtil.isValid(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "CSRF không hợp lệ");
            return;
        }

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Object obj = session.getAttribute("user");
        if (!(obj instanceof NguoiDung)) {
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        NguoiDung user = (NguoiDung) obj;

        String vaiTro = user.getVaiTro() != null ? user.getVaiTro().toUpperCase() : "";
        String trangThai = user.getTrangThai() != null ? user.getTrangThai().toUpperCase() : "";

        if (!"USER".equals(vaiTro) || !"HOAT_DONG".equals(trangThai)) {
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String idThuocStr = request.getParameter("idThuoc");
        String soLuongStr = request.getParameter("soLuong");

        int idThuoc;
        int soLuong;

        try {
            idThuoc = Integer.parseInt(idThuocStr);
            soLuong = Integer.parseInt(soLuongStr);

            if (idThuoc <= 0) throw new Exception();
        } catch (Exception e) {
            session.setAttribute("cartMessage", "Dữ liệu không hợp lệ.");
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        if (soLuong < 1) soLuong = 1;

        ThuocDAO thuocDAO = new ThuocDAO();
        Thuoc thuoc = thuocDAO.getThuocById(idThuoc);

        if (thuoc == null) {
            session.setAttribute("cartMessage", "Không tìm thấy sản phẩm.");
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        if (thuoc.getSoLuong() <= 0) {
            session.setAttribute("cartMessage", "Sản phẩm đã hết hàng.");
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        if (soLuong > thuoc.getSoLuong()) {
            soLuong = thuoc.getSoLuong();
        }

        GioHangDAO dao = new GioHangDAO();
        int idGioHang = dao.getOrCreateGioHang(user.getId());

        boolean ok = dao.capNhatSoLuongTuyetDoi(idGioHang, idThuoc, soLuong);

        session.setAttribute("cartMessage", ok ? "Cập nhật thành công." : "Cập nhật thất bại.");

        response.sendRedirect(request.getContextPath() + "/cart");
    }
}