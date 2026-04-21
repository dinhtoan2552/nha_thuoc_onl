<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.Thuoc" %>
<%@ page import="model.CauHinhThanhToan" %>
<%@ page import="utils.CurrencyUtil" %>
<%
    Thuoc thuoc = (Thuoc) request.getAttribute("thuoc");
    Integer soLuong = (Integer) request.getAttribute("soLuong");
    Double tongTien = (Double) request.getAttribute("tongTien");
    Double phiVanChuyen = (Double) request.getAttribute("phiVanChuyen");
    Double tongThanhToan = (Double) request.getAttribute("tongThanhToan");
    String hoTenNhan = (String) request.getAttribute("hoTenNhan");
    String sdtNhan = (String) request.getAttribute("sdtNhan");
    String diaChiNhan = (String) request.getAttribute("diaChiNhan");
    String nguon = (String) request.getAttribute("nguon");
    CauHinhThanhToan cauHinhThanhToan = (CauHinhThanhToan) request.getAttribute("cauHinhThanhToan");

    if (soLuong == null) soLuong = 1;
    if (tongTien == null) tongTien = 0.0;
    if (phiVanChuyen == null) phiVanChuyen = 0.0;
    if (tongThanhToan == null) tongThanhToan = tongTien + phiVanChuyen;
    if (hoTenNhan == null) hoTenNhan = "";
    if (sdtNhan == null) sdtNhan = "";
    if (diaChiNhan == null) diaChiNhan = "";
    if (nguon == null) nguon = "home";

    String anhQR = "";
    String tenNganHangQr = "";
    String soTaiKhoanQr = "";
    String chuTaiKhoanQr = "";

    String anhThe = "";
    String tenNganHangThe = "";
    String soTaiKhoanThe = "";
    String chiNhanhThe = "";
    String chuThe = "";

    boolean batCOD = true;
    boolean batChuyenKhoan = true;
    boolean batThe = true;

    if (cauHinhThanhToan != null) {
        anhQR = cauHinhThanhToan.getAnhQR() != null ? cauHinhThanhToan.getAnhQR() : "";
        tenNganHangQr = cauHinhThanhToan.getTenNganHangQr() != null ? cauHinhThanhToan.getTenNganHangQr() : "";
        soTaiKhoanQr = cauHinhThanhToan.getSoTaiKhoanQr() != null ? cauHinhThanhToan.getSoTaiKhoanQr() : "";
        chuTaiKhoanQr = cauHinhThanhToan.getChuTaiKhoanQr() != null ? cauHinhThanhToan.getChuTaiKhoanQr() : "";

        anhThe = cauHinhThanhToan.getAnhThe() != null ? cauHinhThanhToan.getAnhThe() : "";
        tenNganHangThe = cauHinhThanhToan.getTenNganHangThe() != null ? cauHinhThanhToan.getTenNganHangThe() : "";
        soTaiKhoanThe = cauHinhThanhToan.getSoTaiKhoanThe() != null ? cauHinhThanhToan.getSoTaiKhoanThe() : "";
        chiNhanhThe = cauHinhThanhToan.getChiNhanhThe() != null ? cauHinhThanhToan.getChiNhanhThe() : "";
        chuThe = cauHinhThanhToan.getChuThe() != null ? cauHinhThanhToan.getChuThe() : "";

        batCOD = cauHinhThanhToan.isBatCOD();
        batChuyenKhoan = cauHinhThanhToan.isBatChuyenKhoan();
        batThe = cauHinhThanhToan.isBatThe();
    }
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thanh toán</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/thanh-toan-ngay.css?v=99">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <style>
        /* Chỉ thêm phần upload bill, không đụng phần khác */
        .bill-file-input {
            display: none !important;
            width: 0 !important;
            height: 0 !important;
            opacity: 0 !important;
            visibility: hidden !important;
            position: absolute !important;
            left: -9999px !important;
            pointer-events: none !important;
        }

        .bill-upload-wrap {
            display: flex;
            align-items: center;
            gap: 10px;
            flex-wrap: wrap;
            margin-top: 6px;
        }

        .bill-upload-trigger {
            width: 56px;
            height: 56px;
            border-radius: 14px;
            border: 1.5px dashed #bcd2ff;
            background: #f7fbff;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            cursor: pointer;
            color: #1d4ed8;
            transition: 0.2s ease;
            flex: 0 0 auto;
        }

        .bill-upload-trigger:hover {
            background: #eef5ff;
            transform: translateY(-1px);
        }

        .bill-upload-trigger i {
            font-size: 22px;
        }

        .bill-preview-box {
            width: 70px;
            height: 70px;
            border-radius: 12px;
            overflow: hidden;
            border: 1px solid #dbe7ff;
            background: #f9fbff;
            display: flex;
            align-items: center;
            justify-content: center;
            flex: 0 0 auto;
        }

        .bill-preview-box img {
            width: 100%;
            height: 100%;
            object-fit: cover;
            display: block;
        }

        .bill-file-name {
            font-size: 13px;
            color: #475569;
            margin: 6px 0 0;
            line-height: 1.4;
            word-break: break-word;
        }

        @media (max-width: 768px) {
            .bill-upload-trigger {
                width: 50px;
                height: 50px;
                border-radius: 12px;
            }

            .bill-upload-trigger i {
                font-size: 20px;
            }

            .bill-preview-box {
                width: 60px;
                height: 60px;
                border-radius: 10px;
            }

            .bill-file-name {
                font-size: 12px;
            }
        }
    </style>
