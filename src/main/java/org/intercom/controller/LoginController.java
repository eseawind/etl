package org.intercom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String goLogin() {
		return "login";
	}

	@RequestMapping(value = "/login_failed", method = RequestMethod.GET)
	public String goLoginFailed(ModelMap model) {
		Boolean error = true;
		model.addAttribute("error", error);
		return "login";

	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String goLogout(ModelMap model) {
		return "logout";
	}

}
