package utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.security.SecureRandom;
import java.util.Base64;

public class CsrfUtil {

    private static final String CSRF_TOKEN_SESSION_KEY = "csrf_token";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static String getToken(HttpSession session) {
        if (session == null) {
            return null;
        }

        String token = (String) session.getAttribute(CSRF_TOKEN_SESSION_KEY);
        if (token == null || token.trim().isEmpty()) {
            token = generateToken();
            session.setAttribute(CSRF_TOKEN_SESSION_KEY, token);
        }
        return token;
    }

    public static void refreshToken(HttpSession session) {
        if (session != null) {
            session.setAttribute(CSRF_TOKEN_SESSION_KEY, generateToken());
        }
    }

    public static boolean isValid(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }

        String sessionToken = (String) session.getAttribute(CSRF_TOKEN_SESSION_KEY);
        String requestToken = request.getParameter("csrfToken");

        if (sessionToken == null || requestToken == null) {
            return false;
        }

        return sessionToken.equals(requestToken);
    }

    private static String generateToken() {
        byte[] randomBytes = new byte[32];
        SECURE_RANDOM.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}