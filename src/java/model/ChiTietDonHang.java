package model;

public class ChiTietDonHang {
    private int idChiTiet;
    private int idDonHang;
    private int idThuoc;
    private int soLuong;
    private double donGia;
    private double phiShip;
    private double thanhTien;

    private String tenThuoc;
    private String hinhAnh;

    private String anhHoaDonSP;
    private String ghiChuHoaDonSP;

    public ChiTietDonHang() {
    }

    public ChiTietDonHang(int idChiTiet, int idDonHang, int idThuoc, int soLuong,
                          double donGia, double phiShip, double thanhTien,
                          String tenThuoc, String hinhAnh) {
        this.idChiTiet = idChiTiet;
        this.idDonHang = idDonHang;
        this.idThuoc = idThuoc;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.phiShip = phiShip;
        this.thanhTien = thanhTien;
        this.tenThuoc = tenThuoc;
        this.hinhAnh = hinhAnh;
    }

    public int getIdChiTiet() {
        return idChiTiet;
    }

    public void setIdChiTiet(int idChiTiet) {
        this.idChiTiet = idChiTiet;
    }

    public int getIdDonHang() {
        return idDonHang;
    }

    public void setIdDonHang(int idDonHang) {
        this.idDonHang = idDonHang;
    }

    public int getIdThuoc() {
        return idThuoc;
    }

    public void setIdThuoc(int idThuoc) {
        this.idThuoc = idThuoc;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public double getPhiShip() {
        return phiShip;
    }

    public void setPhiShip(double phiShip) {
        this.phiShip = phiShip;
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
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

    public String getAnhHoaDonSP() {
        return anhHoaDonSP;
    }

    public void setAnhHoaDonSP(String anhHoaDonSP) {
        this.anhHoaDonSP = anhHoaDonSP;
    }

    public String getGhiChuHoaDonSP() {
        return ghiChuHoaDonSP;
    }

    public void setGhiChuHoaDonSP(String ghiChuHoaDonSP) {
        this.ghiChuHoaDonSP = ghiChuHoaDonSP;
    }
}