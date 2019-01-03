package cn.aone.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicUpdate;

/**
 * 
 * @author 开发
 *
 */
@Entity//类映射到表
@DynamicUpdate//动态更新
//@Data(省去get/set方法的注解)
public class ProductCategory implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6316480293087494219L;

	/**类目id**/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增类型
    private Integer categoryId;

    /**类目名字**/
    private String categoryName;

    /**类目编号**/
    private Integer categoryType;
    
    /**创建时间**/
    private Date createTime;
    
    /**更新时间**/
    private Date updateTime;

    public ProductCategory() {
    }

    public ProductCategory(String categoryName, Integer categoryType) {
        this.categoryName = categoryName;
        this.categoryType = categoryType;
    }

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

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
	
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "ProductCategory [categoryId=" + categoryId + ", categoryName=" + categoryName + ", categoryType="
				+ categoryType + "]";
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
}
