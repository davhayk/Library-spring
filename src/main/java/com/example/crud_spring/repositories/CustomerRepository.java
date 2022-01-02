package com.example.crud_spring.repositories;

import com.example.crud_spring.entities.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
}
