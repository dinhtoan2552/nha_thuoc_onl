package model;

import java.sql.Timestamp;

public class HoTroPhien {
    private int idPhien;
    private int idNguoiDung;
    private String tieuDe;
    private String trangThai;
    private Integer idNhanVienXuLy;
    private Timestamp ngayTao;
    private Timestamp ngayCapNhat;

    private String tenKhachHang;
    private String tenNhanVienXuLy;
    private String tinNhanCuoi;
    private Timestamp thoiGianTinNhanCuoi;

    private int soTinChuaDoc;

    public int getIdPhien() {
        return idPhien;
    }

    public void setIdPhien(int idPhien) {
        this.idPhien = idPhien;
    }

    public int getIdNguoiDung() {
        return idNguoiDung;
    }

    public void setIdNguoiDung(int idNguoiDung) {
        this.idNguoiDung = idNguoiDung;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public Integer getIdNhanVienXuLy() {
        return idNhanVienXuLy;
    }

    public void setIdNhanVienXuLy(Integer idNhanVienXuLy) {
        this.idNhanVienXuLy = idNhanVienXuLy;
    }

    public Timestamp getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Timestamp ngayTao) {
        this.ngayTao = ngayTao;
    }

    public Timestamp getNgayCapNhat() {
        return ngayCapNhat;
    }

    public void setNgayCapNhat(Timestamp ngayCapNhat) {
        this.ngayCapNhat = ngayCapNhat;
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }

    public String getTenNhanVienXuLy() {
        return tenNhanVienXuLy;
    }

    public void setTenNhanVienXuLy(String tenNhanVienXuLy) {
        this.tenNhanVienXuLy = tenNhanVienXuLy;
    }

    public String getTinNhanCuoi() {
        return tinNhanCuoi;
    }

    public void setTinNhanCuoi(String tinNhanCuoi) {
        this.tinNhanCuoi = tinNhanCuoi;
    }

    public Timestamp getThoiGianTinNhanCuoi() {
        return thoiGianTinNhanCuoi;
    }

    public void setThoiGianTinNhanCuoi(Timestamp thoiGianTinNhanCuoi) {
        this.thoiGianTinNhanCuoi = thoiGianTinNhanCuoi;
    }

    public int getSoTinChuaDoc() {
        return soTinChuaDoc;
    }

    public void setSoTinChuaDoc(int soTinChuaDoc) {
        this.soTinChuaDoc = soTinChuaDoc;
    }
}