package cn.aone.dao;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.aone.dao.ProductInfoRepository;
import cn.aone.model.ProductInfo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductInfoRepositoryTest {
	
	@Autowired
	private ProductInfoRepository repository;
	
	@Test
	public void test(){
		List<ProductInfo> productInfos=repository.findByProductStatus(1);
		productInfos.forEach(p->{
			System.out.println(p.getProductName());
		});
	}	
	@Test
    public void saveTest() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductId("123456");
        productInfo.setProductName("皮蛋粥");
        productInfo.setProductPrice(new BigDecimal(3.2));
        productInfo.setProductStock(100);
        productInfo.setProductDescription("很好喝的粥");
        productInfo.setProductIcon("http://xxxxx.jpg");
        productInfo.setProductStatus(0);
        productInfo.setCategoryType(2);
        productInfo.setCreateTime(new Date());
        productInfo.setUpdateTime(new Date());

        ProductInfo result = repository.save(productInfo);
        Assert.assertNotNull(result);
	}
	@Test
	public void test1(){
		repository.getOne("1");
	}
	
}
