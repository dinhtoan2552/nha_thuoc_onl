package utils;

import java.sql.Timestamp;
import java.security.SecureRandom;
import java.time.LocalDateTime;

public class OtpUtil {
    private static final SecureRandom RANDOM = new SecureRandom();
    public static final int OTP_EXPIRE_MINUTES = 5;
    public static final int MAX_VERIFY_ATTEMPTS = 5;

    private OtpUtil() {
    }

    public static String generate6DigitOtp() {
        int value = 100000 + RANDOM.nextInt(900000);
        return String.valueOf(value);
    }

    public static Timestamp getExpiryTimestamp() {
        return Timestamp.valueOf(LocalDateTime.now().plusMinutes(OTP_EXPIRE_MINUTES));
    }

    public static String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }
}