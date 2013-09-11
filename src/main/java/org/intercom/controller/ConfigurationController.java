package org.intercom.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.intercom.model.ConfigurationBean;
import org.intercom.model.IntervalSelectionBean;
import org.intercom.service.ConfigurationService;
import org.intercom.service.exceptions.BusinessLayerException;
import org.intercom.validator.ConfigurationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;

@Controller
public class ConfigurationController {
	@Autowired
	private ConfigurationService configurationService;
	@Autowired
	private ConfigurationValidator validator;
	private List<IntervalSelectionBean> temp_interva_lList;
	private int id_interval;

	@RequestMapping(value = "/configuration_add", method = RequestMethod.GET)
	public String showForm(ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		String msg_error = "";
		try {
			ConfigurationBean configurationBean = new ConfigurationBean();
			configurationService.prepare(configurationBean);
			model.addAttribute("configurationBean", configurationBean);
			temp_interva_lList = new ArrayList<IntervalSelectionBean>();
			id_interval = 0;
		} catch (BusinessLayerException e) {
			msg_error = e.getMessage();
			model.addAttribute("msg_error", msg_error);
		}
		return "configuration_add";
	}

	@RequestMapping(value = "/configuration_add", method = RequestMethod.POST)
	public String processForm(
			@ModelAttribute(value = "configurationBean") @Valid ConfigurationBean configurationBean,
			BindingResult result, ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			configurationService.prepare(configurationBean);
			model.addAttribute("configurationBean", configurationBean);
			Integer id;
			String interval_name;
			Map<Integer, String> intervalSelectItems = new LinkedHashMap<Integer, String>();
			for (IntervalSelectionBean intervalSelectionBean : temp_interva_lList) {
				id = intervalSelectionBean.getId();
				interval_name = intervalSelectionBean.getStart_line_nb()
						+ " - " + intervalSelectionBean.getEnd_line_nb();
				intervalSelectItems.put(id, interval_name);
			}
			configurationBean.setIntervalSelectItems(intervalSelectItems);
			configurationBean.setIntervalList(temp_interva_lList);
			validator.validate(configurationBean, result);
			if (!(result.hasErrors())) {
				if (!configurationService.findSimilarBeans(configurationBean)) {
					configurationService
							.saveConfigurationBean(configurationBean);
					model.addAttribute("msg_succcess",
							"Configuration enregistrée.");
					List<ConfigurationBean> configurationBeanList = configurationService
							.getConfigurationsList();
					model.addAttribute("configurationList", configurationBeanList);
					return "configuration_list";
				} else {
					model.addAttribute("msg_error",
							"Une configuration avec le même nom existe déjà.");
				}
			}
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		return "configuration_add";
	}

	@RequestMapping(value = "ajax/add_interval", method = RequestMethod.GET)
	public void addInterval(@RequestParam("start") Integer start,
			@RequestParam("end") Integer end, HttpServletResponse response)
			throws IOException {
		id_interval++;
		IntervalSelectionBean interval = new IntervalSelectionBean(id_interval,
				start, end);
		temp_interva_lList.add(interval);
		response.setContentType("text/plain");
		PrintWriter writer = response.getWriter();
		Gson gson = new Gson();
		String json = gson.toJson(temp_interva_lList);
		writer.write(json);
		writer.close();
	}

	private IntervalSelectionBean getIntervalById(Integer id) {
		IntervalSelectionBean intervalSelectionBean = null;
		for (IntervalSelectionBean interval : temp_interva_lList) {
			if (interval.getId() == id)
				intervalSelectionBean = interval;
		}
		return intervalSelectionBean;
	}

	@RequestMapping(value = "ajax/delete_interval", method = RequestMethod.GET)
	public void deleteInterval(@RequestParam("id") Integer id,
			HttpServletResponse response) throws IOException {
		IntervalSelectionBean interval = getIntervalById(id);
		temp_interva_lList.remove(interval);
		response.setContentType("text/plain");
		PrintWriter writer = response.getWriter();
		Gson gson = new Gson();
		String json = gson.toJson(true);
		writer.write(json);
		writer.close();
	}

