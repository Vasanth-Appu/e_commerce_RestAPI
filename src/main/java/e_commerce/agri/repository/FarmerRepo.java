package e_commerce.agri.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import e_commerce.agri.modal.Category;
import e_commerce.agri.modal.Farmer;
@Repository
public interface FarmerRepo extends JpaRepository<Farmer,Long>{

	    Optional<Farmer> findByFarmerEmail(String farmerEmail);
	
}

