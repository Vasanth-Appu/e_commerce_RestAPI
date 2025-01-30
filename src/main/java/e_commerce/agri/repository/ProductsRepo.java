package e_commerce.agri.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import e_commerce.agri.modal.Category;
import e_commerce.agri.modal.Customer;
import e_commerce.agri.modal.Products;

@Repository
public interface ProductsRepo extends JpaRepository<Products,Long>{

	List<Products> findByIsAvailableTrue();

    List<Products> findByProductName(String productName);




}
