package com.object.haru.user.Security;

import com.object.haru.user.UserEntity;
import com.object.haru.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *   UserDetail의 UserDetailsService를 상속받은 시큐리티 커스텀 클래스
 *   loadUserByUsername() 메서드 오버라이드 해서 사용
 *
 *   @version          1.00    2023.02.07
 *   @author           한승완
 */

//Spring Securtiy 사용을 위한 인가를 위해 UserDetailsService를 상속받아 특정 메소드를 재정의하여 사용
@Service
@RequiredArgsConstructor
public class PrincipalDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String kakaoid)throws UsernameNotFoundException {
        UserEntity user = userRepository.findByKakaoid(Long.parseLong(kakaoid));
        return new PrincipalDetails(user);
    }

}