</head>
<body>
    <div class="checkout-page">
        <div class="checkout-container">
            <div class="checkout-header">
                <h1>Xác nhận đơn hàng</h1>
                <a href="${pageContext.request.contextPath}/cart" class="back-btn">Quay lại</a>
            </div>

            <% if (thuoc == null) { %>
                <div class="checkout-box">
                    <p>Không tìm thấy sản phẩm.</p>
                </div>
            <% } else { %>
                <div class="checkout-grid">
                    <div class="checkout-box">
                        <h2>Sản phẩm</h2>
                        <div class="product-row">
                            <div class="product-image">
                                <img src="${pageContext.request.contextPath}/load-image?name=<%= thuoc.getHinhAnh() %>" alt="<%= thuoc.getTenThuoc() %>">
                            </div>
                            <div class="product-info">
                                <h3><%= thuoc.getTenThuoc() %></h3>
                                <p>Đơn giá: <strong><%= CurrencyUtil.yen(thuoc.getDonGia()) %></strong></p>
                                <p>Số lượng: <strong><%= soLuong %></strong></p>
                                <p>Tiền thuốc: <strong><%= CurrencyUtil.yen(tongTien) %></strong></p>
                                <p>Phí vận chuyển: <strong><%= CurrencyUtil.yen(phiVanChuyen) %></strong></p>
                                <p class="total-price">Tổng thanh toán: <strong><%= CurrencyUtil.yen(tongThanhToan) %></strong></p>
                            </div>
                        </div>
                    </div>

                    <div class="checkout-box">
                        <h2>Thông tin nhận hàng</h2>

                        <form action="${pageContext.request.contextPath}/xac-nhan-dat-hang-ngay"
                              method="post"
                              enctype="multipart/form-data"
                              class="checkout-form">
                            <input type="hidden" name="idThuoc" value="<%= thuoc.getIdThuoc() %>">
                            <input type="hidden" name="soLuong" value="<%= soLuong %>">
                            <input type="hidden" name="nguon" value="<%= nguon %>">
                            <input type="hidden" name="phiVanChuyen" value="<%= phiVanChuyen %>">

                            <div class="form-group">
                                <label>Tên người nhận</label>
                                <input type="text" name="tenNguoiNhan" value="<%= hoTenNhan %>" required>
                            </div>

                            <div class="form-group">
                                <label>Số điện thoại</label>
                                <input type="text" name="sdtNhan" value="<%= sdtNhan %>" required>
                            </div>

                            <div class="form-group">
                                <label>Địa chỉ nhận</label>
                                <textarea name="diaChiNhan" rows="3" required><%= diaChiNhan %></textarea>
                            </div>

                            <div class="form-group">
                                <label>Ghi chú</label>
                                <textarea name="ghiChu" rows="3" placeholder="Ví dụ: giao giờ hành chính..."></textarea>
                            </div>

                            <div class="form-group">
                                <label>Phương thức thanh toán</label>

                                <div class="payment-dropdown">
                                    <button type="button" class="payment-dropdown-btn" onclick="togglePaymentOptions()">
                                        <span id="selectedPaymentText">Chọn phương thức thanh toán</span>
                                        <i class="fa-solid fa-chevron-down"></i>
                                    </button>

                                    <div class="payment-dropdown-menu" id="paymentOptions">
                                        <% if (batCOD) { %>
                                            <div class="payment-dropdown-item" onclick="selectPayment('COD', 'Nhận hàng rồi trả tiền')">
                                                Nhận hàng rồi trả tiền
                                            </div>
                                        <% } %>

                                        <% if (batChuyenKhoan) { %>
                                            <div class="payment-dropdown-item" onclick="selectPayment('CHUYEN_KHOAN', 'Chuyển khoản QR')">
                                                Chuyển khoản QR
                                            </div>
                                        <% } %>

                                        <% if (batThe) { %>
                                            <div class="payment-dropdown-item" onclick="selectPayment('THE', 'Thanh toán thẻ')">
                                                Thanh toán thẻ
                                            </div>
                                        <% } %>
                                    </div>

                                    <input type="hidden" name="phuongThucThanhToan" id="phuongThucThanhToan" value="">
                                </div>

                                <div class="payment-preview" id="paymentPreview" style="display:none;">
                                    <img id="paymentImage" src="" alt="Thanh toán" class="payment-image" style="display:none;">
                                    <div id="paymentBankInfo" class="payment-bank-info"></div>
                                    <p id="paymentNote" class="payment-note"></p>
                                </div>
                            </div>

                            <div class="form-group" id="billUploadGroup" style="display:none;">
                                <label>Ảnh bill / chứng từ thanh toán</label>

                                <div class="bill-upload-wrap">
                                    <label for="anhBill" class="bill-upload-trigger" title="Chọn ảnh bill">
                                        <i class="fa-regular fa-image"></i>
                                    </label>

                                    <input type="file"
                                           name="anhBill"
                                           id="anhBill"
                                           accept="image/*"
                                           class="bill-file-input">

                                    <div class="bill-preview-box" id="billPreviewBox" style="display:none;">
                                        <img id="billPreviewImage" src="" alt="Ảnh bill xem trước">
                                    </div>
                                </div>

                                <p class="bill-file-name" id="billFileName">Chưa chọn ảnh</p>
                                <small class="upload-note">Bắt buộc khi chọn chuyển khoản hoặc thanh toán thẻ.</small>
                            </div>

                            <div class="form-group">
                                <label>Nguồn đặt hàng</label>
                                <select name="nguonHienThi" disabled>
                                    <option value="home" <%= "home".equalsIgnoreCase(nguon) ? "selected" : "" %>>Mua ngay từ trang chủ</option>
                                    <option value="detail" <%= "detail".equalsIgnoreCase(nguon) ? "selected" : "" %>>Mua ngay từ chi tiết sản phẩm</option>
                                    <option value="cart" <%= "cart".equalsIgnoreCase(nguon) ? "selected" : "" %>>Đặt hàng từ giỏ hàng</option>
                                </select>
                            </div>

                            <button type="submit" id="submitOrderBtn" class="confirm-btn" disabled>Xác nhận đặt hàng</button>
                        </form>
                    </div>
                </div>
            <% } %>
        </div>
    </div>

