package e_commerce.agri.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import e_commerce.agri.dto.ProductDto;
import e_commerce.agri.modal.Products;
import e_commerce.agri.service.ProductService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/product")
public class ProductsController {

    @Autowired 
    ProductService productService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadProducts(@Valid @RequestBody ProductDto productDto, 
                                            @RequestParam(name="email") String farmerEmail, 
                                            BindingResult result) {
        // Check for validation errors
        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(
                result.getAllErrors().stream()
                      .map(ObjectError::getDefaultMessage)
                      .toList()
            );
        }

        try {
            // Upload the product by passing the DTO to the service method
            Products newProduct = productService.uploadProduct(productDto, farmerEmail);

            Map<String, Object> uploadedProduct = new HashMap<>();
            uploadedProduct.put("Status", "Successfully Uploaded");
            uploadedProduct.put("Data", newProduct);
            System.out.println(uploadedProduct);
            return ResponseEntity.status(HttpStatus.CREATED).body(uploadedProduct);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Map.of("error", e.getMessage())
            );
        }
    }

    @GetMapping("/getProduct")
    public ResponseEntity<?> getProduct(@RequestParam(name = "farmerEmail") String farmerEmail) {
        try {
            // Retrieve the products by farmer email
            List<ProductDto> retrieved = productService.getProductsByFarmerEmail(farmerEmail);
            Map<String, Object> dataRetrieved = new HashMap<>();
            dataRetrieved.put("Email", farmerEmail);
            dataRetrieved.put("Status", "Successfully Retrieved");
            dataRetrieved.put("Retrieved Products", retrieved);

            return ResponseEntity.status(HttpStatus.OK).body(dataRetrieved);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "error", e.getMessage()
            ));
        }
    }
}
