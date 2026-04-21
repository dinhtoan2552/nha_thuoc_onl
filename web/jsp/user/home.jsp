<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.NguoiDung" %>
<%@ page import="model.Thuoc" %>
<%@ page import="model.QuangCao" %>
<%@ page import="model.DanhMuc" %>
<%@ page import="model.ThongTinLienHe" %>
<%@ page import="utils.CurrencyUtil" %>

<%
    NguoiDung user = (NguoiDung) session.getAttribute("user");

    String hoTen = (user != null && user.getHoTen() != null && !user.getHoTen().trim().isEmpty())
            ? user.getHoTen()
            : "Quý Khách";

    String email = (user != null && user.getEmail() != null) ? user.getEmail() : "";
    String soDienThoai = (user != null && user.getSoDienThoai() != null) ? user.getSoDienThoai() : "";
    String diaChi = (user != null && user.getDiaChi() != null) ? user.getDiaChi() : "";

    String cartMessage = (String) session.getAttribute("cartMessage");
    if (cartMessage != null) {
        session.removeAttribute("cartMessage");
    }

    List<Thuoc> danhSachThuoc = (List<Thuoc>) request.getAttribute("danhSachThuoc");
    if (danhSachThuoc == null) {
        danhSachThuoc = new ArrayList<>();
    }

    List<QuangCao> dsQuangCao = (List<QuangCao>) request.getAttribute("dsQuangCao");
    if (dsQuangCao == null) {
        dsQuangCao = new ArrayList<>();
    }

    List<DanhMuc> dsDanhMuc = (List<DanhMuc>) request.getAttribute("dsDanhMuc");
    if (dsDanhMuc == null) {
        dsDanhMuc = new ArrayList<>();
    }

    Integer currentPage = (Integer) request.getAttribute("currentPage");
    Integer totalPages = (Integer) request.getAttribute("totalPages");
    Integer idDanhMuc = (Integer) request.getAttribute("idDanhMuc");

    if (currentPage == null) currentPage = 1;
    if (totalPages == null) totalPages = 1;
    if (idDanhMuc == null) idDanhMuc = 0;

    ThongTinLienHe thongTinLienHe = (ThongTinLienHe) request.getAttribute("thongTinLienHe");

    String linkZaloNoi = "#";
    String linkMessengerNoi = "#";
    String soHotlineNoi = "";

    String diaChiNhaThuocBanner = "";
    String noiDungBannerLienHe = "";

    if (thongTinLienHe != null) {
        if (thongTinLienHe.getSoZalo() != null && !thongTinLienHe.getSoZalo().trim().isEmpty()) {
            linkZaloNoi = "https://zalo.me/" + thongTinLienHe.getSoZalo().trim();
        }

        if (thongTinLienHe.getLinkMessenger() != null && !thongTinLienHe.getLinkMessenger().trim().isEmpty()) {
            linkMessengerNoi = thongTinLienHe.getLinkMessenger().trim();
        }

        if (thongTinLienHe.getSoHotline() != null && !thongTinLienHe.getSoHotline().trim().isEmpty()) {
            soHotlineNoi = thongTinLienHe.getSoHotline().trim();
        }

        if (thongTinLienHe.getDiaChiNhaThuoc() != null && !thongTinLienHe.getDiaChiNhaThuoc().trim().isEmpty()) {
            diaChiNhaThuocBanner = thongTinLienHe.getDiaChiNhaThuoc().trim();
        }

        if (thongTinLienHe.getNoiDungBanner() != null && !thongTinLienHe.getNoiDungBanner().trim().isEmpty()) {
            noiDungBannerLienHe = thongTinLienHe.getNoiDungBanner().trim();
        }
    }
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trang chủ khách hàng</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/home.css?v=4001">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/search-suggest.css?v=1001">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
</head>
<body>
<div class="home-page">
    <div class="container">

        <section class="hero-card">
            <div class="hero-overlay"></div>
            <div class="hero-main">
                <div class="hero-header-row">

                    <div class="brand-row">
                        <div class="brand-logo">
                            <img src="${pageContext.request.contextPath}/assets/css/images/logo-pharmacy-hero.png"
                                 alt="Nhà thuốc Online">
                        </div>

                        <div class="brand-text">
                            <h1>Nhà thuốc Online</h1>
                            <p>Chăm sóc sức khỏe cho cả gia đình</p>
                        </div>
                    </div>

                    <div class="top-tools-row top-tools-row--inside">
                        <div class="top-tools-right">
                            <div class="hello-user">
                                <span class="hello-icon">
                                    <i class="fa-solid fa-user"></i>
                                </span>
                                <span>Xin chào, <%= hoTen %></span>
                            </div>

                            <div class="account-dropdown">
                                <button type="button"
                                        class="top-icon-btn account-toggle"
                                        title="Tài khoản"
                                        onclick="toggleAccountMenu(event)">
                                    <i class="fa-solid fa-user"></i>
                                </button>

                                <div class="account-menu" id="accountMenu">
                                    <div class="account-menu-header">
                                        <div class="account-avatar">
                                            <i class="fa-solid fa-user"></i>
                                        </div>
                                        <div class="account-info">
                                            <div class="account-name"><%= hoTen %></div>
                                            <div class="account-role"><%= user != null ? "Khách hàng" : "Khách vãng lai" %></div>
                                        </div>
                                    </div>

                                    <div class="account-menu-body">
                                        <div class="account-row">
                                            <span class="account-label">Họ tên:</span>
                                            <span class="account-value"><%= hoTen %></span>
                                        </div>

                                        <div class="account-row">
                                            <span class="account-label">Email:</span>
                                            <span class="account-value"><%= email.isEmpty() ? "Chưa đăng nhập" : email %></span>
                                        </div>

                                        <div class="account-row">
                                            <span class="account-label">Số điện thoại:</span>
                                            <span class="account-value"><%= soDienThoai.isEmpty() ? "Chưa cập nhật" : soDienThoai %></span>
                                        </div>

                                        <div class="account-row">
                                            <span class="account-label">Địa chỉ:</span>
                                            <span class="account-value"><%= diaChi.isEmpty() ? "Chưa cập nhật" : diaChi %></span>
                                        </div>
                                    </div>

                                    <div class="account-menu-actions">
                                        <% if (user != null) { %>
                                            <a href="${pageContext.request.contextPath}/doi-gmail" class="account-action-btn change-email-btn">
                                                <i class="fa-solid fa-envelope"></i>
                                                <span>Đổi gmail</span>
                                            </a>

                                            <a href="${pageContext.request.contextPath}/doi-mat-khau" class="account-action-btn change-pass-btn">
                                                <i class="fa-solid fa-key"></i>
                                                <span>Đổi mật khẩu</span>
                                            </a>

                                            <a href="${pageContext.request.contextPath}/logout" class="account-action-btn logout-btn">
                                                <i class="fa-solid fa-right-from-bracket"></i>
                                                <span>Đăng xuất</span>
                                            </a>
                                        <% } else { %>
                                            <a href="${pageContext.request.contextPath}/login" class="account-action-btn change-email-btn">
                                                <i class="fa-solid fa-right-to-bracket"></i>
                                                <span>Đăng nhập</span>
                                            </a>

                                            <a href="${pageContext.request.contextPath}/register" class="account-action-btn change-pass-btn">
                                                <i class="fa-solid fa-user-plus"></i>
                                                <span>Đăng ký</span>
                                            </a>
                                        <% } %>
                                    </div>
                                </div>
                            </div>

                            <a href="${pageContext.request.contextPath}/cart" class="top-icon-btn" title="Giỏ hàng">
                                <i class="fa-solid fa-bag-shopping"></i>
                            </a>

                            <a href="${pageContext.request.contextPath}/don-hang-cua-toi" class="top-icon-btn" title="Đơn hàng">
                                <i class="fa-solid fa-receipt"></i>
                            </a>

                            <a href="${pageContext.request.contextPath}/ho-tro" class="top-icon-btn support-top-btn" title="Hỗ trợ khách hàng">
                                <i class="fa-solid fa-headset"></i>
                            </a>
                        </div>
                    </div>

                </div>
            </div>
        </section>

        <% if (cartMessage != null) { %>
            <div class="message-box"><%= cartMessage %></div>
        <% } %>

        <section class="action-card action-card--search">
            <div class="search-row">
                <div class="home-search-area">
                    <form action="${pageContext.request.contextPath}/user/home" method="get" class="modern-search-form" autocomplete="off">
                        <div class="modern-search-box">
                            <button type="submit" class="modern-search-btn-left" title="Tìm kiếm">
                                <i class="fa-solid fa-magnifying-glass"></i>
                            </button>

                            <input
                                type="text"
                                id="searchKeyword"
                                name="keyword"
                                class="modern-search-input"
                                placeholder="Tìm kiếm thuốc, sản phẩm..."
                                value="<%= request.getAttribute("keyword") != null ? request.getAttribute("keyword") : "" %>"
                                autocomplete="off"
                            />

                            <button type="button" class="modern-search-clear" id="clearSearchBtn" title="Xóa">
                                <i class="fa-solid fa-xmark"></i>
                            </button>

                            <button type="submit" class="modern-search-btn-right" title="Tìm kiếm">
                                <i class="fa-solid fa-magnifying-glass"></i>
                            </button>

                            <div id="searchSuggestList" class="modern-search-suggest"></div>
                        </div>
                    </form>
                </div>

                <a href="${pageContext.request.contextPath}/user/home" class="search-side-btn">Sức khỏe</a>
            </div>
        </section>

        <div class="home-content-layout">
            <aside class="category-sidebar">
                <div class="category-sidebar-card">
                    <div class="category-sidebar-title">Danh mục thuốc</div>

                    <div class="category-sidebar-list">
                        <a href="${pageContext.request.contextPath}/user/home"
                           class="category-side-item <%= idDanhMuc == 0 ? "active-category" : "" %>">
                            <span class="category-side-icon">
                                <i class="fa-solid fa-border-all"></i>
                            </span>
                            <span>Tất cả</span>
                        </a>

                        <% for (DanhMuc dm : dsDanhMuc) { %>
                            <a href="${pageContext.request.contextPath}/user/home?idDanhMuc=<%= dm.getIdDanhMuc() %>"
                               class="category-side-item <%= idDanhMuc == dm.getIdDanhMuc() ? "active-category" : "" %>">
                                <span class="category-side-icon">
                                    <i class="fa-solid fa-capsules"></i>
                                </span>
                                <span><%= dm.getTenDanhMuc() %></span>
                            </a>
                        <% } %>
                    </div>
                </div>
            </aside>

            <section class="section-block section-block-main">
                <div class="section-title">
                    <h2>
                        <%= idDanhMuc > 0 ? "Sản phẩm theo danh mục" : "Sản phẩm nổi bật" %>
                    </h2>
                    <p>
                        <%= idDanhMuc > 0 ? "Danh sách sản phẩm thuộc danh mục bạn đã chọn" : "Một số sản phẩm nổi bật đang được quan tâm" %>
                    </p>
                </div>

                <div class="product-grid">
                    <%
                        if (danhSachThuoc.isEmpty()) {
                    %>
                        <div class="product-card">
                            <div class="product-link">
                                <div class="product-image empty-image">
                                    <i class="fa-solid fa-capsules"></i>
                                </div>
                                <h3>Chưa có sản phẩm</h3>
                                <p>Hiện tại chưa có dữ liệu sản phẩm trong hệ thống.</p>
                                <div class="price"><%= CurrencyUtil.yen(0) %></div>
                            </div>
                            <div class="product-actions">
                                <div class="product-btn-group">
                                    <button class="add-cart-btn" disabled>Thêm vào giỏ</button>
                                    <button class="buy-now-btn" disabled>Mua ngay</button>
                                </div>
                            </div>
                        </div>
                    <%
                        } else {
                            for (Thuoc t : danhSachThuoc) {
                                boolean hetHang = t.getSoLuong() <= 0 || "HET_HANG".equals(t.getTrangThai());
                    %>
                        <div class="product-card">
                            <a class="product-link"
                               href="${pageContext.request.contextPath}/chi-tiet-thuoc?id=<%= t.getIdThuoc() %>">
                                <div class="product-image">
                                    <img src="${pageContext.request.contextPath}/load-image?name=<%= t.getHinhAnh() %>"
                                         alt="<%= t.getTenThuoc() %>">
                                </div>

                                <h3><%= t.getTenThuoc() %></h3>
                                <p><%= (t.getMoTa() != null && !t.getMoTa().trim().isEmpty()) ? t.getMoTa() : "Sản phẩm chăm sóc sức khỏe" %></p>
                                <div class="price"><%= CurrencyUtil.yen(t.getDonGia()) %></div>

                                <% if (hetHang) { %>
                                    <div class="stock-badge out-of-stock">Hết hàng</div>
                                <% } else { %>
                                    <div class="stock-badge in-stock">Còn hàng</div>
                                <% } %>
                            </a>

                            <div class="product-actions">
                                <div class="product-btn-group">
                                    <% if (hetHang) { %>
                                        <button type="button" class="add-cart-btn disabled-btn" disabled>Hết hàng</button>
                                        <button type="button" class="buy-now-btn disabled-btn" disabled>Không thể mua</button>
                                    <% } else { %>
                                        <form action="${pageContext.request.contextPath}/them-vao-gio"
                                              method="post"
                                              class="product-form">
                                            <input type="hidden" name="csrfToken" value="${sessionScope.csrf_token}">
                                            <input type="hidden" name="thuocId" value="<%= t.getIdThuoc() %>">
                                            <input type="hidden" name="soLuong" value="1">
                                            <button type="submit" class="add-cart-btn">Thêm vào giỏ</button>
                                        </form>

                                        <form action="${pageContext.request.contextPath}/dat-hang-ngay"
                                              method="post"
                                              class="product-form">
                                            <input type="hidden" name="csrfToken" value="${sessionScope.csrf_token}">
                                            <input type="hidden" name="idThuoc" value="<%= t.getIdThuoc() %>">
                                            <input type="hidden" name="soLuong" value="1">
                                            <button type="submit" class="buy-now-btn">Mua ngay</button>
                                        </form>
                                    <% } %>
                                </div>
                            </div>
                        </div>
                    <%
                            }
                        }
                    %>
                </div>

                <div class="pagination-wrap">
                    <%
                        String keywordParam = request.getAttribute("keyword") != null
                                ? "&keyword=" + java.net.URLEncoder.encode(String.valueOf(request.getAttribute("keyword")), "UTF-8")
                                : "";

                        String categoryQuery = idDanhMuc > 0 ? "&idDanhMuc=" + idDanhMuc : "";

                        int maxShowPages = 5;
                        int startPage = Math.max(1, currentPage - 2);
                        int endPage = startPage + maxShowPages - 1;

                        if (endPage > totalPages) {
                            endPage = totalPages;
                            startPage = Math.max(1, endPage - maxShowPages + 1);
                        }
                    %>

                    <% if (currentPage > 1) { %>
                        <a href="${pageContext.request.contextPath}/user/home?page=<%= currentPage - 1 %><%= categoryQuery %><%= keywordParam %>"
                           class="page-btn nav-btn">‹ Trước</a>
                    <% } %>

                    <% if (startPage > 1) { %>
                        <a href="${pageContext.request.contextPath}/user/home?page=1<%= categoryQuery %><%= keywordParam %>"
                           class="page-btn">1</a>

                        <% if (startPage > 2) { %>
                            <span class="page-dots">...</span>
                        <% } %>
                    <% } %>

                    <% for (int i = startPage; i <= endPage; i++) { %>
                        <a href="${pageContext.request.contextPath}/user/home?page=<%= i %><%= categoryQuery %><%= keywordParam %>"
                           class="page-btn <%= (i == currentPage) ? "active" : "" %>">
                            <%= i %>
                        </a>
                    <% } %>

                    <% if (endPage < totalPages) { %>
                        <% if (endPage < totalPages - 1) { %>
                            <span class="page-dots">...</span>
                        <% } %>

                        <a href="${pageContext.request.contextPath}/user/home?page=<%= totalPages %><%= categoryQuery %><%= keywordParam %>"
                           class="page-btn"><%= totalPages %></a>
                    <% } %>

                    <% if (currentPage < totalPages) { %>
                        <a href="${pageContext.request.contextPath}/user/home?page=<%= currentPage + 1 %><%= categoryQuery %><%= keywordParam %>"
                           class="page-btn nav-btn">Sau ›</a>
                    <% } %>
                </div>
            </section>
        </div>

        <section class="commit-section">
            <h2>Cam kết nhà thuốc</h2>
            <div class="commit-grid">
                <div class="commit-card">
                    <span class="commit-badge">Cam kết</span>
                    <h3>Nguồn gốc rõ ràng</h3>
                    <p>Sản phẩm rõ nguồn gốc, minh bạch thông tin.</p>
                </div>

                <div class="commit-card">
                    <span class="commit-badge">Cam kết</span>
                    <h3>Đóng gói cẩn thận</h3>
                    <p>Đảm bảo đóng gói cẩn thận trước khi giao hàng.</p>
                </div>

                <div class="commit-card">
                    <span class="commit-badge">Cam kết</span>
                    <h3>Hỗ trợ tư vấn nhanh</h3>
                    <p>Hỗ trợ giải đáp thông tin thuốc nhanh chóng.</p>
                </div>

                <div class="commit-card">
                    <span class="commit-badge">Cam kết</span>
                    <h3>Giao hàng tiện lợi</h3>
                    <p>Giao hàng nhanh và tiện lợi cho khách hàng.</p>
                </div>
            </div>
        </section>

        <% if (!dsQuangCao.isEmpty()) { %>
            <section class="promo-section">
                <div class="promo-header">
                    <h2>Ưu đãi nổi bật</h2>
                    <p>Khám phá các sản phẩm và chương trình đang được nhà thuốc giới thiệu.</p>
                </div>

                <div class="promo-slider" id="promoSlider">
                    <% for (int i = 0; i < dsQuangCao.size(); i++) {
                        QuangCao qc = dsQuangCao.get(i);
                        String link = (qc.getLinkUrl() != null && !qc.getLinkUrl().trim().isEmpty())
                                ? qc.getLinkUrl().trim()
                                : "#";
                    %>
                        <a href="<%= link.startsWith("http") ? link : request.getContextPath() + link %>"
                           class="promo-slide <%= i == 0 ? "active" : "" %>">
                            <img src="${pageContext.request.contextPath}/load-image?name=<%= qc.getHinhAnh() %>"
                                 alt="<%= qc.getTieuDe() != null ? qc.getTieuDe() : "Quảng cáo" %>">

                            <div class="promo-overlay">
                                <div class="promo-content">
                                    <span class="promo-badge">Quảng cáo</span>
                                    <h3><%= qc.getTieuDe() != null && !qc.getTieuDe().trim().isEmpty()
                                            ? qc.getTieuDe()
                                            : "Sản phẩm nổi bật từ nhà thuốc" %></h3>
                                    <div class="promo-action">Xem ngay</div>
                                </div>
                            </div>
                        </a>
                    <% } %>

                    <button type="button" class="promo-nav prev" onclick="movePromo(-1)">
                        <i class="fa-solid fa-chevron-left"></i>
                    </button>

                    <button type="button" class="promo-nav next" onclick="movePromo(1)">
                        <i class="fa-solid fa-chevron-right"></i>
                    </button>

                    <div class="promo-dots" id="promoDots">
                        <% for (int i = 0; i < dsQuangCao.size(); i++) { %>
                            <button type="button"
                                    class="promo-dot <%= i == 0 ? "active" : "" %>"
                                    onclick="showPromoSlide(<%= i %>)"></button>
                        <% } %>
                    </div>
                </div>
            </section>
        <% } %>

        <% if (!diaChiNhaThuocBanner.isEmpty() || !soHotlineNoi.isEmpty() || !noiDungBannerLienHe.isEmpty()) { %>
            <section class="contact-banner-static">
                <div class="contact-banner-content">
                    <div class="contact-banner-badge">THÔNG TIN LIÊN HỆ</div>
                    <h2 class="contact-banner-title">Nhà thuốc luôn sẵn sàng hỗ trợ bạn</h2>

                    <% if (!diaChiNhaThuocBanner.isEmpty()) { %>
                        <div class="contact-banner-row">
                            <i class="fa-solid fa-location-dot"></i>
                            <div><strong>Địa chỉ:</strong> <%= diaChiNhaThuocBanner %></div>
                        </div>
                    <% } %>

                    <% if (!soHotlineNoi.isEmpty()) { %>
                        <div class="contact-banner-row">
                            <i class="fa-solid fa-phone"></i>
                            <div><strong>Hotline:</strong> <%= soHotlineNoi %></div>
                        </div>
                    <% } %>

                    <% if (!noiDungBannerLienHe.isEmpty()) { %>
                        <div class="contact-banner-row">
                            <i class="fa-solid fa-circle-info"></i>
                            <div><%= noiDungBannerLienHe %></div>
                        </div>
                    <% } %>
                </div>
            </section>
        <% } %>

    </div>
