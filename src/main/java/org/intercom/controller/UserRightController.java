package org.intercom.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.intercom.model.UserRightBean;
import org.intercom.service.UserRightService;
import org.intercom.service.exceptions.BusinessLayerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserRightController {

	@Autowired
	UserRightService userRightService;

	@RequestMapping(value = "/user_right_list", method = RequestMethod.GET)
	public String showList(ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			List<UserRightBean> userRightBeanList = userRightService
					.getUserRightsList();
			model.addAttribute("userRightList", userRightBeanList);
		} catch (BusinessLayerException e) {
			String msg_error = "Une erreur s'est produite lors du traitement de votre demande.";
			model.addAttribute("msg_error", msg_error);
		}
		return "user_right_list";
	}

	@RequestMapping(value = "/user_right_details", method = RequestMethod.GET)
	public String detailUserRight(@RequestParam("id") Integer id,
			ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			UserRightBean userRightBean = userRightService.getById(id);
			model.addAttribute("userRightBean", userRightBean);
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		return "user_right_detalis";
	}

	@RequestMapping(value = "/user_right_add", method = RequestMethod.GET)
	public String showForm(ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			UserRightBean userRightBean = userRightService
					.prepareUserRightBean(new UserRightBean());
			model.addAttribute("userRightBean", userRightBean);
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		return "user_right_add";
	}

	@RequestMapping(value = "/user_right_add", method = RequestMethod.POST)
	public String processForm(
			@ModelAttribute(value = "userRightBean") @Valid UserRightBean userRightBean,
			BindingResult result, ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		if (!result.hasErrors()) {
			try {
				userRightService.saveUserRight(userRightBean);
				model.addAttribute("msg_succcess",
						"Droit utilisateur enregistré");
				List<UserRightBean> userRightBeanList = userRightService
						.getUserRightsList();
				model.addAttribute("userRightList", userRightBeanList);
				return "user_right_list";
			} catch (BusinessLayerException e) {
				model.addAttribute("msg_error",
						"Une erreur s'est produite lors du traitement de votre demande.");
			}
		}
		try {
			userRightBean = userRightService
					.prepareUserRightBean(userRightBean);
			model.addAttribute("userRightBean", userRightBean);
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		return "user_right_add";
	}

	@RequestMapping(value = "/user_right_delete", method = RequestMethod.GET)
	public String deleteUserRight(@RequestParam("id") Integer id,
			ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			userRightService.deleteById(id);
			model.addAttribute("msg_succcess", "Suppression avec succès");
			List<UserRightBean> userRightBeanList = userRightService
					.getUserRightsList();
			model.addAttribute("userRightList", userRightBeanList);
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		return "user_right_list";
	}

	@RequestMapping(value = "/user_right_edit", method = RequestMethod.GET)
	public String editUserRight(@RequestParam("id") Integer id, ModelMap model,
			Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			UserRightBean userRightBean = userRightService.getById(id);
			userRightBean = userRightService
					.prepareEditableUserRightBean(userRightBean);
			model.addAttribute("userRightBean", userRightBean);
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		return "user_right_edit";
	}

	@RequestMapping(value = "/user_right_edit", method = RequestMethod.POST)
	public String updateUserRight(
			@ModelAttribute(value = "userRightBean") @Valid UserRightBean userRightBean,
			BindingResult result, ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		if (!result.hasErrors()) {
			try {
				userRightService.update(userRightBean);
				model.addAttribute("msg_succcess", "Modification avec succès");
				List<UserRightBean> userRightBeanList = userRightService
						.getUserRightsList();
				model.addAttribute("userRightList", userRightBeanList);
				return "user_right_list";
			} catch (BusinessLayerException e) {
				model.addAttribute("msg_error",
						"Une erreur s'est produite lors du traitement de votre demande.");
			}
		}
		try {
			userRightBean = userRightService
					.prepareUserRightBean(userRightBean);
			model.addAttribute("userRightBean", userRightBean);
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		return "user_right_edit";
	}
}
