<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.DonHang" %>
<%@ page import="utils.CurrencyUtil" %>
<%!
    public String hienThiTrangThai(String trangThai) {
        if (trangThai == null) return "Chưa cập nhật";

        switch (trangThai) {
            case "CHO_XAC_NHAN": return "Chờ xác nhận";
            case "DA_XAC_NHAN": return "Đã xác nhận";
            case "DANG_CHUAN_BI": return "Đang chuẩn bị";
            case "DANG_GIAO": return "Đang giao";
            case "DA_GIAO": return "Đã giao";
            case "HOAN_THANH": return "Hoàn thành";
            case "DA_HUY": return "Đã hủy";
            default: return trangThai;
        }
    }

    public String hienThiThanhToan(String trangThai) {
        if (trangThai == null) return "Chưa cập nhật";

        switch (trangThai) {
            case "CHUA_THANH_TOAN": return "Chưa thanh toán";
            case "CHO_XAC_NHAN": return "Chờ xác nhận bill";
            case "DA_THANH_TOAN": return "Đã thanh toán";
            case "TU_CHOI": return "Từ chối";
            default: return trangThai;
        }
    }
%>

<%
    List<DonHang> listDonHang = (List<DonHang>) request.getAttribute("listDonHang");
    if (listDonHang == null) listDonHang = new ArrayList<>();

    String keyword = (String) request.getAttribute("keyword");
    String trangThai = (String) request.getAttribute("trangThai");
    if (keyword == null) keyword = "";
    if (trangThai == null) trangThai = "";

    String staffOrderMessage = (String) session.getAttribute("staffOrderMessage");
    if (staffOrderMessage != null) {
        session.removeAttribute("staffOrderMessage");
    }
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Xử lý đơn hàng</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/staff/donhang.css?v=10">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
</head>
<body>
<div class="staff-layout">
    <jsp:include page="/jsp/staff/staff_sidebar.jsp" />

    <div class="staff-main">
        <div class="staff-topbar">
            <div>
                <h1>Xử lý đơn hàng</h1>
                <p>Theo dõi đơn, bill chuyển khoản và xác nhận thanh toán cho khách.</p>
            </div>

            <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
        </div>

        <% if (staffOrderMessage != null) { %>
            <div class="message-box"><%= staffOrderMessage %></div>
        <% } %>

        <div class="filter-card">
            <form action="${pageContext.request.contextPath}/staff/donhang" method="get" class="filter-form">
                <div class="filter-group search-group">
                    <label>Tìm kiếm</label>
                    <input type="text" name="keyword" value="<%= keyword %>" placeholder="Tên khách, email, mã đơn...">
                </div>

                <div class="filter-group">
                    <label>Trạng thái đơn</label>
                    <select name="trangThai">
                        <option value="">Tất cả</option>
                        <option value="CHO_XAC_NHAN" <%= "CHO_XAC_NHAN".equals(trangThai) ? "selected" : "" %>>Chờ xác nhận</option>
                        <option value="DA_XAC_NHAN" <%= "DA_XAC_NHAN".equals(trangThai) ? "selected" : "" %>>Đã xác nhận</option>
                        <option value="DANG_CHUAN_BI" <%= "DANG_CHUAN_BI".equals(trangThai) ? "selected" : "" %>>Đang chuẩn bị</option>
                        <option value="DANG_GIAO" <%= "DANG_GIAO".equals(trangThai) ? "selected" : "" %>>Đang giao</option>
                        <option value="DA_GIAO" <%= "DA_GIAO".equals(trangThai) ? "selected" : "" %>>Đã giao</option>
                        <option value="DA_HUY" <%= "DA_HUY".equals(trangThai) ? "selected" : "" %>>Đã hủy</option>
                    </select>
                </div>

                <div class="filter-actions">
                    <button type="submit" class="btn-filter">Lọc</button>
                    <a href="${pageContext.request.contextPath}/staff/donhang" class="btn-reset">Đặt lại</a>
                </div>
            </form>
        </div>

        <div class="table-card">
    <div class="table-header">
        <h2>Danh sách đơn hàng</h2>
        <span><%= listDonHang.size() %> đơn</span>
    </div>

    <form action="${pageContext.request.contextPath}/staff/donhang" method="post" id="bulkStaffOrderForm">
        <input type="hidden" name="keyword" value="<%= keyword %>">
        <input type="hidden" name="trangThai" value="<%= trangThai %>">

        <div style="display:flex;gap:10px;align-items:center;justify-content:flex-end;margin-bottom:14px;flex-wrap:wrap;">
            <button type="button" class="btn-filter" onclick="selectAllPendingOrdersStaff()">Chọn tất cả đơn chờ xác nhận</button>
            <button type="submit" name="action" value="xacNhanTatCa" class="btn-filter">Xác nhận tất</button>
            <input type="text" name="lyDoHuy" placeholder="Lý do hủy đơn..." style="min-width:260px;padding:10px 12px;border:1px solid #dbe5f3;border-radius:10px;">
            <button type="submit" name="action" value="huyNhieuDon" class="btn-reset" style="background:#ef4444;color:#fff;border-color:#ef4444;">Hủy các đơn đã chọn</button>
        </div>

        <% if (listDonHang.isEmpty()) { %>
            <div class="empty-box">
                <p>Không có đơn hàng nào phù hợp.</p>
            </div>
        <% } else { %>
            <div class="table-wrap">
                <table class="order-table">
                    <thead>
                        <tr>
                            <th style="width:50px;">
                                <input type="checkbox" id="checkAllStaff">
                            </th>
                            <th>Mã đơn</th>
                            <th>Khách hàng</th>
                            <th>Tổng tiền</th>
                            <th>Thanh toán</th>
                            <th>Bill</th>
                            <th>Trạng thái đơn</th>
                            <th>Lý do hủy</th>
                            <th>Ngày đặt</th>
                            <th>Xử lý</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (DonHang dh : listDonHang) { %>
                            <tr>
                                <td>
                                    <% if ("CHO_XAC_NHAN".equals(dh.getTrangThai())) { %>
                                        <input type="checkbox"
                                               class="staff-order-checkbox"
                                               data-status="<%= dh.getTrangThai() %>"
                                               name="selectedOrderIds"
                                               value="<%= dh.getIdDonHang() %>">
                                    <% } %>
                                </td>

                                <td>#<%= dh.getIdDonHang() %></td>

                                <td>
                                    <div class="customer-cell">
                                        <strong><%= dh.getTenKhachHang() == null ? "Khách hàng" : dh.getTenKhachHang() %></strong>
                                        <span><%= dh.getEmailKhachHang() == null ? "" : dh.getEmailKhachHang() %></span>
                                        <small><%= dh.getSoDienThoaiKhach() == null ? "" : dh.getSoDienThoaiKhach() %></small>
                                    </div>
                                </td>

                                <td class="money"><%= CurrencyUtil.yen(dh.getTongTien()) %></td>

                                <td>
                                    <div class="customer-cell">
                                        <strong><%= dh.getPhuongThucThanhToan() == null ? "" : dh.getPhuongThucThanhToan() %></strong>
                                        <span><%= hienThiThanhToan(dh.getTrangThaiThanhToan()) %></span>
                                    </div>
                                </td>

                                <td>
                                    <% if (dh.getAnhBill() != null && !dh.getAnhBill().trim().isEmpty()) { %>
                                        <button type="button"
                                                class="btn-detail"
                                                onclick="openBillModal('${pageContext.request.contextPath}/load-image?name=<%= dh.getAnhBill() %>')">
                                            Xem bill
                                        </button>
                                    <% } else { %>
                                        <span class="no-bill">Không có</span>
                                    <% } %>
                                </td>

                                <td>
                                    <span class="status-badge">
                                        <%= hienThiTrangThai(dh.getTrangThai()) %>
                                    </span>
                                </td>

                                <td><%= dh.getLyDoHuy() == null ? "" : dh.getLyDoHuy() %></td>

                                <td><%= dh.getNgayDat() %></td>

                                <td>
                                    <div class="action-group">
                                        <a href="${pageContext.request.contextPath}/staff/chi-tiet-don-hang?id=<%= dh.getIdDonHang() %>" class="btn-detail">
                                            Chi tiết
                                        </a>

                                        <form action="${pageContext.request.contextPath}/staff/cap-nhat-trang-thai-don-hang" method="post" class="status-form">
                                            <input type="hidden" name="idDonHang" value="<%= dh.getIdDonHang() %>">
                                            <select name="trangThaiMoi">
                                                <option value="CHO_XAC_NHAN" <%= "CHO_XAC_NHAN".equals(dh.getTrangThai()) ? "selected" : "" %>>Chờ xác nhận</option>
                                                <option value="DANG_CHUAN_BI" <%= "DANG_CHUAN_BI".equals(dh.getTrangThai()) ? "selected" : "" %>>Đang chuẩn bị</option>
                                                <option value="DANG_GIAO" <%= "DANG_GIAO".equals(dh.getTrangThai()) ? "selected" : "" %>>Đang giao</option>
                                                <option value="DA_GIAO" <%= "DA_GIAO".equals(dh.getTrangThai()) ? "selected" : "" %>>Đã giao</option>
                                                <option value="DA_HUY" <%= "DA_HUY".equals(dh.getTrangThai()) ? "selected" : "" %>>Đã hủy</option>
                                            </select>
                                            <button type="submit" class="btn-update">Cập nhật đơn</button>
                                        </form>

                                        <% if ("CHUYEN_KHOAN".equals(dh.getPhuongThucThanhToan()) || "THE".equals(dh.getPhuongThucThanhToan())) { %>
                                            <form action="${pageContext.request.contextPath}/staff/cap-nhat-thanh-toan" method="post" class="status-form">
                                                <input type="hidden" name="idDonHang" value="<%= dh.getIdDonHang() %>">
                                                <select name="trangThaiThanhToan">
                                                    <option value="CHO_XAC_NHAN" <%= "CHO_XAC_NHAN".equals(dh.getTrangThaiThanhToan()) ? "selected" : "" %>>Chờ xác nhận bill</option>
                                                    <option value="DA_THANH_TOAN" <%= "DA_THANH_TOAN".equals(dh.getTrangThaiThanhToan()) ? "selected" : "" %>>Đã thanh toán</option>
                                                    <option value="TU_CHOI" <%= "TU_CHOI".equals(dh.getTrangThaiThanhToan()) ? "selected" : "" %>>Từ chối</option>
                                                </select>
                                                <input type="text" name="ghiChuThanhToan" placeholder="Ghi chú bill">
                                                <button type="submit" class="btn-update">Xác nhận bill</button>
                                            </form>
                                        <% } %>
                                    </div>
                                </td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        <% } %>
    </form>
</div>
    </div>
</div>
        <div id="billModal" class="bill-modal" onclick="closeBillModal(event)">
    <div class="bill-modal-content">
        <button type="button" class="bill-close-btn" onclick="closeBillModal()">&times;</button>
        <img id="billModalImage" src="" alt="Ảnh bill">
    </div>
</div>
        <script>
    function openBillModal(imageUrl) {
        document.getElementById("billModalImage").src = imageUrl;
        document.getElementById("billModal").classList.add("show");
    }

    function closeBillModal(event) {
        if (!event || event.target.id === "billModal" || event.type === "click") {
            document.getElementById("billModal").classList.remove("show");
            document.getElementById("billModalImage").src = "";
        }
    }
</script>
<script>
document.getElementById("checkAllStaff")?.addEventListener("change", function () {
    const checked = this.checked;
    document.querySelectorAll(".staff-order-checkbox").forEach(function (cb) {
        cb.checked = checked;
    });
});

function selectAllPendingOrdersStaff() {
    document.querySelectorAll(".staff-order-checkbox").forEach(function (cb) {
        cb.checked = cb.dataset.status === "CHO_XAC_NHAN";
    });
}
</script>
</body>
</html>