<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.DanhMuc" %>

<%
    List<DanhMuc> danhSachDanhMuc = (List<DanhMuc>) request.getAttribute("danhSachDanhMuc");
    if (danhSachDanhMuc == null) danhSachDanhMuc = new java.util.ArrayList<>();
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý danh mục</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/quanly-danhmuc.css">
</head>
<body>
    <div class="admin-layout">
        <jsp:include page="/jsp/admin/admin_sidebar.jsp" />

        <div class="admin-main">
            <div class="admin-topbar">
                <div>
                    <h1>Quản lý danh mục</h1>
                    <p>Danh sách toàn bộ danh mục trong hệ thống</p>
                </div>

                <div class="topbar-actions">
                    <a class="add-btn" href="${pageContext.request.contextPath}/admin/danhmuc/them">+ Thêm danh mục</a>
                    <a class="logout-btn" href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
                </div>
            </div>

            <div class="content-card">
                <div class="table-box">
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Tên danh mục</th>
                                <th>Mô tả</th>
                                <th>Hành động</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% if (danhSachDanhMuc.isEmpty()) { %>
                                <tr>
                                    <td colspan="4" class="empty-row">Chưa có dữ liệu danh mục</td>
                                </tr>
                            <% } else { %>
                                <% for (DanhMuc dm : danhSachDanhMuc) { %>
                                    <tr>
                                        <td><%= dm.getIdDanhMuc() %></td>
                                        <td class="category-name"><%= dm.getTenDanhMuc() %></td>
                                        <td><%= dm.getMoTa() != null ? dm.getMoTa() : "" %></td>
                                        <td>
                                            <div class="action-group">
                                                <a class="edit-btn" href="${pageContext.request.contextPath}/admin/danhmuc/sua?id=<%= dm.getIdDanhMuc() %>">Sửa</a>
                                                <a class="delete-btn"
                                                   href="${pageContext.request.contextPath}/admin/danhmuc/xoa?id=<%= dm.getIdDanhMuc() %>"
                                                   onclick="return confirm('Bạn có chắc muốn xóa danh mục này không?');">
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