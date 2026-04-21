package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.HoTroPhien;
import model.HoTroTinNhan;
import utils.DBConnection;

public class HoTroDAO {

    public int taoPhienHoTro(int idNguoiDung, String tieuDe, String noiDungDauTien) {
        Connection conn = null;
        PreparedStatement psPhien = null;
        PreparedStatement psTinUser = null;
        PreparedStatement psTinBot = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            String sqlPhien = "INSERT INTO hotro_phien (idNguoiDung, tieuDe, trangThai) VALUES (?, ?, 'DANG_MO')";
            psPhien = conn.prepareStatement(sqlPhien, Statement.RETURN_GENERATED_KEYS);
            psPhien.setInt(1, idNguoiDung);
            psPhien.setString(2, tieuDe);

            int ok = psPhien.executeUpdate();
            if (ok <= 0) {
                conn.rollback();
                return -1;
            }

            rs = psPhien.getGeneratedKeys();
            int idPhien = -1;
            if (rs.next()) {
                idPhien = rs.getInt(1);
            }

            if (idPhien <= 0) {
                conn.rollback();
                return -1;
            }

            String sqlTinUser = "INSERT INTO hotro_tinnhan "
                    + "(idPhien, idNguoiGui, loaiNguoiGui, noiDung, anhDinhKem, daDoc, daDocStaff, daDocUser) "
                    + "VALUES (?, ?, 'USER', ?, NULL, FALSE, FALSE, TRUE)";
            psTinUser = conn.prepareStatement(sqlTinUser);
            psTinUser.setInt(1, idPhien);
            psTinUser.setInt(2, idNguoiDung);
            psTinUser.setString(3, noiDungDauTien);

            if (psTinUser.executeUpdate() <= 0) {
                conn.rollback();
                return -1;
            }

            String tinNhanChao = "Nhà thuốc xin chào quý khách, bạn cần hỗ trợ gì ạ?";

            String sqlTinBot = "INSERT INTO hotro_tinnhan "
                    + "(idPhien, idNguoiGui, loaiNguoiGui, noiDung, anhDinhKem, daDoc, daDocStaff, daDocUser) "
                    + "VALUES (?, ?, 'STAFF', ?, NULL, TRUE, TRUE, FALSE)";
            psTinBot = conn.prepareStatement(sqlTinBot);
            psTinBot.setInt(1, idPhien);
            psTinBot.setInt(2, 2);
            psTinBot.setString(3, tinNhanChao);

            if (psTinBot.executeUpdate() <= 0) {
                conn.rollback();
                return -1;
            }

            conn.commit();
            return idPhien;

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (psTinBot != null) psTinBot.close(); } catch (Exception e) {}
            try { if (psTinUser != null) psTinUser.close(); } catch (Exception e) {}
            try { if (psPhien != null) psPhien.close(); } catch (Exception e) {}
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (Exception e) {}
        }

        return -1;
    }

    public boolean themTinNhan(int idPhien, int idNguoiGui, String loaiNguoiGui, String noiDung, String anhDinhKem) {
        String sql = "INSERT INTO hotro_tinnhan "
                + "(idPhien, idNguoiGui, loaiNguoiGui, noiDung, anhDinhKem, daDoc, daDocStaff, daDocUser) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPhien);
            ps.setInt(2, idNguoiGui);
            ps.setString(3, loaiNguoiGui);
            ps.setString(4, noiDung);
            ps.setString(5, anhDinhKem);

            boolean laUser = "USER".equalsIgnoreCase(loaiNguoiGui);

            ps.setBoolean(6, !laUser);      // giữ tương thích cột cũ
            ps.setBoolean(7, !laUser);      // daDocStaff
            ps.setBoolean(8, laUser);       // daDocUser

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<HoTroPhien> getDanhSachPhienTheoUser(int idNguoiDung) {
        List<HoTroPhien> list = new ArrayList<>();

        String sql = "SELECT hp.*, nd.hoTen AS tenKhachHang, nv.hoTen AS tenNhanVienXuLy, "
                   + "(SELECT CASE "
                   + " WHEN ht.noiDung IS NOT NULL AND TRIM(ht.noiDung) <> '' THEN ht.noiDung "
                   + " WHEN ht.anhDinhKem IS NOT NULL AND TRIM(ht.anhDinhKem) <> '' THEN '[Ảnh]' "
                   + " ELSE '' END "
                   + " FROM hotro_tinnhan ht WHERE ht.idPhien = hp.idPhien ORDER BY ht.idTinNhan DESC LIMIT 1) AS tinNhanCuoi, "
                   + "(SELECT ht.ngayGui FROM hotro_tinnhan ht WHERE ht.idPhien = hp.idPhien ORDER BY ht.idTinNhan DESC LIMIT 1) AS thoiGianTinNhanCuoi, "
                   + "(SELECT COUNT(*) FROM hotro_tinnhan ht2 "
                   + " WHERE ht2.idPhien = hp.idPhien AND ht2.loaiNguoiGui = 'STAFF' AND ht2.daDocUser = FALSE) AS soTinChuaDoc "
                   + "FROM hotro_phien hp "
                   + "JOIN nguoidung nd ON hp.idNguoiDung = nd.id "
                   + "LEFT JOIN nguoidung nv ON hp.idNhanVienXuLy = nv.id "
                   + "WHERE hp.idNguoiDung = ? "
                   + "ORDER BY hp.ngayCapNhat DESC, hp.idPhien DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idNguoiDung);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    HoTroPhien p = mapPhien(rs);
                    list.add(p);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<HoTroPhien> getTatCaPhien() {
        List<HoTroPhien> list = new ArrayList<>();

        String sql = "SELECT hp.*, nd.hoTen AS tenKhachHang, nv.hoTen AS tenNhanVienXuLy, "
                   + "(SELECT CASE "
                   + " WHEN ht.noiDung IS NOT NULL AND TRIM(ht.noiDung) <> '' THEN ht.noiDung "
                   + " WHEN ht.anhDinhKem IS NOT NULL AND TRIM(ht.anhDinhKem) <> '' THEN '[Ảnh]' "
                   + " ELSE '' END "
                   + " FROM hotro_tinnhan ht WHERE ht.idPhien = hp.idPhien ORDER BY ht.idTinNhan DESC LIMIT 1) AS tinNhanCuoi, "
                   + "(SELECT ht.ngayGui FROM hotro_tinnhan ht WHERE ht.idPhien = hp.idPhien ORDER BY ht.idTinNhan DESC LIMIT 1) AS thoiGianTinNhanCuoi, "
                   + "(SELECT COUNT(*) FROM hotro_tinnhan ht2 "
                   + " WHERE ht2.idPhien = hp.idPhien AND ht2.loaiNguoiGui = 'USER' AND ht2.daDocStaff = FALSE) AS soTinChuaDoc "
                   + "FROM hotro_phien hp "
                   + "JOIN nguoidung nd ON hp.idNguoiDung = nd.id "
                   + "LEFT JOIN nguoidung nv ON hp.idNhanVienXuLy = nv.id "
                   + "ORDER BY CASE WHEN hp.trangThai = 'DANG_MO' THEN 0 ELSE 1 END, hp.ngayCapNhat DESC, hp.idPhien DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                HoTroPhien p = mapPhien(rs);
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public HoTroPhien getPhienById(int idPhien) {
        String sql = "SELECT hp.*, nd.hoTen AS tenKhachHang, nv.hoTen AS tenNhanVienXuLy "
                   + "FROM hotro_phien hp "
                   + "JOIN nguoidung nd ON hp.idNguoiDung = nd.id "
                   + "LEFT JOIN nguoidung nv ON hp.idNhanVienXuLy = nv.id "
                   + "WHERE hp.idPhien = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPhien);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapPhien(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<HoTroTinNhan> getTinNhanTheoPhien(int idPhien) {
        List<HoTroTinNhan> list = new ArrayList<>();

        String sql = "SELECT ht.*, nd.hoTen AS tenNguoiGui "
                   + "FROM hotro_tinnhan ht "
                   + "JOIN nguoidung nd ON ht.idNguoiGui = nd.id "
                   + "WHERE ht.idPhien = ? "
                   + "ORDER BY ht.idTinNhan ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPhien);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    HoTroTinNhan t = new HoTroTinNhan();
                    t.setIdTinNhan(rs.getInt("idTinNhan"));
                    t.setIdPhien(rs.getInt("idPhien"));
                    t.setIdNguoiGui(rs.getInt("idNguoiGui"));
                    t.setLoaiNguoiGui(rs.getString("loaiNguoiGui"));
                    t.setNoiDung(rs.getString("noiDung"));
                    t.setAnhDinhKem(rs.getString("anhDinhKem"));
                    t.setNgayGui(rs.getTimestamp("ngayGui"));
                    t.setTenNguoiGui(rs.getString("tenNguoiGui"));
                    list.add(t);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean capNhatTrangThaiPhien(int idPhien, String trangThai, Integer idNhanVienXuLy) {
        String sql = "UPDATE hotro_phien SET trangThai = ?, idNhanVienXuLy = ? WHERE idPhien = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, trangThai);
            if (idNhanVienXuLy == null) {
                ps.setNull(2, java.sql.Types.INTEGER);
            } else {
                ps.setInt(2, idNhanVienXuLy);
            }
            ps.setInt(3, idPhien);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public void markAsReadByStaff(int idPhien) {
        String sql = "UPDATE hotro_tinnhan "
                   + "SET daDoc = TRUE, daDocStaff = TRUE "
                   + "WHERE idPhien = ? AND loaiNguoiGui = 'USER' AND daDocStaff = FALSE";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPhien);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void markAsReadByUser(int idPhien) {
        String sql = "UPDATE hotro_tinnhan "
                   + "SET daDocUser = TRUE "
                   + "WHERE idPhien = ? AND loaiNguoiGui = 'STAFF' AND daDocUser = FALSE";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPhien);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HoTroPhien mapPhien(ResultSet rs) throws Exception {
        HoTroPhien p = new HoTroPhien();
        p.setIdPhien(rs.getInt("idPhien"));
        p.setIdNguoiDung(rs.getInt("idNguoiDung"));
        p.setTieuDe(rs.getString("tieuDe"));
        p.setTrangThai(rs.getString("trangThai"));

        int idNv = rs.getInt("idNhanVienXuLy");
        p.setIdNhanVienXuLy(rs.wasNull() ? null : idNv);

        p.setNgayTao(rs.getTimestamp("ngayTao"));
        p.setNgayCapNhat(rs.getTimestamp("ngayCapNhat"));
        p.setTenKhachHang(rs.getString("tenKhachHang"));
        p.setTenNhanVienXuLy(rs.getString("tenNhanVienXuLy"));

        try {
            p.setTinNhanCuoi(rs.getString("tinNhanCuoi"));
        } catch (Exception e) {}

        try {
            p.setThoiGianTinNhanCuoi(rs.getTimestamp("thoiGianTinNhanCuoi"));
        } catch (Exception e) {}

        try {
            p.setSoTinChuaDoc(rs.getInt("soTinChuaDoc"));
        } catch (Exception e) {
            p.setSoTinChuaDoc(0);
        }

        return p;
    }

    public int countPhienDangMo() {
        String sql = "SELECT COUNT(*) FROM hotro_phien WHERE trangThai = 'DANG_MO'";

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

    public int countKhachHangCoTinNhanMoi() {
        String sql = "SELECT COUNT(DISTINCT hp.idNguoiDung) "
                   + "FROM hotro_tinnhan ht "
                   + "JOIN hotro_phien hp ON ht.idPhien = hp.idPhien "
                   + "WHERE ht.loaiNguoiGui = 'USER' "
                   + "AND ht.daDocStaff = FALSE "
                   + "AND hp.trangThai = 'DANG_MO'";

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

    public int countPhienCoTinNhanMoiTuKhach() {
        String sql = "SELECT COUNT(DISTINCT ht.idPhien) "
                   + "FROM hotro_tinnhan ht "
                   + "JOIN hotro_phien hp ON ht.idPhien = hp.idPhien "
                   + "WHERE ht.loaiNguoiGui = 'USER' "
                   + "AND ht.daDocStaff = FALSE "
                   + "AND hp.trangThai = 'DANG_MO'";

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
}