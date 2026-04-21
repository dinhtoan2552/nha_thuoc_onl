<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.DonHang" %>
<%@ page import="utils.CurrencyUtil" %>
<%
    List<DonHang> danhSachDonHang = (List<DonHang>) request.getAttribute("danhSachDonHang");
    if (danhSachDonHang == null) danhSachDonHang = new java.util.ArrayList<>();

    String keyword = request.getAttribute("keyword") != null ? request.getAttribute("keyword").toString() : "";
    String trangThai = request.getAttribute("trangThai") != null ? request.getAttribute("trangThai").toString() : "";

    Integer tongDonHang = (Integer) request.getAttribute("tongDonHang");
    Integer donChoXacNhan = (Integer) request.getAttribute("donChoXacNhan");
    Integer donDangGiao = (Integer) request.getAttribute("donDangGiao");
    Integer donDaGiao = (Integer) request.getAttribute("donDaGiao");

    if (tongDonHang == null) tongDonHang = 0;
    if (donChoXacNhan == null) donChoXacNhan = 0;
    if (donDangGiao == null) donDangGiao = 0;
    if (donDaGiao == null) donDaGiao = 0;
%>
<%
    String adminOrderMessage = (String) session.getAttribute("adminOrderMessage");
    if (adminOrderMessage != null) {
        session.removeAttribute("adminOrderMessage");
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý đơn hàng</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/quanly-donhang.css">
</head>
<body>
    <div class="admin-layout">
        <jsp:include page="/jsp/admin/admin_sidebar.jsp" />

        <div class="admin-main">
            <div class="admin-topbar">
                <% if (adminOrderMessage != null) { %>
    <div class="message-box" style="margin-bottom:16px;background:#ecfdf5;color:#166534;border:1px solid #bbf7d0;padding:12px 16px;border-radius:12px;font-weight:600;">
        <%= adminOrderMessage %>
    </div>
<% } %>
                <div>
                    <h1>Quản lý đơn hàng</h1>
                    <p>Theo dõi, tìm kiếm và cập nhật trạng thái đơn hàng</p>
                </div>
                <div class="topbar-actions">
                    <a class="logout-btn" href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
                </div>
            </div>

            <div class="stats-grid">
                <div class="stat-card">
                    <span class="stat-label">Tổng đơn hàng</span>
                    <h3><%= tongDonHang %></h3>
                </div>
                <div class="stat-card warning">
                    <span class="stat-label">Chờ xác nhận</span>
                    <h3><%= donChoXacNhan %></h3>
                </div>
                <div class="stat-card info">
                    <span class="stat-label">Đang giao</span>
                    <h3><%= donDangGiao %></h3>
                </div>
                <div class="stat-card success">
                    <span class="stat-label">Đã giao</span>
                    <h3><%= donDaGiao %></h3>
                </div>
            </div>

            <div class="filter-card">
                <form action="${pageContext.request.contextPath}/admin/donhang" method="get" class="filter-form">
                    <div class="filter-group">
                        <label>Tìm kiếm</label>
                        <input type="text" name="keyword" value="<%= keyword %>" placeholder="Tên khách hàng, email, mã đơn...">
                    </div>

                    <div class="filter-group">
                        <label>Trạng thái</label>
                        <select name="trangThai">
                            <option value="">Tất cả trạng thái</option>
                            <option value="CHO_XAC_NHAN" <%= "CHO_XAC_NHAN".equals(trangThai) ? "selected" : "" %>>Chờ xác nhận</option>
                            <option value="DA_XAC_NHAN" <%= "DA_XAC_NHAN".equals(trangThai) ? "selected" : "" %>>Đã xác nhận</option>
                            <option value="DANG_CHUAN_BI" <%= "DANG_CHUAN_BI".equals(trangThai) ? "selected" : "" %>>Đang chuẩn bị</option>
                            <option value="DANG_GIAO" <%= "DANG_GIAO".equals(trangThai) ? "selected" : "" %>>Đang giao</option>
                            <option value="DA_GIAO" <%= "DA_GIAO".equals(trangThai) ? "selected" : "" %>>Đã giao</option>
                            <option value="DA_HUY" <%= "DA_HUY".equals(trangThai) ? "selected" : "" %>>Đã hủy</option>
                        </select>
                    </div>

                    <div class="filter-actions">
                        <button type="submit" class="filter-btn">Lọc đơn hàng</button>
                        <a class="reset-btn" href="${pageContext.request.contextPath}/admin/donhang">Đặt lại</a>
                    </div>
                </form>
            </div>

            <div class="content-card">
    <form action="${pageContext.request.contextPath}/admin/donhang" method="post" id="bulkOrderForm">
        <input type="hidden" name="keyword" value="<%= keyword %>">
        <input type="hidden" name="trangThai" value="<%= trangThai %>">

        <div style="display:flex;gap:10px;align-items:center;justify-content:flex-end;margin-bottom:14px;flex-wrap:wrap;">
            <button type="button" class="filter-btn" onclick="selectAllPendingOrders()">Chọn tất cả đơn chờ xác nhận</button>
            <button type="submit" name="action" value="xacNhanTatCa" class="filter-btn">Xác nhận tất</button>
            <input type="text" name="lyDoHuy" placeholder="Lý do hủy đơn..." style="min-width:260px;padding:10px 12px;border:1px solid #dbe5f3;border-radius:10px;">
            <button type="submit" name="action" value="huyNhieuDon" class="reset-btn" style="background:#ef4444;color:#fff;border-color:#ef4444;">Hủy các đơn đã chọn</button>
        </div>

        <div class="table-box">
            <table>
                <thead>
                    <tr>
                        <th style="width:50px;">
                            <input type="checkbox" id="checkAllAdmin">
                        </th>
                        <th>Mã đơn</th>
                        <th>Khách hàng</th>
                        <th>Ngày đặt</th>
                        <th>Liên hệ</th>
                        <th>Tổng tiền</th>
                        <th>Trạng thái</th>
                        <th>Lý do hủy</th>
                        <th>Xử lý nhanh</th>
                        <th>Hành động</th>
                    </tr>
                </thead>
                <tbody>
                    <% if (danhSachDonHang.isEmpty()) { %>
                        <tr>
                            <td colspan="10" class="empty-row">Chưa có đơn hàng nào</td>
                        </tr>
                    <% } else { %>
                        <% for (DonHang dh : danhSachDonHang) { %>
                            <tr>
                                <td>
                                    <% if ("CHO_XAC_NHAN".equals(dh.getTrangThai())) { %>
                                        <input type="checkbox" class="order-checkbox admin-order-checkbox"
                                               data-status="<%= dh.getTrangThai() %>"
                                               name="selectedOrderIds"
                                               value="<%= dh.getIdDonHang() %>">
                                    <% } %>
                                </td>
                                <td class="order-code">#DH<%= dh.getIdDonHang() %></td>
                                <td>
                                    <div class="customer-block">
                                        <strong><%= dh.getTenKhachHang() != null ? dh.getTenKhachHang() : "" %></strong>
                                        <span><%= dh.getEmailKhachHang() != null ? dh.getEmailKhachHang() : "" %></span>
                                    </div>
                                </td>
                                <td><%= dh.getNgayDat() != null ? dh.getNgayDat().toString().replace(".0", "") : "" %></td>
                                <td>
                                    <div class="contact-block">
                                        <span><%= dh.getSdtNhan() != null ? dh.getSdtNhan() : "" %></span>
                                        <small><%= dh.getDiaChiNhan() != null ? dh.getDiaChiNhan() : "" %></small>
                                    </div>
                                </td>
                                <td class="price-cell"><%= CurrencyUtil.yen(dh.getTongTien()) %></td>
                                <td>
                                    <span class="status-badge
                                        <%= "CHO_XAC_NHAN".equals(dh.getTrangThai()) ? "status-wait" :
                                            "DA_XAC_NHAN".equals(dh.getTrangThai()) ? "status-confirm" :
                                            "DANG_CHUAN_BI".equals(dh.getTrangThai()) ? "status-preparing" :
                                            "DANG_GIAO".equals(dh.getTrangThai()) ? "status-delivering" :
                                            "DA_GIAO".equals(dh.getTrangThai()) ? "status-done" :
                                            "status-cancel" %>">
                                        <%= dh.getTrangThai().replace("_", " ") %>
                                    </span>
                                </td>
                                <td><%= dh.getLyDoHuy() == null ? "" : dh.getLyDoHuy() %></td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/admin/donhang/capnhat-trangthai" method="post" class="status-form">
                                        <input type="hidden" name="idDonHang" value="<%= dh.getIdDonHang() %>">
                                        <select name="trangThai" class="status-select">
                                            <option value="CHO_XAC_NHAN" <%= "CHO_XAC_NHAN".equals(dh.getTrangThai()) ? "selected" : "" %>>Chờ xác nhận</option>
                                            <option value="DANG_CHUAN_BI" <%= "DANG_CHUAN_BI".equals(dh.getTrangThai()) ? "selected" : "" %>>Đang chuẩn bị</option>
                                            <option value="DANG_GIAO" <%= "DANG_GIAO".equals(dh.getTrangThai()) ? "selected" : "" %>>Đang giao</option>
                                            <option value="DA_GIAO" <%= "DA_GIAO".equals(dh.getTrangThai()) ? "selected" : "" %>>Đã giao</option>
                                            <option value="DA_HUY" <%= "DA_HUY".equals(dh.getTrangThai()) ? "selected" : "" %>>Đã hủy</option>
                                        </select>
                                        <button type="submit" class="update-btn">Cập nhật</button>
                                    </form>
                                </td>
                                <td>
                                    <a class="detail-btn" href="${pageContext.request.contextPath}/admin/donhang/chitiet?id=<%= dh.getIdDonHang() %>">Xem</a>
                                </td>
                            </tr>
                        <% } %>
                    <% } %>
                </tbody>
            </table>
        </div>
    </form>
</div>

<script>
document.getElementById("checkAllAdmin")?.addEventListener("change", function () {
    const checked = this.checked;
    document.querySelectorAll(".admin-order-checkbox").forEach(function (cb) {
        cb.checked = checked;
    });
});

function selectAllPendingOrders() {
    document.querySelectorAll(".admin-order-checkbox").forEach(function (cb) {
        cb.checked = cb.dataset.status === "CHO_XAC_NHAN";
    });
}
</script>

        </div>
    </div>
</body>
</html>