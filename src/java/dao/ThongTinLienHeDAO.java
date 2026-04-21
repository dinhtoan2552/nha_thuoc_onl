package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.ThongTinLienHe;
import utils.DBConnection;

public class ThongTinLienHeDAO {

    public ThongTinLienHe getThongTinLienHe() {
        String sql = "SELECT * FROM thongtinlienhe WHERE trangThai = 'ACTIVE' ORDER BY id ASC LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                ThongTinLienHe t = new ThongTinLienHe();
                t.setId(rs.getInt("id"));
                t.setSoZalo(rs.getString("soZalo"));
                t.setLinkMessenger(rs.getString("linkMessenger"));
                t.setSoHotline(rs.getString("soHotline"));

                // thêm mới
                try {
                    t.setDiaChiNhaThuoc(rs.getString("diaChiNhaThuoc"));
                } catch (Exception e) {
                }

                try {
                    t.setNoiDungBanner(rs.getString("noiDungBanner"));
                } catch (Exception e) {
                }

                t.setTrangThai(rs.getString("trangThai"));
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean updateThongTinLienHe(ThongTinLienHe t) {
        String sql = "UPDATE thongtinlienhe "
                + "SET soZalo = ?, linkMessenger = ?, soHotline = ?, diaChiNhaThuoc = ?, noiDungBanner = ? "
                + "WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, t.getSoZalo());
            ps.setString(2, t.getLinkMessenger());
            ps.setString(3, t.getSoHotline());
            ps.setString(4, t.getDiaChiNhaThuoc());
            ps.setString(5, t.getNoiDungBanner());
            ps.setInt(6, t.getId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean insertThongTinLienHe(ThongTinLienHe t) {
        String sql = "INSERT INTO thongtinlienhe "
                + "(soZalo, linkMessenger, soHotline, diaChiNhaThuoc, noiDungBanner, trangThai) "
                + "VALUES (?, ?, ?, ?, ?, 'ACTIVE')";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, t.getSoZalo());
            ps.setString(2, t.getLinkMessenger());
            ps.setString(3, t.getSoHotline());
            ps.setString(4, t.getDiaChiNhaThuoc());
            ps.setString(5, t.getNoiDungBanner());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean saveOrUpdate(ThongTinLienHe t) {
        ThongTinLienHe current = getThongTinLienHe();

        if (current == null) {
            return insertThongTinLienHe(t);
        }

        t.setId(current.getId());
        return updateThongTinLienHe(t);
    }
}