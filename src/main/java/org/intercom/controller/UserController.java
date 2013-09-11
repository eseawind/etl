package org.intercom.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.intercom.model.UserBean;
import org.intercom.service.UserService;
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
public class UserController {
	@Autowired
	UserService userService;

	@RequestMapping(value = "/user_list", method = RequestMethod.GET)
	public String showList(ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			List<UserBean> userBeanList = userService.getUsersList();
			model.addAttribute("userList", userBeanList);
		} catch (BusinessLayerException e) {
			String msg_error = "Une erreur s'est produite lors du traitement de votre demande.";
			model.addAttribute("msg_error", msg_error);
		}
		return "user_list";
	}

	@RequestMapping(value = "/user_add", method = RequestMethod.GET)
	public String showForm(ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			UserBean userBean = new UserBean();
			userBean = userService.prepareUserBean(userBean);
			model.addAttribute("userBean", userBean);
		} catch (BusinessLayerException e) {
			String msg_error = "Une erreur s'est produite lors du traitement de votre demande.";
			model.addAttribute("msg_error", msg_error);
		}
		return "user_add";
	}

	@RequestMapping(value = "/user_add", method = RequestMethod.POST)
	public String processForm(
			@ModelAttribute(value = "UserBean") @Valid UserBean userBean,
			BindingResult result, ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		if (!result.hasErrors()) {
			try {
				if (!userService.exists(userBean)) {
					try {
						userService.saveUser(userBean);
						model.addAttribute("msg_succcess",
								"Utilsateur enregistré");
						List<UserBean> userBeanList = userService
								.getUsersList();
						model.addAttribute("userList", userBeanList);
						return "user_list";
					} catch (Exception e) {
						model.addAttribute("msg_error",
								"Une erreur s'est produite lors du traitement de votre demande.");
					}
				} else {
					model.addAttribute(
							"msg_error",
							"Le nom d'utilisateur saisie existe déjà. Veuillez choisir un autre nom d'utilisateur.");
				}
			} catch (BusinessLayerException e) {
				model.addAttribute("msg_error",
						"Une erreur s'est produite lors du traitement de votre demande.");
			}
		}
		try {
			userBean = userService.prepareUserBean(userBean);
			model.addAttribute("userBean", userBean);
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		return "user_add";
	}

	@RequestMapping(value = "/user_delete", method = RequestMethod.GET)
	public String deleteConnection(@RequestParam("id") Integer id,
			ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			userService.deleteById(id);
			model.addAttribute("msg_succcess", "Suppression avec succès");
		} catch (Exception e) {
			model.addAttribute("msg_error", "Erreur dans la suppression");
		}
		try {
			List<UserBean> userBeanList = userService.getUsersList();
			model.addAttribute("userList", userBeanList);
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		return "user_list";
	}

	@RequestMapping(value = "/user_edit", method = RequestMethod.GET)
	public String editUser(@RequestParam("id") Integer id, ModelMap model,
			Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			UserBean userBean = userService.getById(id);
			userBean = userService.prepareEditableUserBean(userBean);
			model.addAttribute("userBean", userBean);
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		return "user_edit";
	}

	@RequestMapping(value = "/user_edit", method = RequestMethod.POST)
	public String updateUser(
			@ModelAttribute(value = "userBean") @Valid UserBean userBean,
			BindingResult result, ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		if (!result.hasErrors()) {
			try {
				userService.update(userBean);
				model.addAttribute("msg_succcess", "Modification avec succès");
				List<UserBean> userBeanList = userService.getUsersList();
				model.addAttribute("userList", userBeanList);
				return "user_list";
			} catch (BusinessLayerException e) {
				model.addAttribute("msg_error",
						"Une erreur s'est produite lors du traitement de votre demande.");
			}
		}
		try {
			userBean = userService.prepareUserBean(userBean);
			model.addAttribute("userBean", userBean);
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		return "user_edit";
	}

	@RequestMapping(value = "/user_details", method = RequestMethod.GET)
	public String detailUserRight(@RequestParam("id") Integer id,
			ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			UserBean userBean = userService.getById(id);
			model.addAttribute("userBean", userBean);
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		return "user_detalis";
	}
}
