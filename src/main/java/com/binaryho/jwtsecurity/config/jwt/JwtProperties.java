package com.binaryho.jwtsecurity.config.jwt;

/* 원래는 없어야 한다. */
public class JwtProperties {

    private JwtProperties() {}

    public static final String SECRET = "jinho";
    public static final int EXPIRATION_TIME = 60_000 * 10;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}
