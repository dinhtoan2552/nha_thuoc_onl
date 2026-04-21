<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quên mật khẩu</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
        body{font-family:Arial,sans-serif;background:#f4f7fb;margin:0;padding:0}
        .box{max-width:460px;margin:70px auto;background:#fff;padding:28px;border-radius:18px;box-shadow:0 10px 30px rgba(0,0,0,.08)}
        h1{margin:0 0 10px;font-size:32px;color:#163b97}
        p{color:#667799;margin-bottom:18px}
        label{display:block;margin-bottom:8px;font-weight:700;color:#223762}
        input{width:100%;height:48px;border:1px solid #dbe4f0;border-radius:12px;padding:0 14px;font-size:15px;box-sizing:border-box}
        button{width:100%;height:48px;border:none;border-radius:12px;background:#2563eb;color:#fff;font-size:17px;font-weight:700;cursor:pointer;margin-top:16px}
        .error{background:#fff1f2;color:#dc2626;border:1px solid #fecdd3;padding:12px 14px;border-radius:12px;margin-bottom:14px}
        .back{display:inline-block;margin-top:14px;color:#2563eb;text-decoration:none}
    </style>
</head>
<body>
    <div class="box">
        <h1>Quên mật khẩu</h1>
        <p>Nhập email để nhận mã xác nhận đặt lại mật khẩu.</p>

        <% if (request.getAttribute("error") != null) { %>
            <div class="error"><%= request.getAttribute("error") %></div>
        <% } %>

        <form action="${pageContext.request.contextPath}/quen-mat-khau" method="post">
            <label for="email">Email</label>
            <input type="email" id="email" name="email" required>
            <button type="submit">Gửi mã OTP</button>
        </form>

        <a class="back" href="${pageContext.request.contextPath}/login">← Quay lại đăng nhập</a>
    </div>
</body>
</html>