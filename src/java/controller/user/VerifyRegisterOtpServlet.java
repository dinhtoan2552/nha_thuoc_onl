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
import utils.PasswordUtil;

@WebServlet(name = "VerifyRegisterOtpServlet", urlPatterns = {"/verify-register-otp"})
public class VerifyRegisterOtpServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("register_email") == null) {
            response.sendRedirect(request.getContextPath() + "/register");
            return;
        }

        // Tạo token CSRF để form submit dùng lại
        String csrfToken = CsrfUtil.getToken(session);
        request.setAttribute("csrfToken", csrfToken);

        request.getRequestDispatcher("/jsp/user/verify_register_otp.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("register_email") == null) {
            response.sendRedirect(request.getContextPath() + "/register");
            return;
        }

        // Luôn đẩy lại token ra form khi forward lỗi
        String csrfToken = CsrfUtil.getToken(session);
        request.setAttribute("csrfToken", csrfToken);

        if (!CsrfUtil.isValid(request)) {
            request.setAttribute("error", "Phiên làm việc không hợp lệ. Vui lòng thử lại.");
            request.getRequestDispatcher("/jsp/user/verify_register_otp.jsp").forward(request, response);
            return;
        }

        String otpNhap = request.getParameter("otp");
        if (otpNhap != null) {
            otpNhap = otpNhap.trim();
        }

        String otpHeThong = (String) session.getAttribute("register_otp");
        Long otpExpire = (Long) session.getAttribute("register_otp_expire");

        if (otpNhap == null || otpNhap.isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập mã OTP.");
            request.getRequestDispatcher("/jsp/user/verify_register_otp.jsp").forward(request, response);
            return;
        }

        if (otpExpire == null || System.currentTimeMillis() > otpExpire) {
            request.setAttribute("error", "Mã OTP đã hết hạn. Vui lòng gửi lại mã.");
            request.getRequestDispatcher("/jsp/user/verify_register_otp.jsp").forward(request, response);
            return;
        }

        if (otpHeThong == null || !otpHeThong.equals(otpNhap)) {
            request.setAttribute("error", "Mã OTP không đúng.");
            request.getRequestDispatcher("/jsp/user/verify_register_otp.jsp").forward(request, response);
            return;
        }

        String hoTen = (String) session.getAttribute("register_hoTen");
        String email = (String) session.getAttribute("register_email");
        String matKhauDaHash = (String) session.getAttribute("register_matKhau");
        String soDienThoai = (String) session.getAttribute("register_soDienThoai");
        String diaChi = (String) session.getAttribute("register_diaChi");

        if (hoTen == null || hoTen.trim().isEmpty()
                || email == null || email.trim().isEmpty()
                || matKhauDaHash == null || matKhauDaHash.trim().isEmpty()) {

            request.setAttribute("error", "Thông tin đăng ký không hợp lệ. Vui lòng đăng ký lại.");
            request.getRequestDispatcher("/jsp/user/verify_register_otp.jsp").forward(request, response);
            return;
        }

        if (!PasswordUtil.isBcryptHash(matKhauDaHash)) {
            request.setAttribute("error", "Thông tin bảo mật không hợp lệ. Vui lòng đăng ký lại.");
            request.getRequestDispatcher("/jsp/user/verify_register_otp.jsp").forward(request, response);
            return;
        }

        NguoiDungDAO dao = new NguoiDungDAO();
        if (dao.emailDaTonTai(email)) {
            request.setAttribute("error", "Email này đã tồn tại.");
            request.getRequestDispatcher("/jsp/user/verify_register_otp.jsp").forward(request, response);
            return;
        }

        NguoiDung nd = new NguoiDung();
        nd.setHoTen(hoTen);
        nd.setEmail(email);
        nd.setMatKhau(matKhauDaHash);
        nd.setSoDienThoai((soDienThoai == null || soDienThoai.trim().isEmpty()) ? null : soDienThoai.trim());
        nd.setDiaChi((diaChi == null || diaChi.trim().isEmpty()) ? null : diaChi.trim());
        nd.setVaiTro("USER");
        nd.setTrangThai("HOAT_DONG");

        boolean ok = dao.insertNguoiDung(nd);

        if (!ok) {
            request.setAttribute("error", "Tạo tài khoản thất bại.");
            request.getRequestDispatcher("/jsp/user/verify_register_otp.jsp").forward(request, response);
            return;
        }

        session.removeAttribute("register_hoTen");
        session.removeAttribute("register_email");
        session.removeAttribute("register_matKhau");
        session.removeAttribute("register_soDienThoai");
        session.removeAttribute("register_diaChi");
        session.removeAttribute("register_otp");
        session.removeAttribute("register_otp_expire");
        session.removeAttribute("register_otp_resend");

        request.getSession().setAttribute("successMessage", "Đăng ký thành công. Bạn hãy đăng nhập.");
        response.sendRedirect(request.getContextPath() + "/login");
    }
}