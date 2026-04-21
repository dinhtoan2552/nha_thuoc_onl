    <%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ page import="utils.InputSanitizer" %>
    <%
        String error = (String) request.getAttribute("error");
    %>
    <!DOCTYPE html>
    <html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Đăng ký tài khoản</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/auth.css?v=1">
    </head>
    <body>
        <div class="auth-page">
            <div class="auth-card auth-card--register">
                <h1 class="auth-title">Tạo tài khoản khách hàng</h1>
                <p class="auth-subtitle">Nhập thông tin và xác minh email bằng mã OTP</p>

                <% if (error != null) { %>
                    <div class="auth-alert error"><%= InputSanitizer.escapeHtml(error) %></div>
                <% } %>

                <form action="${pageContext.request.contextPath}/register" method="post" class="auth-form">
                    <input type="hidden" name="csrfToken" value="${sessionScope.csrf_token}">

                    <div class="form-group">
                        <label for="hoTen">Họ tên</label>
                        <input id="hoTen" type="text" name="hoTen" placeholder="Nhập họ tên" required>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="email">Email</label>
                            <input id="email" type="email" name="email" placeholder="Nhập email thật" required>
                        </div>

                        <div class="form-group">
                            <label for="soDienThoai">Số điện thoại</label>
                            <input id="soDienThoai" type="text" name="soDienThoai" placeholder="Nhập số điện thoại">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="diaChi">Địa chỉ</label>
                        <input id="diaChi" type="text" name="diaChi" placeholder="Nhập địa chỉ">
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="matKhau">Mật khẩu</label>
                            <input id="matKhau" type="password" name="matKhau" placeholder="Nhập mật khẩu" required>
                        </div>

                        <div class="form-group">
                            <label for="xacNhanMatKhau">Xác nhận mật khẩu</label>
                            <input id="xacNhanMatKhau" type="password" name="xacNhanMatKhau" placeholder="Nhập lại mật khẩu" required>
                        </div>
                    </div>

                    <button type="submit" class="auth-btn">Gửi mã OTP</button>
                </form>

                <div class="auth-footer">
                    Đã có tài khoản?
                    <a href="${pageContext.request.contextPath}/login">Đăng nhập</a>
                </div>
            </div>
        </div>
    </body>
    </html>