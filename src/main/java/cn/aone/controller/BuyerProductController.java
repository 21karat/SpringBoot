package cn.aone.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.aone.model.ProductCategory;
import cn.aone.model.ProductInfo;
import cn.aone.service.CategoryService;
import cn.aone.service.ProductService;
import cn.aone.util.ResultVOUtil;
import cn.aone.vo.ProductInfoVO;
import cn.aone.vo.ProductVo;
import cn.aone.vo.ResultVo;

/**
 * 买家商品
 * @author 开发
 *
 */
//@RestController
@Controller
@RequestMapping("/buyer/product")
public class BuyerProductController {
	
	@Autowired
	private ProductService productService;
	@Autowired 
	private CategoryService categoryService;
	
	//@Transactional//懒加载
	@SuppressWarnings("rawtypes")
	//@GetMapping("/list")
	@RequestMapping("/list")
	@Cacheable(cacheNames="product",key="#sellerId",condition="#sellerId.length()>3",unless="#result.getCode!=0")
	//达到入参长度大于3，结果为0正确时再缓存
	//redis缓存注解
	public ResultVo list(@RequestParam("sellerId") String sellerId){
		//1.查询所有上架商品
		List<ProductInfo> productInfos=productService.findUpAll();
		//2.查询类目(一次性查询)
		//List<Integer> categoryTypeList=new ArrayList<>();
		//传统方法
		/*for(ProductInfo p:productInfos){
			categoryTypeList.add(p.getCategoryType());
		}*/
		//精简方法
		List<Integer> categoryTypeList = productInfos.stream()
				.map(e->e.getCategoryType())
				.collect(Collectors.toList());
		
		List<ProductCategory> productCategories=categoryService.findByCategoryTypeIn(categoryTypeList);
		
		//数据拼装
		List<ProductVo> pList=new ArrayList<>();
		productCategories.forEach(p->{
			ProductVo productVo=new ProductVo();
			productVo.setCategoryName(p.getCategoryName());
			productVo.setCategoryType(p.getCategoryType());
			
			List<ProductInfoVO> productInfoVOList=new ArrayList<>();
			productInfos.forEach(p1->{
				ProductInfoVO productInfoVO=new ProductInfoVO();
				productInfoVO.setProductDescription(p1.getProductDescription());
				productInfoVO.setProductIcon(p1.getProductIcon());
				productInfoVO.setProductId(p1.getProductId());
				productInfoVO.setProductName(p1.getProductName());
				productInfoVO.setProductPrice(p1.getProductPrice());
				productInfoVOList.add(productInfoVO);
				
			});
			productVo.setProductInfoVOList(productInfoVOList);
			pList.add(productVo);
		});

		return ResultVOUtil.success(pList);
	}
}
