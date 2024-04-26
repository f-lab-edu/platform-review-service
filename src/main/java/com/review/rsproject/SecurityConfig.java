package com.review.rsproject;

import com.review.rsproject.security.CustomUserDetailsService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;


@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.userDetailsService(customUserDetailsService)
                .authorizeHttpRequests(auth -> auth.
                        requestMatchers("/api/review", "/api/platform").
                        authenticated().anyRequest().permitAll())

                // 간단한 api 로그인 구현을 위해 csrf 비활성화
                .csrf(csrf -> csrf.disable())


                .formLogin(form -> form.loginProcessingUrl("/api/login")
                        .defaultSuccessUrl("/login/success").failureUrl("/login/failed"))


                .logout(out -> out.logoutUrl("/api/logout")
                        .logoutSuccessHandler(((request, response, authentication) -> {
                            response.setContentType("text/html;charset:UTF-8");
                            response.setCharacterEncoding("UTF-8");
                            response.getWriter().write("로그아웃 되었습니다.");
                        })))


                // 로그인되지 않은 사용자가 로그인이 필요한 자원에 접근 시 예외 발생
                .exceptionHandling(e ->
                        e.authenticationEntryPoint(
                                (request, response, authException) -> {
                                    response.setContentType("text/html;charset:UTF-8");
                                    response.setCharacterEncoding("UTF-8");
                                    response.getWriter().write("인증이 필요한 요청입니다.");
                                }
                        ))

                .sessionManagement(session -> session.maximumSessions(1)
                        .maxSessionsPreventsLogin(false)) // 동시 접속 비활성화, 같은 아이디로 접속 시 기존 사용자 세션 만료
        ;

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


//    @Bean
//    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
//        UserDetails user = User.withUsername("admin")
//                .password("{noop}1111")
//                .build();
//        return new InMemoryUserDetailsManager(user);
//    }
}
