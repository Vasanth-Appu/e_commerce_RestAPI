package e_commerce.agri.dto;

import e_commerce.agri.modal.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class ProductDto {

    private String productName;
    private String productDescription;
    private long stock;  // Changed from String to String for quantity
    private double price;
    private String unit;
    private String categoryName;
    private Category category;
    private byte[]productImage;
    private String farmerEmail;



	

}
