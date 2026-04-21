<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Thuoc" %>
<%@ page import="utils.CurrencyUtil" %>
<%
    List<Thuoc> listThuoc = (List<Thuoc>) request.getAttribute("listThuoc");
    if (listThuoc == null) listThuoc = new ArrayList<>();

    String keyword = (String) request.getAttribute("keyword");
    String trangThaiKho = (String) request.getAttribute("trangThaiKho");
    if (keyword == null) keyword = "";
    if (trangThaiKho == null) trangThaiKho = "";

    Integer tongThuoc = (Integer) request.getAttribute("tongThuoc");
    Integer sapHet = (Integer) request.getAttribute("sapHet");
    Integer hetHang = (Integer) request.getAttribute("hetHang");

    if (tongThuoc == null) tongThuoc = 0;
    if (sapHet == null) sapHet = 0;
    if (hetHang == null) hetHang = 0;

    String staffKhoMessage = (String) session.getAttribute("staffKhoMessage");
    if (staffKhoMessage != null) {
        session.removeAttribute("staffKhoMessage");
    }
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Kiểm tra kho</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/staff/kho.css?v=3">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
</head>
<body>
<div class="staff-layout">
    <jsp:include page="/jsp/staff/staff_sidebar.jsp" />

    <div class="staff-main">
        <div class="staff-topbar">
            <div>
                <h1>Kiểm tra kho</h1>
                <p>Theo dõi tồn kho, tìm thuốc sắp hết và nhập thêm số lượng nhanh.</p>
            </div>

            <a href="${pageContext.request.contextPath}/logout" class="logout-btn">
                <i class="fa-solid fa-right-from-bracket"></i>
                <span>Đăng xuất</span>
            </a>
        </div>

        <% if (staffKhoMessage != null) { %>
            <div class="message-box"><%= staffKhoMessage %></div>
        <% } %>

        <div class="stats-grid">
            <div class="stat-card total">
                <div class="stat-icon"><i class="fa-solid fa-capsules"></i></div>
                <div class="stat-content">
                    <span>Tổng sản phẩm</span>
                    <h2><%= tongThuoc %></h2>
                </div>
            </div>

            <div class="stat-card warning">
                <div class="stat-icon"><i class="fa-solid fa-triangle-exclamation"></i></div>
                <div class="stat-content">
                    <span>Sắp hết hàng</span>
                    <h2><%= sapHet %></h2>
                </div>
            </div>

            <div class="stat-card danger">
                <div class="stat-icon"><i class="fa-solid fa-circle-xmark"></i></div>
                <div class="stat-content">
                    <span>Hết hàng</span>
                    <h2><%= hetHang %></h2>
                </div>
            </div>
        </div>

        <div class="filter-card">
            <form action="${pageContext.request.contextPath}/staff/kho" method="get" class="filter-form">
                <div class="filter-group search-group">
                    <label>Tìm kiếm</label>
                    <input type="text" name="keyword" value="<%= keyword %>" placeholder="Tên thuốc hoặc mã thuốc...">
                </div>

                <div class="filter-group">
                    <label>Trạng thái kho</label>
                    <select name="trangThaiKho">
                        <option value="">Tất cả</option>
                        <option value="CON_HANG" <%= "CON_HANG".equals(trangThaiKho) ? "selected" : "" %>>Còn hàng</option>
                        <option value="SAP_HET" <%= "SAP_HET".equals(trangThaiKho) ? "selected" : "" %>>Sắp hết</option>
                        <option value="HET_HANG" <%= "HET_HANG".equals(trangThaiKho) ? "selected" : "" %>>Hết hàng</option>
                    </select>
                </div>

                <div class="filter-actions">
                    <button type="submit" class="btn-filter">Lọc</button>
                    <a href="${pageContext.request.contextPath}/staff/kho" class="btn-reset">Đặt lại</a>
                </div>
            </form>
        </div>

        <div class="table-card">
            <div class="table-header">
                <h2>Danh sách thuốc trong kho</h2>
                <span><%= listThuoc.size() %> sản phẩm</span>
            </div>

            <% if (listThuoc.isEmpty()) { %>
                <div class="empty-box">
                    <p>Không có sản phẩm nào phù hợp.</p>
                </div>
            <% } else { %>
                <div class="table-wrap">
                    <table class="stock-table">
                        <thead>
                            <tr>
                                <th>Mã</th>
                                <th>Sản phẩm</th>
                                <th>Giá bán</th>
                                <th>Số lượng</th>
                                <th>Trạng thái</th>
                                <th>Nhập thêm</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Thuoc t : listThuoc) {
                                String hinhAnh = t.getHinhAnh();
                                if (hinhAnh == null || hinhAnh.trim().isEmpty() || "null".equalsIgnoreCase(hinhAnh.trim())) {
                                    hinhAnh = "no-image.png";
                                }

                                String trangThaiText = "Còn hàng";
                                String trangThaiClass = "status-badge status-ok";

                                if (t.getSoLuong() <= 0) {
                                    trangThaiText = "Hết hàng";
                                    trangThaiClass = "status-badge status-out";
                                } else if (t.getSoLuong() <= 10) {
                                    trangThaiText = "Sắp hết";
                                    trangThaiClass = "status-badge status-low";
                                }
                            %>
                            <tr>
                                <td class="col-id">#<%= t.getIdThuoc() %></td>

                                <td>
                                    <div class="product-cell">
                                        <div class="product-thumb">
                                            <img src="<%= request.getContextPath() %>/load-image?name=<%= hinhAnh %>" alt="Sản phẩm">
                                        </div>
                                        <div class="product-info">
                                            <strong><%= t.getTenThuoc() == null ? "Chưa có tên thuốc" : t.getTenThuoc() %></strong>
                                            <span>Thuốc trong kho</span>
                                        </div>
                                    </div>
                                </td>

                                <td class="money"><%= CurrencyUtil.yen(t.getDonGia()) %></td>

                                <td>
                                    <span class="quantity <%= t.getSoLuong() <= 10 ? "low-qty" : "" %>">
                                        <%= t.getSoLuong() %>
                                    </span>
                                </td>

                                <td>
                                    <span class="<%= trangThaiClass %>"><%= trangThaiText %></span>
                                </td>

                                <td>
                                    <form action="${pageContext.request.contextPath}/staff/cap-nhat-kho" method="post" class="stock-form">
                                        <input type="hidden" name="idThuoc" value="<%= t.getIdThuoc() %>">
                                        <input type="number" name="soLuongNhap" min="1" placeholder="SL" required>
                                        <button type="submit" class="btn-update-stock">Nhập thêm</button>
                                    </form>
                                </td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            <% } %>
        </div>
    </div>
</div>
</body>
</html>