</div>

<script>
    function toggleAccountMenu(event) {
        event.stopPropagation();
        const menu = document.getElementById("accountMenu");
        if (menu) {
            menu.classList.toggle("show");
        }
    }

    document.addEventListener("click", function (event) {
        const dropdown = document.querySelector(".account-dropdown");
        const menu = document.getElementById("accountMenu");

        if (dropdown && menu && !dropdown.contains(event.target)) {
            menu.classList.remove("show");
        }
    });

    const promoSlides = document.querySelectorAll(".promo-slide");
    const promoDots = document.querySelectorAll(".promo-dot");
    let promoIndex = 0;
    let promoInterval = null;

    function showPromoSlide(index) {
        if (!promoSlides.length) return;

        if (index < 0) index = promoSlides.length - 1;
        if (index >= promoSlides.length) index = 0;

        promoSlides.forEach(slide => slide.classList.remove("active"));
        promoDots.forEach(dot => dot.classList.remove("active"));

        promoSlides[index].classList.add("active");
        if (promoDots[index]) {
            promoDots[index].classList.add("active");
        }

        promoIndex = index;
    }

    function movePromo(step) {
        showPromoSlide(promoIndex + step);
        restartPromoAuto();
    }

    function startPromoAuto() {
        if (promoSlides.length <= 1) return;

        promoInterval = setInterval(() => {
            showPromoSlide(promoIndex + 1);
        }, 4000);
    }

    function restartPromoAuto() {
        if (promoInterval) {
            clearInterval(promoInterval);
        }
        startPromoAuto();
    }

    showPromoSlide(0);
    startPromoAuto();
