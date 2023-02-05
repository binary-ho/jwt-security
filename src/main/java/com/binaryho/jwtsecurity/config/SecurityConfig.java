package com.binaryho.jwtsecurity.config;

import com.binaryho.jwtsecurity.filter.AfterBasicAuthenticationFilter;
import com.binaryho.jwtsecurity.filter.BeforeBasicAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsFilter corsFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.addFilterBefore(new BeforeBasicAuthenticationFilter(), BasicAuthenticationFilter.class);
        http.addFilterAfter(new AfterBasicAuthenticationFilter(), BasicAuthenticationFilter.class);
        http.csrf().disable();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilter(corsFilter)
            .formLogin().disable() /* 폼 로그인 안 쓸게요 */
            .httpBasic().disable() /* basic 방식이 아닌 bearer 방식을 쓰겠다 */
            .authorizeRequests()
            .antMatchers("/api/v1/user/**")
            .access("hasAnyRole('ROLE_USER', 'ROLE_MANAGER', 'ROLE_ADMIN')")

            .antMatchers("/api/v1/manager/**")
            .access("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")

            .antMatchers("/api/v1/admin/**")
            .access("hasAnyRole('ROLE_ADMIN')")
            .anyRequest().permitAll();

        return http.build();
    }
}
