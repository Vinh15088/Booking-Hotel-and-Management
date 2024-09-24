package com.Seo.SeoHotel.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Service
public class JWTUtils {
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7;

    // private key sign in token JWT (hmacSHA256)
    private final SecretKey key;


    public JWTUtils() {
        // secret string -> create key for hmacSHA256
        String secretString = "843567893696976453275974432697R634976R738467TR678T34865R6834R8763T478378637664538745673865783678548735687R3";

        // encode base64 -> decode byte array
        byte[] keyBytes = Base64.getDecoder().decode(secretString.getBytes(StandardCharsets.UTF_8));

        // create a secret key from byte array with hmacSHA256 algorithm
        this.key = new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    // generate token with UserDetails
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername()) // subject token
                .issuedAt(new Date(System.currentTimeMillis())) // issued time
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // expiration time
                .signWith(key) // sign token with key
                .compact(); // complete and return token with string
    }

    // extract username from jwt (token)
    public String extractUsername(String token) {
        // Claims::getSubject: get subject from payload of jwt
        return extractClaims(token, Claims::getSubject);
    }

    // extract info from token with function -> claims
    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction) {
        return claimsTFunction
                .apply(Jwts.parser()
                        .verifyWith(key) // verify with key
                        .build()
                        .parseSignedClaims(token)
                        .getPayload()); // get payload from jwt
    }

    // check valid token
    public boolean isValidToken(String token, UserDetails userDetails) {
        // create username extract from token
        final String username = extractUsername(token);

        // compare username with username in userDetails and check token expired
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // check token expired
    private boolean isTokenExpired(String token) {
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }
}
