<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.DonHang" %>
<%@ page import="model.ChiTietDonHang" %>
<%@ page import="java.util.List" %>
<%@ page import="utils.CurrencyUtil" %>
<%
    DonHang donHang = (DonHang) request.getAttribute("donHang");
    List<ChiTietDonHang> danhSachChiTiet = donHang != null ? donHang.getDanhSachChiTiet() : new java.util.ArrayList<>();
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chi tiết đơn hàng</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/chitiet-donhang.css">
</head>
<body>
    <div class="admin-layout">
        <jsp:include page="/jsp/admin/admin_sidebar.jsp" />

        <div class="admin-main">
            <div class="admin-topbar">
                <div class="topbar-left">
                    <h1>Chi tiết đơn hàng</h1>
                    <p>Theo dõi đầy đủ thông tin đơn hàng và sản phẩm</p>
                </div>
                <div class="topbar-right">
                    <a class="back-btn" href="${pageContext.request.contextPath}/admin/donhang">Quay lại</a>
                </div>
            </div>

            <% if (donHang != null) { %>
                <div class="detail-grid">
                    <div class="detail-card">
                        <h3>Thông tin đơn hàng</h3>

                        <div class="detail-row">
                            <span>Mã đơn:</span>
                            <strong>#DH<%= donHang.getIdDonHang() %></strong>
                        </div>

                        <div class="detail-row">
                            <span>Ngày đặt:</span>
                            <strong><%= donHang.getNgayDat() != null ? donHang.getNgayDat().toString().replace(".0", "") : "" %></strong>
                        </div>

                        <div class="detail-row">
                            <span>Trạng thái:</span>
                            <strong><%= donHang.getTrangThai() != null ? donHang.getTrangThai().replace("_", " ") : "" %></strong>
                        </div>

                        <div class="detail-row">
                            <span>Tiền thuốc:</span>
                            <strong><%= CurrencyUtil.yen(donHang.getTongTien()) %></strong>
                        </div>

                        <div class="detail-row">
                            <span>Phí ship:</span>
                            <strong><%= CurrencyUtil.yen(donHang.getPhiVanChuyen()) %></strong>
                        </div>

                        <div class="detail-row">
                            <span>Tổng thanh toán:</span>
                            <strong class="price-highlight"><%= CurrencyUtil.yen(donHang.getTongTien() + donHang.getPhiVanChuyen()) %></strong>
                        </div>

                        <div class="detail-row">
                            <span>Người xử lý:</span>
                            <strong><%= donHang.getTenNguoiXuLy() != null ? donHang.getTenNguoiXuLy() : "Chưa có" %></strong>
                        </div>

                        <form action="${pageContext.request.contextPath}/admin/donhang/cap-nhat-ship" method="post" class="ship-form">
                            <input type="hidden" name="idDonHang" value="<%= donHang.getIdDonHang() %>">

                            <div class="detail-row ship-edit-row">
                                <span>Nhập phí ship:</span>
                                <div class="ship-edit-box">
                                    <input type="number" name="phiVanChuyen" min="0" step="1"
                                           value="<%= (int) donHang.getPhiVanChuyen() %>" class="ship-input" required>
                                    <button type="submit" class="ship-save-btn">Lưu ship</button>
                                </div>
                            </div>
                        </form>
                    </div>

                    <div class="detail-card">
                        <h3>Thông tin người nhận</h3>
                        <div class="detail-row"><span>Khách hàng:</span><strong><%= donHang.getTenKhachHang() != null ? donHang.getTenKhachHang() : "" %></strong></div>
                        <div class="detail-row"><span>Email:</span><strong><%= donHang.getEmailKhachHang() != null ? donHang.getEmailKhachHang() : "" %></strong></div>
                        <div class="detail-row"><span>Số điện thoại:</span><strong><%= donHang.getSdtNhan() != null ? donHang.getSdtNhan() : "" %></strong></div>
                        <div class="detail-row"><span>Địa chỉ:</span><strong><%= donHang.getDiaChiNhan() != null ? donHang.getDiaChiNhan() : "" %></strong></div>
                        <div class="detail-row note-row"><span>Ghi chú:</span><strong><%= donHang.getGhiChu() != null && !donHang.getGhiChu().trim().isEmpty() ? donHang.getGhiChu() : "Không có" %></strong></div>
                    </div>
                </div>

                <div class="content-card">
                    <div class="section-title">Danh sách sản phẩm</div>

                    <div class="table-box">
                        <table>
                            <thead>
                                <tr>
                                    <th>Sản phẩm</th>
                                    <th>Đơn giá</th>
                                    <th>Số lượng</th>
                                    <th>Thành tiền</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% if (danhSachChiTiet.isEmpty()) { %>
                                    <tr>
                                        <td colspan="4" class="empty-row">Không có chi tiết đơn hàng</td>
                                    </tr>
                                <% } else { %>
                                    <% for (ChiTietDonHang ct : danhSachChiTiet) { %>
                                        <tr>
                                            <td>
                                                <div class="product-cell">
                                                    <% if (ct.getHinhAnh() != null && !ct.getHinhAnh().trim().isEmpty()) { %>
                                                        <img src="${pageContext.request.contextPath}/image?name=<%= ct.getHinhAnh() %>" alt="Ảnh thuốc" class="product-img">
                                                    <% } %>
                                                    <span><%= ct.getTenThuoc() != null ? ct.getTenThuoc() : "" %></span>
                                                </div>
                                            </td>
                                            <td><%= CurrencyUtil.yen(ct.getDonGia()) %></td>
                                            <td><%= ct.getSoLuong() %></td>
                                            <td class="price-highlight"><%= CurrencyUtil.yen(ct.getThanhTien()) %></td>
                                        </tr>
                                    <% } %>
                                <% } %>
                            </tbody>
                        </table>
                    </div>
                </div>
            <% } %>
        </div>
    </div>
</body>
</html>