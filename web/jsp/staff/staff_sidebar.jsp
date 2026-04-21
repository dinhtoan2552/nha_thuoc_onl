<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
    String uri = request.getRequestURI();
%>

<button type="button" class="mobile-sidebar-toggle staff-mobile-toggle" onclick="toggleStaffSidebar()">
    <i class="fa-solid fa-bars"></i>
</button>

<div class="mobile-sidebar-overlay" id="staffSidebarOverlay" onclick="closeStaffSidebar()"></div>

<div class="staff-sidebar" id="staffSidebar">
    <div class="staff-sidebar-top">
        <h2>Staff Panel</h2>
        <p>Nhân viên nhà thuốc</p>
    </div>

    <button type="button" class="mobile-sidebar-close" onclick="closeStaffSidebar()">
        <i class="fa-solid fa-xmark"></i>
    </button>

    <div class="staff-menu">
        <a href="${pageContext.request.contextPath}/staff/dashboard"
           class="staff-link <%= uri.contains("/staff/dashboard") ? "active" : "" %>"
           onclick="closeStaffSidebar()">
            <i class="fa-solid fa-house"></i>
            <span>Tổng quan</span>
        </a>

        <a href="${pageContext.request.contextPath}/staff/donhang"
           class="staff-link <%= uri.contains("/staff/donhang") ? "active" : "" %>"
           onclick="closeStaffSidebar()">
            <i class="fa-solid fa-receipt"></i>
            <span>Xử lý đơn hàng</span>
            <span id="staffOrderBadge" class="order-badge" style="display: none;">0</span>
        </a>

        <a href="${pageContext.request.contextPath}/staff/kho"
           class="staff-link <%= uri.contains("/staff/kho") ? "active" : "" %>"
           onclick="closeStaffSidebar()">
            <i class="fa-solid fa-boxes-stacked"></i>
            <span>Kiểm tra kho</span>
        </a>

        <a href="${pageContext.request.contextPath}/staff/hotro"
           class="staff-link <%= uri.contains("/staff/hotro") ? "active" : "" %>"
           onclick="closeStaffSidebar()">
            <i class="fa-solid fa-headset"></i>
            <span>Hỗ trợ khách hàng</span>
            <span id="supportBadge" class="order-badge" style="display:none;">0</span>
        </a>
    </div>

    <script>
        function toggleStaffSidebar() {
            const sidebar = document.getElementById("staffSidebar");
            const overlay = document.getElementById("staffSidebarOverlay");
            if (sidebar) sidebar.classList.toggle("show");
            if (overlay) overlay.classList.toggle("show");
        }

        function closeStaffSidebar() {
            const sidebar = document.getElementById("staffSidebar");
            const overlay = document.getElementById("staffSidebarOverlay");
            if (sidebar) sidebar.classList.remove("show");
            if (overlay) overlay.classList.remove("show");
        }

        function updateStaffOrderBadge() {
            fetch('${pageContext.request.contextPath}/staff/thong-bao-don-hang', {
                method: 'GET',
                headers: { 'X-Requested-With': 'XMLHttpRequest' }
            })
            .then(response => response.json())
            .then(data => {
                const badge = document.getElementById('staffOrderBadge');
                const dashboardChoXacNhan = document.getElementById('dashboardChoXacNhan');

                if (!badge && !dashboardChoXacNhan) return;

                if (data.success) {
                    if (badge) {
                        if (data.soDonChoXacNhan > 0) {
                            badge.textContent = data.soDonChoXacNhan;
                            badge.style.display = 'inline-flex';
                        } else {
                            badge.textContent = '0';
                            badge.style.display = 'none';
                        }
                    }

                    if (dashboardChoXacNhan) {
                        dashboardChoXacNhan.textContent = data.soDonChoXacNhan;
                    }
                }
            })
            .catch(error => console.log('Lỗi lấy badge đơn hàng:', error));
        }

        function updateSupportBadge() {
            fetch('${pageContext.request.contextPath}/staff/thong-bao-ho-tro', {
                method: 'GET',
                headers: { 'X-Requested-With': 'XMLHttpRequest' }
            })
            .then(response => response.json())
            .then(data => {
                const badge = document.getElementById('supportBadge');
                if (!badge) return;

                if (data.success && data.soHoTroMoi > 0) {
                    badge.textContent = data.soHoTroMoi;
                    badge.style.display = 'inline-flex';
                } else {
                    badge.textContent = '0';
                    badge.style.display = 'none';
                }
            })
            .catch(error => console.log('Lỗi lấy badge hỗ trợ:', error));
        }

        updateStaffOrderBadge();
        updateSupportBadge();

        setInterval(updateStaffOrderBadge, 5000);
        setInterval(updateSupportBadge, 5000);
    </script>
</div>