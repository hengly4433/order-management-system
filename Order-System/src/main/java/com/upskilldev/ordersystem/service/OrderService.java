package com.upskilldev.ordersystem.service;

import com.upskilldev.ordersystem.dto.order.CreateOrderDTO;
import com.upskilldev.ordersystem.dto.order.OrderDTO;
import com.upskilldev.ordersystem.dto.order.UpdateOrderStatusDTO;

import java.util.List;

public interface OrderService {
    OrderDTO createOrder(CreateOrderDTO createOrderDTO);
    OrderDTO updateStatus(Long orderId, UpdateOrderStatusDTO orderStatus);
    void deleteOrder(Long orderId);
    OrderDTO getOrderById(Long orderId);
    List<OrderDTO> getAllOrders();
}
