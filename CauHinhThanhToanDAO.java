package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.CauHinhThanhToan;
import utils.DBConnection;

public class CauHinhThanhToanDAO {

    public CauHinhThanhToan getCauHinh() {
        String sql = "SELECT * FROM cauhinh_thanhtoan WHERE id = 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                CauHinhThanhToan c = new CauHinhThanhToan();

                c.setId(rs.getInt("id"));

                c.setTenNganHangQr(rs.getString("tenNganHangQr"));
                c.setSoTaiKhoanQr(rs.getString("soTaiKhoanQr"));
                c.setChuTaiKhoanQr(rs.getString("chuTaiKhoanQr"));
                c.setAnhQR(rs.getString("anhQR"));

                c.setTenNganHangThe(rs.getString("tenNganHangThe"));
                c.setSoTaiKhoanThe(rs.getString("soTaiKhoanThe"));
                c.setChiNhanhThe(rs.getString("chiNhanhThe"));
                c.setChuThe(rs.getString("chuThe"));
                c.setAnhThe(rs.getString("anhThe"));

                c.setBatCOD(rs.getBoolean("batCOD"));
                c.setBatChuyenKhoan(rs.getBoolean("batChuyenKhoan"));
                c.setBatThe(rs.getBoolean("batThe"));

                c.setNgayCapNhat(rs.getTimestamp("ngayCapNhat"));

                return c;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean capNhat(CauHinhThanhToan c) {
        String sql = "UPDATE cauhinh_thanhtoan SET "
                + "tenNganHangQr = ?, "
                + "soTaiKhoanQr = ?, "
                + "chuTaiKhoanQr = ?, "
                + "anhQR = ?, "
                + "tenNganHangThe = ?, "
                + "soTaiKhoanThe = ?, "
                + "chiNhanhThe = ?, "
                + "chuThe = ?, "
                + "anhThe = ?, "
                + "batCOD = ?, "
                + "batChuyenKhoan = ?, "
                + "batThe = ? "
                + "WHERE id = 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getTenNganHangQr());
            ps.setString(2, c.getSoTaiKhoanQr());
            ps.setString(3, c.getChuTaiKhoanQr());
            ps.setString(4, c.getAnhQR());

            ps.setString(5, c.getTenNganHangThe());
            ps.setString(6, c.getSoTaiKhoanThe());
            ps.setString(7, c.getChiNhanhThe());
            ps.setString(8, c.getChuThe());
            ps.setString(9, c.getAnhThe());

            ps.setBoolean(10, c.isBatCOD());
            ps.setBoolean(11, c.isBatChuyenKhoan());
            ps.setBoolean(12, c.isBatThe());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}