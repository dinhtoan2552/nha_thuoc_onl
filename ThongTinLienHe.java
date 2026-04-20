package model;

public class ThongTinLienHe {

    private int id;
    private String soZalo;
    private String linkMessenger;
    private String soHotline;
    private String diaChiNhaThuoc;   // mới
    private String noiDungBanner;    // mới
    private String trangThai;

    public ThongTinLienHe() {
    }

    // ===== ID =====
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // ===== ZALO =====
    public String getSoZalo() {
        return soZalo;
    }

    public void setSoZalo(String soZalo) {
        this.soZalo = soZalo;
    }

    // ===== MESSENGER =====
    public String getLinkMessenger() {
        return linkMessenger;
    }

    public void setLinkMessenger(String linkMessenger) {
        this.linkMessenger = linkMessenger;
    }

    // ===== HOTLINE =====
    public String getSoHotline() {
        return soHotline;
    }

    public void setSoHotline(String soHotline) {
        this.soHotline = soHotline;
    }

    // ===== ĐỊA CHỈ =====
    public String getDiaChiNhaThuoc() {
        return diaChiNhaThuoc;
    }

    public void setDiaChiNhaThuoc(String diaChiNhaThuoc) {
        this.diaChiNhaThuoc = diaChiNhaThuoc;
    }

    // ===== BANNER =====
    public String getNoiDungBanner() {
        return noiDungBanner;
    }

    public void setNoiDungBanner(String noiDungBanner) {
        this.noiDungBanner = noiDungBanner;
    }

    // ===== TRẠNG THÁI =====
    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}