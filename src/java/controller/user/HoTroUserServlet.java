package controller.user;

import dao.HoTroDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.util.List;
import model.HoTroPhien;
import model.HoTroTinNhan;
import model.NguoiDung;
import utils.FileUploadUtil;
import utils.InputSanitizer;
import utils.CsrfUtil;

@WebServlet("/ho-tro")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 10 * 1024 * 1024,
        maxRequestSize = 20 * 1024 * 1024
)
public class HoTroUserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // 👉 tạo CSRF token
        CsrfUtil.getToken(session);

        NguoiDung user = (NguoiDung) session.getAttribute("user");
        HoTroDAO dao = new HoTroDAO();

        List<HoTroPhien> dsPhien = dao.getDanhSachPhienTheoUser(user.getId());

        String idPhienStr = request.getParameter("idPhien");
        HoTroPhien phienChon = null;
        List<HoTroTinNhan> dsTinNhan = null;

        if (idPhienStr != null && !idPhienStr.trim().isEmpty()) {
            try {
                int idPhien = Integer.parseInt(idPhienStr);
                phienChon = dao.getPhienById(idPhien);

                if (phienChon != null && phienChon.getIdNguoiDung() == user.getId()) {
                    dao.markAsReadByUser(idPhien);
                    dsTinNhan = dao.getTinNhanTheoPhien(idPhien);
                } else {
                    phienChon = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        request.setAttribute("dsPhien", dsPhien);
        request.setAttribute("phienChon", phienChon);
        request.setAttribute("dsTinNhan", dsTinNhan);

        request.getRequestDispatcher("/jsp/user/hotro.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // ✅ CHECK CSRF
        if (!CsrfUtil.isValid(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "CSRF không hợp lệ");
            return;
        }

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        NguoiDung user = (NguoiDung) session.getAttribute("user");
        HoTroDAO dao = new HoTroDAO();
        String action = request.getParameter("action");

        // ================= TẠO PHIÊN =================
        if ("taoPhien".equals(action)) {

            String tieuDe = InputSanitizer.cleanPlainText(
                    request.getParameter("tieuDe"), 150);

            String noiDung = InputSanitizer.cleanMultilineText(
                    request.getParameter("noiDung"), 1000);

            if (tieuDe == null || noiDung == null) {
                session.setAttribute("supportMessage", "Vui lòng nhập tiêu đề và nội dung hỗ trợ.");
                response.sendRedirect(request.getContextPath() + "/ho-tro");
                return;
            }

            // chống input độc hại
            if (InputSanitizer.containsHtmlRisk(tieuDe) || InputSanitizer.containsHtmlRisk(noiDung)) {
                session.setAttribute("supportMessage", "Nội dung không hợp lệ.");
                response.sendRedirect(request.getContextPath() + "/ho-tro");
                return;
            }

            int idPhien = dao.taoPhienHoTro(user.getId(), tieuDe, noiDung);

            if (idPhien > 0) {
                session.setAttribute("supportMessage", "Đã gửi yêu cầu hỗ trợ thành công.");
                response.sendRedirect(request.getContextPath() + "/ho-tro?idPhien=" + idPhien);
                return;
            }

            session.setAttribute("supportMessage", "Gửi hỗ trợ thất bại.");
            response.sendRedirect(request.getContextPath() + "/ho-tro");
            return;
        }

        // ================= GỬI TIN NHẮN =================
        if ("guiTinNhan".equals(action)) {

            String idPhienStr = request.getParameter("idPhien");

            String noiDung = InputSanitizer.cleanMultilineText(
                    request.getParameter("noiDung"), 1000);

            try {
                int idPhien = Integer.parseInt(idPhienStr);
                HoTroPhien phien = dao.getPhienById(idPhien);

                if (phien == null || phien.getIdNguoiDung() != user.getId()) {
                    response.sendRedirect(request.getContextPath() + "/ho-tro");
                    return;
                }

                if ("DA_XU_LY".equals(phien.getTrangThai())) {
                    session.setAttribute("supportMessage", "Phiên hỗ trợ này đã đóng.");
                    response.sendRedirect(request.getContextPath() + "/ho-tro?idPhien=" + idPhien);
                    return;
                }

                if (noiDung != null && InputSanitizer.containsHtmlRisk(noiDung)) {
                    session.setAttribute("supportMessage", "Nội dung không hợp lệ.");
                    response.sendRedirect(request.getContextPath() + "/ho-tro?idPhien=" + idPhien);
                    return;
                }

                Part filePart = request.getPart("anhDinhKem");
                String tenAnh = FileUploadUtil.saveImage(filePart);

                boolean coNoiDung = noiDung != null && !noiDung.isEmpty();
                boolean coAnh = tenAnh != null && !tenAnh.isEmpty();

                if (coNoiDung || coAnh) {

                    boolean ok = dao.themTinNhan(
                            idPhien,
                            user.getId(),
                            "USER",
                            coNoiDung ? noiDung : null,
                            coAnh ? tenAnh : null
                    );

                    if (!ok) {
                        session.setAttribute("supportMessage", "Gửi tin nhắn thất bại.");
                    }

                } else {
                    session.setAttribute("supportMessage", "Vui lòng nhập nội dung hoặc chọn ảnh.");
                }

                response.sendRedirect(request.getContextPath() + "/ho-tro?idPhien=" + idPhien);
                return;

            } catch (Exception e) {
                e.printStackTrace();
                session.setAttribute("supportMessage", "Có lỗi xảy ra khi gửi tin nhắn.");
            }
        }

        response.sendRedirect(request.getContextPath() + "/ho-tro");
    }
}