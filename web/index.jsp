<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đang chuyển hướng...</title>
</head>
<body>

<%
    // Chuyển hướng về trang home của user
    response.sendRedirect(request.getContextPath() + "/user/home");
%>

</body>
</html>