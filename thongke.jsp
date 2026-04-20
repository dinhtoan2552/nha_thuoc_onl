<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.temporal.WeekFields" %>
<%@ page import="model.ThongKeNgay" %>
<%@ page import="model.ThongKeSanPham" %>
<%@ page import="model.ThongKeKhachHang" %>
<%@ page import="utils.CurrencyUtil" %>
<%
    String tuNgay = (String) request.getAttribute("tuNgay");
    String denNgay = (String) request.getAttribute("denNgay");
    String quickRange = (String) request.getAttribute("quickRange");

    Double tongDoanhThu = (Double) request.getAttribute("tongDoanhThu");
    Double tongGiaVon = (Double) request.getAttribute("tongGiaVon");
    Double tongLoiNhuan = (Double) request.getAttribute("tongLoiNhuan");

    Integer tongDonHang = (Integer) request.getAttribute("tongDonHang");
    Integer hoanThanh = (Integer) request.getAttribute("hoanThanh");
    Integer choXacNhan = (Integer) request.getAttribute("choXacNhan");
    Integer dangGiao = (Integer) request.getAttribute("dangGiao");
    Integer daHuy = (Integer) request.getAttribute("daHuy");

    List<ThongKeNgay> doanhThuTheoNgay = (List<ThongKeNgay>) request.getAttribute("doanhThuTheoNgay");
    if (doanhThuTheoNgay == null) doanhThuTheoNgay = new ArrayList<>();

    List<ThongKeNgay> doanhThuTheoTuan = (List<ThongKeNgay>) request.getAttribute("doanhThuTheoTuan");
    if (doanhThuTheoTuan == null) doanhThuTheoTuan = new ArrayList<>();

    List<ThongKeNgay> doanhThuTheoThang = (List<ThongKeNgay>) request.getAttribute("doanhThuTheoThang");
    if (doanhThuTheoThang == null) doanhThuTheoThang = new ArrayList<>();

    List<ThongKeNgay> loiNhuanTheoNgay = (List<ThongKeNgay>) request.getAttribute("loiNhuanTheoNgay");
    if (loiNhuanTheoNgay == null) loiNhuanTheoNgay = new ArrayList<>();

    List<ThongKeNgay> loiNhuanTheoTuan = (List<ThongKeNgay>) request.getAttribute("loiNhuanTheoTuan");
    if (loiNhuanTheoTuan == null) loiNhuanTheoTuan = new ArrayList<>();

    List<ThongKeNgay> loiNhuanTheoThang = (List<ThongKeNgay>) request.getAttribute("loiNhuanTheoThang");
    if (loiNhuanTheoThang == null) loiNhuanTheoThang = new ArrayList<>();

    List<ThongKeSanPham> topSanPham = (List<ThongKeSanPham>) request.getAttribute("topSanPham");
    if (topSanPham == null) topSanPham = new ArrayList<>();

    List<ThongKeKhachHang> topKhachHang = (List<ThongKeKhachHang>) request.getAttribute("topKhachHang");
    if (topKhachHang == null) topKhachHang = new ArrayList<>();

    if (tongDoanhThu == null) tongDoanhThu = 0.0;
    if (tongGiaVon == null) tongGiaVon = 0.0;
    if (tongLoiNhuan == null) tongLoiNhuan = 0.0;

    if (tongDonHang == null) tongDonHang = 0;
    if (hoanThanh == null) hoanThanh = 0;
    if (choXacNhan == null) choXacNhan = 0;
    if (dangGiao == null) dangGiao = 0;
    if (daHuy == null) daHuy = 0;

    DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate startDate = (tuNgay != null && !tuNgay.trim().isEmpty()) ? LocalDate.parse(tuNgay) : LocalDate.now().minusDays(6);
    LocalDate endDate = (denNgay != null && !denNgay.trim().isEmpty()) ? LocalDate.parse(denNgay) : LocalDate.now();

    if (endDate.isBefore(startDate)) {
        LocalDate temp = startDate;
        startDate = endDate;
        endDate = temp;
    }

    WeekFields weekFields = WeekFields.ISO;

    Map<String, Double> mapNgay = new LinkedHashMap<>();
    Map<String, Double> mapLoiNhuanNgay = new LinkedHashMap<>();

    LocalDate chartDayEnd = endDate;
    long totalDays = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;

    if (totalDays < 7) {
        chartDayEnd = startDate.plusDays(6);
    }

    for (LocalDate d = startDate; !d.isAfter(chartDayEnd); d = d.plusDays(1)) {
        String label = d.format(dateFmt);
        mapNgay.put(label, 0.0);
        mapLoiNhuanNgay.put(label, 0.0);
    }

    for (ThongKeNgay item : doanhThuTheoNgay) {
        if (item != null && item.getNhan() != null) {
            mapNgay.put(item.getNhan(), CurrencyUtil.toJPY(item.getGiaTri()));
        }
    }

    for (ThongKeNgay item : loiNhuanTheoNgay) {
        if (item != null && item.getNhan() != null) {
            mapLoiNhuanNgay.put(item.getNhan(), CurrencyUtil.toJPY(item.getGiaTri()));
        }
    }

    Map<String, Double> mapTuan = new LinkedHashMap<>();
    Map<String, Double> mapLoiNhuanTuan = new LinkedHashMap<>();

    LocalDate weekCursor = startDate;
    while (!weekCursor.isAfter(endDate)) {
        int week = weekCursor.get(weekFields.weekOfWeekBasedYear());
        int year = weekCursor.get(weekFields.weekBasedYear());
        String label = "Tuần " + week + "/" + year;
        if (!mapTuan.containsKey(label)) {
            mapTuan.put(label, 0.0);
        }
        if (!mapLoiNhuanTuan.containsKey(label)) {
            mapLoiNhuanTuan.put(label, 0.0);
        }
        weekCursor = weekCursor.plusDays(1);
    }

    if (mapTuan.size() < 4) {
        LocalDate extendWeek = endDate.plusDays(1);
        while (mapTuan.size() < 4) {
            int week = extendWeek.get(weekFields.weekOfWeekBasedYear());
            int year = extendWeek.get(weekFields.weekBasedYear());
            String label = "Tuần " + week + "/" + year;
            if (!mapTuan.containsKey(label)) {
                mapTuan.put(label, 0.0);
            }
            if (!mapLoiNhuanTuan.containsKey(label)) {
                mapLoiNhuanTuan.put(label, 0.0);
            }
            extendWeek = extendWeek.plusDays(7);
        }
    }

    for (ThongKeNgay item : doanhThuTheoTuan) {
        if (item != null && item.getNhan() != null) {
            mapTuan.put(item.getNhan(), CurrencyUtil.toJPY(item.getGiaTri()));
        }
    }

    for (ThongKeNgay item : loiNhuanTheoTuan) {
        if (item != null && item.getNhan() != null) {
            mapLoiNhuanTuan.put(item.getNhan(), CurrencyUtil.toJPY(item.getGiaTri()));
        }
    }

    Map<String, Double> mapThang = new LinkedHashMap<>();
    Map<String, Double> mapLoiNhuanThang = new LinkedHashMap<>();

    LocalDate monthCursor = startDate.withDayOfMonth(1);
    LocalDate monthEnd = endDate.withDayOfMonth(1);

    while (!monthCursor.isAfter(monthEnd)) {
        String label = monthCursor.getYear() + "-" + String.format("%02d", monthCursor.getMonthValue());
        mapThang.put(label, 0.0);
        mapLoiNhuanThang.put(label, 0.0);
        monthCursor = monthCursor.plusMonths(1);
    }

    if (mapThang.size() < 6) {
        LocalDate extendMonth = endDate.withDayOfMonth(1).plusMonths(1);
        while (mapThang.size() < 6) {
            String label = extendMonth.getYear() + "-" + String.format("%02d", extendMonth.getMonthValue());
            if (!mapThang.containsKey(label)) {
                mapThang.put(label, 0.0);
            }
            if (!mapLoiNhuanThang.containsKey(label)) {
                mapLoiNhuanThang.put(label, 0.0);
            }
            extendMonth = extendMonth.plusMonths(1);
        }
    }

    for (ThongKeNgay item : doanhThuTheoThang) {
        if (item != null && item.getNhan() != null) {
            mapThang.put(item.getNhan(), CurrencyUtil.toJPY(item.getGiaTri()));
        }
    }

    for (ThongKeNgay item : loiNhuanTheoThang) {
        if (item != null && item.getNhan() != null) {
            mapLoiNhuanThang.put(item.getNhan(), CurrencyUtil.toJPY(item.getGiaTri()));
        }
    }

    StringBuilder labelsNgay = new StringBuilder("[");
    StringBuilder valuesNgay = new StringBuilder("[");
    StringBuilder valuesLoiNhuanNgay = new StringBuilder("[");
    int dayIndex = 0;
    for (String key : mapNgay.keySet()) {
        labelsNgay.append("'").append(key).append("'");
        valuesNgay.append(mapNgay.get(key));
        valuesLoiNhuanNgay.append(mapLoiNhuanNgay.containsKey(key) ? mapLoiNhuanNgay.get(key) : 0);
        if (dayIndex < mapNgay.size() - 1) {
            labelsNgay.append(",");
            valuesNgay.append(",");
            valuesLoiNhuanNgay.append(",");
        }
        dayIndex++;
    }
    labelsNgay.append("]");
    valuesNgay.append("]");
    valuesLoiNhuanNgay.append("]");

    StringBuilder labelsTuan = new StringBuilder("[");
    StringBuilder valuesTuan = new StringBuilder("[");
    StringBuilder valuesLoiNhuanTuan = new StringBuilder("[");
    int weekIndex = 0;
    for (String key : mapTuan.keySet()) {
        labelsTuan.append("'").append(key).append("'");
        valuesTuan.append(mapTuan.get(key));
        valuesLoiNhuanTuan.append(mapLoiNhuanTuan.containsKey(key) ? mapLoiNhuanTuan.get(key) : 0);
        if (weekIndex < mapTuan.size() - 1) {
            labelsTuan.append(",");
            valuesTuan.append(",");
            valuesLoiNhuanTuan.append(",");
        }
        weekIndex++;
    }
    labelsTuan.append("]");
    valuesTuan.append("]");
    valuesLoiNhuanTuan.append("]");

    StringBuilder labelsThang = new StringBuilder("[");
    StringBuilder valuesThang = new StringBuilder("[");
    StringBuilder valuesLoiNhuanThang = new StringBuilder("[");
    int monthIndex = 0;
    for (String key : mapThang.keySet()) {
        labelsThang.append("'").append(key).append("'");
        valuesThang.append(mapThang.get(key));
        valuesLoiNhuanThang.append(mapLoiNhuanThang.containsKey(key) ? mapLoiNhuanThang.get(key) : 0);
        if (monthIndex < mapThang.size() - 1) {
            labelsThang.append(",");
            valuesThang.append(",");
            valuesLoiNhuanThang.append(",");
        }
        monthIndex++;
    }
    labelsThang.append("]");
    valuesThang.append("]");
    valuesLoiNhuanThang.append("]");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Thống kê báo cáo</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/dashboard.css?v=2">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/thongke.css?v=5">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        .chart-card canvas.chart-small {
            width: 100% !important;
            height: 320px !important;
            max-height: 320px !important;
        }
        .stat-card.cost .stat-icon {
            background: rgba(255, 152, 0, 0.15);
        }
        .stat-card.profit .stat-icon {
            background: rgba(46, 204, 113, 0.15);
        }
        .profit-positive {
            color: #1e9e5a;
        }
        .profit-negative {
            color: #d63031;
        }
    </style>
