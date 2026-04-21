<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.DanhMuc" %>
<%
    List<DanhMuc> dsDanhMuc = (List<DanhMuc>) request.getAttribute("dsDanhMuc");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Thêm thuốc</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/form-thuoc.css">
</head>
<body>
    <div class="admin-layout">
        <jsp:include page="/jsp/admin/admin_sidebar.jsp" />

        <div class="admin-main">
            <div class="admin-topbar">
                <div class="topbar-left">
                    <h1>Thêm thuốc</h1>
                    <p>Nhập thông tin thuốc mới vào hệ thống</p>
                </div>
                <div class="topbar-right">
                    <a class="back-btn" href="${pageContext.request.contextPath}/admin/thuoc">Quay lại</a>
                </div>
            </div>

            <div class="form-card">
                <form class="thuoc-form" action="${pageContext.request.contextPath}/admin/thuoc/them" method="post" enctype="multipart/form-data">
                    <div class="form-section">
                        <div class="section-header">
                            <h2>Thông tin cơ bản</h2>
                            <p>Nhập các thông tin chính của thuốc</p>
                        </div>

                        <div class="image-upload-card">
                            <div class="image-upload-header">
                                <div>
                                    <h3>Hình ảnh thuốc</h3>
                                    <p>Có thể chọn ảnh từ máy tính, thư viện điện thoại hoặc chụp trực tiếp bằng camera</p>
                                </div>
                            </div>

                            <label for="hinhAnhFile" class="custom-upload-box">
                                <span class="upload-icon">↑</span>
                                <span class="upload-title">Chọn hoặc chụp ảnh sản phẩm</span>
                                <span class="upload-desc">Hỗ trợ máy tính, điện thoại, thư viện ảnh và camera</span>
                            </label>

                            <input type="file"
                                   id="hinhAnhFile"
                                   name="hinhAnhFile"
                                   accept="image/*"
                                   capture="environment"
                                   class="hidden-file-input">
                        </div>

                        <div class="form-grid modern-grid">
    <div class="form-group">
        <label for="tenThuoc">Tên thuốc</label>
        <input type="text" id="tenThuoc" name="tenThuoc" placeholder="Nhập tên thuốc" required>
    </div>

    <div class="form-group">
        <label for="idDanhMuc">Danh mục</label>
        <select id="idDanhMuc" name="idDanhMuc" required>
            <option value="">-- Chọn danh mục --</option>
            <% if (dsDanhMuc != null) {
                for (DanhMuc dm : dsDanhMuc) { %>
                    <option value="<%= dm.getIdDanhMuc() %>"><%= dm.getTenDanhMuc() %></option>
            <%  }
            } %>
        </select>
    </div>

    <div class="form-group">
        <label for="giaGoc">Giá nhập (¥)</label>
        <input type="number" step="1" min="0" id="giaGoc" name="giaGoc"
               placeholder="Nhập giá nhập bằng Yên" required>
    </div>

    <div class="form-group">
        <label for="donGia">Giá bán (¥)</label>
        <input type="number" step="1" min="0" id="donGia" name="donGia"
               placeholder="Nhập giá bán bằng Yên" required>
    </div>

    <div class="form-group">
        <label for="phiShip">Phí ship / 1 sản phẩm (¥)</label>
        <input type="number" step="1" min="0" id="phiShip" name="phiShip"
               placeholder="Nhập phí ship riêng cho sản phẩm" required>
    </div>

    <div class="form-group">
        <label for="soLuong">Số lượng</label>
        <input type="number" min="0" id="soLuong" name="soLuong" placeholder="Nhập số lượng" required>
    </div>

    <div class="form-group">
        <label for="nhaSanXuat">Nhà sản xuất</label>
        <input type="text" id="nhaSanXuat" name="nhaSanXuat" placeholder="Nhập nhà sản xuất">
    </div>

    <div class="form-group">
        <label for="hanSuDung">Hạn sử dụng</label>
        <input type="date" id="hanSuDung" name="hanSuDung">
    </div>

    <div class="form-group full-line">
        <label for="trangThai">Trạng thái</label>
        <select id="trangThai" name="trangThai">
            <option value="CON_HANG">Còn hàng</option>
            <option value="HET_HANG">Hết hàng</option>
            <option value="NGUNG_KINH_DOANH">Ngừng kinh doanh</option>
        </select>
    </div>
</div>
                    </div>

                    <div class="form-section">
                        <div class="section-header">
                            <h2>Thông tin chi tiết</h2>
                            <p>Bổ sung mô tả và hướng dẫn sử dụng thuốc</p>
                        </div>

                        <div class="form-group">
                            <label for="moTa">Mô tả</label>
                            <textarea id="moTa" name="moTa" rows="4" placeholder="Nhập mô tả sản phẩm"></textarea>
                        </div>

                        <div class="form-group">
                            <label for="congDung">Công dụng</label>
                            <textarea id="congDung" name="congDung" rows="4" placeholder="Nhập công dụng của thuốc"></textarea>
                        </div>

                        <div class="form-group">
                            <label for="cachDung">Cách dùng</label>
                            <textarea id="cachDung" name="cachDung" rows="4" placeholder="Nhập cách dùng"></textarea>
                        </div>

                        <div class="form-group">
                            <label for="thanhPhan">Thành phần</label>
                            <textarea id="thanhPhan" name="thanhPhan" rows="4" placeholder="Nhập thành phần thuốc"></textarea>
                        </div>
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="save-btn">Lưu thuốc</button>
                        <a class="cancel-btn" href="${pageContext.request.contextPath}/admin/thuoc">Hủy</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</body>
</html>