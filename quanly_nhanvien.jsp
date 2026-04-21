<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.NguoiDung" %>

<%
    List<NguoiDung> danhSachNhanVien = (List<NguoiDung>) request.getAttribute("danhSachNhanVien");
    if (danhSachNhanVien == null) danhSachNhanVien = new ArrayList<>();

    String keyword = (String) request.getAttribute("keyword");
    String trangThai = (String) request.getAttribute("trangThai");

    if (keyword == null) keyword = "";
    if (trangThai == null) trangThai = "";

    Integer tongNhanVien = (Integer) request.getAttribute("tongNhanVien");
    Integer dangHoatDong = (Integer) request.getAttribute("dangHoatDong");
    Integer biKhoa = (Integer) request.getAttribute("biKhoa");

    if (tongNhanVien == null) tongNhanVien = 0;
    if (dangHoatDong == null) dangHoatDong = 0;
    if (biKhoa == null) biKhoa = 0;

    String adminStaffMessage = (String) session.getAttribute("adminStaffMessage");
    if (adminStaffMessage != null) {
        session.removeAttribute("adminStaffMessage");
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản lý nhân viên</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/dashboard.css?v=2">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/quanly-nhanvien.css?v=2">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
</head>
<body>
    <div class="admin-layout">
        <jsp:include page="/jsp/admin/admin_sidebar.jsp" />

        <div class="admin-main">
            <div class="admin-topbar">
                <div>
                    <h1>Quản lý nhân viên</h1>
                    <p>Quản trị tài khoản nhân viên nhà thuốc.</p>
                </div>
                <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
            </div>

            <% if (adminStaffMessage != null) { %>
                <div class="message-box"><%= adminStaffMessage %></div>
            <% } %>

            <div class="stats-grid">
                <div class="stat-card total">
                    <div class="stat-icon"><i class="fa-solid fa-user-group"></i></div>
                    <div class="stat-content">
                        <span>Tổng nhân viên</span>
                        <h2><%= tongNhanVien %></h2>
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
                    <h2>Bộ lọc nhân viên</h2>
                    <form action="${pageContext.request.contextPath}/admin/nhanvien" method="get" class="filter-form">
                        <div class="form-group">
                            <label>Từ khóa</label>
                            <input type="text" name="keyword" value="<%= keyword %>" placeholder="Tên, email, số điện thoại...">
                        </div>

                        <div class="form-group">
                            <label>Trạng thái</label>
                            <select name="trangThai">
                                <option value="">Tất cả</option>
                                <option value="HOAT_DONG" <%= "HOAT_DONG".equals(trangThai) ? "selected" : "" %>>Hoạt động</option>
                                <option value="KHOA" <%= "KHOA".equals(trangThai) ? "selected" : "" %>>Bị khóa</option>
                            </select>
                        </div>

                        <div class="form-actions">
                            <button type="submit" class="btn-primary">Lọc</button>
                            <a href="${pageContext.request.contextPath}/admin/nhanvien" class="btn-outline">Đặt lại</a>
                        </div>
                    </form>
                </div>

                <div class="add-card">
                    <h2>Thêm nhân viên</h2>
                    <form action="${pageContext.request.contextPath}/admin/nhanvien" method="post" class="add-form">
                        <input type="hidden" name="action" value="them">

                        <div class="form-group">
                            <label>Họ tên</label>
                            <input type="text" name="hoTen" required>
                        </div>

                        <div class="form-group">
                            <label>Email</label>
                            <input type="email" name="email" required>
                        </div>

                        <div class="form-group">
                            <label>Mật khẩu</label>
                            <input type="text" name="matKhau" required>
                        </div>

                        <div class="form-group">
                            <label>Số điện thoại</label>
                            <input type="text" name="soDienThoai">
                        </div>

                        <div class="form-group">
                            <label>Địa chỉ</label>
                            <input type="text" name="diaChi">
                        </div>

                        <div class="form-group">
                            <label>Trạng thái</label>
                            <select name="trangThai">
                                <option value="HOAT_DONG">Hoạt động</option>
                                <option value="KHOA">Bị khóa</option>
                            </select>
                        </div>

                        <button type="submit" class="btn-primary full-btn">Thêm nhân viên</button>
                    </form>
                </div>
            </div>

            <div class="table-card">
                <div class="table-header">
                    <h2>Danh sách nhân viên</h2>
                    <span><%= danhSachNhanVien.size() %> tài khoản</span>
                </div>

                <% if (danhSachNhanVien.isEmpty()) { %>
                    <div class="empty-box">
                        <p>Không có nhân viên nào phù hợp.</p>
                    </div>
                <% } else { %>
                    <div class="table-wrap">
                        <table class="staff-table">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Nhân viên</th>
                                    <th>SĐT</th>
                                    <th>Địa chỉ</th>
                                    <th>Trạng thái</th>
                                    <th>Cập nhật</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% for (NguoiDung nv : danhSachNhanVien) { %>
                                    <tr>
                                        <td>#<%= nv.getId() %></td>

                                        <td>
                                            <div class="user-cell">
                                                <strong><%= nv.getHoTen() == null ? "Chưa có tên" : nv.getHoTen() %></strong>
                                                <span><%= nv.getEmail() == null ? "" : nv.getEmail() %></span>
                                            </div>
                                        </td>

                                        <td><%= nv.getSoDienThoai() == null ? "" : nv.getSoDienThoai() %></td>
                                        <td><%= nv.getDiaChi() == null ? "" : nv.getDiaChi() %></td>

                                        <td>
                                            <span class="status-badge <%= "HOAT_DONG".equals(nv.getTrangThai()) ? "status-active" : "status-lock" %>">
                                                <%= "HOAT_DONG".equals(nv.getTrangThai()) ? "Hoạt động" : "Bị khóa" %>
                                            </span>
                                        </td>

                                        <td>
                                            <div class="action-stack">
                                                <form action="${pageContext.request.contextPath}/admin/nhanvien" method="post" class="edit-form">
                                                    <input type="hidden" name="action" value="sua">
                                                    <input type="hidden" name="id" value="<%= nv.getId() %>">

                                                    <input type="text" name="hoTen" value="<%= nv.getHoTen() == null ? "" : nv.getHoTen() %>" placeholder="Họ tên">
                                                    <input type="text" name="soDienThoai" value="<%= nv.getSoDienThoai() == null ? "" : nv.getSoDienThoai() %>" placeholder="Số điện thoại">
                                                    <input type="text" name="diaChi" value="<%= nv.getDiaChi() == null ? "" : nv.getDiaChi() %>" placeholder="Địa chỉ">

                                                    <select name="trangThai">
                                                        <option value="HOAT_DONG" <%= "HOAT_DONG".equals(nv.getTrangThai()) ? "selected" : "" %>>Hoạt động</option>
                                                        <option value="KHOA" <%= "KHOA".equals(nv.getTrangThai()) ? "selected" : "" %>>Bị khóa</option>
                                                    </select>

                                                    <button type="submit" class="btn-save">Lưu</button>
                                                </form>

                                                <form action="${pageContext.request.contextPath}/admin/nhanvien" method="post" class="toggle-form">
                                                    <input type="hidden" name="action" value="doiTrangThai">
                                                    <input type="hidden" name="id" value="<%= nv.getId() %>">
                                                    <input type="hidden" name="trangThaiMoi" value="<%= "HOAT_DONG".equals(nv.getTrangThai()) ? "KHOA" : "HOAT_DONG" %>">

                                                    <button type="submit" class="<%= "HOAT_DONG".equals(nv.getTrangThai()) ? "btn-lock" : "btn-open" %>">
                                                        <%= "HOAT_DONG".equals(nv.getTrangThai()) ? "Khóa tài khoản" : "Mở khóa" %>
                                                    </button>
                                                </form>
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