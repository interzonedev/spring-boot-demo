package com.interzonedev.springbootdemo.web.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

public class SpringBootDemoServletRequestListener implements ServletRequestListener {

    private static final Logger log = LoggerFactory.getLogger(SpringBootDemoServletRequestListener.class);

    @Override
    public void requestDestroyed(ServletRequestEvent event) {
        HttpServletRequest httpRequest = (HttpServletRequest) event.getServletRequest();
        String requestUri = httpRequest.getRequestURI();
        String requestMethod = httpRequest.getMethod();

        if (!ServletUtils.isAssetRequest(requestUri)) {
            log.info("********** Ending " + requestMethod + " " + requestUri + " **********");
        }
    }

    @Override
    public void requestInitialized(ServletRequestEvent event) {
        HttpServletRequest httpRequest = (HttpServletRequest) event.getServletRequest();
        String requestUri = httpRequest.getRequestURI();
        String requestMethod = httpRequest.getMethod();

        if (!ServletUtils.isAssetRequest(requestUri)) {
            log.info("********** Starting " + requestMethod + " " + requestUri + " **********");
        }
    }

}
