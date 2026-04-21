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

    String emailHienTai = user.getEmail() == null ? "" : user.getEmail();
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đổi gmail</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/doi-gmail.css?v=4001">
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
                        <h1>Đổi gmail</h1>
                        <p>Gmail hiện tại: <strong><%= InputSanitizer.escapeHtml(emailHienTai) %></strong></p>
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

                <form action="${pageContext.request.contextPath}/doi-gmail" method="post" class="change-password-form">
                    <input type="hidden" name="csrfToken" value="${sessionScope.csrf_token}">

                    <div class="form-group">
                        <label for="emailMoi">Gmail mới</label>
                        <div class="input-wrapper">
                            <i class="fa-solid fa-envelope input-icon"></i>
                            <input type="email" id="emailMoi" name="emailMoi" placeholder="Nhập gmail mới" required>
                        </div>
                    </div>

                    <div class="form-note">
                        Sau khi bấm cập nhật, hệ thống sẽ gửi mã OTP 6 số về gmail hiện tại để xác nhận.
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn-submit">
                            <i class="fa-solid fa-envelope-circle-check"></i>
                            <span>Cập nhật gmail</span>
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