</script>

<div class="bottom-nav">
    <a href="${pageContext.request.contextPath}/user/home">
        <i class="fa fa-home"></i>
        <span>Trang chủ</span>
    </a>

    <a href="${pageContext.request.contextPath}/user/home">
        <i class="fa fa-th-large"></i>
        <span>Danh mục</span>
    </a>

    <a href="${pageContext.request.contextPath}/ho-tro">
        <i class="fa fa-headset"></i>
        <span>Tư vấn</span>
    </a>

    <a href="${pageContext.request.contextPath}/don-hang-cua-toi">
        <i class="fa fa-receipt"></i>
        <span>Đơn hàng</span>
    </a>

    <a href="<%= user != null ? request.getContextPath() + "/doi-mat-khau" : request.getContextPath() + "/login" %>">
        <i class="fa fa-user"></i>
        <span>Tài khoản</span>
    </a>
</div>

<div class="floating-contact">
    <a href="<%= linkZaloNoi %>"
       class="floating-contact-btn zalo-btn"
       target="_blank"
       rel="noopener noreferrer"
       title="Liên hệ Zalo">
        <span class="floating-icon">Z</span>
    </a>

    <a href="<%= linkMessengerNoi %>"
       class="floating-contact-btn messenger-btn"
       target="_blank"
       rel="noopener noreferrer"
       title="Nhắn Facebook">
        <i class="fa-brands fa-facebook-messenger"></i>
    </a>

    <a href="tel:<%= soHotlineNoi %>"
       class="floating-contact-btn phone-btn"
       title="Gọi điện">
        <i class="fa-solid fa-phone"></i>
    </a>
