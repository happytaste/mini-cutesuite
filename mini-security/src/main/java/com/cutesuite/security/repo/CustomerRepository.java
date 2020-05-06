package com.cutesuite.security.repo;

import com.cutesuite.security.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
    List<Customer> findByLastName(String lastName);

    Customer findById(long id);
}
