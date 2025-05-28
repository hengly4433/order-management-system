package com.upskilldev.ordersystem.dto.stock;

import jakarta.validation.constraints.NotNull;

public class StockDTO {

    @NotNull(message = "Quantity is required")
    private Integer quantity;

    private Long productId;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
