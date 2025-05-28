package com.upskilldev.ordersystem.dto.order;

import jakarta.validation.constraints.NotNull;

public class UpdateOrderStatusDTO {

    @NotNull(message = "Status is required")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
