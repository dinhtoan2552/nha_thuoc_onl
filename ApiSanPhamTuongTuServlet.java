package api.user;

import dao.ThuocDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import model.Thuoc;

@WebServlet("/api/user/san-pham-tuong-tu")
public class ApiSanPhamTuongTuServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");

        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.getWriter().write("[]");
            return;
        }

        try {
            int idThuoc = Integer.parseInt(idStr);

            ThuocDAO thuocDAO = new ThuocDAO();
            Thuoc thuocHienTai = thuocDAO.getThuocById(idThuoc);

            if (thuocHienTai == null) {
                response.getWriter().write("[]");
                return;
            }

            List<Thuoc> dsTuongTu = thuocDAO.getSanPhamTuongTuChoUser(
                    thuocHienTai.getIdThuoc(),
                    thuocHienTai.getIdDanhMuc(),
                    thuocHienTai.getCongDung(),
                    6
            );

            StringBuilder json = new StringBuilder();
            json.append("[");

            for (int i = 0; i < dsTuongTu.size(); i++) {
                Thuoc t = dsTuongTu.get(i);

                if (i > 0) {
                    json.append(",");
                }

                json.append("{")
                    .append("\"idThuoc\":").append(t.getIdThuoc()).append(",")
                    .append("\"tenThuoc\":\"").append(escapeJson(t.getTenThuoc())).append("\",")
                    .append("\"hinhAnh\":\"").append(escapeJson(t.getHinhAnh())).append("\",")
                    .append("\"donGia\":").append(t.getDonGia()).append(",")
                    .append("\"moTa\":\"").append(escapeJson(t.getMoTa())).append("\"")
                    .append("}");
            }

            json.append("]");
            response.getWriter().write(json.toString());

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("[]");
        }
    }

    private String escapeJson(String text) {
        if (text == null) {
            return "";
        }

        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", " ")
                   .replace("\r", " ");
    }
}