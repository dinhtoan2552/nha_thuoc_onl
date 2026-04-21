<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.TimeZone" %>
<%@ page import="model.HoTroPhien" %>
<%@ page import="model.HoTroTinNhan" %>

<%
    List<HoTroPhien> dsPhien = (List<HoTroPhien>) request.getAttribute("dsPhien");
    if (dsPhien == null) dsPhien = new ArrayList<>();

    HoTroPhien phienChon = (HoTroPhien) request.getAttribute("phienChon");
    List<HoTroTinNhan> dsTinNhan = (List<HoTroTinNhan>) request.getAttribute("dsTinNhan");
    if (dsTinNhan == null) dsTinNhan = new ArrayList<>();

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    sdf.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Hỗ trợ khách hàng</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/staff/dashboard.css?v=3">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/staff/hotro.css?v=8">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
</head>
<body>
<div class="staff-layout">
    <jsp:include page="/jsp/staff/staff_sidebar.jsp" />

    <div class="staff-main support-main">
        <div class="support-page-header">
            <div>
                <h1>Hỗ trợ khách hàng</h1>
                <p>Trao đổi trực tiếp với khách hàng và xử lý yêu cầu hỗ trợ nhanh chóng.</p>
            </div>

            <div class="support-header-actions">
                <% if (phienChon != null) { %>
                    <a href="${pageContext.request.contextPath}/staff/hotro?idPhien=<%= phienChon.getIdPhien() %>" class="refresh-btn">
                        <i class="fa-solid fa-rotate-right"></i>
                        <span>Làm mới</span>
                    </a>
                <% } else { %>
                    <a href="${pageContext.request.contextPath}/staff/hotro" class="refresh-btn">
                        <i class="fa-solid fa-rotate-right"></i>
                        <span>Làm mới</span>
                    </a>
                <% } %>
            </div>
        </div>

        <div class="support-board">
            <div class="support-list">
                <div class="support-title">
                    <h2><i class="fa-solid fa-headset"></i> Yêu cầu hỗ trợ</h2>
                    <p>Danh sách hội thoại với khách hàng</p>
                </div>

                <% if (dsPhien.isEmpty()) { %>
                    <div class="empty-ticket-list">
                        <i class="fa-regular fa-message"></i>
                        <p>Chưa có yêu cầu hỗ trợ nào.</p>
                    </div>
                <% } else { %>
                    <% for (HoTroPhien p : dsPhien) { %>
                        <%
                            boolean chuaDoc = false;
                            try {
                                chuaDoc = p.getSoTinChuaDoc() > 0;
                            } catch (Exception e) {
                                chuaDoc = false;
                            }
                        %>

                        <a class="support-ticket <%= (phienChon != null && phienChon.getIdPhien() == p.getIdPhien()) ? "active" : "" %> <%= chuaDoc ? "unread-ticket" : "" %>"
                           href="${pageContext.request.contextPath}/staff/hotro?idPhien=<%= p.getIdPhien() %>">
                            <div class="line-1">
                                <strong class="<%= chuaDoc ? "unread-text" : "" %>">
                                    <%= p.getTenKhachHang() %>
                                </strong>

                                <span class="<%= "DA_XU_LY".equals(p.getTrangThai()) ? "done" : "open" %>">
                                    <%= "DA_XU_LY".equals(p.getTrangThai()) ? "Đã xử lý" : "Đang mở" %>
                                </span>
                            </div>

                            <div class="ticket-last <%= chuaDoc ? "unread-text" : "" %>">
                                <%= (p.getTinNhanCuoi() == null || p.getTinNhanCuoi().trim().isEmpty())
                                        ? "Chưa có tin nhắn"
                                        : p.getTinNhanCuoi() %>
                            </div>

                            <% if (p.getThoiGianTinNhanCuoi() != null) { %>
                                <div class="ticket-time"><%= sdf.format(p.getThoiGianTinNhanCuoi()) %></div>
                            <% } %>
                        </a>
                    <% } %>
                <% } %>
            </div>

            <div class="support-chat">
                <% if (phienChon == null) { %>
                    <div class="empty-chat">
                        <i class="fa-solid fa-comments"></i>
                        <h3>Chọn cuộc trò chuyện</h3>
                        <p>Danh sách yêu cầu hỗ trợ nằm bên trên hoặc bên trái tùy thiết bị.</p>
                    </div>
                <% } else { %>
                    <div class="chat-topbar">
                        <div>
                            <h2><%= phienChon.getTenKhachHang() %></h2>
                            <p><%= phienChon.getTieuDe() %></p>
                        </div>

                        <form action="${pageContext.request.contextPath}/staff/hotro" method="post" class="status-form">
                            <input type="hidden" name="action" value="doiTrangThai">
                            <input type="hidden" name="idPhien" value="<%= phienChon.getIdPhien() %>">

                            <select name="trangThai">
                                <option value="DANG_MO" <%= "DANG_MO".equals(phienChon.getTrangThai()) ? "selected" : "" %>>Đang mở</option>
                                <option value="DA_XU_LY" <%= "DA_XU_LY".equals(phienChon.getTrangThai()) ? "selected" : "" %>>Đã xử lý</option>
                            </select>

                            <button type="submit">Cập nhật</button>
                        </form>
                    </div>

                    <div class="chat-messages" id="chatMessages">
                        <% for (HoTroTinNhan t : dsTinNhan) { %>
                            <div class="chat-row <%= "STAFF".equals(t.getLoaiNguoiGui()) ? "mine" : "customer" %>">
                                <div class="avatar">
                                    <% if ("STAFF".equals(t.getLoaiNguoiGui())) { %>
                                        <i class="fa-solid fa-headset"></i>
                                    <% } else { %>
                                        <i class="fa-solid fa-user"></i>
                                    <% } %>
                                </div>

                                <div class="bubble">
                                    <div class="meta">
                                        <strong><%= t.getTenNguoiGui() %></strong>
                                        <span><%= sdf.format(t.getNgayGui()) %></span>
                                    </div>

                                    <% if (t.getNoiDung() != null && !t.getNoiDung().trim().isEmpty()) { %>
                                        <div class="text"><%= t.getNoiDung() %></div>
                                    <% } %>

                                    <% if (t.getAnhDinhKem() != null && !t.getAnhDinhKem().trim().isEmpty()) { %>
                                        <div class="chat-image-wrap">
                                            <a href="${pageContext.request.contextPath}/load-image?name=<%= t.getAnhDinhKem() %>" target="_blank">
                                                <img src="${pageContext.request.contextPath}/load-image?name=<%= t.getAnhDinhKem() %>" class="chat-image" alt="Ảnh đính kèm">
                                            </a>
                                        </div>
                                    <% } %>
                                </div>
                            </div>
                        <% } %>
                    </div>

                    <% if (!"DA_XU_LY".equals(phienChon.getTrangThai())) { %>
                        <form class="chat-form" action="${pageContext.request.contextPath}/staff/hotro" method="post" enctype="multipart/form-data">
                            <input type="hidden" name="action" value="guiTinNhan">
                            <input type="hidden" name="idPhien" value="<%= phienChon.getIdPhien() %>">

                            <form class="chat-form" action="${pageContext.request.contextPath}/staff/hotro" method="post" enctype="multipart/form-data">
    <input type="hidden" name="action" value="guiTinNhan">
    <input type="hidden" name="idPhien" value="<%= phienChon.getIdPhien() %>">

    <label class="attach-btn" for="anhDinhKemStaff" title="Gửi ảnh">
        <i class="fa-solid fa-image"></i>
    </label>

    <input
        type="file"
        id="anhDinhKemStaff"
        name="anhDinhKem"
        accept=".jpg,.jpeg,.png,.webp,image/*"
        class="hidden-file"
    >

    <input type="text" id="staffMessageInput" name="noiDung" placeholder="Nhập phản hồi cho khách..." autocomplete="off">

    <button type="submit" title="Gửi tin nhắn">
        <i class="fa-solid fa-paper-plane"></i>
    </button>
</form>

<div id="staffTypingIndicator" class="typing-indicator">
    Đang nhập<span></span><span></span><span></span>
</div>

<div id="staffSelectedFileName" class="selected-file-name"></div>
                    <% } else { %>
                        <div class="closed-box">Phiên hỗ trợ này đã được đánh dấu đã xử lý.</div>
                    <% } %>
                <% } %>
            </div>
        </div>
    </div>
