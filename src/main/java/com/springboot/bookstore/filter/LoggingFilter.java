package com.springboot.bookstore.filter;

import com.springboot.bookstore.utils.logger.Logger;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public class LoggingFilter implements Filter {
    @Override
    public void doFilter(
            ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain
    ) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        String requestURI = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();
        String ipAddress = httpRequest.getRemoteAddr();
        String logMessage = String.format("[%s] %s: %s", ipAddress, method, requestURI);

        var logger = Logger.getInstance();
        logger.log("--- Request ---");
        logger.log(logMessage);

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
