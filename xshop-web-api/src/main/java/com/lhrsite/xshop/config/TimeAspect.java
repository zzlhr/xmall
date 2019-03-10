package com.lhrsite.xshop.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Aspect
@Slf4j
public class TimeAspect {

    @Around("execution(* com.lhrsite.xshop.webapi.controller.*.*(..))")
    private Object mappingAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = new Date().getTime();
        Object result = joinPoint.proceed();
        log.info("【执行耗时】clazz={}, method={},  time={}"
                , joinPoint.getTarget().getClass().getName()
                , joinPoint.getSignature().getName()
                , (new Date().getTime() - startTime) + "ms");
        return result;
    }
}
