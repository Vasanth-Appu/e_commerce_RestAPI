package e_commerce.agri.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import e_commerce.agri.exceptionHandler.AlreadyExistsException;
import e_commerce.agri.modal.Customer;
import e_commerce.agri.modal.Farmer;
import e_commerce.agri.modal.Products;
import e_commerce.agri.repository.CustomerRepo;
import e_commerce.agri.repository.ProductsRepo;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepo customerRepo;
	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public Customer signup(Customer customer) {

		// Check if mobile number already exists
		if (customerRepo.findByCustContact(customer.getCustContact()).isPresent()) {
			throw new AlreadyExistsException("Mobile number already exists");
		}

		// Check if email already exists

		if (customerRepo.findByEmailId(customer.getemailId()).isPresent()) {
			throw new AlreadyExistsException("Email ID already exists");
		}
		String encodePassword = passwordEncoder.encode(customer.getPassword());
		customer.setPassword(encodePassword);
		return customerRepo.save(customer);
	}

	public boolean authenticate(String customerEmail, String password) {
		// Fetch farmer by email
		Optional<Customer> customer = customerRepo.findByEmailId(customerEmail);

		if (customer.isPresent()) {
			Customer existingCustomer = customer.get();
			if (existingCustomer.getPassword() != null
					&& passwordEncoder.matches(password, existingCustomer.getPassword())) {

				return true; // Email and password match
			}
		}

		return false; // Either farmer doesn't exist or password doesn't match
	}

}