</div>

<script>
    const chatBox = document.getElementById("chatMessages");
    if (chatBox) {
        chatBox.scrollTop = chatBox.scrollHeight;
    }

    const staffFileInput = document.getElementById("anhDinhKemStaff");
    const staffFileNameBox = document.getElementById("staffSelectedFileName");

    if (staffFileInput && staffFileNameBox) {
        staffFileInput.addEventListener("change", function () {
            if (this.files && this.files.length > 0) {
                staffFileNameBox.textContent = "Đã chọn: " + this.files[0].name;
            } else {
                staffFileNameBox.textContent = "";
            }
        });
    }
    const staffMessageInput = document.getElementById("staffMessageInput");
const staffTypingIndicator = document.getElementById("staffTypingIndicator");
let staffTypingTimeout;

if (staffMessageInput && staffTypingIndicator) {
    staffMessageInput.addEventListener("input", function () {
        const value = this.value.trim();

        clearTimeout(staffTypingTimeout);

        if (value.length > 0) {
            staffTypingIndicator.classList.add("show");
        } else {
            staffTypingIndicator.classList.remove("show");
        }

        staffTypingTimeout = setTimeout(() => {
            staffTypingIndicator.classList.remove("show");
        }, 800);
    });
} 
</script>
</body>
</html>