package controller.admin;

import dao.CauHinhThanhToanDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.IOException;
import model.CauHinhThanhToan;
import utils.FileUploadUtil;

@WebServlet("/admin/thanh-toan")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 10 * 1024 * 1024,
        maxRequestSize = 20 * 1024 * 1024
)
public class AdminThanhToanServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        CauHinhThanhToanDAO dao = new CauHinhThanhToanDAO();
        CauHinhThanhToan cauHinh = dao.getCauHinh();

        request.setAttribute("cauHinh", cauHinh);
        request.getRequestDispatcher("/jsp/admin/quanly-thanhtoan.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        FileUploadUtil.createUploadDirIfNotExists();

        CauHinhThanhToanDAO dao = new CauHinhThanhToanDAO();
        CauHinhThanhToan c = dao.getCauHinh();

        if (c == null) {
            response.sendRedirect(request.getContextPath() + "/admin/thanh-toan");
            return;
        }

        String tenNganHangQr = request.getParameter("tenNganHangQr");
        String soTaiKhoanQr = request.getParameter("soTaiKhoanQr");
        String chuTaiKhoanQr = request.getParameter("chuTaiKhoanQr");

        String tenNganHangThe = request.getParameter("tenNganHangThe");
        String soTaiKhoanThe = request.getParameter("soTaiKhoanThe");
        String chiNhanhThe = request.getParameter("chiNhanhThe");
        String chuThe = request.getParameter("chuThe");

        c.setTenNganHangQr(tenNganHangQr);
        c.setSoTaiKhoanQr(soTaiKhoanQr);
        c.setChuTaiKhoanQr(chuTaiKhoanQr);

        c.setTenNganHangThe(tenNganHangThe);
        c.setSoTaiKhoanThe(soTaiKhoanThe);
        c.setChiNhanhThe(chiNhanhThe);
        c.setChuThe(chuThe);

        Part qrPart = request.getPart("anhQR");
        if (qrPart != null && qrPart.getSize() > 0) {
            String fileName = FileUploadUtil.saveImage(qrPart);
            if (fileName != null) {
                c.setAnhQR(fileName);
            }
        }

        Part thePart = request.getPart("anhThe");
        if (thePart != null && thePart.getSize() > 0) {
            String fileName = FileUploadUtil.saveImage(thePart);
            if (fileName != null) {
                c.setAnhThe(fileName);
            }
        }

        boolean ok = dao.capNhat(c);

        if (ok) {
            request.getSession().setAttribute("adminMessage", "Cập nhật cấu hình thanh toán thành công.");
        } else {
            request.getSession().setAttribute("adminMessage", "Cập nhật cấu hình thanh toán thất bại.");
        }

        response.sendRedirect(request.getContextPath() + "/admin/thanh-toan");
    }
}