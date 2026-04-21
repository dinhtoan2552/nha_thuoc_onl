package model;

public class GioHangItem {
    private int idChiTiet;
    private int idThuoc;
    private String tenThuoc;
    private String hinhAnh;
    private double donGia;
    private int soLuong;
    private double thanhTien;

    public GioHangItem() {
    }

    public int getIdChiTiet() {
        return idChiTiet;
    }

    public void setIdChiTiet(int idChiTiet) {
        this.idChiTiet = idChiTiet;
    }

    public int getIdThuoc() {
        return idThuoc;
    }

    public void setIdThuoc(int idThuoc) {
        this.idThuoc = idThuoc;
    }

    public String getTenThuoc() {
        return tenThuoc;
    }

    public void setTenThuoc(String tenThuoc) {
        this.tenThuoc = tenThuoc;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }
}