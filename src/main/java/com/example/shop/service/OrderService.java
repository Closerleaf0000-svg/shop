package com.example.shop.service;

import com.example.shop.entity.Order;
import com.example.shop.entity.OrderDetail;
import com.example.shop.entity.Product;
import com.example.shop.mapper.OrderMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

        private final OrderMapper orderMapper;
        private final OrderDetailService orderDetailService;
        private final ProductService productService;

        public OrderService(
                        OrderMapper orderMapper,
                        OrderDetailService orderDetailService,
                        ProductService productService) {

                this.orderMapper = orderMapper;
                this.orderDetailService = orderDetailService;
                this.productService = productService;
        }

        @Transactional
        public void createOrder(
                        Order order,
                        List<OrderDetail> orderDetails) {

                int totalPrice = 0;

                for (OrderDetail detail : orderDetails) {

                        Product product = productService.findByProductId(
                                        detail.getProductId());

                        if (product == null) {
                                throw new RuntimeException(
                                                "商品不存在：" + detail.getProductId());
                        }

                        if (detail.getQuantity() <= 0) {
                                throw new RuntimeException(
                                                "商品數量必須大於 0");
                        }

                        if (detail.getQuantity() > product.getQuantity()) {
                                throw new RuntimeException(
                                                "庫存不足：" + product.getProductName());
                        }

                        int standPrice = product.getPrice();
                        int itemPrice = standPrice * detail.getQuantity();

                        detail.setStandPrice(standPrice);
                        detail.setItemPrice(itemPrice);

                        totalPrice += itemPrice;
                }

                order.setPrice(totalPrice);

                orderMapper.insert(order);

                for (OrderDetail detail : orderDetails) {

                        detail.setOrderId(order.getOrderId());

                        orderDetailService.createOrderDetail(detail);

                        productService.decreaseQuantity(
                                        detail.getProductId(),
                                        detail.getQuantity());
                }
        }

        public Order getOrder(String orderId) {
                return orderMapper.findByOrderId(orderId);
        }

        // 管理者查詢所有訂單
        public List<Order> findAllOrders() {
                return orderMapper.findAll();
        }
}