package com.binaryho.jwtsecurity.config.jwt;

import com.binaryho.jwtsecurity.config.auth.PrincipalDetails;
import com.binaryho.jwtsecurity.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
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
//            BufferedReader bufferedReader = request.getReader();
//            String input = null;
//            while((input = bufferedReader.readLine()) != null) {
//                System.out.println(input);
//            }

            ObjectMapper objectMapper = new ObjectMapper();
            InputStream inputStream = request.getInputStream();
            User user = objectMapper.readValue(inputStream, User.class);
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
        return null;
    }
}
