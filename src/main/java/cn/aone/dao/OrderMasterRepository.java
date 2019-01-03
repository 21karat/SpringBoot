package cn.aone.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cn.aone.model.OrderMaster;

public interface OrderMasterRepository extends JpaRepository<OrderMaster,String>{
	//按照openid分页查
	Page<OrderMaster> findByBuyerOpenid(String buyerOpenId,Pageable pageable);
}
