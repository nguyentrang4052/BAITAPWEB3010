package vn.iotstar.controllers.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web")
public class HomeController {
	@GetMapping("/home")
	public String index() {
		return "web/home";
	}
}
