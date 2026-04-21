package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.ThongKeKhachHang;
import model.ThongKeNgay;
import model.ThongKeSanPham;
import utils.DBConnection;

public class ThongKeDAO {

    private String buildDateCondition(String tuNgay, String denNgay, String aliasNgayDat) {
        StringBuilder sql = new StringBuilder(" WHERE 1=1 ");

        if (tuNgay != null && !tuNgay.trim().isEmpty()) {
            sql.append(" AND DATE(").append(aliasNgayDat).append(") >= ? ");
        }

        if (denNgay != null && !denNgay.trim().isEmpty()) {
            sql.append(" AND DATE(").append(aliasNgayDat).append(") <= ? ");
        }

        return sql.toString();
    }

    private int setDateParams(PreparedStatement ps, String tuNgay, String denNgay, int startIndex) throws Exception {
        int index = startIndex;

        if (tuNgay != null && !tuNgay.trim().isEmpty()) {
            ps.setString(index++, tuNgay);
        }

        if (denNgay != null && !denNgay.trim().isEmpty()) {
            ps.setString(index++, denNgay);
        }

        return index;
    }

    public double getTongDoanhThu(String tuNgay, String denNgay) {
        String sql = "SELECT COALESCE(SUM(dh.tongTien), 0) AS tongDoanhThu "
                + "FROM donhang dh "
                + buildDateCondition(tuNgay, denNgay, "dh.ngayDat")
                + " AND dh.trangThai = 'DA_GIAO' "
                + " AND dh.trangThaiThanhToan = 'DA_THANH_TOAN'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            setDateParams(ps, tuNgay, denNgay, 1);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("tongDoanhThu");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public double getTongGiaVon(String tuNgay, String denNgay) {
        String sql = "SELECT COALESCE(SUM(ct.soLuong * t.giaGoc), 0) AS tongGiaVon "
                + "FROM chitietdonhang ct "
                + "JOIN donhang dh ON ct.idDonHang = dh.idDonHang "
                + "JOIN thuoc t ON ct.idThuoc = t.idThuoc "
                + buildDateCondition(tuNgay, denNgay, "dh.ngayDat")
                + " AND dh.trangThai = 'DA_GIAO' "
                + " AND dh.trangThaiThanhToan = 'DA_THANH_TOAN'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            setDateParams(ps, tuNgay, denNgay, 1);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("tongGiaVon");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public double getTongLoiNhuan(String tuNgay, String denNgay) {
        String sql = "SELECT COALESCE(SUM(ct.soLuong * (t.donGia - t.giaGoc)), 0) AS tongLoiNhuan "
                + "FROM chitietdonhang ct "
                + "JOIN donhang dh ON ct.idDonHang = dh.idDonHang "
                + "JOIN thuoc t ON ct.idThuoc = t.idThuoc "
                + buildDateCondition(tuNgay, denNgay, "dh.ngayDat")
                + " AND dh.trangThai = 'DA_GIAO' "
                + " AND dh.trangThaiThanhToan = 'DA_THANH_TOAN'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            setDateParams(ps, tuNgay, denNgay, 1);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("tongLoiNhuan");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int getTongDonHang(String tuNgay, String denNgay) {
        String sql = "SELECT COUNT(*) AS tongDonHang "
                + "FROM donhang dh "
                + buildDateCondition(tuNgay, denNgay, "dh.ngayDat");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            setDateParams(ps, tuNgay, denNgay, 1);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("tongDonHang");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int getTongDonHangTheoTrangThai(String tuNgay, String denNgay, String trangThai) {
        String sql = "SELECT COUNT(*) AS tongTrangThai "
                + "FROM donhang dh "
                + buildDateCondition(tuNgay, denNgay, "dh.ngayDat")
                + " AND dh.trangThai = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int index = setDateParams(ps, tuNgay, denNgay, 1);
            ps.setString(index, trangThai);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("tongTrangThai");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int getTongDonHangHoanThanhThucTe(String tuNgay, String denNgay) {
        String sql = "SELECT COUNT(*) AS tongHoanThanh "
                + "FROM donhang dh "
                + buildDateCondition(tuNgay, denNgay, "dh.ngayDat")
                + " AND dh.trangThai = 'DA_GIAO' "
                + " AND dh.trangThaiThanhToan = 'DA_THANH_TOAN'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            setDateParams(ps, tuNgay, denNgay, 1);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("tongHoanThanh");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public List<ThongKeNgay> getDoanhThuTheoNgay(String tuNgay, String denNgay) {
        List<ThongKeNgay> list = new ArrayList<>();

        String sql = "SELECT DATE(dh.ngayDat) AS ngay, COALESCE(SUM(dh.tongTien), 0) AS doanhThu "
                + "FROM donhang dh "
                + buildDateCondition(tuNgay, denNgay, "dh.ngayDat")
                + " AND dh.trangThai = 'DA_GIAO' "
                + " AND dh.trangThaiThanhToan = 'DA_THANH_TOAN' "
                + "GROUP BY DATE(dh.ngayDat) "
                + "ORDER BY DATE(dh.ngayDat) ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            setDateParams(ps, tuNgay, denNgay, 1);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ThongKeNgay tk = new ThongKeNgay();
                    tk.setNhan(rs.getString("ngay"));
                    tk.setGiaTri(rs.getDouble("doanhThu"));
                    list.add(tk);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<ThongKeNgay> getDoanhThuTheoTuan(String tuNgay, String denNgay) {
        List<ThongKeNgay> list = new ArrayList<>();

        String sql = "SELECT YEAR(dh.ngayDat) AS nam, WEEK(dh.ngayDat, 1) AS tuan, "
                + "COALESCE(SUM(dh.tongTien), 0) AS doanhThu "
                + "FROM donhang dh "
                + buildDateCondition(tuNgay, denNgay, "dh.ngayDat")
                + " AND dh.trangThai = 'DA_GIAO' "
                + " AND dh.trangThaiThanhToan = 'DA_THANH_TOAN' "
                + "GROUP BY YEAR(dh.ngayDat), WEEK(dh.ngayDat, 1) "
                + "ORDER BY YEAR(dh.ngayDat) ASC, WEEK(dh.ngayDat, 1) ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            setDateParams(ps, tuNgay, denNgay, 1);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ThongKeNgay tk = new ThongKeNgay();
                    tk.setNhan("Tuần " + rs.getInt("tuan") + "/" + rs.getInt("nam"));
                    tk.setGiaTri(rs.getDouble("doanhThu"));
                    list.add(tk);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<ThongKeNgay> getDoanhThuTheoThang(String tuNgay, String denNgay) {
        List<ThongKeNgay> list = new ArrayList<>();

        String sql = "SELECT DATE_FORMAT(dh.ngayDat, '%Y-%m') AS thang, COALESCE(SUM(dh.tongTien), 0) AS doanhThu "
                + "FROM donhang dh "
                + buildDateCondition(tuNgay, denNgay, "dh.ngayDat")
                + " AND dh.trangThai = 'DA_GIAO' "
                + " AND dh.trangThaiThanhToan = 'DA_THANH_TOAN' "
                + "GROUP BY DATE_FORMAT(dh.ngayDat, '%Y-%m') "
                + "ORDER BY DATE_FORMAT(dh.ngayDat, '%Y-%m') ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            setDateParams(ps, tuNgay, denNgay, 1);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ThongKeNgay tk = new ThongKeNgay();
                    tk.setNhan(rs.getString("thang"));
                    tk.setGiaTri(rs.getDouble("doanhThu"));
                    list.add(tk);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<ThongKeNgay> getLoiNhuanTheoNgay(String tuNgay, String denNgay) {
        List<ThongKeNgay> list = new ArrayList<>();

        String sql = "SELECT DATE(dh.ngayDat) AS ngay, "
                + "COALESCE(SUM(ct.soLuong * (t.donGia - t.giaGoc)), 0) AS loiNhuan "
                + "FROM chitietdonhang ct "
                + "JOIN donhang dh ON ct.idDonHang = dh.idDonHang "
                + "JOIN thuoc t ON ct.idThuoc = t.idThuoc "
                + buildDateCondition(tuNgay, denNgay, "dh.ngayDat")
                + " AND dh.trangThai = 'DA_GIAO' "
                + " AND dh.trangThaiThanhToan = 'DA_THANH_TOAN' "
                + "GROUP BY DATE(dh.ngayDat) "
                + "ORDER BY DATE(dh.ngayDat) ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            setDateParams(ps, tuNgay, denNgay, 1);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ThongKeNgay tk = new ThongKeNgay();
                    tk.setNhan(rs.getString("ngay"));
                    tk.setGiaTri(rs.getDouble("loiNhuan"));
                    list.add(tk);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<ThongKeNgay> getLoiNhuanTheoTuan(String tuNgay, String denNgay) {
        List<ThongKeNgay> list = new ArrayList<>();

        String sql = "SELECT YEAR(dh.ngayDat) AS nam, WEEK(dh.ngayDat, 1) AS tuan, "
                + "COALESCE(SUM(ct.soLuong * (t.donGia - t.giaGoc)), 0) AS loiNhuan "
                + "FROM chitietdonhang ct "
                + "JOIN donhang dh ON ct.idDonHang = dh.idDonHang "
                + "JOIN thuoc t ON ct.idThuoc = t.idThuoc "
                + buildDateCondition(tuNgay, denNgay, "dh.ngayDat")
                + " AND dh.trangThai = 'DA_GIAO' "
                + " AND dh.trangThaiThanhToan = 'DA_THANH_TOAN' "
                + "GROUP BY YEAR(dh.ngayDat), WEEK(dh.ngayDat, 1) "
                + "ORDER BY YEAR(dh.ngayDat) ASC, WEEK(dh.ngayDat, 1) ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            setDateParams(ps, tuNgay, denNgay, 1);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ThongKeNgay tk = new ThongKeNgay();
                    tk.setNhan("Tuần " + rs.getInt("tuan") + "/" + rs.getInt("nam"));
                    tk.setGiaTri(rs.getDouble("loiNhuan"));
                    list.add(tk);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<ThongKeNgay> getLoiNhuanTheoThang(String tuNgay, String denNgay) {
        List<ThongKeNgay> list = new ArrayList<>();

        String sql = "SELECT DATE_FORMAT(dh.ngayDat, '%Y-%m') AS thang, "
                + "COALESCE(SUM(ct.soLuong * (t.donGia - t.giaGoc)), 0) AS loiNhuan "
                + "FROM chitietdonhang ct "
                + "JOIN donhang dh ON ct.idDonHang = dh.idDonHang "
                + "JOIN thuoc t ON ct.idThuoc = t.idThuoc "
                + buildDateCondition(tuNgay, denNgay, "dh.ngayDat")
                + " AND dh.trangThai = 'DA_GIAO' "
                + " AND dh.trangThaiThanhToan = 'DA_THANH_TOAN' "
                + "GROUP BY DATE_FORMAT(dh.ngayDat, '%Y-%m') "
                + "ORDER BY DATE_FORMAT(dh.ngayDat, '%Y-%m') ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            setDateParams(ps, tuNgay, denNgay, 1);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ThongKeNgay tk = new ThongKeNgay();
                    tk.setNhan(rs.getString("thang"));
                    tk.setGiaTri(rs.getDouble("loiNhuan"));
                    list.add(tk);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<ThongKeSanPham> getTopSanPhamBanChay(String tuNgay, String denNgay, int limit) {
        List<ThongKeSanPham> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT t.idThuoc, t.tenThuoc, t.hinhAnh, ");
        sql.append("SUM(ct.soLuong) AS tongSoLuongBan, ");
        sql.append("SUM(ct.thanhTien) AS tongDoanhThu ");
        sql.append("FROM chitietdonhang ct ");
        sql.append("JOIN donhang dh ON ct.idDonHang = dh.idDonHang ");
        sql.append("JOIN thuoc t ON ct.idThuoc = t.idThuoc ");
        sql.append("WHERE dh.trangThai = 'DA_GIAO' ");
        sql.append("AND dh.trangThaiThanhToan = 'DA_THANH_TOAN' ");

        if (tuNgay != null && !tuNgay.trim().isEmpty()) {
            sql.append("AND DATE(dh.ngayDat) >= ? ");
        }

        if (denNgay != null && !denNgay.trim().isEmpty()) {
            sql.append("AND DATE(dh.ngayDat) <= ? ");
        }

        sql.append("GROUP BY t.idThuoc, t.tenThuoc, t.hinhAnh ");
        sql.append("ORDER BY tongSoLuongBan DESC, tongDoanhThu DESC ");
        sql.append("LIMIT ?");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;

            if (tuNgay != null && !tuNgay.trim().isEmpty()) {
                ps.setString(index++, tuNgay);
            }

            if (denNgay != null && !denNgay.trim().isEmpty()) {
                ps.setString(index++, denNgay);
            }

            ps.setInt(index, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ThongKeSanPham sp = new ThongKeSanPham();
                    sp.setIdThuoc(rs.getInt("idThuoc"));
                    sp.setTenThuoc(rs.getString("tenThuoc"));
                    sp.setHinhAnh(rs.getString("hinhAnh"));
                    sp.setTongSoLuongBan(rs.getInt("tongSoLuongBan"));
                    sp.setTongDoanhThu(rs.getDouble("tongDoanhThu"));
                    list.add(sp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<ThongKeKhachHang> getTopKhachHangMuaNhieu(String tuNgay, String denNgay, int limit) {
        List<ThongKeKhachHang> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT nd.id AS idNguoiDung, nd.hoTen, nd.email, ");
        sql.append("COUNT(dh.idDonHang) AS tongDonHang, ");
        sql.append("COALESCE(SUM(dh.tongTien), 0) AS tongChiTieu ");
        sql.append("FROM donhang dh ");
        sql.append("JOIN nguoidung nd ON dh.idNguoiDung = nd.id ");
        sql.append("WHERE dh.trangThai = 'DA_GIAO' ");
        sql.append("AND dh.trangThaiThanhToan = 'DA_THANH_TOAN' ");

        if (tuNgay != null && !tuNgay.trim().isEmpty()) {
            sql.append("AND DATE(dh.ngayDat) >= ? ");
        }

        if (denNgay != null && !denNgay.trim().isEmpty()) {
            sql.append("AND DATE(dh.ngayDat) <= ? ");
        }

        sql.append("GROUP BY nd.id, nd.hoTen, nd.email ");
        sql.append("ORDER BY tongChiTieu DESC, tongDonHang DESC ");
        sql.append("LIMIT ?");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;

            if (tuNgay != null && !tuNgay.trim().isEmpty()) {
                ps.setString(index++, tuNgay);
            }

            if (denNgay != null && !denNgay.trim().isEmpty()) {
                ps.setString(index++, denNgay);
            }

            ps.setInt(index, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ThongKeKhachHang kh = new ThongKeKhachHang();
                    kh.setIdNguoiDung(rs.getInt("idNguoiDung"));
                    kh.setHoTen(rs.getString("hoTen"));
                    kh.setEmail(rs.getString("email"));
                    kh.setTongDonHang(rs.getInt("tongDonHang"));
                    kh.setTongChiTieu(rs.getDouble("tongChiTieu"));
                    list.add(kh);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}