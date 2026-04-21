<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="utils.InputSanitizer" %>
<%
    String error = (String) request.getAttribute("error");
    String successMessage = (String) session.getAttribute("successMessage");
    if (successMessage != null) {
        session.removeAttribute("successMessage");
    }
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập hệ thống</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/auth.css?v=1">
</head>
<body>
    <div class="auth-page">
        <div class="auth-card auth-card--login">
            <h1 class="auth-title">Đăng nhập hệ thống</h1>
            <p class="auth-subtitle">Vui lòng đăng nhập để tiếp tục</p>

            <% if (error != null) { %>
                <div class="auth-alert error"><%= InputSanitizer.escapeHtml(error) %></div>
            <% } %>

            <% if (successMessage != null) { %>
                <div class="auth-alert success"><%= InputSanitizer.escapeHtml(successMessage) %></div>
            <% } %>

            <form action="${pageContext.request.contextPath}/login" method="post" class="auth-form">
                <input type="hidden" name="csrfToken" value="${sessionScope.csrf_token}">

                <div class="form-group">
                    <label for="email">Email</label>
                    <input id="email" type="email" name="email" placeholder="Nhập email" required>
                </div>

                <div class="form-group">
                    <label for="matKhau">Mật khẩu</label>
                    <input id="matKhau" type="password" name="matKhau" placeholder="Nhập mật khẩu" required>
                </div>

                <div style="text-align: right; margin: -4px 0 14px;">
                    <a href="${pageContext.request.contextPath}/quen-mat-khau"
                       style="color: #2563eb; text-decoration: none; font-weight: 600; font-size: 14px;">
                        Quên mật khẩu?
                    </a>
                </div>

                <button type="submit" class="auth-btn">Đăng nhập</button>
            </form>

            <div class="auth-footer">
                Chưa có tài khoản?
                <a href="${pageContext.request.contextPath}/register">Đăng ký ngay</a>
            </div>
        </div>
    </div>
</body>
</html>