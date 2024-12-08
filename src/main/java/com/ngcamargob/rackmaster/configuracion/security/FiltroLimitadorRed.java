package com.ngcamargob.rackmaster.configuracion.security;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.ngcamargob.rackmaster.utilidades.AppUtil;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
public class FiltroLimitadorRed implements Filter {

    private final static AppUtil appUtil = new AppUtil();
    private final static Logger LOGGER = LoggerFactory.getLogger(FiltroLimitadorRed.class);

    private static final long BLOCK_DURATION_MINUTES = 1; // Bloqueo de 1 minuto
    private static final int MAX_REQUESTS_PER_MINUTE = 250; // Máximo de solicitudes por minuto

    private final Cache<String, Bucket> userBuckets = Caffeine.newBuilder()
            .expireAfterAccess(5, TimeUnit.MINUTES) // Expira si no hay acceso en 10 minutos
            .build();

    private final Cache<String, Long> blockedUsers = Caffeine.newBuilder()
            .expireAfterWrite(BLOCK_DURATION_MINUTES, TimeUnit.MINUTES) // Bloqueo de 1 minuto
            .build();

    private Bucket createNewBucket() {
        return Bucket.builder()
                .addLimit(Bandwidth.classic(MAX_REQUESTS_PER_MINUTE, Refill.greedy(MAX_REQUESTS_PER_MINUTE, Duration.ofMinutes(1))))
                .build();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String userId = getUserId(httpRequest);
        // Verificar si el usuario está bloqueado
        Long blockEndTime = blockedUsers.getIfPresent(userId);
        if (blockEndTime != null && System.currentTimeMillis() < blockEndTime) {
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpResponse.getWriter().write("{\"error\": \"Se superó el límite de solicitudes. Vuelve dentro de un 1 minuto.\"}");
            return;
        }

        // Obtener el bucket del usuario
        Bucket bucket = userBuckets.get(userId, k -> createNewBucket());

        // Verificar si hay tokens disponibles en el bucket
        if (bucket.tryConsume(1)) {
            // LOGGER.info("Solicitud permitida para usuario: {}", userId);
            chain.doFilter(request, response); // Continuar si tiene tokens disponibles
        } else {
            // Si no tiene tokens, bloquear al usuario
            LOGGER.info("Límite de solicitudes excedido por el usuario: {}", userId);
            blockedUsers.put(userId, System.currentTimeMillis() + Duration.ofMinutes(BLOCK_DURATION_MINUTES).toMillis());
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpResponse.getWriter().write("{\"error\": \"Demasiadas solicitudes. Vuelve dentro de 1 minuto.\"}");
        }
    }

    private String getUserId(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        } else {
            return request.getSession().getId();
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

}