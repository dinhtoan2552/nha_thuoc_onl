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
import utils.OtpUtil;
import utils.PasswordUtil;

@WebServlet("/doi-mat-khau")
public class DoiMatKhauServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        CsrfUtil.getToken(session);
        request.getRequestDispatcher("/jsp/user/doi_mat_khau.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        if (!CsrfUtil.isValid(request)) {
            request.setAttribute("error", "Phiên làm việc không hợp lệ. Vui lòng thử lại.");
            request.getRequestDispatcher("/jsp/user/doi_mat_khau.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        NguoiDung user = (NguoiDung) session.getAttribute("user");

        String matKhauCu = request.getParameter("matKhauCu");
        String matKhauMoi = request.getParameter("matKhauMoi");
        String xacNhanMatKhau = request.getParameter("xacNhanMatKhau");

        if (matKhauCu == null || matKhauMoi == null || xacNhanMatKhau == null
                || matKhauCu.trim().isEmpty()
                || matKhauMoi.trim().isEmpty()
                || xacNhanMatKhau.trim().isEmpty()) {

            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin.");
            request.getRequestDispatcher("/jsp/user/doi_mat_khau.jsp").forward(request, response);
            return;
        }

        matKhauCu = matKhauCu.trim();
        matKhauMoi = matKhauMoi.trim();
        xacNhanMatKhau = xacNhanMatKhau.trim();

        NguoiDungDAO dao = new NguoiDungDAO();
        NguoiDung freshUser = dao.getNguoiDungById(user.getId());

        if (freshUser == null) {
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        boolean matKhauCuDung = false;
        String matKhauTrongDb = freshUser.getMatKhau();

        if (PasswordUtil.isBcryptHash(matKhauTrongDb)) {
            matKhauCuDung = PasswordUtil.verifyPassword(matKhauCu, matKhauTrongDb);
        } else if (matKhauTrongDb != null && matKhauTrongDb.equals(matKhauCu)) {
            matKhauCuDung = true;
        }

        if (!matKhauCuDung) {
            request.setAttribute("error", "Mật khẩu cũ không đúng.");
            request.getRequestDispatcher("/jsp/user/doi_mat_khau.jsp").forward(request, response);
            return;
        }

        if (!PasswordUtil.isStrongPassword(matKhauMoi)) {
            request.setAttribute("error", "Mật khẩu mới phải có ít nhất 8 ký tự, gồm chữ và số.");
            request.getRequestDispatcher("/jsp/user/doi_mat_khau.jsp").forward(request, response);
            return;
        }

        if (!matKhauMoi.equals(xacNhanMatKhau)) {
            request.setAttribute("error", "Xác nhận mật khẩu mới không khớp.");
            request.getRequestDispatcher("/jsp/user/doi_mat_khau.jsp").forward(request, response);
            return;
        }

        if (matKhauCu.equals(matKhauMoi)) {
            request.setAttribute("error", "Mật khẩu mới không được trùng mật khẩu cũ.");
            request.getRequestDispatcher("/jsp/user/doi_mat_khau.jsp").forward(request, response);
            return;
        }

        String otp = OtpUtil.generate6DigitOtp();
        String matKhauMoiDaHash = PasswordUtil.hashPassword(matKhauMoi);

        session.setAttribute("account_action_type", "CHANGE_PASSWORD");
        session.setAttribute("account_action_otp", otp);
        session.setAttribute("account_action_otp_expire", System.currentTimeMillis() + 5 * 60 * 1000L);
        session.setAttribute("account_new_password", matKhauMoiDaHash);

        try {
            EmailUtil.sendChangePasswordOtp(freshUser.getEmail(), otp);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Không gửi được mã OTP về email. Vui lòng thử lại.");
            request.getRequestDispatcher("/jsp/user/doi_mat_khau.jsp").forward(request, response);
            return;
        }

        session.setAttribute("successMessage", "Mã OTP đã được gửi về email của bạn. Vui lòng xác nhận để đổi mật khẩu.");
        response.sendRedirect(request.getContextPath() + "/xac-nhan-otp-tai-khoan");
    }
}