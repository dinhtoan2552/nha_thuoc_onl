<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
    String uri = request.getRequestURI();
    String context = request.getContextPath();
%>

<button type="button" class="mobile-sidebar-toggle admin-mobile-toggle" onclick="toggleAdminSidebar()" aria-label="Mở menu">
    <i class="fa-solid fa-bars"></i>
</button>

<div class="mobile-sidebar-overlay" id="adminSidebarOverlay" onclick="closeAdminSidebar()"></div>

<aside class="admin-sidebar" id="adminSidebar">
    <div class="sidebar-brand-wrap">
        <div class="sidebar-brand">
            <div class="brand-logo">QL</div>
            <div class="brand-text">
                <h2>Pharmacy Admin</h2>
                <p>Dashboard</p>
            </div>
        </div>

        <button type="button" class="mobile-sidebar-close" onclick="closeAdminSidebar()" aria-label="Đóng menu">
            <i class="fa-solid fa-xmark"></i>
        </button>
    </div>

    <ul class="sidebar-menu">
        <li>
            <a href="<%= context %>/admin/dashboard"
               class="<%= uri.contains("/admin/dashboard") ? "active" : "" %>">
                <i class="fa-solid fa-chart-line"></i>
                <span>Dashboard</span>
            </a>
        </li>

        <li>
            <a href="<%= context %>/admin/thuoc"
               class="<%= uri.contains("/admin/thuoc") ? "active" : "" %>">
                <i class="fa-solid fa-pills"></i>
                <span>Quản lý thuốc</span>
            </a>
        </li>

        <li>
            <a href="<%= context %>/admin/danhmuc"
               class="<%= uri.contains("/admin/danhmuc") ? "active" : "" %>">
                <i class="fa-solid fa-layer-group"></i>
                <span>Quản lý danh mục</span>
            </a>
        </li>

        <li>
            <a href="<%= context %>/admin/donhang"
               class="<%= uri.contains("/admin/donhang") ? "active" : "" %>">
                <i class="fa-solid fa-cart-shopping"></i>
                <span>Quản lý đơn hàng</span>
            </a>
        </li>

        <li>
            <a href="<%= context %>/admin/nguoidung"
               class="<%= uri.contains("/admin/nguoidung") ? "active" : "" %>">
                <i class="fa-solid fa-users"></i>
                <span>Quản lý người dùng</span>
            </a>
        </li>

        <li>
            <a href="<%= context %>/admin/nhanvien"
               class="<%= uri.contains("/admin/nhanvien") ? "active" : "" %>">
                <i class="fa-solid fa-user-tie"></i>
                <span>Quản lý nhân viên</span>
            </a>
        </li>

        <li>
            <a href="<%= context %>/admin/thanh-toan"
               class="<%= uri.contains("/admin/thanh-toan") ? "active" : "" %>">
                <i class="fa-solid fa-money-check-dollar"></i>
                <span>Quản lý thanh toán</span>
            </a>
        </li>
        <li>
    <a href="<%= context %>/admin/lien-he"
       class="<%= uri.contains("/admin/lien-he") ? "active" : "" %>">
        <i class="fa-solid fa-address-book"></i>
        <span>Cài đặt liên hệ</span>
    </a>
</li>

        <li>
            <a href="<%= context %>/admin/thongke"
               class="<%= uri.contains("/admin/thongke") ? "active" : "" %>">
                <i class="fa-solid fa-chart-column"></i>
                <span>Thống kê báo cáo</span>
            </a>
        </li>

        <li>
            <a href="<%= context %>/admin/quangcao"
               class="<%= uri.contains("/admin/quangcao") ? "active" : "" %>">
                <i class="fa-solid fa-image"></i>
                <span>Quản lý quảng cáo</span>
            </a>
        </li>
    </ul>
</aside>

<script>
    function toggleAdminSidebar() {
        const sidebar = document.getElementById("adminSidebar");
        const overlay = document.getElementById("adminSidebarOverlay");
        const body = document.body;

        if (sidebar) sidebar.classList.toggle("show");
        if (overlay) overlay.classList.toggle("show");
        if (body) body.classList.toggle("sidebar-open");
    }

    function closeAdminSidebar() {
        const sidebar = document.getElementById("adminSidebar");
        const overlay = document.getElementById("adminSidebarOverlay");
        const body = document.body;

        if (sidebar) sidebar.classList.remove("show");
        if (overlay) overlay.classList.remove("show");
        if (body) body.classList.remove("sidebar-open");
    }

    document.addEventListener("DOMContentLoaded", function () {
        const menuLinks = document.querySelectorAll("#adminSidebar .sidebar-menu a");
        menuLinks.forEach(function (link) {
            link.addEventListener("click", function () {
                if (window.innerWidth <= 900) {
                    closeAdminSidebar();
                }
            });
        });

        window.addEventListener("resize", function () {
            if (window.innerWidth > 900) {
                closeAdminSidebar();
            }
        });
    });
</script>