<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Thêm danh mục</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/form-danhmuc.css">
</head>
<body>
    <div class="admin-layout">
        <jsp:include page="/jsp/admin/admin_sidebar.jsp" />

        <div class="admin-main">
            <div class="admin-topbar">
                <div class="topbar-left">
                    <h1>Thêm danh mục</h1>
                    <p>Nhập thông tin danh mục mới</p>
                </div>
                <div class="topbar-right">
                    <a class="back-btn" href="${pageContext.request.contextPath}/admin/danhmuc">Quay lại</a>
                </div>
            </div>

            <div class="form-card">
                <form class="danhmuc-form" action="${pageContext.request.contextPath}/admin/danhmuc/them" method="post">
                    <div class="form-section">
                        <div class="section-header">
                            <h2>Thông tin danh mục</h2>
                            <p>Nhập thông tin cơ bản của danh mục</p>
                        </div>

                        <div class="form-group">
                            <label for="tenDanhMuc">Tên danh mục</label>
                            <input type="text" id="tenDanhMuc" name="tenDanhMuc" placeholder="Nhập tên danh mục" required>
                        </div>

                        <div class="form-group">
                            <label for="moTa">Mô tả</label>
                            <textarea id="moTa" name="moTa" rows="5" placeholder="Nhập mô tả danh mục"></textarea>
                        </div>
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="save-btn">Lưu danh mục</button>
                        <a class="cancel-btn" href="${pageContext.request.contextPath}/admin/danhmuc">Hủy</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</body>
</html>