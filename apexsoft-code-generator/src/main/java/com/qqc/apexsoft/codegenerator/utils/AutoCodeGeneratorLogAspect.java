package com.qqc.apexsoft.codegenerator.utils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AutoCodeGeneratorLogAspect {
    public static final Logger log = LoggerFactory.getLogger(AutoCodeGeneratorLogAspect.class);

    @Pointcut("execution(* com.qqc.apexsoft.codegenerator..write*())")
    public void AutoCodeGeneratorLogAspectPointCut() {

    }

    @Before("AutoCodeGeneratorLogAspectPointCut()")
    public void before(JoinPoint point) {
        log.info(point.getSignature().getName() + " 开始...");
    }

    @AfterReturning("AutoCodeGeneratorLogAspectPointCut()")
    public void afterReturning(JoinPoint point) {
        log.info(point.getSignature().getName() + " 结束...");
    }
}
