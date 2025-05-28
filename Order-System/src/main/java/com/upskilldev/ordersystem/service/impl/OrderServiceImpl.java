package com.upskilldev.ordersystem.service.impl;

import com.upskilldev.ordersystem.dto.order.CreateOrderDTO;
import com.upskilldev.ordersystem.dto.order.OrderDTO;
import com.upskilldev.ordersystem.dto.order.UpdateOrderStatusDTO;
import com.upskilldev.ordersystem.entity.Order;
import com.upskilldev.ordersystem.entity.OrderItem;
import com.upskilldev.ordersystem.entity.OrderStatus;
import com.upskilldev.ordersystem.entity.Product;
import com.upskilldev.ordersystem.exception.ResourceNotFoundException;
import com.upskilldev.ordersystem.mapper.OrderMapper;
import com.upskilldev.ordersystem.repository.OrderRepository;
import com.upskilldev.ordersystem.repository.ProductRepository;
import com.upskilldev.ordersystem.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Override
    public OrderDTO createOrder(CreateOrderDTO createOrderDTO) {
        Order order = new Order();

        Set<OrderItem> items = createOrderDTO.getItems().stream().map(itemDto -> {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found" + itemDto.getProductId()));
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemDto.getQuantity());
            item.setPrice(
                    product.getPrice()
                            .multiply(BigDecimal.valueOf(itemDto.getQuantity()))
            );
            return item;
        }).collect(Collectors.toSet());

        order.setOrderItems(items);
        BigDecimal total = items.stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotal(total);
        return OrderMapper.toDTO(orderRepository.save(order));
    }

    @Override
    public OrderDTO updateStatus(Long orderId, UpdateOrderStatusDTO orderStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found" + orderId));
        OrderStatus newStatus = OrderStatus.valueOf(orderStatus.getStatus());
        OrderStatus oldStatus = order.getOrderStatus();

        // Only reduce stock on the transition → COMPLETED
        if (oldStatus != OrderStatus.COMPLETED && newStatus == OrderStatus.COMPLETED) {
            order.getOrderItems().forEach(orderItem -> {
               Product product = orderItem.getProduct();
               int remaining = product.getStock() - orderItem.getQuantity();
               if (remaining < 0) {
                   throw new ResourceNotFoundException(
                           "Insufficient stock for product " + product.getId() +
                                   " (“" + product.getName() + "”): have " + product.getStock() +
                                   ", need " + orderItem.getQuantity()
                   );
               }
               product.setStock(remaining);
               productRepository.save(product);
            });
        }
        order.setOrderStatus(newStatus);
        return OrderMapper.toDTO(orderRepository.save(order));
    }

    @Override
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found" + orderId));
        orderRepository.delete(order);
    }

    @Override
    public OrderDTO getOrderById(Long orderId) {
        return OrderMapper.toDTO(
                orderRepository.findById(orderId)
                        .orElseThrow(() -> new ResourceNotFoundException("Order not found" + orderId))
        );
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(OrderMapper::toDTO)
                .collect(Collectors.toList());
    }
}
