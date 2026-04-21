<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="utils.InputSanitizer" %>
<%
    String error = (String) request.getAttribute("error");
    String success = (String) request.getAttribute("success");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Xác minh OTP</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/login.css?v=20">
</head>
<body>
    <div class="login-page">
        <div class="login-left">
            <div class="brand-box">
                <div class="brand-badge">OTP</div>
                <h1>Xác minh email</h1>
                <p>Nhập mã OTP 6 số đã được gửi tới email bạn vừa đăng ký.</p>
            </div>
        </div>

        <div class="login-right">
            <div class="login-card">
                <div class="login-header">
                    <h2>Nhập mã OTP</h2>
                    <p>Mã OTP có hiệu lực trong 5 phút</p>
                </div>

                <% if (error != null) { %>
                    <div class="error-box"><%= InputSanitizer.escapeHtml(error) %></div>
                <% } %>

                <% if (success != null) { %>
                    <div class="error-box" style="background:#ecfdf3;border:1px solid #bbf7d0;color:#15803d;">
                        <%= InputSanitizer.escapeHtml(success) %>
                    </div>
                <% } %>

                <form action="${pageContext.request.contextPath}/verify-register-otp" method="post" class="login-form">
                    <input type="hidden" name="csrfToken" value="${csrfToken}">

                    <div class="form-group">
                        <label for="otp">Mã OTP</label>
                        <input type="text" id="otp" name="otp" maxlength="6" placeholder="Nhập 6 chữ số" required>
                    </div>

                    <button type="submit" class="btn-login">Xác nhận đăng ký</button>
                </form>

                <form action="${pageContext.request.contextPath}/resend-register-otp" method="post" style="margin-top:12px;">
                    <input type="hidden" name="csrfToken" value="${csrfToken}">
                    <button type="submit" class="btn-login" style="background:#edf4ff;color:#2157d9;border:1px solid #cfe0ff;">
                        Gửi lại mã OTP
                    </button>
                </form>

                <p style="margin-top: 14px; text-align:center;">
                    <a href="${pageContext.request.contextPath}/register">Quay lại đăng ký</a>
                </p>
            </div>
        </div>
    </div>
</body>
</html>