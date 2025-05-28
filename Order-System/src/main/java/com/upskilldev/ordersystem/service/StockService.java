package com.upskilldev.ordersystem.service;

import com.upskilldev.ordersystem.dto.stock.StockDTO;

import java.util.List;

public interface StockService {
    StockDTO getStockByProductId(Long productId);
    StockDTO setStock(Long productId, StockDTO stockDTO);
    StockDTO adjustStock(Long productId, Integer delta);
    List<StockDTO> listAllStocks();
}
