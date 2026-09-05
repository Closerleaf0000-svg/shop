package com.example.shop.controller;

import com.example.shop.dto.CartRequest;
import com.example.shop.entity.Cart;
import com.example.shop.service.CartService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // 加入購物車
    @PostMapping
    public String addToCart(
            @Valid @RequestBody CartRequest request,
            HttpSession session) {

        // 從 Session 取得目前登入會員 ID
        Long memberId = (Long) session.getAttribute("memberId");

        // 從 Session 取得目前登入者角色
        String role = (String) session.getAttribute("role");

        // 尚未登入
        if (memberId == null) {

            return "請先登入";
        }

        // 管理員不能使用購物車
        if ("RULER".equals(role)) {

            return "管理員不能使用購物車";
        }

        // DTO → Entity
        Cart cart = new Cart();
        cart.setMemberId(memberId);
        cart.setProductId(request.getProductId());
        cart.setQuantity(request.getQuantity());

        // 加入購物車
        cartService.addToCart(cart);

        return "商品已加入購物車";
    }

    // 查詢目前登入會員自己的購物車
    @GetMapping
    public List<Cart> getMyCart(
            HttpSession session) {

        Long memberId = (Long) session.getAttribute("memberId");
        String role = (String) session.getAttribute("role");

        // 尚未登入
        if (memberId == null) {

            return List.of();
        }

        // 管理員沒有購物車
        if ("RULER".equals(role)) {

            return List.of();
        }

        return cartService.findByMemberId(memberId);
    }

    // 修改商品數量
    @PutMapping
    public String updateQuantity(
            @RequestParam String productId,
            @RequestParam Integer quantity,
            HttpSession session) {

        Long memberId = (Long) session.getAttribute("memberId");
        String role = (String) session.getAttribute("role");

        // 尚未登入
        if (memberId == null) {

            return "請先登入";
        }

        // 管理員不能使用購物車
        if ("RULER".equals(role)) {

            return "管理員不能使用購物車";
        }

        // 修改購物車數量
        cartService.updateQuantity(
                memberId,
                productId,
                quantity);

        return "商品數量已更新";
    }

    // 移除購物車商品
    @DeleteMapping("/{productId}")
    public String removeCartItem(
            @PathVariable String productId,
            HttpSession session) {

        Long memberId = (Long) session.getAttribute("memberId");
        String role = (String) session.getAttribute("role");

        // 尚未登入
        if (memberId == null) {

            return "請先登入";
        }

        // 管理員不能使用購物車
        if ("RULER".equals(role)) {

            return "管理員不能使用購物車";
        }

        // 移除商品
        cartService.deleteCartItem(
                memberId,
                productId);

        return "商品已移除";
    }
}