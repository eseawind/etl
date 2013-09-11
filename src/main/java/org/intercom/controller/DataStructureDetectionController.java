package org.intercom.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.intercom.model.ColumnBean;
import org.intercom.model.DataStructureBean;
import org.intercom.model.FTPConnectionBean;
import org.intercom.service.ConfigurationService;
import org.intercom.service.DataStructureService;
import org.intercom.service.FTPConnectionService;
import org.intercom.service.LocalUploadService;
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
public class DataStructureDetectionController {

	@Autowired
	private FTPConnectionService ftpConnectionService;
	@Autowired
	private LocalUploadService localUploadService;
	@Autowired
	ConfigurationService configurationService;
	@Autowired
	private DataStructureService dataStructureService;

	private Map<Integer, String> tempLocalFilesList;
	private Map<Integer, String> tempFTPConnectionList;
	private Map<Integer, String> tempFTPFilesList;
	private Map<Integer, String> tempConfigurationsList;

	private DataStructureBean tempDataStructureBean;
	private List<ColumnBean> tempColumnsList;

	private static Comparator<ColumnBean> COMPARATOR = new Comparator<ColumnBean>() {
		public int compare(ColumnBean column_1, ColumnBean column_2) {
			return column_1.getOrder() - column_2.getOrder();
		}
	};

