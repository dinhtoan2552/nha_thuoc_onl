package controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import service.OtpService;

@WebServlet("/xac-minh-otp-quen-mat-khau")
public class XacMinhOtpQuenMatKhauServlet extends HttpServlet {

    private final OtpService otpService = new OtpService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("resetEmail") == null) {
            response.sendRedirect(request.getContextPath() + "/quen-mat-khau");
            return;
        }

        String email = String.valueOf(session.getAttribute("resetEmail"));
        String otp = request.getParameter("otp");

        OtpService.OtpResult result = otpService.xacMinhOtp(email, OtpService.MUC_DICH_QUEN_MAT_KHAU, otp);

        request.setAttribute("message", result.getMessage());
        request.setAttribute("success", result.isSuccess());

        if (result.isSuccess()) {
            session.setAttribute("otpVerifiedResetPassword", true);
            response.sendRedirect(request.getContextPath() + "/dat-lai-mat-khau");
            return;
        }

        request.getRequestDispatcher("/jsp/user/nhap-otp-quen-mat-khau.jsp").forward(request, response);
    }
}