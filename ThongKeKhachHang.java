package model;

public class ThongKeKhachHang {
    private int idNguoiDung;
    private String hoTen;
    private String email;
    private int tongDonHang;
    private double tongChiTieu;

    public int getIdNguoiDung() {
        return idNguoiDung;
    }

    public void setIdNguoiDung(int idNguoiDung) {
        this.idNguoiDung = idNguoiDung;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTongDonHang() {
        return tongDonHang;
    }

    public void setTongDonHang(int tongDonHang) {
        this.tongDonHang = tongDonHang;
    }

    public double getTongChiTieu() {
        return tongChiTieu;
    }

    public void setTongChiTieu(double tongChiTieu) {
        this.tongChiTieu = tongChiTieu;
    }
}