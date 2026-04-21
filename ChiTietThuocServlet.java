package controller.user;

import dao.ThuocDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.Thuoc;
import utils.CsrfUtil;

@WebServlet("/chi-tiet-thuoc")
public class ChiTietThuocServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");
        int idThuoc;

        try {
            idThuoc = Integer.parseInt(idStr);
            if (idThuoc <= 0) {
                throw new NumberFormatException("id <= 0");
            }
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/user/home");
            return;
        }

        HttpSession session = request.getSession(true);
        CsrfUtil.getToken(session);

        ThuocDAO thuocDAO = new ThuocDAO();
        Thuoc thuoc = thuocDAO.getThuocById(idThuoc);

        if (thuoc == null) {
            response.sendRedirect(request.getContextPath() + "/user/home");
            return;
        }

        request.setAttribute("thuoc", thuoc);
        request.getRequestDispatcher("/jsp/user/chi_tiet_thuoc.jsp").forward(request, response);
    }
}