<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="model.NguoiDung" %>
<%@ page import="model.ThongKeNgay" %>
<%@ page import="utils.CurrencyUtil" %>
<%
    NguoiDung user = (NguoiDung) session.getAttribute("user");
    String tenAdmin = (user != null) ? user.getHoTen() : "Admin";

    int tongThuoc = request.getAttribute("tongThuoc") != null ? (Integer) request.getAttribute("tongThuoc") : 0;
    int tongDonHang = request.getAttribute("tongDonHang") != null ? (Integer) request.getAttribute("tongDonHang") : 0;
    int tongNguoiDung = request.getAttribute("tongNguoiDung") != null ? (Integer) request.getAttribute("tongNguoiDung") : 0;
    int tongNhanVien = request.getAttribute("tongNhanVien") != null ? (Integer) request.getAttribute("tongNhanVien") : 0;

    int donChoXacNhan = request.getAttribute("donChoXacNhan") != null ? (Integer) request.getAttribute("donChoXacNhan") : 0;
    int thuocSapHet = request.getAttribute("thuocSapHet") != null ? (Integer) request.getAttribute("thuocSapHet") : 0;
    int thuocHetHang = request.getAttribute("thuocHetHang") != null ? (Integer) request.getAttribute("thuocHetHang") : 0;
    int donDaHuy = request.getAttribute("donDaHuy") != null ? (Integer) request.getAttribute("donDaHuy") : 0;

    double tongDoanhThu = request.getAttribute("tongDoanhThu") != null ? (Double) request.getAttribute("tongDoanhThu") : 0;
    double tongVonNhapHang = request.getAttribute("tongVonNhapHang") != null ? (Double) request.getAttribute("tongVonNhapHang") : 0;

    List<ThongKeNgay> doanhThuTheoThang = (List<ThongKeNgay>) request.getAttribute("doanhThuTheoThang");
    if (doanhThuTheoThang == null) doanhThuTheoThang = new ArrayList<>();

    Map<String, Double> mapThang = new LinkedHashMap<>();
    LocalDate currentMonth = LocalDate.now().withDayOfMonth(1).minusMonths(5);
    DateTimeFormatter monthFmt = DateTimeFormatter.ofPattern("yyyy-MM");

    for (int i = 0; i < 6; i++) {
        mapThang.put(currentMonth.format(monthFmt), 0.0);
        currentMonth = currentMonth.plusMonths(1);
    }

    for (ThongKeNgay item : doanhThuTheoThang) {
        if (item != null && item.getNhan() != null) {
            mapThang.put(item.getNhan(), CurrencyUtil.toJPY(item.getGiaTri()));
        }
    }

    StringBuilder labelsThang = new StringBuilder("[");
    StringBuilder valuesThang = new StringBuilder("[");
    int idx = 0;
    for (Map.Entry<String, Double> entry : mapThang.entrySet()) {
        labelsThang.append("'").append(entry.getKey()).append("'");
        valuesThang.append(entry.getValue());
        if (idx < mapThang.size() - 1) {
            labelsThang.append(",");
            valuesThang.append(",");
        }
        idx++;
    }
    labelsThang.append("]");
    valuesThang.append("]");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/dashboard.css?v=2001">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
    <div class="admin-layout">
        <jsp:include page="/jsp/admin/admin_sidebar.jsp" />

        <div class="admin-main">
            <div class="admin-topbar">
                <div>
                    <h1>Dashboard quản trị</h1>
                    <p>Chào mừng quay lại, <strong><%= tenAdmin %></strong></p>
                </div>

                <div class="topbar-right">
                    <div class="welcome-box">Xin chào, <strong><%= tenAdmin %></strong></div>
                    <a class="logout-btn" href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
                </div>
            </div>

            <div class="stats-grid">
                <div class="stat-card revenue">
                    <div class="stat-title">Tổng doanh thu</div>
                    <div class="stat-value"><%= CurrencyUtil.yen(tongDoanhThu) %></div>
                    <div class="stat-note up">Đơn đã giao và đã thanh toán</div>
                </div>

                <div class="stat-card">
                    <div class="stat-title">Tổng vốn nhập hàng</div>
                    <div class="stat-value"><%= CurrencyUtil.yen(tongVonNhapHang) %></div>
                    <div class="stat-note">Tổng giá gốc của toàn bộ hàng trong kho</div>
                </div>

                <div class="stat-card">
                    <div class="stat-title">Tổng đơn hàng</div>
                    <div class="stat-value"><%= tongDonHang %></div>
                    <div class="stat-note">Tất cả đơn trong hệ thống</div>
                </div>

                <div class="stat-card">
                    <div class="stat-title">Tổng thuốc</div>
                    <div class="stat-value"><%= tongThuoc %></div>
                    <div class="stat-note">Sản phẩm đang quản lý</div>
                </div>

                <div class="stat-card">
                    <div class="stat-title">Khách hàng</div>
                    <div class="stat-value"><%= tongNguoiDung %></div>
                    <div class="stat-note">Tài khoản người dùng</div>
                </div>

                <div class="stat-card">
                    <div class="stat-title">Nhân viên</div>
                    <div class="stat-value"><%= tongNhanVien %></div>
                    <div class="stat-note">Tài khoản staff</div>
                </div>

                <div class="stat-card alert-card">
                    <div class="stat-title">Đơn chờ xác nhận</div>
                    <div class="stat-value"><%= donChoXacNhan %></div>
                    <div class="stat-note danger">Cần xử lý sớm</div>
                </div>
            </div>

            <div class="dashboard-row">
                <div class="panel chart-panel">
                    <div class="panel-header">
                        <h3>Doanh thu theo tháng</h3>
                        <p>Biểu đồ doanh thu 6 tháng gần nhất của hệ thống</p>
                    </div>

                    <div class="chart-box">
                        <canvas id="monthRevenueChart"></canvas>
                    </div>
                </div>

                <div class="panel">
                    <div class="panel-header">
                        <h3>Cảnh báo hệ thống</h3>
                        <p>Bấm vào từng mục để xem danh sách chi tiết</p>
                    </div>

                    <div class="alert-list">
                        <a href="${pageContext.request.contextPath}/admin/donhang?trangThai=CHO_XAC_NHAN" class="alert-link">
                            <div class="alert-item">
                                <div>
                                    <strong>Đơn chờ xác nhận</strong>
                                    <span>Cần kiểm tra và xử lý kịp thời</span>
                                </div>
                                <b class="badge warning"><%= donChoXacNhan %></b>
                            </div>
                        </a>

                        <a href="${pageContext.request.contextPath}/admin/thuoc?trangThaiKho=SAP_HET" class="alert-link">
                            <div class="alert-item">
                                <div>
                                    <strong>Thuốc sắp hết</strong>
                                    <span>Bấm để xem danh sách thuốc còn từ 1 đến 10</span>
                                </div>
                                <b class="badge orange"><%= thuocSapHet %></b>
                            </div>
                        </a>

                        <a href="${pageContext.request.contextPath}/admin/thuoc?trangThaiKho=HET_HANG" class="alert-link">
                            <div class="alert-item">
                                <div>
                                    <strong>Thuốc hết hàng</strong>
                                    <span>Bấm để xem danh sách thuốc đã hết hàng</span>
                                </div>
                                <b class="badge danger"><%= thuocHetHang %></b>
                            </div>
                        </a>

                        <a href="${pageContext.request.contextPath}/admin/donhang?trangThai=DA_HUY" class="alert-link">
                            <div class="alert-item">
                                <div>
                                    <strong>Đơn đã hủy</strong>
                                    <span>Bấm để xem danh sách đơn hàng đã hủy</span>
                                </div>
                                <b class="badge dark"><%= donDaHuy %></b>
                            </div>
                        </a>
                    </div>
                </div>
            </div>

            <div class="dashboard-row single-row">
                <div class="panel">
                    <div class="panel-header">
                        <h3>Khu vực quản trị</h3>
                        <p>Truy cập nhanh các chức năng chính, không lặp lại nhiều khối giống nhau</p>
                    </div>

                    <div class="admin-menu-grid">
                        <a href="${pageContext.request.contextPath}/admin/thuoc" class="admin-menu-card">
                            <span class="menu-title">Quản lý thuốc</span>
                            <span class="menu-desc">Thêm, sửa, xóa và kiểm tra danh sách thuốc</span>
                        </a>

                        <a href="${pageContext.request.contextPath}/admin/danhmuc" class="admin-menu-card">
                            <span class="menu-title">Quản lý danh mục</span>
                            <span class="menu-desc">Sắp xếp và quản lý nhóm thuốc trong hệ thống</span>
                        </a>

                        <a href="${pageContext.request.contextPath}/admin/donhang" class="admin-menu-card">
                            <span class="menu-title">Quản lý đơn hàng</span>
                            <span class="menu-desc">Theo dõi đơn mới, đơn giao và tình trạng xử lý</span>
                        </a>

                        <a href="${pageContext.request.contextPath}/admin/nguoidung" class="admin-menu-card">
                            <span class="menu-title">Quản lý người dùng</span>
                            <span class="menu-desc">Xem danh sách khách hàng và trạng thái tài khoản</span>
                        </a>

                        <a href="${pageContext.request.contextPath}/admin/nhanvien" class="admin-menu-card">
                            <span class="menu-title">Quản lý nhân viên</span>
                            <span class="menu-desc">Quản lý tài khoản staff và phân quyền phù hợp</span>
                        </a>

                        <a href="${pageContext.request.contextPath}/admin/thongke" class="admin-menu-card highlight">
                            <span class="menu-title">Thống kê báo cáo</span>
                            <span class="menu-desc">Xem doanh thu, lợi nhuận và số liệu hoạt động</span>
                        </a>
                    </div>
                </div>
            </div>

        </div>
    </div>

    <script>
        new Chart(document.getElementById('monthRevenueChart'), {
            type: 'bar',
            data: {
                labels: <%= labelsThang.toString() %>,
                datasets: [{
                    label: 'Doanh thu theo tháng',
                    data: <%= valuesThang.toString() %>,
                    borderWidth: 1,
                    borderRadius: 10,
                    barThickness: 42,
                    maxBarThickness: 48,
                    categoryPercentage: 0.7,
                    barPercentage: 0.8
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        display: true
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            callback: function(value) {
                                return '¥' + Number(value).toLocaleString('ja-JP');
                            }
                        }
                    }
                }
            }
        });
    </script>
</body>
</html>