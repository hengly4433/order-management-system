package com.upskilldev.ordersystem.controller;

import com.upskilldev.ordersystem.dto.order.CreateOrderDTO;
import com.upskilldev.ordersystem.dto.order.OrderDTO;
import com.upskilldev.ordersystem.dto.order.UpdateOrderStatusDTO;
import com.upskilldev.ordersystem.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order Management", description = "Admin operations for managing orders")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Create Order")
    @PreAuthorize("hasAuthority('CREATE_ORDER')")
    @PostMapping
    public ResponseEntity<OrderDTO> create(@Valid @RequestBody CreateOrderDTO orderDTO) {
        log.info("Creating new order for customer ID: {}", orderDTO.getCustomerId());
        OrderDTO created = orderService.createOrder(orderDTO);
        log.info("Created order with ID: {}", created.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Get Order By ID")
    @PreAuthorize("hasAuthority('VIEW_ORDER')")
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getById(@PathVariable Long orderId) {
        log.debug("Retrieving order by ID: {}", orderId);
        OrderDTO result = orderService.getOrderById(orderId);
        log.info("Retrieved order with ID: {}", result.getId());
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "List All Orders")
    @PreAuthorize("hasAuthority('VIEW_ORDER')")
    @GetMapping
    public ResponseEntity<List<OrderDTO>> list() {
        log.debug("Listing all orders");
        List<OrderDTO> result = orderService.getAllOrders();
        log.info("Total orders retrieved: {}", result.size());
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Update Order Status")
    @PreAuthorize("hasAuthority('EDIT_ORDER')")
    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderDTO> updateStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateOrderStatusDTO updateOrderStatusDTO
    ) {
        log.info("Updating status for order ID: {} to {}", orderId, updateOrderStatusDTO.getStatus());
        OrderDTO result = orderService.updateStatus(orderId, updateOrderStatusDTO);
        log.info("Updated status for order ID: {} to {}", result.getId(), result.getStatus());
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Delete Order")
    @PreAuthorize("hasAuthority('DELETE_ORDER')")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> delete(@PathVariable Long orderId) {
        log.warn("Deleting order ID: {}", orderId);
        orderService.deleteOrder(orderId);
        log.info("Deleted order with ID: {}", orderId);
        return ResponseEntity.noContent().build();
    }
}
