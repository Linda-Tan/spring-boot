package com.junliang.spring.aop;

import com.alibaba.fastjson.JSON;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 使用 @Aspect注解将一个java类定义为切面类
 * 使用 @Pointcut定义一个切入点，可以是一个规则表达式，比如下例中某个package下的所有函数，也可以是一个注解等。
 * 根据 需要在切入点不同位置的切入内容
 * 使用 @Before在切入点开始处切入内容
 * 使用 @After在切入点结尾处切入内容
 * 使用 @AfterReturning在切入点return内容之后切入内容（可以用来对处理返回值做一些加工处理）
 * 使用 @Around在切入点前后切入内容，并自己控制何时执行切入点自身的内容
 * 使用 @AfterThrowing用来处理当切入内容部分抛出异常之后的处理逻辑
 */

@Aspect
@Component
@Log4j2
public class WebLogAspect {
    ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Pointcut("execution(public * com.junliang.spring.restcontrol..*.*(..))")
    public void logPointcut(){}

    @Before("logPointcut()")
    public void doBefore(JoinPoint point) {
        startTime.set(System.currentTimeMillis());
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 记录下请求内容
        log.info("Request URL : " + request.getRequestURL().toString());
        log.info("http-method : " + request.getMethod());
        log.info("Request IP : " + request.getRemoteAddr());
        log.info("class.method() : " + point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName());
        log.info("args[] : " + JSON.toJSONString(point.getArgs()));

    }
    @AfterReturning(returning = "ret", pointcut = "logPointcut()")
    public void doAfterReturning(Object ret){
        // 处理完请求，返回内容
        log.info("Response result : " + ret);
        log.info("total time : " + (System.currentTimeMillis() - startTime.get()));
    }

}