</head>
<body>
<div class="admin-layout">
    <jsp:include page="/jsp/admin/admin_sidebar.jsp" />

    <div class="admin-main">
        <div class="admin-topbar">
            <div>
                <h1>Thống kê báo cáo</h1>
                <p>Theo dõi doanh thu, giá vốn, lợi nhuận, đơn hàng, khách hàng và sản phẩm bán chạy của nhà thuốc.</p>
            </div>
            <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
        </div>

        <div class="filter-card">
            <form action="${pageContext.request.contextPath}/admin/thongke" method="get" class="filter-form">
                <div class="form-group">
                    <label>Từ ngày</label>
                    <input type="date" name="tuNgay" value="<%= tuNgay == null ? "" : tuNgay %>">
                </div>

                <div class="form-group">
                    <label>Đến ngày</label>
                    <input type="date" name="denNgay" value="<%= denNgay == null ? "" : denNgay %>">
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn-primary">Lọc báo cáo</button>
                    <a href="${pageContext.request.contextPath}/admin/thongke" class="btn-outline">Đặt lại</a>
                </div>
            </form>

            <div class="quick-filter">
                <a href="${pageContext.request.contextPath}/admin/thongke?quickRange=today"
                   class="quick-btn <%= "today".equals(quickRange) ? "active" : "" %>">Hôm nay</a>

                <a href="${pageContext.request.contextPath}/admin/thongke?quickRange=7days"
                   class="quick-btn <%= "7days".equals(quickRange) ? "active" : "" %>">7 ngày</a>

                <a href="${pageContext.request.contextPath}/admin/thongke?quickRange=30days"
                   class="quick-btn <%= "30days".equals(quickRange) ? "active" : "" %>">30 ngày</a>
            </div>
        </div>

        <div class="stats-grid">
            <div class="stat-card revenue">
                <div class="stat-icon"><i class="fa-solid fa-sack-dollar"></i></div>
                <div class="stat-content">
                    <span>Tổng doanh thu</span>
                    <h2><%= CurrencyUtil.yen(tongDoanhThu) %></h2>
                </div>
            </div>

            <div class="stat-card cost">
                <div class="stat-icon"><i class="fa-solid fa-coins"></i></div>
                <div class="stat-content">
                    <span>Tổng giá vốn</span>
                    <h2><%= CurrencyUtil.yen(tongGiaVon) %></h2>
                </div>
            </div>

            <div class="stat-card profit">
                <div class="stat-icon"><i class="fa-solid fa-chart-line"></i></div>
                <div class="stat-content">
                    <span>Lợi nhuận</span>
                    <h2 class="<%= tongLoiNhuan >= 0 ? "profit-positive" : "profit-negative" %>">
                        <%= CurrencyUtil.yen(tongLoiNhuan) %>
                    </h2>
                </div>
            </div>

            <div class="stat-card order">
                <div class="stat-icon"><i class="fa-solid fa-cart-shopping"></i></div>
                <div class="stat-content">
                    <span>Tổng đơn hàng</span>
                    <h2><%= tongDonHang %></h2>
                </div>
            </div>

            <div class="stat-card done">
                <div class="stat-icon"><i class="fa-solid fa-circle-check"></i></div>
                <div class="stat-content">
                    <span>Đơn hoàn thành</span>
                    <h2><%= hoanThanh %></h2>
                </div>
            </div>

            <div class="stat-card pending">
                <div class="stat-icon"><i class="fa-solid fa-hourglass-half"></i></div>
                <div class="stat-content">
                    <span>Chờ xác nhận</span>
                    <h2><%= choXacNhan %></h2>
                </div>
            </div>

            <div class="stat-card shipping">
                <div class="stat-icon"><i class="fa-solid fa-truck-fast"></i></div>
                <div class="stat-content">
                    <span>Đang giao</span>
                    <h2><%= dangGiao %></h2>
                </div>
            </div>

            <div class="stat-card cancel">
                <div class="stat-icon"><i class="fa-solid fa-ban"></i></div>
                <div class="stat-content">
                    <span>Đã hủy</span>
                    <h2><%= daHuy %></h2>
                </div>
            </div>
        </div>

        <div class="chart-grid">
            <div class="chart-card">
                <div class="card-header">
                    <h2>Biểu đồ doanh thu theo ngày</h2>
                </div>
                <canvas id="revenueDayChart" class="chart-small"></canvas>
            </div>

            <div class="chart-card">
                <div class="card-header">
                    <h2>Biểu đồ lợi nhuận theo ngày</h2>
                </div>
                <canvas id="profitDayChart" class="chart-small"></canvas>
            </div>
        </div>

        <div class="chart-grid">
            <div class="chart-card">
                <div class="card-header">
                    <h2>Trạng thái đơn hàng</h2>
                </div>
                <canvas id="statusChart" class="chart-small"></canvas>
            </div>

            <div class="chart-card">
                <div class="card-header">
                    <h2>Doanh thu so với lợi nhuận theo ngày</h2>
                </div>
                <canvas id="compareDayChart" class="chart-small"></canvas>
            </div>
        </div>

        <div class="chart-grid">
            <div class="chart-card">
                <div class="card-header">
                    <h2>Biểu đồ doanh thu theo tuần</h2>
                </div>
                <canvas id="revenueWeekChart" class="chart-small"></canvas>
            </div>

            <div class="chart-card">
                <div class="card-header">
                    <h2>Biểu đồ lợi nhuận theo tuần</h2>
                </div>
                <canvas id="profitWeekChart" class="chart-small"></canvas>
            </div>
        </div>

        <div class="chart-grid">
            <div class="chart-card">
                <div class="card-header">
                    <h2>Biểu đồ doanh thu theo tháng</h2>
                </div>
                <canvas id="revenueMonthChart" class="chart-small"></canvas>
            </div>

            <div class="chart-card">
                <div class="card-header">
                    <h2>Biểu đồ lợi nhuận theo tháng</h2>
                </div>
                <canvas id="profitMonthChart" class="chart-small"></canvas>
            </div>
        </div>

        <div class="table-two-col">
            <div class="table-card">
                <div class="card-header">
                    <h2>Top sản phẩm bán chạy</h2>
                </div>

                <% if (topSanPham.isEmpty()) { %>
                    <div class="empty-box">
                        <p>Chưa có dữ liệu sản phẩm bán chạy.</p>
                    </div>
                <% } else { %>
                    <div class="table-wrap">
                        <table class="report-table">
                            <thead>
                                <tr>
                                    <th>Mã</th>
                                    <th>Sản phẩm</th>
                                    <th>Số lượng bán</th>
                                    <th>Doanh thu</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% for (ThongKeSanPham sp : topSanPham) {
                                    String hinhAnh = sp.getHinhAnh();
                                    if (hinhAnh == null || hinhAnh.trim().isEmpty() || "null".equalsIgnoreCase(hinhAnh.trim())) {
                                        hinhAnh = "no-image.png";
                                    }
                                %>
                                <tr>
                                    <td>#<%= sp.getIdThuoc() %></td>
                                    <td>
                                        <div class="product-cell">
                                            <img src="<%= request.getContextPath() %>/image?name=<%= hinhAnh %>" alt="Sản phẩm">
                                            <span><%= sp.getTenThuoc() %></span>
                                        </div>
                                    </td>
                                    <td><strong><%= sp.getTongSoLuongBan() %></strong></td>
                                    <td class="money"><%= CurrencyUtil.yen(sp.getTongDoanhThu()) %></td>
                                </tr>
                                <% } %>
                            </tbody>
                        </table>
                    </div>
                <% } %>
            </div>

            <div class="table-card">
                <div class="card-header">
                    <h2>Top khách hàng mua nhiều</h2>
                </div>

                <% if (topKhachHang.isEmpty()) { %>
                    <div class="empty-box">
                        <p>Chưa có dữ liệu khách hàng.</p>
                    </div>
                <% } else { %>
                    <div class="table-wrap">
                        <table class="report-table">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Khách hàng</th>
                                    <th>Số đơn</th>
                                    <th>Tổng chi tiêu</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% for (ThongKeKhachHang kh : topKhachHang) { %>
                                <tr>
                                    <td>#<%= kh.getIdNguoiDung() %></td>
                                    <td>
                                        <div class="user-cell">
                                            <strong><%= kh.getHoTen() == null ? "Khách hàng" : kh.getHoTen() %></strong>
                                            <span><%= kh.getEmail() == null ? "" : kh.getEmail() %></span>
                                        </div>
                                    </td>
                                    <td><strong><%= kh.getTongDonHang() %></strong></td>
                                    <td class="money"><%= CurrencyUtil.yen(kh.getTongChiTieu()) %></td>
                                </tr>
                                <% } %>
                            </tbody>
                        </table>
                    </div>
                <% } %>
            </div>
        </div>
    </div>
