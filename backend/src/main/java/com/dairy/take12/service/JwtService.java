package com.dairy.take12.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

   private String secretKey  ="";
    public JwtService(){
        try{
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGenerator.generateKey();
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    public String generateAccessToken(String id){
        Map<String,Object> claims = new HashMap<>();
        return Jwts
                .builder()
                .claims()
                .add(claims)
                .subject(id)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+1000*60))
                .and()
                .signWith(getKey())
                .compact();
    }
    public String generateRefreshToken(String id){
        Map<String,Object> claims = new HashMap<>();
        return Jwts
                .builder()
                .claims()
                .add(claims)
                .subject(id)
                .issuedAt(new Date(System.currentTimeMillis()))
            //    .expiration(new Date(System.currentTimeMillis()+1000*60*60*24*7))
                .expiration(new Date(System.currentTimeMillis()+1000*60*60*24*7))
                .and()
                .signWith(getKey())
                .compact();
    }
    private SecretKey getKey(){
        byte [] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public String extractId(String token){
        return extractClaims(token, Claims::getSubject);
    }
    private <T> T extractClaims(String token, Function<Claims,T> claimsResolver)
    {
        final Claims claims = extractAllClaims(token);
        return  claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token)
    {
        return  Jwts
                .parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    public boolean validateToken(String token, UserDetails adminDetails){
        String id = extractId(token);

        return (id.equals(adminDetails.getUsername()) && !isTokenExpired(token));
    }
    private boolean isTokenExpired(String token)
    {
        return extractExpiration(token).before(new Date());
    }
    private Date extractExpiration(String token)
    {
        return extractClaims(token, Claims::getExpiration);
    }
    public boolean doesNeedUpdation(String token) {
        return false;
     //      return extractExpiration(token).before(new Date(System.currentTimeMillis() - 2L * 24 * 60 * 60 * 1000));
    }

   public String extractIdFromRequest(HttpServletRequest request) {
        String requestHeader = request.getHeader("Authorization");
        String accessToken = null;
        String adminId = null;
        accessToken = requestHeader.substring(7);
        adminId  = extractId(accessToken);
        return  adminId;

    }

}
