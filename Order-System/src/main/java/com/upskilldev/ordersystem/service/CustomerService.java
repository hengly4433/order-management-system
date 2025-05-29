package com.upskilldev.ordersystem.service;

import com.upskilldev.ordersystem.dto.customer.CreateCustomerDTO;
import com.upskilldev.ordersystem.dto.customer.CustomerDTO;
import com.upskilldev.ordersystem.dto.customer.UpdateCustomerDTO;

import java.util.List;

public interface CustomerService {

    CustomerDTO createCustomer(CreateCustomerDTO createCustomerDTO);
    CustomerDTO updateCustomer(Long customerId, UpdateCustomerDTO updateCustomerDTO);
    void deleteCustomer(Long customerId);
    CustomerDTO getCustomerById(Long customerId);
    List<CustomerDTO> getAllCustomers();
}

