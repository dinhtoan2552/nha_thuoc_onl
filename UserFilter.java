package filter;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.NguoiDung;

//@WebFilter("/user/*")
public class UserFilter extends HttpFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        String path = uri.substring(contextPath.length());

        boolean laTrangCongKhai =
                "/user/home".equals(path);

        if (laTrangCongKhai) {
            chain.doFilter(request, response);
            return;
        }

        if (session == null) {
            res.sendRedirect(contextPath + "/login");
            return;
        }

        Object sessionUser = session.getAttribute("user");
        if (!(sessionUser instanceof NguoiDung)) {
            session.invalidate();
            res.sendRedirect(contextPath + "/login");
            return;
        }

        NguoiDung user = (NguoiDung) sessionUser;
        String vaiTro = user.getVaiTro() != null ? user.getVaiTro().trim().toUpperCase() : "";
        String trangThai = user.getTrangThai() != null ? user.getTrangThai().trim().toUpperCase() : "";

        if (!"HOAT_DONG".equals(trangThai)) {
            session.invalidate();
            res.sendRedirect(contextPath + "/login");
            return;
        }

        if (!"USER".equals(vaiTro)) {
            res.sendRedirect(contextPath + "/login");
            return;
        }

        chain.doFilter(request, response);
    }
}
