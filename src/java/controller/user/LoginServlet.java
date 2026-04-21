package controller.user;

import dao.NguoiDungDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.NguoiDung;
import utils.CsrfUtil;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private static final int SESSION_TIMEOUT = 30 * 60;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            NguoiDung user = (NguoiDung) session.getAttribute("user");
            String vaiTro = user.getVaiTro() != null ? user.getVaiTro().trim().toUpperCase() : "";

            if ("ADMIN".equals(vaiTro)) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                return;
            } else if ("STAFF".equals(vaiTro)) {
                response.sendRedirect(request.getContextPath() + "/staff/dashboard");
                return;
            } else if ("USER".equals(vaiTro)) {
                response.sendRedirect(request.getContextPath() + "/user/home");
                return;
            }
        }

        HttpSession newSession = request.getSession(true);
        CsrfUtil.getToken(newSession);

        request.getRequestDispatcher("/jsp/user/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        if (!CsrfUtil.isValid(request)) {
            request.setAttribute("error", "Phiên làm việc không hợp lệ. Vui lòng thử lại.");
            request.getRequestDispatcher("/jsp/user/login.jsp").forward(request, response);
            return;
        }

        String email = request.getParameter("email");
        String matKhau = request.getParameter("matKhau");

        if (email != null) {
            email = email.trim().toLowerCase();
        }

        if (matKhau != null) {
            matKhau = matKhau.trim();
        }

        if (email == null || email.isEmpty() || matKhau == null || matKhau.isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ email và mật khẩu!");
            request.getRequestDispatcher("/jsp/user/login.jsp").forward(request, response);
            return;
        }

        NguoiDungDAO dao = new NguoiDungDAO();
        NguoiDung user = dao.login(email, matKhau);

        if (user != null) {
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }

            HttpSession newSession = request.getSession(true);
            newSession.setAttribute("user", user);
            newSession.setMaxInactiveInterval(SESSION_TIMEOUT);
            CsrfUtil.refreshToken(newSession);

            String vaiTro = user.getVaiTro() != null ? user.getVaiTro().trim().toUpperCase() : "";

            if ("ADMIN".equals(vaiTro)) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                return;
            } else if ("STAFF".equals(vaiTro)) {
                response.sendRedirect(request.getContextPath() + "/staff/dashboard");
                return;
            } else if ("USER".equals(vaiTro)) {
                response.sendRedirect(request.getContextPath() + "/user/home");
                return;
            } else {
                newSession.invalidate();
                request.setAttribute("error", "Tài khoản không có quyền truy cập!");
                request.getRequestDispatcher("/jsp/user/login.jsp").forward(request, response);
                return;
            }
        } else {
            request.setAttribute("error", "Email hoặc mật khẩu không đúng, hoặc tài khoản chưa hoạt động!");
            request.getRequestDispatcher("/jsp/user/login.jsp").forward(request, response);
        }
    }
}