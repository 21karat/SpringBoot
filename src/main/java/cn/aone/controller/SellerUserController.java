package cn.aone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.aone.constant.CookieConstant;
import cn.aone.constant.RedisConstant;
import cn.aone.enums.ResultEnum;
import cn.aone.model.SellerInfo;
import cn.aone.service.SellerService;
import cn.aone.util.CookieUtil;

@Controller
@RequestMapping("/seller")
public class SellerUserController {

	@Autowired
	private SellerService sellerService;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	/**
	 * 登陆
	 * @param openid
	 * @param map
	 * @param response
	 * @return
	 */
	@RequestMapping("/login")
	public ModelAndView login(
			@RequestParam("openid")String openid,
			Map<String,Object> map,
			HttpServletResponse response){
		//1.openid去和数据库里的数据匹配
		SellerInfo sellerInfo=sellerService.findSellerInfoByOpenId(openid);
		if(sellerInfo==null){
			map.put("msg", ResultEnum.LOGIN_FAIL.getCode());
			map.put("url", "/seller/order/list");//登陆失败跳转订单列表页
	        return new ModelAndView("common/error");
		}
		//2.设置token至redis
		String token=UUID.randomUUID().toString();
		Integer expire=RedisConstant.EXPIRE;
		stringRedisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN_PREFIX, token),openid,expire,TimeUnit.SECONDS);//参数(key,value,过期时间,时间单位)
		//3.设置token至cookie
		CookieUtil.set(response, CookieConstant.TOKEN, token, expire);
		return new ModelAndView("redirect:/seller/order/list");
	}
	/**
	 * 退出
	 * @param request
	 * @param response
	 * @param map
	 */
	@RequestMapping("/logout")
	public ModelAndView logout(HttpServletRequest request,
            HttpServletResponse response,
            Map<String, Object> map){
		//1. 从cookie里查询
        Cookie cookie = CookieUtil.get(request, CookieConstant.TOKEN);
        if (cookie != null) {
            //2. 清除redis
        	stringRedisTemplate.opsForValue().getOperations().delete(String.format(RedisConstant.TOKEN_PREFIX, cookie.getValue()));
            //3. 清除cookie
            CookieUtil.set(response, CookieConstant.TOKEN, null, 0);
        }

        map.put("msg", ResultEnum.LOGOUT_SUCCESS.getMessage());
        map.put("url", "/seller/order/list");
        return new ModelAndView("common/success", map);
	}
}
