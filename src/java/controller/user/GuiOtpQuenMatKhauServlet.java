package controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import service.OtpService;

@WebServlet("/gui-otp-quen-mat-khau")
public class GuiOtpQuenMatKhauServlet extends HttpServlet {

    private final OtpService otpService = new OtpService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String email = request.getParameter("email");

        OtpService.OtpResult result = otpService.guiOtp(email, OtpService.MUC_DICH_QUEN_MAT_KHAU);

        request.setAttribute("message", result.getMessage());
        request.setAttribute("success", result.isSuccess());

        if (result.isSuccess()) {
            HttpSession session = request.getSession();
            session.setAttribute("resetEmail", email);
        }

        request.getRequestDispatcher("/jsp/user/quen-mat-khau.jsp").forward(request, response);
    }
}