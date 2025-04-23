package com.example.fooddiary.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.example.fooddiary..*(..)) && "
            + "!execution(* com.example.fooddiary.service.VisitCounterService.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        if (logger.isInfoEnabled()) {
            logger.info("Выполнение: {}", joinPoint.getSignature().toShortString());
        }
    }

    @AfterReturning(
            pointcut = "execution(* com.example.fooddiary..*(..))",
            returning = "result"
    )
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        if (logger.isInfoEnabled()) {
            logger.info("Выполнение: {} с результатом: {}",
                    joinPoint.getSignature().toShortString(), result);
        }
    }

    @AfterThrowing(
            pointcut = "execution(* com.example.fooddiary..*(..))",
            throwing = "error"
    )
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
        if (logger.isErrorEnabled()) {
            logger.error("Исключение в: {} по причине: {}",
                    joinPoint.getSignature().toShortString(), error.getMessage());
        }
    }

}