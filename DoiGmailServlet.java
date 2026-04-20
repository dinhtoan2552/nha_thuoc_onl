package controller.user;

import dao.NguoiDungDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.NguoiDung;
import utils.CsrfUtil;
import utils.EmailUtil;
import utils.InputSanitizer;
import utils.OtpUtil;

@WebServlet("/doi-gmail")
public class DoiGmailServlet extends HttpServlet {

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
        request.getRequestDispatcher("/jsp/user/doi_gmail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        if (!CsrfUtil.isValid(request)) {
            request.setAttribute("error", "Phiên làm việc không hợp lệ. Vui lòng thử lại.");
            request.getRequestDispatcher("/jsp/user/doi_gmail.jsp").forward(request, response);
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

        String emailMoi = InputSanitizer.normalizeEmail(request.getParameter("emailMoi"));

        if (emailMoi == null || emailMoi.isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập gmail mới.");
            request.getRequestDispatcher("/jsp/user/doi_gmail.jsp").forward(request, response);
            return;
        }

        if (InputSanitizer.containsHtmlRisk(emailMoi)) {
            request.setAttribute("error", "Gmail không hợp lệ.");
            request.getRequestDispatcher("/jsp/user/doi_gmail.jsp").forward(request, response);
            return;
        }

        String emailHienTai = user.getEmail() == null ? "" : user.getEmail().trim().toLowerCase();

        if (emailMoi.equalsIgnoreCase(emailHienTai)) {
            request.setAttribute("error", "Gmail mới không được trùng gmail hiện tại.");
            request.getRequestDispatcher("/jsp/user/doi_gmail.jsp").forward(request, response);
            return;
        }

        NguoiDungDAO dao = new NguoiDungDAO();
        if (dao.emailDaTonTai(emailMoi)) {
            request.setAttribute("error", "Gmail này đã được sử dụng.");
            request.getRequestDispatcher("/jsp/user/doi_gmail.jsp").forward(request, response);
            return;
        }

        String otp = OtpUtil.generate6DigitOtp();

        session.setAttribute("account_action_type", "CHANGE_EMAIL");
        session.setAttribute("account_action_otp", otp);
        session.setAttribute("account_action_otp_expire", System.currentTimeMillis() + 5 * 60 * 1000L);
        session.setAttribute("account_new_email", emailMoi);

        try {
            EmailUtil.sendChangeEmailOtp(user.getEmail(), otp, emailMoi);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Không gửi được mã OTP về email hiện tại. Vui lòng thử lại.");
            request.getRequestDispatcher("/jsp/user/doi_gmail.jsp").forward(request, response);
            return;
        }

        session.setAttribute("successMessage", "Mã OTP đã được gửi về gmail hiện tại để xác nhận đổi gmail.");
        response.sendRedirect(request.getContextPath() + "/xac-nhan-otp-tai-khoan");
    }
}