package com.example.datarest.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Spring AOP logging annotation. See https://www.baeldung.com/spring-aop-annotation for details.
 * Note @LogExecutionTime doesn't seem to work when applied to method run on threads.
 *
 */
@Slf4j
@Aspect
@Component
public class LoggingAspect {

   @Around("@annotation(LogExecutionTime)")
   public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
      final long start = System.currentTimeMillis();
      final Object proceed = joinPoint.proceed();
      final long executionTime = System.currentTimeMillis() - start;

      log.info( joinPoint.getSignature() + " executed in " + executionTime + "ms" );
      return proceed;
   }

}
