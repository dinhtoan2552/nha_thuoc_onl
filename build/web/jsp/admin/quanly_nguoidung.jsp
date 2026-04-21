<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.NguoiDung" %>

<%
    List<NguoiDung> danhSachNguoiDung = (List<NguoiDung>) request.getAttribute("danhSachNguoiDung");
    if (danhSachNguoiDung == null) danhSachNguoiDung = new ArrayList<>();

    String keyword = (String) request.getAttribute("keyword");
    String vaiTro = (String) request.getAttribute("vaiTro");
    String trangThai = (String) request.getAttribute("trangThai");

    if (keyword == null) keyword = "";
    if (vaiTro == null) vaiTro = "";
    if (trangThai == null) trangThai = "";

    Integer tongNguoiDung = (Integer) request.getAttribute("tongNguoiDung");
    Integer dangHoatDong = (Integer) request.getAttribute("dangHoatDong");
    Integer biKhoa = (Integer) request.getAttribute("biKhoa");

    if (tongNguoiDung == null) tongNguoiDung = 0;
    if (dangHoatDong == null) dangHoatDong = 0;
    if (biKhoa == null) biKhoa = 0;

    String adminUserMessage = (String) session.getAttribute("adminUserMessage");
    if (adminUserMessage != null) {
        session.removeAttribute("adminUserMessage");
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản lý người dùng</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/dashboard.css?v=2">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/quanly-nguoidung.css?v=2">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
</head>
<body>
    <div class="admin-layout">
        <jsp:include page="/jsp/admin/admin_sidebar.jsp" />

        <div class="admin-main">
            <div class="admin-topbar">
                <div>
                    <h1>Quản lý người dùng</h1>
                    <p>Quản trị tài khoản khách hàng, tìm kiếm, lọc và khóa mở tài khoản khi cần.</p>
                </div>
                <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
            </div>

            <% if (adminUserMessage != null) { %>
                <div class="message-box"><%= adminUserMessage %></div>
            <% } %>

            <div class="stats-grid">
                <div class="stat-card total">
                    <div class="stat-icon"><i class="fa-solid fa-users"></i></div>
                    <div class="stat-content">
                        <span>Tổng người dùng</span>
                        <h2><%= tongNguoiDung %></h2>
                    </div>
                </div>

                <div class="stat-card active">
                    <div class="stat-icon"><i class="fa-solid fa-user-check"></i></div>
                    <div class="stat-content">
                        <span>Đang hoạt động</span>
                        <h2><%= dangHoatDong %></h2>
                    </div>
                </div>

                <div class="stat-card lock">
                    <div class="stat-icon"><i class="fa-solid fa-user-lock"></i></div>
                    <div class="stat-content">
                        <span>Bị khóa</span>
                        <h2><%= biKhoa %></h2>
                    </div>
                </div>
            </div>

            <div class="tool-grid">
                <div class="filter-card">
                    <h2>Bộ lọc người dùng</h2>

                    <form action="${pageContext.request.contextPath}/admin/nguoidung" method="get" class="filter-form">
                        <div class="form-group">
                            <label>Từ khóa</label>
                            <input type="text" name="keyword" value="<%= keyword %>" placeholder="Tên, email, số điện thoại...">
                        </div>

                        <div class="form-group">
                            <label>Vai trò</label>
                            <select name="vaiTro">
                                <option value="">Tất cả</option>
                                <option value="USER" <%= "USER".equals(vaiTro) ? "selected" : "" %>>USER</option>
                                <option value="STAFF" <%= "STAFF".equals(vaiTro) ? "selected" : "" %>>STAFF</option>
                                <option value="ADMIN" <%= "ADMIN".equals(vaiTro) ? "selected" : "" %>>ADMIN</option>
                            </select>
                        </div>

                        <div class="form-group">
                            <label>Trạng thái</label>
                            <select name="trangThai">
                                <option value="">Tất cả</option>
                                <option value="HOAT_DONG" <%= "HOAT_DONG".equals(trangThai) ? "selected" : "" %>>Hoạt động</option>
                                <option value="BI_KHOA" <%= "BI_KHOA".equals(trangThai) ? "selected" : "" %>>Bị khóa</option>
                            </select>
                        </div>

                        <div class="form-actions">
                            <button type="submit" class="btn-primary">Lọc</button>
                            <a href="${pageContext.request.contextPath}/admin/nguoidung" class="btn-outline">Đặt lại</a>
                        </div>
                    </form>
                </div>
            </div>

            <div class="table-card">
                <div class="table-header">
                    <h2>Danh sách người dùng</h2>
                    <span><%= danhSachNguoiDung.size() %> tài khoản</span>
                </div>

                <% if (danhSachNguoiDung.isEmpty()) { %>
                    <div class="empty-box">
                        <p>Không có người dùng nào phù hợp.</p>
                    </div>
                <% } else { %>
                    <div class="table-wrap">
                        <table class="user-table">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Người dùng</th>
                                    <th>SĐT</th>
                                    <th>Địa chỉ</th>
                                    <th>Vai trò</th>
                                    <th>Trạng thái</th>
                                    <th>Cập nhật</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% for (NguoiDung nd : danhSachNguoiDung) { %>
                                    <tr>
                                        <td>#<%= nd.getId() %></td>

                                        <td>
                                            <div class="user-cell">
                                                <strong><%= nd.getHoTen() == null ? "Chưa có tên" : nd.getHoTen() %></strong>
                                                <span><%= nd.getEmail() == null ? "" : nd.getEmail() %></span>
                                            </div>
                                        </td>

                                        <td><%= nd.getSoDienThoai() == null ? "" : nd.getSoDienThoai() %></td>
                                        <td><%= nd.getDiaChi() == null ? "" : nd.getDiaChi() %></td>

                                        <td>
                                            <span class="role-badge role-<%= nd.getVaiTro() == null ? "USER" : nd.getVaiTro() %>">
                                                <%= nd.getVaiTro() == null ? "USER" : nd.getVaiTro() %>
                                            </span>
                                        </td>

                                        <td>
                                            <span class="status-badge <%= "HOAT_DONG".equals(nd.getTrangThai()) ? "status-active" : "status-lock" %>">
                                                <%= "HOAT_DONG".equals(nd.getTrangThai()) ? "Hoạt động" : "Bị khóa" %>
                                            </span>
                                        </td>

                                        <td>
                                            <div class="action-stack">
                                                <form action="${pageContext.request.contextPath}/admin/nguoidung" method="post" class="edit-form">
                                                    <input type="hidden" name="action" value="sua">
                                                    <input type="hidden" name="id" value="<%= nd.getId() %>">

                                                    <input type="text" name="hoTen" value="<%= nd.getHoTen() == null ? "" : nd.getHoTen() %>" placeholder="Họ tên">
                                                    <input type="text" name="soDienThoai" value="<%= nd.getSoDienThoai() == null ? "" : nd.getSoDienThoai() %>" placeholder="Số điện thoại">
                                                    <input type="text" name="diaChi" value="<%= nd.getDiaChi() == null ? "" : nd.getDiaChi() %>" placeholder="Địa chỉ">

                                                    <select name="vaiTro">
                                                        <option value="ADMIN" <%= "ADMIN".equals(nd.getVaiTro()) ? "selected" : "" %>>ADMIN</option>
                                                        <option value="STAFF" <%= "STAFF".equals(nd.getVaiTro()) ? "selected" : "" %>>STAFF</option>
                                                        <option value="USER" <%= "USER".equals(nd.getVaiTro()) ? "selected" : "" %>>USER</option>
                                                    </select>

                                                    <select name="trangThai">
                                                        <option value="HOAT_DONG" <%= "HOAT_DONG".equals(nd.getTrangThai()) ? "selected" : "" %>>Hoạt động</option>
                                                        <option value="BI_KHOA" <%= "BI_KHOA".equals(nd.getTrangThai()) ? "selected" : "" %>>Bị khóa</option>
                                                    </select>

                                                    <button type="submit" class="btn-save">Lưu</button>
                                                </form>

                                                <% if (!"ADMIN".equalsIgnoreCase(nd.getVaiTro())) { %>
                                                    <form action="${pageContext.request.contextPath}/admin/nguoidung" method="post" class="toggle-form">
                                                        <input type="hidden" name="action" value="doiTrangThai">
                                                        <input type="hidden" name="id" value="<%= nd.getId() %>">
                                                        <input type="hidden" name="trangThaiMoi" value="<%= "HOAT_DONG".equals(nd.getTrangThai()) ? "BI_KHOA" : "HOAT_DONG" %>">

                                                        <button type="submit" class="<%= "HOAT_DONG".equals(nd.getTrangThai()) ? "btn-lock" : "btn-open" %>">
                                                            <%= "HOAT_DONG".equals(nd.getTrangThai()) ? "Khóa tài khoản" : "Mở khóa" %>
                                                        </button>
                                                    </form>
                                                <% } else { %>
                                                    <span class="admin-note">ADMIN không thể khóa</span>
                                                <% } %>
                                            </div>
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