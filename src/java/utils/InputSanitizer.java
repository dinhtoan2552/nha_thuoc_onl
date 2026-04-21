package utils;

public class InputSanitizer {

    private InputSanitizer() {
    }

    public static String trimToNull(String value) {
        if (value == null) {
            return null;
        }

        String cleaned = value.trim();
        return cleaned.isEmpty() ? null : cleaned;
    }

    public static String normalizeEmail(String email) {
        String value = trimToNull(email);
        if (value == null) {
            return null;
        }

        value = value.toLowerCase();

        if (value.length() > 100) {
            value = value.substring(0, 100);
        }

        return value;
    }

    public static String normalizePhone(String phone) {
        String value = trimToNull(phone);
        if (value == null) {
            return null;
        }

        value = value.replaceAll("[^0-9+]", "");

        if (value.length() > 15) {
            value = value.substring(0, 15);
        }

        return value;
    }

    public static String cleanPlainText(String value, int maxLength) {
        String cleaned = trimToNull(value);
        if (cleaned == null) {
            return null;
        }

        cleaned = cleaned
                .replace('\u0000', ' ')
                .replaceAll("[\\r\\n\\t]+", " ")
                .replaceAll("\\s{2,}", " ")
                .trim();

        if (maxLength > 0 && cleaned.length() > maxLength) {
            cleaned = cleaned.substring(0, maxLength);
        }

        return cleaned;
    }

    public static String cleanMultilineText(String value, int maxLength) {
        String cleaned = trimToNull(value);
        if (cleaned == null) {
            return null;
        }

        cleaned = cleaned
                .replace('\u0000', ' ')
                .replace("\r\n", "\n")
                .replace('\r', '\n')
                .replaceAll("[\\x00-\\x08\\x0B\\x0C\\x0E-\\x1F]", "")
                .replaceAll("\\n{3,}", "\n\n")
                .trim();

        if (maxLength > 0 && cleaned.length() > maxLength) {
            cleaned = cleaned.substring(0, maxLength);
        }

        return cleaned;
    }

    public static String escapeHtml(String value) {
        if (value == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder(value.length() + 32);
        for (char c : value.toCharArray()) {
            switch (c) {
                case '&':
                    sb.append("&amp;");
                    break;
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '"':
                    sb.append("&quot;");
                    break;
                case '\'':
                    sb.append("&#39;");
                    break;
                case '/':
                    sb.append("&#47;");
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }

    public static boolean containsHtmlRisk(String value) {
        if (value == null) {
            return false;
        }

        String lower = value.toLowerCase();
        return lower.contains("<script")
                || lower.contains("</script")
                || lower.contains("javascript:")
                || lower.contains("onerror=")
                || lower.contains("onload=")
                || lower.contains("onclick=")
                || lower.contains("<iframe")
                || lower.contains("<object")
                || lower.contains("<embed");
    }
}