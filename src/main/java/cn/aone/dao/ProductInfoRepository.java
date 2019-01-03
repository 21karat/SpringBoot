package cn.aone.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.aone.model.ProductInfo;
//商品
public interface ProductInfoRepository extends JpaRepository<ProductInfo, String> {
	 List<ProductInfo> findByProductStatus(Integer productStatus);
}
