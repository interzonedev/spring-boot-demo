package com.interzonedev.springbootdemo.web.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class RequestResponseLoggingFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestUri = httpRequest.getRequestURI();
        String requestMethod = httpRequest.getMethod();

        if (!ServletUtils.isAssetRequest(requestUri)) {
            log.info("***** Starting " + requestMethod + " " + requestUri + " *****");
        }

        chain.doFilter(request, response);

        if (!ServletUtils.isAssetRequest(requestUri)) {
            log.info("***** Ending " + requestMethod + " " + requestUri + " *****");
        }
    }

}
