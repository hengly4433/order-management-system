package com.upskilldev.ordersystem.controller;

import com.upskilldev.ordersystem.dto.customer.CreateCustomerDTO;
import com.upskilldev.ordersystem.dto.customer.CustomerDTO;
import com.upskilldev.ordersystem.dto.customer.UpdateCustomerDTO;
import com.upskilldev.ordersystem.entity.Customer;
import com.upskilldev.ordersystem.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customer Management")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "Create Customer")
    @PreAuthorize("hasAuthority('CREATE_CUSTOMER')")
    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CreateCustomerDTO createCustomerDTO) {
        return ResponseEntity.ok(customerService.createCustomer(createCustomerDTO));
    }

    @Operation(summary = "Get Customer By ID")
    @PreAuthorize("hasAuthority('VIEW_CUSTOMER')")
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long customerId) {
        return ResponseEntity.ok(customerService.getCustomerById(customerId));
    }

    @Operation(summary = "Get All Customers")
    @PreAuthorize("hasAuthority('VIEW_CUSTOMER')")
    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @Operation(summary = "Update Customer")
    @PreAuthorize("hasAuthority('EDIT_CUSTOMER')")
    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long customerId, @Valid @RequestBody UpdateCustomerDTO updateCustomerDTO) {
        return ResponseEntity.ok(customerService.updateCustomer(customerId, updateCustomerDTO));
    }

    @Operation(summary = "Delete Customer")
    @PreAuthorize("hasAuthority('DELETE_CUSTOMER')")
    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }
}
