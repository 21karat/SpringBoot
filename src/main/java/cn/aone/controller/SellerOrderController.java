package cn.aone.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.aone.dto.OrderDTO;
import cn.aone.enums.ResultEnum;
import cn.aone.exception.SellException;
import cn.aone.service.OrderService;
import lombok.extern.slf4j.Slf4j;

/**
 * 订单
 * @author 开发
 *
 */
@Slf4j
@RestController
@RequestMapping("/seller/order")
public class SellerOrderController {

	
	@Autowired
	private OrderService orderService;
	/**
	 * 订单列表
	 * @return
	 */
	//@PostMapping("/list")
	//@GetMapping("/list")
	@RequestMapping("/list")
	@ResponseBody
	public ModelAndView list(
			@RequestParam(value="page",defaultValue = "1") Integer page,
			@RequestParam(value="size",defaultValue = "3") Integer size,
			Map<String,Object> map){
		//从1开始查的(需要从0开始查)
		@SuppressWarnings("deprecation")
		PageRequest request=new PageRequest(page-1, size);
		Page<OrderDTO> orderDTOPage=orderService.findList(request);
		map.put("orderDTOPage", orderDTOPage);
	    map.put("currentPage", page);
	    map.put("size", size);
		return new ModelAndView("order/list",map);
	}
	/**
	 * 取消订单
	 * @return
	 */
	@RequestMapping("/cancel")
	@ResponseBody
	public ModelAndView cancel(
			@RequestParam("orderId")String orderId,
			Map<String, Object> map){
		try{
			OrderDTO orderDTO=orderService.findOne(orderId);
			orderService.cancel(orderDTO);
		}catch(SellException e){
			log.error("[卖家端取消订单]发生异常");
			map.put("msg",e.getMessage());
			map.put("url", "/springBoot/seller/order/list");
			return new ModelAndView("common/error",map);
		}
		map.put("msg",ResultEnum.SUCCESS.getMessage());
		map.put("url", "/springBoot/seller/order/list");
		return new ModelAndView("common/success");
	}
	/**
	 * 订单详情
	 * @return
	 */
	@RequestMapping("/detail")
	@ResponseBody
	public ModelAndView detail(
			@RequestParam("orderId")String orderId,
			Map<String, Object> map){
		OrderDTO o=new OrderDTO();
		try{
			o=orderService.findOne(orderId);
		}catch(SellException e){
			log.error("[卖家端查询订单详情]发生异常");
			map.put("msg",e.getMessage());
			map.put("url", "/springBoot/seller/order/list");
			return new ModelAndView("common/error",map);
		}
		map.put("orderDTO",o);
		return new ModelAndView("order/detail",map);
	}
	/**
	 * 订单完结
	 * @return
	 */
	@RequestMapping("/finish")
	@ResponseBody
	public ModelAndView finish(
			@RequestParam("orderId")String orderId,
			Map<String, Object> map){
		try{
			OrderDTO o=orderService.findOne(orderId);
			orderService.finish(o);
		}catch(SellException e){
			log.error("[卖家端订单完结]发生异常");
			map.put("msg",e.getMessage());
			map.put("url", "/springBoot/seller/order/list");
			return new ModelAndView("common/error",map);
		}
		map.put("msg",ResultEnum.ORDER_FINISH_SUCCESS.getMessage());
		map.put("url", "/springBoot/seller/order/list");
		return new ModelAndView("common/success");
	}
}
