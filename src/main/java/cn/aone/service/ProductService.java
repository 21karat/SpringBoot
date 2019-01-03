package cn.aone.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.aone.dto.CartDTO;
import cn.aone.model.ProductInfo;
/**
 * 商品
 * @author 开发
 *
 */
public interface ProductService {
	
		//根据id查单件
	 	ProductInfo findOne(String productId);
	    //查询所有在架商品列表
	    List<ProductInfo> findUpAll();
	    //分页查
	    Page<ProductInfo> findAll(Pageable pageable);
	    //保存
	    ProductInfo save(ProductInfo productInfo);

	    //加库存
	    void increaseStock(List<CartDTO> cartDTOList);

	    //减库存
	    void decreaseStock(List<CartDTO> cartDTOList);

	    //上架
	    ProductInfo onSale(String productId);

	    //下架
	    ProductInfo offSale(String productId);
}
