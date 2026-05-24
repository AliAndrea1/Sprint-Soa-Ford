package com.autoinsight.autoinsight_api.security;

import com.autoinsight.autoinsight_api.model.AuditLog;
import com.autoinsight.autoinsight_api.repository.AuditLogRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuditLogFilter extends OncePerRequestFilter {

    private final AuditLogRepository auditLogRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        ContentCachingResponseWrapper responseWrapper =
                new ContentCachingResponseWrapper(response);

        filterChain.doFilter(request, responseWrapper);

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = (auth != null && auth.isAuthenticated()
                    && !auth.getPrincipal().equals("anonymousUser"))
                    ? auth.getName() : "anonymous";

            String endpoint = request.getRequestURI();
            String action = request.getMethod();
            String ip = request.getRemoteAddr();
            int status = responseWrapper.getStatus();

            if (endpoint.startsWith("/api/")) {
                AuditLog auditLog = AuditLog.builder()
                        .username(username)
                        .action(action)
                        .endpoint(endpoint)
                        .ipAddress(ip)
                        .statusCode(status)
                        .build();

                auditLogRepository.save(auditLog);

                if (status == 401 || status == 403) {
                    log.warn("[AUDIT] Acesso negado — user={} method={} endpoint={} ip={} status={}",
                            username, action, endpoint, ip, status);
                } else {
                    log.info("[AUDIT] user={} method={} endpoint={} ip={} status={}",
                            username, action, endpoint, ip, status);
                }
            }

        } catch (Exception e) {
            log.error("[AUDIT] Erro ao salvar log: {}", e.getMessage());
        }

        responseWrapper.copyBodyToResponse();
    }
}
