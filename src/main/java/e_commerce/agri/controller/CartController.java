package e_commerce.agri.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import e_commerce.agri.modal.Cart;
import e_commerce.agri.modal.Products;
import e_commerce.agri.repository.CustomerRepo;
import e_commerce.agri.service.CartService;

@RestController
@RequestMapping("/cart")
public class CartController {
	@Autowired CartService cartService;
	
	@PostMapping("/addproduct")
	public Cart uploadCart(@RequestParam (name ="emailId") String emailId , @RequestParam(name="productId")long productId) throws Exception{
		
		return cartService.saveCart1(emailId,productId);
		
	}
	@GetMapping("/getcart")
		public ResponseEntity<?>getCart(@RequestParam(name="emailId")String emailId){
		List<Products>cartProduct=cartService.getCartProducts(emailId);
		if(cartProduct.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("your Cart Is Empty");
		}
		Map<String,Object> response = new HashMap<>();
		response.put("EmailId",emailId);
		response.put("Your Cart",cartProduct);
		
		return ResponseEntity.status(HttpStatus.FOUND).body(response);
	}
}
