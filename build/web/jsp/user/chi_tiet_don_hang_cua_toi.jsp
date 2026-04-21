<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.DonHang" %>
<%@ page import="model.ChiTietDonHang" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="utils.CurrencyUtil" %>

<%
    DonHang donHang = (DonHang) request.getAttribute("donHang");
    List<ChiTietDonHang> dsChiTiet = (donHang != null && donHang.getDanhSachChiTiet() != null)
            ? donHang.getDanhSachChiTiet()
            : new ArrayList<>();
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết đơn hàng</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/don-hang-cua-toi.css?v=3">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/chi-tiet-don-hang.css?v=1">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
</head>
<body>
    <div class="order-page detail-order-page">
        <div class="order-container detail-order-container">

            <div class="order-header detail-order-header">
                <h1>Chi tiết đơn hàng</h1>
                <a href="${pageContext.request.contextPath}/don-hang-cua-toi" class="back-btn">
                    <i class="fa-solid fa-arrow-left"></i>
                    <span>Quay lại danh sách đơn</span>
                </a>
            </div>

            <% if (donHang == null) { %>
                <div class="empty-order">
                    <h2>Không tìm thấy đơn hàng</h2>
                </div>
            <% } else { %>
                <div class="order-card detail-order-card">
                    <div class="order-top detail-order-top">
                        <div>
                            <h3>Đơn hàng #<%= donHang.getIdDonHang() %></h3>
                            <p>Ngày đặt: <%= donHang.getNgayDat() %></p>
                        </div>

                        <div class="order-status-wrap">
                            <span class="status order-status"><%= donHang.getTrangThai() %></span>
                            <span class="status payment-status">
                                <%= donHang.getTrangThaiThanhToan() == null ? "CHƯA CẬP NHẬT" : donHang.getTrangThaiThanhToan() %>
                            </span>
                        </div>
                    </div>

                    <div class="order-info-grid detail-info-grid">
                        <div class="info-item">
                            <span class="label">Người nhận</span>
                            <span class="value"><%= donHang.getTenNguoiNhan() == null ? "" : donHang.getTenNguoiNhan() %></span>
                        </div>

                        <div class="info-item">
                            <span class="label">Số điện thoại</span>
                            <span class="value"><%= donHang.getSdtNhan() == null ? "" : donHang.getSdtNhan() %></span>
                        </div>

                        <div class="info-item full">
                            <span class="label">Địa chỉ nhận</span>
                            <span class="value"><%= donHang.getDiaChiNhan() == null ? "" : donHang.getDiaChiNhan() %></span>
                        </div>

                        <div class="info-item">
                            <span class="label">Phương thức thanh toán</span>
                            <span class="value"><%= donHang.getPhuongThucThanhToan() == null ? "COD" : donHang.getPhuongThucThanhToan() %></span>
                        </div>

                        <div class="info-item">
                            <span class="label">Tổng tiền</span>
                            <span class="value total-price"><%= CurrencyUtil.yen(donHang.getTongTien()) %></span>
                        </div>

                        <div class="info-item full">
                            <span class="label">Ghi chú</span>
                            <span class="value">
                                <%= (donHang.getGhiChu() == null || donHang.getGhiChu().trim().isEmpty())
                                        ? "Không có"
                                        : donHang.getGhiChu() %>
                            </span>
                        </div>
                    </div>
                </div>

                <div class="detail-list detail-order-list">
                    <% for (ChiTietDonHang ct : dsChiTiet) { %>
                        <div class="detail-item detail-order-item">
                            <div class="detail-image">
                                <img src="${pageContext.request.contextPath}/load-image?name=<%= ct.getHinhAnh() %>"
                                     alt="<%= ct.getTenThuoc() %>">
                            </div>

                            <div class="detail-info">
                                <h3><%= ct.getTenThuoc() %></h3>
                                <p>Đơn giá: <strong><%= CurrencyUtil.yen(ct.getDonGia()) %></strong></p>
                                <p>Số lượng: <strong><%= ct.getSoLuong() %></strong></p>
                            </div>

                            <div class="detail-total">
                                <span>Thành tiền</span>
                                <strong><%= CurrencyUtil.yen(ct.getThanhTien()) %></strong>
                            </div>
                        </div>
                    <% } %>
                </div>
            <% } %>

        </div>
    </div>
</body>
</html>