package cn.aone.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import cn.aone.converter.OrderMaster2OrderDTOConverter;
import cn.aone.dao.OrderDetailRepository;
import cn.aone.dao.OrderMasterRepository;
import cn.aone.dto.CartDTO;
import cn.aone.dto.OrderDTO;
import cn.aone.enums.OrderStatusEnum;
import cn.aone.enums.PayStatusEnum;
import cn.aone.enums.ResultEnum;
import cn.aone.exception.SellException;
import cn.aone.model.OrderDetail;
import cn.aone.model.OrderMaster;
import cn.aone.model.ProductInfo;
import cn.aone.service.OrderService;
import cn.aone.service.ProductService;
import cn.aone.service.WebSocket;
import cn.aone.util.KeyUtil;
import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
public class OrderServiceImpl implements OrderService{


	@Autowired
	private OrderMasterRepository orderMaster;
	@Autowired
	private OrderDetailRepository orderDetail;
	@Autowired
	private ProductService productService;
	@Autowired
	private WebSocket webSocket;
	
	
	//创建订单
	@Override
	@Transactional//加上事务(错误会回滚)
	public OrderDTO create(OrderDTO orderDTO) {
		
		String orderId=KeyUtil.genUniqueKey();//生成主键
		BigDecimal orderAmount=new BigDecimal(BigInteger.ZERO);//总价
//      List<CartDTO> cartDTOList = new ArrayList<>();
		// TODO Auto-generated method stub
		//1.查询商品(数量,价格)
		for (OrderDetail o: orderDTO.getOrderDetailList()) {
	        ProductInfo productInfo=productService.findOne(o.getProductId());
	        if(productInfo==null){
	        	throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
	        }
	        //2.计算订单总格
	        //orderDetail.getProductPrice().multiply(new BigDecimal(orderDetail.getProductQuantity()));
	        orderAmount=productInfo.getProductPrice()
	        		.multiply(new BigDecimal(o.getProductQuantity())).add(orderAmount);
	        //订单详情入库
	        o.setDetailId(KeyUtil.genUniqueKey());
	        o.setOrderId(orderId);
	        BeanUtils.copyProperties(productInfo, o);
	        orderDetail.save(o);
//          CartDTO cartDTO = new CartDTO(orderDetail.getProductId(), orderDetail.getProductQuantity());
//          cartDTOList.add(cartDTO);
	        
		}
		
		
		//3.写入数据库数据(orderMaster和orderDetail)
		OrderMaster om=new OrderMaster();
		orderDTO.setOrderId(orderId);
		BeanUtils.copyProperties(orderDTO, om);//将前者对象中数据拷贝到后者对象中
		
		om.setOrderAmount(orderAmount);
		om.setOrderStatus(OrderStatusEnum.NEW.getCode());
	    om.setPayStatus(PayStatusEnum.WAIT.getCode());
	    om.setCreateTime(new Date());
	    om.setUpdateTime(new Date());
		orderMaster.save(om);
		//4.扣库存
		List<CartDTO> cartDTOList=orderDTO.getOrderDetailList()
				.stream()
				.map(e ->new CartDTO(e.getProductId(), e.getProductQuantity()))
				.collect(Collectors.toList());//直接返回list
		productService.decreaseStock(cartDTOList);
		
		//发送websocket消息
		webSocket.sendMessage(orderDTO.getOrderId());
		
		return orderDTO;
	}
	
	//查询单个订单
	@Override
	@Transactional
	public OrderDTO findOne(String orderId) {
		// TODO Auto-generated method stub
		OrderMaster order=orderMaster.getOne(orderId);
		if(order==null){
			throw new SellException(ResultEnum.ORDER_NOT_EXIST);
		}
		
		List<OrderDetail> orderDetails=orderDetail.findByOrderId(orderId);
		if(CollectionUtils.isEmpty(orderDetails)){
			throw new SellException(ResultEnum.ORDERDETAIL_NOT_EXIST);
		}
		
		
		OrderDTO orderDTO=new OrderDTO();
		BeanUtils.copyProperties(order, orderDTO);
		orderDTO.setOrderDetailList(orderDetails);
		return orderDTO;
	}

	//分页查
	@Override
	public Page<OrderDTO> findList(String buyerOpenid, Pageable pageable) {
		// TODO Auto-generated method stub
		Page<OrderMaster> oPage=orderMaster.findByBuyerOpenid(buyerOpenid, pageable);
		List<OrderDTO> orderDTOs=OrderMaster2OrderDTOConverter.convert(oPage.getContent());
		return new PageImpl<OrderDTO>(orderDTOs,pageable,oPage.getTotalElements());
	}

	//取消订单
	@Override
	@Transactional
	public OrderDTO cancel(OrderDTO orderDTO) {
		 OrderMaster order = new OrderMaster();
		// TODO Auto-generated method stub
		//判断状态
		if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
			 log.error("【取消订单】订单状态不正确, orderId={}, orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
			 throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
		}
		//修改订单状态
		  //修改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        BeanUtils.copyProperties(orderDTO, order);
        OrderMaster updateResult = orderMaster.save(order);
        if (updateResult == null) {
            log.error("【取消订单】更新失败, order={}", order);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
		//返还库存
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
            log.error("【取消订单】订单中无商品详情, orderDTO={}", orderDTO);
            throw new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
        }
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream()
                .map(e -> new CartDTO(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());
        productService.increaseStock(cartDTOList);
		//退款(已支付)
        if(orderDTO.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())){
        	
        }
		return orderDTO;
	}

	//完成订单
	@Override
	@Transactional
	public OrderDTO finish(OrderDTO orderDTO) {
		// TODO Auto-generated method stub
		//判断订单状态
		if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
			 log.error("【完结订单】订单状态不正确, orderId={}, orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
	         throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
		}		
		//修改状态
		orderDTO.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
		OrderMaster order=new OrderMaster();
		BeanUtils.copyProperties(orderDTO, order);
		OrderMaster updateResult = orderMaster.save(order);
	    if (updateResult == null) {
	            log.error("【完结订单】更新失败, orderMaster={}", orderMaster);
	            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
	    }

	    //推送微信模版消息
	    return orderDTO;
	}

	//支付订单
	@Override
	@Transactional
	public OrderDTO paid(OrderDTO orderDTO) {
		// TODO Auto-generated method stub
		 //判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【订单支付完成】订单状态不正确, orderId={}, orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        //判断支付状态
        if (!orderDTO.getPayStatus().equals(PayStatusEnum.WAIT.getCode())) {
            log.error("【订单支付完成】订单支付状态不正确, orderDTO={}", orderDTO);
            throw new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR);
        }

        //修改支付状态
        orderDTO.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        OrderMaster order = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, order);
        OrderMaster updateResult = orderMaster.save(order);
        if (updateResult == null) {
            log.error("【订单支付完成】更新失败, orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        return orderDTO;
	}

	/** 查询订单列表. 分页查(所有订单,后端使用)*/
	@Override
	public Page<OrderDTO> findList(Pageable pageable) {
		// TODO Auto-generated method stub
		Page<OrderMaster> oPage=orderMaster.findAll(pageable);
		List<OrderDTO> pDtos=OrderMaster2OrderDTOConverter.convert(oPage.getContent());				
		return new PageImpl<OrderDTO>(pDtos,pageable,oPage.getTotalElements());
	}

}
