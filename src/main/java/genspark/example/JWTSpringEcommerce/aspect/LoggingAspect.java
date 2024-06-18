package genspark.example.JWTSpringEcommerce.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Aspect
@Component
@EnableAspectJAutoProxy
@Slf4j
public class LoggingAspect {

//    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Before("execution(* com.example.JWTSpringdemo.service.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Executing method: " + joinPoint.getSignature().getName());
    }

    @Before("execution(* com.example.JWTSpringdemo.controller.*.*(..))")
    public void logBeforeController(JoinPoint joinPoint) {
        log.info("Executing Controller method: " + joinPoint.getSignature().getName());
    }



    @AfterReturning(pointcut = "execution(* com.example.WTSpringdemo.service.*.*(..))", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        log.info("Method executed: " + joinPoint.getSignature().getName() + ", Return: " + result);
    }

    @AfterReturning(pointcut = "execution(* com.example.WTSpringdemo.controller.*.*(..))", returning = "result")
    public void logAfterController(JoinPoint joinPoint, Object result) {
        log.info("Controller Method executed: " + joinPoint.getSignature().getName() + ", Return: " + result);
    }
}
