package org.intercom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RedirectController {
	
	@RequestMapping(value = "/acces_interdit", method = RequestMethod.GET)
	public String goAccessDenied(ModelMap model) {
		model.addAttribute("message",
				"Vous n'avez pas les droits suffisants pour accéder à cette page.");
		return "acces_interdit";
	}

}