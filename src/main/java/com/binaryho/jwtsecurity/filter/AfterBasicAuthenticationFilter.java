package com.binaryho.jwtsecurity.filter;


import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class AfterBasicAuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        System.out.println("BasicAuthenticationFilter 이후에 작동하는 필터");
        chain.doFilter(request, response);
    }
}
