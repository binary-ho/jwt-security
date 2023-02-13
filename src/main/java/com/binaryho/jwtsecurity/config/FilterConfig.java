package com.binaryho.jwtsecurity.config;

import com.binaryho.jwtsecurity.filter.Order0Filter;
import com.binaryho.jwtsecurity.filter.Order1Filter;
import com.binaryho.jwtsecurity.filter.TokenTestFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<Order0Filter> order0FilterBean() {
        FilterRegistrationBean<Order0Filter> bean = new FilterRegistrationBean<>(new Order0Filter());

        bean.addUrlPatterns("/*");
        bean.setOrder(0);
        return bean;
    }

    @Bean
    public FilterRegistrationBean<Order1Filter> order1FilterBean() {
        FilterRegistrationBean<Order1Filter> bean = new FilterRegistrationBean<>(new Order1Filter());

        bean.addUrlPatterns("/*");
        bean.setOrder(1);
        return bean;
    }
}
