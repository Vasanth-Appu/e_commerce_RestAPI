package e_commerce.agri.dto;

import e_commerce.agri.modal.Category;

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

   // private String productImage;


    public String getFarmerEmail() {
		return farmerEmail;
	}

	public void setFarmerEmail(String farmerEmail) {
		this.farmerEmail = farmerEmail;
	}

	public byte[] getProductImage() {
		return productImage;
	}

	public void setProductImage(byte[] productImage) {
		this.productImage = productImage;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Category getCategory() {
		return category;
	}

	// Constructor
    public ProductDto() {
        this.productName = productName;
        this.productDescription = productDescription;
        this.stock = stock;
        this.price = price;
        this.unit = unit;
        this.categoryName = categoryName;
        this.category=category;
        this.productImage=productImage;
        this.farmerEmail=farmerEmail;
    }

    // Getters and Setters
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public long getStock() {  // Changed to String
        return stock;
    }

    public void setStock(long stock) {  // Changed to String
        this.stock = stock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

	

}
