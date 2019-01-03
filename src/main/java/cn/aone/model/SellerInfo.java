package cn.aone.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 用户登陆信息表
 */
@Data
@Entity
public class SellerInfo {

    @Id
    private String sellerId;

    private String username;

    private String password;

    private String openid;
    
    
    
}
