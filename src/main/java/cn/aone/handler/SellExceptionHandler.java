package cn.aone.handler;

import cn.aone.config.ProjectUrlConfig;
import cn.aone.exception.SellerAuthorizeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * 拦截登陆异常
 * @ControllerAdvice 注解定义全局异常处理类
 * @ExceptionHandler 注解声明异常处理方法
 */
@ControllerAdvice
public class SellExceptionHandler {

    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    //拦截登录异常
    //http://sell.natapp4.cc/sell/wechat/qrAuthorize?returnUrl=http://sell.natapp4.cc/sell/seller/login
    @ExceptionHandler(value = SellerAuthorizeException.class)
    public ModelAndView handlerAuthorizeException() {
        return new ModelAndView("redirect:"
        .concat(projectUrlConfig.getWechatOpenAuthorize())
        .concat("/wechat/qrAuthorize")
        .concat("?returnUrl=")
        //.concat(projectUrlConfig.getSpringBoot())
        .concat("/seller/login"));
    }
}
