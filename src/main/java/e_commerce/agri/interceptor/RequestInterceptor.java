package e_commerce.agri.interceptor;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RequestInterceptor implements HandlerInterceptor {

    public Long sessionTime;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        final List<String> uris = List.of(
                "/customer/signup",
                "/customer/login",
                "/customer/verify-otp",
                "/product/all-products",
                "/farmer/signup",
                "/farmer/login");
        String uri = request.getRequestURI();
        if (uris.contains(uri))return true;
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("email") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Session Expired or Unauthorized Login Again");
            return false;
        }
        return true;
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {

    }

}