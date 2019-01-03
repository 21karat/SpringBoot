package cn.aone.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.aone.exception.SellException;
import cn.aone.form.CategoryForm;
import cn.aone.model.ProductCategory;
import cn.aone.service.CategoryService;

/**
 * 卖家类目
 * @author 开发
 *
 */
@RestController
@RequestMapping("/seller/category")
public class SellerCategoryController {
	
	
	@Autowired
	private CategoryService categoryService;
	
	/**
	 * 查所有
	 * @param map
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	public ModelAndView list(
			Map<String, Object> map){
		List<ProductCategory> ps=categoryService.findAll();
		map.put("categoryList", ps);
		return new ModelAndView("category/list",map);
	}
	 /**
     * 展示
     * @param categoryId
     * @param map
     * @return
     */
	@RequestMapping("/index")
    public ModelAndView index(
    		@RequestParam(value = "categoryId", required = false) Integer categoryId,
                              Map<String, Object> map) {
        if (categoryId != null) {
            ProductCategory productCategory = categoryService.findOne(categoryId);
            map.put("category", productCategory);
        }
        return new ModelAndView("category/index", map);
    }
	/**
     * 保存/更新
     * @param form
     * @param bindingResult
     * @param map
     * @return
     */
	@RequestMapping("/save")
	//@CachePut(cacheNames="product",key="123")//跟新数据库数据时同时跟新缓存数据
    //@CacheEvict(cacheNames="product",key="123")//清除缓存
	public ModelAndView save(@Valid CategoryForm form,
                             BindingResult bindingResult,
                             Map<String, Object> map) {
        //效验
		if (bindingResult.hasErrors()) {
            map.put("msg", bindingResult.getFieldError().getDefaultMessage());
            map.put("url", "/springBoot/seller/category/index");
            return new ModelAndView("common/error", map);
        }
		//id存在跟新不存在新增
        ProductCategory productCategory = new ProductCategory();
        try {
            if (form.getCategoryId() != null) {
                productCategory = categoryService.findOne(form.getCategoryId());
            }
            BeanUtils.copyProperties(form, productCategory);
            productCategory.setCreateTime(new Date());
            productCategory.setUpdateTime(new Date());
            categoryService.save(productCategory);
        } catch (SellException e) {
            map.put("msg", e.getMessage());
            map.put("url", "/springBoot/seller/category/index");
            return new ModelAndView("common/error", map);
        }
        map.put("url", "/springBoot/seller/category/list");
        return new ModelAndView("common/success", map);
    }
}
