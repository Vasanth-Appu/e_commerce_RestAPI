package e_commerce.agri.modal;



import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.annotation.Nonnull;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="customer")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Customer {
	
		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		
		private long customer_id;
		
		@Column(name="customer_name", nullable = false)
		private String cusName ;
		

		private String password;
		
		@Column( name="cust_email",unique=true)
		private  String emailId;
		
		@Column(name="cus_address")
	    private String cusAddress;
	    
		@Column(name = "cus_contact" ,nullable = false,unique= true)
	    private String custContact;
	
	    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	    private List<Cart> cartItems;
	    
	    
	    @CreationTimestamp
	    @Column(name = "created_time", nullable = false, updatable = false)
		protected Date createdTime	;
		
		
		public long getCustomer_id() {
			return customer_id;
		}


		public void setCustomer_id(long customer_id) {
			this.customer_id = customer_id;
		}


		public String getCusName() {
			return cusName;
		}


		public void setCusName(String cusName) {
			this.cusName = cusName;
		}


		public String getPassword() {
			return password;
		}


		public void setPassword(String password) {
			this.password = password;
		}


		public String getemailId() {
			return emailId;
		}


		public void setemailId(String emailId) {
			this.emailId = emailId;
		}


		public String getCusAddress() {
			return cusAddress;
		}


		public void setCusAddress(String cusAddress) {
			this.cusAddress = cusAddress;
		}


		public String getCustContact() {
			return custContact;
		}


		public void setCustContact(String custContact) {
			this.custContact = custContact;
		}


		public Date getCreatedTime() {
			return createdTime;
		}


		public void setCreatedTime(Date createdTime) {
			this.createdTime = createdTime;
		}


	 
}

