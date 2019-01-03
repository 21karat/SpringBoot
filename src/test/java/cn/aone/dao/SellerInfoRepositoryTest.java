package cn.aone.dao;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.aone.dao.SellerInfoRepository;
import cn.aone.util.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import cn.aone.model.SellerInfo;




@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SellerInfoRepositoryTest {
	
	@Autowired
	private SellerInfoRepository repository;
	
	@Test
	public void test(){
		SellerInfo sellerInfo = new SellerInfo();
        sellerInfo.setSellerId(KeyUtil.genUniqueKey());
        sellerInfo.setUsername("root");
        sellerInfo.setPassword("root");
        sellerInfo.setOpenid("root");
        SellerInfo result = repository.save(sellerInfo);
        log.info(result.getUsername());
	}
	@Test
	public void test1() throws Exception{
		SellerInfo reInfo=repository.findByOpenid("1");
		log.info("打印"+reInfo.getUsername());
	}
}
