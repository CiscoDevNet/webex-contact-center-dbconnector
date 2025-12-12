package com.cisco.webexcc.dbconnector;

import com.cisco.webexcc.dbconnector.interceptor.EndpointTrackingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private EndpointTrackingInterceptor endpointTrackingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(endpointTrackingInterceptor);
    }
}
