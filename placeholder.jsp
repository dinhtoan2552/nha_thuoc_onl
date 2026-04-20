<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String tenTrang = (String) request.getAttribute("tenTrang");
    if (tenTrang == null) tenTrang = "Chức năng nhân viên";
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><%= tenTrang %></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/staff/dashboard.css">
</head>
<body>
    <div class="staff-layout">

        <jsp:include page="/jsp/staff/staff_sidebar.jsp" />

        <div class="staff-main">
            <div class="staff-topbar">
                <div>
                    <h1><%= tenTrang %></h1>
                    <p>Trang này đã có route, sẽ làm chức năng chi tiết sau</p>
                </div>

                <a class="logout-btn" href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
            </div>

            <div class="panel">
                <div class="panel-header">
                    <h3><%= tenTrang %></h3>
                </div>
                <p class="note-text">
                    Chức năng này đã bấm vào được. Bước sau ta sẽ làm chi tiết từng phần cho nhân viên.
                </p>
            </div>
        </div>
    </div>
</body>
</html>