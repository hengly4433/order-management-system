package com.upskilldev.ordersystem.service.impl;

import com.upskilldev.ordersystem.dto.stock.StockDTO;
import com.upskilldev.ordersystem.entity.Product;
import com.upskilldev.ordersystem.exception.ResourceNotFoundException;
import com.upskilldev.ordersystem.mapper.StockMapper;
import com.upskilldev.ordersystem.repository.ProductRepository;
import com.upskilldev.ordersystem.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StockServiceImpl implements StockService {

    private static final Logger log = LoggerFactory.getLogger(StockServiceImpl.class);

    private final ProductRepository productRepository;

    public StockServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public StockDTO getStockByProductId(Long productId) {
        log.debug("Service: Getting stock for product ID: {}", productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.error("Service: Product not found for getStock, ID: {}", productId);
                    return new ResourceNotFoundException("Product with id " + productId + " not found");
                });
        StockDTO dto = StockMapper.toDTO(product);
        log.info("Service: Retrieved stock for product ID: {} (quantity: {})", productId, dto.getQuantity());
        return dto;
    }

    @Override
    public StockDTO setStock(Long productId, StockDTO stockDTO) {
        log.info("Service: Setting stock for product ID: {} to {}", productId, stockDTO.getQuantity());
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.error("Service: Product not found for setStock, ID: {}", productId);
                    return new ResourceNotFoundException("Product with id " + productId + " not found");
                });
        product.setStock(stockDTO.getQuantity());
        StockDTO dto = StockMapper.toDTO(productRepository.save(product));
        log.info("Service: Set stock for product ID: {} to {}", productId, dto.getQuantity());
        return dto;
    }

    @Override
    public StockDTO adjustStock(Long productId, Integer delta) {
        log.info("Service: Adjusting stock for product ID: {} by delta: {}", productId, delta);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.error("Service: Product not found for adjustStock, ID: {}", productId);
                    return new ResourceNotFoundException("Product with id " + productId + " not found");
                });
        product.setStock(product.getStock() + delta);
        StockDTO dto = StockMapper.toDTO(productRepository.save(product));
        log.info("Service: Adjusted stock for product ID: {} to {}", productId, dto.getQuantity());
        return dto;
    }

    @Override
    public List<StockDTO> listAllStocks() {
        log.debug("Service: Listing all stocks");
        List<StockDTO> stocks = productRepository.findAll()
                .stream()
                .map(StockMapper::toDTO)
                .collect(Collectors.toList());
        log.info("Service: Total stock records retrieved: {}", stocks.size());
        return stocks;
    }
}
