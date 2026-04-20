package controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import utils.FileUploadUtil;

@WebServlet("/load-image")
public class LoadImageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String fileName = request.getParameter("name");

        if (fileName == null || fileName.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu tên ảnh");
            return;
        }

        fileName = fileName.trim();

        if (!FileUploadUtil.isAllowedImageFileName(fileName)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Tên ảnh không hợp lệ");
            return;
        }

        Path uploadDir = Paths.get(FileUploadUtil.getUploadDir()).normalize().toAbsolutePath();
        Path filePath = uploadDir.resolve(fileName).normalize();

        if (!filePath.startsWith(uploadDir)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Đường dẫn ảnh không hợp lệ");
            return;
        }

        if (!Files.exists(filePath) || !Files.isRegularFile(filePath)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy ảnh");
            return;
        }

        String contentType = Files.probeContentType(filePath);
        if (contentType == null) {
            String lower = fileName.toLowerCase();
            if (lower.endsWith(".png")) {
                contentType = "image/png";
            } else if (lower.endsWith(".webp")) {
                contentType = "image/webp";
            } else {
                contentType = "image/jpeg";
            }
        }

        contentType = contentType.toLowerCase();
        if (!contentType.equals("image/jpeg")
                && !contentType.equals("image/png")
                && !contentType.equals("image/webp")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Định dạng ảnh không được hỗ trợ");
            return;
        }

        response.setContentType(contentType);
        response.setContentLengthLong(Files.size(filePath));
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setHeader("Cache-Control", "public, max-age=86400");

        try (InputStream in = Files.newInputStream(filePath);
             OutputStream out = response.getOutputStream()) {

            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }

            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            if (!response.isCommitted()) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi đọc ảnh");
            }
        }
    }
}