package com.object.haru.user.token;

import com.object.haru.user.Security.PrincipalDetailService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

/**
 *   JwtToken 생성해주는 클래스
 *
 *   @version          1.00    2023.02.07
 *   @author           한승완
 */

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private String secretKey = "key";
    //해당 JwtToken 생성기의 시크릿키 이후 properties 파일로 확장한 뒤 암호화 예정

    private long tokenValidTime = 1000L * 60 * 60 * 24 * 30;
    //토큰 발급시의 유효시간 = 현재 30일

    private final PrincipalDetailService memberDetailsService;
    //시큐리티 세션의 인증을 위해 재정의한 UserDetailsService 사용

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String Kakaoid) {
        Claims claims = Jwts.claims().setSubject(Kakaoid);
        Date now = new Date();

        //토큰 생성
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    //인증 객체 생성
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = memberDetailsService.loadUserByUsername(getMemberKakaoId(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    //토큰을 파싱하여 해당하는 정보를 가져옴
    public String getMemberKakaoId(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        } catch(ExpiredJwtException e) {
            return e.getClaims().getSubject();
        }
    }

    //토큰의 특징을 가지고 파싱
    public String resolveToken(HttpServletRequest req) {
        return req.getHeader("X-AUTH-TOKEN");
    }

    //토큰이 유효한지 검증
    public boolean validateTokenExceptExpiration(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch(Exception e) {
            return false;
        }
    }
}