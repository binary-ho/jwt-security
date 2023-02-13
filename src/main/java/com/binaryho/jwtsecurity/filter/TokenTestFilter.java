package com.binaryho.jwtsecurity.filter;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenTestFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        if (httpServletRequest.getMethod().equals("POST")) {
            System.out.println("POST 요청됨");
            String headerAuth = httpServletRequest.getHeader("Authorization");
            System.out.println(headerAuth);

            if (headerAuth.equals("jinho")) {
                chain.doFilter(httpServletRequest, httpServletResponse);
            } else {
                PrintWriter outPrintWriter = httpServletResponse.getWriter();
                outPrintWriter.println("인증 실패");
            }
        }
    }
}
