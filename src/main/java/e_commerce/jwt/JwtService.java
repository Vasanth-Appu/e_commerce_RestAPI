package e_commerce.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

        private final SecretKey secretKey;

        private final String key = "12345678901234567890123456789012";

        private final long expiration = 180 * 1000; // 60 seconds

        // Constructor
        public JwtService() {
                this.secretKey = Keys.hmacShaKeyFor(
                                key.getBytes(StandardCharsets.UTF_8));
        }

        public String generateToken(String username) {

                Date now = new Date();

                Date expiryDate = new Date(now.getTime() + expiration);

                return Jwts.builder()

                                // Subject
                                .subject(username)

                                // Token created time
                                .issuedAt(now)

                                // Token expiration
                                .expiration(expiryDate)

                                // Sign token
                                .signWith(secretKey)

                                // Convert to String
                                .compact();
        }

        public String extractUsername(String token) {

                return getClaims(token)
                                .getSubject();
        }

        public boolean isTokenValid(String token, String username) {

                String tokenUsername = extractUsername(token);

                return tokenUsername.equals(username)
                                && !isTokenExpired(token);
        }

        private boolean isTokenExpired(String token) {

                Date expiration = getClaims(token).getExpiration();

                return expiration.before(new Date());
        }

        private Claims getClaims(String token) {

                return Jwts.parser()
                                .verifyWith(secretKey)
                                .build()
                                .parseSignedClaims(token)
                                .getPayload();
        }
}