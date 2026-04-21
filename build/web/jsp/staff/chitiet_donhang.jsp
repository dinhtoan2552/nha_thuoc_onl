<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.DonHang" %>
<%@ page import="model.ChiTietDonHang" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="utils.CurrencyUtil" %>
<%!
    public String hienThiTrangThai(String trangThai) {
        if (trangThai == null) return "Chưa cập nhật";

        switch (trangThai) {
            case "CHO_XAC_NHAN":
                return "Chờ xác nhận";
            case "DA_XAC_NHAN":
                return "Đã xác nhận";
            case "DANG_GIAO":
                return "Đang giao";
            case "HOAN_THANH":
                return "Hoàn thành";
            case "DA_HUY":
                return "Đã hủy";
            default:
                return trangThai;
        }
    }
%>

<%
    DonHang donHang = (DonHang) request.getAttribute("donHang");
    List<ChiTietDonHang> dsChiTiet = (donHang != null && donHang.getDanhSachChiTiet() != null)
            ? donHang.getDanhSachChiTiet()
            : new ArrayList<>();
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chi tiết đơn hàng</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/staff/chitiet-donhang.css?v=10002">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/staff/donhang.css?v=10002">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/staff/hoa-don-modal.css?v=10002">
</head>
<body>
<div class="staff-layout">
    <jsp:include page="/jsp/staff/staff_sidebar.jsp" />

    <div class="staff-main">
        <div class="staff-topbar">
            <div>
                <h1>Chi tiết đơn hàng</h1>
                <p>Kiểm tra đầy đủ thông tin khách hàng và sản phẩm trong đơn.</p>
            </div>

            <a href="${pageContext.request.contextPath}/staff/donhang" class="btn-detail">Quay lại</a>
        </div>

        <% if (donHang == null) { %>
            <div class="empty-box">
                <p>Không tìm thấy đơn hàng.</p>
            </div>
        <% } else { %>
            <div class="detail-card">
                <div class="detail-top">
                    <div>
                        <h2>Đơn hàng #<%= donHang.getIdDonHang() %></h2>
                        <p>Ngày đặt: <%= donHang.getNgayDat() %></p>
                    </div>
                    <span class="status-badge"><%= hienThiTrangThai(donHang.getTrangThai()) %></span>
                </div>

                <div class="detail-grid">
                    <div class="detail-item">
                        <span class="label">Khách hàng</span>
                        <strong><%= donHang.getTenKhachHang() == null ? "Khách hàng" : donHang.getTenKhachHang() %></strong>
                    </div>

                    <div class="detail-item">
                        <span class="label">Email</span>
                        <strong><%= donHang.getEmailKhachHang() == null ? "" : donHang.getEmailKhachHang() %></strong>
                    </div>

                    <div class="detail-item">
                        <span class="label">SĐT khách</span>
                        <strong><%= donHang.getSoDienThoaiKhach() == null ? "" : donHang.getSoDienThoaiKhach() %></strong>
                    </div>

                    <div class="detail-item">
                        <span class="label">Tổng tiền</span>
                        <strong class="money"><%= CurrencyUtil.yen(donHang.getTongTien()) %></strong>
                    </div>

                    <div class="detail-item full">
                        <span class="label">Địa chỉ nhận</span>
                        <strong><%= donHang.getDiaChiNhan() == null ? "" : donHang.getDiaChiNhan() %></strong>
                    </div>

                    <div class="detail-item full">
                        <span class="label">Ghi chú</span>
                        <strong><%= (donHang.getGhiChu() == null || donHang.getGhiChu().trim().isEmpty()) ? "Không có" : donHang.getGhiChu() %></strong>
                    </div>
                </div>
            </div>

            <div class="table-card">
                <div class="table-header">
                    <h2>Sản phẩm trong đơn</h2>
                    <span><%= dsChiTiet.size() %> sản phẩm</span>
                </div>

                <div class="detail-product-list">
                    <% for (ChiTietDonHang ct : dsChiTiet) { %>
                        <div class="detail-product-item">
                            <div class="product-image detail-product-thumb">
                                <img class="detail-product-img"
                                     src="${pageContext.request.contextPath}/load-image?name=<%= ct.getHinhAnh() %>"
                                     alt="<%= ct.getTenThuoc() %>">
                            </div>

                            <div class="product-info detail-product-info">
                                <h3><%= ct.getTenThuoc() %></h3>
                                <p>Số lượng: <strong><%= ct.getSoLuong() %></strong></p>
                                <p>Đơn giá: <strong><%= CurrencyUtil.yen(ct.getDonGia()) %></strong></p>
                            </div>

                            <div class="product-total">
                                <span>Thành tiền</span>
                                <strong><%= CurrencyUtil.yen(ct.getThanhTien()) %></strong>
                            </div>

                            <div class="product-invoice-box">
                                <p class="invoice-box-title">Hóa đơn sản phẩm</p>

                                <% if (ct.getAnhHoaDonSP() != null && !ct.getAnhHoaDonSP().trim().isEmpty()) { %>
                                    <div class="invoice-preview-wrap">
                                        <img class="invoice-preview-image"
                                             src="${pageContext.request.contextPath}/load-image?name=<%= ct.getAnhHoaDonSP() %>"
                                             alt="Hóa đơn sản phẩm"
                                             onclick="openBillModal('${pageContext.request.contextPath}/load-image?name=<%= ct.getAnhHoaDonSP() %>')">
                                    </div>
                                <% } else { %>
                                    <p class="invoice-empty-text">Chưa có ảnh hóa đơn</p>
                                <% } %>

                                <form action="${pageContext.request.contextPath}/staff/cap-nhat-hoa-don-san-pham"
                                      method="post"
                                      enctype="multipart/form-data"
                                      class="invoice-upload-form">
                                    <input type="hidden" name="idChiTiet" value="<%= ct.getIdChiTiet() %>">
                                    <input type="hidden" name="idDonHang" value="<%= donHang.getIdDonHang() %>">

                                    <input type="file"
                                           id="anhHoaDonSP_<%= ct.getIdChiTiet() %>"
                                           name="anhHoaDonSP"
                                           accept="image/*"
                                           class="invoice-file-input"
                                           onchange="updateFileName(this, 'fileName_<%= ct.getIdChiTiet() %>')">

                                    <label for="anhHoaDonSP_<%= ct.getIdChiTiet() %>" class="invoice-file-label" title="Chọn ảnh hóa đơn">
                                        <i class="fa-regular fa-image"></i>
                                        <span>Tải ảnh</span>
                                    </label>

                                    <div id="fileName_<%= ct.getIdChiTiet() %>" class="selected-file-name">
                                        Chưa chọn ảnh
                                    </div>

                                    <input type="text"
                                           name="ghiChuHoaDonSP"
                                           value="<%= ct.getGhiChuHoaDonSP() == null ? "" : ct.getGhiChuHoaDonSP() %>"
                                           placeholder="Ghi chú hóa đơn sản phẩm">

                                    <button type="submit">
                                        Lưu hóa đơn SP
                                    </button>
                                </form>
                            </div>
                        </div>
                    <% } %>
                </div>
            </div>
        <% } %>
    </div>
</div>

<div id="billModal" class="bill-modal" onclick="closeBillModal(event)">
    <div class="bill-modal-content">
        <button type="button" class="bill-close-btn" onclick="closeBillModal()">&times;</button>
        <img id="billModalImage" src="" alt="Ảnh hóa đơn">
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

    function updateFileName(input, targetId) {
        const target = document.getElementById(targetId);
        if (!target) return;

        if (input.files && input.files.length > 0) {
            target.textContent = input.files[0].name;
        } else {
            target.textContent = "Chưa chọn ảnh";
        }
    }
</script>
</body>
</html>