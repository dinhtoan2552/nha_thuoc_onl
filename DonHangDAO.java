package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.ChiTietDonHang;
import model.DonHang;
import model.NguoiDung;
import model.Thuoc;
import utils.DBConnection;

public class DonHangDAO {

    private static final double PHI_VAN_CHUYEN_MAC_DINH = 120.0;

    public List<DonHang> getAllDonHang(String keyword, String trangThai) {
        List<DonHang> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT dh.idDonHang, dh.idNguoiDung, dh.ngayDat, dh.tongTien, dh.phiVanChuyen, dh.trangThai, ");
        sql.append("dh.tenNguoiNhan, dh.diaChiNhan, dh.sdtNhan, dh.ghiChu, dh.nguoiXuLy, dh.lyDoHuy, ");
        sql.append("dh.phuongThucThanhToan, dh.trangThaiThanhToan, dh.maChuyenKhoan, dh.anhBill, dh.ghiChuThanhToan, dh.ngayXacNhanThanhToan, ");
        sql.append("nd.hoTen AS tenKhachHang, nd.email AS emailKhachHang, nd.soDienThoai AS soDienThoaiKhach, ");
        sql.append("nx.hoTen AS tenNguoiXuLy, ");

        sql.append("(SELECT t.tenThuoc ");
        sql.append("   FROM chitietdonhang ct1 ");
        sql.append("   JOIN thuoc t ON ct1.idThuoc = t.idThuoc ");
        sql.append("   WHERE ct1.idDonHang = dh.idDonHang ");
        sql.append("   ORDER BY ct1.idChiTiet ASC LIMIT 1) AS tenSanPhamDaiDien, ");

        sql.append("(SELECT t.hinhAnh ");
        sql.append("   FROM chitietdonhang ct2 ");
        sql.append("   JOIN thuoc t ON ct2.idThuoc = t.idThuoc ");
        sql.append("   WHERE ct2.idDonHang = dh.idDonHang ");
        sql.append("   ORDER BY ct2.idChiTiet ASC LIMIT 1) AS hinhAnhDaiDien, ");

        sql.append("(SELECT COUNT(*) ");
        sql.append("   FROM chitietdonhang ct3 ");
        sql.append("   WHERE ct3.idDonHang = dh.idDonHang) AS tongSoSanPham ");

        sql.append("FROM donhang dh ");
        sql.append("JOIN nguoidung nd ON dh.idNguoiDung = nd.id ");
        sql.append("LEFT JOIN nguoidung nx ON dh.nguoiXuLy = nx.id ");
        sql.append("WHERE 1=1 ");

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (nd.hoTen LIKE ? OR nd.email LIKE ? OR CAST(dh.idDonHang AS CHAR) LIKE ?) ");
        }

        if (trangThai != null && !trangThai.trim().isEmpty()) {
            sql.append("AND dh.trangThai = ? ");
        }

        sql.append("ORDER BY dh.ngayDat DESC, dh.idDonHang DESC");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;

            if (keyword != null && !keyword.trim().isEmpty()) {
                String kw = "%" + keyword.trim() + "%";
                ps.setString(index++, kw);
                ps.setString(index++, kw);
                ps.setString(index++, kw);
            }

