package com.klb.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class AccessLogFilter implements Filter {

    private static final Logger accessLogger = LoggerFactory.getLogger("ACCESS_LOGGER");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        long startTime = System.currentTimeMillis();

        // Ghi lại thông tin truy cập trước khi xử lý request
        chain.doFilter(request, response);

        long duration = System.currentTimeMillis() - startTime;

        // Ghi lại thông tin truy cập sau khi xử lý request
        accessLogger.info("{} {} {} {} {}ms",
                httpRequest.getRemoteAddr(),
                httpRequest.getMethod(),
                httpRequest.getRequestURI(),
                httpResponse.getStatus(),
                duration);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Khởi tạo filter nếu cần
    }

    @Override
    public void destroy() {
        // Dọn dẹp filter nếu cần
    }
}
