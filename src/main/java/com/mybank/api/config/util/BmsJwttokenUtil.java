package com.mybank.api.config.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RefreshScope
public class BmsJwttokenUtil {

    private String secretKey;
    private long validityInMilliseconds;

    public BmsJwttokenUtil(@Value ("${security.jwt.token.secret-key}") final String secretKey, @Value ("${security.jwt.token.expire-length}") final long validityInMilliseconds) {
        this.secretKey =  Base64.getEncoder().encodeToString(secretKey.getBytes());;
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public String createToken(String username, List<String> roles) {
        System.out.println (String.format ("Generating token for {%s} with roles {%s} ", username,roles) );
        Claims claims = Jwts.claims().setSubject(username);
        //claims.put ("auth",new SimpleGrantedAuthority ("ROLE_USER"));
        claims.put("auth", roles.stream().map(s -> new SimpleGrantedAuthority ("ROLE_"+s)).collect(Collectors.toList()));
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    /*public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }*/

    public boolean validateToken(String token){
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
       }

}
