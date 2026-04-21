<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Thuoc" %>
<%@ page import="model.DanhMuc" %>
<%
    Thuoc t = (Thuoc) request.getAttribute("thuoc");
    List<DanhMuc> dsDanhMuc = (List<DanhMuc>) request.getAttribute("dsDanhMuc");

    String hinhAnh = "";
    try {
        if (t != null && t.getHinhAnh() != null) {
            hinhAnh = t.getHinhAnh();
        }
    } catch (Exception e) {
        hinhAnh = "";
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Sửa thuốc</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/form-thuoc.css">
</head>
<body>
    <div class="admin-layout">
        <jsp:include page="/jsp/admin/admin_sidebar.jsp" />

        <div class="admin-main">
            <div class="admin-topbar">
                <div class="topbar-left">
                    <h1>Sửa thuốc</h1>
                    <p>Cập nhật thông tin thuốc trong hệ thống</p>
                </div>
                <div class="topbar-right">
                    <a class="back-btn" href="${pageContext.request.contextPath}/admin/thuoc">Quay lại</a>
                </div>
            </div>

            <div class="form-card">
                <form class="thuoc-form" action="${pageContext.request.contextPath}/admin/thuoc/sua" method="post" enctype="multipart/form-data">

                    <input type="hidden" name="idThuoc" value="<%= t != null ? t.getIdThuoc() : 0 %>">
                    <input type="hidden" name="hinhAnhCu" value="<%= hinhAnh %>">

                    <div class="form-section">
                        <div class="section-header">
                            <h2>Thông tin cơ bản</h2>
                            <p>Chỉnh sửa các thông tin chính của thuốc</p>
                        </div>

                        <div class="image-upload-card">
                            <div class="image-upload-header">
                                <div>
                                    <h3>Hình ảnh thuốc</h3>
                                    <p>Có thể chọn ảnh mới từ máy tính, thư viện điện thoại hoặc chụp trực tiếp bằng camera</p>
                                </div>
                            </div>

                            <% if (hinhAnh != null && !hinhAnh.trim().isEmpty()) { %>
                                <div class="current-image-box">
                                    <span class="current-image-label">Ảnh hiện tại</span>
                                    <img src="${pageContext.request.contextPath}/assets/images/products/<%= hinhAnh %>" alt="Ảnh thuốc" class="current-product-image">
                                </div>
                            <% } %>

                            <label for="hinhAnhFile" class="custom-upload-box">
                                <span class="upload-icon">↑</span>
                                <span class="upload-title">Chọn hoặc chụp ảnh mới</span>
                                <span class="upload-desc">Hỗ trợ máy tính, điện thoại, thư viện ảnh và camera</span>
                            </label>

                            <input
                                type="file"
                                id="hinhAnhFile"
                                name="hinhAnhFile"
                                accept="image/*"
                                capture="environment"
                                class="hidden-file-input">
                        </div>

                        <div class="form-grid modern-grid">
    <div class="form-group">
        <label for="tenThuoc">Tên thuốc</label>
        <input type="text" id="tenThuoc" name="tenThuoc"
               value="<%= t != null ? t.getTenThuoc() : "" %>"
               placeholder="Nhập tên thuốc" required>
    </div>

    <div class="form-group">
        <label for="idDanhMuc">Danh mục</label>
        <select id="idDanhMuc" name="idDanhMuc" required>
            <option value="">-- Chọn danh mục --</option>
            <% if (dsDanhMuc != null) {
                for (DanhMuc dm : dsDanhMuc) { %>
                    <option value="<%= dm.getIdDanhMuc() %>"
                        <%= (t != null && t.getIdDanhMuc() == dm.getIdDanhMuc()) ? "selected" : "" %>>
                        <%= dm.getTenDanhMuc() %>
                    </option>
            <%  }
            } %>
        </select>
    </div>

    <div class="form-group">
        <label for="giaGoc">Giá nhập (¥)</label>
        <input type="number" step="1" min="0" id="giaGoc" name="giaGoc"
               value="<%= t != null ? t.getGiaGoc() : 0 %>"
               placeholder="Nhập giá nhập bằng Yên" required>
    </div>

    <div class="form-group">
        <label for="donGia">Giá bán (¥)</label>
        <input type="number" step="1" min="0" id="donGia" name="donGia"
               value="<%= t != null ? t.getDonGia() : 0 %>"
               placeholder="Nhập giá bán bằng Yên" required>
    </div>

    <div class="form-group">
        <label for="phiShip">Phí ship / 1 sản phẩm (¥)</label>
        <input type="number" step="1" min="0" id="phiShip" name="phiShip"
               value="<%= t != null ? t.getPhiShip() : 0 %>"
               placeholder="Nhập phí ship riêng cho sản phẩm" required>
    </div>

    <div class="form-group">
        <label for="soLuong">Số lượng</label>
        <input type="number" min="0" id="soLuong" name="soLuong"
               value="<%= t != null ? t.getSoLuong() : 0 %>"
               placeholder="Nhập số lượng" required>
    </div>

    <div class="form-group">
        <label for="nhaSanXuat">Nhà sản xuất</label>
        <input type="text" id="nhaSanXuat" name="nhaSanXuat"
               value="<%= t != null && t.getNhaSanXuat() != null ? t.getNhaSanXuat() : "" %>"
               placeholder="Nhập nhà sản xuất">
    </div>

    <div class="form-group">
        <label for="hanSuDung">Hạn sử dụng</label>
        <input type="date" id="hanSuDung" name="hanSuDung"
               value="<%= t != null && t.getHanSuDung() != null ? t.getHanSuDung() : "" %>">
    </div>

    <div class="form-group full-line">
        <label for="trangThai">Trạng thái</label>
        <select id="trangThai" name="trangThai">
            <option value="CON_HANG" <%= t != null && "CON_HANG".equals(t.getTrangThai()) ? "selected" : "" %>>Còn hàng</option>
            <option value="HET_HANG" <%= t != null && "HET_HANG".equals(t.getTrangThai()) ? "selected" : "" %>>Hết hàng</option>
            <option value="NGUNG_KINH_DOANH" <%= t != null && "NGUNG_KINH_DOANH".equals(t.getTrangThai()) ? "selected" : "" %>>Ngừng kinh doanh</option>
        </select>
    </div>
</div>
                    </div>

                    <div class="form-section">
                        <div class="section-header">
                            <h2>Thông tin chi tiết</h2>
                            <p>Cập nhật mô tả, công dụng và thành phần của thuốc</p>
                        </div>

                        <div class="form-group">
                            <label for="moTa">Mô tả</label>
                            <textarea id="moTa" name="moTa" rows="4" placeholder="Nhập mô tả sản phẩm"><%= t != null && t.getMoTa() != null ? t.getMoTa() : "" %></textarea>
                        </div>

                        <div class="form-group">
                            <label for="congDung">Công dụng</label>
                            <textarea id="congDung" name="congDung" rows="4" placeholder="Nhập công dụng"><%= t != null && t.getCongDung() != null ? t.getCongDung() : "" %></textarea>
                        </div>

                        <div class="form-group">
                            <label for="cachDung">Cách dùng</label>
                            <textarea id="cachDung" name="cachDung" rows="4" placeholder="Nhập cách dùng"><%= t != null && t.getCachDung() != null ? t.getCachDung() : "" %></textarea>
                        </div>

                        <div class="form-group">
                            <label for="thanhPhan">Thành phần</label>
                            <textarea id="thanhPhan" name="thanhPhan" rows="4" placeholder="Nhập thành phần"><%= t != null && t.getThanhPhan() != null ? t.getThanhPhan() : "" %></textarea>
                        </div>
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="save-btn">Cập nhật thuốc</button>
                        <a class="cancel-btn" href="${pageContext.request.contextPath}/admin/thuoc">Hủy</a>
                    </div>

                </form>
            </div>
        </div>
    </div>
</body>
</html>