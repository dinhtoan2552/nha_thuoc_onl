package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.DanhMuc;
import utils.DBConnection;

public class DanhMucDAO {

    public List<DanhMuc> getAllDanhMuc() {
    List<DanhMuc> list = new ArrayList<>();
    String sql = "SELECT * FROM danhmuc ORDER BY tenDanhMuc ASC";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            DanhMuc dm = new DanhMuc();
            dm.setIdDanhMuc(rs.getInt("idDanhMuc"));
            dm.setTenDanhMuc(rs.getString("tenDanhMuc"));
            dm.setMoTa(rs.getString("moTa"));
            list.add(dm);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}

    public DanhMuc getDanhMucById(int id) {
        String sql = "SELECT idDanhMuc, tenDanhMuc, moTa FROM danhmuc WHERE idDanhMuc = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DanhMuc dm = new DanhMuc();
                    dm.setIdDanhMuc(rs.getInt("idDanhMuc"));
                    dm.setTenDanhMuc(rs.getString("tenDanhMuc"));
                    dm.setMoTa(rs.getString("moTa"));
                    return dm;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean insertDanhMuc(DanhMuc dm) {
        String sql = "INSERT INTO danhmuc (tenDanhMuc, moTa) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dm.getTenDanhMuc());
            ps.setString(2, dm.getMoTa());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateDanhMuc(DanhMuc dm) {
        String sql = "UPDATE danhmuc SET tenDanhMuc = ?, moTa = ? WHERE idDanhMuc = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dm.getTenDanhMuc());
            ps.setString(2, dm.getMoTa());
            ps.setInt(3, dm.getIdDanhMuc());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteDanhMuc(int id) {
        String sql = "DELETE FROM danhmuc WHERE idDanhMuc = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}