package com.cisco.webexcc.dbconnector.aspect;

import jakarta.annotation.PostConstruct;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @PostConstruct
    public void init() {
        logger.info("LoggingAspect initialized!");
    }

    @Around("execution(* com.cisco.webexcc.dbconnector.controller.ApiController.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().getName();
        
        logger.info("Entering method: {}", methodName);
        
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            long executionTime = System.currentTimeMillis() - start;
            logger.error("Method {} failed after {} ms with exception: {}", methodName, executionTime, e.getMessage());
            throw e;
        }

        long executionTime = System.currentTimeMillis() - start;
        logger.info("Method {} executed in {} ms", methodName, executionTime);
        
        return result;
    }
}
