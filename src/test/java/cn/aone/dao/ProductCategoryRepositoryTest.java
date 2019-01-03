package cn.aone.dao;

import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.aone.dao.ProductCategoryRepository;
import cn.aone.model.ProductCategory;

/**
 * 
 * @author 开发
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryRepositoryTest {
	@Autowired
	private  ProductCategoryRepository repository;
	
	@Test
	public  void findAllTest(){
		List<ProductCategory> productCategoryList = repository.findAll();
		productCategoryList.forEach(p->{
			System.out.println(p.toString());
		});
	}	
	@Test
	public void insertOneTest(){
		ProductCategory productCategory=new ProductCategory();
		productCategory.setCategoryName("33");
		productCategory.setCategoryType(4);
		repository.save(productCategory);
	}
	@Test
	@Transactional//事务回滚(完全回滚，测试完成，数据仍是之前的数据)
	public void updateBuIdOneTest(){
		ProductCategory productCategory=new ProductCategory();
		productCategory.setCategoryId(3);
		productCategory.setCategoryName("小明");
		productCategory.setCategoryType(10);
		repository.save(productCategory);
	}	
	@Test
	public void delOneTest(){
		ProductCategory productCategory=new ProductCategory();
		productCategory.setCategoryId(2);
		repository.delete(productCategory);
	}
	@Test
	public void findByCategoryTypeInTest(){
		List<Integer> list=Arrays.asList(3,10);
		List<ProductCategory> result=repository.findByCategoryTypeIn(list);
		Assert.assertNotEquals(0, result.size());
	}
}
