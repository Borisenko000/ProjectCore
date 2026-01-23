package edu.polina.kafkaservice.infrastructure.redis.ratelimit;

import edu.polina.kafkaservice.infrastructure.metric.CustomMetricService;
import edu.polina.kafkaservice.properties.AppProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
@Slf4j
public class RateLimitFilter extends OncePerRequestFilter {

    private final CustomMetricService customMetricService;
    private final FixedWindowRateLimiter fixedWindowRateLimiter;
    private final AppProperties props;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain
    ) throws ServletException, IOException {
        if (!(request.getMethod().equals("POST") && request.getRequestURI().equals("/users"))) {
            filterChain.doFilter(request, response);
            return;
        }
        String client = Optional.ofNullable(request.getHeader("X-API-KEY"))
                .filter(s -> !s.isBlank())
                .orElseGet(() -> Optional.ofNullable(request.getRemoteAddr()).orElse("unknown"));
       // log.info("RateLimit settings: limit={}, window={}", props.getRateLimit(), props.getWindow());
        boolean allowed = fixedWindowRateLimiter.allowRequest(
                client,
                props.getRateLimit(),
                props.getWindow()
        );
        if (!allowed) {
            response.setStatus(429);
            response.getWriter().write("Лимит запросов превышен, повторите позднее");
            return;
        }
        customMetricService.rateLimited(response);
        filterChain.doFilter(request, response);
    }
}
