package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.ThongKeNgay;
import utils.DBConnection;

public class DashboardDAO {

    public int countThuoc() {
        String sql = "SELECT COUNT(*) FROM thuoc";
        return getCount(sql);
    }

    public int countDonHang() {
        String sql = "SELECT COUNT(*) FROM donhang";
        return getCount(sql);
    }

    public int countNguoiDung() {
        String sql = "SELECT COUNT(*) FROM nguoidung WHERE vaiTro = 'USER'";
        return getCount(sql);
    }

    public int countNhanVien() {
        String sql = "SELECT COUNT(*) FROM nguoidung WHERE vaiTro = 'STAFF'";
        return getCount(sql);
    }

    public int countDonChoXacNhan() {
        String sql = "SELECT COUNT(*) FROM donhang WHERE trangThai = 'CHO_XAC_NHAN'";
        return getCount(sql);
    }

    public int countThuocSapHet(int mucCanhBao) {
        String sql = "SELECT COUNT(*) FROM thuoc WHERE soLuong > 0 AND soLuong <= ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, mucCanhBao);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int countThuocHetHang() {
        String sql = "SELECT COUNT(*) FROM thuoc WHERE soLuong <= 0";
        return getCount(sql);
    }

    public int countDonDaHuy() {
        String sql = "SELECT COUNT(*) FROM donhang WHERE trangThai = 'DA_HUY'";
        return getCount(sql);
    }

    public double tongDoanhThu() {
        double tong = 0;
        String sql = "SELECT COALESCE(SUM(tongTien), 0) "
                   + "FROM donhang "
                   + "WHERE trangThai = 'DA_GIAO' "
                   + "AND trangThaiThanhToan = 'DA_THANH_TOAN'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                tong = rs.getDouble(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tong;
    }

    public double tongVonNhapHang() {
        double tong = 0;
        String sql = "SELECT COALESCE(SUM(giaGoc * soLuong), 0) FROM thuoc";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                tong = rs.getDouble(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tong;
    }

    public List<ThongKeNgay> getDoanhThuTheo6ThangGanNhat() {
        List<ThongKeNgay> list = new ArrayList<>();

        String sql = "SELECT DATE_FORMAT(ngayDat, '%Y-%m') AS thang, "
                   + "COALESCE(SUM(tongTien), 0) AS doanhThu "
                   + "FROM donhang "
                   + "WHERE trangThai = 'DA_GIAO' "
                   + "AND trangThaiThanhToan = 'DA_THANH_TOAN' "
                   + "AND ngayDat >= DATE_SUB(CURDATE(), INTERVAL 5 MONTH) "
                   + "GROUP BY DATE_FORMAT(ngayDat, '%Y-%m') "
                   + "ORDER BY DATE_FORMAT(ngayDat, '%Y-%m') ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ThongKeNgay tk = new ThongKeNgay();
                tk.setNhan(rs.getString("thang"));
                tk.setGiaTri(rs.getDouble("doanhThu"));
                list.add(tk);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    private int getCount(String sql) {
        int count = 0;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }
}