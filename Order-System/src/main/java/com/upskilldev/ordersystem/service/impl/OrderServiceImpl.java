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
    private final CustomerRepository customerRepository;

    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository, CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public OrderDTO createOrder(CreateOrderDTO createOrderDTO) {
        Order order = new Order();

        // **load & set customer**
        Customer customer = customerRepository.findById(createOrderDTO.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found: " + createOrderDTO.getCustomerId()));
        order.setCustomer(customer);

        // 1) Reserve stock at creation (PENDING)
        Set<OrderItem> items = createOrderDTO.getItems().stream().map(itemDto -> {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found" + itemDto.getProductId()));

            int qty = itemDto.getQuantity();
            if (product.getStock() < qty) {
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

        // 2) Compute total
        BigDecimal total = items.stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotal(total);

        // 3) Save with status = PENDING (default)
        return OrderMapper.toDTO(orderRepository.save(order));
    }

    @Override
    public OrderDTO updateStatus(Long orderId, UpdateOrderStatusDTO orderStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found" + orderId));

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
