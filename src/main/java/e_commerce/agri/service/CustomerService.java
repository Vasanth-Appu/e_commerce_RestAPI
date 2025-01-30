package e_commerce.agri.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import e_commerce.agri.modal.Customer;
import e_commerce.agri.modal.Farmer;
import e_commerce.agri.modal.Products;
import e_commerce.agri.repository.CustomerRepo;
import e_commerce.agri.repository.ProductsRepo;

@Service
public class CustomerService {
	
	@Autowired CustomerRepo customerRepo;

			public Customer signup(Customer customer) throws Exception {
					
				 // Check if mobile number already exists
		        if (customerRepo.findByCustContact(customer.getCustContact()).isPresent()) {
		            throw new Exception("Mobile number already exists");
		        }

		        // Check if email already exists
		        if (customerRepo.findByEmailId(customer.getemailId()).isPresent()) {
		            throw new Exception("Email ID already exists");
		        }
					return customerRepo.save(customer);
			}
			
			public boolean authenticate(String customerEmail, String password) {
			    // Fetch farmer by email
			    Optional<Customer> customer = customerRepo.findByEmailId(customerEmail);

			    if (customer.isPresent()) {
			        Customer existingCustomer = customer.get();

			        // Log farmer details for debugging
			       // System.out.println("customer Email: " + existingCustomer.getCusEmail());
			        System.out.println("customer Name: " + existingCustomer.getCusName());

			        // Check if the password matches
			        if (existingCustomer.getPassword() != null && existingCustomer.getPassword().equals(password)) {
			        	
			            return true ; // Email and password match
			        }
			    }

			    return false; // Either farmer doesn't exist or password doesn't match
			}
	
}
	
