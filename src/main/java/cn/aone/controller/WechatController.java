package cn.aone.controller;

import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.aone.config.ProjectUrlConfig;
import cn.aone.enums.ResultEnum;
import cn.aone.exception.SellException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;

@Controller//@RestController注解返回gson不会跳转，跳转用@Controller
@RequestMapping("/wechat")
@Slf4j
public class WechatController {
	
	@Autowired
	private WxMpService wxMpService;
	@Autowired
	private WxMpService	wxOpenService;
	
	@SuppressWarnings("unused")
	@Autowired
    private ProjectUrlConfig projectUrlConfig;
	
	@SuppressWarnings("deprecation")
	@RequestMapping("/authorize")
	public String authorize(@RequestParam("returnUrl") String returnUrl) {
	        //1. 配置
	        //2. 调用方法
	        String url = "http://10.0.1.183/wechat/userInfo";
	        String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAUTH2_SCOPE_BASE, URLEncoder.encode(returnUrl));
	        log.info("code="+redirectUrl);
	        return "redirect:" + redirectUrl;
	}

	@RequestMapping("/userInfo")
	public String userInfo(@RequestParam("code") String code,
	                         @RequestParam("state") String returnUrl) {
	        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
	        try {
	            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
	        } catch (WxErrorException e) {
	            log.error("【微信网页授权】{}", e);
	            throw new SellException(ResultEnum.WECHAT_MP_ERROR.getCode(), e.getError().getErrorMsg());
	        }

	        String openId = wxMpOAuth2AccessToken.getOpenId();

	        return "redirect:" + returnUrl + "?openid=" + openId;
	}

	
	
	
	
	
	
	
	@RequestMapping("/qrAuthorize")
	public String qrAuthorize(@RequestParam("returnUrl") String returnUrl){
		String url="";
		@SuppressWarnings("deprecation")
		String redirectUrl = wxOpenService.buildQrConnectUrl(url, WxConsts.QRCONNECT_SCOPE_SNSAPI_LOGIN, URLEncoder.encode(returnUrl));
	    return "redirect:" + redirectUrl;
	}
	
	@RequestMapping("/qrUserInfo")
    public String qrUserInfo(@RequestParam("code") String code,
                             @RequestParam("state") String returnUrl) {
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
        try {
            wxMpOAuth2AccessToken = wxOpenService.oauth2getAccessToken(code);
        } catch (WxErrorException e) {
            log.error("【微信网页授权】{}", e);
            throw new SellException(ResultEnum.WECHAT_MP_ERROR.getCode(), e.getError().getErrorMsg());
        }
        log.info("wxMpOAuth2AccessToken={}", wxMpOAuth2AccessToken);
        String openId = wxMpOAuth2AccessToken.getOpenId();

        return "redirect:" + returnUrl + "?openid=" + openId;
   }

}
