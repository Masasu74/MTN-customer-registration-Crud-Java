package com.bmt.webapp.repositories;

import com.bmt.webapp.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
     public Customer findByEmail(String email);
}
