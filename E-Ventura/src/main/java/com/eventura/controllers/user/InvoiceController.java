package com.eventura.controllers.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"invoice"})

public class InvoiceController {
	@GetMapping({"checkout"})
	public String Checkout() {
		return "customer/pages/invoice/checkout";
	}

}
