
package pe.edu.cibertec.auth.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.cibertec.auth.dto.LoginRequest;
import pe.edu.cibertec.auth.entity.Usuario;
import pe.edu.cibertec.auth.service.AuthService;
import pe.edu.cibertec.auth.service.JwtService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthRestController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthRestController(
            AuthService authService,
            JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Usuario usuario = authService.autenticar(
                    request.getUsername(),
                    request.getPassword());

            String token = jwtService.generarToken(
                    usuario.getUsername(),
                    usuario.getRol());

            return ResponseEntity.ok(Map.of(
                    "autenticado", true,
                    "username", usuario.getUsername(),
                    "rol", usuario.getRol(),
                    "token", token,
                    "mensaje", "Login correcto."
            ));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "autenticado", false,
                            "mensaje", e.getMessage()
                    ));
        }
    }
}
