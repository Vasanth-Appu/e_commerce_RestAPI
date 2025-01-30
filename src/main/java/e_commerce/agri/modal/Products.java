package e_commerce.agri.modal;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "product_id")
    private long productId;
    
    

    @NotBlank(message = "Product name is mandatory")
    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_desc")
    private String productDescription;

   public long getProductId() {
		return productId;
	}

	

	// @Positive(message = "Stock must be a positive number")
    @Column(name = "product_stock", nullable = false)
    private long stock; // Changed from String to int

    @Positive(message = "Price must be greater than zero")
    @Column(name = "product_price", nullable = false)
    private double price;

    @NotBlank(message = "Unit is mandatory")
    @Column(name = "product_unit", nullable = false)
    private String unit;

	@NotNull(message = "Category is mandatory")
    @Valid
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id", referencedColumnName = "s_no")
    private Category category;


	@Lob
    @Column(name = "product_img")
    private byte[] productImage;

//    @NotNull(message = "Farmer is mandatory")
	@ManyToOne
	@JoinColumn(name = "farmer_id", nullable = false)
	
	@JsonIgnore
	private Farmer farmer;
	
	  @Column(name = "farmer_email", nullable = false)
	    private String farmerEmail;

	   @Column(name = "is_available", nullable = false)
	    private boolean isAvailable = true; // Default value
	     
	   @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	   private List<Cart> cartItems;
	   
	   public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName.toLowerCase();
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public long getStock() {
		return stock;
	}

	public void setStock(long stock) {
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

	

	public byte[] getProductImage() {
		return productImage;
	}

	public void setProductImage(byte[] productImage) {
		this.productImage = productImage;
	}

	

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

 
	   
    public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	    public boolean getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

//		public void updateAvailability() {
//	        this.isAvailable = (this.stock > 0) ? true : false;
//	    }

	public String getFarmerEmail() {
		return farmerEmail;
	}

	public void setFarmerEmail(String farmerEmail) {
		this.farmerEmail = farmerEmail.toLowerCase();
	}

	public Farmer getFarmer() {
		return farmer;
	}

	public void setFarmer(Farmer farmer) {
		this.farmer = farmer;
	}

	@CreationTimestamp
    @Column(name = "created_time", nullable = false, updatable = false)
    private Date createdTime;
}
