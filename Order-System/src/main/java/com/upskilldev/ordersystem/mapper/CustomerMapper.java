package com.upskilldev.ordersystem.mapper;

import com.upskilldev.ordersystem.dto.customer.CreateCustomerDTO;
import com.upskilldev.ordersystem.dto.customer.CustomerDTO;
import com.upskilldev.ordersystem.dto.customer.UpdateCustomerDTO;
import com.upskilldev.ordersystem.entity.Customer;

public class CustomerMapper {
    public static CustomerDTO toDTO(Customer c) {
        CustomerDTO d = new CustomerDTO();
        d.setId(c.getId());
        d.setName(c.getName());
        d.setEmail(c.getEmail());
        d.setPhone(c.getPhone());
        d.setCreatedAt(c.getCreatedAt());
        d.setUpdatedAt(c.getUpdatedAt());
        return d;
    }
    public static Customer toEntity(CreateCustomerDTO d) {
        Customer c = new Customer();
        c.setName(d.getName());
        c.setEmail(d.getEmail());
        c.setPhone(d.getPhone());
        return c;
    }
    public static void updateEntity(UpdateCustomerDTO d, Customer c) {
        c.setName(d.getName());
        c.setEmail(d.getEmail());
        c.setPhone(d.getPhone());
    }
}
