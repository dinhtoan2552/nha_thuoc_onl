package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import utils.DBConnection;

public class GioHangDAO {

    public int getOrCreateGioHang(int idNguoiDung) {
        String selectSql = "SELECT idGioHang FROM giohang WHERE idNguoiDung = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(selectSql)) {

            ps.setInt(1, idNguoiDung);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("idGioHang");
            }

            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String insertSql = "INSERT INTO giohang(idNguoiDung) VALUES (?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, idNguoiDung);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }

            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    public boolean daTonTaiTrongGio(int idGioHang, int idThuoc) {
        String sql = "SELECT idChiTiet FROM chitietgiohang WHERE idGioHang = ? AND idThuoc = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idGioHang);
            ps.setInt(2, idThuoc);

            ResultSet rs = ps.executeQuery();
            boolean exists = rs.next();
            rs.close();
            return exists;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean capNhatSoLuong(int idGioHang, int idThuoc, int soLuongCongThem) {
        String sql = "UPDATE chitietgiohang SET soLuong = soLuong + ? WHERE idGioHang = ? AND idThuoc = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, soLuongCongThem);
            ps.setInt(2, idGioHang);
            ps.setInt(3, idThuoc);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean themMoiVaoGio(int idGioHang, int idThuoc, int soLuong, double donGia) {
        String sql = "INSERT INTO chitietgiohang(idGioHang, idThuoc, soLuong, donGia) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idGioHang);
            ps.setInt(2, idThuoc);
            ps.setInt(3, soLuong);
            ps.setDouble(4, donGia);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public java.util.List<model.GioHangItem> getDanhSachGioHang(int idNguoiDung) {
        java.util.List<model.GioHangItem> list = new java.util.ArrayList<>();

        String sql = "SELECT ct.idChiTiet, ct.idThuoc, t.tenThuoc, t.hinhAnh, ct.donGia, ct.soLuong " +
                     "FROM giohang g " +
                     "JOIN chitietgiohang ct ON g.idGioHang = ct.idGioHang " +
                     "JOIN thuoc t ON ct.idThuoc = t.idThuoc " +
                     "WHERE g.idNguoiDung = ? " +
                     "ORDER BY ct.idChiTiet DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idNguoiDung);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.GioHangItem item = new model.GioHangItem();
                item.setIdChiTiet(rs.getInt("idChiTiet"));
                item.setIdThuoc(rs.getInt("idThuoc"));
                item.setTenThuoc(rs.getString("tenThuoc"));
                item.setHinhAnh(rs.getString("hinhAnh"));
                item.setDonGia(rs.getDouble("donGia"));
                item.setSoLuong(rs.getInt("soLuong"));
                item.setThanhTien(item.getDonGia() * item.getSoLuong());
                list.add(item);
            }

            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public double tinhTongTienGioHang(int idNguoiDung) {
        double tongTien = 0;

        String sql = "SELECT SUM(ct.donGia * ct.soLuong) AS tongTien " +
                     "FROM giohang g " +
                     "JOIN chitietgiohang ct ON g.idGioHang = ct.idGioHang " +
                     "WHERE g.idNguoiDung = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idNguoiDung);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                tongTien = rs.getDouble("tongTien");
            }

            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tongTien;
    }

    public double tinhPhiVanChuyenMotLanChoGioHang(int idNguoiDung) {
        double phiVanChuyen = 0;

        String sql = "SELECT MAX(COALESCE(t.phiShip, 0)) AS phiVanChuyen " +
                     "FROM giohang g " +
                     "JOIN chitietgiohang ct ON g.idGioHang = ct.idGioHang " +
                     "JOIN thuoc t ON ct.idThuoc = t.idThuoc " +
                     "WHERE g.idNguoiDung = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idNguoiDung);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                phiVanChuyen = rs.getDouble("phiVanChuyen");
            }

            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return phiVanChuyen;
    }

    public double tinhTongThanhToanGioHang(int idNguoiDung) {
        double tongTien = tinhTongTienGioHang(idNguoiDung);
        double phiVanChuyen = tinhPhiVanChuyenMotLanChoGioHang(idNguoiDung);
        return tongTien + phiVanChuyen;
    }

    public boolean capNhatSoLuongTuyetDoi(int idGioHang, int idThuoc, int soLuongMoi) {
        String sql = "UPDATE chitietgiohang SET soLuong = ? WHERE idGioHang = ? AND idThuoc = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, soLuongMoi);
            ps.setInt(2, idGioHang);
            ps.setInt(3, idThuoc);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean xoaKhoiGio(int idGioHang, int idThuoc) {
        String sql = "DELETE FROM chitietgiohang WHERE idGioHang = ? AND idThuoc = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idGioHang);
            ps.setInt(2, idThuoc);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}