            if (trangThai != null && !trangThai.trim().isEmpty()) {
                ps.setString(index++, trangThai);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DonHang dh = new DonHang();

                    dh.setIdDonHang(rs.getInt("idDonHang"));
                    dh.setIdNguoiDung(rs.getInt("idNguoiDung"));
                    dh.setNgayDat(rs.getTimestamp("ngayDat"));
                    dh.setTongTien(rs.getDouble("tongTien"));
                    dh.setPhiVanChuyen(rs.getDouble("phiVanChuyen"));
                    dh.setTrangThai(rs.getString("trangThai"));
                    dh.setTenNguoiNhan(rs.getString("tenNguoiNhan"));
                    dh.setDiaChiNhan(rs.getString("diaChiNhan"));
                    dh.setSdtNhan(rs.getString("sdtNhan"));
                    dh.setGhiChu(rs.getString("ghiChu"));
                    dh.setLyDoHuy(rs.getString("lyDoHuy"));
                    dh.setPhuongThucThanhToan(rs.getString("phuongThucThanhToan"));
                    dh.setTrangThaiThanhToan(rs.getString("trangThaiThanhToan"));
                    dh.setMaChuyenKhoan(rs.getString("maChuyenKhoan"));
                    dh.setAnhBill(rs.getString("anhBill"));
                    dh.setGhiChuThanhToan(rs.getString("ghiChuThanhToan"));
                    dh.setNgayXacNhanThanhToan(rs.getTimestamp("ngayXacNhanThanhToan"));

                    int nguoiXuLyValue = rs.getInt("nguoiXuLy");
                    dh.setNguoiXuLy(rs.wasNull() ? null : nguoiXuLyValue);

                    dh.setTenKhachHang(rs.getString("tenKhachHang"));
                    dh.setEmailKhachHang(rs.getString("emailKhachHang"));
                    dh.setSoDienThoaiKhach(rs.getString("soDienThoaiKhach"));
                    dh.setTenNguoiXuLy(rs.getString("tenNguoiXuLy"));

                    dh.setTenSanPhamDaiDien(rs.getString("tenSanPhamDaiDien"));
                    dh.setHinhAnhDaiDien(rs.getString("hinhAnhDaiDien"));
                    dh.setTongSoSanPham(rs.getInt("tongSoSanPham"));

                    list.add(dh);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public DonHang getDonHangById(int idDonHang) {
        String sql = "SELECT dh.idDonHang, dh.idNguoiDung, dh.ngayDat, dh.tongTien, dh.phiVanChuyen, dh.trangThai, "
                + "dh.tenNguoiNhan, dh.diaChiNhan, dh.sdtNhan, dh.ghiChu, dh.nguoiXuLy, dh.lyDoHuy, "
                + "dh.phuongThucThanhToan, dh.trangThaiThanhToan, dh.maChuyenKhoan, dh.anhBill, dh.ghiChuThanhToan, dh.ngayXacNhanThanhToan, "
                + "nd.hoTen AS tenKhachHang, nd.email AS emailKhachHang, nd.soDienThoai AS soDienThoaiKhach, "
                + "nx.hoTen AS tenNguoiXuLy "
                + "FROM donhang dh "
                + "JOIN nguoidung nd ON dh.idNguoiDung = nd.id "
                + "LEFT JOIN nguoidung nx ON dh.nguoiXuLy = nx.id "
                + "WHERE dh.idDonHang = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idDonHang);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DonHang dh = new DonHang();
                    dh.setIdDonHang(rs.getInt("idDonHang"));
                    dh.setIdNguoiDung(rs.getInt("idNguoiDung"));
                    dh.setNgayDat(rs.getTimestamp("ngayDat"));
                    dh.setTongTien(rs.getDouble("tongTien"));
                    dh.setPhiVanChuyen(rs.getDouble("phiVanChuyen"));
                    dh.setTrangThai(rs.getString("trangThai"));
                    dh.setTenNguoiNhan(rs.getString("tenNguoiNhan"));
                    dh.setDiaChiNhan(rs.getString("diaChiNhan"));
                    dh.setSdtNhan(rs.getString("sdtNhan"));
                    dh.setGhiChu(rs.getString("ghiChu"));
                    dh.setLyDoHuy(rs.getString("lyDoHuy"));
                    dh.setPhuongThucThanhToan(rs.getString("phuongThucThanhToan"));
                    dh.setTrangThaiThanhToan(rs.getString("trangThaiThanhToan"));
                    dh.setMaChuyenKhoan(rs.getString("maChuyenKhoan"));
                    dh.setAnhBill(rs.getString("anhBill"));
                    dh.setGhiChuThanhToan(rs.getString("ghiChuThanhToan"));
                    dh.setNgayXacNhanThanhToan(rs.getTimestamp("ngayXacNhanThanhToan"));

                    int nguoiXuLyValue = rs.getInt("nguoiXuLy");
                    dh.setNguoiXuLy(rs.wasNull() ? null : nguoiXuLyValue);

                    dh.setTenKhachHang(rs.getString("tenKhachHang"));
                    dh.setEmailKhachHang(rs.getString("emailKhachHang"));
                    dh.setSoDienThoaiKhach(rs.getString("soDienThoaiKhach"));
                    dh.setTenNguoiXuLy(rs.getString("tenNguoiXuLy"));
                    dh.setDanhSachChiTiet(getChiTietByDonHangId(idDonHang));

                    return dh;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<ChiTietDonHang> getChiTietByDonHangId(int idDonHang) {
        List<ChiTietDonHang> list = new ArrayList<>();

        String sql = "SELECT ctdh.idChiTiet, ctdh.idDonHang, ctdh.idThuoc, ctdh.soLuong, "
                + "ctdh.donGia, ctdh.phiShip, ctdh.thanhTien, ctdh.anhHoaDonSP, ctdh.ghiChuHoaDonSP, "
                + "t.tenThuoc, t.hinhAnh "
                + "FROM chitietdonhang ctdh "
                + "JOIN thuoc t ON ctdh.idThuoc = t.idThuoc "
                + "WHERE ctdh.idDonHang = ? "
                + "ORDER BY ctdh.idChiTiet ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idDonHang);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ChiTietDonHang ct = new ChiTietDonHang();
                    ct.setIdChiTiet(rs.getInt("idChiTiet"));
                    ct.setIdDonHang(rs.getInt("idDonHang"));
                    ct.setIdThuoc(rs.getInt("idThuoc"));
                    ct.setSoLuong(rs.getInt("soLuong"));
                    ct.setDonGia(rs.getDouble("donGia"));
                    ct.setPhiShip(rs.getDouble("phiShip"));
                    ct.setThanhTien(rs.getDouble("thanhTien"));
                    ct.setTenThuoc(rs.getString("tenThuoc"));
                    ct.setHinhAnh(rs.getString("hinhAnh"));
                    ct.setAnhHoaDonSP(rs.getString("anhHoaDonSP"));
                    ct.setGhiChuHoaDonSP(rs.getString("ghiChuHoaDonSP"));
                    list.add(ct);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean updateTrangThaiDonHang(int idDonHang, String trangThai, Integer nguoiXuLy) {
        String sql = "UPDATE donhang SET trangThai = ?, nguoiXuLy = ? WHERE idDonHang = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, trangThai);
            if (nguoiXuLy == null) {
                ps.setNull(2, java.sql.Types.INTEGER);
            } else {
                ps.setInt(2, nguoiXuLy);
            }
            ps.setInt(3, idDonHang);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean capNhatTrangThaiThanhToan(int idDonHang, String trangThaiThanhToan, String ghiChuThanhToan, Integer nguoiXuLy) {
        String sql = "UPDATE donhang SET trangThaiThanhToan = ?, ghiChuThanhToan = ?, nguoiXuLy = ?, "
                + "ngayXacNhanThanhToan = CASE WHEN ? = 'DA_THANH_TOAN' THEN NOW() ELSE ngayXacNhanThanhToan END "
                + "WHERE idDonHang = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, trangThaiThanhToan);
            ps.setString(2, ghiChuThanhToan);

            if (nguoiXuLy == null) {
                ps.setNull(3, java.sql.Types.INTEGER);
            } else {
                ps.setInt(3, nguoiXuLy);
            }

            ps.setString(4, trangThaiThanhToan);
            ps.setInt(5, idDonHang);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public int countAllDonHang() {
        String sql = "SELECT COUNT(*) FROM donhang";

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

    public int countDonHangTheoTrangThai(String trangThai) {
        String sql = "SELECT COUNT(*) FROM donhang WHERE trangThai = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, trangThai);

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

    public int taoDonHangNgay(NguoiDung user, Thuoc thuoc, int soLuong) {
        Connection conn = null;
        PreparedStatement psDonHang = null;
        PreparedStatement psChiTiet = null;
        PreparedStatement psUpdateThuoc = null;
        ResultSet rs = null;

        try {
            if (user == null || thuoc == null || soLuong <= 0) {
                return -1;
            }

            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            if (soLuong > thuoc.getSoLuong()) {
                soLuong = thuoc.getSoLuong();
            }

            if (soLuong <= 0) {
                conn.rollback();
                return -1;
            }

            double tongTien = thuoc.getDonGia() * soLuong;
            double phiVanChuyen = PHI_VAN_CHUYEN_MAC_DINH;

            String diaChiNhan = user.getDiaChi();
            String sdtNhan = user.getSoDienThoai();

            if (diaChiNhan == null || diaChiNhan.trim().isEmpty()) {
                diaChiNhan = "Chưa cập nhật địa chỉ";
            }

            if (sdtNhan == null || sdtNhan.trim().isEmpty()) {
                sdtNhan = "Chưa cập nhật SĐT";
            }

            String sqlDonHang = "INSERT INTO donhang "
                    + "(idNguoiDung, ngayDat, tongTien, phiVanChuyen, trangThai, diaChiNhan, sdtNhan, ghiChu, nguoiXuLy) "
                    + "VALUES (?, NOW(), ?, ?, ?, ?, ?, ?, ?)";

            psDonHang = conn.prepareStatement(sqlDonHang, Statement.RETURN_GENERATED_KEYS);
            psDonHang.setInt(1, user.getId());
            psDonHang.setDouble(2, tongTien);
            psDonHang.setDouble(3, phiVanChuyen);
            psDonHang.setString(4, "CHO_XAC_NHAN");
            psDonHang.setString(5, diaChiNhan);
            psDonHang.setString(6, sdtNhan);
            psDonHang.setString(7, "Đặt hàng nhanh từ giỏ hàng");
            psDonHang.setNull(8, java.sql.Types.INTEGER);

            int insertDonHang = psDonHang.executeUpdate();
            if (insertDonHang <= 0) {
                conn.rollback();
                return -1;
            }

            rs = psDonHang.getGeneratedKeys();
            int idDonHang = -1;
            if (rs.next()) {
                idDonHang = rs.getInt(1);
            }

            if (idDonHang == -1) {
                conn.rollback();
                return -1;
            }

            String sqlChiTiet = "INSERT INTO chitietdonhang "
                    + "(idDonHang, idThuoc, soLuong, donGia, phiShip, thanhTien) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";

            psChiTiet = conn.prepareStatement(sqlChiTiet);
            psChiTiet.setInt(1, idDonHang);
            psChiTiet.setInt(2, thuoc.getIdThuoc());
            psChiTiet.setInt(3, soLuong);
            psChiTiet.setDouble(4, thuoc.getDonGia());
            psChiTiet.setDouble(5, 0);
            psChiTiet.setDouble(6, tongTien);

            int insertChiTiet = psChiTiet.executeUpdate();
            if (insertChiTiet <= 0) {
                conn.rollback();
                return -1;
            }

            String sqlUpdateThuoc = "UPDATE thuoc "
                    + "SET soLuong = soLuong - ? "
                    + "WHERE idThuoc = ? AND soLuong >= ?";

            psUpdateThuoc = conn.prepareStatement(sqlUpdateThuoc);
            psUpdateThuoc.setInt(1, soLuong);
            psUpdateThuoc.setInt(2, thuoc.getIdThuoc());
            psUpdateThuoc.setInt(3, soLuong);

            int updateKho = psUpdateThuoc.executeUpdate();
            if (updateKho <= 0) {
                conn.rollback();
                return -1;
            }

            conn.commit();
            return idDonHang;

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return -1;
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (psUpdateThuoc != null) psUpdateThuoc.close(); } catch (Exception e) {}
            try { if (psChiTiet != null) psChiTiet.close(); } catch (Exception e) {}
            try { if (psDonHang != null) psDonHang.close(); } catch (Exception e) {}
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (Exception e) {}
        }
    }

    public int taoDonHangTheoThongTin(NguoiDung user, Thuoc thuoc, int soLuong,
                                      String tenNguoiNhan, String sdtNhan,
                                      String diaChiNhan, String ghiChu,
                                      String phuongThucThanhToan,
                                      String maChuyenKhoan,
                                      String anhBill,
                                      double phiVanChuyen) {
        Connection conn = null;
        PreparedStatement psDonHang = null;
        PreparedStatement psChiTiet = null;
        PreparedStatement psUpdateThuoc = null;
        ResultSet rs = null;

        try {
            if (user == null || thuoc == null || soLuong <= 0) {
                return -1;
            }

            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            if (soLuong > thuoc.getSoLuong()) {
                soLuong = thuoc.getSoLuong();
            }

            if (soLuong <= 0) {
                conn.rollback();
                return -1;
            }

            double donGia = thuoc.getDonGia();
            double phiShipMotSanPham = thuoc.getPhiShip();
            double tongTien = donGia * soLuong;
            double tongShip = phiShipMotSanPham * soLuong;

            String trangThaiThanhToan = "CHUA_THANH_TOAN";
            if ("CHUYEN_KHOAN".equals(phuongThucThanhToan) || "THE".equals(phuongThucThanhToan)) {
                trangThaiThanhToan = "CHO_XAC_NHAN";
            }

            String sqlDonHang = "INSERT INTO donhang "
                    + "(idNguoiDung, ngayDat, tongTien, phiVanChuyen, trangThai, tenNguoiNhan, diaChiNhan, sdtNhan, ghiChu, "
                    + "phuongThucThanhToan, trangThaiThanhToan, maChuyenKhoan, anhBill, nguoiXuLy) "
                    + "VALUES (?, NOW(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            psDonHang = conn.prepareStatement(sqlDonHang, Statement.RETURN_GENERATED_KEYS);
            psDonHang.setInt(1, user.getId());
            psDonHang.setDouble(2, tongTien);
            psDonHang.setDouble(3, tongShip);
            psDonHang.setString(4, "CHO_XAC_NHAN");
            psDonHang.setString(5, tenNguoiNhan);
            psDonHang.setString(6, diaChiNhan);
            psDonHang.setString(7, sdtNhan);
            psDonHang.setString(8, (ghiChu == null || ghiChu.trim().isEmpty()) ? null : ghiChu.trim());
            psDonHang.setString(9, phuongThucThanhToan);
            psDonHang.setString(10, trangThaiThanhToan);
            psDonHang.setString(11, maChuyenKhoan);
            psDonHang.setString(12, anhBill);
            psDonHang.setNull(13, java.sql.Types.INTEGER);

            int insertDonHang = psDonHang.executeUpdate();
            if (insertDonHang <= 0) {
                conn.rollback();
                return -1;
            }

            rs = psDonHang.getGeneratedKeys();
            int idDonHang = -1;
            if (rs.next()) {
                idDonHang = rs.getInt(1);
            }

            if (idDonHang == -1) {
                conn.rollback();
                return -1;
            }

            String sqlChiTiet = "INSERT INTO chitietdonhang "
                    + "(idDonHang, idThuoc, soLuong, donGia, phiShip, thanhTien) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";

            psChiTiet = conn.prepareStatement(sqlChiTiet);
            psChiTiet.setInt(1, idDonHang);
            psChiTiet.setInt(2, thuoc.getIdThuoc());
            psChiTiet.setInt(3, soLuong);
            psChiTiet.setDouble(4, donGia);
            psChiTiet.setDouble(5, phiShipMotSanPham);
            psChiTiet.setDouble(6, tongTien);

            int insertChiTiet = psChiTiet.executeUpdate();
            if (insertChiTiet <= 0) {
                conn.rollback();
                return -1;
            }

            String sqlUpdateThuoc = "UPDATE thuoc "
                    + "SET soLuong = soLuong - ? "
                    + "WHERE idThuoc = ? AND soLuong >= ?";

            psUpdateThuoc = conn.prepareStatement(sqlUpdateThuoc);
            psUpdateThuoc.setInt(1, soLuong);
            psUpdateThuoc.setInt(2, thuoc.getIdThuoc());
            psUpdateThuoc.setInt(3, soLuong);

            int updateKho = psUpdateThuoc.executeUpdate();
            if (updateKho <= 0) {
                conn.rollback();
                return -1;
            }

            conn.commit();
            return idDonHang;

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return -1;
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (psUpdateThuoc != null) psUpdateThuoc.close(); } catch (Exception e) {}
            try { if (psChiTiet != null) psChiTiet.close(); } catch (Exception e) {}
            try { if (psDonHang != null) psDonHang.close(); } catch (Exception e) {}
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (Exception e) {}
        }
    }

    public int taoDonHangTheoThongTin(NguoiDung user, Thuoc thuoc, int soLuong,
                                      String tenNguoiNhan, String sdtNhan,
                                      String diaChiNhan, String ghiChu,
                                      String phuongThucThanhToan,
                                      String maChuyenKhoan,
                                      String anhBill) {
        return taoDonHangTheoThongTin(
                user,
                thuoc,
                soLuong,
                tenNguoiNhan,
                sdtNhan,
                diaChiNhan,
                ghiChu,
                phuongThucThanhToan,
                maChuyenKhoan,
                anhBill,
                thuoc != null ? (thuoc.getPhiShip() * Math.max(soLuong, 1)) : 0
        );
    }

    public List<DonHang> getDonHangCuaNguoiDung(int idNguoiDung) {
        List<DonHang> list = new ArrayList<>();

        String sql = "SELECT dh.idDonHang, dh.idNguoiDung, dh.ngayDat, dh.tongTien, dh.phiVanChuyen, dh.trangThai, "
                + "dh.tenNguoiNhan, dh.diaChiNhan, dh.sdtNhan, dh.ghiChu, dh.lyDoHuy, "
                + "dh.phuongThucThanhToan, dh.trangThaiThanhToan, dh.maChuyenKhoan, dh.anhBill, dh.ghiChuThanhToan, dh.ngayXacNhanThanhToan, dh.nguoiXuLy, "
                + "(SELECT t.tenThuoc "
                + "   FROM chitietdonhang ct1 "
                + "   JOIN thuoc t ON ct1.idThuoc = t.idThuoc "
                + "   WHERE ct1.idDonHang = dh.idDonHang "
                + "   ORDER BY ct1.idChiTiet ASC LIMIT 1) AS tenSanPhamDaiDien, "
                + "(SELECT t.hinhAnh "
                + "   FROM chitietdonhang ct2 "
                + "   JOIN thuoc t ON ct2.idThuoc = t.idThuoc "
                + "   WHERE ct2.idDonHang = dh.idDonHang "
                + "   ORDER BY ct2.idChiTiet ASC LIMIT 1) AS hinhAnhDaiDien, "
                + "(SELECT COUNT(*) FROM chitietdonhang ct3 WHERE ct3.idDonHang = dh.idDonHang) AS tongSoSanPham "
                + "FROM donhang dh "
                + "WHERE dh.idNguoiDung = ? "
                + "ORDER BY dh.ngayDat DESC, dh.idDonHang DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idNguoiDung);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DonHang dh = new DonHang();
                    dh.setIdDonHang(rs.getInt("idDonHang"));
                    dh.setIdNguoiDung(rs.getInt("idNguoiDung"));
                    dh.setNgayDat(rs.getTimestamp("ngayDat"));
                    dh.setTongTien(rs.getDouble("tongTien"));
                    dh.setPhiVanChuyen(rs.getDouble("phiVanChuyen"));
                    dh.setTrangThai(rs.getString("trangThai"));
                    dh.setTenNguoiNhan(rs.getString("tenNguoiNhan"));
                    dh.setDiaChiNhan(rs.getString("diaChiNhan"));
                    dh.setSdtNhan(rs.getString("sdtNhan"));
                    dh.setGhiChu(rs.getString("ghiChu"));
                    dh.setLyDoHuy(rs.getString("lyDoHuy"));
                    dh.setPhuongThucThanhToan(rs.getString("phuongThucThanhToan"));
                    dh.setTrangThaiThanhToan(rs.getString("trangThaiThanhToan"));
                    dh.setMaChuyenKhoan(rs.getString("maChuyenKhoan"));
                    dh.setAnhBill(rs.getString("anhBill"));
                    dh.setGhiChuThanhToan(rs.getString("ghiChuThanhToan"));
                    dh.setNgayXacNhanThanhToan(rs.getTimestamp("ngayXacNhanThanhToan"));

                    int nguoiXuLyValue = rs.getInt("nguoiXuLy");
                    dh.setNguoiXuLy(rs.wasNull() ? null : nguoiXuLyValue);

                    dh.setTenSanPhamDaiDien(rs.getString("tenSanPhamDaiDien"));
                    dh.setHinhAnhDaiDien(rs.getString("hinhAnhDaiDien"));
                    dh.setTongSoSanPham(rs.getInt("tongSoSanPham"));

                    list.add(dh);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public DonHang getDonHangCuaNguoiDungById(int idDonHang, int idNguoiDung) {
        String sql = "SELECT dh.idDonHang, dh.idNguoiDung, dh.ngayDat, dh.tongTien, dh.phiVanChuyen, dh.trangThai, "
                + "dh.tenNguoiNhan, dh.diaChiNhan, dh.sdtNhan, dh.ghiChu, dh.lyDoHuy, "
                + "dh.phuongThucThanhToan, dh.trangThaiThanhToan, dh.maChuyenKhoan, dh.anhBill, dh.ghiChuThanhToan, dh.ngayXacNhanThanhToan, dh.nguoiXuLy "
                + "FROM donhang dh "
                + "WHERE dh.idDonHang = ? AND dh.idNguoiDung = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idDonHang);
            ps.setInt(2, idNguoiDung);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DonHang dh = new DonHang();
                    dh.setIdDonHang(rs.getInt("idDonHang"));
                    dh.setIdNguoiDung(rs.getInt("idNguoiDung"));
                    dh.setNgayDat(rs.getTimestamp("ngayDat"));
                    dh.setTongTien(rs.getDouble("tongTien"));
                    dh.setPhiVanChuyen(rs.getDouble("phiVanChuyen"));
                    dh.setTrangThai(rs.getString("trangThai"));
                    dh.setTenNguoiNhan(rs.getString("tenNguoiNhan"));
                    dh.setDiaChiNhan(rs.getString("diaChiNhan"));
                    dh.setSdtNhan(rs.getString("sdtNhan"));
                    dh.setGhiChu(rs.getString("ghiChu"));
                    dh.setLyDoHuy(rs.getString("lyDoHuy"));
                    dh.setPhuongThucThanhToan(rs.getString("phuongThucThanhToan"));
                    dh.setTrangThaiThanhToan(rs.getString("trangThaiThanhToan"));
                    dh.setMaChuyenKhoan(rs.getString("maChuyenKhoan"));
                    dh.setAnhBill(rs.getString("anhBill"));
                    dh.setGhiChuThanhToan(rs.getString("ghiChuThanhToan"));
                    dh.setNgayXacNhanThanhToan(rs.getTimestamp("ngayXacNhanThanhToan"));

                    int nguoiXuLyValue = rs.getInt("nguoiXuLy");
                    dh.setNguoiXuLy(rs.wasNull() ? null : nguoiXuLyValue);

                    dh.setDanhSachChiTiet(getChiTietByDonHangId(idDonHang));
                    return dh;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean huyDonHangBoiKhach(int idDonHang, int idNguoiDung) {
        Connection conn = null;
        PreparedStatement psCheck = null;
        PreparedStatement psChiTiet = null;
        PreparedStatement psUpdateThuoc = null;
        PreparedStatement psUpdateDonHang = null;
        ResultSet rsCheck = null;
        ResultSet rsChiTiet = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            String sqlCheck = "SELECT idDonHang, trangThai, ngayDat "
                    + "FROM donhang "
                    + "WHERE idDonHang = ? AND idNguoiDung = ?";

            psCheck = conn.prepareStatement(sqlCheck);
            psCheck.setInt(1, idDonHang);
            psCheck.setInt(2, idNguoiDung);
            rsCheck = psCheck.executeQuery();

            if (!rsCheck.next()) {
                conn.rollback();
                return false;
            }

            String trangThai = rsCheck.getString("trangThai");
            java.sql.Timestamp ngayDat = rsCheck.getTimestamp("ngayDat");

            if (ngayDat == null) {
                conn.rollback();
                return false;
            }

            long now = System.currentTimeMillis();
            long diffMs = now - ngayDat.getTime();
            long limitMs = 8L * 60 * 60 * 1000;

            if (diffMs > limitMs) {
                conn.rollback();
                return false;
            }

            if (!"CHO_XAC_NHAN".equals(trangThai) && !"DA_XAC_NHAN".equals(trangThai)) {
                conn.rollback();
                return false;
            }

            String sqlChiTiet = "SELECT idThuoc, soLuong FROM chitietdonhang WHERE idDonHang = ?";
            psChiTiet = conn.prepareStatement(sqlChiTiet);
            psChiTiet.setInt(1, idDonHang);
            rsChiTiet = psChiTiet.executeQuery();

            String sqlUpdateThuoc = "UPDATE thuoc SET soLuong = soLuong + ? WHERE idThuoc = ?";
            psUpdateThuoc = conn.prepareStatement(sqlUpdateThuoc);

            while (rsChiTiet.next()) {
                int idThuoc = rsChiTiet.getInt("idThuoc");
                int soLuong = rsChiTiet.getInt("soLuong");

                psUpdateThuoc.setInt(1, soLuong);
                psUpdateThuoc.setInt(2, idThuoc);
                psUpdateThuoc.addBatch();
            }
            psUpdateThuoc.executeBatch();

            String sqlUpdateDonHang = "UPDATE donhang "
                    + "SET trangThai = 'DA_HUY' "
                    + "WHERE idDonHang = ? AND idNguoiDung = ?";
            psUpdateDonHang = conn.prepareStatement(sqlUpdateDonHang);
            psUpdateDonHang.setInt(1, idDonHang);
            psUpdateDonHang.setInt(2, idNguoiDung);

            int updated = psUpdateDonHang.executeUpdate();
            if (updated <= 0) {
                conn.rollback();
                return false;
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try { if (rsChiTiet != null) rsChiTiet.close(); } catch (Exception e) {}
            try { if (rsCheck != null) rsCheck.close(); } catch (Exception e) {}
            try { if (psUpdateDonHang != null) psUpdateDonHang.close(); } catch (Exception e) {}
            try { if (psUpdateThuoc != null) psUpdateThuoc.close(); } catch (Exception e) {}
            try { if (psChiTiet != null) psChiTiet.close(); } catch (Exception e) {}
            try { if (psCheck != null) psCheck.close(); } catch (Exception e) {}
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (Exception e) {}
        }
    }

    public boolean capNhatHoaDonSanPham(int idChiTiet, String anhHoaDonSP, String ghiChuHoaDonSP) {
        String sql = "UPDATE chitietdonhang SET anhHoaDonSP = ?, ghiChuHoaDonSP = ? WHERE idChiTiet = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, anhHoaDonSP);
            ps.setString(2, (ghiChuHoaDonSP == null || ghiChuHoaDonSP.trim().isEmpty()) ? null : ghiChuHoaDonSP.trim());
            ps.setInt(3, idChiTiet);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean capNhatPhiVanChuyen(int idDonHang, double phiVanChuyen, Integer nguoiXuLy) {
        String sql = "UPDATE donhang SET phiVanChuyen = ?, nguoiXuLy = ? WHERE idDonHang = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, Math.max(0, phiVanChuyen));

            if (nguoiXuLy == null) {
                ps.setNull(2, java.sql.Types.INTEGER);
            } else {
                ps.setInt(2, nguoiXuLy);
            }

            ps.setInt(3, idDonHang);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean capNhatTrangThaiTheoQuyTrinh(int idDonHang, Integer nguoiXuLy) {
        String sql = "UPDATE donhang "
                + "SET trangThai = CASE "
                + "    WHEN trangThai = 'CHO_XAC_NHAN' THEN 'DANG_CHUAN_BI' "
                + "    WHEN trangThai = 'DANG_CHUAN_BI' THEN 'DANG_GIAO' "
                + "    WHEN trangThai = 'DANG_GIAO' THEN 'DA_GIAO' "
                + "    ELSE trangThai "
                + "END, "
                + "nguoiXuLy = ? "
                + "WHERE idDonHang = ? "
                + "AND trangThai IN ('CHO_XAC_NHAN', 'DANG_CHUAN_BI', 'DANG_GIAO')";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (nguoiXuLy == null) {
                ps.setNull(1, java.sql.Types.INTEGER);
            } else {
                ps.setInt(1, nguoiXuLy);
            }
            ps.setInt(2, idDonHang);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public int xacNhanTatCaDonChoXacNhan(List<Integer> dsIdDonHang, Integer nguoiXuLy) {
        if (dsIdDonHang == null || dsIdDonHang.isEmpty()) {
            return 0;
        }

        String sql = "UPDATE donhang "
                + "SET trangThai = 'DANG_CHUAN_BI', nguoiXuLy = ? "
                + "WHERE idDonHang = ? AND trangThai = 'CHO_XAC_NHAN'";

        int tongCapNhat = 0;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (Integer idDonHang : dsIdDonHang) {
                if (idDonHang == null) {
                    continue;
                }

                if (nguoiXuLy == null) {
                    ps.setNull(1, java.sql.Types.INTEGER);
                } else {
                    ps.setInt(1, nguoiXuLy);
                }
                ps.setInt(2, idDonHang);

                tongCapNhat += ps.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tongCapNhat;
    }

    public boolean huyDonHangCoLyDo(int idDonHang, String lyDoHuy, Integer nguoiXuLy) {
        String sql = "UPDATE donhang "
                + "SET trangThai = 'DA_HUY', lyDoHuy = ?, nguoiXuLy = ? "
                + "WHERE idDonHang = ? AND trangThai IN ('CHO_XAC_NHAN', 'DANG_CHUAN_BI')";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, (lyDoHuy == null || lyDoHuy.trim().isEmpty()) ? "Khách yêu cầu hủy đơn" : lyDoHuy.trim());

            if (nguoiXuLy == null) {
                ps.setNull(2, java.sql.Types.INTEGER);
            } else {
                ps.setInt(2, nguoiXuLy);
            }

            ps.setInt(3, idDonHang);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}