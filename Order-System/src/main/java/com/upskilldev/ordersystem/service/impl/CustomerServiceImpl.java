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

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerDTO createCustomer(CreateCustomerDTO createCustomerDTO) {
        Customer customer = CustomerMapper.toEntity(createCustomerDTO);
        customer = customerRepository.save(customer);
        log.info("Created Customer : {}", customer.getId());
        return CustomerMapper.toDTO(customer);
    }

    @Override
    public CustomerDTO updateCustomer(Long customerId, UpdateCustomerDTO updateCustomerDTO) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with id " + customerId + " not found"));
        CustomerMapper.updateEntity(updateCustomerDTO, customer);
        customer = customerRepository.save(customer);
        log.info("Updated Customer : {}", customerId);
        return CustomerMapper.toDTO(customer);
    }

    @Override
    public void deleteCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with id " + customerId + " not found"));
        log.info("Deleted Customer : {}", customerId);
        customerRepository.delete(customer);
    }

    @Override
    public CustomerDTO getCustomerById(Long customerId) {
        return CustomerMapper.toDTO(customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with id " + customerId + " not found")));
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(CustomerMapper::toDTO)
                .collect(Collectors.toList());
    }
}
