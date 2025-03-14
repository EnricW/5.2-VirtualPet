package s05.virtualpet.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import s05.virtualpet.repository.UserRepository;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JwtUtil {

    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long accessTokenExpiration = 86400000; // 1 day
    private final long refreshTokenExpiration = 604800000; // 7 days

    private final ConcurrentHashMap<String, String> refreshTokens = new ConcurrentHashMap<>();
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;

    public JwtUtil(UserRepository userRepository, UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(String username) {
        String refreshToken = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(secretKey)
                .compact();
        refreshTokens.put(username, refreshToken);
        return refreshToken;
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            String username = extractUsername(token);
            if (!userRepository.findByUsername(username).isPresent()) {
                return false;
            }
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public boolean validateRefreshToken(String token) {
        try {
            String username = extractUsername(token);
            return refreshTokens.containsKey(username) && refreshTokens.get(username).equals(token);
        } catch (Exception e) {
            return false;
        }
    }

    public String refreshAccessToken(String refreshToken) {
        if (!validateRefreshToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }
        String username = extractUsername(refreshToken);
        return generateToken(username);
    }

    public void configureSecurity(HttpSecurity http, JwtFilter jwtFilter) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/pets/**").hasRole("USER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
