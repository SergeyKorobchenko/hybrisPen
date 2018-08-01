package com.bridgex.b2cstorefront.filters;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class RedirectToCartFilter extends OncePerRequestFilter {

    private static final List<String> URLS = Arrays.asList(new String[]{"/cart", "/login/checkout", "/login/pw/change"});
    private static final String MULTI_URL = "/checkout/";

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        if(!(URLS.contains(httpServletRequest.getServletPath()) || httpServletRequest.getServletPath().startsWith(MULTI_URL))){
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/cart");
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}