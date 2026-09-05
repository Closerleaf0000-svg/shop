package com.example.shop.service;

import com.example.shop.entity.OrderDetail;
import com.example.shop.mapper.OrderDetailMapper;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class OrderDetailService {

    private final OrderDetailMapper orderDetailMapper;

    public OrderDetailService(OrderDetailMapper orderDetailMapper) {
        this.orderDetailMapper = orderDetailMapper;
    }

    // 新增訂單明細
    public void createOrderDetail(OrderDetail orderDetail) {
        orderDetailMapper.insert(orderDetail);
    }

    // 查詢訂單明細
    public List<OrderDetail> getOrderDetails(String orderId) {
        return orderDetailMapper.findByOrderId(orderId);
    }

}