package e_commerce.agri.modal;


import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long category_Id;
	
	@Column(name="s_no")
	private long sNO;
	
	public long getsNO() {
		return sNO;
	}

	public void setsNO(long sNO) {
		this.sNO = sNO;
	}

	public long getCategory_Id() {
		return category_Id;
	}

	public void setCategory_Id(long category_Id) {
		this.category_Id = category_Id;
	}

	@Column(name="category_name", nullable = false)
	private String categoryName;
	
//	  @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
//	    private List<Products> products;
//
//	public List<Products> getProducts() {
//		return products;
//	}
//
//	public void setProducts(List<Products> products) {
//		this.products = products;
//	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
}
