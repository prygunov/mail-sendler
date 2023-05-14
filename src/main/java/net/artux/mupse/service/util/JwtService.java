package net.artux.mupse.service.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import net.artux.mupse.model.user.SecurityUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private final UserDetailService userDetailService;

    public String generateToken(SecurityUser securityUser) {
        Map<String, Object> claims = new HashMap<>();
        Instant expirationTime = Instant.now().plus(2, ChronoUnit.HOURS);
        Date expirationDate = Date.from(expirationTime);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(securityUser.getUsername())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public UserDetails parseToken(String token) {
        final Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return userDetailService.loadUserByUsername(claims.getSubject());
    }
}
