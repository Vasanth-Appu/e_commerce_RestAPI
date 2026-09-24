package e_commerce.agri.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import e_commerce.agri.exceptionHandler.NotFoundException;
import e_commerce.agri.exceptionHandler.AlreadyExistsException;
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

	public String saveCart1(String emailId, long productId) throws Exception {

		Optional<Customer> customerOptional = customerRepo.findByEmailId(emailId);
		boolean isPresent = cartRepo.isProductInCart(emailId, productId);
		if (isPresent) {
			throw new AlreadyExistsException("Product Already Exixts in your cart");
		}
		if (!customerOptional.isPresent()) {
			throw new NotFoundException("Customer not found with email: " + emailId);
		}
		Customer customer = customerOptional.get();

		Optional<Products> productOptional = productsRepo.findById(productId);
		if (!productOptional.isPresent()) {
			throw new NotFoundException("Product not found with ID: " + productId);
		}
		Products product = productOptional.get();

		Cart newCart = new Cart();
		newCart.setCustomer(customer);
		newCart.setProduct(product);
		return "saved to Cart";
	}

	public List<Products> getCartProducts(String emailId) {
		return cartRepo.findProductsByCustomerEmail(emailId);
	}

	public boolean removeCart(long productid, String emailId) throws NotFoundException {
		// Find customer by email
		Optional<Customer> customerOpt = customerRepo.findByEmailId(emailId);
		Optional<Products> productOpt = productsRepo.findById(productid);

		// Ensure both customer and product exist
		if (customerOpt.isPresent() && productOpt.isPresent()) {
			Customer customer = customerOpt.get();
			Products product = productOpt.get();

			// Find the cart item(s) associated with the product and customer
			List<Cart> cartItems = cartRepo.findAll().stream()
					.filter(c -> c.getProduct().getProductId() == product.getProductId() &&
							c.getCustomer().getCustomer_id() == customer.getCustomer_id())
					.collect(Collectors.toList());
			// Remove the items from the cart
			if (!cartItems.isEmpty()) {
				cartRepo.deleteAll(cartItems);
				System.out.println("Cart item(s) removed: " + cartItems);
				return true;
			} else {
				throw new NotFoundException("Item Not Found");
			}
		} else {
			System.out.println("Customer or product not found.");
		}
		return false;
	}

}
