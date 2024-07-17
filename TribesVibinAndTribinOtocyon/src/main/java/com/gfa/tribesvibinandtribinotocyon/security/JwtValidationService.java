package com.gfa.tribesvibinandtribinotocyon.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtValidationService {


    private static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();

    public JwtValidationResult validateJwtToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token);

            Date expirationDate = claimsJws.getPayload().getExpiration();
            Date now = new Date();
            if (expirationDate.before(now)) {
                return new JwtValidationResult(false, "Token has expired");
            }

            return new JwtValidationResult(true, "Token is valid");

        } catch (Exception e) {
            return new JwtValidationResult(false, "Invalid token");
        }
    }
}