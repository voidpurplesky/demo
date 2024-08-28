/*
8. 스프링 시큐리티 - 인증처리를 위한 UserDetailsService p685
 */
package com.example.demo.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class CustomUserDetailsService implements UserDetailsService {
    // p689 
    private PasswordEncoder passwordEncoder;
    
    public CustomUserDetailsService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername: {}", username);
        //p687 UserDetails 반환
        UserDetails userDetails = User.builder()
                .username("user1")
                //.password("1111")
                .password(passwordEncoder.encode("1111")) // 패스워드 인코딩 필요
                .authorities("ROLE_USER")
                .build();
        return userDetails;
    }
//org.springframework.security.authentication.InternalAuthenticationServiceException: UserDetailsService returned null, which is an interface contract violation
/*
[Principal=org.springframework.security.core.userdetails.User [Username=user1, Password=[PROTECTED], Enabled=true, AccountNonExpired=true, CredentialsNonExpired=true, AccountNonLocked=true, Granted Authorities=[ROLE_USER]]
, Credentials=[PROTECTED], Authenticated=true, Details=WebAuthenticationDetails [RemoteIpAddress=0:0:0:0:0:0:0:1, SessionId=4803218873D4D19A5B207BB76080EA47], Granted Authorities=[ROLE_USER]]] from SPRING_SECURITY_CONTEXT
*/

}
