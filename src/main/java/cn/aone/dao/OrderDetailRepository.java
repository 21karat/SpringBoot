package cn.aone.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.aone.model.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, String>{
	List<OrderDetail> findByOrderId(String OrderId);
}
