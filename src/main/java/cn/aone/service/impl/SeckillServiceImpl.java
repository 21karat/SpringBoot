package cn.aone.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.aone.exception.SellException;
import cn.aone.service.RedisLock;
import cn.aone.service.SeckillService;
import cn.aone.util.KeyUtil;
@Service
public class SeckillServiceImpl implements SeckillService{
	
	private static final int TIMEOUT=10*1000;//超时时间10秒
	
	@Autowired
	private RedisLock redisLock;
	
	/**
	 * 国庆活动，特价，限量100000份
	 */
	static Map<String,Integer> products;
	static Map<String,Integer> stock;
	static Map<String,String> orders;
	static{
		/**
		 * 模拟多个表，商品信息表，库存表，秒杀成功订单表
		 */
		products=new HashMap<>();
		stock=new HashMap<>();
		orders=new HashMap<>();
		products.put("123456", 100000);
		stock.put("123456", 100000);
	}
	
	/**
	 * 查询
	 */
	private String queryMap(String productId){
		return "国庆活动，价格优惠限量"
				+products.get(productId)
				+"还剩："+stock.get(productId)+"份"
				+"改商品成功下单用户数目"
				+orders.size()+"人";
	}
	
	/**
	 * 查询商品
	 */
	@Override
	public String querySeckillProductInfo(String productId) {
		// TODO Auto-generated method stub
		return this.queryMap(productId);
	}

	/**
	 * 下单
	 */
	@Override
	public synchronized void orderProductMockDiffUser(String productId) {
		// TODO Auto-generated method stub
		//1.查询该商品库存，为0则活动结束
		int stockNum=stock.get(productId);
		if(stockNum==0){
			throw new SellException(100, "活动结束");
		}else{
			//2.下单(模拟不同用户openid不同)
			orders.put(KeyUtil.genUniqueKey(),productId);
			//3.减库存
			stockNum=stockNum-1;
			try{
				Thread.sleep(100);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			stock.put(productId, stockNum);
		}
	}
	/*@Override
	public void orderProductMockDiffUser(String productId) {
		// TODO Auto-generated method stub
		//加锁
		long time=System.currentTimeMillis()+TIMEOUT;
		//判断加锁是否成功
		if(!redisLock.lock(productId, String.valueOf(time))){
			throw new SellException(101,"哎呦喂，人也太多了，换个姿势再试试~");
		}
		
		//1.查询该商品库存，为0则活动结束
		int stockNum=stock.get(productId);
		if(stockNum==0){
			throw new SellException(100, "活动结束");
		}else{
			//2.下单(模拟不同用户openid不同)
			orders.put(KeyUtil.genUniqueKey(),productId);
			//3.减库存
			stockNum=stockNum-1;
			try{
				Thread.sleep(100);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			stock.put(productId, stockNum);
		}
		
		//解锁
		redisLock.unlock(productId, String.valueOf(time));
	}*/
	
}
