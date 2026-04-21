<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String tenTrang = (String) request.getAttribute("tenTrang");
    if (tenTrang == null) tenTrang = "Chức năng đang cập nhật";
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><%= tenTrang %></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/dashboard.css">
</head>
<body>
    <div class="admin-layout">

        <jsp:include page="/jsp/admin/admin_sidebar.jsp" />

        <div class="admin-main">
            <div class="admin-topbar">
                <div>
                    <h1><%= tenTrang %></h1>
                    <p>Trang này đã có route, sẽ làm chức năng chi tiết sau</p>
                </div>

                <a class="logout-btn" href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
            </div>

            <div class="content-card">
                <h2><%= tenTrang %></h2>
                <p>Chức năng này đã bấm vào được. Bước sau ta sẽ làm chi tiết CRUD riêng cho trang này.</p>
            </div>
        </div>

    </div>
</body>
</html>