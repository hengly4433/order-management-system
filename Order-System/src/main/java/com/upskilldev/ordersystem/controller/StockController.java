package com.upskilldev.ordersystem.controller;

import com.upskilldev.ordersystem.dto.stock.StockDTO;
import com.upskilldev.ordersystem.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
@Tag(name = "Stock Management", description = "Operations for managing product stock")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @Operation(summary = "Get Stock by Product")
    @PreAuthorize("hasAuthority('VIEW_STOCK')")
    @GetMapping("/{productId}")
    public ResponseEntity<StockDTO> get(@PathVariable Long productId) {
        return ResponseEntity.ok(stockService.getStockByProductId(productId));
    }

    @Operation(summary = "Set Stock for Product")
    @PreAuthorize("hasAuthority('EDIT_STOCK')")
    @PutMapping("/{productId}")
    public ResponseEntity<StockDTO> set(@PathVariable Long productId, @Valid @RequestBody StockDTO stockDTO) {
        return ResponseEntity.ok(stockService.setStock(productId, stockDTO));
    }


    @Operation(summary = "Adjust Stock by Delta")
    @PreAuthorize("hasAuthority('EDIT_STOCK')")
    @PostMapping("/{productId}/adjust")
    public ResponseEntity<StockDTO> adjust(@PathVariable Long productId, @RequestBody @NotBlank(message = "Delta is required") Integer delta) {
        return ResponseEntity.ok(stockService.adjustStock(productId, delta));
    }

    @Operation(summary = "List All Stock Levels")
    @PreAuthorize("hasAuthority('VIEW_STOCK')")
    @GetMapping
    public ResponseEntity<List<StockDTO>> list() {
        return ResponseEntity.ok(stockService.listAllStocks());
    }
}
