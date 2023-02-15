package com.binaryho.jwtsecurity.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.binaryho.jwtsecurity.config.auth.PrincipalDetails;
import com.binaryho.jwtsecurity.model.User;
import com.binaryho.jwtsecurity.repository.UserRepository;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/*
* 전자 서명을 통해 인가를 진행
* 시큐리티는 BasicAuthenticationFilter 를 가지고 있는데,
* 권한이나 인증이 필요한 특정 주소를 요청하면 무조건 이 필터를 거치게 된다. */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        super.doFilterInternal(request, response, chain);
        System.out.println("JwtAuthorizationFilter 거침.");

        String jwtHeader = request.getHeader("Authorization");
        System.out.println("jwtHeader: " + jwtHeader);

        /* JWT 토큰을 검증 해서 정상적인 사용자인지 확인
        * 1. header가 있는지 확인한다. */
        if (jwtHeader == null || !jwtHeader.startsWith("Bearer")) {
            chain.doFilter(request, response);
            return;
        }

        /* 2. token 다듬고 username 꺼내기*/
        String jwtToken = request.getHeader("Authorization")
            .replace("Bearer ", "");

        String username = JWT.require(Algorithm.HMAC512("jinho"))
            .build()
            .verify(jwtToken) /* 서명하기 */
            .getClaim("username")/* 유저 네임 거내기 */
            .asString();

        /* 서명이 정상적으로 됐다. */
        if (username != null) {
            User userEntity = userRepository.findByUsername(username);

            PrincipalDetails principalDetails = new PrincipalDetails(userEntity);

            /* JWT 토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 만들어 준다.
            * 인증은 무조건 되었다는 가정 하에 막 나가는 코드 */
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

            /* 시큐리티 세션에 강제로 Authentication 객체 저장 */
            SecurityContextHolder.getContext().setAuthentication(authentication);

            chain.doFilter(request, response);
        }
    }
}
