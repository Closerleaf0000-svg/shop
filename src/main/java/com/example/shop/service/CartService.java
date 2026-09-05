package com.example.shop.service;

import com.example.shop.entity.Cart;
import com.example.shop.mapper.CartMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    private final CartMapper cartMapper;

    public CartService(CartMapper cartMapper) {
        this.cartMapper = cartMapper;
    }

    // 加入購物車
    public void addToCart(Cart cart) {
        cartMapper.insert(cart);
    }

    // 查詢會員購物車
    public List<Cart> findByMemberId(Long memberId) {
        return cartMapper.findByMemberId(memberId);
    }

    // 查詢指定商品
    public Cart findByMemberIdAndProductId(
            Long memberId,
            String productId) {

        return cartMapper.findByMemberIdAndProductId(
                memberId,
                productId);
    }

    // 修改數量
    public void updateQuantity(
            Long memberId,
            String productId,
            Integer quantity) {

        cartMapper.updateQuantity(
                memberId,
                productId,
                quantity);
    }

    // 數量 +1
    public void increaseQuantity(
            Long memberId,
            String productId) {

        cartMapper.increaseQuantity(
                memberId,
                productId);
    }

    // 數量 -1
    public void decreaseQuantity(
            Long memberId,
            String productId) {

        cartMapper.decreaseQuantity(
                memberId,
                productId);
    }

    // 移除商品
    public void deleteCartItem(
            Long memberId,
            String productId) {

        cartMapper.deleteCartItem(
                memberId,
                productId);
    }

}