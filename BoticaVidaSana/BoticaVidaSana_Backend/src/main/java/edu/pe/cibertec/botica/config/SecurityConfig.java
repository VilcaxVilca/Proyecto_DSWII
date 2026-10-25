
package edu.pe.cibertec.botica.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import edu.pe.cibertec.botica.security.CustomUserDetailsService;
import edu.pe.cibertec.botica.security.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            CustomUserDetailsService userDetailsService,
            JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(
                List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            SecurityContextRepository securityContextRepository)
            throws Exception {

        http
            .authenticationProvider(authenticationProvider())

            // Validación del JWT recibido desde Angular
            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            )

            .cors(cors -> cors
                .configurationSource(corsConfigurationSource()))

            .securityContext(context -> context
                .securityContextRepository(securityContextRepository)
                .requireExplicitSave(false))

            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))

            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**", "/ws", "/ws/**"))

            .authorizeHttpRequests(auth -> auth

                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // Login y SOAP
                .requestMatchers(
                    "/api/auth/login",
                    "/ws",
                    "/ws/**"
                ).permitAll()

                // Consultas públicas
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/productos", "/api/productos/**",
                    "/api/categorias", "/api/categorias/**",
                    "/api/proveedores", "/api/proveedores/**"
                ).permitAll()

                // Crear productos
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/productos", "/api/productos/**"
                ).hasAnyRole("ADMINISTRADOR", "FARMACEUTICO")

                // Editar productos
                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/productos", "/api/productos/**"
                ).hasAnyRole("ADMINISTRADOR", "FARMACEUTICO")

                // Eliminar productos
                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/productos", "/api/productos/**"
                ).hasRole("ADMINISTRADOR")

                // Reportes de ventas
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/ventas/reporte"
                ).hasAnyRole("ADMINISTRADOR", "FARMACEUTICO")

                // Consultar ventas
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/ventas", "/api/ventas/**"
                ).hasAnyRole("ADMINISTRADOR", "FARMACEUTICO", "CAJERO")

                // Registrar ventas
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/ventas"
                ).hasAnyRole("ADMINISTRADOR", "FARMACEUTICO", "CAJERO")

                // Anular ventas
                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/ventas/*/anular"
                ).hasAnyRole("ADMINISTRADOR", "FARMACEUTICO")

                .anyRequest().authenticated()
            )

            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())
            .logout(logout -> logout.disable());

        return http.build();
    }
}
