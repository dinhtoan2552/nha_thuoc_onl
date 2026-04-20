package model;

import java.sql.Timestamp;

public class CauHinhThanhToan {
    private int id;

    // QR chuyển khoản
    private String tenNganHangQr;
    private String soTaiKhoanQr;
    private String chuTaiKhoanQr;
    private String anhQR;

    // Thanh toán thẻ
    private String tenNganHangThe;
    private String soTaiKhoanThe;
    private String chiNhanhThe;
    private String chuThe;
    private String anhThe;

    // Bật / tắt phương thức
    private boolean batCOD;
    private boolean batChuyenKhoan;
    private boolean batThe;

    private Timestamp ngayCapNhat;

    public CauHinhThanhToan() {
    }

    public CauHinhThanhToan(int id,
                            String tenNganHangQr,
                            String soTaiKhoanQr,
                            String chuTaiKhoanQr,
                            String anhQR,
                            String tenNganHangThe,
                            String soTaiKhoanThe,
                            String chiNhanhThe,
                            String chuThe,
                            String anhThe,
                            boolean batCOD,
                            boolean batChuyenKhoan,
                            boolean batThe,
                            Timestamp ngayCapNhat) {
        this.id = id;
        this.tenNganHangQr = tenNganHangQr;
        this.soTaiKhoanQr = soTaiKhoanQr;
        this.chuTaiKhoanQr = chuTaiKhoanQr;
        this.anhQR = anhQR;
        this.tenNganHangThe = tenNganHangThe;
        this.soTaiKhoanThe = soTaiKhoanThe;
        this.chiNhanhThe = chiNhanhThe;
        this.chuThe = chuThe;
        this.anhThe = anhThe;
        this.batCOD = batCOD;
        this.batChuyenKhoan = batChuyenKhoan;
        this.batThe = batThe;
        this.ngayCapNhat = ngayCapNhat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenNganHangQr() {
        return tenNganHangQr;
    }

    public void setTenNganHangQr(String tenNganHangQr) {
        this.tenNganHangQr = tenNganHangQr;
    }

    public String getSoTaiKhoanQr() {
        return soTaiKhoanQr;
    }

    public void setSoTaiKhoanQr(String soTaiKhoanQr) {
        this.soTaiKhoanQr = soTaiKhoanQr;
    }

    public String getChuTaiKhoanQr() {
        return chuTaiKhoanQr;
    }

    public void setChuTaiKhoanQr(String chuTaiKhoanQr) {
        this.chuTaiKhoanQr = chuTaiKhoanQr;
    }

    public String getAnhQR() {
        return anhQR;
    }

    public void setAnhQR(String anhQR) {
        this.anhQR = anhQR;
    }

    public String getTenNganHangThe() {
        return tenNganHangThe;
    }

    public void setTenNganHangThe(String tenNganHangThe) {
        this.tenNganHangThe = tenNganHangThe;
    }

    public String getSoTaiKhoanThe() {
        return soTaiKhoanThe;
    }

    public void setSoTaiKhoanThe(String soTaiKhoanThe) {
        this.soTaiKhoanThe = soTaiKhoanThe;
    }

    public String getChiNhanhThe() {
        return chiNhanhThe;
    }

    public void setChiNhanhThe(String chiNhanhThe) {
        this.chiNhanhThe = chiNhanhThe;
    }

    public String getChuThe() {
        return chuThe;
    }

    public void setChuThe(String chuThe) {
        this.chuThe = chuThe;
    }

    public String getAnhThe() {
        return anhThe;
    }

    public void setAnhThe(String anhThe) {
        this.anhThe = anhThe;
    }

    public boolean isBatCOD() {
        return batCOD;
    }

    public void setBatCOD(boolean batCOD) {
        this.batCOD = batCOD;
    }

    public boolean isBatChuyenKhoan() {
        return batChuyenKhoan;
    }

    public void setBatChuyenKhoan(boolean batChuyenKhoan) {
        this.batChuyenKhoan = batChuyenKhoan;
    }

    public boolean isBatThe() {
        return batThe;
    }

    public void setBatThe(boolean batThe) {
        this.batThe = batThe;
    }

    public Timestamp getNgayCapNhat() {
        return ngayCapNhat;
    }

    public void setNgayCapNhat(Timestamp ngayCapNhat) {
        this.ngayCapNhat = ngayCapNhat;
    }
}