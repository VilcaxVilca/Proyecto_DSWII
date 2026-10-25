
package pe.edu.cibertec.producto.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import pe.edu.cibertec.producto.security.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // Permitir acceso al WSDL y al servicio SOAP
                .requestMatchers(
                    "/ws",
                    "/ws/**",
                    "/productos.wsdl",
                    "/productos.xsd"
                ).permitAll()

                // Consultas públicas REST
                .requestMatchers(HttpMethod.GET,
                    "/api/productos", "/api/productos/**",
                    "/api/categorias", "/api/categorias/**",
                    "/api/proveedores", "/api/proveedores/**"
                ).permitAll()

                // Solo ADMINISTRADOR modifica productos
                .requestMatchers(HttpMethod.POST,
                    "/api/productos", "/api/productos/**")
                    .hasRole("ADMINISTRADOR")
                .requestMatchers(HttpMethod.PUT,
                    "/api/productos", "/api/productos/**")
                    .hasRole("ADMINISTRADOR")
                .requestMatchers(HttpMethod.DELETE,
                    "/api/productos", "/api/productos/**")
                    .hasRole("ADMINISTRADOR")

                .anyRequest().authenticated()
            )
            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}
