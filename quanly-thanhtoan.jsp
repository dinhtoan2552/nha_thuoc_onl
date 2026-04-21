<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.CauHinhThanhToan" %>

<%
    CauHinhThanhToan cauHinh = (CauHinhThanhToan) request.getAttribute("cauHinh");
    String adminMessage = (String) session.getAttribute("adminMessage");
    if (adminMessage != null) session.removeAttribute("adminMessage");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản lý thanh toán</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/dashboard.css?v=2">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/quanly-thanhtoan.css?v=2">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
</head>
<body>
<div class="admin-layout">
    <jsp:include page="/jsp/admin/admin_sidebar.jsp" />

    <div class="admin-main">
        <div class="admin-topbar">
            <div>
                <h1>Quản lý thanh toán</h1>
                <p>Cập nhật riêng QR và thanh toán thẻ</p>
            </div>
        </div>

        <% if (adminMessage != null) { %>
            <div class="payment-admin-message"><%= adminMessage %></div>
        <% } %>

        <div class="content-card">
            <form action="${pageContext.request.contextPath}/admin/thanh-toan"
                  method="post"
                  enctype="multipart/form-data"
                  class="payment-admin-form">

                <!-- ===== TOGGLE ===== -->
                <div class="toggle-group">
                    <label><input type="checkbox" name="batCOD" <%= cauHinh != null && cauHinh.isBatCOD() ? "checked" : "" %>> Bật COD</label>
                    <label><input type="checkbox" name="batChuyenKhoan" <%= cauHinh != null && cauHinh.isBatChuyenKhoan() ? "checked" : "" %>> Bật chuyển khoản</label>
                    <label><input type="checkbox" name="batThe" <%= cauHinh != null && cauHinh.isBatThe() ? "checked" : "" %>> Bật thanh toán thẻ</label>
                </div>

                <!-- ================= QR ================= -->
                <h2>Thông tin chuyển khoản QR</h2>

                <div class="payment-admin-grid">
                    <div class="payment-admin-field">
                        <label>Tên ngân hàng</label>
                        <input type="text" name="tenNganHangQr"
                               value="<%= cauHinh != null ? cauHinh.getTenNganHangQr() : "" %>">
                    </div>

                    <div class="payment-admin-field">
                        <label>Số tài khoản</label>
                        <input type="text" name="soTaiKhoanQr"
                               value="<%= cauHinh != null ? cauHinh.getSoTaiKhoanQr() : "" %>">
                    </div>

                    <div class="payment-admin-field full">
                        <label>Chủ tài khoản</label>
                        <input type="text" name="chuTaiKhoanQr"
                               value="<%= cauHinh != null ? cauHinh.getChuTaiKhoanQr() : "" %>">
                    </div>
                </div>

                <div class="upload-card">
                    <h3>Ảnh QR</h3>
                    <% if (cauHinh != null && cauHinh.getAnhQR() != null && !cauHinh.getAnhQR().isEmpty()) { %>
                        <img class="preview-img"
                             src="${pageContext.request.contextPath}/load-image?name=<%= cauHinh.getAnhQR() %>">
                    <% } else { %>
                        <div class="empty-preview">Chưa có QR</div>
                    <% } %>
                    <input type="file" name="anhQR">
                </div>

                <!-- ================= THẺ ================= -->
                <h2>Thông tin thanh toán thẻ</h2>

                <div class="payment-admin-grid">
                    <div class="payment-admin-field">
                        <label>Tên ngân hàng</label>
                        <input type="text" name="tenNganHangThe"
                               value="<%= cauHinh != null ? cauHinh.getTenNganHangThe() : "" %>">
                    </div>

                    <div class="payment-admin-field">
                        <label>Số tài khoản</label>
                        <input type="text" name="soTaiKhoanThe"
                               value="<%= cauHinh != null ? cauHinh.getSoTaiKhoanThe() : "" %>">
                    </div>

                    <div class="payment-admin-field">
                        <label>Chi nhánh</label>
                        <input type="text" name="chiNhanhThe"
                               value="<%= cauHinh != null ? cauHinh.getChiNhanhThe() : "" %>">
                    </div>

                    <div class="payment-admin-field">
                        <label>Chủ thẻ</label>
                        <input type="text" name="chuThe"
                               value="<%= cauHinh != null ? cauHinh.getChuThe() : "" %>">
                    </div>
                </div>

                <div class="upload-card">
                    <h3>Ảnh thẻ</h3>
                    <% if (cauHinh != null && cauHinh.getAnhThe() != null && !cauHinh.getAnhThe().isEmpty()) { %>
                        <img class="preview-img"
                             src="${pageContext.request.contextPath}/load-image?name=<%= cauHinh.getAnhThe() %>">
                    <% } else { %>
                        <div class="empty-preview">Chưa có ảnh thẻ</div>
                    <% } %>
                    <input type="file" name="anhThe">
                </div>

                <!-- SAVE -->
                <div class="payment-admin-actions">
                    <button type="submit" class="save-btn">
                        <i class="fa-solid fa-floppy-disk"></i>
                        Lưu cấu hình thanh toán
                    </button>
                </div>

            </form>
        </div>
    </div>
</div>
</body>
</html>