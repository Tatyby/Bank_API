package com.sberStart.bank_api.logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class BankLoggingAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Before("execution(* com.sberStart.bank_api.service.admin.AdminBankServiceImp.*(..))")
    public void logBeforeMethodAdminService(JoinPoint joinPoint) {
        writeLoggerInfoWithArgs(joinPoint);
    }

    @AfterReturning(pointcut = "execution(* com.sberStart.bank_api.service.admin.AdminBankServiceImp.*(..))", returning = "result")
    public void logAfterReturningAdminService(JoinPoint joinPoint, Object result) {
        writeLoggerInfoReturn(joinPoint, result);
    }

    @Before("execution(* com.sberStart.bank_api.service.client.ClientBankServiceImp.*(..))")
    public void logBeforeMethodClientService(JoinPoint joinPoint) {
        writeLoggerInfoWithArgs(joinPoint);
    }

    @AfterReturning(pointcut = "execution(* com.sberStart.bank_api.service.client.ClientBankServiceImp.*(..))", returning = "result")
    public void logAfterReturningClientService(JoinPoint joinPoint, Object result) {
        writeLoggerInfoReturn(joinPoint, result);
    }

    private void writeLoggerInfoWithArgs(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getName();
        Object[] args = joinPoint.getArgs();
        logger.info("Вызывается метод: {}.{} с аргументами: {}", className, methodName, args);
    }

    private void writeLoggerInfoReturn(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getName();
        logger.info("Метод {}.{} возвращает: {}", className, methodName, result);
    }
}
