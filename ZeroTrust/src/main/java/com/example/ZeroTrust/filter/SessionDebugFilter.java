package com.example.ZeroTrust.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;

@Component
public class SessionDebugFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session != null) {
            System.out.println("🔍 [DEBUG] 요청 경로: " + request.getRequestURI());
            System.out.println("🔍 [DEBUG] 세션 ID: " + session.getId());
            Enumeration<String> attrs = session.getAttributeNames();
            while (attrs.hasMoreElements()) {
                String key = attrs.nextElement();
                Object value = session.getAttribute(key);
                System.out.println("    • " + key + " = " + value);
            }
        }
        filterChain.doFilter(request, response);
    }
}
