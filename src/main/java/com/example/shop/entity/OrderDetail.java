package com.example.shop.entity;

public class OrderDetail {

    private int orderItemSN;
    private String orderId;
    private String productId;
    private int quantity;
    private int standPrice;
    private int itemPrice;

    public OrderDetail() {
    }

    public int getOrderItemSN() {
        return orderItemSN;
    }

    public void setOrderItemSN(int orderItemSN) {
        this.orderItemSN = orderItemSN;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getStandPrice() {
        return standPrice;
    }

    public void setStandPrice(int standPrice) {
        this.standPrice = standPrice;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }
}