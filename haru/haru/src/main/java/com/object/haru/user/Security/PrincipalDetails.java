package com.object.haru.user.Security;

import com.object.haru.user.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *   UserDetail 상속받은 시큐리티 커스텀 클래스
 *
 *   @version          1.00    2023.02.07
 *   @author           한승완
 */

//Spring Securtiy 사용을 위한 인가를 위해 UserDetails 상속받아 메소드를 오버라이드로 재정의 후 사용
public class PrincipalDetails implements UserDetails {
    private final UserEntity userEntity; //유저 엔티티 의존성 주입
    public PrincipalDetails(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { //
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(userEntity.getUserrole()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    @Override
    public String getUsername() { // uid로 유저이름 사용
        return Long.toString(userEntity.getKakaoid());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
