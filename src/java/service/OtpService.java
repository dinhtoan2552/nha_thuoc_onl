package service;

import dao.OtpDAO;
import model.OtpXacThuc;
import utils.EmailUtil;
import utils.OtpUtil;
import utils.PasswordUtil;

public class OtpService {

    public static final String MUC_DICH_DANG_KY = "DANG_KY";
    public static final String MUC_DICH_QUEN_MAT_KHAU = "QUEN_MAT_KHAU";
    public static final String MUC_DICH_DOI_MAT_KHAU = "DOI_MAT_KHAU";
    public static final String MUC_DICH_DOI_EMAIL = "DOI_EMAIL";

    private final OtpDAO otpDAO = new OtpDAO();

    public static class OtpResult {
        private final boolean success;
        private final String message;

        public OtpResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
    }

    public OtpResult guiOtp(String email, String mucDich) {
        try {
            String emailChuan = OtpUtil.normalizeEmail(email);
            if (emailChuan == null || emailChuan.isEmpty()) {
                return new OtpResult(false, "Email không hợp lệ.");
            }

            String otpRaw = OtpUtil.generate6DigitOtp();
            String otpHash = PasswordUtil.hashPassword(otpRaw);

            otpDAO.voHieuHoaOtpCu(emailChuan, mucDich);
            boolean taoThanhCong = otpDAO.taoOtp(emailChuan, otpHash, mucDich, OtpUtil.getExpiryTimestamp());

            if (!taoThanhCong) {
                return new OtpResult(false, "Không thể tạo OTP.");
            }

            if (MUC_DICH_DANG_KY.equals(mucDich)) {
                EmailUtil.sendRegisterOtp(emailChuan, otpRaw);
            } else if (MUC_DICH_QUEN_MAT_KHAU.equals(mucDich)) {
                EmailUtil.sendForgotPasswordOtp(emailChuan, otpRaw);
            } else if (MUC_DICH_DOI_MAT_KHAU.equals(mucDich)) {
                EmailUtil.sendChangePasswordOtp(emailChuan, otpRaw);
            } else if (MUC_DICH_DOI_EMAIL.equals(mucDich)) {
                EmailUtil.sendEmail(
                        emailChuan,
                        "Ma xac minh doi gmail",
                        "Ma OTP cua ban la: " + otpRaw + ". Ma co hieu luc trong 5 phut."
                );
            } else {
                EmailUtil.sendEmail(
                        emailChuan,
                        "Ma xac minh OTP",
                        "Ma OTP cua ban la: " + otpRaw + ". Ma co hieu luc trong 5 phut."
                );
            }

            return new OtpResult(true, "Đã gửi OTP đến email của bạn.");
        } catch (Exception e) {
            e.printStackTrace();
            return new OtpResult(false, "Gửi OTP thất bại.");
        }
    }

    public OtpResult xacMinhOtp(String email, String mucDich, String otpNguoiDungNhap) {
        String emailChuan = OtpUtil.normalizeEmail(email);

        if (emailChuan == null || emailChuan.isEmpty()) {
            return new OtpResult(false, "Email không hợp lệ.");
        }

        if (otpNguoiDungNhap == null || !otpNguoiDungNhap.matches("\\d{6}")) {
            return new OtpResult(false, "OTP không hợp lệ.");
        }

        OtpXacThuc otpDb = otpDAO.layOtpConHieuLucMoiNhat(emailChuan, mucDich);
        if (otpDb == null) {
            return new OtpResult(false, "OTP không tồn tại hoặc đã hết hạn.");
        }

        if (otpDb.getSoLanSai() >= OtpUtil.MAX_VERIFY_ATTEMPTS) {
            otpDAO.danhDauDaDung(otpDb.getId());
            return new OtpResult(false, "Bạn đã nhập sai OTP quá nhiều lần.");
        }

        boolean dung = PasswordUtil.verifyPassword(otpNguoiDungNhap, otpDb.getOtpHash());
        if (!dung) {
            otpDAO.tangSoLanSai(otpDb.getId());

            OtpXacThuc otpCapNhat = otpDAO.layOtpConHieuLucMoiNhat(emailChuan, mucDich);
            if (otpCapNhat != null && otpCapNhat.getSoLanSai() >= OtpUtil.MAX_VERIFY_ATTEMPTS) {
                otpDAO.danhDauDaDung(otpCapNhat.getId());
                return new OtpResult(false, "Bạn đã nhập sai OTP quá 5 lần. OTP đã bị hủy.");
            }

            return new OtpResult(false, "OTP không đúng.");
        }

        otpDAO.danhDauDaDung(otpDb.getId());
        return new OtpResult(true, "Xác minh OTP thành công.");
    }
}