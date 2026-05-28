package com.taskflow.security.jwt;

import com.taskflow.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.taskflow.security.exception.InvalidTokenException;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import io.jsonwebtoken.JwtException;

@Service
public class JwtService {

    private final SecretKey key;
    private final long expirationMs;

    public JwtService(
        @Value("${taskflow.jwt.secret}") String secret,
        @Value("${taskflow.jwt.expiration-ms}") long expirationMs
    ) {
        byte[] bytes;
        try { bytes = Decoders.BASE64.decode(secret); }
        catch (io.jsonwebtoken.io.DecodingException ex) { bytes = secret.getBytes(StandardCharsets.UTF_8); }
        if (bytes.length < 32) {
            //removed
            //byte[] padded = new byte[32];
            //System.arraycopy(bytes, 0, padded, 0, bytes.length);
            //bytes = padded;
			throw new IllegalArgumentException(
				"JWT secret must be at least 32 bytes (256 bits)"
			);
        }
        this.key = Keys.hmacShaKeyFor(bytes);
        this.expirationMs = expirationMs;
    }

    public String generate(User user) {
        Date now = new Date();
        return Jwts.builder()
            .subject(String.valueOf(user.getId()))
	// new line added   .issuer("taskflow")
			.issuer("taskflow")
            .claim("email", user.getEmail())
            .claim("username", user.getUsername())
            .issuedAt(now)
            .expiration(new Date(now.getTime() + expirationMs))
            // replaced  : .signWith(key)
			.signWith(key, Jwts.SIG.HS256)
            .compact();
    }
	//-----OLD_CODE------
    //public Claims parse(String token) {
    //    return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    //}

	
	public Claims parse(String token) {
    try {
        return Jwts.parser()
            .verifyWith(key)
         //this line is added : .requireIssuer("taskflow")
            .requireIssuer("taskflow")
         // this line also added : .clockSkewSeconds(30)
            .clockSkewSeconds(30)
            .build()
            .parseSignedClaims(token)
            .getPayload();

    } catch (JwtException ex) {
        throw new InvalidTokenException("Invalid or expired JWT token", ex);
		}
	}
}
