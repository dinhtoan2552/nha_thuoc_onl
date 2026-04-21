package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import model.OtpXacThuc;
import utils.DBConnection;

public class OtpDAO {

    public boolean voHieuHoaOtpCu(String email, String mucDich) {
        String sql = "UPDATE otp_xac_thuc SET da_dung = 1 "
                   + "WHERE email = ? AND muc_dich = ? AND da_dung = 0";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, mucDich);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean taoOtp(String email, String otpHash, String mucDich, Timestamp hetHanLuc) {
        String sql = "INSERT INTO otp_xac_thuc(email, otp_hash, muc_dich, het_han_luc, da_dung, so_lan_sai) "
                   + "VALUES (?, ?, ?, ?, 0, 0)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, otpHash);
            ps.setString(3, mucDich);
            ps.setTimestamp(4, hetHanLuc);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public OtpXacThuc layOtpConHieuLucMoiNhat(String email, String mucDich) {
        String sql = "SELECT * FROM otp_xac_thuc "
                   + "WHERE email = ? AND muc_dich = ? AND da_dung = 0 AND het_han_luc >= NOW() "
                   + "ORDER BY id DESC LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, mucDich);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    OtpXacThuc otp = new OtpXacThuc();
                    otp.setId(rs.getInt("id"));
                    otp.setEmail(rs.getString("email"));
                    otp.setOtpHash(rs.getString("otp_hash"));
                    otp.setMucDich(rs.getString("muc_dich"));
                    otp.setHetHanLuc(rs.getTimestamp("het_han_luc"));
                    otp.setDaDung(rs.getBoolean("da_dung"));
                    otp.setSoLanSai(rs.getInt("so_lan_sai"));
                    otp.setTaoLuc(rs.getTimestamp("tao_luc"));
                    return otp;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean tangSoLanSai(int idOtp) {
        String sql = "UPDATE otp_xac_thuc SET so_lan_sai = so_lan_sai + 1 WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idOtp);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean danhDauDaDung(int idOtp) {
        String sql = "UPDATE otp_xac_thuc SET da_dung = 1 WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idOtp);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean xoaOtpHetHan() {
        String sql = "DELETE FROM otp_xac_thuc WHERE het_han_luc < NOW() OR da_dung = 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}