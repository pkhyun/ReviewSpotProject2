package com.sparta.reviewspotproject.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Slf4j(topic = "AOP 로깅")
@Component
public class LoggingAspect {

    @Pointcut("execution(* com.sparta.reviewspotproject.controller.*.*(..))")
    public void controller() {
    }

    @Before("controller()")
    public void logging(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String requestURL = request.getRequestURL().toString();
        String httpMethod = request.getMethod();

            log.info("\n Request URL: {}, \n HTTP Method: {}", requestURL, httpMethod);
    }


}
