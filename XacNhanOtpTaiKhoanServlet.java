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
import utils.PasswordUtil;
import utils.InputSanitizer;

@WebServlet("/xac-nhan-otp-tai-khoan")
public class XacNhanOtpTaiKhoanServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String actionType = (String) session.getAttribute("account_action_type");
        if (actionType == null) {
            response.sendRedirect(request.getContextPath() + "/user/home");
            return;
        }

        CsrfUtil.getToken(session);
        request.getRequestDispatcher("/jsp/user/xac_nhan_otp_tai_khoan.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        if (!CsrfUtil.isValid(request)) {
            request.setAttribute("error", "Phiên làm việc không hợp lệ. Vui lòng thử lại.");
            request.getRequestDispatcher("/jsp/user/xac_nhan_otp_tai_khoan.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        NguoiDung user = (NguoiDung) session.getAttribute("user");
        String otpNhap = request.getParameter("otp");
        String otpHeThong = (String) session.getAttribute("account_action_otp");
        Long otpExpire = (Long) session.getAttribute("account_action_otp_expire");
        String actionType = (String) session.getAttribute("account_action_type");

        if (actionType == null) {
            response.sendRedirect(request.getContextPath() + "/user/home");
            return;
        }

        if (otpNhap != null) {
            otpNhap = otpNhap.trim();
        }

        if (otpNhap == null || otpNhap.isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập mã OTP.");
            request.getRequestDispatcher("/jsp/user/xac_nhan_otp_tai_khoan.jsp").forward(request, response);
            return;
        }

        if (otpExpire == null || System.currentTimeMillis() > otpExpire) {
            request.setAttribute("error", "Mã OTP đã hết hạn. Vui lòng thực hiện lại thao tác.");
            request.getRequestDispatcher("/jsp/user/xac_nhan_otp_tai_khoan.jsp").forward(request, response);
            return;
        }

        if (otpHeThong == null || !otpHeThong.equals(otpNhap)) {
            request.setAttribute("error", "Mã OTP không đúng.");
            request.getRequestDispatcher("/jsp/user/xac_nhan_otp_tai_khoan.jsp").forward(request, response);
            return;
        }

        NguoiDungDAO dao = new NguoiDungDAO();

        if ("CHANGE_PASSWORD".equals(actionType)) {
            String matKhauMoiDaHash = (String) session.getAttribute("account_new_password");

            if (matKhauMoiDaHash == null || matKhauMoiDaHash.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/doi-mat-khau");
                return;
            }

            if (!PasswordUtil.isBcryptHash(matKhauMoiDaHash)) {
                request.setAttribute("error", "Thông tin bảo mật không hợp lệ. Vui lòng thực hiện lại.");
                request.getRequestDispatcher("/jsp/user/xac_nhan_otp_tai_khoan.jsp").forward(request, response);
                return;
            }

            boolean ok = dao.doiMatKhau(user.getId(), matKhauMoiDaHash);

            if (!ok) {
                request.setAttribute("error", "Đổi mật khẩu thất bại. Vui lòng thử lại.");
                request.getRequestDispatcher("/jsp/user/xac_nhan_otp_tai_khoan.jsp").forward(request, response);
                return;
            }

            user.setMatKhau(matKhauMoiDaHash);
            session.setAttribute("user", user);

            clearAccountOtpSession(session);
            session.setAttribute("successMessage", "Đổi mật khẩu thành công.");
            response.sendRedirect(request.getContextPath() + "/doi-mat-khau");
            return;
        }

        if ("CHANGE_EMAIL".equals(actionType)) {
            String emailMoi = (String) session.getAttribute("account_new_email");

            if (emailMoi == null || emailMoi.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/doi-gmail");
                return;
            }

            emailMoi = InputSanitizer.normalizeEmail(emailMoi);

            if (dao.emailDaTonTaiNgoaiTruId(emailMoi, user.getId())) {
                request.setAttribute("error", "Gmail mới đã được sử dụng.");
                request.getRequestDispatcher("/jsp/user/xac_nhan_otp_tai_khoan.jsp").forward(request, response);
                return;
            }

            boolean ok = dao.doiEmail(user.getId(), emailMoi);

            if (!ok) {
                request.setAttribute("error", "Đổi gmail thất bại. Vui lòng thử lại.");
                request.getRequestDispatcher("/jsp/user/xac_nhan_otp_tai_khoan.jsp").forward(request, response);
                return;
            }

            String oldEmail = user.getEmail();
            user.setEmail(emailMoi);
            session.setAttribute("user", user);

            clearAccountOtpSession(session);

            try {
                EmailUtil.sendEmailChangedNotice(emailMoi);
                if (oldEmail != null && !oldEmail.trim().isEmpty() && !oldEmail.equalsIgnoreCase(emailMoi)) {
                    EmailUtil.sendEmailChangedNotice(oldEmail);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            session.setAttribute("successMessage", "Đổi gmail thành công.");
            response.sendRedirect(request.getContextPath() + "/doi-gmail");
            return;
        }

        response.sendRedirect(request.getContextPath() + "/user/home");
    }

    private void clearAccountOtpSession(HttpSession session) {
        session.removeAttribute("account_action_type");
        session.removeAttribute("account_action_otp");
        session.removeAttribute("account_action_otp_expire");
        session.removeAttribute("account_new_password");
        session.removeAttribute("account_new_email");
    }
}