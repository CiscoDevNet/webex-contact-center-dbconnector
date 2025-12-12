package com.cisco.webexcc.dbconnector.interceptor;

import com.cisco.webexcc.dbconnector.service.EndpointTrackingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class EndpointTrackingInterceptor implements HandlerInterceptor {

    @Autowired
    private EndpointTrackingService trackingService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String uri = request.getRequestURI();
        // Filter out static resources and error pages if needed
        if (!uri.startsWith("/css") && !uri.startsWith("/js") && !uri.startsWith("/images") && !uri.equals("/error")) {
            // Normalize to lowercase for consistent tracking
            trackingService.trackExecution(uri.toLowerCase(), response.getStatus());
        }
    }
}
