package cn.aone.service;

import cn.aone.model.SellerInfo;

/**
 * 卖家端
 * @author 开发
 *
 */
public interface SellerService {
	//通过openid查询卖家端信息
	SellerInfo findSellerInfoByOpenId(String openid);
}
