package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SomeController {

	@Autowired
	SomeService someService;
		
	@PostMapping("/retry")
	public void doRetry() {
		someService.duh();
	}

}
