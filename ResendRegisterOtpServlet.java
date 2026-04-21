package controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import utils.EmailUtil;
import utils.OtpUtil;

@WebServlet(name = "ResendRegisterOtpServlet", urlPatterns = {"/resend-register-otp"})
public class ResendRegisterOtpServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("register_email") == null) {
            response.sendRedirect(request.getContextPath() + "/register");
            return;
        }

        Long resendTime = (Long) session.getAttribute("register_otp_resend");
        if (resendTime != null && System.currentTimeMillis() < resendTime) {
            request.setAttribute("error", "Bạn vừa gửi mã OTP. Vui lòng chờ 60 giây rồi thử lại.");
            request.getRequestDispatcher("/jsp/user/verify_register_otp.jsp").forward(request, response);
            return;
        }

        String email = (String) session.getAttribute("register_email");
        String newOtp = OtpUtil.generate6DigitOtp();

        session.setAttribute("register_otp", newOtp);
        session.setAttribute("register_otp_expire", System.currentTimeMillis() + 5 * 60 * 1000L);
        session.setAttribute("register_otp_resend", System.currentTimeMillis() + 60 * 1000L);

        try {
            EmailUtil.sendRegisterOtp(email, newOtp);
            request.setAttribute("success", "Đã gửi lại mã OTP.");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Không thể gửi lại mã OTP.");
        }

        request.getRequestDispatcher("/jsp/user/verify_register_otp.jsp").forward(request, response);
    }
}