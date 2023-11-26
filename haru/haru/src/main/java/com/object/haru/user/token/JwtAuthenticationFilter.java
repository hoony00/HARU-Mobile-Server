package com.object.haru.user.token;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 *   Jwt 인가에 사용되는 시큐리티 필터
 *
 *   @version          1.00    2023.02.07
 *   @author           한승완
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override //JwtToken 검증을 위한 제네릭필터 정의
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
        //헤더에 포함되어 있는 토큰을 스트링으로 파싱해준다

        //토큰이 비어있지 않거나 유효한 시간의 응답일 경우 Authentication 객체를 만들어 다음 필터로 전송
        if (token != null && jwtTokenProvider.validateTokenExceptExpiration(token)) {
            Authentication auth = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        chain.doFilter(request, response);
    }
}