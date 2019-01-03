package cn.aone.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.aone.model.SellerInfo;

public interface SellerInfoRepository extends JpaRepository<SellerInfo, String>{
	//通过openId查询信息
	SellerInfo findByOpenid(String openid);
}
