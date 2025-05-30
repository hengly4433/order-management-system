package com.upskilldev.ordersystem.controller;

import com.upskilldev.ordersystem.dto.customer.CreateCustomerDTO;
import com.upskilldev.ordersystem.dto.customer.CustomerDTO;
import com.upskilldev.ordersystem.dto.customer.UpdateCustomerDTO;
import com.upskilldev.ordersystem.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customer Management")
public class CustomerController {

    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "Create Customer")
    @PreAuthorize("hasAuthority('CREATE_CUSTOMER')")
    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CreateCustomerDTO createCustomerDTO) {
        log.info("Creating customer: {}", createCustomerDTO.getName());
        CustomerDTO result = customerService.createCustomer(createCustomerDTO);
        log.info("Created customer with ID: {}", result.getId());
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Get Customer By ID")
    @PreAuthorize("hasAuthority('VIEW_CUSTOMER')")
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long customerId) {
        log.debug("Retrieving customer by ID: {}", customerId);
        CustomerDTO result = customerService.getCustomerById(customerId);
        log.info("Retrieved customer: {}", result.getName());
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Get All Customers")
    @PreAuthorize("hasAuthority('VIEW_CUSTOMER')")
    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        log.debug("Listing all customers");
        List<CustomerDTO> result = customerService.getAllCustomers();
        log.info("Total customers retrieved: {}", result.size());
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Update Customer")
    @PreAuthorize("hasAuthority('EDIT_CUSTOMER')")
    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long customerId, @Valid @RequestBody UpdateCustomerDTO updateCustomerDTO) {
        log.info("Updating customer ID: {}", customerId);
        CustomerDTO result = customerService.updateCustomer(customerId, updateCustomerDTO);
        log.info("Updated customer ID: {}", result.getId());
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Delete Customer")
    @PreAuthorize("hasAuthority('DELETE_CUSTOMER')")
    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long customerId) {
        log.warn("Deleting customer ID: {}", customerId);
        customerService.deleteCustomer(customerId);
        log.info("Deleted customer with ID: {}", customerId);
        return ResponseEntity.noContent().build();
    }
}
