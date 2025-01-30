package e_commerce.agri.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import e_commerce.agri.modal.Category;
import e_commerce.agri.modal.Customer;
import e_commerce.agri.modal.Farmer;
import e_commerce.agri.modal.Products;
import e_commerce.agri.repository.CategoryRepo;
import e_commerce.agri.repository.ProductsRepo;
import e_commerce.agri.service.CustomerService;
import e_commerce.agri.service.OrderService;

@RestController
@RequestMapping("/customer")
public class CustomerController {
	@Autowired CustomerService customerService;
	@Autowired ProductsRepo productsRepo;
	@Autowired OrderService orderService;
	@Autowired CategoryRepo categoryRepo;
	
	@PostMapping("/signup")
	public ResponseEntity<?> customerSignup(@RequestBody Customer customer, BindingResult result) throws Exception {
	    System.out.println("Customer details: " + customer);

	    // Check for validation errors
	    if (result.hasErrors()) {
	        result.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
	        return ResponseEntity.badRequest().body(
	            result.getAllErrors().stream()
	                  .map(ObjectError::getDefaultMessage)
	                  .toList()
	        );
	    }

	    try {
	        // Attempt to sign up the customer
	        Customer createdCustomer = customerService.signup(customer);
	        Map<String, Object> created = new HashMap<>();
	        created.put("Status", "Successfully created");
	        created.put("Data", createdCustomer);

	        return ResponseEntity.status(HttpStatus.CREATED).body(created);
	    }  catch (Exception e) {
	        // Return error message
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("Status", "error", "Message", e.getMessage()));
	    }

}
	
	   @PostMapping("/login")
	    public ResponseEntity<?> customerLogin(@RequestParam(name="email") String email, @RequestParam(name="password") String password) {
	        try {
	            boolean isAuthenticated = customerService.authenticate(email, password);
	            if (isAuthenticated) {
	            	List<Products> products = productsRepo.findByIsAvailableTrue();;
		        	Map<String,Object> viewAllproduct = new HashMap<>();
		        	viewAllproduct.put("Message", "LoginSuccessfull");
		        	viewAllproduct.put("Products",products);
	                return ResponseEntity.ok(viewAllproduct);
	            } else {
	                return ResponseEntity.status(401).body("Invalid email or password");
	            }
	        } catch (Exception e) {
	            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
	        }
	    }
	   
		 @PostMapping("/order")
		  public ResponseEntity<?> orderItem(@RequestParam(name="productId")long id,@RequestParam(name="farmerEmail")String toEmail,@RequestParam(name="customerEmail")String sendEmail,@RequestParam(name="quantity")long quantity){
			  try {
				  Map<String,Object> response = orderService.orderedItem(id,toEmail,quantity,sendEmail);
				  return ResponseEntity.ok(response);
			    } catch (Exception e) {
			        // Return error response
			        return ResponseEntity.status(400).body(e.getMessage());
			    }
	 
		 }
		 
		 @GetMapping("/search")//search by product name
		 public ResponseEntity<?> searchProducts(@RequestParam(name = "parameter") String value) {
		     try {
		         System.out.println("Searching for: " + value); // Log the search parameter

		         // Use the updated query for exact match
		         List<Products> products = productsRepo.findByProductName( value.toLowerCase());
		         //Optional<Category> product = categoryRepo.findByCategoryName( value.toLowerCase());

                 
		         if (products.isEmpty()) {
		             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No products found");
		         }

		         // Return the search results
		         Map<String, Object> response = new HashMap<>();
		         response.put("Message", "Products found");
		         response.put("Products", products);
		         
		         return ResponseEntity.ok(response);
		     } catch (Exception e) {
		         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
		     }
		 }


}		 
		 
		 
		 
		 
		 
		 
		