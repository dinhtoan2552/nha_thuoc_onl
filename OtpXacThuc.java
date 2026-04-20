package model;

import java.sql.Timestamp;

public class OtpXacThuc {
    private int id;
    private String email;
    private String otpHash;
    private String mucDich;
    private Timestamp hetHanLuc;
    private boolean daDung;
    private int soLanSai;
    private Timestamp taoLuc;

    public OtpXacThuc() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtpHash() {
        return otpHash;
    }

    public void setOtpHash(String otpHash) {
        this.otpHash = otpHash;
    }

    public String getMucDich() {
        return mucDich;
    }

    public void setMucDich(String mucDich) {
        this.mucDich = mucDich;
    }

    public Timestamp getHetHanLuc() {
        return hetHanLuc;
    }

    public void setHetHanLuc(Timestamp hetHanLuc) {
        this.hetHanLuc = hetHanLuc;
    }

    public boolean isDaDung() {
        return daDung;
    }

    public void setDaDung(boolean daDung) {
        this.daDung = daDung;
    }

    public int getSoLanSai() {
        return soLanSai;
    }

    public void setSoLanSai(int soLanSai) {
        this.soLanSai = soLanSai;
    }

    public Timestamp getTaoLuc() {
        return taoLuc;
    }

    public void setTaoLuc(Timestamp taoLuc) {
        this.taoLuc = taoLuc;
    }
}