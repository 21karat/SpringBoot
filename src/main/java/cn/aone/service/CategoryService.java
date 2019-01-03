package cn.aone.service;

import java.util.List;

import cn.aone.model.ProductCategory;

/**
 * 类目
 * @author 开发
 *
 */
public interface CategoryService {
	
	ProductCategory findOne(Integer categoryId);
	
	List<ProductCategory> findAll();
	
	List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);

	ProductCategory save(ProductCategory productCategory);
}
