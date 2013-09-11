package org.intercom.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.validation.Valid;

import org.intercom.dao.ParametresDao;
import org.intercom.model.ColumnBean;
import org.intercom.model.DataStructureBean;
import org.intercom.service.DataStructureService;
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
public class DataStructureController {

	@Autowired
	private DataStructureService dataStructureService;

	@Autowired
	ParametresDao parametresDao;

	private DataStructureBean tempDataStructure;
	private List<ColumnBean> tempColumnsBeanList;

	private static Comparator<ColumnBean> COMPARATOR = new Comparator<ColumnBean>() {
		public int compare(ColumnBean column_1, ColumnBean column_2) {
			return column_1.getOrder() - column_2.getOrder();
		}
	};

	private ColumnBean getColumnByOrder(Integer order) {
		ColumnBean columnBean = null;
		for (ColumnBean column : tempColumnsBeanList) {
			if (column.getOrder() == order)
				columnBean = column;
		}
		return columnBean;
	}

	@RequestMapping(value = "/data_structure_list", method = RequestMethod.GET)
	public String showDataStructureList(ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			List<DataStructureBean> DataStructureBeanList = dataStructureService
					.getDataStructureList();
			model.addAttribute("DataStructureList", DataStructureBeanList);
		} catch (BusinessLayerException e) {
			String msg_error = "Une erreur s'est produite lors du traitement de votre demande.";
			model.addAttribute("msg_error", msg_error);
		}
		return "data_structure_list";
	}

	@RequestMapping(value = "/data_structure_details", method = RequestMethod.GET)
	public String goDetailsDataStructure(@RequestParam("id") Integer id,
			ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			DataStructureBean dataStructure = dataStructureService.getById(id);
			List<ColumnBean> columnsBeanList = dataStructure.getColumnList();
			Collections.sort(columnsBeanList, COMPARATOR);
			model.addAttribute("DataStructureBean", dataStructure);
			model.addAttribute("ColumnsList", columnsBeanList);
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		return "data_structure_details";
	}

	@RequestMapping(value = "/data_structure_delete", method = RequestMethod.GET)
	public String deleteConnection(@RequestParam("id") Integer id,
			ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			dataStructureService.deleteById(id);
			model.addAttribute("msg_succcess", "Suppression avec succès");
		} catch (Exception e) {
			model.addAttribute("msg_error",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		try {
			List<DataStructureBean> DataStructureBeanList = dataStructureService
					.getDataStructureList();
			model.addAttribute("DataStructureList", DataStructureBeanList);
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		return "data_structure_list";
	}

	@RequestMapping(value = "/data_structure_edit", method = RequestMethod.GET)
	public String goEditDataStructure(@RequestParam("id") Integer id,
			ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			DataStructureBean dataStructureBean = dataStructureService
					.getById(id);
			tempDataStructure = new DataStructureBean();
			tempDataStructure.setId(id);
			tempDataStructure.setName(dataStructureBean.getName());
			tempColumnsBeanList = dataStructureBean.getColumnList();
			Collections.sort(tempColumnsBeanList, COMPARATOR);
			model.addAttribute("DataStructureBean", dataStructureBean);
			model.addAttribute("ColumnsList", tempColumnsBeanList);
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		return "data_structure_edit";
	}

	@RequestMapping(value = "/data_structure_edit", method = RequestMethod.POST)
	public String update(
			@ModelAttribute(value = "DataStructureBean") @Valid DataStructureBean dataStructureBean,
			BindingResult result, ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		if (!result.hasErrors()) {
			try {
				dataStructureBean.setId(tempDataStructure.getId());
				dataStructureBean.setColumnList(tempColumnsBeanList);
				dataStructureService.updateDataStructureBean(dataStructureBean);
				List<DataStructureBean> DataStructureBeanList = dataStructureService
						.getDataStructureList();
				model.addAttribute("DataStructureList", DataStructureBeanList);
				model.addAttribute("msg_succcess", "Modification avec succès");
			} catch (BusinessLayerException e) {
				model.addAttribute("msg_error",
						"Une erreur s'est produite lors du traitement de votre demande.");
			}
		}
		return "data_structure_list";
	}

	@RequestMapping(value = "/column_add", method = RequestMethod.GET)
	public String addColumn(ModelMap model, Principal principal) {
		try {
			String name = principal.getName();
			model.addAttribute("username", name);
			ColumnBean columnBean = new ColumnBean();
			dataStructureService.prepareColumnBean(columnBean);
			model.addAttribute("ColumnBean", columnBean);
			model.addAttribute("actionUrl", "done_add");
			return "column_add";
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error",
					"Une erreur s'est produite lors du traitement de votre demande.");
			return "data_structure_list";
		}
	}

	@RequestMapping(value = "/done_add", method = RequestMethod.POST)
	public String processAddColumn(
			@ModelAttribute(value = "ColumnBean") @Valid ColumnBean columnBean,
			BindingResult result, ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			if (!(result.hasErrors())) {
				dataStructureService.prepareColumnBean(columnBean);
				Integer order = columnBean.getOrder();
				String edit_url = "column_edit?order=" + order;
				String delete_url = "column_delete?order=" + order;
				columnBean.setEdit_url(edit_url);
				columnBean.setDelete_url(delete_url);
				tempColumnsBeanList.add(columnBean);
				Collections.sort(tempColumnsBeanList, COMPARATOR);
				model.addAttribute("ColumnsList", tempColumnsBeanList);
				model.addAttribute("DataStructureBean", tempDataStructure);
				model.addAttribute("msg_succcess", "Colonne enregistrée.");
				return "data_structure_edit";
			}
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		return "column_add";
	}

	@RequestMapping(value = "/column_edit", method = RequestMethod.GET)
	public String editColumn(@RequestParam("order") Integer order,
			ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			ColumnBean columnBean = getColumnByOrder(order);
			tempColumnsBeanList.remove(columnBean);
			dataStructureService.prepareColumnBean(columnBean);
			model.addAttribute("ColumnBean", columnBean);
			model.addAttribute("actionUrl", "done_edit");
			return "column_edit";
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error",
					"Une erreur s'est produite lors du traitement de votre demande.");
			return "data_structure_list";
		}
	}

	@RequestMapping(value = "/done_edit", method = RequestMethod.POST)
	public String processEditColumn(
			@ModelAttribute(value = "ColumnBean") @Valid ColumnBean columnBean,
			BindingResult result, ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			if (!(result.hasErrors())) {
				dataStructureService.prepareColumnBean(columnBean);
				Integer order = columnBean.getOrder();
				String edit_url = "column_edit?order=" + order;
				String delete_url = "column_delete?order=" + order;
				columnBean.setEdit_url(edit_url);
				columnBean.setDelete_url(delete_url);
				tempColumnsBeanList.add(columnBean);
				Collections.sort(tempColumnsBeanList, COMPARATOR);
				model.addAttribute("ColumnsList", tempColumnsBeanList);
				model.addAttribute("DataStructureBean", tempDataStructure);
				model.addAttribute("msg_succcess", "Colonne enregistrée.");
				return "data_structure_edit";
			}
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		return "column_edit";
	}

	@RequestMapping(value = "/column_delete", method = RequestMethod.GET)
	public String deleteColumn(@RequestParam("order") Integer order,
			ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			ColumnBean column = getColumnByOrder(order);
			tempColumnsBeanList.remove(column);
			model.addAttribute("ColumnsList", tempColumnsBeanList);
			model.addAttribute("msg_succcess", "Colonne supprimée.");
			model.addAttribute("DataStructureBean", tempDataStructure);
		} catch (Exception e) {
			model.addAttribute("msg_error",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		return "data_structure_edit";
	}
}
