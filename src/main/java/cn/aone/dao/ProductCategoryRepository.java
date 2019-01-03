package cn.aone.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.aone.model.ProductCategory;
//类目																//对象		               主键
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {
	 List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);
}
