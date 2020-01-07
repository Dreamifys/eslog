package com.brand.log.exception;

import com.brand.log.util.DateFormatV1;
import com.brand.log.util.RedisUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * 全局捕获异常 - 案例（aop）
 * 返回页面+json
 */

@Controller
@Aspect
public class aopException {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    DateFormatV1 dateFormat;

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")//连接点是@RequestMapping注解的方法
    private void webPointcut() {}

    @AfterThrowing(pointcut = "webPointcut()", throwing = "e")//切点在webpointCut()
    public void handleThrowing(JoinPoint joinPoint, Exception e) {//controller类抛出的异常在这边捕获
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        String err = "@ex:" + e.getMessage()+" @ex-class：" + className+" @es-func：" + methodName;
        redisUtil.setex("brand-log:exception-" + dateFormat.getDate("yyyy-MM-dd'T'HH:mm", "GMT+8"),86400 * 3, err);
    }

    @Before("execution(* com.brand.log.controller.*.*(..))")
    public void beforeProcess(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
//        System.out.println("操作所在类：" + className);
//        System.out.println("操作所在方法：" + methodName);
//        System.out.println("操作中的参数：");
//        for (int i = 0; i < args.length; i++) {
//            System.out.println(args[i].toString());
//        }
    }

}
