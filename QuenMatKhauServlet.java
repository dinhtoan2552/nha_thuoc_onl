package controller.user;

import dao.NguoiDungDAO;
import dao.QuenMatKhauOtpDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;
import utils.EmailUtil;

@WebServlet("/quen-mat-khau")
public class QuenMatKhauServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/user/quen_mat_khau.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String email = request.getParameter("email");

        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập email.");
            request.getRequestDispatcher("/jsp/user/quen_mat_khau.jsp").forward(request, response);
            return;
        }

        email = email.trim().toLowerCase();

        NguoiDungDAO nguoiDungDAO = new NguoiDungDAO();
        if (!nguoiDungDAO.emailTonTai(email)) {
            request.setAttribute("error", "Email không tồn tại trong hệ thống.");
            request.getRequestDispatcher("/jsp/user/quen_mat_khau.jsp").forward(request, response);
            return;
        }

        QuenMatKhauOtpDAO otpDAO = new QuenMatKhauOtpDAO();

        if (otpDAO.emailDangChoResend(email)) {
            request.setAttribute("error", "Bạn vừa yêu cầu mã. Vui lòng đợi một chút rồi thử lại.");
            request.getRequestDispatcher("/jsp/user/quen_mat_khau.jsp").forward(request, response);
            return;
        }

        String otp = String.format("%06d", new Random().nextInt(1000000));
        LocalDateTime now = LocalDateTime.now();

        otpDAO.xoaOtpCuTheoEmail(email);

        boolean saved = otpDAO.taoOtp(
                email,
                otp,
                now.plusMinutes(5),
                now.plusSeconds(60)
        );

        if (!saved) {
            request.setAttribute("error", "Không thể tạo mã xác nhận.");
            request.getRequestDispatcher("/jsp/user/quen_mat_khau.jsp").forward(request, response);
            return;
        }

        boolean sent = EmailUtil.guiOtpQuenMatKhau(email, otp);
        if (!sent) {
            request.setAttribute("error", "Không gửi được email. Vui lòng kiểm tra cấu hình gửi mail.");
            request.getRequestDispatcher("/jsp/user/quen_mat_khau.jsp").forward(request, response);
            return;
        }

        request.getSession().setAttribute("reset_email", email);
        response.sendRedirect(request.getContextPath() + "/xac-nhan-otp-quen-mat-khau");
    }
}