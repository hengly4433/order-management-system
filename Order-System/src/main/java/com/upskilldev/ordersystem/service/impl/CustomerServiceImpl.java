package com.upskilldev.ordersystem.service.impl;

import com.upskilldev.ordersystem.dto.customer.CreateCustomerDTO;
import com.upskilldev.ordersystem.dto.customer.CustomerDTO;
import com.upskilldev.ordersystem.dto.customer.UpdateCustomerDTO;
import com.upskilldev.ordersystem.entity.Customer;
import com.upskilldev.ordersystem.exception.ResourceNotFoundException;
import com.upskilldev.ordersystem.mapper.CustomerMapper;
import com.upskilldev.ordersystem.repository.CustomerRepository;
import com.upskilldev.ordersystem.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerDTO createCustomer(CreateCustomerDTO createCustomerDTO) {
        log.debug("Service: Creating customer: {}", createCustomerDTO.getName());
        Customer customer = CustomerMapper.toEntity(createCustomerDTO);
        customer = customerRepository.save(customer);
        CustomerDTO dto = CustomerMapper.toDTO(customer);
        log.info("Service: Customer created with ID: {}", dto.getId());
        return dto;
    }

    @Override
    public CustomerDTO updateCustomer(Long customerId, UpdateCustomerDTO updateCustomerDTO) {
        log.debug("Service: Updating customer ID: {}", customerId);
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> {
                    log.error("Service: Customer not found for update, ID: {}", customerId);
                    return new ResourceNotFoundException("Customer with id " + customerId + " not found");
                });
        CustomerMapper.updateEntity(updateCustomerDTO, customer);
        customer = customerRepository.save(customer);
        CustomerDTO dto = CustomerMapper.toDTO(customer);
        log.info("Service: Updated customer ID: {}", dto.getId());
        return dto;
    }

    @Override
    public void deleteCustomer(Long customerId) {
        log.warn("Service: Deleting customer ID: {}", customerId);
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> {
                    log.error("Service: Customer not found for delete, ID: {}", customerId);
                    return new ResourceNotFoundException("Customer with id " + customerId + " not found");
                });
        customerRepository.delete(customer);
        log.info("Service: Deleted customer ID: {}", customerId);
    }

    @Override
    public CustomerDTO getCustomerById(Long customerId) {
        log.debug("Service: Retrieving customer by ID: {}", customerId);
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> {
                    log.error("Service: Customer not found, ID: {}", customerId);
                    return new ResourceNotFoundException("Customer with id " + customerId + " not found");
                });
        CustomerDTO dto = CustomerMapper.toDTO(customer);
        log.info("Service: Retrieved customer ID: {}", customerId);
        return dto;
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        log.debug("Service: Retrieving all customers");
        List<CustomerDTO> customers = customerRepository.findAll()
                .stream()
                .map(CustomerMapper::toDTO)
                .collect(Collectors.toList());
        log.info("Service: Total customers retrieved: {}", customers.size());
        return customers;
    }
}
