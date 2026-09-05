package com.example.shop.controller;

import com.example.shop.dto.OrderRequest;
import com.example.shop.entity.Order;
import com.example.shop.entity.OrderDetail;
import com.example.shop.service.OrderService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class OrderController {

    private final OrderService orderService;

    // 建立訂單
    @PostMapping
    public Map<String, Object> createOrder(
            @Valid @RequestBody OrderRequest request,
            HttpSession session) {

        Map<String, Object> result = new HashMap<>();

        // 從 Session 取得會員 ID
        Long memberId = (Long) session.getAttribute("memberId");

        // 從 Session 取得角色
        String role = (String) session.getAttribute("role");

        // 尚未登入
        if (memberId == null) {

            result.put("success", false);
            result.put("message", "請先登入");

            return result;
        }

        // 管理者不能建立訂單
        if ("RULER".equals(role)) {

            result.put("success", false);
            result.put("message", "管理者不能建立訂單");

            return result;
        }

        // 建立 Order Entity
        Order order = new Order();

        // 訂單編號
        order.setOrderId(request.getOrderId());

        // 付款狀態
        order.setPayStatus(request.getPayStatus());

        // 會員 ID 不相信前端
        // 使用 Session 裡的會員 ID
        order.setMemberId(memberId);

        // DTO → Entity
        List<OrderDetail> orderDetails = request.getOrderDetails()
                .stream()
                .map(detailRequest -> {

                    OrderDetail detail = new OrderDetail();

                    // 商品編號
                    detail.setProductId(
                            detailRequest.getProductId());

                    // 購買數量
                    detail.setQuantity(
                            detailRequest.getQuantity());

                    return detail;
                })
                .toList();

        // 建立訂單
        orderService.createOrder(
                order,
                orderDetails);

        // 回傳結果
        result.put("success", true);
        result.put("message", "訂單建立成功");
        result.put("orderId", order.getOrderId());

        return result;
    }

    // 管理者查詢所有訂單
    @GetMapping("/all")
    public Map<String, Object> getAllOrders(
            HttpSession session) {

        Map<String, Object> result = new HashMap<>();

        // 取得登入者角色
        String role = (String) session.getAttribute("role");

        // 只有 RULER 可以查看所有訂單
        if (!"RULER".equals(role)) {

            result.put("success", false);
            result.put(
                    "message",
                    "沒有權限查看所有訂單");

            return result;
        }

        // 查詢所有訂單
        List<Order> orders = orderService.findAllOrders();

        result.put("success", true);
        result.put("orders", orders);

        return result;
    }

    // 查詢單筆訂單
    @GetMapping("/{orderId}")
    public Map<String, Object> getOrder(
            @PathVariable String orderId,
            HttpSession session) {

        Map<String, Object> result = new HashMap<>();

        // 取得目前登入會員 ID
        Long memberId = (Long) session.getAttribute("memberId");

        // 取得目前登入者角色
        String role = (String) session.getAttribute("role");

        // 尚未登入
        if (memberId == null) {

            result.put("success", false);
            result.put("message", "請先登入");

            return result;
        }

        // 查詢訂單
        Order order = orderService.getOrder(orderId);

        // 找不到訂單
        if (order == null) {

            result.put("success", false);
            result.put("message", "找不到訂單");

            return result;
        }

        // 管理者
        if ("RULER".equals(role)) {

            result.put("success", true);
            result.put("order", order);

            return result;
        }

        // 一般會員只能查看自己的訂單
        if (!order.getMemberId().equals(memberId)) {

            result.put("success", false);
            result.put("message", "無權限查看此訂單");

            return result;
        }

        result.put("success", true);
        result.put("order", order);

        return result;
    }
}