package e_commerce.agri.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import e_commerce.agri.modal.Farmer;
import e_commerce.agri.modal.Products;
import e_commerce.agri.repository.FarmerRepo;
import e_commerce.agri.repository.ProductsRepo;

@Service
public class OrderService {
	@Autowired ProductsRepo productsRepo;
	
	@Autowired MailSender  javaMailSender;
	
	@Autowired FarmerRepo farmerRepo;

	public Map<String, Object> orderedItem(long productid, String farmerEmail, long quantity,String customerEmail)throws Exception {
		Map<String,Object> response = new HashMap<>();
			Optional<Products>	selectedProduct= productsRepo.findById(productid);
			if(selectedProduct .isEmpty()) {
				throw new Exception("Product Not Found");
			}
			Products product = selectedProduct.get();
			if(product.getStock() < quantity ) {
				throw new Exception ("Insufficent Stock !!");
			}
			  product.setStock(product.getStock() - quantity);
			    productsRepo.save(product);
			   
			    if (product.getStock() <= 0) {
			       product.setIsAvailable(false);
				    productsRepo.save(product);
			    }
			  //  product.updateAvailability();
			   // productsRepo.save(product);
			    
			    Farmer farmerDetails = farmerRepo.findByFarmerEmail(farmerEmail).get();
				   farmerDetails.getFarmerName();
				   farmerDetails.getFarmerEmail();
//			    // send mail
			    String message = String.format(
			    	    "Dear %s,\n\nYour product '%s' has been ordered successfully.\nQuantity: %d\n\nCustomer Email: %s\n\n Thank you!\n\nRegards,\nTeam Apsa 😉😉",
			    	    farmerDetails.getFarmerName(),  // Farmer's name
			    	    product.getProductName(),       // Product name
			    	    quantity,                       // Quantity ordered
			    	    customerEmail                  // Customer's email
			    	);
			
		        // sendEmail(farmerEmail, "Order Confirmation", message,customerEmail); make to call the function  if other network when its connected----------
			    
			    response.put("message", "Order placed successfully");
			    response.put("productId", productid);
			    response.put("quantity", quantity);
				return response;
	}
	 private void sendEmail(String to, String subject, String body,String from) { 
	        SimpleMailMessage mailMessage = new SimpleMailMessage();
	        mailMessage.setFrom(from);
	        mailMessage.setTo(to);
	        mailMessage.setSubject(subject);
	        mailMessage.setText(body);
	        javaMailSender.send(mailMessage);
	    }

}
