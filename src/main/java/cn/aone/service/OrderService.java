package cn.aone.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.aone.dto.OrderDTO;

public interface OrderService {
	
    /** 创建订单. */
    OrderDTO create(OrderDTO orderDTO);

    /** 查询单个订单. */
    OrderDTO findOne(String orderId);

    /** 查询订单列表. 分页查(某人所有订单,前端使用)*/
    Page<OrderDTO> findList(String buyerOpenid, Pageable pageable);

    /** 取消订单. */
    OrderDTO cancel(OrderDTO orderDTO);

    /** 完结订单. */
    OrderDTO finish(OrderDTO orderDTO);

    /** 支付订单. */
    OrderDTO paid(OrderDTO orderDTO);

    /** 查询订单列表. 分页查(所有订单,后端使用)*/
    Page<OrderDTO> findList(Pageable pageable);
}
