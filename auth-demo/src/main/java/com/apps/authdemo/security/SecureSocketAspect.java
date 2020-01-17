package com.apps.authdemo.security;

import org.apache.tomcat.util.net.SecureNio2Channel;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class SecureSocketAspect {

    @Around("execution(* org.apache.tomcat.util.net.TLSClientHelloExtractor.new(..))")
    public void create(ProceedingJoinPoint joinPoint) throws Throwable {
        joinPoint.proceed();
    }



//    @Before("execution(* org.apache.tomcat.util.net.TLSClientHelloExtractor.new(..))")
    @Before("execution(* org.apache.tomcat.util.net.TLSClientHelloExtractor.new(..))")
    public Object doBasicProfiling(ProceedingJoinPoint pjp) throws Throwable {
        // start stopwatch
        Object retVal = pjp.proceed();
        // stop stopwatch
        return retVal;
    }
}
