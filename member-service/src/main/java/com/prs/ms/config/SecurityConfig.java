package com.prs.ms.config;

import com.prs.ms.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    private static final String UTF8_CONTENT_TYPE = "text/html;charset:UTF-8";


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.userDetailsService(customUserDetailsService)

                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())

                // 간단한 api 로그인 구현을 위해 csrf 비활성화
                .csrf(csrf -> csrf.disable())

                .formLogin(form -> form.loginProcessingUrl("/api/login")
                        .defaultSuccessUrl("/login/success").failureUrl("/login/failed"))

                .logout(out -> out.logoutUrl("/api/logout")
                        .logoutSuccessHandler(((request, response, authentication) -> {
                            response.setContentType(UTF8_CONTENT_TYPE);
                            response.setCharacterEncoding("UTF-8");
                            response.getWriter().write("로그아웃 되었습니다.");
                        })))
                .sessionManagement(session -> session.maximumSessions(1)
                        .maxSessionsPreventsLogin(false)) // 동시 접속 비활성화, 같은 아이디로 접속 시 기존 사용자 세션 만료
        ;

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
