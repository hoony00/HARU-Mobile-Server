package com.object.haru.config;

import com.object.haru.user.token.JwtAuthenticationFilter;
import com.object.haru.user.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 *   스프링 시큐리티 Config 파일
 *
 *   @version          1.00    2023.02.07
 *   @author           한승완
 */
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable() // 기본설정 미사용
                .csrf().disable() // csrf 보안 미사용
                .formLogin().disable() //기본 로그인 폼 미사용
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 미사용
                .and()
                .authorizeRequests()
                .antMatchers("/kakao/**").permitAll()
                .antMatchers("/fcm/**").permitAll()
                .antMatchers("/test/**","/addr_daum.html","/admin/notice/**").permitAll()
                .antMatchers("/v2/api-docs", "/swagger-resources/**", "/swagger-ui/index.html", "/swagger-ui.html"
                        ,"/webjars/**", "/swagger/**").permitAll()
                .anyRequest().authenticated() //나머지 요청에 대해서는 인가 필요
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
                //Jwt 필터 추가
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
