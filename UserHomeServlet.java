package controller.user;

import dao.DanhMucDAO;
import dao.QuangCaoDAO;
import dao.ThuocDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import model.DanhMuc;
import model.NguoiDung;
import model.Thuoc;
import utils.CsrfUtil;
import utils.InputSanitizer;
import dao.ThongTinLienHeDAO;
import model.ThongTinLienHe;

@WebServlet("/user/home")
public class UserHomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(true);
        NguoiDung user = (session != null) ? (NguoiDung) session.getAttribute("user") : null;

        request.setAttribute("userDangNhap", user);

        // Tạo CSRF token cho các form POST ở home.jsp
        CsrfUtil.getToken(session);

        QuangCaoDAO quangCaoDAO = new QuangCaoDAO();
        request.setAttribute("dsQuangCao", quangCaoDAO.getQuangCaoDangBat());

        DanhMucDAO danhMucDAO = new DanhMucDAO();
        List<DanhMuc> dsDanhMuc = danhMucDAO.getAllDanhMuc();
        request.setAttribute("dsDanhMuc", dsDanhMuc);

        String keyword = InputSanitizer.cleanPlainText(request.getParameter("keyword"), 200);
        request.setAttribute("keyword", keyword);

        String idDanhMucParam = request.getParameter("idDanhMuc");
        int idDanhMuc = 0;
        try {
            if (idDanhMucParam != null && !idDanhMucParam.trim().isEmpty()) {
                idDanhMuc = Integer.parseInt(idDanhMucParam);
                if (idDanhMuc < 0) {
                    idDanhMuc = 0;
                }
            }
        } catch (Exception e) {
            idDanhMuc = 0;
        }

        int page = 1;
        int pageSize = 12;

        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
                if (page < 1) {
                    page = 1;
                }
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        ThuocDAO thuocDAO = new ThuocDAO();
        int tongSoThuoc;
        List<Thuoc> danhSachThuoc;

        boolean coKeyword = keyword != null && !keyword.isEmpty();

        if (coKeyword && idDanhMuc > 0) {
            tongSoThuoc = thuocDAO.demTongSoThuocChoUserTheoTuKhoaVaDanhMuc(keyword, idDanhMuc);
        } else if (coKeyword) {
            tongSoThuoc = thuocDAO.demTongSoThuocChoUserTheoTuKhoa(keyword);
        } else if (idDanhMuc > 0) {
            tongSoThuoc = thuocDAO.demTongSoThuocChoUserTheoDanhMuc(idDanhMuc);
        } else {
            tongSoThuoc = thuocDAO.demTongSoThuocChoUser();
        }

        int tongSoTrang = (int) Math.ceil((double) tongSoThuoc / pageSize);
        if (tongSoTrang == 0) {
            tongSoTrang = 1;
        }

        if (page > tongSoTrang) {
            page = tongSoTrang;
        }

        int offset = (page - 1) * pageSize;

        if (coKeyword && idDanhMuc > 0) {
            danhSachThuoc = thuocDAO.getThuocPhanTrangChoUserTheoTuKhoaVaDanhMuc(keyword, idDanhMuc, offset, pageSize);
        } else if (coKeyword) {
            danhSachThuoc = thuocDAO.getThuocPhanTrangChoUserTheoTuKhoa(keyword, offset, pageSize);
        } else if (idDanhMuc > 0) {
            danhSachThuoc = thuocDAO.getThuocPhanTrangChoUserTheoDanhMuc(idDanhMuc, offset, pageSize);
        } else {
            danhSachThuoc = thuocDAO.getThuocPhanTrangChoUser(offset, pageSize);
        }

        request.setAttribute("danhSachThuoc", danhSachThuoc);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", tongSoTrang);
        request.setAttribute("idDanhMuc", idDanhMuc);
        ThongTinLienHeDAO thongTinLienHeDAO = new ThongTinLienHeDAO();
ThongTinLienHe thongTinLienHe = thongTinLienHeDAO.getThongTinLienHe();
request.setAttribute("thongTinLienHe", thongTinLienHe);

        request.getRequestDispatcher("/jsp/user/home.jsp").forward(request, response);
    }
}