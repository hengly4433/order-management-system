package com.upskilldev.ordersystem.service.impl;

import com.upskilldev.ordersystem.dto.stock.StockDTO;
import com.upskilldev.ordersystem.entity.Product;
import com.upskilldev.ordersystem.exception.ResourceNotFoundException;
import com.upskilldev.ordersystem.mapper.StockMapper;
import com.upskilldev.ordersystem.repository.ProductRepository;
import com.upskilldev.ordersystem.service.StockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StockServiceImpl implements StockService {

    private final ProductRepository productRepository;

    public StockServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public StockDTO getStockByProductId(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + productId + " not found"));
        return StockMapper.toDTO(product);
    }

    @Override
    public StockDTO setStock(Long productId, StockDTO stockDTO) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + productId + " not found"));
        product.setStock(stockDTO.getQuantity());
        return StockMapper.toDTO(productRepository.save(product));
    }

    @Override
    public StockDTO adjustStock(Long productId, Integer delta) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + productId + " not found"));
        product.setStock(product.getStock() + delta);
        return StockMapper.toDTO(productRepository.save(product));
    }

    @Override
    public List<StockDTO> listAllStocks() {
        return productRepository.findAll()
                .stream()
                .map(StockMapper::toDTO)
                .collect(Collectors.toList());
    }
}
