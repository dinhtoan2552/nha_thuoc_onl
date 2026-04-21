package controller.common;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.io.PrintWriter;
import utils.FileUploadUtil;

@WebServlet("/test-upload")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 10 * 1024 * 1024,
        maxRequestSize = 20 * 1024 * 1024
)
public class TestUploadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html><head><meta charset='UTF-8'><title>Test Upload</title></head><body>");
        out.println("<h2>Test upload ảnh</h2>");
        out.println("<form method='post' enctype='multipart/form-data'>");
        out.println("<input type='file' name='anhDinhKem' accept='.jpg,.jpeg,.png,.webp,image/*'>");
        out.println("<button type='submit'>Upload</button>");
        out.println("</form>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain; charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        Part filePart = request.getPart("anhDinhKem");
        String tenAnh = FileUploadUtil.saveImage(filePart);

        response.getWriter().println("tenAnh = " + tenAnh);
    }
}