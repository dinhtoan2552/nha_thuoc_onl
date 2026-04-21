package controller.user;

import dao.QuenMatKhauOtpDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/xac-nhan-otp-quen-mat-khau")
public class XacNhanOtpQuenMatKhauServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("reset_email") == null) {
            response.sendRedirect(request.getContextPath() + "/quen-mat-khau");
            return;
        }

        request.getRequestDispatcher("/jsp/user/xac_nhan_otp_quen_mat_khau.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("reset_email") == null) {
            response.sendRedirect(request.getContextPath() + "/quen-mat-khau");
            return;
        }

        String email = String.valueOf(session.getAttribute("reset_email"));
        String otp = request.getParameter("otp");

        if (otp == null || otp.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập mã OTP.");
            request.getRequestDispatcher("/jsp/user/xac_nhan_otp_quen_mat_khau.jsp").forward(request, response);
            return;
        }

        QuenMatKhauOtpDAO otpDAO = new QuenMatKhauOtpDAO();
        boolean ok = otpDAO.xacThucOtp(email, otp.trim());

        if (!ok) {
            request.setAttribute("error", "Mã OTP không đúng, đã hết hạn hoặc đã bị khóa.");
            request.getRequestDispatcher("/jsp/user/xac_nhan_otp_quen_mat_khau.jsp").forward(request, response);
            return;
        }

        session.setAttribute("reset_verified", true);
        response.sendRedirect(request.getContextPath() + "/dat-lai-mat-khau");
    }
}