package cn.aone.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.aone.dto.OrderDTO;
import cn.aone.enums.OrderStatusEnum;
import cn.aone.enums.PayStatusEnum;
import cn.aone.model.OrderDetail;
import cn.aone.service.impl.OrderServiceImpl;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class OrderServiceImplTest {
	
	@Autowired
	private OrderServiceImpl orderServiceImpl;
	
	private final String buyerOpenid="11011011101";
	
	/**
	 * 下订单逻辑(创建订单)
	 * 
	 * 入参:用户信息+购物车商品id以及数量的集合
	 * 
	 * 1.遍历购物车id以及数量的集合,判断商品是否存在(不存在抛出异常)
	 * 2.计算传入商品对应数量需要的总价格
	 * 3.插入对应商品的订单详情表
	 * 4.创建一张订单表并生成
	 * 5.调用减库存接口
	 * @throws Exception
	 */
	@Test
	public void create() throws Exception{
		OrderDTO orderDTO=new OrderDTO();
		orderDTO.setBuyerName("LLL");
		orderDTO.setBuyerAddress("wwww");
		orderDTO.setBuyerPhone("123456789");
		orderDTO.setBuyerOpenid(buyerOpenid);
		//购物车
		List<OrderDetail> orderDetails=new ArrayList<>();	
		OrderDetail o1=new OrderDetail();
		o1.setProductId("22");
		o1.setProductQuantity(1);
		orderDetails.add(o1);
		orderDTO.setOrderDetailList(orderDetails);
		orderServiceImpl.create(orderDTO);
	}
	//查单条
	@Test
	public void findOne() throws Exception{
		OrderDTO o=orderServiceImpl.findOne("1536222684705849897");
		log.info("查询结果:"+o.getBuyerAddress());
		o.getOrderDetailList().forEach(a->{
			log.info(a.getProductName());
		});
		Assert.assertEquals("1536222684705849897", o.getOrderId());
	}
	//分页查
	@Test
	public void findPage() throws Exception{
		@SuppressWarnings("deprecation")
		PageRequest request=new PageRequest(1, 2);
		Page<OrderDTO> oPage=orderServiceImpl.findList("123",request);
		log.info("结束"+oPage.getTotalElements());
		Assert.assertNotEquals(0, oPage.getTotalElements());	
	}
	//取消订单
	@Test
    public void cancel() throws Exception {
        OrderDTO orderDTO = orderServiceImpl.findOne("1536216863645204270");
        OrderDTO result = orderServiceImpl.cancel(orderDTO);
        Assert.assertEquals(OrderStatusEnum.CANCEL.getCode(), result.getOrderStatus());
    }
	//订单完结
	@Test
	public void finish() throws Exception {
	    OrderDTO orderDTO = orderServiceImpl.findOne("1536216863645204270");
	    OrderDTO result = orderServiceImpl.finish(orderDTO);
	    Assert.assertEquals(OrderStatusEnum.FINISHED.getCode(), result.getOrderStatus());
	}	
	//支付订单
	@Test
    public void paid() throws Exception {
        OrderDTO orderDTO = orderServiceImpl.findOne("1536216863645204270");
        OrderDTO result = orderServiceImpl.paid(orderDTO);
        Assert.assertEquals(PayStatusEnum.SUCCESS.getCode(), result.getPayStatus());
    }
	 /** 查询订单列表. 分页查(所有订单,后端使用)*/
	@Test
	public void findAll(){
		@SuppressWarnings("deprecation")
		PageRequest request=new PageRequest(1, 10);
		Page<OrderDTO> page=orderServiceImpl.findList(request);
		page.getContent().forEach(p->{
			log.info("kk"+p.getBuyerName());
		});
		log.info("出来"+page.getPageable()+"大小"+page.getContent().size());
		Assert.assertNotNull(page);
	}
}
