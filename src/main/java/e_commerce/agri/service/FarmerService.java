package e_commerce.agri.service;



import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import e_commerce.agri.modal.Farmer;
import e_commerce.agri.repository.FarmerRepo;


@Service
public class FarmerService {
	
	@Autowired FarmerRepo farmerRepo;

	public Farmer signup(Farmer farmer) {
		
//		Farmer newFarmer = new Farmer();
//		
//		newfarmer.se
//		
//     farmer
//	 farmer.setFarmerContact("1234567890");
//	farmer.setFarmerEmail("farmer@gmail.com");
//	farmer.setPassword("Farmer@123");
		return farmerRepo.save(farmer);
	}

	public boolean authenticate(String farmerEmail, String password) {
	    // Fetch farmer by email
	    Optional<Farmer> farmer = farmerRepo.findByFarmerEmail(farmerEmail);

	    if (farmer.isPresent()) {
	        Farmer existingFarmer = farmer.get();

	        // Log farmer details for debugging
	        System.out.println("Farmer Email: " + existingFarmer.getFarmerEmail());
	        System.out.println("Farmer Name: " + existingFarmer.getFarmerName());

	        // Check if the password matches
	        if (existingFarmer.getPassword() != null && existingFarmer.getPassword().equals(password)) {
	            return true; // Email and password match
	        }
	    }

	    return false; // Either farmer doesn't exist or password doesn't match
	}



	 
}

