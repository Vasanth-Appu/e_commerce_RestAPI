package e_commerce.agri.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import e_commerce.agri.modal.Customer;
public interface CustomerRepo extends JpaRepository <Customer,Long> {

	  Optional<Customer> findByCustContact(String custContact); // For mobile number    // For email
		Optional<Customer> findByEmailId(String emailId);

}
