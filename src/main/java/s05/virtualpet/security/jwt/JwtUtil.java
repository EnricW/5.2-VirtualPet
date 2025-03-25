package s05.virtualpet.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import s05.virtualpet.exception.custom.InvalidJwtTokenException;
import s05.virtualpet.repository.UserRepository;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final long accessTokenExpiration = 86400000; // 1 day

    private final UserRepository userRepository;

    public JwtUtil(@Value("${jwt.secret}") String secret, UserRepository userRepository) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.userRepository = userRepository;
    }

    public String generateToken(String username, String role) {
        log.info("Generating token for user '{}', role '{}'", username, role);

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(secretKey)
                .compact();
    }

    public String extractUsername(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (JwtException e) {
            throw new InvalidJwtTokenException("Invalid or expired JWT token.");
        }
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = extractClaims(token);
            boolean valid = !claims.getExpiration().before(new Date());
            log.debug("Token validation result: {}", valid);
            return valid;
        } catch (JwtException e) {
            throw new InvalidJwtTokenException("Invalid or expired JWT token.");
        }
    }

    public List<GrantedAuthority> getAuthoritiesFromToken(String token) {
        Claims claims = extractClaims(token);
        String role = claims.get("role", String.class);
        return List.of(new SimpleGrantedAuthority(role));
    }
}