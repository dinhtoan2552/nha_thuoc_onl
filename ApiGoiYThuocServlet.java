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
import utils.InputSanitizer;

@WebServlet("/api/user/goi-y-thuoc")
public class ApiGoiYThuocServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");

        String q = InputSanitizer.cleanPlainText(request.getParameter("q"), 100);

        if (q == null || q.trim().isEmpty()) {
            response.getWriter().write("[]");
            return;
        }

        ThuocDAO thuocDAO = new ThuocDAO();
        List<Thuoc> dsGoiY = thuocDAO.getGoiYTenThuocChoUser(q.trim(), 8);

        StringBuilder json = new StringBuilder();
        json.append("[");

        for (int i = 0; i < dsGoiY.size(); i++) {
            Thuoc t = dsGoiY.get(i);

            if (i > 0) {
                json.append(",");
            }

            json.append("{")
                .append("\"idThuoc\":").append(t.getIdThuoc()).append(",")
                .append("\"tenThuoc\":\"").append(escapeJson(t.getTenThuoc())).append("\"")
                .append("}");
        }

        json.append("]");

        response.getWriter().write(json.toString());
    }

    private String escapeJson(String text) {
        if (text == null) {
            return "";
        }
        return text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", " ")
                .replace("\r", " ");
    }
}