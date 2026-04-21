package filter;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.NguoiDung;

@WebFilter("/admin/*")
public class AdminFilter extends HttpFilter implements Filter {

    @Override
    public void init(FilterConfig fConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        if (session == null) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Object sessionUser = session.getAttribute("user");
        if (!(sessionUser instanceof NguoiDung)) {
            session.invalidate();
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        NguoiDung user = (NguoiDung) sessionUser;
        String vaiTro = user.getVaiTro() != null ? user.getVaiTro().trim().toUpperCase() : "";
        String trangThai = user.getTrangThai() != null ? user.getTrangThai().trim().toUpperCase() : "";

        if (!"HOAT_DONG".equals(trangThai)) {
            session.invalidate();
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if (!"ADMIN".equals(vaiTro)) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
