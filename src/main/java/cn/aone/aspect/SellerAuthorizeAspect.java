package cn.aone.aspect;

import cn.aone.constant.CookieConstant;
import cn.aone.constant.RedisConstant;
import cn.aone.exception.SellerAuthorizeException;
import cn.aone.util.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * aop效验
 * @Aspect作用是把当前类标识为一个切面供容器读取
 * @Before标识一个前置增强方法，相当于BeforeAdvice的功能
 * @Pointcut 来声明切入点表达式. 后面的其他通知直接使用方法名来引用当前的切入点表达式
 */
@Aspect
@Component
@Slf4j
public class SellerAuthorizeAspect {

    @Autowired
    private StringRedisTemplate redisTemplate;

 /*   //切入点(过滤的东西)
    @Pointcut("execution(public * cn.aone.controller.Seller*.*(..))" +//应该拦截的
    "&& !execution(public * cn.aone.controller.SellerUserController.*(..))")//不该拦截的
    public void verify() {}

    @Before("verify()")//在切入点之前所作事情(方法的具体实现)
    public void doVerify() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        //查询cookie
        Cookie cookie = CookieUtil.get(request, CookieConstant.TOKEN);
        if (cookie == null) {
            log.warn("【登录校验】Cookie中查不到token");
            throw new SellerAuthorizeException();
        }

        //去redis里查询
        String tokenValue = redisTemplate.opsForValue().get(String.format(RedisConstant.TOKEN_PREFIX, cookie.getValue()));
        if (StringUtils.isEmpty(tokenValue)) {
            log.warn("【登录校验】Redis中查不到token");
            throw new SellerAuthorizeException();
        }
    }*/
}
