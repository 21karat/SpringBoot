package cn.aone.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.aone.model.SellerInfo;
import cn.aone.service.SellerService;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SellerServiceImplTest {
	
	@Autowired//就是自动装配，其作用是为了消除代码Java代码里面的getter/setter与bean属性中的property。
	private SellerService sellerService;
	
	@Test
	public void test(){
		SellerInfo sellerInfo=sellerService.findSellerInfoByOpenId("abc");
		log.info(sellerInfo.getUsername());
	}
	
}
