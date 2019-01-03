package cn.aone.service;

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
import org.springframework.transaction.annotation.Transactional;

import cn.aone.model.ProductInfo;
import cn.aone.service.impl.ProductServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceImplTest {
	
	@Autowired
	private ProductServiceImpl productServiceImpl;
	
	@Test
	@Transactional//懒加载
	public void test1(){
		ProductInfo productInfo=productServiceImpl.findOne("1");
		System.out.println(productInfo.getProductDescription());
		System.out.println(productInfo==null);
		Assert.assertEquals("1", productInfo.getProductId());
	}
	@Test
	public void test2(){
		List<ProductInfo> productInfos=productServiceImpl.findUpAll();		
		productInfos.forEach(p->{
			System.out.println(p.getProductDescription());
		});
	}
	@Test
	public void test3(){
		@SuppressWarnings("deprecation")
		PageRequest request=new PageRequest(0, 2);
		Page<ProductInfo>  pd=productServiceImpl.findAll(request);
		System.out.println(pd.getTotalElements());
	}
	@Test
	public void test4(){
		ProductInfo productInfo=new ProductInfo();
		productInfo.setProductId("22");
	    productInfo.setProductName("方式");
	    productInfo.setProductPrice(new BigDecimal(3.2));
	    productInfo.setProductStock(100);
	    productInfo.setProductDescription("闪光灯");
	    productInfo.setProductIcon("http://xxxxx.jpg");
	    productInfo.setProductStatus(0);
	    productInfo.setCategoryType(2);
	    productInfo.setCreateTime(new Date());
	    productInfo.setUpdateTime(new Date());
		productServiceImpl.save(productInfo);
	}
	@Test
	public void test5(){
		
	}
	@Test
	public void test6(){
		
	}
	@Test
	public void test7(){
		productServiceImpl.onSale("1");
	}
	@Test
	public void test8(){
		productServiceImpl.offSale("2");
	}
}
