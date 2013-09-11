package org.intercom.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.intercom.model.FTPConnectionBean;
import org.intercom.service.FTPConnectionService;
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
public class FTPConnectionController {

	@Autowired
	private FTPConnectionService ftpConnectionService;

	@RequestMapping(value = "/ftp_add", method = RequestMethod.GET)
	public String showForm(ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		FTPConnectionBean ftpConnectionBean = new FTPConnectionBean();
		model.addAttribute("FTPConnectionBean", ftpConnectionBean);
		return "ftp_add";
	}

	@RequestMapping(value = "/ftp_add", method = RequestMethod.POST)
	public void processForm(
			@RequestParam("action") String actionButton,
			@ModelAttribute(value = "FTPConnectionBean") @Valid FTPConnectionBean ftpConnection,
			BindingResult result, ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		if (!result.hasErrors()) {
			if (actionButton.equals("Tester la connexion")) {
				try {
					if (ftpConnectionService.testFTPConnection(ftpConnection)) {
						model.addAttribute("msg_succcess",
								"Connexion avec succès");
					} else {
						model.addAttribute("msg_error", "Problème de connexion");
					}
				} catch (BusinessLayerException e) {
					model.addAttribute("msg_error",
							"Une erreur s'est produite lors du traitement de votre demande.");
				}
			} else {
				try {
					Boolean exists = ftpConnectionService.exists(ftpConnection);
					if (!exists) {
						ftpConnectionService.saveFTPConnection(ftpConnection);
						model.addAttribute("msg_succcess",
								"Connexion enregistrée");
					} else {
						model.addAttribute("msg_error",
								"Une connexion FTP avec les mêmes paramètres existe déjà.");
					}
				} catch (BusinessLayerException e1) {
					model.addAttribute("msg_error",
							"Une erreur s'est produite lors du traitement de votre demande.");
				}
			}
		}
	}

	@RequestMapping(value = "/ftp_list", method = RequestMethod.GET)
	public String goListFTPConnections(ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		List<FTPConnectionBean> ftpConnectionBeanList;
		try {
			ftpConnectionBeanList = ftpConnectionService.getConnections();
			model.addAttribute("ftpConnectionList", ftpConnectionBeanList);
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		return "ftp_list";
	}

	@RequestMapping(value = "/ftp_delete", method = RequestMethod.GET)
	public String deleteConnection(@RequestParam("id") Integer id,
			ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			ftpConnectionService.deleteById(id);
			model.addAttribute("msg_succcess", "Suppression avec succès");
		} catch (Exception e) {
			model.addAttribute("msg_error",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}

		try {
			List<FTPConnectionBean> ftpConnectionBeanList = ftpConnectionService
					.getConnections();
			model.addAttribute("ftpConnectionList", ftpConnectionBeanList);
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		return "ftp_list";
	}

	@RequestMapping(value = "/ftp_edit", method = RequestMethod.GET)
	public String editConnection(@RequestParam("id") Integer id,
			ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			FTPConnectionBean ftpConnectionBean = ftpConnectionService
					.getById(id);
			model.addAttribute("FTPConnectionBean", ftpConnectionBean);
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		return "ftp_edit";
	}

	@RequestMapping(value = "/ftp_edit", method = RequestMethod.POST)
	public void update(
			@RequestParam("action") String actionButton,
			@ModelAttribute(value = "FTPConnectionBean") @Valid FTPConnectionBean ftpConnectionBean,
			BindingResult result, ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		if (!result.hasErrors()) {
			if (actionButton.equals("Tester la connexion")) {
				try {
					if (ftpConnectionService
							.testFTPConnection(ftpConnectionBean)) {
						model.addAttribute("msg_succcess",
								"Connexion avec succès");
					} else {
						model.addAttribute("msg_error", "Problème de connexion");
					}
				} catch (BusinessLayerException e) {
					model.addAttribute("msg_error",
							"Une erreur s'est produite lors du traitement de votre demande.");
				}
			} else {
				try {
					ftpConnectionService.update(ftpConnectionBean);
					model.addAttribute("msg_succcess",
							"Modification avec succès");
				} catch (BusinessLayerException e) {
					model.addAttribute("msg_error",
							"Une erreur s'est produite lors du traitement de votre demande.");
				}
			}
		}
	}

}
