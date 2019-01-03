package cn.aone.service;

public interface SeckillService {
	
	//查询
	String	querySeckillProductInfo(String productId);
	//下单
	void orderProductMockDiffUser(String productId);
}
