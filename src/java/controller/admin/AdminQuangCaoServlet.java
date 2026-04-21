package controller.admin;

import dao.QuangCaoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.util.List;
import model.QuangCao;
import utils.FileUploadUtil;

@WebServlet("/admin/quangcao")
@MultipartConfig
public class AdminQuangCaoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        QuangCaoDAO dao = new QuangCaoDAO();
        List<QuangCao> dsQuangCao = dao.getTatCaQuangCao();

        request.setAttribute("dsQuangCao", dsQuangCao);
        request.getRequestDispatcher("/jsp/admin/quangcao.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        QuangCaoDAO dao = new QuangCaoDAO();

        try {
            if ("them".equals(action)) {
                String tieuDe = request.getParameter("tieuDe");
                String linkUrl = request.getParameter("linkUrl");
                String thuTuStr = request.getParameter("thuTu");
                String trangThaiStr = request.getParameter("trangThai");

                int thuTu = 0;
                try {
                    thuTu = Integer.parseInt(thuTuStr);
                } catch (Exception e) {
                    thuTu = 0;
                }

                boolean trangThai = "1".equals(trangThaiStr);

                Part filePart = request.getPart("hinhAnh");
                String fileName = FileUploadUtil.saveImage(filePart);

                if (fileName != null) {
                    dao.themQuangCao(tieuDe, fileName, linkUrl, thuTu, trangThai);
                }
            } else if ("sua".equals(action)) {
                int idQuangCao = Integer.parseInt(request.getParameter("idQuangCao"));
                String tieuDe = request.getParameter("tieuDe");
                String linkUrl = request.getParameter("linkUrl");
                String thuTuStr = request.getParameter("thuTu");
                String trangThaiStr = request.getParameter("trangThai");

                int thuTu = 0;
                try {
                    thuTu = Integer.parseInt(thuTuStr);
                } catch (Exception e) {
                    thuTu = 0;
                }

                boolean trangThai = "1".equals(trangThaiStr);

                dao.capNhatQuangCao(idQuangCao, tieuDe, linkUrl, thuTu, trangThai);

                Part filePart = request.getPart("hinhAnh");
                if (filePart != null && filePart.getSize() > 0) {
                    String fileName = FileUploadUtil.saveImage(filePart);
                    if (fileName != null) {
                        dao.capNhatAnhQuangCao(idQuangCao, fileName);
                    }
                }
            } else if ("xoa".equals(action)) {
                int idQuangCao = Integer.parseInt(request.getParameter("idQuangCao"));
                dao.xoaQuangCao(idQuangCao);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect(request.getContextPath() + "/admin/quangcao");
    }
}