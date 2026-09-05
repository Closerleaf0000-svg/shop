package com.example.shop.controller;

import com.example.shop.entity.Order;
import com.example.shop.entity.OrderDetail;
import com.example.shop.service.OrderDetailService;
import com.example.shop.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order-detail")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class OrderDetailController {

    private final OrderDetailService orderDetailService;
    private final OrderService orderService;

    // 查詢訂單明細
    @GetMapping("/{orderId}")
    public Map<String, Object> getOrderDetails(
            @PathVariable String orderId,
            HttpSession session) {

        Map<String, Object> result = new HashMap<>();

        Long memberId = (Long) session.getAttribute("memberId");
        String role = (String) session.getAttribute("role");

        // 沒登入
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
            result.put("message", "找不到此訂單");

            return result;
        }

        // USER 只能查看自己的訂單
        if (!"RULER".equals(role)
                && !order.getMemberId().equals(memberId)) {

            result.put("success", false);
            result.put("message", "無權限查看此訂單");

            return result;
        }

        // 查詢訂單明細
        List<OrderDetail> orderDetails = orderDetailService.getOrderDetails(orderId);

        result.put("success", true);
        result.put("orderDetails", orderDetails);

        return result;
    }
}