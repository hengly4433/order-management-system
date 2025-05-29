package com.upskilldev.ordersystem.mapper;

import com.upskilldev.ordersystem.dto.order.OrderDTO;
import com.upskilldev.ordersystem.dto.order.OrderItemDetailDTO;
import com.upskilldev.ordersystem.entity.Order;

import java.util.stream.Collectors;

public class OrderMapper {
    public static OrderDTO toDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getOrderStatus().name());
        dto.setTotal(order.getTotal());

        // Customer info
        dto.setCustomerId(order.getCustomer().getId());
        dto.setCustomerName(order.getCustomer().getName());

        dto.setItems(order.getOrderItems().stream().map(item -> {
            OrderItemDetailDTO it = new OrderItemDetailDTO();
            it.setId(item.getId());
            it.setProductId(item.getProduct().getId());
            it.setProductName(item.getProduct().getName());
            it.setQuantity(item.getQuantity());
            it.setPrice(item.getPrice());
            return it;
        }).collect(Collectors.toList()));
        return dto;
    }
}
