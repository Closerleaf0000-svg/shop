package com.example.shop.mapper;

import com.example.shop.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper {

    int insert(Order order);

    Order findByOrderId(
            @Param("orderId") String orderId);

    // 查詢所有訂單
    List<Order> findAll();
}