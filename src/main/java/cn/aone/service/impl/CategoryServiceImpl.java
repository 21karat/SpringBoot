package cn.aone.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.aone.dao.ProductCategoryRepository;
import cn.aone.model.ProductCategory;
import cn.aone.service.CategoryService;
/**
 * 类目
 * @author 开发
 *
 */
@Service
public class CategoryServiceImpl implements CategoryService{
	
	@Autowired
	private	ProductCategoryRepository repository; 
	@Override
	//@Cacheable(cacheNames="product",key="123")//redis缓存注解
	public ProductCategory findOne(Integer categoryId) {
		// TODO Auto-generated method stub
		return repository.getOne(categoryId);
	}

	@Override
	public List<ProductCategory> findAll() {
		// TODO Auto-generated method stub
		return repository.findAll();
	}

	@Override
	public List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList) {
		// TODO Auto-generated method stub
		return repository.findByCategoryTypeIn(categoryTypeList);
	}

	@Override
	//@CachePut(cacheNames="product",key="123")
	public ProductCategory save(ProductCategory productCategory) {
		// TODO Auto-generated method stub
		return repository.save(productCategory);
	}

}