	private void prepareModel(ModelMap model) throws BusinessLayerException {
		try {
			tempLocalFilesList = localUploadService.getLocalFilesList();
			model.addAttribute("filesSelectionList", tempLocalFilesList);
			tempFTPConnectionList = ftpConnectionService.getSelectList();
			model.addAttribute("ftpConnectionList", tempFTPConnectionList);
			tempConfigurationsList = configurationService
					.getConfigurationSelectionList();
			model.addAttribute("ConfigurationsSelectionList",
					tempConfigurationsList);
		} catch (BusinessLayerException e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	@RequestMapping(value = "/data_structure_add", method = RequestMethod.GET)
	public String goAddDataStructure(ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		model.addAttribute("tab_number", 1);
		try {
			tempColumnsList = new ArrayList<ColumnBean>();
			DataStructureBean dataStructure = new DataStructureBean();
			model.addAttribute("DataStructureBean", dataStructure);
			prepareModel(model);
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error_local",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		return "data_structure_add";
	}

	@RequestMapping(value = "/display_local_data_structure", method = RequestMethod.POST)
	public String detectFromLocalFile(
			@ModelAttribute(value = "DataStructureBean") DataStructureBean dataStructure,
			ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		// save options
		tempDataStructureBean = new DataStructureBean();
		tempDataStructureBean.setName(dataStructure.getName());
		tempDataStructureBean.setSelectedconfiguration(dataStructure
				.getSelectedconfiguration());
		tempDataStructureBean.setSelectedLocalFile(dataStructure
				.getSelectedLocalFile());
		try {
			String filename = tempLocalFilesList.get(dataStructure
					.getSelectedLocalFile());
			tempColumnsList = dataStructureService
					.getDataStructureFromLocalFile(filename,
							dataStructure.getSelectedconfiguration());
			Collections.sort(tempColumnsList, COMPARATOR);
			model.addAttribute("LocalColumnsList", tempColumnsList);
			prepareModel(model);
			model.addAttribute("DataStructureBean", dataStructure);
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error_local",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		return "data_structure_add";
	}

	@RequestMapping(value = "/save_local_structure", method = RequestMethod.POST)
	public String addLocalStructure(
			@ModelAttribute(value = "DataStructureBean") @Valid DataStructureBean dataStructureBean,
			BindingResult result, ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		if (!result.hasErrors()) {
			try {
				dataStructureBean.setColumnList(tempColumnsList);
				dataStructureService.saveDataStructure(dataStructureBean);
				List<DataStructureBean> DataStructureBeanList = dataStructureService
						.getDataStructureList();
				model.addAttribute("DataStructureList", DataStructureBeanList);
				model.addAttribute("msg_succcess",
						"Structure de données enregistrée.");
			} catch (BusinessLayerException e) {
				model.addAttribute("msg_error_local",
						"Une erreur s'est produite lors du traitement de votre demande.");
			}
			return "data_structure_list";
		} else {
			model.addAttribute("filesSelectionList", tempLocalFilesList);
			model.addAttribute("ftpConnectionList", tempFTPConnectionList);
			model.addAttribute("ConfigurationsSelectionList",
					tempConfigurationsList);
			model.addAttribute("LocalColumnsList", tempColumnsList);
			model.addAttribute("DataStructureBean", dataStructureBean);
			model.addAttribute("msg_error_local", "Nom invalide.");
			return "data_structure_add";
		}
	}

	private ColumnBean getColumnByOrder(Integer order) {
		ColumnBean columnBean = null;
		for (ColumnBean column : tempColumnsList) {
			if (column.getOrder() == order)
				columnBean = column;
		}
		return columnBean;
	}

	@RequestMapping(value = "/column_local_delete", method = RequestMethod.GET)
	public String deleteColumn(@RequestParam("order") Integer order,
			ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			ColumnBean column = getColumnByOrder(order);
			tempColumnsList.remove(column);
			model.addAttribute("LocalColumnsList", tempColumnsList);
			model.addAttribute("msg_succcess_local", "Colonne supprimée.");
			tempLocalFilesList = localUploadService.getLocalFilesList();
			model.addAttribute("filesSelectionList", tempLocalFilesList);
			model.addAttribute("ConfigurationsSelectionList",
					tempConfigurationsList);
			model.addAttribute("DataStructureBean", tempDataStructureBean);
		} catch (Exception e) {
			model.addAttribute("msg_error_local",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		return "data_structure_add";
	}

	@RequestMapping(value = "/column_local_edit", method = RequestMethod.GET)
	public String editColumn(@RequestParam("order") Integer order,
			ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			ColumnBean columnBean = getColumnByOrder(order);
			tempColumnsList.remove(columnBean);
			dataStructureService.prepareColumnBean(columnBean);
			model.addAttribute("ColumnBean", columnBean);
			model.addAttribute("actionUrl", "done_local_edit");
			return "column_edit";
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error_local",
					"Une erreur s'est produite lors du traitement de votre demande.");
			return "data_structure_add";
		}
	}

	@RequestMapping(value = "/done_local_edit", method = RequestMethod.POST)
	public String processEditColumn(
			@ModelAttribute(value = "ColumnBean") @Valid ColumnBean columnBean,
			BindingResult result, ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			if (!(result.hasErrors())) {
				dataStructureService.prepareColumnBean(columnBean);
				Integer order = columnBean.getOrder();
				String edit_url = "column_local_edit?order=" + order;
				String delete_url = "column_local_delete?order=" + order;
				columnBean.setEdit_url(edit_url);
				columnBean.setDelete_url(delete_url);
				tempColumnsList.add(columnBean);
				Collections.sort(tempColumnsList, COMPARATOR);
				model.addAttribute("LocalColumnsList", tempColumnsList);
				model.addAttribute("filesSelectionList", tempLocalFilesList);
				model.addAttribute("ConfigurationsSelectionList",
						tempConfigurationsList);
				model.addAttribute("DataStructureBean", tempDataStructureBean);
				model.addAttribute("msg_succcess_local", "Colonne enregistrée.");
				return "data_structure_add";
			}
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error_local",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		return "column_local_edit";
	}

	@RequestMapping(value = "/column_local_add", method = RequestMethod.GET)
	public String addColumn(ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			ColumnBean columnBean = new ColumnBean();
			dataStructureService.prepareColumnBean(columnBean);
			model.addAttribute("ColumnBean", columnBean);
			model.addAttribute("actionUrl", "done_local_add");
			return "column_add";
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error_local",
					"Une erreur s'est produite lors du traitement de votre demande.");
			return "column_local_edit";
		}
	}

	@RequestMapping(value = "/done_local_add", method = RequestMethod.POST)
	public String processAddColumn(
			@ModelAttribute(value = "ColumnBean") @Valid ColumnBean columnBean,
			BindingResult result, ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			if (!(result.hasErrors())) {
				dataStructureService.prepareColumnBean(columnBean);
				Integer order = columnBean.getOrder();
				String edit_url = "column_local_edit?order=" + order;
				String delete_url = "column_local_delete?order=" + order;
				columnBean.setEdit_url(edit_url);
				columnBean.setDelete_url(delete_url);
				tempColumnsList.add(columnBean);
				Collections.sort(tempColumnsList, COMPARATOR);
				model.addAttribute("LocalColumnsList", tempColumnsList);
				model.addAttribute("filesSelectionList", tempLocalFilesList);
				if (tempDataStructureBean == null)
					tempDataStructureBean = new DataStructureBean();
				model.addAttribute("ConfigurationsSelectionList",
						tempConfigurationsList);
				model.addAttribute("DataStructureBean", tempDataStructureBean);
				model.addAttribute("msg_succcess_local", "Colonne enregistrée.");
				return "data_structure_add";
			}
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error_local",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		return "column_local_add";
	}

	// ********** CONTROLS THE FTP CONNECTION TAB ***************/
	@RequestMapping(value = "/connect_ftp", method = RequestMethod.POST)
	public String connectFTP(
			@ModelAttribute(value = "DataStructureBean") DataStructureBean dataStructureBean,
			BindingResult result, ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		model.addAttribute("tab_number", 2);
		// save options
		tempDataStructureBean = new DataStructureBean();
		tempDataStructureBean.setName(dataStructureBean.getName());
		tempDataStructureBean.setSelectedconfiguration(dataStructureBean
				.getSelectedconfiguration());
		tempDataStructureBean.setSelectedFTPConnection(dataStructureBean
				.getSelectedFTPConnection());
		tempDataStructureBean.setSelectedFTPFile(dataStructureBean
				.getSelectedFTPFile());
		try {
			prepareModel(model);
		} catch (BusinessLayerException e1) {
			model.addAttribute("msg_error_ftp",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		try {
			Integer connection_id = dataStructureBean
					.getSelectedFTPConnection();
			FTPConnectionBean ftpConnectionBean = ftpConnectionService
					.getById(connection_id);
			if (ftpConnectionService.testFTPConnection(ftpConnectionBean)) {
				tempFTPFilesList = ftpConnectionService
						.getFTPFilesList(ftpConnectionBean);
				model.addAttribute("FTPFilesSelectionList", tempFTPFilesList);
			} else {
				String msg_error = "Problème de connexion au serveur FTP";
				model.addAttribute("msg_error_ftp", msg_error);
			}
			DataStructureBean dataStructure = new DataStructureBean();
			model.addAttribute("DataStructureBean", dataStructure);
		} catch (BusinessLayerException e) {
			String msg_error = "Problème de connexion au serveur FTP";
			model.addAttribute("msg_error_ftp", msg_error);
		}
		model.addAttribute("DataStructureBean", dataStructureBean);
		return "data_structure_add";
	}

	@RequestMapping(value = "/display_ftp_data_structure", method = RequestMethod.POST)
	public String detectFromFTPFile(
			@ModelAttribute(value = "DataStructureBean") DataStructureBean dataStructure,
			ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		model.addAttribute("tab_number", 2);
		model.addAttribute("ftpConnectionList", tempFTPConnectionList);
		model.addAttribute("FTPFilesSelectionList", tempFTPFilesList);
		model.addAttribute("ConfigurationsSelectionList",
				tempConfigurationsList);
		try {
			Integer connection_id = dataStructure.getSelectedFTPConnection();
			FTPConnectionBean ftpConnectionBean = ftpConnectionService
					.getById(connection_id);
			String filename = tempFTPFilesList.get(dataStructure
					.getSelectedFTPFile());
			Integer configuration_id = dataStructure.getSelectedconfiguration();
			tempColumnsList = dataStructureService.getDataStructureFromFTPFile(
					filename, ftpConnectionBean, configuration_id);
			Collections.sort(tempColumnsList, COMPARATOR);
			model.addAttribute("FTPColumnsList", tempColumnsList);
			prepareModel(model);
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error_ftp",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		model.addAttribute("DataStructureBean", dataStructure);
		return "data_structure_add";
	}

	@RequestMapping(value = "/save_ftp_data_structure", method = RequestMethod.POST)
	public String saveFTPDataStructure(
			@ModelAttribute(value = "DataStructureBean") @Valid DataStructureBean dataStructureBean,
			BindingResult result, ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		if (!result.hasErrors()) {
			try {
				dataStructureBean.setColumnList(tempColumnsList);
				dataStructureService.saveDataStructure(dataStructureBean);
				List<DataStructureBean> DataStructureBeanList = dataStructureService
						.getDataStructureList();
				model.addAttribute("DataStructureList", DataStructureBeanList);
				model.addAttribute("msg_succcess",
						"Structure de données enregistrée.");
			} catch (BusinessLayerException e) {
				model.addAttribute("msg_error_local",
						"Une erreur s'est produite lors du traitement de votre demande.");
			}
			return "data_structure_list";
		} else {
			model.addAttribute("tab_number", 2);
			model.addAttribute("ftpConnectionList", tempFTPConnectionList);
			model.addAttribute("FTPFilesSelectionList", tempFTPFilesList);
			model.addAttribute("ConfigurationsSelectionList",
					tempConfigurationsList);
			model.addAttribute("FTPColumnsList", tempColumnsList);
			model.addAttribute("DataStructureBean", dataStructureBean);
			model.addAttribute("msg_error_ftp", "Nom invalide.");
			return "data_structure_add";
		}
	}

	@RequestMapping(value = "/column_ftp_delete", method = RequestMethod.GET)
	public String deleteFTPColumn(@RequestParam("order") Integer order,
			ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			ColumnBean column = getColumnByOrder(order);
			tempColumnsList.remove(column);
			model.addAttribute("msg_succcess_ftp", "Colonne supprimée.");
			model.addAttribute("tab_number", 2);
			model.addAttribute("ftpConnectionList", tempFTPConnectionList);
			model.addAttribute("FTPFilesSelectionList", tempFTPFilesList);
			model.addAttribute("ConfigurationsSelectionList",
					tempConfigurationsList);
			model.addAttribute("FTPColumnsList", tempColumnsList);
			model.addAttribute("DataStructureBean", tempDataStructureBean);
		} catch (Exception e) {
			model.addAttribute("msg_error_ftp",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		return "data_structure_add";
	}

	@RequestMapping(value = "/column_ftp_edit", method = RequestMethod.GET)
	public String editFTPColumn(@RequestParam("order") Integer order,
			ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			ColumnBean columnBean = getColumnByOrder(order);
			tempColumnsList.remove(columnBean);
			dataStructureService.prepareColumnBean(columnBean);
			model.addAttribute("ColumnBean", columnBean);
			model.addAttribute("actionUrl", "done_ftp_edit");
			return "column_edit";
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error_ftp",
					"Une erreur s'est produite lors du traitement de votre demande.");
			model.addAttribute("tab_number", 2);
			model.addAttribute("ftpConnectionList", tempFTPConnectionList);
			model.addAttribute("FTPFilesSelectionList", tempFTPFilesList);
			model.addAttribute("ConfigurationsSelectionList",
					tempConfigurationsList);
			model.addAttribute("FTPColumnsList", tempColumnsList);
			model.addAttribute("DataStructureBean", tempDataStructureBean);
			return "data_structure_add";
		}
	}

	@RequestMapping(value = "/done_ftp_edit", method = RequestMethod.POST)
	public String processEditFTPColumn(
			@ModelAttribute(value = "ColumnBean") @Valid ColumnBean columnBean,
			BindingResult result, ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			if (!(result.hasErrors())) {
				dataStructureService.prepareColumnBean(columnBean);
				Integer order = columnBean.getOrder();
				String edit_url = "column_ftp_edit?order=" + order;
				String delete_url = "column_ftp_delete?order=" + order;
				columnBean.setEdit_url(edit_url);
				columnBean.setDelete_url(delete_url);
				tempColumnsList.add(columnBean);
				Collections.sort(tempColumnsList, COMPARATOR);
				model.addAttribute("tab_number", 2);
				model.addAttribute("ftpConnectionList", tempFTPConnectionList);
				model.addAttribute("FTPFilesSelectionList", tempFTPFilesList);
				model.addAttribute("ConfigurationsSelectionList",
						tempConfigurationsList);
				model.addAttribute("FTPColumnsList", tempColumnsList);
				model.addAttribute("DataStructureBean", tempDataStructureBean);
				model.addAttribute("msg_succcess_ftp", "Colonne enregistrée.");
				return "data_structure_add";
			}
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error_ftp",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		return "column_ftp_edit";
	}

	@RequestMapping(value = "/column_ftp_add", method = RequestMethod.GET)
	public String addFTPColumn(ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			ColumnBean columnBean = new ColumnBean();
			dataStructureService.prepareColumnBean(columnBean);
			model.addAttribute("ColumnBean", columnBean);
			model.addAttribute("actionUrl", "done_ftp_add");
			return "column_add";
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error_ftp",
					"Une erreur s'est produite lors du traitement de votre demande.");
			model.addAttribute("tab_number", 2);
			model.addAttribute("ftpConnectionList", tempFTPConnectionList);
			model.addAttribute("FTPFilesSelectionList", tempFTPFilesList);
			model.addAttribute("ConfigurationsSelectionList",
					tempConfigurationsList);
			model.addAttribute("FTPColumnsList", tempColumnsList);
			model.addAttribute("DataStructureBean", tempDataStructureBean);
			return "data_structure_add";
		}
	}

	@RequestMapping(value = "/done_ftp_add", method = RequestMethod.POST)
	public String processAddFTPColumn(
			@ModelAttribute(value = "ColumnBean") @Valid ColumnBean columnBean,
			BindingResult result, ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			if (!(result.hasErrors())) {
				dataStructureService.prepareColumnBean(columnBean);
				Integer order = columnBean.getOrder();
				String edit_url = "column_ftp_edit?order=" + order;
				String delete_url = "column_ftp_delete?order=" + order;
				columnBean.setEdit_url(edit_url);
				columnBean.setDelete_url(delete_url);
				tempColumnsList.add(columnBean);
				Collections.sort(tempColumnsList, COMPARATOR);
				model.addAttribute("tab_number", 2);
				model.addAttribute("ftpConnectionList", tempFTPConnectionList);
				model.addAttribute("FTPFilesSelectionList", tempFTPFilesList);
				model.addAttribute("ConfigurationsSelectionList",
						tempConfigurationsList);
				model.addAttribute("FTPColumnsList", tempColumnsList);
				if (tempDataStructureBean == null)
					tempDataStructureBean = new DataStructureBean();
				model.addAttribute("DataStructureBean", tempDataStructureBean);
				model.addAttribute("msg_succcess_ftp", "Colonne enregistrée.");
				return "data_structure_add";
			}
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error_ftp",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		return "column_ftp_add";
	}
	
	// ********** CONTROLS THE MANUAL CONNECTION TAB ***************/

	@RequestMapping(value = "/save_manual_data_structure", method = RequestMethod.POST)
	public String savemanualDataStructure(
			@ModelAttribute(value = "DataStructureBean") @Valid DataStructureBean dataStructureBean,
			BindingResult result, ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		if (!result.hasErrors()) {
			try {
				dataStructureBean.setColumnList(tempColumnsList);
				dataStructureService.saveDataStructure(dataStructureBean);
				List<DataStructureBean> DataStructureBeanList = dataStructureService
						.getDataStructureList();
				model.addAttribute("DataStructureList", DataStructureBeanList);
				model.addAttribute("msg_succcess",
						"Structure de données enregistrée.");
			} catch (BusinessLayerException e) {
				model.addAttribute("msg_error_manual",
						"Une erreur s'est produite lors du traitement de votre demande.");
			}
			return "data_structure_list";
		} else {
			model.addAttribute("tab_number", 3);
			model.addAttribute("ManualColumnsList", tempColumnsList);
			model.addAttribute("DataStructureBean", dataStructureBean);
			model.addAttribute("msg_error_manual", "Nom invalide.");
			return "data_structure_add";
		}
	}
	
	@RequestMapping(value = "/column_manual_add", method = RequestMethod.GET)
	public String addLocalColumn(ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			ColumnBean columnBean = new ColumnBean();
			dataStructureService.prepareColumnBean(columnBean);
			model.addAttribute("ColumnBean", columnBean);
			model.addAttribute("actionUrl", "done_manual_add");
			return "column_add";
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error_manual",
					"Une erreur s'est produite lors du traitement de votre demande.");
			model.addAttribute("tab_number", 3);
			model.addAttribute("ManualColumnsList", tempColumnsList);
			model.addAttribute("DataStructureBean", tempDataStructureBean);
			return "data_structure_add";
		}
	}

	@RequestMapping(value = "/done_manual_add", method = RequestMethod.POST)
	public String processAddManualColumn(
			@ModelAttribute(value = "ColumnBean") @Valid ColumnBean columnBean,
			BindingResult result, ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			if (!(result.hasErrors())) {
				dataStructureService.prepareColumnBean(columnBean);
				Integer order = columnBean.getOrder();
				String edit_url = "column_manual_edit?order=" + order;
				String delete_url = "column_manual_delete?order=" + order;
				columnBean.setEdit_url(edit_url);
				columnBean.setDelete_url(delete_url);
				tempColumnsList.add(columnBean);
				Collections.sort(tempColumnsList, COMPARATOR);
				model.addAttribute("tab_number", 3);
				model.addAttribute("ManualColumnsList", tempColumnsList);
				if (tempDataStructureBean == null)
					tempDataStructureBean = new DataStructureBean();
				model.addAttribute("DataStructureBean", tempDataStructureBean);
				model.addAttribute("msg_succcess_manual", "Colonne enregistrée.");
				return "data_structure_add";
			}
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error_manual",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		return "column_manual_add";
	}
	
	@RequestMapping(value = "/column_manual_edit", method = RequestMethod.GET)
	public String editManualColumn(@RequestParam("order") Integer order,
			ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			ColumnBean columnBean = getColumnByOrder(order);
			tempColumnsList.remove(columnBean);
			dataStructureService.prepareColumnBean(columnBean);
			model.addAttribute("ColumnBean", columnBean);
			model.addAttribute("actionUrl", "done_manual_edit");
			return "column_edit";
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error_manual",
					"Une erreur s'est produite lors du traitement de votre demande.");
			model.addAttribute("tab_number", 3);
			model.addAttribute("ManualColumnsList", tempColumnsList);
			model.addAttribute("DataStructureBean", tempDataStructureBean);
			return "data_structure_add";
		}
	}

	@RequestMapping(value = "/done_manual_edit", method = RequestMethod.POST)
	public String processEditManualColumn(
			@ModelAttribute(value = "ColumnBean") @Valid ColumnBean columnBean,
			BindingResult result, ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			if (!(result.hasErrors())) {
				dataStructureService.prepareColumnBean(columnBean);
				Integer order = columnBean.getOrder();
				String edit_url = "column_manual_edit?order=" + order;
				String delete_url = "column_manual_delete?order=" + order;
				columnBean.setEdit_url(edit_url);
				columnBean.setDelete_url(delete_url);
				tempColumnsList.add(columnBean);
				Collections.sort(tempColumnsList, COMPARATOR);
				model.addAttribute("tab_number", 3);
				model.addAttribute("ManualColumnsList", tempColumnsList);
				model.addAttribute("DataStructureBean", tempDataStructureBean);
				model.addAttribute("msg_succcess_manual", "Colonne enregistrée.");
				return "data_structure_add";
			}
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error_manual",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		return "column_manual_edit";
	}
	
	@RequestMapping(value = "/column_manual_delete", method = RequestMethod.GET)
	public String deleteManualColumn(@RequestParam("order") Integer order,
			ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			ColumnBean column = getColumnByOrder(order);
			tempColumnsList.remove(column);
			model.addAttribute("msg_succcess_manual", "Colonne supprimée.");
			model.addAttribute("tab_number", 3);
			model.addAttribute("ManualColumnsList", tempColumnsList);
			model.addAttribute("DataStructureBean", tempDataStructureBean);
		} catch (Exception e) {
			model.addAttribute("msg_error_manual",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		return "data_structure_add";
	}
}
