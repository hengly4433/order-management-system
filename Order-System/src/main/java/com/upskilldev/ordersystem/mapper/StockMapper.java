package com.upskilldev.ordersystem.mapper;

import com.upskilldev.ordersystem.dto.stock.StockDTO;
import com.upskilldev.ordersystem.entity.Product;

public class StockMapper {

    public static StockDTO toDTO(Product product) {
        StockDTO stockDto = new StockDTO();
        stockDto.setProductId(product.getId());
        stockDto.setQuantity(product.getStock());
        return stockDto;
    }
}
