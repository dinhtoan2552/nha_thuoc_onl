package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Thuoc;
import utils.DBConnection;

public class ThuocDAO {

    public List<Thuoc> getAllThuoc() {
        List<Thuoc> list = new ArrayList<>();
        String sql = "SELECT * FROM thuoc ORDER BY idThuoc DESC";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Thuoc t = mapThuoc(rs);
                list.add(t);
            }

            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public Thuoc getThuocById(int idThuoc) {
        Thuoc thuoc = null;
        String sql = "SELECT * FROM thuoc WHERE idThuoc = ?";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idThuoc);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                thuoc = mapThuoc(rs);
            }

            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return thuoc;
    }

    public boolean insertThuoc(Thuoc t) {
        String sql = "INSERT INTO thuoc (tenThuoc, idDanhMuc, giaGoc, donGia, phiShip, soLuong, hinhAnh, moTa, congDung, cachDung, thanhPhan, hanSuDung, nhaSanXuat, trangThai) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, t.getTenThuoc());
            ps.setInt(2, t.getIdDanhMuc());
            ps.setDouble(3, t.getGiaGoc());
            ps.setDouble(4, t.getDonGia());
            ps.setDouble(5, t.getPhiShip());
            ps.setInt(6, t.getSoLuong());
            ps.setString(7, t.getHinhAnh());
            ps.setString(8, t.getMoTa());
            ps.setString(9, t.getCongDung());
            ps.setString(10, t.getCachDung());
            ps.setString(11, t.getThanhPhan());
            ps.setString(12, t.getHanSuDung());
            ps.setString(13, t.getNhaSanXuat());
            ps.setString(14, t.getTrangThai());

            boolean result = ps.executeUpdate() > 0;

            ps.close();
            conn.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateThuoc(Thuoc t) {
        String sql = "UPDATE thuoc SET tenThuoc=?, idDanhMuc=?, giaGoc=?, donGia=?, phiShip=?, soLuong=?, hinhAnh=?, moTa=?, congDung=?, cachDung=?, thanhPhan=?, hanSuDung=?, nhaSanXuat=?, trangThai=? "
                + "WHERE idThuoc=?";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, t.getTenThuoc());
            ps.setInt(2, t.getIdDanhMuc());
            ps.setDouble(3, t.getGiaGoc());
            ps.setDouble(4, t.getDonGia());
            ps.setDouble(5, t.getPhiShip());
            ps.setInt(6, t.getSoLuong());
            ps.setString(7, t.getHinhAnh());
            ps.setString(8, t.getMoTa());
            ps.setString(9, t.getCongDung());
            ps.setString(10, t.getCachDung());
            ps.setString(11, t.getThanhPhan());
            ps.setString(12, t.getHanSuDung());
            ps.setString(13, t.getNhaSanXuat());
            ps.setString(14, t.getTrangThai());
            ps.setInt(15, t.getIdThuoc());

            boolean result = ps.executeUpdate() > 0;

            ps.close();
            conn.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteThuoc(int idThuoc) {
        String sql = "DELETE FROM thuoc WHERE idThuoc = ?";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idThuoc);

            boolean result = ps.executeUpdate() > 0;

            ps.close();
            conn.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private Thuoc mapThuoc(ResultSet rs) throws Exception {
        Thuoc t = new Thuoc();
        t.setIdThuoc(rs.getInt("idThuoc"));
        t.setTenThuoc(rs.getString("tenThuoc"));
        t.setIdDanhMuc(rs.getInt("idDanhMuc"));
        t.setGiaGoc(rs.getDouble("giaGoc"));
        t.setDonGia(rs.getDouble("donGia"));
        t.setPhiShip(rs.getDouble("phiShip"));
        t.setSoLuong(rs.getInt("soLuong"));
        t.setHinhAnh(rs.getString("hinhAnh"));
        t.setMoTa(rs.getString("moTa"));
        t.setCongDung(rs.getString("congDung"));
        t.setCachDung(rs.getString("cachDung"));
        t.setThanhPhan(rs.getString("thanhPhan"));
        t.setHanSuDung(rs.getString("hanSuDung"));
        t.setNhaSanXuat(rs.getString("nhaSanXuat"));
        t.setTrangThai(rs.getString("trangThai"));
        return t;
    }

    public int demTongSoThuocChoUser() {
        int total = 0;
        String sql = "SELECT COUNT(*) FROM thuoc WHERE trangThai = 'CON_HANG'";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getInt(1);
            }

            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }

    public List<Thuoc> getThuocPhanTrangChoUser(int offset, int limit) {
        List<Thuoc> list = new ArrayList<>();
        String sql = "SELECT * FROM thuoc WHERE trangThai = 'CON_HANG' ORDER BY idThuoc DESC LIMIT ?, ?";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, offset);
            ps.setInt(2, limit);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Thuoc t = mapThuoc(rs);
                list.add(t);
            }

            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Thuoc> getThuocTrongKho(String keyword, String trangThaiKho) {
        List<Thuoc> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT idThuoc, tenThuoc, giaGoc, donGia, phiShip, soLuong, hinhAnh, nhaSanXuat, trangThai ");
        sql.append("FROM thuoc WHERE 1=1 ");

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (tenThuoc LIKE ? OR CAST(idThuoc AS CHAR) LIKE ?) ");
        }

        if (trangThaiKho != null && !trangThaiKho.trim().isEmpty()) {
            if ("CON_HANG".equals(trangThaiKho)) {
                sql.append("AND soLuong > 10 ");
            } else if ("SAP_HET".equals(trangThaiKho)) {
                sql.append("AND soLuong > 0 AND soLuong <= 10 ");
            } else if ("HET_HANG".equals(trangThaiKho)) {
                sql.append("AND soLuong <= 0 ");
            }
        }

        sql.append("ORDER BY soLuong ASC, idThuoc DESC");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;

            if (keyword != null && !keyword.trim().isEmpty()) {
                String kw = "%" + keyword.trim() + "%";
                ps.setString(index++, kw);
                ps.setString(index++, kw);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Thuoc t = new Thuoc();
                    t.setIdThuoc(rs.getInt("idThuoc"));
                    t.setTenThuoc(rs.getString("tenThuoc"));
                    t.setGiaGoc(rs.getDouble("giaGoc"));
                    t.setDonGia(rs.getDouble("donGia"));
                    t.setPhiShip(rs.getDouble("phiShip"));
                    t.setSoLuong(rs.getInt("soLuong"));
                    t.setHinhAnh(rs.getString("hinhAnh"));
                    t.setNhaSanXuat(rs.getString("nhaSanXuat"));
                    t.setTrangThai(rs.getString("trangThai"));
                    list.add(t);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public int countTatCaThuoc() {
        String sql = "SELECT COUNT(*) FROM thuoc";

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

    public int countThuocSapHet(int mucCanhBao) {
        String sql = "SELECT COUNT(*) FROM thuoc WHERE soLuong > 0 AND soLuong <= ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, mucCanhBao);

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

    public int countThuocHetHang() {
        String sql = "SELECT COUNT(*) FROM thuoc WHERE soLuong <= 0";

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

    public boolean nhapThemSoLuongThuoc(int idThuoc, int soLuongNhap) {
        String sql = "UPDATE thuoc SET soLuong = soLuong + ? WHERE idThuoc = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, soLuongNhap);
            ps.setInt(2, idThuoc);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public int demTongSoThuocChoUserTheoDanhMuc(int idDanhMuc) {
        int total = 0;
        String sql = "SELECT COUNT(*) FROM thuoc WHERE trangThai = 'CON_HANG' AND idDanhMuc = ?";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idDanhMuc);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getInt(1);
            }

            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }

    public List<Thuoc> getThuocPhanTrangChoUserTheoDanhMuc(int idDanhMuc, int offset, int limit) {
        List<Thuoc> list = new ArrayList<>();
        String sql = "SELECT * FROM thuoc WHERE trangThai = 'CON_HANG' AND idDanhMuc = ? ORDER BY idThuoc DESC LIMIT ?, ?";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idDanhMuc);
            ps.setInt(2, offset);
            ps.setInt(3, limit);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Thuoc t = mapThuoc(rs);
                list.add(t);
            }

            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public int demTongSoThuocChoUserTheoTuKhoa(String keyword) {
        int total = 0;
        String sql = "SELECT COUNT(*) FROM thuoc "
                + "WHERE trangThai = 'CON_HANG' "
                + "AND (tenThuoc LIKE ? OR moTa LIKE ? OR nhaSanXuat LIKE ?)";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            String kw = "%" + keyword.trim() + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            ps.setString(3, kw);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getInt(1);
            }

            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }

    public int demTongSoThuocChoUserTheoTuKhoaVaDanhMuc(String keyword, int idDanhMuc) {
        int total = 0;
        String sql = "SELECT COUNT(*) FROM thuoc "
                + "WHERE trangThai = 'CON_HANG' AND idDanhMuc = ? "
                + "AND (tenThuoc LIKE ? OR moTa LIKE ? OR nhaSanXuat LIKE ?)";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            String kw = "%" + keyword.trim() + "%";
            ps.setInt(1, idDanhMuc);
            ps.setString(2, kw);
            ps.setString(3, kw);
            ps.setString(4, kw);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getInt(1);
            }

            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }

    public List<Thuoc> getThuocPhanTrangChoUserTheoTuKhoa(String keyword, int offset, int limit) {
        List<Thuoc> list = new ArrayList<>();
        String sql = "SELECT * FROM thuoc "
                + "WHERE trangThai = 'CON_HANG' "
                + "AND (tenThuoc LIKE ? OR moTa LIKE ? OR nhaSanXuat LIKE ?) "
                + "ORDER BY idThuoc DESC LIMIT ?, ?";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            String kw = "%" + keyword.trim() + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            ps.setString(3, kw);
            ps.setInt(4, offset);
            ps.setInt(5, limit);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Thuoc t = mapThuoc(rs);
                list.add(t);
            }

            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Thuoc> getThuocPhanTrangChoUserTheoTuKhoaVaDanhMuc(String keyword, int idDanhMuc, int offset, int limit) {
        List<Thuoc> list = new ArrayList<>();
        String sql = "SELECT * FROM thuoc "
                + "WHERE trangThai = 'CON_HANG' AND idDanhMuc = ? "
                + "AND (tenThuoc LIKE ? OR moTa LIKE ? OR nhaSanXuat LIKE ?) "
                + "ORDER BY idThuoc DESC LIMIT ?, ?";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            String kw = "%" + keyword.trim() + "%";
            ps.setInt(1, idDanhMuc);
            ps.setString(2, kw);
            ps.setString(3, kw);
            ps.setString(4, kw);
            ps.setInt(5, offset);
            ps.setInt(6, limit);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Thuoc t = mapThuoc(rs);
                list.add(t);
            }

            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public int countThuocTrongKho(String keyword, String trangThaiKho) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM thuoc WHERE 1=1 ");

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (tenThuoc LIKE ? OR CAST(idThuoc AS CHAR) LIKE ?) ");
        }

        if (trangThaiKho != null && !trangThaiKho.trim().isEmpty()) {
            if ("CON_HANG".equals(trangThaiKho)) {
                sql.append("AND soLuong > 10 ");
            } else if ("SAP_HET".equals(trangThaiKho)) {
                sql.append("AND soLuong > 0 AND soLuong <= 10 ");
            } else if ("HET_HANG".equals(trangThaiKho)) {
                sql.append("AND soLuong <= 0 ");
            }
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;
            if (keyword != null && !keyword.trim().isEmpty()) {
                String kw = "%" + keyword.trim() + "%";
                ps.setString(index++, kw);
                ps.setString(index++, kw);
            }

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

    public List<Thuoc> getThuocTrongKhoPhanTrang(String keyword, String trangThaiKho, int offset, int limit) {
        List<Thuoc> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT idThuoc, tenThuoc, giaGoc, donGia, phiShip, soLuong, hinhAnh, nhaSanXuat, trangThai ");
        sql.append("FROM thuoc WHERE 1=1 ");

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (tenThuoc LIKE ? OR CAST(idThuoc AS CHAR) LIKE ?) ");
        }

        if (trangThaiKho != null && !trangThaiKho.trim().isEmpty()) {
            if ("CON_HANG".equals(trangThaiKho)) {
                sql.append("AND soLuong > 10 ");
            } else if ("SAP_HET".equals(trangThaiKho)) {
                sql.append("AND soLuong > 0 AND soLuong <= 10 ");
            } else if ("HET_HANG".equals(trangThaiKho)) {
                sql.append("AND soLuong <= 0 ");
            }
        }

        sql.append("ORDER BY idThuoc DESC LIMIT ?, ?");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;

            if (keyword != null && !keyword.trim().isEmpty()) {
                String kw = "%" + keyword.trim() + "%";
                ps.setString(index++, kw);
                ps.setString(index++, kw);
            }

            ps.setInt(index++, offset);
            ps.setInt(index, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Thuoc t = new Thuoc();
                    t.setIdThuoc(rs.getInt("idThuoc"));
                    t.setTenThuoc(rs.getString("tenThuoc"));
                    t.setGiaGoc(rs.getDouble("giaGoc"));
                    t.setDonGia(rs.getDouble("donGia"));
                    t.setPhiShip(rs.getDouble("phiShip"));
                    t.setSoLuong(rs.getInt("soLuong"));
                    t.setHinhAnh(rs.getString("hinhAnh"));
                    t.setNhaSanXuat(rs.getString("nhaSanXuat"));
                    t.setTrangThai(rs.getString("trangThai"));
                    list.add(t);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Thuoc> getGoiYTenThuocChoUser(String keyword, int limit) {
        List<Thuoc> ds = new ArrayList<>();

        String sql = "SELECT idThuoc, tenThuoc "
                + "FROM thuoc "
                + "WHERE trangThai = 'CON_HANG' "
                + "AND tenThuoc LIKE ? "
                + "ORDER BY "
                + "CASE WHEN tenThuoc LIKE ? THEN 0 ELSE 1 END, "
                + "tenThuoc ASC "
                + "LIMIT ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String kw = "%" + keyword.trim() + "%";
            String kwStart = keyword.trim() + "%";

            ps.setString(1, kw);
            ps.setString(2, kwStart);
            ps.setInt(3, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Thuoc t = new Thuoc();
                    t.setIdThuoc(rs.getInt("idThuoc"));
                    t.setTenThuoc(rs.getString("tenThuoc"));
                    ds.add(t);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }

    public List<Thuoc> getSanPhamTuongTuChoUser(int idThuocHienTai, int idDanhMuc, String congDung, int limit) {
        List<Thuoc> list = new ArrayList<>();

        String sql = "SELECT idThuoc, tenThuoc, hinhAnh, donGia, moTa, congDung, idDanhMuc, trangThai, soLuong "
                + "FROM thuoc "
                + "WHERE trangThai = 'CON_HANG' "
                + "AND soLuong > 0 "
                + "AND idThuoc <> ? "
                + "AND idDanhMuc = ? "
                + "ORDER BY "
                + "CASE "
                + "    WHEN congDung IS NOT NULL AND congDung <> '' AND congDung LIKE ? THEN 0 "
                + "    ELSE 1 "
                + "END, "
                + "idThuoc DESC "
                + "LIMIT ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String tuKhoaCongDung = "%";
            if (congDung != null && !congDung.trim().isEmpty()) {
                String cd = congDung.trim();
                if (cd.length() > 80) {
                    cd = cd.substring(0, 80);
                }
                tuKhoaCongDung = "%" + cd + "%";
            }

            ps.setInt(1, idThuocHienTai);
            ps.setInt(2, idDanhMuc);
            ps.setString(3, tuKhoaCongDung);
            ps.setInt(4, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Thuoc t = new Thuoc();
                    t.setIdThuoc(rs.getInt("idThuoc"));
                    t.setTenThuoc(rs.getString("tenThuoc"));
                    t.setHinhAnh(rs.getString("hinhAnh"));
                    t.setDonGia(rs.getDouble("donGia"));
                    t.setMoTa(rs.getString("moTa"));
                    t.setCongDung(rs.getString("congDung"));
                    t.setIdDanhMuc(rs.getInt("idDanhMuc"));
                    t.setTrangThai(rs.getString("trangThai"));
                    t.setSoLuong(rs.getInt("soLuong"));
                    list.add(t);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}