<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="model.GioHangItem" %>
<%@ page import="utils.CurrencyUtil" %>
<%@ page import="utils.InputSanitizer" %>
<%
    List<GioHangItem> dsGioHang = (List<GioHangItem>) request.getAttribute("dsGioHang");
    Double tongTien = (Double) request.getAttribute("tongTien");

    if (dsGioHang == null) dsGioHang = new ArrayList<>();
    if (tongTien == null) tongTien = 0.0;

    String cartMessage = (String) session.getAttribute("cartMessage");
    if (cartMessage != null) {
        session.removeAttribute("cartMessage");
    }
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Giỏ hàng</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/cart.css?v=2">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
</head>
<body>
    <div class="cart-page">
        <div class="cart-container">

            <div class="cart-header">
                <h1>Giỏ hàng của bạn</h1>
                <a href="${pageContext.request.contextPath}/user/home" class="back-btn">
                    <i class="fa-solid fa-arrow-left"></i>
                    <span>Quay lại mua hàng</span>
                </a>
            </div>

            <% if (cartMessage != null) { %>
                <div class="cart-message">
                    <%= InputSanitizer.escapeHtml(cartMessage) %>
                </div>
            <% } %>

            <% if (dsGioHang.isEmpty()) { %>
                <div class="empty-cart">
                    <div class="empty-cart-icon">
                        <i class="fa-solid fa-cart-shopping"></i>
                    </div>
                    <h2>Giỏ hàng của bạn đang trống</h2>
                    <p>Hãy quay lại trang chủ để thêm sản phẩm vào giỏ hàng.</p>
                    <a href="${pageContext.request.contextPath}/user/home" class="empty-cart-btn">
                        Tiếp tục mua sắm
                    </a>
                </div>
            <% } else { %>

                <div class="cart-list">
                    <% for (GioHangItem item : dsGioHang) { %>
                        <%
                            String tenThuoc = item.getTenThuoc() == null ? "Sản phẩm" : item.getTenThuoc();
                            String hinhAnh = item.getHinhAnh();
                            String imageUrl = request.getContextPath() + "/load-image?name=" +
                                    URLEncoder.encode(hinhAnh == null ? "" : hinhAnh, "UTF-8");
                        %>
                        <div class="cart-item">
                            <div class="cart-item-image">
                                <img src="<%= imageUrl %>"
                                     alt="<%= InputSanitizer.escapeHtml(tenThuoc) %>">
                            </div>

                            <div class="cart-item-info">
                                <h3><%= InputSanitizer.escapeHtml(tenThuoc) %></h3>

                                <p class="cart-item-price">
                                    Đơn giá:
                                    <strong><%= CurrencyUtil.yen(item.getDonGia()) %></strong>
                                </p>

                                <form action="${pageContext.request.contextPath}/cap-nhat-gio-hang"
                                      method="post"
                                      class="cart-qty-form">

                                    <input type="hidden" name="csrfToken" value="${sessionScope.csrf_token}">
                                    <input type="hidden" name="idThuoc" value="<%= item.getIdThuoc() %>">

                                    <div class="qty-control">
                                        <button type="button"
                                                class="qty-btn"
                                                onclick="changeQty('qty_<%= item.getIdThuoc() %>', -1)">
                                            −
                                        </button>

                                        <input type="number"
                                               id="qty_<%= item.getIdThuoc() %>"
                                               name="soLuong"
                                               value="<%= item.getSoLuong() %>"
                                               min="1"
                                               class="qty-input">

                                        <button type="button"
                                                class="qty-btn"
                                                onclick="changeQty('qty_<%= item.getIdThuoc() %>', 1)">
                                            +
                                        </button>
                                    </div>

                                    <div class="cart-item-actions">
                                        <button type="submit" class="update-btn">
                                            <i class="fa-solid fa-rotate"></i>
                                            <span>Cập nhật</span>
                                        </button>

                                        <button type="submit"
                                                class="order-btn"
                                                formaction="${pageContext.request.contextPath}/dat-hang-ngay?nguon=cart"
                                                formmethod="post"
                                                onclick="return confirm('Xác nhận đặt hàng sản phẩm này?');">
                                            <i class="fa-solid fa-bag-shopping"></i>
                                            <span>Đặt hàng</span>
                                        </button>

                                        <a href="${pageContext.request.contextPath}/xoa-khoi-gio?idThuoc=<%= item.getIdThuoc() %>"
                                           class="remove-btn"
                                           onclick="return confirm('Bạn có chắc muốn xóa sản phẩm này khỏi giỏ hàng?');">
                                            <i class="fa-solid fa-trash"></i>
                                            <span>Xóa</span>
                                        </a>
                                    </div>
                                </form>
                            </div>

                            <div class="cart-item-total">
                                <span>Thành tiền</span>
                                <strong><%= CurrencyUtil.yen(item.getThanhTien()) %></strong>
                            </div>
                        </div>
                    <% } %>
                </div>

                <div class="cart-summary">
    <h2>
        Tổng tiền:
        <span><%= CurrencyUtil.yen(tongTien) %></span>
    </h2>

    <form action="${pageContext.request.contextPath}/thanh-toan-gio-hang" method="post">
        <input type="hidden" name="csrfToken" value="${sessionScope.csrf_token}">
        <button type="submit" class="checkout-all-btn">
            <i class="fa-solid fa-credit-card"></i>
            <span>Thanh toán tất cả</span>
        </button>
    </form>
</div>
            <% } %>

        </div>
    </div>

    <script>
        function changeQty(inputId, delta) {
            const input = document.getElementById(inputId);
            if (!input) return;

            let value = parseInt(input.value) || 1;
            value += delta;

            if (value < 1) value = 1;

            input.value = value;
        }
    </script>
</body>
</html>