package controller.admin;

import dao.ThongKeDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import model.ThongKeKhachHang;
import model.ThongKeNgay;
import model.ThongKeSanPham;

@WebServlet("/admin/thongke")
public class AdminThongKeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String tuNgay = request.getParameter("tuNgay");
        String denNgay = request.getParameter("denNgay");
        String quickRange = request.getParameter("quickRange");

        LocalDate today = LocalDate.now();

        if (quickRange != null && !quickRange.trim().isEmpty()) {
            switch (quickRange) {
                case "today":
                    tuNgay = today.toString();
                    denNgay = today.toString();
                    break;
                case "7days":
                    tuNgay = today.minusDays(6).toString();
                    denNgay = today.toString();
                    break;
                case "30days":
                    tuNgay = today.minusDays(29).toString();
                    denNgay = today.toString();
                    break;
                default:
                    break;
            }
        }

        if ((tuNgay == null || tuNgay.trim().isEmpty()) && (denNgay == null || denNgay.trim().isEmpty())) {
            tuNgay = today.minusDays(6).toString();
            denNgay = today.toString();
            quickRange = "7days";
        }

        ThongKeDAO dao = new ThongKeDAO();

        double tongDoanhThu = dao.getTongDoanhThu(tuNgay, denNgay);
        double tongGiaVon = dao.getTongGiaVon(tuNgay, denNgay);
        double tongLoiNhuan = dao.getTongLoiNhuan(tuNgay, denNgay);

        int tongDonHang = dao.getTongDonHang(tuNgay, denNgay);

        // Đơn hoàn thành thực tế = đã giao và đã thanh toán
        int hoanThanh = dao.getTongDonHangHoanThanhThucTe(tuNgay, denNgay);

        int choXacNhan = dao.getTongDonHangTheoTrangThai(tuNgay, denNgay, "CHO_XAC_NHAN");
        int dangGiao = dao.getTongDonHangTheoTrangThai(tuNgay, denNgay, "DANG_GIAO");
        int daHuy = dao.getTongDonHangTheoTrangThai(tuNgay, denNgay, "DA_HUY");

        List<ThongKeNgay> doanhThuTheoNgay = dao.getDoanhThuTheoNgay(tuNgay, denNgay);
        List<ThongKeNgay> doanhThuTheoTuan = dao.getDoanhThuTheoTuan(tuNgay, denNgay);
        List<ThongKeNgay> doanhThuTheoThang = dao.getDoanhThuTheoThang(tuNgay, denNgay);

        List<ThongKeNgay> loiNhuanTheoNgay = dao.getLoiNhuanTheoNgay(tuNgay, denNgay);
        List<ThongKeNgay> loiNhuanTheoTuan = dao.getLoiNhuanTheoTuan(tuNgay, denNgay);
        List<ThongKeNgay> loiNhuanTheoThang = dao.getLoiNhuanTheoThang(tuNgay, denNgay);

        List<ThongKeSanPham> topSanPham = dao.getTopSanPhamBanChay(tuNgay, denNgay, 5);
        List<ThongKeKhachHang> topKhachHang = dao.getTopKhachHangMuaNhieu(tuNgay, denNgay, 5);

        request.setAttribute("tuNgay", tuNgay);
        request.setAttribute("denNgay", denNgay);
        request.setAttribute("quickRange", quickRange == null ? "" : quickRange);

        request.setAttribute("tongDoanhThu", tongDoanhThu);
        request.setAttribute("tongGiaVon", tongGiaVon);
        request.setAttribute("tongLoiNhuan", tongLoiNhuan);

        request.setAttribute("tongDonHang", tongDonHang);
        request.setAttribute("hoanThanh", hoanThanh);
        request.setAttribute("choXacNhan", choXacNhan);
        request.setAttribute("dangGiao", dangGiao);
        request.setAttribute("daHuy", daHuy);

        request.setAttribute("doanhThuTheoNgay", doanhThuTheoNgay);
        request.setAttribute("doanhThuTheoTuan", doanhThuTheoTuan);
        request.setAttribute("doanhThuTheoThang", doanhThuTheoThang);

        request.setAttribute("loiNhuanTheoNgay", loiNhuanTheoNgay);
        request.setAttribute("loiNhuanTheoTuan", loiNhuanTheoTuan);
        request.setAttribute("loiNhuanTheoThang", loiNhuanTheoThang);

        request.setAttribute("topSanPham", topSanPham);
        request.setAttribute("topKhachHang", topKhachHang);

        request.getRequestDispatcher("/jsp/admin/thongke.jsp").forward(request, response);
    }
}