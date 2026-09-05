package com.example.shop.mapper;

import com.example.shop.entity.OrderDetail;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderDetailMapper {

    // 新增訂單明細
    int insert(OrderDetail orderDetail);

    // 查詢訂單明細
    List<OrderDetail> findByOrderId(
            @Param("orderId") String orderId);

}