package controller.admin;

import dao.NguoiDungDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import model.NguoiDung;

@WebServlet("/admin/nguoidung")
public class AdminNguoiDungServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String keyword = request.getParameter("keyword");
        String vaiTro = request.getParameter("vaiTro");
        String trangThai = request.getParameter("trangThai");

        if (keyword == null) keyword = "";
        if (vaiTro == null) vaiTro = "";
        if (trangThai == null) trangThai = "";

        NguoiDungDAO dao = new NguoiDungDAO();

        if (vaiTro == null || vaiTro.trim().isEmpty()) {
    vaiTro = "USER";
}

List<NguoiDung> danhSachNguoiDung = dao.getAllNguoiDung(keyword, vaiTro, trangThai);

        int tongNguoiDung = dao.countAllNguoiDung();
        int dangHoatDong = dao.countNguoiDungTheoTrangThai("HOAT_DONG");
        int biKhoa = dao.countNguoiDungTheoTrangThai("BI_KHOA");

        request.setAttribute("danhSachNguoiDung", danhSachNguoiDung);
        request.setAttribute("keyword", keyword);
        request.setAttribute("vaiTro", vaiTro);
        request.setAttribute("trangThai", trangThai);

        request.setAttribute("tongNguoiDung", tongNguoiDung);
        request.setAttribute("dangHoatDong", dangHoatDong);
        request.setAttribute("biKhoa", biKhoa);

        request.getRequestDispatcher("/jsp/admin/quanly_nguoidung.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        NguoiDungDAO dao = new NguoiDungDAO();

        String action = request.getParameter("action");
        if (action == null) action = "";

        try {
            switch (action) {
                case "them":
                    themNguoiDung(request, response, session, dao);
                    break;
                case "sua":
                    suaNguoiDung(request, response, session, dao);
                    break;
                case "doiTrangThai":
                    doiTrangThai(request, response, session, dao);
                    break;
                default:
                    session.setAttribute("adminUserMessage", "Hành động không hợp lệ.");
                    response.sendRedirect(request.getContextPath() + "/admin/nguoidung");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("adminUserMessage", "Đã xảy ra lỗi khi xử lý người dùng.");
            response.sendRedirect(request.getContextPath() + "/admin/nguoidung");
        }
    }

    private void themNguoiDung(HttpServletRequest request, HttpServletResponse response,
                               HttpSession session, NguoiDungDAO dao) throws IOException {

        String hoTen = request.getParameter("hoTen");
        String email = request.getParameter("email");
        String matKhau = request.getParameter("matKhau");
        String soDienThoai = request.getParameter("soDienThoai");
        String diaChi = request.getParameter("diaChi");
        String vaiTro = request.getParameter("vaiTro");
        String trangThai = request.getParameter("trangThai");

        if (hoTen == null || hoTen.trim().isEmpty()
                || email == null || email.trim().isEmpty()
                || matKhau == null || matKhau.trim().isEmpty()) {
            session.setAttribute("adminUserMessage", "Vui lòng nhập đầy đủ họ tên, email và mật khẩu.");
            response.sendRedirect(request.getContextPath() + "/admin/nguoidung");
            return;
        }

        if (dao.emailDaTonTai(email.trim())) {
            session.setAttribute("adminUserMessage", "Email đã tồn tại.");
            response.sendRedirect(request.getContextPath() + "/admin/nguoidung");
            return;
        }

        NguoiDung nd = new NguoiDung();
        nd.setHoTen(hoTen.trim());
        nd.setEmail(email.trim());
        nd.setMatKhau(matKhau.trim());
        nd.setSoDienThoai(soDienThoai == null ? "" : soDienThoai.trim());
        nd.setDiaChi(diaChi == null ? "" : diaChi.trim());
        nd.setVaiTro(vaiTro == null || vaiTro.trim().isEmpty() ? "USER" : vaiTro.trim());
        nd.setTrangThai(trangThai == null || trangThai.trim().isEmpty() ? "HOAT_DONG" : trangThai.trim());

        boolean ok = dao.insertNguoiDung(nd);
        session.setAttribute("adminUserMessage", ok ? "Thêm người dùng thành công." : "Thêm người dùng thất bại.");

        response.sendRedirect(request.getContextPath() + "/admin/nguoidung");
    }

    private void suaNguoiDung(HttpServletRequest request, HttpServletResponse response,
                              HttpSession session, NguoiDungDAO dao) throws IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        String hoTen = request.getParameter("hoTen");
        String soDienThoai = request.getParameter("soDienThoai");
        String diaChi = request.getParameter("diaChi");
        String vaiTro = request.getParameter("vaiTro");
        String trangThai = request.getParameter("trangThai");

        NguoiDung cu = dao.getNguoiDungById(id);
        if (cu == null) {
            session.setAttribute("adminUserMessage", "Không tìm thấy người dùng.");
            response.sendRedirect(request.getContextPath() + "/admin/nguoidung");
            return;
        }

        cu.setHoTen(hoTen == null ? "" : hoTen.trim());
        cu.setSoDienThoai(soDienThoai == null ? "" : soDienThoai.trim());
        cu.setDiaChi(diaChi == null ? "" : diaChi.trim());
        cu.setVaiTro(vaiTro == null || vaiTro.trim().isEmpty() ? cu.getVaiTro() : vaiTro.trim());
        cu.setTrangThai(trangThai == null || trangThai.trim().isEmpty() ? cu.getTrangThai() : trangThai.trim());

        boolean ok = dao.updateNguoiDung(cu);
        session.setAttribute("adminUserMessage", ok ? "Cập nhật người dùng thành công." : "Cập nhật người dùng thất bại.");

        response.sendRedirect(request.getContextPath() + "/admin/nguoidung");
    }

    private void doiTrangThai(HttpServletRequest request, HttpServletResponse response,
                              HttpSession session, NguoiDungDAO dao) throws IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        String trangThaiMoi = request.getParameter("trangThaiMoi");

        NguoiDung nd = dao.getNguoiDungById(id);
        if (nd == null) {
            session.setAttribute("adminUserMessage", "Không tìm thấy người dùng.");
            response.sendRedirect(request.getContextPath() + "/admin/nguoidung");
            return;
        }

        if ("ADMIN".equalsIgnoreCase(nd.getVaiTro()) && "BI_KHOA".equalsIgnoreCase(trangThaiMoi)) {
            session.setAttribute("adminUserMessage", "Không thể khóa tài khoản ADMIN.");
            response.sendRedirect(request.getContextPath() + "/admin/nguoidung");
            return;
        }

        boolean ok = dao.updateTrangThaiNguoiDung(id, trangThaiMoi);
        session.setAttribute("adminUserMessage", ok ? "Cập nhật trạng thái thành công." : "Cập nhật trạng thái thất bại.");

        response.sendRedirect(request.getContextPath() + "/admin/nguoidung");
    }
}