package cn.aone.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.aone.model.ProductCategory;
import cn.aone.service.impl.CategoryServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryServiceImplTest {
	@Autowired
	private  CategoryServiceImpl categoryServiceImpl;
	
	@Test
	public void findOne() throws Exception{
		ProductCategory productCategory=categoryServiceImpl.findOne(new Integer(1));
		Assert.assertEquals(new Integer(1), productCategory.getCategoryId());
	}	
	@Test
	public void findAll() throws Exception{
		List<ProductCategory> productCategories=categoryServiceImpl.findAll();
		productCategories.forEach(p->{
			System.out.println(p.getCategoryName());
			if(p.getCategoryId().equals(1)){
				System.out.println(p.getCategoryName());
			}
		});
	}
	@Test
	public void findByCategoryTypeIn() throws Exception{
		List<ProductCategory> productCategories=categoryServiceImpl.findByCategoryTypeIn(Arrays.asList(3,10,4,12));
		productCategories.forEach(p->{
			System.out.println(p.getCategoryName()+":"+p.getCategoryType());
		});
		Assert.assertNotEquals(0, productCategories);
	}
	@Test
	public void save() throws Exception{
		ProductCategory productCategory=new ProductCategory("666",8);
		productCategory.setCreateTime(new Date());
		productCategory.setUpdateTime(new Date());
		ProductCategory result=categoryServiceImpl.save(productCategory);
		Assert.assertNotNull(result);
	}
	
}
