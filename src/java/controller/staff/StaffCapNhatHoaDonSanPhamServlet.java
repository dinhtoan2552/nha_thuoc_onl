package controller.staff;

import dao.DonHangDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.IOException;
import utils.FileUploadUtil;

@WebServlet("/staff/cap-nhat-hoa-don-san-pham")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 10 * 1024 * 1024,
        maxRequestSize = 20 * 1024 * 1024
)
public class StaffCapNhatHoaDonSanPhamServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        FileUploadUtil.createUploadDirIfNotExists();

        String idChiTietStr = request.getParameter("idChiTiet");
        String idDonHangStr = request.getParameter("idDonHang");
        String ghiChuHoaDonSP = request.getParameter("ghiChuHoaDonSP");

        int idChiTiet;
        int idDonHang;

        try {
            idChiTiet = Integer.parseInt(idChiTietStr);
            idDonHang = Integer.parseInt(idDonHangStr);
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/staff/donhang");
            return;
        }

        Part filePart = request.getPart("anhHoaDonSP");
        String tenFile = null;

        if (filePart != null && filePart.getSize() > 0) {
            tenFile = FileUploadUtil.saveImage(filePart);
        }

        if (tenFile == null || tenFile.trim().isEmpty()) {
            HttpSession session = request.getSession();
            session.setAttribute("staffOrderMessage", "Vui lòng chọn ảnh hóa đơn sản phẩm.");
            response.sendRedirect(request.getContextPath() + "/staff/chi-tiet-don-hang?id=" + idDonHang);
            return;
        }

        DonHangDAO dao = new DonHangDAO();
        boolean ok = dao.capNhatHoaDonSanPham(idChiTiet, tenFile, ghiChuHoaDonSP);

        HttpSession session = request.getSession();
        if (ok) {
            session.setAttribute("staffOrderMessage", "Đã cập nhật hóa đơn cho sản phẩm.");
        } else {
            session.setAttribute("staffOrderMessage", "Cập nhật hóa đơn sản phẩm thất bại.");
        }

        response.sendRedirect(request.getContextPath() + "/staff/chi-tiet-don-hang?id=" + idDonHang);
    }
}