package controller.staff;

import dao.ThuocDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/staff/cap-nhat-kho")
public class StaffCapNhatKhoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();

        try {
            int idThuoc = Integer.parseInt(request.getParameter("idThuoc"));
            int soLuongNhap = Integer.parseInt(request.getParameter("soLuongNhap"));

            if (soLuongNhap <= 0) {
                session.setAttribute("staffKhoMessage", "Số lượng nhập thêm phải lớn hơn 0.");
                response.sendRedirect(request.getContextPath() + "/staff/kho");
                return;
            }

            ThuocDAO thuocDAO = new ThuocDAO();
            boolean ok = thuocDAO.nhapThemSoLuongThuoc(idThuoc, soLuongNhap);

            if (ok) {
                session.setAttribute("staffKhoMessage", "Cập nhật kho thành công.");
            } else {
                session.setAttribute("staffKhoMessage", "Cập nhật kho thất bại.");
            }

        } catch (Exception e) {
            session.setAttribute("staffKhoMessage", "Dữ liệu không hợp lệ.");
        }

        response.sendRedirect(request.getContextPath() + "/staff/kho");
    }
}