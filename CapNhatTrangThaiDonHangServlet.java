package controller.admin;

import dao.DonHangDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.NguoiDung;

@WebServlet(name = "CapNhatTrangThaiDonHangServlet", urlPatterns = {"/admin/donhang/capnhat-trangthai"})
public class CapNhatTrangThaiDonHangServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            int idDonHang = Integer.parseInt(request.getParameter("idDonHang"));
            String trangThai = request.getParameter("trangThai");

            Integer nguoiXuLy = null;

            HttpSession session = request.getSession(false);
            if (session != null) {
                Object adminObj = session.getAttribute("admin");
                if (adminObj instanceof NguoiDung) {
                    nguoiXuLy = ((NguoiDung) adminObj).getId();
                }
            }

            DonHangDAO dao = new DonHangDAO();
            dao.updateTrangThaiDonHang(idDonHang, trangThai, nguoiXuLy);

        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect(request.getContextPath() + "/admin/donhang");
    }
}