<script>
    const qrImageName = "<%= anhQR %>";
    const tenNganHangQr = "<%= tenNganHangQr %>";
    const soTaiKhoanQr = "<%= soTaiKhoanQr %>";
    const chuTaiKhoanQr = "<%= chuTaiKhoanQr %>";

    const cardImageName = "<%= anhThe %>";
    const tenNganHangThe = "<%= tenNganHangThe %>";
    const soTaiKhoanThe = "<%= soTaiKhoanThe %>";
    const chiNhanhThe = "<%= chiNhanhThe %>";
    const chuThe = "<%= chuThe %>";

    function resetBillPreview() {
        const billInput = document.getElementById("anhBill");
        const billPreviewBox = document.getElementById("billPreviewBox");
        const billPreviewImage = document.getElementById("billPreviewImage");
        const billFileName = document.getElementById("billFileName");

        if (billInput) {
            billInput.value = "";
            billInput.required = false;
        }

        if (billPreviewImage) {
            billPreviewImage.src = "";
        }

        if (billPreviewBox) {
            billPreviewBox.style.display = "none";
        }

        if (billFileName) {
            billFileName.textContent = "Chưa chọn ảnh";
        }
    }

    function togglePaymentOptions() {
        const menu = document.getElementById("paymentOptions");
        if (menu) {
            menu.classList.toggle("show");
        }
    }

    function selectPayment(value, text) {
        const hiddenInput = document.getElementById("phuongThucThanhToan");
        const selectedText = document.getElementById("selectedPaymentText");
        const preview = document.getElementById("paymentPreview");
        const image = document.getElementById("paymentImage");
        const note = document.getElementById("paymentNote");
        const bankInfo = document.getElementById("paymentBankInfo");
        const submitBtn = document.getElementById("submitOrderBtn");
        const menu = document.getElementById("paymentOptions");
        const billGroup = document.getElementById("billUploadGroup");
        const billInput = document.getElementById("anhBill");

        hiddenInput.value = value;
        selectedText.textContent = text;
        submitBtn.disabled = false;

        if (value === "CHUYEN_KHOAN") {
            preview.style.display = "block";
            billGroup.style.display = "block";
            billInput.required = true;

            if (qrImageName) {
                image.style.display = "block";
                image.src = "${pageContext.request.contextPath}/load-image?name=" + qrImageName;
            } else {
                image.style.display = "none";
                image.src = "";
            }

            bankInfo.innerHTML =
                "<strong>Ngân hàng:</strong> " + (tenNganHangQr || "Chưa cập nhật") + "<br>" +
                "<strong>Số tài khoản:</strong> " + (soTaiKhoanQr || "Chưa cập nhật") + "<br>" +
                "<strong>Chủ tài khoản:</strong> " + (chuTaiKhoanQr || "Chưa cập nhật");

            note.textContent = "Sau khi chuyển khoản QR, vui lòng tải lên ảnh bill để nhân viên xác nhận nhanh hơn.";

        } else if (value === "THE") {
            preview.style.display = "block";
            billGroup.style.display = "block";
            billInput.required = true;

            if (cardImageName) {
                image.style.display = "block";
                image.src = "${pageContext.request.contextPath}/load-image?name=" + cardImageName;
            } else {
                image.style.display = "none";
                image.src = "";
            }

            bankInfo.innerHTML =
                "<strong>Ngân hàng:</strong> " + (tenNganHangThe || "Chưa cập nhật") + "<br>" +
                "<strong>Số tài khoản:</strong> " + (soTaiKhoanThe || "Chưa cập nhật") + "<br>" +
                "<strong>Chi nhánh:</strong> " + (chiNhanhThe || "Chưa cập nhật") + "<br>" +
                "<strong>Chủ thẻ:</strong> " + (chuThe || "Chưa cập nhật");

            note.textContent = "Vui lòng thanh toán theo thông tin thẻ và tải lên ảnh bill/chứng từ thanh toán.";

        } else {
            preview.style.display = "none";
            billGroup.style.display = "none";
            image.src = "";
            image.style.display = "none";
            bankInfo.innerHTML = "";
            note.textContent = "";
            resetBillPreview();
        }

        if (menu) {
            menu.classList.remove("show");
        }
    }

    document.addEventListener("click", function (e) {
        const dropdown = document.querySelector(".payment-dropdown");
        const menu = document.getElementById("paymentOptions");

        if (dropdown && menu && !dropdown.contains(e.target)) {
            menu.classList.remove("show");
        }
    });

    const anhBillInput = document.getElementById("anhBill");
    const billPreviewBox = document.getElementById("billPreviewBox");
    const billPreviewImage = document.getElementById("billPreviewImage");
    const billFileName = document.getElementById("billFileName");

    if (anhBillInput) {
        anhBillInput.addEventListener("change", function () {
            const file = this.files && this.files[0];

            if (!file) {
                if (billPreviewImage) billPreviewImage.src = "";
                if (billPreviewBox) billPreviewBox.style.display = "none";
                if (billFileName) billFileName.textContent = "Chưa chọn ảnh";
                return;
            }

            if (!file.type.startsWith("image/")) {
                alert("Vui lòng chọn file ảnh hợp lệ.");
                this.value = "";
                if (billPreviewImage) billPreviewImage.src = "";
                if (billPreviewBox) billPreviewBox.style.display = "none";
                if (billFileName) billFileName.textContent = "Chưa chọn ảnh";
                return;
            }

            if (billFileName) {
                billFileName.textContent = file.name;
            }

            const reader = new FileReader();
            reader.onload = function (e) {
                if (billPreviewImage) billPreviewImage.src = e.target.result;
                if (billPreviewBox) billPreviewBox.style.display = "flex";
            };
            reader.readAsDataURL(file);
        });
    }
</script>
</body>
</html>