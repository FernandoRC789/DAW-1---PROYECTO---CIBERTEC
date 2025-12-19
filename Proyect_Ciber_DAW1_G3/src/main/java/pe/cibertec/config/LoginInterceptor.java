package pe.cibertec.config;

import jakarta.servlet.http.*;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) {
        System.out.println("➡ " + req.getMethod() + " " + req.getRequestURI());
        return true;
    }
}

