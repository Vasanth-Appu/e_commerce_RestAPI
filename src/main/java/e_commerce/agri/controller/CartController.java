package e_commerce.agri.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import e_commerce.agri.exceptionHandler.NotFoundException;
import e_commerce.agri.modal.Products;
import e_commerce.agri.service.CartService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/cart")
@Slf4j 
public class CartController {
	private final CartService cartService;

	CartController(CartService cartService) {
		this.cartService = cartService;
	}

	@PostMapping("/addproduct")
	public ResponseEntity<?> uploadCart(@RequestParam(name = "email") String emailId,
			@RequestParam(name = "productID") long productId ,HttpSession session) throws Exception {

		String res = cartService.saveCart1(emailId, productId);
		return ResponseEntity.status(HttpStatus.OK).body(res);
	}

	@GetMapping("/getcart")
	public ResponseEntity<?> getCart(@RequestParam(name = "email") String emailId) {
		List<Products> cartProduct = cartService.getCartProducts(emailId);
		if (cartProduct.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body("your Cart Is Empty");
		}
		Map<String, Object> response = new HashMap<>();
		response.put("EmailId", emailId);
		response.put("Your Cart", cartProduct);

		return ResponseEntity.status(HttpStatus.FOUND).body(response);
	}

	@DeleteMapping("/removecart")
	public ResponseEntity<?> removeCart(
			@RequestParam(name = "id") long id,
			@RequestParam(name = "emailId") String emailId) throws NotFoundException{

		boolean isRemoved = cartService.removeCart(id, emailId);

		if (isRemoved) {
			return ResponseEntity.ok("Item removed from the cart successfully.");
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Item not found or does not belong to the user.");
		}
	}
}
