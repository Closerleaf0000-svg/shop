package com.example.shop.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class OrderRequest {

    @NotBlank(message = "訂單編號不可空白")
    private String orderId;

    @Min(value = 0, message = "付款狀態只能是 0 或 1")
    @Max(value = 1, message = "付款狀態只能是 0 或 1")
    private int payStatus;

    @NotEmpty(message = "訂單明細不可為空")
    @Valid
    private List<OrderDetailRequest> orderDetails;

    public OrderRequest() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public List<OrderDetailRequest> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(
            List<OrderDetailRequest> orderDetails) {
        this.orderDetails = orderDetails;
    }
}