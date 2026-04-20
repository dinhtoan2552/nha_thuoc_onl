package controller.admin;

import dao.DonHangDAO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.DonHang;
import model.NguoiDung;

@WebServlet(name = "AdminDonHangServlet", urlPatterns = {"/admin/donhang"})
public class AdminDonHangServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String keyword = request.getParameter("keyword");
        String trangThai = request.getParameter("trangThai");

        DonHangDAO dao = new DonHangDAO();
        List<DonHang> danhSachDonHang = dao.getAllDonHang(keyword, trangThai);

        request.setAttribute("danhSachDonHang", danhSachDonHang);
        request.setAttribute("keyword", keyword == null ? "" : keyword);
        request.setAttribute("trangThai", trangThai == null ? "" : trangThai);

        request.setAttribute("tongDonHang", dao.countAllDonHang());
        request.setAttribute("donChoXacNhan", dao.countDonHangTheoTrangThai("CHO_XAC_NHAN"));
        request.setAttribute("donDangGiao", dao.countDonHangTheoTrangThai("DANG_GIAO"));
        request.setAttribute("donDaGiao", dao.countDonHangTheoTrangThai("DA_GIAO"));

        request.getRequestDispatcher("/jsp/admin/quanly_donhang.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        String keyword = request.getParameter("keyword");
        String trangThai = request.getParameter("trangThai");

        HttpSession session = request.getSession(false);
        Integer nguoiXuLy = null;

        if (session != null && session.getAttribute("user") != null) {
            NguoiDung user = (NguoiDung) session.getAttribute("user");
            nguoiXuLy = user.getId();
        }

        DonHangDAO dao = new DonHangDAO();

        if ("xacNhanTatCa".equals(action)) {
            String[] selectedIds = request.getParameterValues("selectedOrderIds");
            List<Integer> dsId = new ArrayList<>();

            if (selectedIds != null) {
                for (String id : selectedIds) {
                    try {
                        dsId.add(Integer.parseInt(id));
                    } catch (Exception e) {
                    }
                }
            }

            int soLuong = dao.xacNhanTatCaDonChoXacNhan(dsId, nguoiXuLy);
            request.getSession().setAttribute("adminOrderMessage", "Đã xác nhận " + soLuong + " đơn hàng.");
        } else if ("huyNhieuDon".equals(action)) {
            String lyDoHuy = request.getParameter("lyDoHuy");
            String[] selectedIds = request.getParameterValues("selectedOrderIds");

            int soLuong = 0;
            if (selectedIds != null) {
                for (String id : selectedIds) {
                    try {
                        int idDonHang = Integer.parseInt(id);
                        if (dao.huyDonHangCoLyDo(idDonHang, lyDoHuy, nguoiXuLy)) {
                            soLuong++;
                        }
                    } catch (Exception e) {
                    }
                }
            }

            request.getSession().setAttribute("adminOrderMessage", "Đã hủy " + soLuong + " đơn hàng.");
        }

        String redirectUrl = request.getContextPath() + "/admin/donhang";
        boolean hasQuery = false;

        if (keyword != null && !keyword.trim().isEmpty()) {
            redirectUrl += (hasQuery ? "&" : "?") + "keyword=" + java.net.URLEncoder.encode(keyword, "UTF-8");
            hasQuery = true;
        }

        if (trangThai != null && !trangThai.trim().isEmpty()) {
            redirectUrl += (hasQuery ? "&" : "?") + "trangThai=" + java.net.URLEncoder.encode(trangThai, "UTF-8");
        }

        response.sendRedirect(redirectUrl);
    }
}