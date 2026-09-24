package e_commerce.agri.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {
    private String emailId;
    private String cusName;
    private String custContact;

}
