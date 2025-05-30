package com.upskilldev.ordersystem.controller;

import com.upskilldev.ordersystem.dto.stock.StockDTO;
import com.upskilldev.ordersystem.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
@Tag(name = "Stock Management", description = "Operations for managing product stock")
public class StockController {

    private static final Logger log = LoggerFactory.getLogger(StockController.class);

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @Operation(summary = "Get Stock by Product")
    @PreAuthorize("hasAuthority('VIEW_STOCK')")
    @GetMapping("/{productId}")
    public ResponseEntity<StockDTO> get(@PathVariable Long productId) {
        log.debug("Retrieving stock for product ID: {}", productId);
        StockDTO result = stockService.getStockByProductId(productId);
        log.info("Retrieved stock for product ID: {} (quantity: {})", productId, result.getQuantity());
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Set Stock for Product")
    @PreAuthorize("hasAuthority('EDIT_STOCK')")
    @PutMapping("/{productId}")
    public ResponseEntity<StockDTO> set(@PathVariable Long productId, @Valid @RequestBody StockDTO stockDTO) {
        log.info("Setting stock for product ID: {} to {}", productId, stockDTO.getQuantity());
        StockDTO result = stockService.setStock(productId, stockDTO);
        log.info("Set stock for product ID: {} to {}", productId, result.getQuantity());
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Adjust Stock by Delta")
    @PreAuthorize("hasAuthority('EDIT_STOCK')")
    @PostMapping("/{productId}/adjust")
    public ResponseEntity<StockDTO> adjust(@PathVariable Long productId, @RequestBody @NotNull(message = "Delta is required") Integer delta) {
        log.info("Adjusting stock for product ID: {} by delta: {}", productId, delta);
        StockDTO result = stockService.adjustStock(productId, delta);
        log.info("Adjusted stock for product ID: {} to {}", productId, result.getQuantity());
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "List All Stock Levels")
    @PreAuthorize("hasAuthority('VIEW_STOCK')")
    @GetMapping
    public ResponseEntity<List<StockDTO>> list() {
        log.debug("Listing all stock levels");
        List<StockDTO> result = stockService.listAllStocks();
        log.info("Total stock records retrieved: {}", result.size());
        return ResponseEntity.ok(result);
    }
}
