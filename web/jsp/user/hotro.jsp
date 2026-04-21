<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.TimeZone" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="model.HoTroPhien" %>
<%@ page import="model.HoTroTinNhan" %>
<%@ page import="utils.InputSanitizer" %>

<%
    List<HoTroPhien> dsPhien = (List<HoTroPhien>) request.getAttribute("dsPhien");
    if (dsPhien == null) dsPhien = new ArrayList<>();

    HoTroPhien phienChon = (HoTroPhien) request.getAttribute("phienChon");
    List<HoTroTinNhan> dsTinNhan = (List<HoTroTinNhan>) request.getAttribute("dsTinNhan");
    if (dsTinNhan == null) dsTinNhan = new ArrayList<>();

    String supportMessage = (String) session.getAttribute("supportMessage");
    if (supportMessage != null) session.removeAttribute("supportMessage");

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    sdf.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hỗ trợ khách hàng</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/hotro.css?v=9">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
</head>
<body>
<div class="support-layout">
    <div class="support-sidebar">
        <div class="support-header">
            <h2><i class="fa-solid fa-headset"></i> Hỗ trợ khách hàng</h2>
            <p>Trao đổi trực tiếp với nhà thuốc</p>
        </div>

        <% if (supportMessage != null) { %>
            <div class="message-box"><%= InputSanitizer.escapeHtml(supportMessage) %></div>
        <% } %>

        <div class="new-ticket-card">
            <h3>Tạo yêu cầu mới</h3>
            <form action="${pageContext.request.contextPath}/ho-tro" method="post">
                <input type="hidden" name="csrfToken" value="${sessionScope.csrf_token}">
                <input type="hidden" name="action" value="taoPhien">
                <input type="text" name="tieuDe" placeholder="Tiêu đề hỗ trợ" required>
                <textarea name="noiDung" placeholder="Nhập nội dung cần hỗ trợ..." required></textarea>
                <button type="submit">Gửi hỗ trợ</button>
            </form>
        </div>

        <div class="ticket-list">
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

                        String tieuDePhien = (p.getTieuDe() == null || p.getTieuDe().trim().isEmpty())
                                ? "Yêu cầu hỗ trợ"
                                : p.getTieuDe();

                        String tinNhanCuoi = (p.getTinNhanCuoi() == null || p.getTinNhanCuoi().trim().isEmpty())
                                ? "Chưa có tin nhắn"
                                : p.getTinNhanCuoi();
                    %>

                    <a class="ticket-item <%= (phienChon != null && phienChon.getIdPhien() == p.getIdPhien()) ? "active" : "" %> <%= chuaDoc ? "unread-ticket" : "" %>"
                       href="${pageContext.request.contextPath}/ho-tro?idPhien=<%= p.getIdPhien() %>">
                        <div class="ticket-top">
                            <strong class="<%= chuaDoc ? "unread-text" : "" %>">
                                <%= InputSanitizer.escapeHtml(tieuDePhien) %>
                            </strong>

                            <span class="<%= "DA_XU_LY".equals(p.getTrangThai()) ? "done" : "open" %>">
                                <%= "DA_XU_LY".equals(p.getTrangThai()) ? "Đã xử lý" : "Đang mở" %>
                            </span>
                        </div>

                        <p class="<%= chuaDoc ? "unread-text" : "" %>">
                            <%= InputSanitizer.escapeHtml(tinNhanCuoi) %>
                        </p>

                        <% if (p.getThoiGianTinNhanCuoi() != null) { %>
                            <div class="ticket-time"><%= sdf.format(p.getThoiGianTinNhanCuoi()) %></div>
                        <% } %>
                    </a>
                <% } %>
            <% } %>
        </div>
    </div>

    <div class="chat-area">
        <% if (phienChon == null) { %>
            <div class="chat-topbar empty-topbar">
                <div class="chat-topbar-left">
                    <h2>Hỗ trợ khách hàng</h2>
                    <p>Chọn cuộc hội thoại hoặc tạo yêu cầu mới.</p>
                </div>

                <div class="chat-topbar-actions">
                    <a href="${pageContext.request.contextPath}/user/home" class="btn-back-home">
                        <i class="fa-solid fa-arrow-left"></i>
                        <span>Quay lại trang chủ</span>
                    </a>
                </div>
            </div>

            <div class="empty-chat">
                <i class="fa-solid fa-comments"></i>
                <h3>Chưa chọn phiên hỗ trợ</h3>
                <p>Hãy tạo yêu cầu mới hoặc chọn cuộc hội thoại bên trái.</p>
            </div>
        <% } else { %>
            <div class="chat-topbar">
                <div class="chat-topbar-left">
                    <h2><%= InputSanitizer.escapeHtml(phienChon.getTieuDe()) %></h2>
                    <p>
                        Trạng thái:
                        <strong><%= "DA_XU_LY".equals(phienChon.getTrangThai()) ? "Đã xử lý" : "Đang mở" %></strong>
                    </p>
                </div>

                <div class="chat-topbar-actions">
                    <a href="${pageContext.request.contextPath}/user/home" class="btn-back-home">
                        <i class="fa-solid fa-arrow-left"></i>
                        <span>Quay lại trang chủ</span>
                    </a>

                    <a href="${pageContext.request.contextPath}/ho-tro?idPhien=<%= phienChon.getIdPhien() %>" class="refresh-btn">
                        <i class="fa-solid fa-rotate-right"></i>
                        <span>Làm mới</span>
                    </a>
                </div>
            </div>

            <div class="chat-messages" id="chatMessages">
                <% for (HoTroTinNhan t : dsTinNhan) { %>
                    <%
                        String tenNguoiGui = InputSanitizer.escapeHtml(t.getTenNguoiGui());
                        String noiDungTinNhan = InputSanitizer.escapeHtml(t.getNoiDung());
                        String anhDinhKem = t.getAnhDinhKem();
                        String anhDinhKemUrl = "";

                        if (anhDinhKem != null && !anhDinhKem.trim().isEmpty()) {
                            anhDinhKemUrl = request.getContextPath() + "/load-image?name=" + URLEncoder.encode(anhDinhKem, "UTF-8");
                        }
                    %>
                    <div class="chat-row <%= "USER".equals(t.getLoaiNguoiGui()) ? "mine" : "support" %>">
                        <div class="avatar">
                            <% if ("USER".equals(t.getLoaiNguoiGui())) { %>
                                <i class="fa-solid fa-user"></i>
                            <% } else { %>
                                <i class="fa-solid fa-headset"></i>
                            <% } %>
                        </div>
                        <div class="bubble">
                            <div class="meta">
                                <strong><%= tenNguoiGui %></strong>
                                <span><%= sdf.format(t.getNgayGui()) %></span>
                            </div>

                            <% if (t.getNoiDung() != null && !t.getNoiDung().trim().isEmpty()) { %>
                                <div class="text"><%= noiDungTinNhan %></div>
                            <% } %>

                            <% if (anhDinhKem != null && !anhDinhKem.trim().isEmpty()) { %>
                                <div class="chat-image-wrap">
                                    <a href="<%= anhDinhKemUrl %>" target="_blank">
                                        <img src="<%= anhDinhKemUrl %>" class="chat-image" alt="Ảnh đính kèm">
                                    </a>
                                </div>
                            <% } %>
                        </div>
                    </div>
                <% } %>
            </div>

            <% if (!"DA_XU_LY".equals(phienChon.getTrangThai())) { %>
                <form class="chat-form" action="${pageContext.request.contextPath}/ho-tro" method="post" enctype="multipart/form-data">
                    <input type="hidden" name="csrfToken" value="${sessionScope.csrf_token}">
                    <input type="hidden" name="action" value="guiTinNhan">
                    <input type="hidden" name="idPhien" value="<%= phienChon.getIdPhien() %>">

                    <label class="attach-btn" for="anhDinhKemUser" title="Gửi ảnh">
                        <i class="fa-solid fa-image"></i>
                    </label>

                    <input
                        type="file"
                        id="anhDinhKemUser"
                        name="anhDinhKem"
                        accept=".jpg,.jpeg,.png,.webp,image/*"
                        class="hidden-file"
                    >

                    <input type="text" id="userMessageInput" name="noiDung" placeholder="Nhập tin nhắn..." autocomplete="off">

                    <button type="submit">
                        <i class="fa-solid fa-paper-plane"></i>
                    </button>
                </form>

                <div id="userTypingIndicator" class="typing-indicator">
                    Đang nhập<span></span><span></span><span></span>
                </div>

                <div id="userSelectedFileName" class="selected-file-name"></div>
            <% } else { %>
                <div class="closed-box">Phiên hỗ trợ này đã được đóng.</div>
            <% } %>
        <% } %>
    </div>
</div>

<script>
    const box = document.getElementById("chatMessages");
    if (box) {
        box.scrollTop = box.scrollHeight;
    }

    const userFileInput = document.getElementById("anhDinhKemUser");
    const userFileNameBox = document.getElementById("userSelectedFileName");

    if (userFileInput && userFileNameBox) {
        userFileInput.addEventListener("change", function () {
            if (this.files && this.files.length > 0) {
                userFileNameBox.textContent = "Đã chọn: " + this.files[0].name;
            } else {
                userFileNameBox.textContent = "";
            }
        });
    }

    const userMessageInput = document.getElementById("userMessageInput");
    const userTypingIndicator = document.getElementById("userTypingIndicator");
    let userTypingTimeout;

    if (userMessageInput && userTypingIndicator) {
        userMessageInput.addEventListener("input", function () {
            const value = this.value.trim();

            clearTimeout(userTypingTimeout);

            if (value.length > 0) {
                userTypingIndicator.classList.add("show");
            } else {
                userTypingIndicator.classList.remove("show");
            }

            userTypingTimeout = setTimeout(() => {
                userTypingIndicator.classList.remove("show");
            }, 800);
        });
    }
</script>
</body>
</html>