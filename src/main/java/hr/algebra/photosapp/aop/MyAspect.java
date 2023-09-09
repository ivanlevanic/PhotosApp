package hr.algebra.photosapp.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MyAspect {

    public static final String ADMIN_ROLE = "ROLE_ADMIN";

    @After("execution(* hr.algebra.photosapp.controller.MainController.*(..))")
    public void afterControllerMethodExecution(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("Logged before executing: " + methodName);
    }

    @Before("execution(* hr.algebra.photosapp.controller.MainController.usage(..)) && @annotation(org.springframework.security.access.annotation.Secured) && @annotation(secured)")
    public void checkUserRole(JoinPoint joinPoint, org.springframework.security.access.annotation.Secured secured) {
        String adminRole = ADMIN_ROLE;

        boolean isAdmin = false;
        if (SecurityUtils.hasRole(adminRole)) {
            isAdmin = true;
        }

        if (!isAdmin) {
            throw new AccessDeniedException("Access denied. Insufficient privileges.");
        }
    }
}
