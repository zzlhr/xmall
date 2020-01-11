package com.lhrsite.xshop.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Aspect
@Slf4j
public class RequestLogAspect {
    @Around("execution(* com.lhrsite.xshop.controller.*.*(..))")
    private Object mappingAround(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("【请求日志】clazz={}, method={}, params={}", joinPoint.getTarget().getClass().getName(),
                joinPoint.getSignature().getName(), getNameAndValue(joinPoint));
        return joinPoint.proceed();
    }

    /**
     * 获取参数Map集合
     */
    Map<String, Object> getNameAndValue(ProceedingJoinPoint joinPoint) {
        Map<String, Object> param = new HashMap<>();

        Object[] paramValues = joinPoint.getArgs();
        String[] paramNames = ((CodeSignature)joinPoint.getSignature()).getParameterNames();

        for (int i = 0; i < paramNames.length; i++) {
            param.put(paramNames[i], paramValues[i]);
        }

        return param;
    }


}
