package e_commerce.agri.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j 
public class TestController {
	
	@GetMapping("/test")
	public String demo() {
		log.info("testing");
		return "This is testing";
	}

}
