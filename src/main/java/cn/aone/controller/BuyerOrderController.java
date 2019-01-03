package cn.aone.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.aone.converter.OrderForm2OrderDTOConverter;
import cn.aone.dto.OrderDTO;
import cn.aone.enums.ResultEnum;
import cn.aone.exception.SellException;
import cn.aone.form.OrderForm;
import cn.aone.service.BuyerService;
import cn.aone.service.OrderService;
import cn.aone.util.ResultVOUtil;
import cn.aone.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
/**
 * 订单接口
 * @author 开发
 *
 */
@RestController
@RequestMapping("/buyter/order")
@Slf4j
public class BuyerOrderController {
	
	@Autowired
	private OrderService orderService;
	@Autowired
	private BuyerService buyerService;
	
	//创建订单
	@SuppressWarnings("unchecked")
	@RequestMapping("/create")
	public ResultVo<Map<String,String>> create(
			@Valid OrderForm orderForm,
			BindingResult bindingResult){//@Valid注解用于校验
		
		 	if (bindingResult.hasErrors()) {//判断表单校验之后有没有错误(错误抛出异常)
	            log.error("【创建订单】参数不正确, orderForm={}", orderForm);
	            throw new SellException(ResultEnum.PARAM_ERROR.getCode(),
	                    bindingResult.getFieldError().getDefaultMessage());
	        }
		 	//转换(效验类转传输类)
	        OrderDTO orderDTO = OrderForm2OrderDTOConverter.convert(orderForm);
	        
	        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
	            log.error("【创建订单】购物车不能为空");
	            throw new SellException(ResultEnum.CART_EMPTY);
	        }
	        //下单调service
	        OrderDTO createResult = orderService.create(orderDTO);
	        
	        Map<String, String> map = new HashMap<>();
	        map.put("orderId", createResult.getOrderId());
	        return ResultVOUtil.success(map);
	}
	//订单列表
	@SuppressWarnings("unchecked")
	@RequestMapping("/list")
	@ResponseBody
	//@GetMapping("/list")
	//@PostMapping("/list")
	public ResultVo<List<OrderDTO>> list(@RequestParam("openid")String openid,
			@RequestParam(value="page",defaultValue="0") Integer page,
			@RequestParam(value="size",defaultValue="10") Integer size){
		if(StringUtils.isEmpty(openid)){
			log.error("订单openid为空");
			throw new SellException(ResultEnum.PARAM_ERROR);
		}
		log.error("openid："+openid);
		@SuppressWarnings("deprecation")
		PageRequest request=new PageRequest(page, size);
		Page<OrderDTO> oPage=orderService.findList(openid,request);
		log.error("数据："+oPage.getContent().size());
		return ResultVOUtil.success(oPage.getContent());		
	}
	//订单详情
	@SuppressWarnings("unchecked")
	@PostMapping("/detail")
	public ResultVo<OrderDTO> detail(@RequestParam("openid")String openid,
			@RequestParam("orderId") String orderId){		
		OrderDTO o=buyerService.findOrderOne(openid, orderId);
		return ResultVOUtil.success(o);
	}
	//取消订单
	@SuppressWarnings("rawtypes")
	@PostMapping("/update")
	public ResultVo update(@RequestParam("openid")String openid,
			@RequestParam("orderId") String orderId){
		buyerService.cancelOrder(openid, orderId);
		return ResultVOUtil.success();
	}
}
