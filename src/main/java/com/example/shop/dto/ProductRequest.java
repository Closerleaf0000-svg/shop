package com.example.shop.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProductRequest {

    // 商品編號
    @NotBlank(message = "商品編號不可空白")
    private String productId;

    // 商品名稱
    @NotBlank(message = "商品名稱不可空白")
    private String productName;

    // 商品價格
    @NotNull(message = "商品價格不可空白")
    @Min(value = 0, message = "商品價格不可小於 0")
    private Integer price;

    // 商品庫存
    @NotNull(message = "商品庫存不可空白")
    @Min(value = 0, message = "商品庫存不可小於 0")
    private Integer quantity;

    public ProductRequest() {
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}