package org.intercom.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.intercom.model.ConnectionBean;
import org.intercom.service.DBconnectService;
import org.intercom.service.exceptions.BusinessLayerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("ConnectionBean")
public class DBConnectionController {
	@Autowired
	private DBconnectService dbConnectService;

	@RequestMapping(value = "/connection_add", method = RequestMethod.GET)
	public String showForm(ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			ConnectionBean connectionBean = new ConnectionBean();
			dbConnectService.prepareConnectionBean(connectionBean);
			model.addAttribute("ConnectionBean", connectionBean);
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		return "connection_add";
	}

	@RequestMapping(value = "/connection_add", method = RequestMethod.POST)
	public String processForm(
			@RequestParam("action") String actionButton,
			@ModelAttribute(value = "ConnectionBean") @Valid ConnectionBean connectionBean,
			BindingResult result, ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		if (!result.hasErrors()) {
			try {
				if (actionButton.equals("Tester la connexion")) {
					Boolean testOK = dbConnectService.connectToDB(connectionBean);
					if (!testOK) {
						model.addAttribute("msg_error", "Problème de connexion à la base de données");
					} else {
						model.addAttribute("msg_succcess", "Connexion avec succès");
					}
				} else {
					if (!dbConnectService.exists(connectionBean)) {
						try {
							dbConnectService.saveDBConnection(connectionBean);
							model.addAttribute("msg_succcess", "Connexion enregistrée");
							List<ConnectionBean> connectionBeanList = dbConnectService
									.getConnextions();
							model.addAttribute("connectionList", connectionBeanList);
							return "connection_list";
						} catch (Exception e) {
							model.addAttribute("msg_error", "Une erreur s'est produite lors du traitement de votre demande.");
						}
					} else {
						model.addAttribute("msg_error", "Une connexion avec les mêmes paramètres existe déjà.");
					}
				}
			} catch (BusinessLayerException e) {
				model.addAttribute("msg_error",
						"Une erreur s'est produite lors du traitement de votre demande.");
			}
		}
		model.addAttribute("ConnectionBean", connectionBean);
		return "connection_add";
	}

	@RequestMapping(value = "/connection_list", method = RequestMethod.GET)
	public String goListConnections(ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			List<ConnectionBean> connectionBeanList = dbConnectService
					.getConnextions();
			model.addAttribute("connectionList", connectionBeanList);
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		return "connection_list";
	}

	@RequestMapping(value = "/db_delete", method = RequestMethod.GET)
	public String deleteConnection(@RequestParam("id") Integer id,
			ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			dbConnectService.deleteById(id);
			model.addAttribute("msg_succcess", "Suppression avec succès");
		} catch (Exception e) {
			model.addAttribute("msg_error", "Erreur dans la suppression");
		}
		try {
			List<ConnectionBean> connectionBeanList = dbConnectService
					.getConnextions();
			model.addAttribute("connectionList", connectionBeanList);
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		return "connection_list";
	}

	@RequestMapping(value = "/connection_edit", method = RequestMethod.GET)
	public String editConnection(@RequestParam("id") Integer id,
			ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			ConnectionBean connectionBean = dbConnectService.getById(id);
			model.addAttribute("ConnectionBean", connectionBean);
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		return "connection_edit";
	}

	@RequestMapping(value = "/connection_edit", method = RequestMethod.POST)
	public String update(
			@RequestParam("action") String actionButton,
			@ModelAttribute(value = "ConnectionBean") @Valid ConnectionBean connectionBean,
			BindingResult result, ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			dbConnectService.prepareConnectionBean(connectionBean);
		} catch (BusinessLayerException e1) {
			model.addAttribute("msg_error",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		if (!result.hasErrors()) {
			if (actionButton.equals("Tester la connexion")) {
				Boolean testOK = dbConnectService.connectToDB(connectionBean);
				if (!testOK) {
					model.addAttribute("msg_error", "Problème de connexion à la base de données");
				} else {
					model.addAttribute("msg_succcess", "Connexion avec succès");
				}
			} else {
				try {
					dbConnectService.update(connectionBean);
					model.addAttribute("msg_succcess", "Modification avec succès");
					List<ConnectionBean> connectionBeanList = dbConnectService
							.getConnextions();
					model.addAttribute("connectionList", connectionBeanList);
					return "connection_list";
				} catch (BusinessLayerException e) {
					model.addAttribute("msg_error",
							"Une erreur s'est produite lors du traitement de votre demande.");
				}
			}
			model.addAttribute("ConnectionBean", connectionBean);
		}
		return "connection_edit";
	}
}
