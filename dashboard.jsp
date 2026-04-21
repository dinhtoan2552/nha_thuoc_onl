<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="model.DonHang" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="utils.CurrencyUtil" %>
<%!
    public String hienThiTrangThai(String trangThai) {
        if (trangThai == null) return "Chưa cập nhật";
        switch (trangThai) {
            case "CHO_XAC_NHAN": return "Chờ xác nhận";
            case "DA_XAC_NHAN": return "Đã xác nhận";
            case "DANG_GIAO": return "Đang giao";
            case "HOAN_THANH": return "Hoàn thành";
            case "DA_HUY": return "Đã hủy";
            default: return trangThai;
        }
    }
%>

<%
    Integer tongDonHang = (Integer) request.getAttribute("tongDonHang");
    Integer choXacNhan = (Integer) request.getAttribute("choXacNhan");
    Integer dangGiao = (Integer) request.getAttribute("dangGiao");
    Integer hoanThanh = (Integer) request.getAttribute("hoanThanh");
    Integer daHuy = (Integer) request.getAttribute("daHuy");

    List<DonHang> donHangMoi = (List<DonHang>) request.getAttribute("donHangMoi");
    if (donHangMoi == null) donHangMoi = new ArrayList<>();

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bảng điều khiển nhân viên</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/staff/dashboard.css?v=10001">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
</head>
<body>
<div class="staff-layout">
    <jsp:include page="/jsp/staff/staff_sidebar.jsp" />

    <div class="staff-main">
        <div class="staff-topbar">
            <div>
                <h1>Tổng quan nhân viên</h1>
                <p>Theo dõi nhanh tình trạng đơn hàng mới trong hệ thống.</p>
            </div>
            <a href="${pageContext.request.contextPath}/logout" class="logout-btn">
                <i class="fa-solid fa-right-from-bracket"></i>
                <span>Đăng xuất</span>
            </a>
        </div>

        <div class="stats-grid">
            <div class="stat-card total">
                <div class="stat-icon"><i class="fa-solid fa-box-archive"></i></div>
                <div class="stat-content">
                    <span>Tổng đơn</span>
                    <h2><%= (tongDonHang != null) ? tongDonHang : 0 %></h2>
                </div>
            </div>

            <div class="stat-card pending">
                <div class="stat-icon"><i class="fa-solid fa-clock-rotate-left"></i></div>
                <div class="stat-content">
                    <span>Chờ duyệt</span>
                    <h2><%= (choXacNhan != null) ? choXacNhan : 0 %></h2>
                </div>
            </div>

            <div class="stat-card shipping">
                <div class="stat-icon"><i class="fa-solid fa-truck-fast"></i></div>
                <div class="stat-content">
                    <span>Đang giao</span>
                    <h2><%= (dangGiao != null) ? dangGiao : 0 %></h2>
                </div>
            </div>

            <div class="stat-card done">
                <div class="stat-icon"><i class="fa-solid fa-check-double"></i></div>
                <div class="stat-content">
                    <span>Thành công</span>
                    <h2><%= (hoanThanh != null) ? hoanThanh : 0 %></h2>
                </div>
            </div>

            <div class="stat-card cancel">
                <div class="stat-icon"><i class="fa-solid fa-circle-xmark"></i></div>
                <div class="stat-content">
                    <span>Đã hủy</span>
                    <h2><%= (daHuy != null) ? daHuy : 0 %></h2>
                </div>
            </div>
        </div>

        <div class="table-card">
            <div class="table-header">
                <h2>Đơn hàng mới nhận</h2>
                <a href="${pageContext.request.contextPath}/staff/donhang" class="view-all-btn">Tất cả đơn hàng</a>
            </div>

            <div class="table-wrap">
                <div class="mobile-order-list">
                    <% if (donHangMoi.isEmpty()) { %>
                        <div class="mobile-order-empty">Chưa có dữ liệu đơn hàng.</div>
                    <% } else {
                        for (DonHang dh : donHangMoi) {
                            String hinhAnhMobile = dh.getHinhAnhDaiDien();
                            if (hinhAnhMobile == null || hinhAnhMobile.trim().isEmpty() || "null".equalsIgnoreCase(hinhAnhMobile.trim())) {
                                hinhAnhMobile = "no-image.png";
                            }

                            String tenSanPhamMobile = dh.getTenSanPhamDaiDien();
                            if (tenSanPhamMobile == null || tenSanPhamMobile.trim().isEmpty() || "null".equalsIgnoreCase(tenSanPhamMobile.trim())) {
                                tenSanPhamMobile = "Chưa có tên sản phẩm";
                            }
                    %>
                        <div class="mobile-order-card">
                            <div class="mobile-order-top">
                                <div class="mobile-order-thumb">
                                    <img src="<%= request.getContextPath() %>/load-image?name=<%= hinhAnhMobile %>" alt="Sản phẩm">
                                </div>

                                <div class="mobile-order-content">
                                    <div class="mobile-order-name"><%= tenSanPhamMobile %></div>
                                    <div class="mobile-order-info"><%= dh.getTenKhachHang() %></div>
                                    <div class="mobile-order-info"><%= dh.getSoDienThoaiKhach() %></div>
                                </div>
                            </div>

                            <div class="mobile-order-bottom">
                                <span class="mobile-order-price"><%= CurrencyUtil.yen(dh.getTongTien()) %></span>
                                <a href="${pageContext.request.contextPath}/staff/chi-tiet-don-hang?id=<%= dh.getIdDonHang() %>"
                                   class="mobile-order-btn">Xem</a>
                            </div>
                        </div>
                    <%  }
                       } %>
                </div>

                <table class="order-table">
                    <thead>
                        <tr>
                            <th>Mã đơn</th>
                            <th>Sản phẩm đại diện</th>
                            <th>Khách hàng</th>
                            <th>Số điện thoại</th>
                            <th>Tổng tiền</th>
                            <th>Trạng thái</th>
                            <th>Thời gian</th>
                            <th>Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                    <% if (donHangMoi.isEmpty()) { %>
                        <tr>
                            <td colspan="8" class="empty-row">Chưa có dữ liệu đơn hàng.</td>
                        </tr>
                    <% } else {
                        for (DonHang dh : donHangMoi) {
                            String hinhAnh = dh.getHinhAnhDaiDien();
                            if (hinhAnh == null || hinhAnh.trim().isEmpty() || "null".equalsIgnoreCase(hinhAnh.trim())) {
                                hinhAnh = "no-image.png";
                            }

                            String tenSanPham = dh.getTenSanPhamDaiDien();
                            if (tenSanPham == null || tenSanPham.trim().isEmpty() || "null".equalsIgnoreCase(tenSanPham.trim())) {
                                tenSanPham = "Chưa có tên sản phẩm";
                            }

                            String cl = "status-badge";
                            String st = dh.getTrangThai();
                            if ("CHO_XAC_NHAN".equals(st)) cl += " status-cho-xac-nhan";
                            else if ("DA_XAC_NHAN".equals(st)) cl += " status-da-xac-nhan";
                            else if ("DANG_GIAO".equals(st)) cl += " status-dang-giao";
                            else if ("HOAN_THANH".equals(st)) cl += " status-hoan-thanh";
                            else if ("DA_HUY".equals(st)) cl += " status-da-huy";
                    %>
                        <tr>
                            <td class="order-id">#<%= dh.getIdDonHang() %></td>

                            <td>
                                <div class="product-cell">
                                    <div class="product-thumb">
                                        <img src="<%= request.getContextPath() %>/load-image?name=<%= hinhAnh %>" alt="Sản phẩm">
                                    </div>
                                    <div class="product-info">
                                        <div class="product-name"><%= tenSanPham %></div>
                                        <div class="product-sub">Sản phẩm đại diện đơn hàng</div>
                                    </div>
                                </div>
                            </td>

                            <td>
                                <div class="customer-cell">
                                    <strong><%= dh.getTenKhachHang() %></strong>
                                </div>
                            </td>

                            <td class="phone-text"><%= dh.getSoDienThoaiKhach() %></td>

                            <td class="money"><%= CurrencyUtil.yen(dh.getTongTien()) %></td>

                            <td>
                                <span class="<%= cl %>"><%= hienThiTrangThai(st) %></span>
                            </td>

                            <td class="time-text">
                                <%= (dh.getNgayDat() != null) ? sdf.format(dh.getNgayDat()) : "---" %>
                            </td>

                            <td>
                                <div class="action-group">
                                    <a href="${pageContext.request.contextPath}/staff/chi-tiet-don-hang?id=<%= dh.getIdDonHang() %>" class="btn-detail">
                                        <i class="fa-solid fa-eye"></i>
                                        <span>Xem</span>
                                    </a>
                                </div>
                            </td>
                        </tr>
                    <% } } %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>