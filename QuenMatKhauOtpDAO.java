package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import utils.DBConnection;

public class QuenMatKhauOtpDAO {

    public void xoaOtpCuTheoEmail(String email) {
        String sql = "DELETE FROM quenmatkhau_otp WHERE email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean taoOtp(String email, String otpCode, LocalDateTime otpExpireAt, LocalDateTime resendAvailableAt) {
        String sql = "INSERT INTO quenmatkhau_otp (email, otpCode, otpExpireAt, resendAvailableAt, soLanSai, daXacThuc) "
                + "VALUES (?, ?, ?, ?, 0, 0)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, otpCode);
            ps.setTimestamp(3, Timestamp.valueOf(otpExpireAt));
            ps.setTimestamp(4, Timestamp.valueOf(resendAvailableAt));

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean emailDangChoResend(String email) {
        String sql = "SELECT resendAvailableAt FROM quenmatkhau_otp WHERE email = ? ORDER BY id DESC LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Timestamp resendTs = rs.getTimestamp("resendAvailableAt");
                    if (resendTs != null) {
                        return resendTs.toLocalDateTime().isAfter(LocalDateTime.now());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean xacThucOtp(String email, String otpCode) {
        String sql = "SELECT id, otpCode, otpExpireAt, soLanSai, daXacThuc "
                + "FROM quenmatkhau_otp WHERE email = ? ORDER BY id DESC LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return false;
                }

                int id = rs.getInt("id");
                String otpDb = rs.getString("otpCode");
                Timestamp expireTs = rs.getTimestamp("otpExpireAt");
                int soLanSai = rs.getInt("soLanSai");
                boolean daXacThuc = rs.getBoolean("daXacThuc");

                if (daXacThuc) {
                    return true;
                }

                if (expireTs == null || expireTs.toLocalDateTime().isBefore(LocalDateTime.now())) {
                    return false;
                }

                if (soLanSai >= 5) {
                    return false;
                }

                if (!otpDb.equals(otpCode)) {
                    tangSoLanSai(id);
                    return false;
                }

                return capNhatDaXacThuc(id);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private void tangSoLanSai(int id) {
        String sql = "UPDATE quenmatkhau_otp SET soLanSai = soLanSai + 1 WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean capNhatDaXacThuc(int id) {
        String sql = "UPDATE quenmatkhau_otp SET daXacThuc = 1 WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean daXacThucOtp(String email) {
        String sql = "SELECT daXacThuc, otpExpireAt FROM quenmatkhau_otp WHERE email = ? ORDER BY id DESC LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    boolean daXacThuc = rs.getBoolean("daXacThuc");
                    Timestamp expireTs = rs.getTimestamp("otpExpireAt");

                    if (!daXacThuc || expireTs == null) {
                        return false;
                    }

                    return !expireTs.toLocalDateTime().isBefore(LocalDateTime.now());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public void xoaTatCaTheoEmail(String email) {
        xoaOtpCuTheoEmail(email);
    }
}