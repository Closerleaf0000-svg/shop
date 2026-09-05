package com.example.shop.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class OrderDetailRequest {

    @NotBlank(message = "商品編號不可空白")
    private String productId;

    @NotNull(message = "商品數量不可空白")
    @Min(value = 1, message = "商品數量至少為 1")
    private Integer quantity;

    public OrderDetailRequest() {
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}