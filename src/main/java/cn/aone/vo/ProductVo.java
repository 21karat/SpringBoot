package cn.aone.vo;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 商品(包含类目)
 * @author 开发
 *
 */
public class ProductVo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6324555918165609666L;
	@JsonProperty("name")
	private String categoryName;
	@JsonProperty("type")//返回前端的字段样式将会由categoryType变成type
    private Integer categoryType;
	@JsonProperty("foods")
    private List<ProductInfoVO> productInfoVOList;
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public Integer getCategoryType() {
		return categoryType;
	}
	public void setCategoryType(Integer categoryType) {
		this.categoryType = categoryType;
	}
	public List<ProductInfoVO> getProductInfoVOList() {
		return productInfoVOList;
	}
	public void setProductInfoVOList(List<ProductInfoVO> productInfoVOList) {
		this.productInfoVOList = productInfoVOList;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
}