</div>

<script>
document.addEventListener("DOMContentLoaded", function () {
    const input = document.getElementById("searchKeyword");
    const suggestBox = document.getElementById("searchSuggestList");
    const clearBtn = document.getElementById("clearSearchBtn");

    if (!input || !suggestBox || !clearBtn) return;

    let debounceTimer = null;
    let currentItems = [];
    let activeIndex = -1;

    function escapeHtml(text) {
        if (!text) return "";
        return text
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#039;");
    }

    function toggleClearButton() {
        if (input.value.trim().length > 0) {
            clearBtn.style.display = "flex";
        } else {
            clearBtn.style.display = "none";
        }
    }

    function hideSuggest() {
        suggestBox.style.display = "none";
        suggestBox.innerHTML = "";
        currentItems = [];
        activeIndex = -1;
    }

    function showSuggest(items) {
        if (!items || items.length === 0) {
            suggestBox.innerHTML = '<div class="modern-search-empty">Không có gợi ý phù hợp</div>';
            suggestBox.style.display = "block";
            currentItems = [];
            activeIndex = -1;
            return;
        }

        let html = "";
        items.forEach(function (item, index) {
            html += '<div class="modern-search-suggest-item" data-index="' + index + '" data-name="' + escapeHtml(item.tenThuoc) + '">';
            html += '<i class="fa-solid fa-magnifying-glass"></i>';
            html += '<span>' + escapeHtml(item.tenThuoc) + '</span>';
            html += '</div>';
        });

        suggestBox.innerHTML = html;
        suggestBox.style.display = "block";
        currentItems = items;
        activeIndex = -1;
    }

    async function fetchSuggestions(keyword) {
        try {
            const url = "${pageContext.request.contextPath}/api/user/goi-y-thuoc?q=" + encodeURIComponent(keyword);
            const response = await fetch(url, {
                method: "GET",
                headers: {
                    "Accept": "application/json"
                }
            });

            if (!response.ok) {
                hideSuggest();
                return;
            }

            const data = await response.json();
            showSuggest(data);
        } catch (e) {
            hideSuggest();
            console.error("Lỗi gọi API gợi ý thuốc:", e);
        }
    }

    input.addEventListener("input", function () {
        const keyword = input.value.trim();
        toggleClearButton();
        clearTimeout(debounceTimer);

        if (keyword.length < 2) {
            hideSuggest();
            return;
        }

        debounceTimer = setTimeout(function () {
            fetchSuggestions(keyword);
        }, 250);
    });

    input.addEventListener("focus", function () {
        const keyword = input.value.trim();
        toggleClearButton();

        if (keyword.length >= 2) {
            fetchSuggestions(keyword);
        }
    });

    input.addEventListener("keydown", function (e) {
        const items = suggestBox.querySelectorAll(".modern-search-suggest-item");
        if (!items.length) return;

        if (e.key === "ArrowDown") {
            e.preventDefault();
            activeIndex++;
            if (activeIndex >= items.length) activeIndex = 0;
        } else if (e.key === "ArrowUp") {
            e.preventDefault();
            activeIndex--;
            if (activeIndex < 0) activeIndex = items.length - 1;
        } else if (e.key === "Enter") {
            if (activeIndex >= 0 && currentItems[activeIndex]) {
                e.preventDefault();
                input.value = currentItems[activeIndex].tenThuoc;
                hideSuggest();
                input.form.submit();
            }
            return;
        } else {
            return;
        }

        items.forEach(function (item) {
            item.classList.remove("active");
        });

        if (items[activeIndex]) {
            items[activeIndex].classList.add("active");
        }
    });

    suggestBox.addEventListener("click", function (e) {
        const item = e.target.closest(".modern-search-suggest-item");
        if (!item) return;

        const tenThuoc = item.getAttribute("data-name");
        input.value = tenThuoc;
        toggleClearButton();
        hideSuggest();
        input.form.submit();
    });

    clearBtn.addEventListener("click", function () {
        input.value = "";
        input.focus();
        toggleClearButton();
        hideSuggest();
    });

    document.addEventListener("click", function (e) {
        if (!e.target.closest(".modern-search-box")) {
            hideSuggest();
        }
    });

    toggleClearButton();
});
</script>

</body>
</html>