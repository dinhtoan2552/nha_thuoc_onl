<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.ThongTinLienHe" %>

<%
    ThongTinLienHe thongTinLienHe = (ThongTinLienHe) request.getAttribute("thongTinLienHe");
    if (thongTinLienHe == null) {
        thongTinLienHe = new ThongTinLienHe();
    }

    String adminMessage = (String) session.getAttribute("adminMessage");
    if (adminMessage != null) {
        session.removeAttribute("adminMessage");
    }
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cài đặt liên hệ</title>

    <!-- CSS admin -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/dashboard.css">

    <!-- CSS riêng -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/lien-he.css">

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
</head>
<body>

<div class="admin-layout">
    <jsp:include page="/jsp/admin/admin_sidebar.jsp" />

    <div class="admin-main">
        <div class="admin-topbar">
            <div>
                <h1>Cài đặt liên hệ</h1>
                <p>Quản lý nút liên hệ nổi bên user và banner thông tin cuối trang</p>
            </div>
        </div>

        <% if (adminMessage != null) { %>
            <div class="admin-message"><%= adminMessage %></div>
        <% } %>

        <div class="contact-setting-card">
            <form action="${pageContext.request.contextPath}/admin/lien-he" method="post" class="contact-setting-form">

                <div class="form-group">
                    <label>Số Zalo</label>
                    <input type="text" name="soZalo"
                           value="<%= thongTinLienHe.getSoZalo() != null ? thongTinLienHe.getSoZalo() : "" %>"
                           placeholder="Ví dụ: 84901234567">
                </div>

                <div class="form-group">
                    <label>Link Facebook Messenger</label>
                    <input type="text" name="linkMessenger"
                           value="<%= thongTinLienHe.getLinkMessenger() != null ? thongTinLienHe.getLinkMessenger() : "" %>"
                           placeholder="https://m.me/yourpage">
                </div>

                <div class="form-group">
                    <label>Số hotline</label>
                    <input type="text" name="soHotline"
                           value="<%= thongTinLienHe.getSoHotline() != null ? thongTinLienHe.getSoHotline() : "" %>"
                           placeholder="0901234567">
                </div>

                <div class="form-group">
                    <label>Địa chỉ nhà thuốc</label>
                    <input type="text" name="diaChiNhaThuoc"
                           value="<%= thongTinLienHe.getDiaChiNhaThuoc() != null ? thongTinLienHe.getDiaChiNhaThuoc() : "" %>"
                           placeholder="Ví dụ: Số nhà..., đường..., quận/huyện..., tỉnh/thành phố...">
                </div>

                <div class="form-group">
                    <label>Nội dung banner cuối trang</label>
                    <textarea name="noiDungBanner" rows="4"
                              placeholder="Ví dụ: Nhà thuốc hỗ trợ tư vấn trực tiếp trong giờ hành chính."><%= thongTinLienHe.getNoiDungBanner() != null ? thongTinLienHe.getNoiDungBanner() : "" %></textarea>
                </div>

                <button type="submit" class="save-contact-btn">
                    Lưu thông tin
                </button>

            </form>
        </div>
    </div>
</div>

</body>
</html>