</div>

<script>
    function yenTick(value) {
        return '¥' + Number(value).toLocaleString('ja-JP');
    }

    new Chart(document.getElementById('revenueDayChart'), {
        type: 'bar',
        data: {
            labels: <%= labelsNgay.toString() %>,
            datasets: [{
                label: 'Doanh thu theo ngày',
                data: <%= valuesNgay.toString() %>,
                borderWidth: 1,
                borderRadius: 6,
                barThickness: 28,
                maxBarThickness: 32,
                categoryPercentage: 0.65,
                barPercentage: 0.75
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { display: true }
            },
            scales: {
                x: { offset: false },
                y: {
                    beginAtZero: true,
                    ticks: {
                        callback: function(value) {
                            return yenTick(value);
                        }
                    }
                }
            }
        }
    });

    new Chart(document.getElementById('profitDayChart'), {
        type: 'bar',
        data: {
            labels: <%= labelsNgay.toString() %>,
            datasets: [{
                label: 'Lợi nhuận theo ngày',
                data: <%= valuesLoiNhuanNgay.toString() %>,
                borderWidth: 1,
                borderRadius: 6,
                barThickness: 28,
                maxBarThickness: 32,
                categoryPercentage: 0.65,
                barPercentage: 0.75
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { display: true }
            },
            scales: {
                x: { offset: false },
                y: {
                    beginAtZero: true,
                    ticks: {
                        callback: function(value) {
                            return yenTick(value);
                        }
                    }
                }
            }
        }
    });

    new Chart(document.getElementById('statusChart'), {
        type: 'doughnut',
        data: {
            labels: ['Chờ xác nhận', 'Đang giao', 'Hoàn thành', 'Đã hủy'],
            datasets: [{
                data: [<%= choXacNhan %>, <%= dangGiao %>, <%= hoanThanh %>, <%= daHuy %>],
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false
        }
    });

    new Chart(document.getElementById('compareDayChart'), {
        type: 'line',
        data: {
            labels: <%= labelsNgay.toString() %>,
            datasets: [
                {
                    label: 'Doanh thu',
                    data: <%= valuesNgay.toString() %>,
                    borderWidth: 2,
                    tension: 0.3,
                    fill: false
                },
                {
                    label: 'Lợi nhuận',
                    data: <%= valuesLoiNhuanNgay.toString() %>,
                    borderWidth: 2,
                    tension: 0.3,
                    fill: false
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { display: true }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        callback: function(value) {
                            return yenTick(value);
                        }
                    }
                }
            }
        }
    });

    new Chart(document.getElementById('revenueWeekChart'), {
        type: 'bar',
        data: {
            labels: <%= labelsTuan.toString() %>,
            datasets: [{
                label: 'Doanh thu theo tuần',
                data: <%= valuesTuan.toString() %>,
                borderWidth: 1,
                borderRadius: 6,
                barThickness: 28,
                maxBarThickness: 32,
                categoryPercentage: 0.65,
                barPercentage: 0.75
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { display: true }
            },
            scales: {
                x: { offset: false },
                y: {
                    beginAtZero: true,
                    ticks: {
                        callback: function(value) {
                            return yenTick(value);
                        }
                    }
                }
            }
        }
    });

    new Chart(document.getElementById('profitWeekChart'), {
        type: 'bar',
        data: {
            labels: <%= labelsTuan.toString() %>,
            datasets: [{
                label: 'Lợi nhuận theo tuần',
                data: <%= valuesLoiNhuanTuan.toString() %>,
                borderWidth: 1,
                borderRadius: 6,
                barThickness: 28,
                maxBarThickness: 32,
                categoryPercentage: 0.65,
                barPercentage: 0.75
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { display: true }
            },
            scales: {
                x: { offset: false },
                y: {
                    beginAtZero: true,
                    ticks: {
                        callback: function(value) {
                            return yenTick(value);
                        }
                    }
                }
            }
        }
    });

    new Chart(document.getElementById('revenueMonthChart'), {
        type: 'line',
        data: {
            labels: <%= labelsThang.toString() %>,
            datasets: [{
                label: 'Doanh thu theo tháng',
                data: <%= valuesThang.toString() %>,
                borderWidth: 2,
                tension: 0.3,
                fill: false
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { display: true }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        callback: function(value) {
                            return yenTick(value);
                        }
                    }
                }
            }
        }
    });

    new Chart(document.getElementById('profitMonthChart'), {
        type: 'line',
        data: {
            labels: <%= labelsThang.toString() %>,
            datasets: [{
                label: 'Lợi nhuận theo tháng',
                data: <%= valuesLoiNhuanThang.toString() %>,
                borderWidth: 2,
                tension: 0.3,
                fill: false
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { display: true }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        callback: function(value) {
                            return yenTick(value);
                        }
                    }
                }
            }
        }
    });
</script>
</body>
</html>