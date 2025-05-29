package com.upskilldev.ordersystem.controller;

import com.upskilldev.ordersystem.dto.order.CreateOrderDTO;
import com.upskilldev.ordersystem.dto.order.OrderDTO;
import com.upskilldev.ordersystem.dto.order.UpdateOrderStatusDTO;
import com.upskilldev.ordersystem.entity.Order;
import com.upskilldev.ordersystem.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order Management", description = "Admin operations for managing orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Create Order")
    @PreAuthorize("hasAuthority('CREATE_ORDER')")
    @PostMapping
    public ResponseEntity<OrderDTO> create(@Valid @RequestBody CreateOrderDTO orderDTO) {
        OrderDTO created = orderService.createOrder(orderDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Create Order")
    @PreAuthorize("hasAuthority('VIEW_ORDER')")
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getById(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @Operation(summary = "List All Orders")
    @PreAuthorize("hasAuthority('VIEW_ORDER')")
    @GetMapping
    public ResponseEntity<List<OrderDTO>> list() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @Operation(summary = "Update Order Status")
    @PreAuthorize("hasAuthority('EDIT_ORDER')")
    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderDTO> updateStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateOrderStatusDTO updateOrderStatusDTO
    ) {
        return ResponseEntity.ok(orderService.updateStatus(orderId, updateOrderStatusDTO));
    }

    @Operation(summary = "Delete Order")
    @PreAuthorize("hasAuthority('DELETE_ORDER')")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> delete(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}
