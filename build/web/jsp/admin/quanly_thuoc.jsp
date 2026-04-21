<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Thuoc" %>
<%@ page import="utils.CurrencyUtil" %>
<%
    List<Thuoc> danhSachThuoc = (List<Thuoc>) request.getAttribute("danhSachThuoc");
    if (danhSachThuoc == null) danhSachThuoc = new java.util.ArrayList<>();

    String keyword = (String) request.getAttribute("keyword");
    if (keyword == null) keyword = "";

    String trangThaiKho = (String) request.getAttribute("trangThaiKho");
    if (trangThaiKho == null) trangThaiKho = "";
%>
<%
    Integer currentPageObj = (Integer) request.getAttribute("currentPage");
    Integer totalPagesObj = (Integer) request.getAttribute("totalPages");
    Integer totalItemsObj = (Integer) request.getAttribute("totalItems");

    int currentPage = currentPageObj == null ? 1 : currentPageObj;
    int totalPages = totalPagesObj == null ? 1 : totalPagesObj;
    int totalItems = totalItemsObj == null ? 0 : totalItemsObj;
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý thuốc</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/quanly-thuoc.css?v=20260406">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
</head>
<body>
    <div class="admin-layout">
        <jsp:include page="/jsp/admin/admin_sidebar.jsp" />

        <div class="admin-main">
            <div class="admin-topbar">
                <div>
                    <h1>Quản lý thuốc</h1>
                    <p>Danh sách toàn bộ thuốc trong hệ thống</p>
                </div>

                <div class="topbar-actions">
                    <a class="add-btn" href="${pageContext.request.contextPath}/admin/thuoc/them">
                        <span class="btn-icon">+</span>
                        <span>Thêm thuốc</span>
                    </a>
                    <a class="logout-btn" href="${pageContext.request.contextPath}/logout">
                        Đăng xuất
                    </a>
                </div>
            </div>

            <div class="content-card">
                <form class="thuoc-filter-form" method="get" action="${pageContext.request.contextPath}/admin/thuoc">
    <div class="search-input-wrap">
        <i class="fa-solid fa-magnifying-glass"></i>
        <input
            type="text"
            name="keyword"
            value="<%= keyword %>"
            placeholder="Tìm tên thuốc hoặc mã thuốc..."
            autocomplete="off">
    </div>
            <div class="pagination-wrap">
    <div class="pagination-info">
        Tổng <strong><%= totalItems %></strong> thuốc
    </div>

    <% if (totalPages > 1) { %>
        <div class="pagination">
            <% if (currentPage > 1) { %>
                <a class="page-btn"
                   href="${pageContext.request.contextPath}/admin/thuoc?page=<%= currentPage - 1 %>&keyword=<%= java.net.URLEncoder.encode(keyword, "UTF-8") %>&trangThaiKho=<%= java.net.URLEncoder.encode(trangThaiKho, "UTF-8") %>">
                    ‹ Trước
                </a>
            <% } else { %>
                <span class="page-btn disabled">‹ Trước</span>
            <% } %>

            <% for (int i = 1; i <= totalPages; i++) { %>
                <% if (i == currentPage) { %>
                    <span class="page-btn active"><%= i %></span>
                <% } else { %>
                    <a class="page-btn"
                       href="${pageContext.request.contextPath}/admin/thuoc?page=<%= i %>&keyword=<%= java.net.URLEncoder.encode(keyword, "UTF-8") %>&trangThaiKho=<%= java.net.URLEncoder.encode(trangThaiKho, "UTF-8") %>">
                        <%= i %>
                    </a>
                <% } %>
            <% } %>

            <% if (currentPage < totalPages) { %>
                <a class="page-btn"
                   href="${pageContext.request.contextPath}/admin/thuoc?page=<%= currentPage + 1 %>&keyword=<%= java.net.URLEncoder.encode(keyword, "UTF-8") %>&trangThaiKho=<%= java.net.URLEncoder.encode(trangThaiKho, "UTF-8") %>">
                    Sau ›
                </a>
            <% } else { %>
                <span class="page-btn disabled">Sau ›</span>
            <% } %>
        </div>
    <% } %>
</div>

    <select name="trangThaiKho" class="stock-filter-select">
        <option value="">Tất cả trạng thái kho</option>
        <option value="CON_HANG" <%= "CON_HANG".equals(trangThaiKho) ? "selected" : "" %>>Còn hàng</option>
        <option value="SAP_HET" <%= "SAP_HET".equals(trangThaiKho) ? "selected" : "" %>>Sắp hết</option>
        <option value="HET_HANG" <%= "HET_HANG".equals(trangThaiKho) ? "selected" : "" %>>Hết hàng</option>
    </select>

    <button type="submit" class="search-btn">
        <i class="fa-solid fa-magnifying-glass"></i>
        <span>Tìm kiếm</span>
    </button>

    <a href="${pageContext.request.contextPath}/admin/thuoc" class="reset-filter-btn">
        <i class="fa-solid fa-rotate-left"></i>
        <span>Xóa lọc</span>
    </a>
</form>

                <div class="table-box">
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Tên thuốc</th>
                                <th>Đơn giá</th>
                                <th>Số lượng</th>
                                <th>Hình ảnh</th>
                                <th>Nhà sản xuất</th>
                                <th>Trạng thái</th>
                                <th>Hành động</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% if (danhSachThuoc.isEmpty()) { %>
                                <tr>
                                    <td colspan="8" class="empty-row">Không tìm thấy thuốc phù hợp</td>
                                </tr>
                            <% } else { %>
                                <% for (Thuoc t : danhSachThuoc) { %>
                                    <tr>
                                        <td><%= t.getIdThuoc() %></td>
                                        <td class="thuoc-name"><%= t.getTenThuoc() %></td>
                                        <td class="price-cell"><%= CurrencyUtil.yen(t.getDonGia()) %></td>
                                        <td><%= t.getSoLuong() %></td>
                                        <td>
                                            <% if (t.getHinhAnh() != null && !t.getHinhAnh().trim().isEmpty()) { %>
                                                <img src="${pageContext.request.contextPath}/image?name=<%= t.getHinhAnh() %>"
                                                     alt="Ảnh thuốc"
                                                     class="product-img">
                                            <% } else { %>
                                                <span class="no-image">Không có ảnh</span>
                                            <% } %>
                                        </td>
                                        <td><%= t.getNhaSanXuat() != null ? t.getNhaSanXuat() : "" %></td>
                                        <td>
                                            <span class="status-badge <%= "CON_HANG".equals(t.getTrangThai()) ? "status-available" : ("HET_HANG".equals(t.getTrangThai()) ? "status-out" : "status-stop") %>">
                                                <%= t.getTrangThai() != null ? t.getTrangThai().replace("_", " ") : "" %>
                                            </span>
                                        </td>
                                        <td>
                                            <div class="action-group">
                                                <a class="edit-btn" href="${pageContext.request.contextPath}/admin/thuoc/sua?id=<%= t.getIdThuoc() %>">
                                                    Sửa
                                                </a>
                                                <a class="delete-btn"
                                                   href="${pageContext.request.contextPath}/admin/thuoc/xoa?id=<%= t.getIdThuoc() %>"
                                                   onclick="return confirm('Bạn có chắc muốn xóa thuốc này không?');">
                                                    Xóa
                                                </a>
                                            </div>
                                        </td>
                                    </tr>
                                <% } %>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</body>
</html>