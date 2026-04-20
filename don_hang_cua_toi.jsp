<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="model.DonHang" %>
<%@ page import="utils.CurrencyUtil" %>
<%@ page import="utils.InputSanitizer" %>

<%
    List<DonHang> dsDonHang = (List<DonHang>) request.getAttribute("dsDonHang");
    if (dsDonHang == null) dsDonHang = new ArrayList<>();

    String cartMessage = (String) session.getAttribute("cartMessage");
    if (cartMessage != null) {
        session.removeAttribute("cartMessage");
    }
%>

<%!
    public String hienThiTrangThai(String trangThai) {
        if (trangThai == null) return "Chưa cập nhật";

        switch (trangThai) {
            case "CHO_XAC_NHAN":
                return "Chờ xác nhận";
            case "DA_XAC_NHAN":
                return "Đã xác nhận";
            case "DANG_CHUAN_BI":
                return "Đang chuẩn bị";
            case "DANG_GIAO":
                return "Đang giao";
            case "DA_GIAO":
                return "Đã giao";
            case "HOAN_THANH":
                return "Hoàn thành";
            case "DA_HUY":
                return "Đã hủy";
            default:
                return trangThai;
        }
    }

    public String hienThiThanhToan(String pt) {
        if (pt == null) return "Chưa cập nhật";

        switch (pt) {
            case "COD":
                return "Nhận hàng rồi trả tiền";
            case "CHUYEN_KHOAN":
                return "Chuyển khoản";
            case "THE":
                return "Thanh toán thẻ";
            default:
                return pt;
        }
    }

    public String hienThiTrangThaiThanhToan(String tt) {
        if (tt == null) return "Chưa cập nhật";

        switch (tt) {
            case "CHUA_THANH_TOAN":
                return "Chưa thanh toán";
            case "CHO_XAC_NHAN":
                return "Chờ xác nhận";
            case "DA_THANH_TOAN":
                return "Đã thanh toán";
            case "TU_CHOI":
                return "Từ chối";
            default:
                return tt;
        }
    }
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đơn hàng của tôi</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/don-hang-cua-toi.css?v=2">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
    <div class="order-page">
        <div class="order-container">

            <div class="order-header">
                <h1>Đơn hàng của tôi</h1>
                <a href="${pageContext.request.contextPath}/user/home" class="back-btn">
                    <i class="fa-solid fa-arrow-left"></i>
                    <span>Quay lại trang chủ</span>
                </a>
            </div>

            <% if (cartMessage != null) { %>
                <div class="message-box"><%= InputSanitizer.escapeHtml(cartMessage) %></div>
            <% } %>

            <% if (dsDonHang.isEmpty()) { %>
                <div class="empty-order">
                    <i class="fa-solid fa-box-open"></i>
                    <h2>Bạn chưa có đơn hàng nào</h2>
                    <p>Hãy chọn sản phẩm và đặt hàng để xem tại đây.</p>
                </div>
            <% } else { %>
                <div class="order-list">
                    <% for (DonHang dh : dsDonHang) { %>
                        <%
                            boolean coTheHuy = false;
                            if (dh.getNgayDat() != null) {
                                long diffMs = System.currentTimeMillis() - dh.getNgayDat().getTime();
                                long limitMs = 8L * 60 * 60 * 1000;

                                if (diffMs <= limitMs &&
                                    ("CHO_XAC_NHAN".equals(dh.getTrangThai()) || "DA_XAC_NHAN".equals(dh.getTrangThai()))) {
                                    coTheHuy = true;
                                }
                            }

                            String tenSanPham = dh.getTenSanPhamDaiDien() == null ? "Sản phẩm" : dh.getTenSanPhamDaiDien();
                            String tenNguoiNhan = dh.getTenNguoiNhan() == null ? "" : dh.getTenNguoiNhan();
                            String sdtNhan = dh.getSdtNhan() == null ? "" : dh.getSdtNhan();
                            String diaChiNhan = dh.getDiaChiNhan() == null ? "" : dh.getDiaChiNhan();
                            String ghiChu = (dh.getGhiChu() == null || dh.getGhiChu().trim().isEmpty()) ? "Không có" : dh.getGhiChu();

                            String hinhAnh = dh.getHinhAnhDaiDien();
                            String imageUrl = request.getContextPath() + "/load-image?name=" +
                                    URLEncoder.encode(hinhAnh == null ? "" : hinhAnh, "UTF-8");
                        %>

                        <div class="order-card">
                            <div class="order-top">
                                <div>
                                    <h3>Đơn hàng #<%= dh.getIdDonHang() %></h3>
                                    <p>Ngày đặt: <%= dh.getNgayDat() %></p>
                                </div>
                                <div class="order-status-wrap">
                                    <span class="status order-status"><%= InputSanitizer.escapeHtml(hienThiTrangThai(dh.getTrangThai())) %></span>
                                    <span class="status payment-status"><%= InputSanitizer.escapeHtml(hienThiTrangThaiThanhToan(dh.getTrangThaiThanhToan())) %></span>
                                </div>
                            </div>

                            <div class="order-product-preview">
                                <div class="order-product-image">
                                    <img src="<%= imageUrl %>"
                                         alt="<%= InputSanitizer.escapeHtml(tenSanPham) %>">
                                </div>

                                <div class="order-product-info">
                                    <h4><%= InputSanitizer.escapeHtml(tenSanPham) %></h4>
                                    <p>
                                        <%= dh.getTongSoSanPham() <= 1
                                                ? "1 sản phẩm"
                                                : dh.getTongSoSanPham() + " sản phẩm trong đơn" %>
                                    </p>
                                </div>
                            </div>

                            <div class="order-info-grid">
                                <div class="info-item">
                                    <span class="label">Người nhận</span>
                                    <span class="value"><%= InputSanitizer.escapeHtml(tenNguoiNhan) %></span>
                                </div>

                                <div class="info-item">
                                    <span class="label">Số điện thoại</span>
                                    <span class="value"><%= InputSanitizer.escapeHtml(sdtNhan) %></span>
                                </div>

                                <div class="info-item full">
                                    <span class="label">Địa chỉ nhận</span>
                                    <span class="value"><%= InputSanitizer.escapeHtml(diaChiNhan) %></span>
                                </div>

                                <div class="info-item">
                                    <span class="label">Thanh toán</span>
                                    <span class="value"><%= InputSanitizer.escapeHtml(hienThiThanhToan(dh.getPhuongThucThanhToan())) %></span>
                                </div>

                                <div class="info-item">
                                    <span class="label">Tổng tiền</span>
                                    <span class="value total-price"><%= CurrencyUtil.yen(dh.getTongTien()) %></span>
                                </div>

                                <div class="info-item full">
                                    <span class="label">Ghi chú</span>
                                    <span class="value"><%= InputSanitizer.escapeHtml(ghiChu) %></span>
                                </div>
                            </div>

                            <div class="order-actions">
                                <a href="${pageContext.request.contextPath}/chi-tiet-don-hang-cua-toi?id=<%= dh.getIdDonHang() %>" class="detail-btn">
                                    Xem chi tiết
                                </a>

                                <% if (coTheHuy) { %>
                                    <form action="${pageContext.request.contextPath}/huy-don-hang" method="post" style="display:inline-block; margin-left:10px;">
                                        <input type="hidden" name="csrfToken" value="${sessionScope.csrf_token}">
                                        <input type="hidden" name="idDonHang" value="<%= dh.getIdDonHang() %>">
                                        <button type="submit" class="cancel-btn"
                                                onclick="return confirm('Bạn có chắc muốn hủy đơn hàng này không?')">
                                            Hủy đơn
                                        </button>
                                    </form>
                                <% } else { %>
                                    <button type="button" class="cancel-btn disabled" disabled>
                                        Không thể hủy
                                    </button>
                                <% } %>
                            </div>
                        </div>
                    <% } %>
                </div>
            <% } %>

        </div>
    </div>
</body>
</html>