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
    String successMessage = (String) session.getAttribute("successMessage");
    if (successMessage != null) {
        session.removeAttribute("successMessage");
    }

    String actionType = (String) session.getAttribute("account_action_type");
    String title = "Xác nhận OTP";
    String note = "Nhập mã OTP 6 số vừa được gửi tới email của bạn.";

    if ("CHANGE_PASSWORD".equals(actionType)) {
        title = "Xác nhận đổi mật khẩu";
        note = "Mã OTP đã được gửi về email tài khoản để xác nhận đổi mật khẩu.";
    } else if ("CHANGE_EMAIL".equals(actionType)) {
        title = "Xác nhận đổi gmail";
        note = "Mã OTP đã được gửi về gmail hiện tại để xác nhận đổi gmail.";
    }
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title><%= InputSanitizer.escapeHtml(title) %></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/doi-mat-khau.css">
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
                        <h1><%= InputSanitizer.escapeHtml(title) %></h1>
                        <p><%= InputSanitizer.escapeHtml(note) %></p>
                    </div>
                </div>

                <% if (error != null) { %>
                    <div class="alert-message alert-error">
                        <i class="fa-solid fa-circle-exclamation"></i>
                        <span><%= InputSanitizer.escapeHtml(error) %></span>
                    </div>
                <% } %>

                <% if (successMessage != null) { %>
                    <div class="alert-message alert-success">
                        <i class="fa-solid fa-circle-check"></i>
                        <span><%= InputSanitizer.escapeHtml(successMessage) %></span>
                    </div>
                <% } %>

                <form action="${pageContext.request.contextPath}/xac-nhan-otp-tai-khoan" method="post" class="change-password-form">
                    <input type="hidden" name="csrfToken" value="${sessionScope.csrf_token}">

                    <div class="form-group">
                        <label for="otp">Mã OTP</label>
                        <div class="input-wrapper">
                            <i class="fa-solid fa-shield-halved input-icon"></i>
                            <input type="text" id="otp" name="otp" placeholder="Nhập mã OTP 6 số" maxlength="6" required>
                        </div>
                    </div>

                    <div class="form-note">
                        Mã OTP có hiệu lực trong 5 phút.
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn-submit">
                            <i class="fa-solid fa-check"></i>
                            <span>Xác nhận</span>
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