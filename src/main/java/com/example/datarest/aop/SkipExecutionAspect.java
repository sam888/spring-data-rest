package com.example.datarest.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

/**
 * Spring Annotation for skipping execution of method from a Singleton class if a boolean variable from the class is true.
 * It's assumed Getter/Setter method for the boolean variable exists. The annotation will need to specify the name of
 * boolean variable as below, i.e.
 *
 *    @SkipExecution(skipFlag = "disablePolling")
 *     public void polling() {
 *         process();
 *     }
 *
 *  In the example above, the boolean variable is specified as 'disablePolling'. Why is this useful? Imagine
 *  disabling/enabling the execution of a method having @SkipExecution annotation by changing the boolean variable at
 *  runtime with a RestController. Exception will be thrown if the boolean variable specified in annotation doesn't exist
 *  in the encapsulating class of the method at runtime!
 *
 */
@Slf4j
@Aspect
@Component
@Order(0) // Highest precedence
public class SkipExecutionAspect {

   @Around(value = "@annotation(SkipExecution)")
   public Object skipExecution(ProceedingJoinPoint joinPoint) throws Throwable {

      String skipFlag = getSkipFlag(joinPoint);

      Class myClass = joinPoint.getStaticPart().getSignature().getDeclaringType();
      Optional<Field> skipFlagOptional = Arrays.stream(myClass.getDeclaredFields()).filter(field ->
         field.getName().equals( skipFlag ) && field.getType() == Boolean.class).findFirst();

      if ( ! skipFlagOptional.isPresent()) {
         throw new IllegalStateException("Boolean field " + skipFlag +
            " specified by the @SkipExecution(..) annotation does not exist");
      }

      Field skipFlagField = skipFlagOptional.get();
      skipFlagField.setAccessible( true );
      Boolean skipFlagBoolean = (Boolean)skipFlagField.get( joinPoint.getTarget() );

      if ( skipFlagBoolean ) {
         return joinPoint;
      }
      return joinPoint.proceed();
   }

   private String getSkipFlag(ProceedingJoinPoint joinPoint) {
      MethodSignature signature = (MethodSignature) joinPoint.getSignature();
      Method method = signature.getMethod();
      SkipExecution skipExecution = method.getAnnotation(SkipExecution.class);
      return skipExecution.skipFlag();
   }
}
