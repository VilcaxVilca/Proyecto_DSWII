
package pe.edu.cibertec.auth.service;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final SecretKey secretKey = Keys.hmacShaKeyFor(
        "BoticaVidaSanaClaveJWT2026Segura123456789".getBytes()
    );

    public String generarToken(String username, String rol) {
        long ahora = System.currentTimeMillis();

        return Jwts.builder()
                .subject(username)
                .claim("rol", rol)
                .issuedAt(new Date(ahora))
                .expiration(new Date(ahora + 1000 * 60 * 60))
                .signWith(secretKey)
                .compact();
    }

    public Claims validarToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
