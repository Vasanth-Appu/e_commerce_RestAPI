package e_commerce.agri.dtoMapper;

import org.mapstruct.Mapper;

import e_commerce.agri.dto.CustomerDTO;
import e_commerce.agri.modal.Customer;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerDTO toDTO(Customer customer);
}
