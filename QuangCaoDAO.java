package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.QuangCao;
import utils.DBConnection;

public class QuangCaoDAO {

    public boolean themQuangCao(String tieuDe, String hinhAnh, String linkUrl, int thuTu, boolean trangThai) {
        String sql = "INSERT INTO quangcao (tieuDe, hinhAnh, linkUrl, thuTu, trangThai) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tieuDe);
            ps.setString(2, hinhAnh);
            ps.setString(3, linkUrl);
            ps.setInt(4, thuTu);
            ps.setBoolean(5, trangThai);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<QuangCao> getTatCaQuangCao() {
        List<QuangCao> list = new ArrayList<>();
        String sql = "SELECT * FROM quangcao ORDER BY thuTu ASC, idQuangCao DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<QuangCao> getQuangCaoDangBat() {
        List<QuangCao> list = new ArrayList<>();
        String sql = "SELECT * FROM quangcao WHERE trangThai = 1 ORDER BY thuTu ASC, idQuangCao DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public QuangCao getById(int idQuangCao) {
        String sql = "SELECT * FROM quangcao WHERE idQuangCao = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idQuangCao);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean capNhatQuangCao(int idQuangCao, String tieuDe, String linkUrl, int thuTu, boolean trangThai) {
        String sql = "UPDATE quangcao SET tieuDe = ?, linkUrl = ?, thuTu = ?, trangThai = ? WHERE idQuangCao = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tieuDe);
            ps.setString(2, linkUrl);
            ps.setInt(3, thuTu);
            ps.setBoolean(4, trangThai);
            ps.setInt(5, idQuangCao);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean capNhatAnhQuangCao(int idQuangCao, String hinhAnh) {
        String sql = "UPDATE quangcao SET hinhAnh = ? WHERE idQuangCao = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, hinhAnh);
            ps.setInt(2, idQuangCao);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean xoaQuangCao(int idQuangCao) {
        String sql = "DELETE FROM quangcao WHERE idQuangCao = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idQuangCao);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private QuangCao map(ResultSet rs) throws Exception {
        QuangCao qc = new QuangCao();
        qc.setIdQuangCao(rs.getInt("idQuangCao"));
        qc.setTieuDe(rs.getString("tieuDe"));
        qc.setHinhAnh(rs.getString("hinhAnh"));
        qc.setLinkUrl(rs.getString("linkUrl"));
        qc.setThuTu(rs.getInt("thuTu"));
        qc.setTrangThai(rs.getBoolean("trangThai"));
        qc.setNgayTao(rs.getTimestamp("ngayTao"));
        return qc;
    }
}