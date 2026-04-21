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
import utils.PasswordUtil;

@WebServlet("/admin/nhanvien")
public class AdminNhanVienServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String keyword = request.getParameter("keyword");
        String trangThai = request.getParameter("trangThai");

        if (keyword == null) keyword = "";
        if (trangThai == null) trangThai = "";

        NguoiDungDAO dao = new NguoiDungDAO();

        List<NguoiDung> danhSachNhanVien = dao.getNhanVien(keyword, trangThai);

        int tongNhanVien = dao.countNhanVien();
        int dangHoatDong = dao.countNhanVienTheoTrangThai("HOAT_DONG");
        int biKhoa = dao.countNhanVienTheoTrangThai("KHOA");

        request.setAttribute("danhSachNhanVien", danhSachNhanVien);
        request.setAttribute("keyword", keyword);
        request.setAttribute("trangThai", trangThai);
        request.setAttribute("tongNhanVien", tongNhanVien);
        request.setAttribute("dangHoatDong", dangHoatDong);
        request.setAttribute("biKhoa", biKhoa);

        request.getRequestDispatcher("/jsp/admin/quanly_nhanvien.jsp").forward(request, response);
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
                    themNhanVien(request, response, session, dao);
                    break;
                case "sua":
                    suaNhanVien(request, response, session, dao);
                    break;
                case "doiTrangThai":
                    doiTrangThaiNhanVien(request, response, session, dao);
                    break;
                default:
                    session.setAttribute("adminStaffMessage", "Hành động không hợp lệ.");
                    response.sendRedirect(request.getContextPath() + "/admin/nhanvien");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("adminStaffMessage", "Đã xảy ra lỗi khi xử lý nhân viên.");
            response.sendRedirect(request.getContextPath() + "/admin/nhanvien");
        }
    }

    private void themNhanVien(HttpServletRequest request, HttpServletResponse response,
                              HttpSession session, NguoiDungDAO dao) throws IOException {

        String hoTen = request.getParameter("hoTen");
        String email = request.getParameter("email");
        String matKhau = request.getParameter("matKhau");
        String soDienThoai = request.getParameter("soDienThoai");
        String diaChi = request.getParameter("diaChi");
        String trangThai = request.getParameter("trangThai");

        if (hoTen != null) hoTen = hoTen.trim();
        if (email != null) email = email.trim().toLowerCase();
        if (matKhau != null) matKhau = matKhau.trim();
        if (soDienThoai != null) soDienThoai = soDienThoai.trim();
        if (diaChi != null) diaChi = diaChi.trim();
        if (trangThai != null) trangThai = trangThai.trim();

        if (hoTen == null || hoTen.isEmpty()
                || email == null || email.isEmpty()
                || matKhau == null || matKhau.isEmpty()) {
            session.setAttribute("adminStaffMessage", "Vui lòng nhập đầy đủ họ tên, email và mật khẩu.");
            response.sendRedirect(request.getContextPath() + "/admin/nhanvien");
            return;
        }

        if (matKhau.length() < 6) {
            session.setAttribute("adminStaffMessage", "Mật khẩu phải có ít nhất 6 ký tự.");
            response.sendRedirect(request.getContextPath() + "/admin/nhanvien");
            return;
        }

        if (dao.emailDaTonTai(email)) {
            session.setAttribute("adminStaffMessage", "Email đã tồn tại.");
            response.sendRedirect(request.getContextPath() + "/admin/nhanvien");
            return;
        }

        if (trangThai == null || trangThai.isEmpty()) {
            trangThai = "HOAT_DONG";
        }

        if (!"HOAT_DONG".equalsIgnoreCase(trangThai) && !"KHOA".equalsIgnoreCase(trangThai)) {
            trangThai = "HOAT_DONG";
        }

        NguoiDung nd = new NguoiDung();
        nd.setHoTen(hoTen);
        nd.setEmail(email);
        nd.setMatKhau(PasswordUtil.hashPassword(matKhau));
        nd.setSoDienThoai(soDienThoai == null ? "" : soDienThoai);
        nd.setDiaChi(diaChi == null ? "" : diaChi);
        nd.setVaiTro("STAFF");
        nd.setTrangThai(trangThai);

        boolean ok = dao.insertNguoiDung(nd);
        session.setAttribute("adminStaffMessage", ok ? "Thêm nhân viên thành công." : "Thêm nhân viên thất bại.");

        response.sendRedirect(request.getContextPath() + "/admin/nhanvien");
    }

    private void suaNhanVien(HttpServletRequest request, HttpServletResponse response,
                             HttpSession session, NguoiDungDAO dao) throws IOException {

        int id = parseInt(request.getParameter("id"));
        String hoTen = request.getParameter("hoTen");
        String soDienThoai = request.getParameter("soDienThoai");
        String diaChi = request.getParameter("diaChi");
        String trangThai = request.getParameter("trangThai");

        if (hoTen != null) hoTen = hoTen.trim();
        if (soDienThoai != null) soDienThoai = soDienThoai.trim();
        if (diaChi != null) diaChi = diaChi.trim();
        if (trangThai != null) trangThai = trangThai.trim();

        NguoiDung nv = dao.getNguoiDungById(id);
        if (nv == null || !"STAFF".equalsIgnoreCase(nv.getVaiTro())) {
            session.setAttribute("adminStaffMessage", "Không tìm thấy nhân viên.");
            response.sendRedirect(request.getContextPath() + "/admin/nhanvien");
            return;
        }

        if (trangThai == null || trangThai.isEmpty()) {
            trangThai = nv.getTrangThai();
        }

        if (!"HOAT_DONG".equalsIgnoreCase(trangThai) && !"KHOA".equalsIgnoreCase(trangThai)) {
            trangThai = nv.getTrangThai();
        }

        nv.setHoTen(hoTen == null ? "" : hoTen);
        nv.setSoDienThoai(soDienThoai == null ? "" : soDienThoai);
        nv.setDiaChi(diaChi == null ? "" : diaChi);
        nv.setVaiTro("STAFF");
        nv.setTrangThai(trangThai);

        boolean ok = dao.updateNguoiDung(nv);
        session.setAttribute("adminStaffMessage", ok ? "Cập nhật nhân viên thành công." : "Cập nhật nhân viên thất bại.");

        response.sendRedirect(request.getContextPath() + "/admin/nhanvien");
    }

    private void doiTrangThaiNhanVien(HttpServletRequest request, HttpServletResponse response,
                                      HttpSession session, NguoiDungDAO dao) throws IOException {

        int id = parseInt(request.getParameter("id"));
        String trangThaiMoi = request.getParameter("trangThaiMoi");

        if (trangThaiMoi != null) trangThaiMoi = trangThaiMoi.trim();

        NguoiDung nv = dao.getNguoiDungById(id);
        if (nv == null || !"STAFF".equalsIgnoreCase(nv.getVaiTro())) {
            session.setAttribute("adminStaffMessage", "Không tìm thấy nhân viên.");
            response.sendRedirect(request.getContextPath() + "/admin/nhanvien");
            return;
        }

        if (!"HOAT_DONG".equalsIgnoreCase(trangThaiMoi) && !"KHOA".equalsIgnoreCase(trangThaiMoi)) {
            session.setAttribute("adminStaffMessage", "Trạng thái không hợp lệ.");
            response.sendRedirect(request.getContextPath() + "/admin/nhanvien");
            return;
        }

        boolean ok = dao.updateTrangThaiNguoiDung(id, trangThaiMoi);
        session.setAttribute("adminStaffMessage", ok ? "Cập nhật trạng thái nhân viên thành công." : "Cập nhật trạng thái nhân viên thất bại.");

        response.sendRedirect(request.getContextPath() + "/admin/nhanvien");
    }

    private int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }
}