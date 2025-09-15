package com.bmt.webapp.controllers;

import com.bmt.webapp.models.Customer;
import com.bmt.webapp.models.CustomerDto;
import com.bmt.webapp.models.ErrorResponse;
import com.bmt.webapp.models.SuccessResponse;
import jakarta.validation.Valid;
import com.bmt.webapp.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "http://localhost:3000")
public class CustomersController {
    @Autowired
    private CustomerRepository customerRepo;

    @GetMapping({"","/"})
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
        return ResponseEntity.ok(customers);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable int id) {
        Optional<Customer> customer = customerRepo.findById(id);
        if (customer.isPresent()) {
            return ResponseEntity.ok(customer.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createCustomer(@Valid @RequestBody CustomerDto customerDto) {
        try {
            // Check if email already exists
            if (customerRepo.findByEmail(customerDto.getEmail()) != null) {
                return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Email already exists", "email"));
            }

            // Check age requirement (16 and above)
            if (!customerDto.isValidAge()) {
                return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Customer must be 16 years or older", "dateOfBirth "));
            }

            Customer customer = new Customer();
            customer.setFirstName(customerDto.getFirstName());
            customer.setLastName(customerDto.getLastName());
            customer.setEmail(customerDto.getEmail());
            customer.setPhone(customerDto.getPhone());
            customer.setDateOfBirth(customerDto.getDateOfBirth());
            customer.setAddress(customerDto.getAddress());
            customer.setCity(customerDto.getCity());
            customer.setState(customerDto.getState());
            customer.setZipCode(customerDto.getZipCode());
            customer.setPlanType(customerDto.getPlanType());
            customer.setIdType(customerDto.getIdType());
            customer.setIdNumber(customerDto.getIdNumber());
            customer.setStatus(customerDto.getStatus());
            customer.setJoinDate(new Date());

            Customer savedCustomer = customerRepo.save(customer);
            
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(savedCustomer.getId())
                    .toUri();
            
            SuccessResponse successResponse = new SuccessResponse(
                "Customer created successfully!", 
                savedCustomer
            );
            
            return ResponseEntity.created(location).body(successResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse("Failed to create customer: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(
            @PathVariable int id,
            @Valid @RequestBody CustomerDto customerDto
    ) {
        try {
            Optional<Customer> existingCustomerOpt = customerRepo.findById(id);
            if (!existingCustomerOpt.isPresent()) {
                return ResponseEntity.status(404)
                    .body(new ErrorResponse("Customer not found"));
            }

            Customer existingCustomer = existingCustomerOpt.get();

            // Check if email is being changed and if the new email already exists
            if (!existingCustomer.getEmail().equals(customerDto.getEmail()) && 
                customerRepo.findByEmail(customerDto.getEmail()) != null) {
                return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Email already exists", "email"));
            }

            // Check age requirement (16 and above)
            if (!customerDto.isValidAge()) {
                return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Customer must be 16 years or older", "dateOfBirth"));
            }

            // Update the existing customer
            existingCustomer.setFirstName(customerDto.getFirstName());
            existingCustomer.setLastName(customerDto.getLastName());
            existingCustomer.setEmail(customerDto.getEmail());
            existingCustomer.setPhone(customerDto.getPhone());
            existingCustomer.setDateOfBirth(customerDto.getDateOfBirth());
            existingCustomer.setAddress(customerDto.getAddress());
            existingCustomer.setCity(customerDto.getCity());
            existingCustomer.setState(customerDto.getState());
            existingCustomer.setZipCode(customerDto.getZipCode());
            existingCustomer.setPlanType(customerDto.getPlanType());
            existingCustomer.setIdType(customerDto.getIdType());
            existingCustomer.setIdNumber(customerDto.getIdNumber());
            existingCustomer.setStatus(customerDto.getStatus());

            Customer updatedCustomer = customerRepo.save(existingCustomer);
            SuccessResponse successResponse = new SuccessResponse(
                "Customer updated successfully!", 
                updatedCustomer
            );
            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse("Failed to update customer: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable int id) {
        Optional<Customer> customer = customerRepo.findById(id);
        if (customer.isPresent()) {
            customerRepo.delete(customer.get());
            SuccessResponse successResponse = new SuccessResponse(
                "Customer deleted successfully!"
            );
            return ResponseEntity.ok(successResponse);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("Customer not found with ID: " + id));
        }
    }
}
