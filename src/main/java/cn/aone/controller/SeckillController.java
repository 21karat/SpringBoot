package cn.aone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.aone.service.SeckillService;
import lombok.extern.slf4j.Slf4j;
/**
 * 秒杀
 * @author 开发
 *
 */
@RestController
@RequestMapping("/skill")
@Slf4j
public class SeckillController {
	
	@Autowired
	private SeckillService seckillService;
	/**
	 * 查询秒杀商品信息
	 * @param productId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/query/{productId}")
	public String query(@PathVariable String productId) throws Exception{
		return seckillService.querySeckillProductInfo(productId);
	}
	/**
	 * 秒杀，没抢到提示，抢到返回剩余库存信息
	 * @param productId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/order/{productId}")
	public String skill(@PathVariable String productId) throws Exception{
		log.info("@skill request,productId:"+productId);
		seckillService.orderProductMockDiffUser(productId);
		return seckillService.querySeckillProductInfo(productId);
	}
	
}
