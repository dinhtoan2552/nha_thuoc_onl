package controller.user;

import dao.CauHinhThanhToanDAO;
import dao.GioHangDAO;
import dao.ThuocDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.CauHinhThanhToan;
import model.GioHangItem;
import model.NguoiDung;
import model.Thuoc;

@WebServlet("/thanh-toan-gio-hang")
public class ThanhToanGioHangServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/cart");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        NguoiDung user = (NguoiDung) session.getAttribute("user");
        String vaiTro = user.getVaiTro() != null ? user.getVaiTro().trim().toUpperCase() : "";

        if (!"USER".equals(vaiTro)) {
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        GioHangDAO gioHangDAO = new GioHangDAO();
        ThuocDAO thuocDAO = new ThuocDAO();

        List<GioHangItem> dsGioHang = gioHangDAO.getDanhSachGioHang(user.getId());
        if (dsGioHang == null) {
            dsGioHang = new ArrayList<>();
        }

        if (dsGioHang.isEmpty()) {
            session.setAttribute("cartMessage", "Giỏ hàng của bạn đang trống.");
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        double tongTien = 0;
        double phiVanChuyen = 0;

        List<GioHangItem> dsHopLe = new ArrayList<>();

        for (GioHangItem item : dsGioHang) {
            Thuoc thuoc = thuocDAO.getThuocById(item.getIdThuoc());

            if (thuoc == null) {
                continue;
            }

            if (thuoc.getSoLuong() <= 0 || "HET_HANG".equalsIgnoreCase(thuoc.getTrangThai())) {
                continue;
            }

            int soLuongHopLe = item.getSoLuong();
            if (soLuongHopLe < 1) {
                soLuongHopLe = 1;
            }
            if (soLuongHopLe > thuoc.getSoLuong()) {
                soLuongHopLe = thuoc.getSoLuong();
            }

            item.setSoLuong(soLuongHopLe);
            item.setDonGia(thuoc.getDonGia());
            item.setThanhTien(thuoc.getDonGia() * soLuongHopLe);

            dsHopLe.add(item);
            tongTien += item.getThanhTien();

            if (thuoc.getPhiShip() > phiVanChuyen) {
                phiVanChuyen = thuoc.getPhiShip();
            }
        }

        if (dsHopLe.isEmpty()) {
            session.setAttribute("cartMessage", "Các sản phẩm trong giỏ hiện không thể đặt hàng.");
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        double tongThanhToan = tongTien + phiVanChuyen;

        CauHinhThanhToanDAO cauHinhDAO = new CauHinhThanhToanDAO();
        CauHinhThanhToan cauHinhThanhToan = cauHinhDAO.getCauHinh();

        request.setAttribute("dsGioHang", dsHopLe);
        request.setAttribute("tongTien", tongTien);
        request.setAttribute("phiVanChuyen", phiVanChuyen);
        request.setAttribute("tongThanhToan", tongThanhToan);

        request.setAttribute("hoTenNhan", user.getHoTen() != null ? user.getHoTen() : "");
        request.setAttribute("sdtNhan", user.getSoDienThoai() != null ? user.getSoDienThoai() : "");
        request.setAttribute("diaChiNhan", user.getDiaChi() != null ? user.getDiaChi() : "");
        request.setAttribute("nguon", "cart_all");

        request.setAttribute("cauHinhThanhToan", cauHinhThanhToan);

        request.getRequestDispatcher("/jsp/user/thanh_toan_gio_hang.jsp").forward(request, response);
    }
}