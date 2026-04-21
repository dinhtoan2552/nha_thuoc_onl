package model;

import java.sql.Timestamp;

public class HoTroTinNhan {
    private int idTinNhan;
    private int idPhien;
    private int idNguoiGui;
    private String loaiNguoiGui;
    private String noiDung;
    private String anhDinhKem;
    private Timestamp ngayGui;
    private String tenNguoiGui;

    public int getIdTinNhan() {
        return idTinNhan;
    }

    public void setIdTinNhan(int idTinNhan) {
        this.idTinNhan = idTinNhan;
    }

    public int getIdPhien() {
        return idPhien;
    }

    public void setIdPhien(int idPhien) {
        this.idPhien = idPhien;
    }

    public int getIdNguoiGui() {
        return idNguoiGui;
    }

    public void setIdNguoiGui(int idNguoiGui) {
        this.idNguoiGui = idNguoiGui;
    }

    public String getLoaiNguoiGui() {
        return loaiNguoiGui;
    }

    public void setLoaiNguoiGui(String loaiNguoiGui) {
        this.loaiNguoiGui = loaiNguoiGui;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getAnhDinhKem() {
        return anhDinhKem;
    }

    public void setAnhDinhKem(String anhDinhKem) {
        this.anhDinhKem = anhDinhKem;
    }

    public Timestamp getNgayGui() {
        return ngayGui;
    }

    public void setNgayGui(Timestamp ngayGui) {
        this.ngayGui = ngayGui;
    }

    public String getTenNguoiGui() {
        return tenNguoiGui;
    }

    public void setTenNguoiGui(String tenNguoiGui) {
        this.tenNguoiGui = tenNguoiGui;
    }
}