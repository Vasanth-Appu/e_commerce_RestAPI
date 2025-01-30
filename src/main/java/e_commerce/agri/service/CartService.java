package e_commerce.agri.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import e_commerce.agri.modal.Cart;
import e_commerce.agri.modal.Customer;
import e_commerce.agri.modal.Products;
import e_commerce.agri.repository.CartRepo;
import e_commerce.agri.repository.CustomerRepo;
import e_commerce.agri.repository.ProductsRepo;

@Service
public class CartService {
	@Autowired
	private CartRepo cartRepo;

	@Autowired
	private CustomerRepo customerRepo;

	@Autowired
	private ProductsRepo productsRepo;


	public Cart saveCart1(String emailId, long productId) throws Exception {

	    // Find the customer by email
	    Optional<Customer> customerOptional = customerRepo.findByEmailId(emailId);
	    if (!customerOptional.isPresent()) {
	        throw new Exception("Customer not found with email: " + emailId);
	    }
	    Customer customer = customerOptional.get(); // Extract the Customer object from Optional

	    // Find the product by ID
	    Optional<Products> productOptional = productsRepo.findById(productId);
	    if (!productOptional.isPresent()) {
	        throw new Exception("Product not found with ID: " + productId);
	    }
	    Products product = productOptional.get(); // Extract the Products object from Optional

	    // Create a new cart
	    Cart newCart = new Cart();
	    newCart.setCustomer(customer); // Set the Customer object
	    newCart.setProduct(product);   // Set the Products object

	    // Save the cart
	    return cartRepo.save(newCart);
	}


	 public List<Products> getCartProducts(String emailId) {
	        return cartRepo.findProductsByCustomerEmail(emailId);
	    }
	}

