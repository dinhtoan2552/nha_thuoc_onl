package utils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class FileUploadUtil {

    private static final String DEFAULT_UPLOAD_DIR = "D:/ql_thuoc_uploads";
    private static final long MAX_IMAGE_SIZE = 5L * 1024 * 1024; // 5MB

    private static final Set<String> ALLOWED_EXTENSIONS = new HashSet<>(Arrays.asList(
            ".jpg", ".jpeg", ".png", ".webp"
    ));

    private static final Set<String> ALLOWED_CONTENT_TYPES = new HashSet<>(Arrays.asList(
            "image/jpeg", "image/png", "image/webp"
    ));

    public static String getUploadDir() {
        String configured = System.getProperty("upload.dir");
        if (configured != null && !configured.trim().isEmpty()) {
            return configured.trim();
        }
        return DEFAULT_UPLOAD_DIR;
    }

    public static void createUploadDirIfNotExists() throws IOException {
        Path dir = Paths.get(getUploadDir()).normalize().toAbsolutePath();
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }
        if (!Files.isDirectory(dir) || !Files.isWritable(dir)) {
            throw new IOException("Thư mục upload không hợp lệ hoặc không có quyền ghi.");
        }
    }

    public static String saveImage(Part filePart) throws IOException, ServletException {
        if (filePart == null) {
            return null;
        }

        if (filePart.getSize() <= 0) {
            return null;
        }

        if (filePart.getSize() > MAX_IMAGE_SIZE) {
            return null;
        }

        String submittedFileName = getSafeSubmittedFileName(filePart);
        String extension = getExtension(submittedFileName);
        String contentType = normalizeContentType(filePart.getContentType());

        if (!isAllowedExtension(extension)) {
            return null;
        }

        if (!isAllowedContentType(contentType)) {
            return null;
        }

        createUploadDirIfNotExists();

        String finalExtension = normalizeExtension(extension, contentType);
        String newFileName = UUID.randomUUID().toString() + finalExtension;

        Path uploadDir = Paths.get(getUploadDir()).normalize().toAbsolutePath();
        Path targetPath = uploadDir.resolve(newFileName).normalize();

        if (!targetPath.startsWith(uploadDir)) {
            return null;
        }

        try (InputStream in = filePart.getInputStream()) {
            Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
        }

        if (!Files.exists(targetPath) || Files.size(targetPath) <= 0) {
            return null;
        }

        String detectedType = Files.probeContentType(targetPath);
        detectedType = normalizeContentType(detectedType);

        if (!isAllowedContentType(detectedType)) {
            Files.deleteIfExists(targetPath);
            return null;
        }

        return newFileName;
    }

    public static boolean isAllowedImageFileName(String fileName) {
        if (fileName == null) {
            return false;
        }

        String clean = fileName.trim();
        if (clean.isEmpty()) {
            return false;
        }

        if (clean.contains("..") || clean.contains("/") || clean.contains("\\") || clean.contains("\0")) {
            return false;
        }

        String extension = getExtension(clean);
        if (!isAllowedExtension(extension)) {
            return false;
        }

        return clean.matches("^[a-zA-Z0-9\\-]{36}\\.(jpg|jpeg|png|webp)$");
    }

    private static String getSafeSubmittedFileName(Part filePart) {
        String submitted = filePart.getSubmittedFileName();
        if (submitted == null) {
            return "";
        }

        submitted = submitted.trim();
        int lastSlash = Math.max(submitted.lastIndexOf('/'), submitted.lastIndexOf('\\'));
        if (lastSlash >= 0) {
            submitted = submitted.substring(lastSlash + 1);
        }

        return submitted;
    }

    private static String getExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }

        int lastDot = fileName.lastIndexOf('.');
        if (lastDot < 0 || lastDot == fileName.length() - 1) {
            return "";
        }

        return fileName.substring(lastDot).toLowerCase();
    }

    private static boolean isAllowedExtension(String extension) {
        return ALLOWED_EXTENSIONS.contains(extension);
    }

    private static boolean isAllowedContentType(String contentType) {
        return ALLOWED_CONTENT_TYPES.contains(contentType);
    }

    private static String normalizeContentType(String contentType) {
        if (contentType == null) {
            return "";
        }
        return contentType.trim().toLowerCase();
    }

    private static String normalizeExtension(String extension, String contentType) {
        if (".jpeg".equals(extension)) {
            return ".jpg";
        }

        if (extension == null || extension.isEmpty()) {
            if ("image/jpeg".equals(contentType)) {
                return ".jpg";
            }
            if ("image/png".equals(contentType)) {
                return ".png";
            }
            if ("image/webp".equals(contentType)) {
                return ".webp";
            }
        }

        return extension;
    }
}