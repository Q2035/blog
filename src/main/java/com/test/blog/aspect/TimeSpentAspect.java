package com.test.blog.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class TimeSpentAspect {
    private Logger logger = LoggerFactory.getLogger(TimeSpentAspect.class);

//    @Pointcut("execution(* com.test.blog.service.*.*(..))")
//    public void log(){
//    }

//    @Before("log()")
//    public void doBefore(JoinPoint joinPoint){
//        logger.info("begin to execute method:{}",joinPoint.getSignature().getName());
//        logger.info("start time:{}",System.currentTimeMillis());
//    }
//
//    @After("log()")
//    public void doAfter(JoinPoint joinPoint){
//        logger.info("exit to method:{}",joinPoint.getSignature().getName());
//        logger.info("end time:{}",System.currentTimeMillis());
//    }
}
