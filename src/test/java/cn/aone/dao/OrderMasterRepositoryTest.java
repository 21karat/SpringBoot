package cn.aone.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.aone.dao.OrderDetailRepository;
import cn.aone.dao.OrderMasterRepository;
import cn.aone.model.OrderDetail;
import cn.aone.model.OrderMaster;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderMasterRepositoryTest {
	
	@Autowired
	private OrderDetailRepository orderDetail;
	
	@Autowired
	private OrderMasterRepository order;
	
	@Test
	public void test(){
		OrderMaster o=new OrderMaster();
		o.setBuyerAddress("是个");
		o.setBuyerName("哦通过");
		o.setBuyerOpenid("123");
		o.setBuyerPhone("11010");
		o.setCreateTime(new Date());
		o.setOrderAmount(new BigDecimal(2.3));
		o.setOrderId("1");
		o.setOrderStatus(1);
		o.setPayStatus(1);
		o.setUpdateTime(new Date());
		
		OrderMaster orderMaster=order.save(o);
		System.out.println(orderMaster.getBuyerName());
	}
	@Test
	public void test1(){
		@SuppressWarnings("deprecation")
		PageRequest request=new PageRequest(0, 2);
		Page<OrderMaster> result=order.findByBuyerOpenid("123", request);
		Assert.assertNotEquals(0, result.getTotalElements());
		System.out.println(result.getTotalElements());
	}
	@Test
	public void test2(){
		OrderDetail o=new OrderDetail();
		o.setDetailId("33");
		o.setOrderId("1");
		o.setProductIcon("fhd11");
		o.setProductId("222");
		o.setProductName("sgsgs馆");
		o.setProductPrice(new BigDecimal(2.2));
		o.setProductQuantity(22);
		OrderDetail order=orderDetail.save(o);
		System.out.println(order.getProductName());
	}
	@Test
	public void test3(){
		List<OrderDetail> orderDetails=orderDetail.findByOrderId("1");
		orderDetails.forEach(o->{
			System.out.println(o.getProductName());
		});
	}
}
