package e_commerce.agri.service;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import e_commerce.agri.dto.ProductDto;
import e_commerce.agri.modal.Category;
import e_commerce.agri.modal.Farmer;
import e_commerce.agri.modal.Products;
import e_commerce.agri.repository.CategoryRepo;
import e_commerce.agri.repository.FarmerRepo;
import e_commerce.agri.repository.ProductsRepo;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class ProductService {
    
    @Autowired 
    private ProductsRepo productRepo;
    
    @Autowired 
    private CategoryRepo categoryRepo;
    
    @Autowired 
    private FarmerRepo farmerRepo;

    @Transactional
    public Products uploadProduct(@Valid ProductDto productDto, String farmerEmail) {
        try {
            // Create a new Products entity from the ProductDto
            Products product = new Products();
            product.setProductName(productDto.getProductName());
            product.setProductDescription(productDto.getProductDescription());
            product.setStock(productDto.getStock());
            product.setPrice(productDto.getPrice());
            product.setUnit(productDto.getUnit());
            product.setFarmerEmail(productDto.getFarmerEmail());

            // Fetch the farmer by email
            Farmer farmer = farmerRepo.findByFarmerEmail(farmerEmail)
                    .orElseThrow(() -> new RuntimeException("Farmer not found for email: " + farmerEmail));

            // Set farmer in the product
            product.setFarmer(farmer);

            // Handle category if provided
            if (productDto.getCategoryName() != null) {
                Category category = categoryRepo.findByCategoryName(productDto.getCategoryName()) // Updated to use findByCategoryName
                        .orElseThrow(() -> new RuntimeException("Category not found"));
                product.setCategory(category);
            }

         // Handle product image if provided
            if (productDto.getProductImage() != null && productDto.getProductImage().length > 0) {
                String base64Image = productDto.getProductImage().toString();
                
                // Check and remove MIME prefix if present
                if (base64Image.startsWith("data:image/")) {
                    base64Image = base64Image.substring(base64Image.indexOf(",") + 1);
                }

                // Decode the Base64 image string
                try {
                    byte[] decodedImage = Base64.getDecoder().decode(base64Image);
                    product.setProductImage(decodedImage);
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Failed to decode Base64 image", e);
                }
            }

            // Save the product
            return productRepo.save(product);

        } catch (Exception e) {
            throw new RuntimeException("Error uploading product: " + e.getMessage(), e);
        }
    }

    public List<ProductDto> getProductsByFarmerEmail(String farmerEmail) {
        Farmer farmer = farmerRepo.findByFarmerEmail(farmerEmail)
                .orElseThrow(() -> new RuntimeException("Farmer with email " + farmerEmail + " not found"));

        return farmer.getProducts().stream().map(product -> {
            ProductDto dto = new ProductDto();
            dto.setProductName(product.getProductName());
            dto.setProductDescription(product.getProductDescription());
            dto.setStock(product.getStock());
            dto.setPrice(product.getPrice());
            dto.setUnit(product.getUnit());
            Category category = new Category();
            category.setCategory_Id(product.getCategory().getCategory_Id());
            category.setCategoryName(product.getCategory().getCategoryName());
            dto.setCategory(category);
            return dto;
        }).collect(Collectors.toList());
    }

}
