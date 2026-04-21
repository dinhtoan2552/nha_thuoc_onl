package model;

public class ThongKeNgay {
    private String nhan;
    private double giaTri;

    public ThongKeNgay() {
    }

    public ThongKeNgay(String nhan, double giaTri) {
        this.nhan = nhan;
        this.giaTri = giaTri;
    }

    public String getNhan() {
        return nhan;
    }

    public void setNhan(String nhan) {
        this.nhan = nhan;
    }

    public double getGiaTri() {
        return giaTri;
    }

    public void setGiaTri(double giaTri) {
        this.giaTri = giaTri;
    }
}