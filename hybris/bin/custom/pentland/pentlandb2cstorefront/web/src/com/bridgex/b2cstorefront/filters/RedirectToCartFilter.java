package com.bridgex.b2cstorefront.filters;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class RedirectToCartFilter extends OncePerRequestFilter {

    private List<String> URLS;
    private String MULTI_URL;
    private String CART_URL;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        if(!(URLS.contains(httpServletRequest.getServletPath()) || httpServletRequest.getServletPath().startsWith(MULTI_URL))){
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + CART_URL);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    public void setURLS(List<String> URLS) {
        this.URLS = URLS;
    }

    public void setMULTI_URL(String MULTI_URL) {
        this.MULTI_URL = MULTI_URL;
    }

    public void setCART_URL(String CART_URL) {
        this.CART_URL = CART_URL;
    }
}