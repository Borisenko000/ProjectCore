package edu.polina.kafkaservice.infrastructure.mdc;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class MdcFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        if (!(request.getMethod().equals("POST") && request.getRequestURI().equals("/users"))) {
            chain.doFilter(request, response);
            return;
        }
        try {
            String correlationId = request.getHeader("X-Correlation-Id");
            if (correlationId == null) {
                MDC.put("correlationId", "empty");
                chain.doFilter(request, response);
                return;
            }
            MDC.put("correlationId", correlationId);
            chain.doFilter(request, response);
        } finally {
            MDC.remove("correlationId");
        }
    }
}
