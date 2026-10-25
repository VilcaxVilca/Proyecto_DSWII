
package edu.pe.cibertec.botica.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        if (authorization != null
                && authorization.startsWith("Bearer ")
                && SecurityContextHolder.getContext()
                        .getAuthentication() == null) {

            String token = authorization.substring(7);

            try {
                Claims claims = jwtService.validarToken(token);

                String username = claims.getSubject();
                String rol = claims.get("rol", String.class);

                if (username != null && !username.isBlank()
                        && rol != null && !rol.isBlank()) {

                    if (rol.startsWith("ROLE_")) {
                        rol = rol.substring(5);
                    }

                    var authorities = List.of(
                        new SimpleGrantedAuthority("ROLE_" + rol)
                    );

                    var authentication =
                        new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            authorities
                        );

                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);
                }

            } catch (io.jsonwebtoken.JwtException
                    | IllegalArgumentException ex) {

                SecurityContextHolder.clearContext();

                response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
                );
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(
                    "{\"mensaje\":\"Token JWT inválido o expirado\"}"
                );
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
