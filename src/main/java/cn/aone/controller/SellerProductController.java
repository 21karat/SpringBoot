package cn.aone.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
/**
 * 卖家商品
 * @author 开发
 *
 */
import org.springframework.web.servlet.ModelAndView;

import cn.aone.exception.SellException;
import cn.aone.form.ProductForm;
import cn.aone.model.ProductCategory;
import cn.aone.model.ProductInfo;
import cn.aone.service.CategoryService;
import cn.aone.service.ProductService;
import cn.aone.util.KeyUtil;
@RestController
@RequestMapping("/seller/product")
public class SellerProductController {
	
	@Autowired
	private ProductService productService;
	@Autowired
	private CategoryService categoryService; 
	
	/**
	 * 查所有商品
	 * @param page
	 * @param size
	 * @param map
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping("/list")
	@ResponseBody
	public ModelAndView list(
			@RequestParam(value="page",defaultValue = "1") Integer page,
			@RequestParam(value="size",defaultValue = "3") Integer size,
			Map<String,Object> map){
		PageRequest request=new PageRequest(page-1, size);
		Page<ProductInfo> page2=productService.findAll(request);
		map.put("productInfoPage", page2);
		map.put("currentPage", page);
		map.put("size", size);
		
		return new ModelAndView("product/list",map);
	}
	/**
	 * 上架
	 * @param productId
	 * @param map
	 * @return
	 */
	@RequestMapping("/on_sale")
	@ResponseBody
	public ModelAndView onSale(
			@RequestParam("productId") String productId,
			Map<String, Object> map){
		   	try {
	            productService.onSale(productId);
	        } catch (SellException e) {
	            map.put("msg", e.getMessage());
	            map.put("url", "/springBoot/seller/product/list");
	            return new ModelAndView("common/error", map);
	        }

	        map.put("url", "/springBoot/seller/product/list");
	        return new ModelAndView("common/success", map);
	}
	/**
	 * 下架
	 * @param productId
	 * @param map
	 * @return
	 */
	@RequestMapping("/off_sale")
	@ResponseBody
	public ModelAndView offSale(
			@RequestParam("productId") String productId,
			Map<String, Object> map){
		   	try {
	            productService.offSale(productId);
	        } catch (SellException e) {
	            map.put("msg", e.getMessage());
	            map.put("url", "/springBoot/seller/product/list");
	            return new ModelAndView("common/error", map);
	        }
	        map.put("url", "/springBoot/seller/product/list");
	        return new ModelAndView("common/success", map);
	}
	/**
	 * 
	 * @param productId
	 * @param map
	 * @return
	 */
	@RequestMapping("/index")
	@ResponseBody
    public ModelAndView index(@RequestParam(value = "productId", required = false) String productId,
                      Map<String, Object> map) {
        if (!StringUtils.isEmpty(productId)) {
            ProductInfo productInfo = productService.findOne(productId);
            map.put("productInfo", productInfo);
        }

        //查询所有的类目
        List<ProductCategory> categoryList = categoryService.findAll();
        map.put("categoryList", categoryList);

        return new ModelAndView("product/index", map);
    }
	/**
	 * 添加跟新
	 * @param form
	 * @param bindingResult
	 * @param map
	 * @return
	 */
	@RequestMapping("/save")
	@ResponseBody
	public ModelAndView save(
			@Valid ProductForm form,
            BindingResult bindingResult,
            Map<String, Object> map){
		//效验(注解效验失败跳转错误页面)
		if (bindingResult.hasErrors()) {
            map.put("msg", bindingResult.getFieldError().getDefaultMessage());
            map.put("url", "/springBoot/seller/product/index");
            return new ModelAndView("common/error", map);
        }
		//有id更新/无id新增
        ProductInfo productInfo = new ProductInfo();
        try {
            //如果productId为空, 说明是新增
            if (!StringUtils.isEmpty(form.getProductId())) {
                productInfo = productService.findOne(form.getProductId());
            } else {
                form.setProductId(KeyUtil.genUniqueKey());
            }
            BeanUtils.copyProperties(form, productInfo);
            productInfo.setCreateTime(new Date());
            productInfo.setUpdateTime(new Date());
            productService.save(productInfo);
        } catch (SellException e) {
            map.put("msg", e.getMessage());
            map.put("url", "/springBoot/seller/product/index");
            return new ModelAndView("common/error", map);
        }

        map.put("url", "/springBoot/seller/product/list");
        return new ModelAndView("common/success", map);
	}
}
