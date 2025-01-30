package e_commerce.agri.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import e_commerce.agri.modal.Cart;
import e_commerce.agri.modal.Customer;
import e_commerce.agri.modal.Products;

@Repository
public interface CartRepo extends JpaRepository<Cart,Long > {


    @Query("SELECT c.product FROM Cart c WHERE c.customer.emailId = :emailId")
    List<Products> findProductsByCustomerEmail(@Param("emailId") String emailId);

}
