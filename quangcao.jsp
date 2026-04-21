<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.QuangCao" %>

<%
    List<QuangCao> dsQuangCao = (List<QuangCao>) request.getAttribute("dsQuangCao");
    if (dsQuangCao == null) dsQuangCao = new ArrayList<>();
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản lý quảng cáo</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/dashboard.css?v=30">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/quangcao.css?v=30">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
</head>
<body>
<div class="admin-layout">
    <jsp:include page="/jsp/admin/admin_sidebar.jsp" />

    <div class="admin-main qc-page">
        <div class="qc-header-card">
            <div>
                <h1>Quản lý quảng cáo</h1>
                <p>Thêm, sửa, bật tắt và xóa banner quảng cáo hiển thị ngoài trang người dùng.</p>
            </div>
        </div>

        <div class="qc-form-card">
            <div class="qc-card-title">
                <i class="fa-solid fa-image"></i>
                <span>Thêm quảng cáo mới</span>
            </div>

            <form action="${pageContext.request.contextPath}/admin/quangcao" method="post" enctype="multipart/form-data" class="qc-form">
                <input type="hidden" name="action" value="them">

                <div class="qc-form-grid">
                    <div class="qc-form-group">
                        <label>Tiêu đề quảng cáo</label>
                        <input type="text" name="tieuDe" placeholder="Nhập tiêu đề quảng cáo">
                    </div>

                    <div class="qc-form-group">
                        <label>Link khi bấm ảnh</label>
                        <input type="text" name="linkUrl" placeholder="Ví dụ: /chi-tiet-thuoc?id=1">
                    </div>

                    <div class="qc-form-group">
                        <label>Thứ tự hiển thị</label>
                        <input type="number" name="thuTu" value="0">
                    </div>

                    <div class="qc-form-group">
                        <label>Trạng thái</label>
                        <select name="trangThai">
                            <option value="1">Đang bật</option>
                            <option value="0">Đang tắt</option>
                        </select>
                    </div>

                    <div class="qc-form-group qc-form-group-full">
                        <label>Ảnh quảng cáo</label>
                        <input type="file" name="hinhAnh" accept="image/*" required>
                    </div>
                </div>

                <button type="submit" class="qc-submit-btn">
                    <i class="fa-solid fa-plus"></i>
                    <span>Thêm quảng cáo</span>
                </button>
            </form>
        </div>

        <div class="qc-list-section">
            <div class="qc-card-title">
                <i class="fa-solid fa-rectangle-ad"></i>
                <span>Danh sách quảng cáo</span>
            </div>

            <% if (dsQuangCao.isEmpty()) { %>
                <div class="qc-empty-box">
                    <i class="fa-regular fa-image"></i>
                    <p>Chưa có quảng cáo nào trong hệ thống.</p>
                </div>
            <% } else { %>
                <div class="qc-list">
                    <% for (QuangCao qc : dsQuangCao) { %>
                        <div class="qc-item">
                            <div class="qc-preview">
                                <img src="${pageContext.request.contextPath}/load-image?name=<%= qc.getHinhAnh() %>" alt="Quảng cáo">
                            </div>

                            <div class="qc-content">
                                <form action="${pageContext.request.contextPath}/admin/quangcao" method="post" enctype="multipart/form-data" class="qc-edit-form">
                                    <input type="hidden" name="action" value="sua">
                                    <input type="hidden" name="idQuangCao" value="<%= qc.getIdQuangCao() %>">

                                    <div class="qc-row">
                                        <div class="qc-form-group">
                                            <label>Tiêu đề</label>
                                            <input type="text" name="tieuDe"
                                                   value="<%= qc.getTieuDe() == null ? "" : qc.getTieuDe() %>"
                                                   placeholder="Tiêu đề quảng cáo">
                                        </div>

                                        <div class="qc-form-group">
                                            <label>Link</label>
                                            <input type="text" name="linkUrl"
                                                   value="<%= qc.getLinkUrl() == null ? "" : qc.getLinkUrl() %>"
                                                   placeholder="Đường dẫn khi bấm">
                                        </div>
                                    </div>

                                    <div class="qc-row">
                                        <div class="qc-form-group">
                                            <label>Thứ tự</label>
                                            <input type="number" name="thuTu" value="<%= qc.getThuTu() %>">
                                        </div>

                                        <div class="qc-form-group">
                                            <label>Trạng thái</label>
                                            <select name="trangThai">
                                                <option value="1" <%= qc.isTrangThai() ? "selected" : "" %>>Đang bật</option>
                                                <option value="0" <%= !qc.isTrangThai() ? "selected" : "" %>>Đang tắt</option>
                                            </select>
                                        </div>
                                    </div>

                                    <div class="qc-form-group">
                                        <label>Đổi ảnh nếu muốn</label>
                                        <input type="file" name="hinhAnh" accept="image/*">
                                    </div>

                                    <div class="qc-actions">
                                        <button type="submit" class="btn-save">
                                            <i class="fa-solid fa-floppy-disk"></i>
                                            <span>Lưu thay đổi</span>
                                        </button>
                                    </div>
                                </form>

                                <form action="${pageContext.request.contextPath}/admin/quangcao" method="post"
                                      onsubmit="return confirm('Xóa quảng cáo này?');"
                                      class="qc-delete-form">
                                    <input type="hidden" name="action" value="xoa">
                                    <input type="hidden" name="idQuangCao" value="<%= qc.getIdQuangCao() %>">

                                    <button type="submit" class="btn-delete">
                                        <i class="fa-solid fa-trash"></i>
                                        <span>Xóa</span>
                                    </button>
                                </form>
                            </div>
                        </div>
                    <% } %>
                </div>
            <% } %>
        </div>
    </div>
</div>
</body>
</html>