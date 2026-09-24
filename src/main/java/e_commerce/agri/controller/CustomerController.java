package e_commerce.agri.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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

import e_commerce.agri.dto.CustomerDTO;
import e_commerce.agri.dtoMapper.CustomerMapper;
import e_commerce.agri.modal.Category;
import e_commerce.agri.modal.Customer;
import e_commerce.agri.modal.Farmer;
import e_commerce.agri.modal.Products;
import e_commerce.agri.repository.CategoryRepo;
import e_commerce.agri.repository.ProductsRepo;
import e_commerce.agri.service.CustomerService;
import e_commerce.agri.service.OrderService;
import e_commerce.agri.service.OtpService;
import e_commerce.jwt.JwtService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/customer")
@Slf4j
public class CustomerController {
	@Autowired
	CustomerService customerService;
	@Autowired
	ProductsRepo productsRepo;
	@Autowired
	OrderService orderService;
	@Autowired
	CategoryRepo categoryRepo;
	@Autowired
	OtpService otpService;

	private JwtService jwtService;
	private CustomerMapper cusMapper;

	public CustomerController(JwtService jwtService, CustomerMapper cusMapper) {
		this.jwtService = jwtService;
		this.cusMapper = cusMapper;
	}

	@PostMapping("/signup")
	public ResponseEntity<?> customerSignup(@Valid @RequestBody Customer customer, BindingResult result)
			throws Exception {
		if (result.hasErrors()) {
			result.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(
					result.getAllErrors().stream()
							.map(ObjectError::getDefaultMessage)
							.toList());
		}

		try {
			Customer createdCustomer = customerService.signup(customer);
			CustomerDTO resMapper = cusMapper.toDTO(createdCustomer);
			Map<String, Object> created = new HashMap<>();
			created.put("Status", "Successfully created");
			created.put("Data", resMapper);

			return ResponseEntity.status(HttpStatus.CREATED).body(created);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(Map.of("Status", "error", "Message", e.getMessage()));
		}

	}

	@PostMapping("/login")
	public ResponseEntity<?> customerLogin(@RequestParam String email,
			@RequestParam String password) {
		try {
			boolean isAuthenticated = customerService.authenticate(email, password);
			if (!isAuthenticated) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(Map.of("Status", "error", "Message", "Invalid email or password"));
			}

			String otp = "000000";// default otp
			// String otp = String.valueOf(new Random().nextInt(900000) + 100000) ;
			otpService.saveOtp(email, otp);

			// Send OTP via email
			otpService.sendOtp(email, otp);
			// Send OTP via SMS
			otpService.sendSms(email, otp);

			return ResponseEntity.ok(Map.of("Status", "OTP Sent", "Message", "Check your email for OTP"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("Status", "error", "Message", e.getMessage()));
		}
	}

	@PostMapping("/verify-otp")
	public ResponseEntity<?> verifyOtp(@RequestParam String email,
			@RequestParam String otp, HttpSession session) {
		try {

			boolean isOtpValid = otpService.verifyOtp(email, otp);
			if (!isOtpValid) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(Map.of("Status", "FAILED", "Message", "Invalid or expired OTP"));
			}
			session.setAttribute("userEmail", email);
			List<Products> products = productsRepo.findAll().stream()
					.filter(x -> x.isAvailable()).collect(Collectors.toList());
			otpService.deleteOtp(email);
			String token = jwtService.generateToken(email);
			session.setAttribute("curToken", token);
			log.info(token);
			return ResponseEntity.ok(Map.of("Status", "Login Successful", "Token", token));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("Status", "error", "Message", e.getMessage()));
		}
	}

	@PostMapping("/order")
	public ResponseEntity<?> orderItem(@RequestParam(name = "productId") long id,
			@RequestParam(name = "farmerEmail") String toEmail, @RequestParam(name = "customerEmail") String sendEmail,
			@RequestParam(name = "quantity") long quantity) {
		try {
			Map<String, Object> response = orderService.orderedItem(id, toEmail, quantity, sendEmail);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			// Return error response
			return ResponseEntity.status(400).body(e.getMessage());
		}

	}

	@GetMapping("/search") // search by product name
	public ResponseEntity<?> searchProducts(@RequestParam(name = "parameter") String value) {
		try {

			// Use the updated query for exact match
			List<Products> products = productsRepo.findByProductName(value.toLowerCase());
			// Optional<Category> product = categoryRepo.findByCategoryName(
			// value.toLowerCase());

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
