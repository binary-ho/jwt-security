package com.binaryho.jwtsecurity.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.binaryho.jwtsecurity.config.auth.PrincipalDetails;
import com.binaryho.jwtsecurity.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter  {

    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter: 로그인 시도중");

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            User user = objectMapper.readValue(request.getInputStream(), User.class);
            System.out.println(user);

            UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            /* PrincipalDetailsService의 loadUserByUsername 함수가 실행됨
            * 내 로그인 정보가 담기게 된다. */
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println("hello! " + principalDetails.getUsername());

            /* return을 통해 authentication 객체는 session 영역에 저장되고, 거기서 가져오는 것이다.
            * JWT 토큰을 만들면서 세션을 만들 이유가 없는데, 단지 권한 처리를 편하게 하기 위해 return을 통해 session에 넣어준다. */
            return authentication;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        System.out.println("================================");
//        return super.attemptAuthentication(request, response);
        return null;
    }

    /* attemptAuthentication 실행한 후 인증이 정상적으로 되었으면, 실행되는 메서드
    *  JWT 토큰을 만들어서 request 요청한 사용자에게 JWT 토큰을 response 해주면 됨. */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("successfulAuthentication 실행됨 (인증이 완료됨) ");

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        String jwtToken = JWT.create()
            .withSubject(principalDetails.getUser().getUsername()) /* 토큰의 이름쯤 된다. */
            .withExpiresAt(new Date(System.currentTimeMillis() + (60_000 * 10))) /* 만료 시간 10분 정도가 적절하다. */
            .withClaim("id", principalDetails.getUser().getId())
            .withClaim("username", principalDetails.getUser().getUsername())
            .sign(Algorithm.HMAC512("jinho")); /* RSA 방식 말고 HMAC로 간다 */

        /* 토큰을 헤더에 담아 보낸다. */
        response.addHeader("Authorization", "Bearer " + jwtToken);
//        super.successfulAuthentication(request, response, chain, authResult);
    }
}
