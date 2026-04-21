package utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    private static final int BCRYPT_ROUNDS = 12;

    public static String hashPassword(String rawPassword) {
        if (rawPassword == null) {
            return null;
        }
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt(BCRYPT_ROUNDS));
    }

    public static boolean verifyPassword(String rawPassword, String hashedPassword) {
        if (rawPassword == null || hashedPassword == null) {
            return false;
        }
        if (!isBcryptHash(hashedPassword)) {
            return false;
        }
        try {
            return BCrypt.checkpw(rawPassword, hashedPassword);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isBcryptHash(String value) {
        if (value == null) {
            return false;
        }
        return value.startsWith("$2a$") || value.startsWith("$2b$") || value.startsWith("$2y$");
    }

    public static boolean isStrongPassword(String password) {
        if (password == null) {
            return false;
        }

        if (password.length() < 8) {
            return false;
        }

        boolean hasLetter = false;
        boolean hasDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                hasLetter = true;
            }
            if (Character.isDigit(c)) {
                hasDigit = true;
            }
        }

        return hasLetter && hasDigit;
    }
}