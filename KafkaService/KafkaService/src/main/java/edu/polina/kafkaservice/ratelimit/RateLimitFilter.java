package edu.polina.kafkaservice.ratelimit;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {

    private final FixedWindowRateLimiter fixedWindowRateLimiter;

    @Value("${rate-limit.limit}")
    private int limit;

    @Value("${rate-limit.window}")
    private Duration windowSize;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain
    ) throws ServletException, IOException {
        log.info("RequestLimitFilter = " + request.getMethod() + " " + request.getRequestURI());
        if (!(request.getMethod().equals("POST") && request.getRequestURI().equals("/users"))) {
            filterChain.doFilter(request, response);
            return;
        }
        String client = Optional.ofNullable(request.getHeader("X-API-KEY"))
                .filter(s -> !s.isBlank())
                .orElseGet(() -> Optional.ofNullable(request.getRemoteAddr()).orElse("unknown"));
        log.info("RateLimit settings: limit={}, window={}", limit, windowSize);
        boolean allowed = fixedWindowRateLimiter.allowRequest(
                client,
                limit,
                windowSize
        );
        if (!allowed) {
            response.setStatus(429);
            response.getWriter().write("Лимит запросов превышен, повторите позднее");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
