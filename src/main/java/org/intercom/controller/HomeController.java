package org.intercom.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {
	
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String goHome(ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		return "home";
	}
	
	@RequestMapping(value = "/access_denied", method = RequestMethod.GET)
	public String goAccessDenied() {
		return "access_denied";
	}
}
