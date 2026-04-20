package controller.admin;

import dao.DanhMucDAO;
import dao.ThuocDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import model.Thuoc;
import utils.FileUploadUtil;

@WebServlet(name = "ThemThuocServlet", urlPatterns = {"/admin/thuoc/them"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 10 * 1024 * 1024,
        maxRequestSize = 20 * 1024 * 1024
)
public class ThemThuocServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        DanhMucDAO danhMucDAO = new DanhMucDAO();
        request.setAttribute("dsDanhMuc", danhMucDAO.getAllDanhMuc());

        request.getRequestDispatcher("/jsp/admin/them_thuoc.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String tenFileAnh = uploadAnh(request);

        Thuoc t = new Thuoc();
        t.setTenThuoc(safeTrim(request.getParameter("tenThuoc")));
        t.setIdDanhMuc(parseInt(request.getParameter("idDanhMuc")));
        t.setGiaGoc(parseMoney(request.getParameter("giaGoc")));
        t.setDonGia(parseMoney(request.getParameter("donGia")));
        t.setPhiShip(parseMoney(request.getParameter("phiShip")));
        t.setSoLuong(parseInt(request.getParameter("soLuong")));
        t.setHinhAnh(tenFileAnh);
        t.setMoTa(safeTrim(request.getParameter("moTa")));
        t.setCongDung(safeTrim(request.getParameter("congDung")));
        t.setCachDung(safeTrim(request.getParameter("cachDung")));
        t.setThanhPhan(safeTrim(request.getParameter("thanhPhan")));
        t.setHanSuDung(request.getParameter("hanSuDung"));
        t.setNhaSanXuat(safeTrim(request.getParameter("nhaSanXuat")));
        t.setTrangThai(request.getParameter("trangThai"));

        ThuocDAO dao = new ThuocDAO();
        dao.insertThuoc(t);

        response.sendRedirect(request.getContextPath() + "/admin/thuoc");
    }

    private String uploadAnh(HttpServletRequest request) throws IOException, ServletException {
        Part filePart = request.getPart("hinhAnhFile");
        return FileUploadUtil.saveImage(filePart);
    }

    private int parseInt(String value) {
        try {
            int number = Integer.parseInt(value);
            return Math.max(number, 0);
        } catch (Exception e) {
            return 0;
        }
    }

    private double parseMoney(String value) {
        try {
            double number = Double.parseDouble(value);
            return Math.max(number, 0);
        } catch (Exception e) {
            return 0;
        }
    }

    private String safeTrim(String value) {
        return value == null ? "" : value.trim();
    }
}