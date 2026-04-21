package model;

import java.sql.Timestamp;
import java.util.List;

public class DonHang {
    private int idDonHang;
    private int idNguoiDung;
    private Timestamp ngayDat;
    private double tongTien;
    private double phiVanChuyen;
    private String trangThai;
    private String tenNguoiNhan;
    private String diaChiNhan;
    private String sdtNhan;
    private String ghiChu;
    private String phuongThucThanhToan;
    private String trangThaiThanhToan;
    private Integer nguoiXuLy;
    private String hinhAnhDaiDien;
    private String tenSanPhamDaiDien;
    private int tongSoSanPham;
    private String maChuyenKhoan;
    private String anhBill;
    private String ghiChuThanhToan;
    private java.sql.Timestamp ngayXacNhanThanhToan;
    private String tenKhachHang;
    private String emailKhachHang;
    private String soDienThoaiKhach;
    private String tenNguoiXuLy;
    private String lyDoHuy;

    private List<ChiTietDonHang> danhSachChiTiet;

    public DonHang() {
    }

    public int getIdDonHang() {
        return idDonHang;
    }

    public void setIdDonHang(int idDonHang) {
        this.idDonHang = idDonHang;
    }

    public int getIdNguoiDung() {
        return idNguoiDung;
    }

    public void setIdNguoiDung(int idNguoiDung) {
        this.idNguoiDung = idNguoiDung;
    }

    public Timestamp getNgayDat() {
        return ngayDat;
    }

    public void setNgayDat(Timestamp ngayDat) {
        this.ngayDat = ngayDat;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public double getPhiVanChuyen() {
        return phiVanChuyen;
    }

    public void setPhiVanChuyen(double phiVanChuyen) {
        this.phiVanChuyen = phiVanChuyen;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getTenNguoiNhan() {
        return tenNguoiNhan;
    }

    public void setTenNguoiNhan(String tenNguoiNhan) {
        this.tenNguoiNhan = tenNguoiNhan;
    }

    public String getDiaChiNhan() {
        return diaChiNhan;
    }

    public void setDiaChiNhan(String diaChiNhan) {
        this.diaChiNhan = diaChiNhan;
    }

    public String getSdtNhan() {
        return sdtNhan;
    }

    public void setSdtNhan(String sdtNhan) {
        this.sdtNhan = sdtNhan;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getPhuongThucThanhToan() {
        return phuongThucThanhToan;
    }

    public void setPhuongThucThanhToan(String phuongThucThanhToan) {
        this.phuongThucThanhToan = phuongThucThanhToan;
    }

    public String getTrangThaiThanhToan() {
        return trangThaiThanhToan;
    }

    public void setTrangThaiThanhToan(String trangThaiThanhToan) {
        this.trangThaiThanhToan = trangThaiThanhToan;
    }

    public Integer getNguoiXuLy() {
        return nguoiXuLy;
    }

    public void setNguoiXuLy(Integer nguoiXuLy) {
        this.nguoiXuLy = nguoiXuLy;
    }

    public String getHinhAnhDaiDien() {
        return hinhAnhDaiDien;
    }

    public void setHinhAnhDaiDien(String hinhAnhDaiDien) {
        this.hinhAnhDaiDien = hinhAnhDaiDien;
    }

    public String getTenSanPhamDaiDien() {
        return tenSanPhamDaiDien;
    }

    public void setTenSanPhamDaiDien(String tenSanPhamDaiDien) {
        this.tenSanPhamDaiDien = tenSanPhamDaiDien;
    }

    public int getTongSoSanPham() {
        return tongSoSanPham;
    }

    public void setTongSoSanPham(int tongSoSanPham) {
        this.tongSoSanPham = tongSoSanPham;
    }

    public String getMaChuyenKhoan() {
        return maChuyenKhoan;
    }

    public void setMaChuyenKhoan(String maChuyenKhoan) {
        this.maChuyenKhoan = maChuyenKhoan;
    }

    public String getAnhBill() {
        return anhBill;
    }

    public void setAnhBill(String anhBill) {
        this.anhBill = anhBill;
    }

    public String getGhiChuThanhToan() {
        return ghiChuThanhToan;
    }

    public void setGhiChuThanhToan(String ghiChuThanhToan) {
        this.ghiChuThanhToan = ghiChuThanhToan;
    }

    public java.sql.Timestamp getNgayXacNhanThanhToan() {
        return ngayXacNhanThanhToan;
    }

    public void setNgayXacNhanThanhToan(java.sql.Timestamp ngayXacNhanThanhToan) {
        this.ngayXacNhanThanhToan = ngayXacNhanThanhToan;
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }

    public String getEmailKhachHang() {
        return emailKhachHang;
    }

    public void setEmailKhachHang(String emailKhachHang) {
        this.emailKhachHang = emailKhachHang;
    }

    public String getSoDienThoaiKhach() {
        return soDienThoaiKhach;
    }

    public void setSoDienThoaiKhach(String soDienThoaiKhach) {
        this.soDienThoaiKhach = soDienThoaiKhach;
    }

    public String getTenNguoiXuLy() {
        return tenNguoiXuLy;
    }

    public void setTenNguoiXuLy(String tenNguoiXuLy) {
        this.tenNguoiXuLy = tenNguoiXuLy;
    }

    public String getLyDoHuy() {
        return lyDoHuy;
    }

    public void setLyDoHuy(String lyDoHuy) {
        this.lyDoHuy = lyDoHuy;
    }

    public List<ChiTietDonHang> getDanhSachChiTiet() {
        return danhSachChiTiet;
    }

    public void setDanhSachChiTiet(List<ChiTietDonHang> danhSachChiTiet) {
        this.danhSachChiTiet = danhSachChiTiet;
    }
}