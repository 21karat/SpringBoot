package cn.aone.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.aone.dao.ProductInfoRepository;
import cn.aone.dto.CartDTO;
import cn.aone.enums.ProductStatusEnum;
import cn.aone.enums.ResultEnum;
import cn.aone.exception.SellException;
import cn.aone.model.ProductInfo;
import cn.aone.service.ProductService;
/**
 * 商品
 * @author 开发
 *
 */
@Service
public class ProductServiceImpl implements ProductService{

	@Autowired
	private ProductInfoRepository repository;
	
	//查单件
	@Override
	//@Cacheable(cacheNames="product",key="123")//redis缓存注解
	public ProductInfo findOne(String productId) {
		// TODO Auto-generated method stub
		return repository.getOne(productId);
	}
	//根据上架状态查询
	@Override
	public List<ProductInfo> findUpAll() {
		// TODO Auto-generated method stub
		return repository.findByProductStatus(ProductStatusEnum.UP.getCode());
	}
	//分页查
	@Override
	public Page<ProductInfo> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return repository.findAll(pageable);
	}
	//保存
	@Override
	//@CachePut(cacheNames="product",key="123")
	public ProductInfo save(ProductInfo productInfo) {
		// TODO Auto-generated method stub
		return repository.save(productInfo);
	}
	//加库存
	@Override
	public void increaseStock(List<CartDTO> cartDTOList) {
		// TODO Auto-generated method stub
		cartDTOList.forEach(c->{
			ProductInfo productInfo=repository.getOne(c.getProductId());
			if(productInfo==null){
				new SellException(ResultEnum.PRODUCT_NOT_EXIST);
			}
			
			Integer result=productInfo.getProductStock()+c.getProductQuantity();
			productInfo.setProductStock(result);
			repository.save(productInfo);
		});
	}
	//减库存
	@Override
	@Transactional//加上事务，要么全部成功要么都不成功
	public void decreaseStock(List<CartDTO> cartDTOList) {
		// TODO Auto-generated method stub
		cartDTOList.forEach(c->{
			ProductInfo productInfo=repository.getOne(c.getProductId());
			if(productInfo==null){
				new SellException(ResultEnum.PRODUCT_NOT_EXIST);
			}
			
			Integer result=productInfo.getProductStock()-c.getProductQuantity();
			if(result<0){
				throw new SellException(ResultEnum.PRODUCT_STOCK_ERROR);
			}
			
			productInfo.setProductStock(result);
			repository.save(productInfo);
		});
	}

	@Override//上架
	public ProductInfo onSale(String productId) {
		System.out.println(productId);
		ProductInfo productInfo = repository.getOne(productId);
        if (productInfo == null) {
            throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);//抛出商品状态不正确异常
        }
        if (productInfo.getProductStatusEnum() == ProductStatusEnum.UP) {
            throw new SellException(ResultEnum.PRODUCT_STATUS_ERROR);//抛出商品状态不正确异常
        }

        //更新
        productInfo.setProductStatus(ProductStatusEnum.UP.getCode());//商品状态上架
        return repository.save(productInfo);
	}

	@Override//下架
	public ProductInfo offSale(String productId) {
		// TODO Auto-generated method stub
		ProductInfo productInfo=repository.getOne(productId);
		if(productInfo==null){
			throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);//抛出商品状态不正确异常
		}
		if(productInfo.getProductStatus().equals("1")){
			throw new SellException(ResultEnum.PRODUCT_STATUS_ERROR);//抛出商品状态不正确异常
		}
		
		productInfo.setProductStatus(ProductStatusEnum.DOWN.getCode());//商品状态下架
		return repository.save(productInfo);
	}

}
