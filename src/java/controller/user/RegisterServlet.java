package controller.user;

import dao.NguoiDungDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import utils.CsrfUtil;
import utils.EmailUtil;
import utils.OtpUtil;
import utils.PasswordUtil;
import utils.InputSanitizer;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        CsrfUtil.getToken(session);
        request.getRequestDispatcher("/jsp/user/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        if (!CsrfUtil.isValid(request)) {
            request.setAttribute("error", "Phiên làm việc không hợp lệ. Vui lòng thử lại.");
            request.getRequestDispatcher("/jsp/user/register.jsp").forward(request, response);
            return;
        }

        String hoTen = request.getParameter("hoTen");
        String email = request.getParameter("email");
        String matKhau = request.getParameter("matKhau");
        String xacNhanMatKhau = request.getParameter("xacNhanMatKhau");
        String soDienThoai = request.getParameter("soDienThoai");
        String diaChi = request.getParameter("diaChi");

        hoTen = InputSanitizer.cleanPlainText(hoTen, 100);
email = InputSanitizer.normalizeEmail(email);

if (matKhau != null) matKhau = matKhau.trim();
if (xacNhanMatKhau != null) xacNhanMatKhau = xacNhanMatKhau.trim();

soDienThoai = InputSanitizer.normalizePhone(soDienThoai);
diaChi = InputSanitizer.cleanMultilineText(diaChi, 255);

        if (hoTen == null || hoTen.isEmpty()
                || email == null || email.isEmpty()
                || matKhau == null || matKhau.isEmpty()
                || xacNhanMatKhau == null || xacNhanMatKhau.isEmpty()) {

            request.setAttribute("error", "Vui lòng nhập đầy đủ họ tên, email, mật khẩu.");
            request.getRequestDispatcher("/jsp/user/register.jsp").forward(request, response);
            return;
        }

        if (!matKhau.equals(xacNhanMatKhau)) {
            request.setAttribute("error", "Mật khẩu xác nhận không khớp.");
            request.getRequestDispatcher("/jsp/user/register.jsp").forward(request, response);
            return;
        }

        if (!PasswordUtil.isStrongPassword(matKhau)) {
            request.setAttribute("error", "Mật khẩu phải có ít nhất 8 ký tự, gồm chữ và số.");
            request.getRequestDispatcher("/jsp/user/register.jsp").forward(request, response);
            return;
        }

        NguoiDungDAO dao = new NguoiDungDAO();
        if (dao.emailDaTonTai(email)) {
            request.setAttribute("error", "Email này đã được sử dụng.");
            request.getRequestDispatcher("/jsp/user/register.jsp").forward(request, response);
            return;
        }

        String otp = OtpUtil.generate6DigitOtp();
        String matKhauDaHash = PasswordUtil.hashPassword(matKhau);

        HttpSession session = request.getSession();
        session.setAttribute("register_hoTen", hoTen);
        session.setAttribute("register_email", email);
        session.setAttribute("register_matKhau", matKhauDaHash);
        session.setAttribute("register_soDienThoai", soDienThoai);
        session.setAttribute("register_diaChi", diaChi);
        session.setAttribute("register_otp", otp);
        session.setAttribute("register_otp_expire", System.currentTimeMillis() + 5 * 60 * 1000L);
        session.setAttribute("register_otp_resend", System.currentTimeMillis() + 60 * 1000L);

        try {
            EmailUtil.sendRegisterOtp(email, otp);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Không gửi được email OTP. Vui lòng kiểm tra lại Gmail gửi và App Password.");
            request.getRequestDispatcher("/jsp/user/register.jsp").forward(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/verify-register-otp");
    }
}   