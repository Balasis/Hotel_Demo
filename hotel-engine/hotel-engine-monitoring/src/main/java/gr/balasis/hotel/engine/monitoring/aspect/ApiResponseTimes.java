package gr.balasis.hotel.engine.monitoring.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ApiResponseTimes {

    @Around("execution(public * gr.balasis.hotel..*Controller.*(..))")
    public Object monitorExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;
        System.out.println("Execution time: " + executionTime + "ms");
        return proceed;
    }
}