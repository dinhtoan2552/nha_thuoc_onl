package controller.staff;

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

@WebServlet("/staff/hotro")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 10 * 1024 * 1024,
        maxRequestSize = 20 * 1024 * 1024
)
public class HoTroStaffServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        NguoiDung staff = (NguoiDung) session.getAttribute("user");
        if (!"STAFF".equalsIgnoreCase(staff.getVaiTro())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        HoTroDAO dao = new HoTroDAO();
        List<HoTroPhien> dsPhien = dao.getTatCaPhien();

        String idPhienStr = request.getParameter("idPhien");
        HoTroPhien phienChon = null;
        List<HoTroTinNhan> dsTinNhan = null;

        if (idPhienStr != null && !idPhienStr.trim().isEmpty()) {
            try {
                int idPhien = Integer.parseInt(idPhienStr);
                phienChon = dao.getPhienById(idPhien);

                if (phienChon != null) {
                    dao.markAsReadByStaff(idPhien);
                    dsTinNhan = dao.getTinNhanTheoPhien(idPhien);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        request.setAttribute("dsPhien", dsPhien);
        request.setAttribute("phienChon", phienChon);
        request.setAttribute("dsTinNhan", dsTinNhan);

        request.getRequestDispatcher("/jsp/staff/hotro.jsp").forward(request, response);
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

        NguoiDung staff = (NguoiDung) session.getAttribute("user");
        if (!"STAFF".equalsIgnoreCase(staff.getVaiTro())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        HoTroDAO dao = new HoTroDAO();
        String action = request.getParameter("action");

        try {
            String idPhienStr = request.getParameter("idPhien");
            int idPhien = Integer.parseInt(idPhienStr);

            HoTroPhien phien = dao.getPhienById(idPhien);
            if (phien == null) {
                response.sendRedirect(request.getContextPath() + "/staff/hotro");
                return;
            }

            if ("guiTinNhan".equals(action)) {
                if ("DA_XU_LY".equals(phien.getTrangThai())) {
                    response.sendRedirect(request.getContextPath() + "/staff/hotro?idPhien=" + idPhien);
                    return;
                }

                String noiDung = request.getParameter("noiDung");
                if (noiDung != null) {
                    noiDung = noiDung.trim();
                }

                Part filePart = request.getPart("anhDinhKem");
                String tenAnh = FileUploadUtil.saveImage(filePart);

                boolean coNoiDung = noiDung != null && !noiDung.isEmpty();
                boolean coAnh = tenAnh != null && !tenAnh.isEmpty();

                System.out.println("=== STAFF GUI TIN NHAN ===");
                System.out.println("idPhien = " + idPhien);
                System.out.println("noiDung = " + noiDung);
                System.out.println("tenAnh = " + tenAnh);
                System.out.println("coNoiDung = " + coNoiDung);
                System.out.println("coAnh = " + coAnh);

                if (coNoiDung || coAnh) {
                    boolean ok = dao.themTinNhan(
                            idPhien,
                            staff.getId(),
                            "STAFF",
                            coNoiDung ? noiDung : null,
                            coAnh ? tenAnh : null
                    );

                    System.out.println("dao.themTinNhan = " + ok);

                    if (ok) {
                       dao.markAsReadByStaff(idPhien);

                        if (phien.getIdNhanVienXuLy() == null) {
                            dao.capNhatTrangThaiPhien(idPhien, "DANG_MO", staff.getId());
                        }
                    }
                }

                response.sendRedirect(request.getContextPath() + "/staff/hotro?idPhien=" + idPhien);
                return;
            }

            if ("doiTrangThai".equals(action)) {
                String trangThai = request.getParameter("trangThai");
                dao.capNhatTrangThaiPhien(idPhien, trangThai, staff.getId());
                dao.markAsReadByStaff(idPhien);

                response.sendRedirect(request.getContextPath() + "/staff/hotro?idPhien=" + idPhien);
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect(request.getContextPath() + "/staff/hotro");
    }
}