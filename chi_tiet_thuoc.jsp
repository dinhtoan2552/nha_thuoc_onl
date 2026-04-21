<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="model.Thuoc" %>
<%@ page import="utils.CurrencyUtil" %>
<%@ page import="utils.InputSanitizer" %>

<%
    Thuoc thuoc = (Thuoc) request.getAttribute("thuoc");
    boolean hetHang = (thuoc == null) || thuoc.getSoLuong() <= 0 || "HET_HANG".equals(thuoc.getTrangThai());

    String imageUrl = "";
    if (thuoc != null && thuoc.getHinhAnh() != null) {
        imageUrl = request.getContextPath() + "/load-image?name=" + URLEncoder.encode(thuoc.getHinhAnh(), "UTF-8");
    }
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chi tiết thuốc</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/chi-tiet-thuoc.css?v=3">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
</head>
<body>
    <div class="detail-page">
        <div class="detail-container">

            <div class="detail-header">
                <h1>Chi tiết sản phẩm</h1>
                <a href="${pageContext.request.contextPath}/user/home" class="back-btn">
                    <i class="fa-solid fa-arrow-left"></i>
                    <span>Quay lại</span>
                </a>
            </div>

            <% if (thuoc == null) { %>
                <div class="detail-box">
                    <p>Không tìm thấy sản phẩm.</p>
                </div>
            <% } else { %>
                <%
                    String tenThuoc = thuoc.getTenThuoc() == null ? "Sản phẩm" : thuoc.getTenThuoc();
                    String moTa = (thuoc.getMoTa() == null || thuoc.getMoTa().trim().isEmpty()) ? "Chưa có mô tả." : thuoc.getMoTa();
                    String congDung = (thuoc.getCongDung() == null || thuoc.getCongDung().trim().isEmpty()) ? "Chưa cập nhật" : thuoc.getCongDung();
                    String cachDung = (thuoc.getCachDung() == null || thuoc.getCachDung().trim().isEmpty()) ? "Chưa cập nhật" : thuoc.getCachDung();
                    String thanhPhan = (thuoc.getThanhPhan() == null || thuoc.getThanhPhan().trim().isEmpty()) ? "Chưa cập nhật" : thuoc.getThanhPhan();
                    String nhaSanXuat = (thuoc.getNhaSanXuat() == null || thuoc.getNhaSanXuat().trim().isEmpty()) ? "Chưa cập nhật" : thuoc.getNhaSanXuat();
                    String trangThai = (thuoc.getTrangThai() == null || thuoc.getTrangThai().trim().isEmpty()) ? "Chưa cập nhật" : thuoc.getTrangThai();
                %>

                <div class="detail-box detail-main">
                    <div class="detail-image">
                        <img src="<%= imageUrl %>"
                             alt="<%= InputSanitizer.escapeHtml(tenThuoc) %>">
                    </div>

                    <div class="detail-info">
                        <h2><%= InputSanitizer.escapeHtml(tenThuoc) %></h2>

                        <div class="price">
                            <%= CurrencyUtil.yen(thuoc.getDonGia()) %>
                        </div>

                        <div class="stock">
                            Tồn kho:
                            <strong><%= thuoc.getSoLuong() %></strong>
                        </div>

                        <div class="desc">
                            <h3>Mô tả</h3>
                            <p><%= InputSanitizer.escapeHtml(moTa) %></p>
                        </div>

                        <div class="extra-grid">
                            <div class="extra-item">
                                <span class="label">Công dụng</span>
                                <span class="value"><%= InputSanitizer.escapeHtml(congDung) %></span>
                            </div>

                            <div class="extra-item">
                                <span class="label">Cách dùng</span>
                                <span class="value"><%= InputSanitizer.escapeHtml(cachDung) %></span>
                            </div>

                            <div class="extra-item">
                                <span class="label">Thành phần</span>
                                <span class="value"><%= InputSanitizer.escapeHtml(thanhPhan) %></span>
                            </div>

                            <div class="extra-item">
                                <span class="label">Hạn sử dụng</span>
                                <span class="value"><%= (thuoc.getHanSuDung() == null) ? "Chưa cập nhật" : thuoc.getHanSuDung() %></span>
                            </div>

                            <div class="extra-item">
                                <span class="label">Nhà sản xuất</span>
                                <span class="value"><%= InputSanitizer.escapeHtml(nhaSanXuat) %></span>
                            </div>

                            <div class="extra-item">
                                <span class="label">Trạng thái</span>
                                <span class="value"><%= InputSanitizer.escapeHtml(trangThai) %></span>
                            </div>
                        </div>

                        <div class="detail-actions">
                            <% if (hetHang) { %>
                                <div class="detail-out-stock-message">Sản phẩm hiện đang hết hàng</div>
                                <button type="button" class="add-cart-btn disabled-btn" disabled>Hết hàng</button>
                                <button type="button" class="buy-now-btn disabled-btn" disabled>Không thể mua</button>
                            <% } else { %>
                                <form action="${pageContext.request.contextPath}/them-vao-gio" method="post">
                                    <input type="hidden" name="csrfToken" value="${sessionScope.csrf_token}">
                                    <input type="hidden" name="thuocId" value="<%= thuoc.getIdThuoc() %>">
                                    <input type="hidden" name="soLuong" value="1">
                                    <button type="submit" class="add-cart-btn">Thêm vào giỏ</button>
                                </form>

                                <form action="${pageContext.request.contextPath}/dat-hang-ngay" method="post">
                                    <input type="hidden" name="csrfToken" value="${sessionScope.csrf_token}">
                                    <input type="hidden" name="idThuoc" value="<%= thuoc.getIdThuoc() %>">
                                    <input type="hidden" name="soLuong" value="1">
                                    <input type="hidden" name="nguon" value="detail">
                                    <button type="submit" class="buy-now-btn">Mua ngay</button>
                                </form>
                            <% } %>
                        </div>
                    </div>
                </div>

                <section class="related-section">
                    <div class="related-header">
                        <h2>Sản phẩm tương tự</h2>
                        <p>Một số sản phẩm có công dụng gần giống để bạn tham khảo thêm.</p>
                    </div>

                    <div id="relatedProductList"
                         class="related-grid"
                         data-id="<%= thuoc.getIdThuoc() %>">
                        <div class="related-loading">Đang tải sản phẩm tương tự...</div>
                    </div>
                </section>
            <% } %>

        </div>
    </div>

    <script>
    document.addEventListener("DOMContentLoaded", function () {
        const relatedBox = document.getElementById("relatedProductList");
        if (!relatedBox) return;

        const idThuoc = relatedBox.dataset.id;

        function escapeHtml(text) {
            if (!text) return "";
            return text
                .replace(/&/g, "&amp;")
                .replace(/</g, "&lt;")
                .replace(/>/g, "&gt;")
                .replace(/"/g, "&quot;")
                .replace(/'/g, "&#039;");
        }

        function formatPrice(value) {
            const num = Number(value || 0);
            return "¥" + num.toLocaleString("en-US");
        }

        if (!idThuoc || idThuoc === "0") {
            relatedBox.innerHTML = '<div class="related-empty">Không tìm thấy ID sản phẩm hiện tại.</div>';
            console.error("idThuoc không hợp lệ:", idThuoc);
            return;
        }

        const apiUrl = "${pageContext.request.contextPath}/api/user/san-pham-tuong-tu?id=" + encodeURIComponent(idThuoc);
        console.log("API sản phẩm tương tự:", apiUrl);

        fetch(apiUrl, {
            method: "GET",
            headers: {
                "Accept": "application/json"
            }
        })
        .then(function (response) {
            if (!response.ok) {
                throw new Error("HTTP " + response.status);
            }
            return response.json();
        })
        .then(function (data) {
            console.log("Dữ liệu sản phẩm tương tự:", data);

            if (!data || data.length === 0) {
                relatedBox.innerHTML = '<div class="related-empty">Hiện chưa có sản phẩm tương tự.</div>';
                return;
            }

            let html = "";

            data.forEach(function (item) {
                const moTa = item.moTa && item.moTa.trim() !== ""
                    ? item.moTa
                    : "Sản phẩm chăm sóc sức khỏe";

                const imgName = item.hinhAnh ? encodeURIComponent(item.hinhAnh) : "";

                html += ''
                    + '<a class="related-card" href="${pageContext.request.contextPath}/chi-tiet-thuoc?id=' + item.idThuoc + '">'
                    + '    <div class="related-image">'
                    + '        <img src="${pageContext.request.contextPath}/load-image?name=' + imgName + '" alt="' + escapeHtml(item.tenThuoc) + '">'
                    + '    </div>'
                    + '    <div class="related-body">'
                    + '        <h3>' + escapeHtml(item.tenThuoc) + '</h3>'
                    + '        <p>' + escapeHtml(moTa) + '</p>'
                    + '        <div class="related-price">' + formatPrice(item.donGia) + '</div>'
                    + '    </div>'
                    + '</a>';
            });

            relatedBox.innerHTML = html;
        })
        .catch(function (error) {
            console.error("Lỗi tải sản phẩm tương tự:", error);
            relatedBox.innerHTML = '<div class="related-empty">Không tải được sản phẩm tương tự.</div>';
        });
    });
    </script>
</body>
</html>