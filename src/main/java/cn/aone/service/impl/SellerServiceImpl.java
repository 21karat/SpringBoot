package cn.aone.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.aone.dao.SellerInfoRepository;
import cn.aone.model.SellerInfo;
import cn.aone.service.SellerService;
@Service
public class SellerServiceImpl implements SellerService{

	
	@Autowired
	private SellerInfoRepository repository;	
	/**
	 * 通过openid查询用户信息
	 */
	@Override
	public SellerInfo findSellerInfoByOpenId(String openid) {
		// TODO Auto-generated method stub
		return repository.findByOpenid(openid);
	}

}
