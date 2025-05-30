package com.upskilldev.ordersystem.service.impl;

import com.upskilldev.ordersystem.dto.order.CreateOrderDTO;
import com.upskilldev.ordersystem.dto.order.OrderDTO;
import com.upskilldev.ordersystem.dto.order.UpdateOrderStatusDTO;
import com.upskilldev.ordersystem.entity.*;
import com.upskilldev.ordersystem.exception.ResourceNotFoundException;
import com.upskilldev.ordersystem.mapper.OrderMapper;
import com.upskilldev.ordersystem.repository.CustomerRepository;
import com.upskilldev.ordersystem.repository.OrderRepository;
import com.upskilldev.ordersystem.repository.ProductRepository;
import com.upskilldev.ordersystem.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository, CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public OrderDTO createOrder(CreateOrderDTO createOrderDTO) {
        log.debug("Service: Creating order for customer ID: {}", createOrderDTO.getCustomerId());
        Order order = new Order();

        // Load & set customer
        Customer customer = customerRepository.findById(createOrderDTO.getCustomerId())
                .orElseThrow(() -> {
                    log.error("Service: Customer not found for order, ID: {}", createOrderDTO.getCustomerId());
                    return new ResourceNotFoundException("Customer not found: " + createOrderDTO.getCustomerId());
                });
        order.setCustomer(customer);

        // Reserve stock for each product item
        Set<OrderItem> items = createOrderDTO.getItems().stream().map(itemDto -> {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> {
                        log.error("Service: Product not found for order item, ID: {}", itemDto.getProductId());
                        return new ResourceNotFoundException("Product not found: " + itemDto.getProductId());
                    });

            int qty = itemDto.getQuantity();
            if (product.getStock() < qty) {
                log.error("Service: Not enough stock for product {}, requested: {}, available: {}", product.getName(), qty, product.getStock());
                throw new ResourceNotFoundException(
                        "Cannot place order: only " + product.getStock() +
                                " unit of " + product.getName() + " available, but you want " + qty
                );
            }

            // decrement available stock
            product.setStock(product.getStock() - qty);
            productRepository.save(product);

            // process the OrderItem
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(qty);
            BigDecimal linePrice = product.getPrice()
                    .multiply(new BigDecimal(itemDto.getQuantity()));
            item.setPrice(linePrice);

            return item;
        }).collect(Collectors.toSet());

        order.setOrderItems(items);
        order.setOrderStatus(OrderStatus.PENDING);

        // Compute total
        BigDecimal total = items.stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotal(total);

        OrderDTO dto = OrderMapper.toDTO(orderRepository.save(order));
        log.info("Service: Order created with ID: {}", dto.getId());
        return dto;
    }

    @Override
    public OrderDTO updateStatus(Long orderId, UpdateOrderStatusDTO orderStatus) {
        log.debug("Service: Updating status for order ID: {} to {}", orderId, orderStatus.getStatus());
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.error("Service: Order not found for update, ID: {}", orderId);
                    return new ResourceNotFoundException("Order not found: " + orderId);
                });

        OrderStatus oldStatus = order.getOrderStatus();
        OrderStatus newStatus = OrderStatus.valueOf(orderStatus.getStatus());

        // Validate allowed transitions
        boolean valid =
                (oldStatus == OrderStatus.PENDING &&
                        (newStatus == OrderStatus.PROCESSING ||
                                newStatus == OrderStatus.CANCELLED)) ||
                        (oldStatus == OrderStatus.PROCESSING &&
                                (newStatus == OrderStatus.COMPLETED ||
                                        newStatus == OrderStatus.CANCELLED)) ||
                        (oldStatus == OrderStatus.CANCELLED && newStatus == OrderStatus.CANCELLED) ||
                        (oldStatus == OrderStatus.COMPLETED && newStatus == OrderStatus.COMPLETED);
        if (!valid) {
            log.error("Service: Invalid order status transition from {} to {} for order ID: {}", oldStatus, newStatus, orderId);
            throw new ResourceNotFoundException(
                    "Cannot transition order from " + oldStatus + " to " + newStatus);
        }

        // On CANCELLED: roll back the reservation
        if ((oldStatus == OrderStatus.PENDING || oldStatus == OrderStatus.PROCESSING)
                && newStatus == OrderStatus.CANCELLED) {
            order.getOrderItems().forEach(item -> {
                Product p = item.getProduct();
                p.setStock(p.getStock() + item.getQuantity());
                productRepository.save(p);
            });
        }

        // On COMPLETED: nothing to do (stock already reserved)
        order.setOrderStatus(newStatus);
        OrderDTO dto = OrderMapper.toDTO(orderRepository.save(order));
        log.info("Service: Updated status for order ID: {} to {}", dto.getId(), dto.getStatus());
        return dto;
    }

    @Override
    public void deleteOrder(Long orderId) {
        log.warn("Service: Deleting order ID: {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.error("Service: Order not found for delete, ID: {}", orderId);
                    return new ResourceNotFoundException("Order not found: " + orderId);
                });
        orderRepository.delete(order);
        log.info("Service: Deleted order ID: {}", orderId);
    }

    @Override
    public OrderDTO getOrderById(Long orderId) {
        log.debug("Service: Retrieving order by ID: {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.error("Service: Order not found, ID: {}", orderId);
                    return new ResourceNotFoundException("Order not found: " + orderId);
                });
        OrderDTO dto = OrderMapper.toDTO(order);
        log.info("Service: Retrieved order ID: {}", orderId);
        return dto;
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        log.debug("Service: Retrieving all orders");
        List<OrderDTO> orders = orderRepository.findAll()
                .stream()
                .map(OrderMapper::toDTO)
                .collect(Collectors.toList());
        log.info("Service: Total orders retrieved: {}", orders.size());
        return orders;
    }
}
