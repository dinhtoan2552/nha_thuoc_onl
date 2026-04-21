<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="utils.InputSanitizer" %>
<%@ page import="model.NguoiDung" %>
<%
    NguoiDung user = (NguoiDung) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }

    String error = (String) request.getAttribute("error");
    String success = (String) request.getAttribute("success");

    String successMessage = (String) session.getAttribute("successMessage");
    if (successMessage != null) {
        session.removeAttribute("successMessage");
        success = successMessage;
    }
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đổi mật khẩu</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/doi-mat-khau.css?v=3002">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
</head>
<body>
    <div class="change-password-page">
        <div class="change-password-container">
            <div class="change-password-card">
                <div class="card-header">
                    <a href="${pageContext.request.contextPath}/user/home" class="back-btn">
                        <i class="fa-solid fa-arrow-left"></i>
                    </a>

                    <div class="header-text">
                        <h1>Đổi mật khẩu</h1>
                        <p>Cập nhật mật khẩu tài khoản của bạn</p>
                    </div>
                </div>

                <% if (error != null) { %>
                    <div class="alert-message alert-error">
                        <i class="fa-solid fa-circle-exclamation"></i>
                        <span><%= InputSanitizer.escapeHtml(error) %></span>
                    </div>
                <% } %>

                <% if (success != null) { %>
                    <div class="alert-message alert-success">
                        <i class="fa-solid fa-circle-check"></i>
                        <span><%= InputSanitizer.escapeHtml(success) %></span>
                    </div>
                <% } %>

                <form action="${pageContext.request.contextPath}/doi-mat-khau" method="post" class="change-password-form">
                    <input type="hidden" name="csrfToken" value="${sessionScope.csrf_token}">

                    <div class="form-group">
                        <label for="matKhauCu">Mật khẩu cũ</label>
                        <div class="input-wrapper">
                            <i class="fa-solid fa-lock input-icon"></i>
                            <input type="password" id="matKhauCu" name="matKhauCu" placeholder="Nhập mật khẩu cũ" required>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="matKhauMoi">Mật khẩu mới</label>
                        <div class="input-wrapper">
                            <i class="fa-solid fa-key input-icon"></i>
                            <input type="password" id="matKhauMoi" name="matKhauMoi" placeholder="Nhập mật khẩu mới" required>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="xacNhanMatKhau">Xác nhận mật khẩu mới</label>
                        <div class="input-wrapper">
                            <i class="fa-solid fa-shield-halved input-icon"></i>
                            <input type="password" id="xacNhanMatKhau" name="xacNhanMatKhau" placeholder="Nhập lại mật khẩu mới" required>
                        </div>
                    </div>

                    <div class="form-note">
                        Sau khi bấm cập nhật, hệ thống sẽ gửi mã OTP 6 số về email tài khoản để xác nhận.
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn-submit">
                            <i class="fa-solid fa-floppy-disk"></i>
                            <span>Cập nhật mật khẩu</span>
                        </button>

                        <a href="${pageContext.request.contextPath}/user/home" class="btn-back-home">
                            <i class="fa-solid fa-house"></i>
                            <span>Quay lại trang chủ</span>
                        </a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</body>
</html>