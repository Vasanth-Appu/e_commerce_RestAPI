package e_commerce.agri.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import e_commerce.agri.modal.Farmer;
import e_commerce.agri.service.FarmerService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/farmer")
public class FarmerController {
    @Autowired
    FarmerService farmerService;

    @PostMapping("/login")
    public ResponseEntity<?> farmerLogin(@RequestParam(name="email") String email, @RequestParam(name="password") String password) {
        try {
            boolean isAuthenticated = farmerService.authenticate(email, password);
            if (isAuthenticated) {
                return ResponseEntity.ok("Login successful");
            } else {
                return ResponseEntity.status(401).body("Invalid email or password");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> saveFarmer(@Valid @RequestBody Farmer farmer, BindingResult result) throws Exception {
    	System.out.println("Farmer details: " + farmer);

        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(
                result.getAllErrors().stream()
                      .map(ObjectError::getDefaultMessage)
                      .toList()
            );
        }
   Farmer farmerr= farmerService.signup(farmer);
        Map <String,Object> created = new HashMap();
        created.put("Status", "successfully created");
        created.put("Data",farmerr);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    


}