	@RequestMapping(value = "/configuration_list", method = RequestMethod.GET)
	public String showList(ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			List<ConfigurationBean> configurationBeanList = configurationService
					.getConfigurationsList();
			model.addAttribute("configurationList", configurationBeanList);
		} catch (BusinessLayerException e) {
			String msg_error = "Une erreur s'est produite lors du traitement de votre demande.";
			model.addAttribute("msg_error", msg_error);
		}
		return "configuration_list";
	}

	@RequestMapping(value = "/config_delete", method = RequestMethod.GET)
	public String deleteConnection(@RequestParam("id") Integer id,
			ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			configurationService.deleteById(id);
			model.addAttribute("msg_succcess", "Suppression avec succès");
		} catch (Exception e) {
			model.addAttribute("msg_error",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		try {
			List<ConfigurationBean> configurationBeanList = configurationService
					.getConfigurationsList();
			model.addAttribute("configurationList", configurationBeanList);
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		return "configuration_list";
	}

	@RequestMapping(value = "/config_edit", method = RequestMethod.GET)
	public String editConfiguration(@RequestParam("id") Integer id,
			ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			ConfigurationBean configurationBean = configurationService
					.getById(id);
			configurationService.prepare(configurationBean);
			temp_interva_lList = new ArrayList<IntervalSelectionBean>();
			if (configurationBean.getIntervalList() != null) {
				id_interval = 0;
				String interval_name;
				for (IntervalSelectionBean intervalBean : configurationBean
						.getIntervalList()) {
					id_interval++;
					Integer start = intervalBean.getStart_line_nb();
					Integer end = intervalBean.getEnd_line_nb();
					IntervalSelectionBean interval = new IntervalSelectionBean(
							id_interval, start, end);
					temp_interva_lList.add(interval);
				}
				Map<Integer, String> intervalSelectItems = new LinkedHashMap<Integer, String>();
				for (IntervalSelectionBean intervalSelectionBean : temp_interva_lList) {
					id = intervalSelectionBean.getId();
					interval_name = intervalSelectionBean.getStart_line_nb()
							+ " - " + intervalSelectionBean.getEnd_line_nb();
					intervalSelectItems.put(id, interval_name);
				}
				configurationBean.setIntervalSelectItems(intervalSelectItems);
			}
			model.addAttribute("configurationBean", configurationBean);
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		return "configuration_edit";
	}

	@RequestMapping(value = "/config_edit", method = RequestMethod.POST)
	public String update(
			@ModelAttribute(value = "configurationBean") @Valid ConfigurationBean configurationBean,
			BindingResult result, ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		if (!result.hasErrors()) {
			try {
				configurationBean.setIntervalList(temp_interva_lList);
				Integer id;
				String interval_name;
				Map<Integer, String> intervalSelectItems = new LinkedHashMap<Integer, String>();
				for (IntervalSelectionBean intervalSelectionBean : temp_interva_lList) {
					id = intervalSelectionBean.getId();
					interval_name = intervalSelectionBean.getStart_line_nb()
							+ " - " + intervalSelectionBean.getEnd_line_nb();
					intervalSelectItems.put(id, interval_name);
				}
				configurationBean.setIntervalSelectItems(intervalSelectItems);
				configurationService.update(configurationBean);
				configurationService.prepare(configurationBean);
				model.addAttribute("configurationBean", configurationBean);
				model.addAttribute("msg_succcess", "Modification avec succès");
				List<ConfigurationBean> configurationBeanList = configurationService
						.getConfigurationsList();
				model.addAttribute("configurationList", configurationBeanList);
			} catch (BusinessLayerException e) {
				model.addAttribute("msg_error",
						"Une erreur s'est produite lors du traitement de votre demande.");
				return "configuration_edit";
			}
		}
		return "configuration_list";
	}

	@RequestMapping(value = "/config_details", method = RequestMethod.GET)
	public String detailConfiguration(@RequestParam("id") Integer id,
			ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			ConfigurationBean configurationBean = configurationService
					.getById(id);
			configurationService.prepare(configurationBean);
			temp_interva_lList = new ArrayList<IntervalSelectionBean>();
			model.addAttribute("configurationBean", configurationBean);
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		return "configuration_details";
	}
}
