package controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import utils.FileUploadUtil;

@WebServlet(name = "AdminLoadImageServlet", urlPatterns = {"/image"})
public class LoadImageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String fileName = request.getParameter("name");

        if (fileName == null || fileName.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        fileName = fileName.trim();

        if (fileName.contains("..") || fileName.contains("/") || fileName.contains("\\")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Tên ảnh không hợp lệ");
            return;
        }

        File file = new File(FileUploadUtil.getUploadDir(), fileName);

        if (!file.exists() || !file.isFile()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String lowerName = fileName.toLowerCase();
        if (lowerName.endsWith(".png")) {
            response.setContentType("image/png");
        } else if (lowerName.endsWith(".jpg") || lowerName.endsWith(".jpeg") || lowerName.endsWith(".jfif")) {
            response.setContentType("image/jpeg");
        } else if (lowerName.endsWith(".gif")) {
            response.setContentType("image/gif");
        } else if (lowerName.endsWith(".webp")) {
            response.setContentType("image/webp");
        } else if (lowerName.endsWith(".bmp")) {
            response.setContentType("image/bmp");
        } else if (lowerName.endsWith(".heic")) {
            response.setContentType("image/heic");
        } else {
            response.setContentType("application/octet-stream");
        }

        response.setContentLengthLong(file.length());

        try (FileInputStream in = new FileInputStream(file);
             OutputStream out = response.getOutputStream()) {

            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }

            out.flush();
        }
    }
}