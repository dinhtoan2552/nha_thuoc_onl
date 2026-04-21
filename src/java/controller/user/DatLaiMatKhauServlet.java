package controller.user;

import dao.NguoiDungDAO;
import dao.QuenMatKhauOtpDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import utils.PasswordUtil;

@WebServlet("/dat-lai-mat-khau")
public class DatLaiMatKhauServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null
                || session.getAttribute("reset_email") == null
                || session.getAttribute("reset_verified") == null
                || !(Boolean) session.getAttribute("reset_verified")) {

            response.sendRedirect(request.getContextPath() + "/quen-mat-khau");
            return;
        }

        request.getRequestDispatcher("/jsp/user/dat_lai_mat_khau.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        // 🔒 Check bảo mật session
        if (session == null
                || session.getAttribute("reset_email") == null
                || session.getAttribute("reset_verified") == null
                || !(Boolean) session.getAttribute("reset_verified")) {

            response.sendRedirect(request.getContextPath() + "/quen-mat-khau");
            return;
        }

        String email = String.valueOf(session.getAttribute("reset_email")).trim().toLowerCase();
        String matKhauMoi = request.getParameter("matKhauMoi");
        String nhapLaiMatKhau = request.getParameter("nhapLaiMatKhau");

        // 🔒 Validate null
        if (matKhauMoi == null || nhapLaiMatKhau == null) {
            request.setAttribute("error", "Dữ liệu không hợp lệ.");
            request.getRequestDispatcher("/jsp/user/dat_lai_mat_khau.jsp").forward(request, response);
            return;
        }

        matKhauMoi = matKhauMoi.trim();
        nhapLaiMatKhau = nhapLaiMatKhau.trim();

        // 🔒 Validate độ mạnh mật khẩu
        if (!PasswordUtil.isStrongPassword(matKhauMoi)) {
            request.setAttribute("error", "Mật khẩu phải >= 8 ký tự, có chữ và số.");
            request.getRequestDispatcher("/jsp/user/dat_lai_mat_khau.jsp").forward(request, response);
            return;
        }

        // 🔒 Check confirm password
        if (!matKhauMoi.equals(nhapLaiMatKhau)) {
            request.setAttribute("error", "Nhập lại mật khẩu không khớp.");
            request.getRequestDispatcher("/jsp/user/dat_lai_mat_khau.jsp").forward(request, response);
            return;
        }

        // 🔐 Hash password chuẩn
        String hash = PasswordUtil.hashPassword(matKhauMoi);

        NguoiDungDAO nguoiDungDAO = new NguoiDungDAO();
        boolean updated = nguoiDungDAO.capNhatMatKhauTheoEmail(email, hash);

        if (!updated) {
            request.setAttribute("error", "Không thể cập nhật mật khẩu.");
            request.getRequestDispatcher("/jsp/user/dat_lai_mat_khau.jsp").forward(request, response);
            return;
        }

        // 🔥 Xóa OTP sau khi dùng (đúng logic bảo mật)
        QuenMatKhauOtpDAO otpDAO = new QuenMatKhauOtpDAO();
        otpDAO.xoaTatCaTheoEmail(email);

        // 🔥 Xóa session để tránh reuse
        session.removeAttribute("reset_email");
        session.removeAttribute("reset_verified");

        // 🔔 Thông báo thành công
        session.setAttribute("successMessage", "Đặt lại mật khẩu thành công. Vui lòng đăng nhập lại.");

        response.sendRedirect(request.getContextPath() + "/login");
    }
}