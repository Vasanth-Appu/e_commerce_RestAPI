package e_commerce.agri.modal;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "farmer_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Farmer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="farmer_id")
    private Long farmerId;

    public Long getFarmerId() {
		return farmerId;
	}

	public void setFarmerId(Long farmerId) {
		this.farmerId = farmerId;
	}

	public String getFarmerName() {
		return farmerName;
	}

	public void setFarmerName(String farmerName) {
		this.farmerName = farmerName;
	}

	public String getFarmerContact() {
		return farmerContact;
	}

	public void setFarmerContact(String farmerContact) {
		this.farmerContact = farmerContact;
	}

	public String getFarmerEmail() {
		return farmerEmail;
	}

	public void setFarmerEmail(String farmerEmail) {
		this.farmerEmail = farmerEmail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "farmer_name", nullable = false)
    private String farmerName;

	@Column(name = "farmer_contact", nullable = false)
    private String farmerContact;

    @Column(name = "farmer_email", nullable = true)
    private String farmerEmail;

    @Column(name = "password", nullable = false)
    private String password;


    @OneToMany(mappedBy = "farmer", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Products> products;
    


  public List<Products> getProducts() {
		return products;
	}

	public void setProducts(List<Products> products) {
		this.products = products;
	}

//@CreationTimestamp
//  @Column(name = "created_time", nullable = false, updatable = false)
//	protected Date createdTime	;

  
    
    
} 


    

