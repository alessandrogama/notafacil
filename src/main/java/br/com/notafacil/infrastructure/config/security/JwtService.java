package br.com.notafacil.infrastructure.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    private final SecretKey chave;
    private final long minutosExpiracaoAccessToken;
    private final long diasExpiracaoRefreshToken;

    public JwtService(@Value("${notafacil.jwt.secret}") String secret,
                      @Value("${notafacil.jwt.access-token-expiration-minutes}") long minutosExpiracaoAccessToken,
                      @Value("${notafacil.jwt.refresh-token-expiration-days}") long diasExpiracaoRefreshToken) {
        this.chave = Keys.hmacShaKeyFor(secret.getBytes());
        this.minutosExpiracaoAccessToken = minutosExpiracaoAccessToken;
        this.diasExpiracaoRefreshToken = diasExpiracaoRefreshToken;
    }

    public String gerarAccessToken(UserDetails usuario) {
        return construirToken(usuario.getUsername(), Instant.now().plus(minutosExpiracaoAccessToken, ChronoUnit.MINUTES), "access");
    }

    public String gerarRefreshToken(UserDetails usuario) {
        return construirToken(usuario.getUsername(), Instant.now().plus(diasExpiracaoRefreshToken, ChronoUnit.DAYS), "refresh");
    }

    private String construirToken(String subject, Instant expiracao, String tipo) {
        return Jwts.builder()
                .subject(subject)
                .claim("tipo", tipo)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(expiracao))
                .signWith(chave)
                .compact();
    }

    public String extrairEmail(String token) {
        return extrairClaim(token, Claims::getSubject);
    }

    public String extrairTipo(String token) {
        return extrairClaim(token, claims -> claims.get("tipo", String.class));
    }

    public boolean tokenValido(String token, UserDetails usuario) {
        var email = extrairEmail(token);
        return email.equals(usuario.getUsername()) && !tokenExpirado(token);
    }

    public boolean ehRefreshToken(String token) {
        return "refresh".equals(extrairTipo(token));
    }

    private boolean tokenExpirado(String token) {
        return extrairClaim(token, Claims::getExpiration).before(new Date());
    }

    private <T> T extrairClaim(String token, Function<Claims, T> resolver) {
        var claims = Jwts.parser()
                .verifyWith(chave)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return resolver.apply(claims);
    }
}