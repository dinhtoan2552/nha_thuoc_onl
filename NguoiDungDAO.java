package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.NguoiDung;
import utils.DBConnection;
import utils.PasswordUtil;

public class NguoiDungDAO {

    public List<NguoiDung> getAllNguoiDung(String keyword, String vaiTro, String trangThai) {
        List<NguoiDung> list = new ArrayList<>();

        String keywordSafe = keyword != null ? keyword.trim() : "";
        String vaiTroSafe = vaiTro != null ? vaiTro.trim() : "";
        String trangThaiSafe = trangThai != null ? trangThai.trim() : "";

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM nguoidung WHERE 1=1 ");

        if (!keywordSafe.isEmpty()) {
            sql.append("AND (hoTen LIKE ? OR email LIKE ? OR soDienThoai LIKE ?) ");
        }

        if (!vaiTroSafe.isEmpty()) {
            sql.append("AND vaiTro = ? ");
        }

        if (!trangThaiSafe.isEmpty()) {
            sql.append("AND trangThai = ? ");
        }

        sql.append("ORDER BY id DESC");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;

            if (!keywordSafe.isEmpty()) {
                String kw = "%" + keywordSafe + "%";
                ps.setString(index++, kw);
                ps.setString(index++, kw);
                ps.setString(index++, kw);
            }

            if (!vaiTroSafe.isEmpty()) {
                ps.setString(index++, vaiTroSafe);
            }

            if (!trangThaiSafe.isEmpty()) {
                ps.setString(index++, trangThaiSafe);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapNguoiDung(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public NguoiDung getNguoiDungById(int id) {
        String sql = "SELECT * FROM nguoidung WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapNguoiDung(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public NguoiDung getNguoiDungByEmail(String email) {
        String emailSafe = normalizeEmail(email);
        if (emailSafe == null) {
            return null;
        }

        String sql = "SELECT * FROM nguoidung WHERE email = ? LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, emailSafe);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapNguoiDung(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean emailDaTonTai(String email) {
        String emailSafe = normalizeEmail(email);
        if (emailSafe == null) {
            return false;
        }

        String sql = "SELECT id FROM nguoidung WHERE email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, emailSafe);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean emailDaTonTaiNgoaiTruId(String email, int id) {
        String emailSafe = normalizeEmail(email);
        if (emailSafe == null) {
            return false;
        }

        String sql = "SELECT id FROM nguoidung WHERE email = ? AND id <> ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, emailSafe);
            ps.setInt(2, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean emailTonTai(String email) {
        String emailSafe = normalizeEmail(email);
        if (emailSafe == null) {
            return false;
        }

        String sql = "SELECT id FROM nguoidung WHERE email = ? LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, emailSafe);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean insertNguoiDung(NguoiDung nd) {
        if (nd == null) {
            return false;
        }

        String sql = "INSERT INTO nguoidung (hoTen, email, matKhau, soDienThoai, diaChi, vaiTro, trangThai) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, safeTrim(nd.getHoTen()));
            ps.setString(2, normalizeEmail(nd.getEmail()));
            ps.setString(3, nd.getMatKhau());
            ps.setString(4, safeTrim(nd.getSoDienThoai()));
            ps.setString(5, safeTrim(nd.getDiaChi()));
            ps.setString(6, safeTrim(nd.getVaiTro()));
            ps.setString(7, safeTrim(nd.getTrangThai()));

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateNguoiDung(NguoiDung nd) {
        if (nd == null) {
            return false;
        }

        String sql = "UPDATE nguoidung SET hoTen = ?, soDienThoai = ?, diaChi = ?, vaiTro = ?, trangThai = ? "
                   + "WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, safeTrim(nd.getHoTen()));
            ps.setString(2, safeTrim(nd.getSoDienThoai()));
            ps.setString(3, safeTrim(nd.getDiaChi()));
            ps.setString(4, safeTrim(nd.getVaiTro()));
            ps.setString(5, safeTrim(nd.getTrangThai()));
            ps.setInt(6, nd.getId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateTrangThaiNguoiDung(int id, String trangThai) {
        String trangThaiSafe = safeTrim(trangThai);
        if (trangThaiSafe == null || trangThaiSafe.isEmpty()) {
            return false;
        }

        String sql = "UPDATE nguoidung SET trangThai = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, trangThaiSafe);
            ps.setInt(2, id);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public int countAllNguoiDung() {
        String sql = "SELECT COUNT(*) FROM nguoidung";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int countNguoiDungTheoTrangThai(String trangThai) {
        String trangThaiSafe = safeTrim(trangThai);
        if (trangThaiSafe == null || trangThaiSafe.isEmpty()) {
            return 0;
        }

        String sql = "SELECT COUNT(*) FROM nguoidung WHERE trangThai = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, trangThaiSafe);

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

    public NguoiDung login(String email, String matKhau) {
        String emailSafe = normalizeEmail(email);
        if (emailSafe == null || matKhau == null) {
            return null;
        }

        NguoiDung user = null;
        String sql = "SELECT * FROM nguoidung WHERE email = ? AND trangThai = 'HOAT_DONG' LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, emailSafe);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String matKhauTrongDb = rs.getString("matKhau");
                    boolean hopLe = false;
                    boolean canNangCapHash = false;

                    if (PasswordUtil.isBcryptHash(matKhauTrongDb)) {
                        hopLe = PasswordUtil.verifyPassword(matKhau, matKhauTrongDb);
                    } else if (matKhauTrongDb != null && matKhauTrongDb.equals(matKhau)) {
                        hopLe = true;
                        canNangCapHash = true;
                    }

                    if (hopLe) {
                        user = mapNguoiDung(rs);

                        if (canNangCapHash) {
                            String matKhauMoiDaHash = PasswordUtil.hashPassword(matKhau);
                            doiMatKhau(user.getId(), matKhauMoiDaHash);
                            user.setMatKhau(matKhauMoiDaHash);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    public boolean doiMatKhau(int id, String matKhauMoiDaHash) {
        if (matKhauMoiDaHash == null || matKhauMoiDaHash.trim().isEmpty()) {
            return false;
        }

        String sql = "UPDATE nguoidung SET matKhau = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, matKhauMoiDaHash);
            ps.setInt(2, id);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean capNhatMatKhauTheoEmail(String email, String matKhauMoiHash) {
        String emailSafe = normalizeEmail(email);
        if (emailSafe == null || matKhauMoiHash == null || matKhauMoiHash.trim().isEmpty()) {
            return false;
        }

        String sql = "UPDATE nguoidung SET matKhau = ? WHERE email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, matKhauMoiHash);
            ps.setString(2, emailSafe);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean doiEmail(int id, String emailMoi) {
        String emailMoiSafe = normalizeEmail(emailMoi);
        if (emailMoiSafe == null) {
            return false;
        }

        String sql = "UPDATE nguoidung SET email = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, emailMoiSafe);
            ps.setInt(2, id);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<NguoiDung> getNhanVien(String keyword, String trangThai) {
        List<NguoiDung> list = new ArrayList<>();

        String keywordSafe = keyword != null ? keyword.trim() : "";
        String trangThaiSafe = trangThai != null ? trangThai.trim() : "";

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM nguoidung WHERE vaiTro = 'STAFF' ");

        if (!keywordSafe.isEmpty()) {
            sql.append("AND (hoTen LIKE ? OR email LIKE ? OR soDienThoai LIKE ?) ");
        }

        if (!trangThaiSafe.isEmpty()) {
            sql.append("AND trangThai = ? ");
        }

        sql.append("ORDER BY id DESC");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;

            if (!keywordSafe.isEmpty()) {
                String kw = "%" + keywordSafe + "%";
                ps.setString(index++, kw);
                ps.setString(index++, kw);
                ps.setString(index++, kw);
            }

            if (!trangThaiSafe.isEmpty()) {
                ps.setString(index++, trangThaiSafe);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapNguoiDung(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public int countNhanVien() {
        String sql = "SELECT COUNT(*) FROM nguoidung WHERE vaiTro = 'STAFF'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int countNhanVienTheoTrangThai(String trangThai) {
        String trangThaiSafe = safeTrim(trangThai);
        if (trangThaiSafe == null || trangThaiSafe.isEmpty()) {
            return 0;
        }

        String sql = "SELECT COUNT(*) FROM nguoidung WHERE vaiTro = 'STAFF' AND trangThai = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, trangThaiSafe);

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

    private NguoiDung mapNguoiDung(ResultSet rs) throws Exception {
        NguoiDung nd = new NguoiDung();
        nd.setId(rs.getInt("id"));
        nd.setHoTen(rs.getString("hoTen"));
        nd.setEmail(rs.getString("email"));
        nd.setMatKhau(rs.getString("matKhau"));
        nd.setSoDienThoai(rs.getString("soDienThoai"));
        nd.setDiaChi(rs.getString("diaChi"));
        nd.setVaiTro(rs.getString("vaiTro"));
        nd.setTrangThai(rs.getString("trangThai"));
        return nd;
    }

    private String normalizeEmail(String email) {
        if (email == null) {
            return null;
        }
        String value = email.trim().toLowerCase();
        return value.isEmpty() ? null : value;
    }

    private String safeTrim(String value) {
        return value == null ? null : value.trim();
    }
}