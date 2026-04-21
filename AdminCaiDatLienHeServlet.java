package controller.admin;

import dao.ThongTinLienHeDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import model.ThongTinLienHe;

@WebServlet("/admin/lien-he")
public class AdminCaiDatLienHeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ThongTinLienHeDAO dao = new ThongTinLienHeDAO();
        ThongTinLienHe thongTinLienHe = dao.getThongTinLienHe();

        request.setAttribute("thongTinLienHe", thongTinLienHe);
        request.getRequestDispatcher("/jsp/admin/cai_dat_lien_he.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String soZalo = safeTrim(request.getParameter("soZalo"));
        String linkMessenger = safeTrim(request.getParameter("linkMessenger"));
        String soHotline = safeTrim(request.getParameter("soHotline"));
        String diaChiNhaThuoc = safeTrim(request.getParameter("diaChiNhaThuoc"));
        String noiDungBanner = safeTrim(request.getParameter("noiDungBanner"));

        ThongTinLienHe t = new ThongTinLienHe();
        t.setSoZalo(soZalo);
        t.setLinkMessenger(linkMessenger);
        t.setSoHotline(soHotline);
        t.setDiaChiNhaThuoc(diaChiNhaThuoc);
        t.setNoiDungBanner(noiDungBanner);

        ThongTinLienHeDAO dao = new ThongTinLienHeDAO();
        boolean success = dao.saveOrUpdate(t);

        if (success) {
            request.getSession().setAttribute("adminMessage", "Cập nhật thông tin liên hệ thành công.");
        } else {
            request.getSession().setAttribute("adminMessage", "Cập nhật thất bại.");
        }

        response.sendRedirect(request.getContextPath() + "/admin/lien-he");
    }

    private String safeTrim(String value) {
        return value == null ? "" : value.trim();
    }
}