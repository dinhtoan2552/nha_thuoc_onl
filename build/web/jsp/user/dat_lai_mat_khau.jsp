<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đặt lại mật khẩu</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
        body{font-family:Arial,sans-serif;background:#f4f7fb;margin:0;padding:0}
        .box{max-width:460px;margin:70px auto;background:#fff;padding:28px;border-radius:18px;box-shadow:0 10px 30px rgba(0,0,0,.08)}
        h1{margin:0 0 10px;font-size:32px;color:#163b97}
        p{color:#667799;margin-bottom:18px}
        label{display:block;margin-bottom:8px;font-weight:700;color:#223762}
        input{width:100%;height:48px;border:1px solid #dbe4f0;border-radius:12px;padding:0 14px;font-size:15px;box-sizing:border-box;margin-bottom:14px}
        button{width:100%;height:48px;border:none;border-radius:12px;background:#2563eb;color:#fff;font-size:17px;font-weight:700;cursor:pointer;margin-top:8px}
        .error{background:#fff1f2;color:#dc2626;border:1px solid #fecdd3;padding:12px 14px;border-radius:12px;margin-bottom:14px}
    </style>
</head>
<body>
    <div class="box">
        <h1>Đặt lại mật khẩu</h1>
        <p>Nhập mật khẩu mới cho tài khoản của bạn.</p>

        <% if (request.getAttribute("error") != null) { %>
            <div class="error"><%= request.getAttribute("error") %></div>
        <% } %>

        <form action="${pageContext.request.contextPath}/dat-lai-mat-khau" method="post">
            <label for="matKhauMoi">Mật khẩu mới</label>
            <input type="password" id="matKhauMoi" name="matKhauMoi" required>

            <label for="nhapLaiMatKhau">Nhập lại mật khẩu mới</label>
            <input type="password" id="nhapLaiMatKhau" name="nhapLaiMatKhau" required>

            <button type="submit">Cập nhật mật khẩu</button>
        </form>
    </div>
</body>
</html>