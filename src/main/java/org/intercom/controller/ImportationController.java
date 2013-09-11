package org.intercom.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

import org.intercom.model.ColumnBean;
import org.intercom.model.ConnectionBean;
import org.intercom.model.DataStructureBean;
import org.intercom.model.FTPConnectionBean;
import org.intercom.model.ImportationBean;
import org.intercom.service.ConfigurationService;
import org.intercom.service.DBconnectService;
import org.intercom.service.DataStructureService;
import org.intercom.service.FTPConnectionService;
import org.intercom.service.ImportationService;
import org.intercom.service.LocalUploadService;
import org.intercom.service.exceptions.BusinessLayerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("importationBean")
public class ImportationController {
	@Autowired
	private ImportationService importationService;
	@Autowired
	private DBconnectService dbConnectService;
	@Autowired
	private ConfigurationService configurationService;
	@Autowired
	private DataStructureService dataStructureService;
	@Autowired
	LocalUploadService localUploadService;
	@Autowired
	private FTPConnectionService ftpConnectionService;

	private static Comparator<ColumnBean> COMPARATOR = new Comparator<ColumnBean>() {
		public int compare(ColumnBean column_1, ColumnBean column_2) {
			return column_1.getOrder() - column_2.getOrder();
		}
	};

	@RequestMapping(value = "/importation", method = RequestMethod.GET)
	public String goImportation(ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			ImportationBean importationBean = new ImportationBean();
			importationBean.setDatabaseConnectionList(dbConnectService
					.getDBConnectionList());
			importationBean.setConfigurationList(configurationService
					.getConfigurationSelectionList());
			importationBean.setDataStructureList(dataStructureService
					.getDataStructureSelectionList());
			importationBean.setLocalfilesList(localUploadService
					.getLocalFilesList());
			importationBean.setFtpConnectionsList(ftpConnectionService
					.getSelectList());
			importationBean.setUse_local_files(true);
			model.addAttribute("importationBean", importationBean);
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		return "importation";
	}

	@RequestMapping(value = "/importation", method = RequestMethod.POST)
	public String backToImportation(
			ModelMap model,
			@ModelAttribute(value = "importationBean") ImportationBean importationBean,
			Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		model.addAttribute("importationBean", importationBean);
		return "importation";
	}

	@RequestMapping(value = "/import_connect_ftp", method = RequestMethod.POST)
	public String connectFTP(
			@ModelAttribute(value = "importationBean") ImportationBean importationBean,
			BindingResult result, ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			Integer connection_id = importationBean.getSelectedFTPConnection();
			FTPConnectionBean ftpConnectionBean = ftpConnectionService
					.getById(connection_id);
			if (ftpConnectionService.testFTPConnection(ftpConnectionBean)) {
				importationBean.setFtpFilesList(ftpConnectionService
						.getFTPFilesList(ftpConnectionBean));
			} else {
				importationBean
						.setFtpFilesList(new LinkedHashMap<Integer, String>());
				model.addAttribute("msg_error_ftp",
						"Problème de connexion au serveur FTP.");
			}
		} catch (BusinessLayerException e1) {
			importationBean
					.setFtpFilesList(new LinkedHashMap<Integer, String>());
			model.addAttribute("msg_error",
					"Problème de connexion au serveur FTP.");
		}
		model.addAttribute("importationBean", importationBean);
		return "importation";
	}

	@RequestMapping(value = "/import_connect_db", method = RequestMethod.POST)
	public String connectDatabase(
			@ModelAttribute(value = "importationBean") ImportationBean importationBean,
			BindingResult result, ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			Integer db_connection_id = importationBean
					.getSelectedDBConnection();
			ConnectionBean connectionBean = dbConnectService
					.getById(db_connection_id);
			if (!dbConnectService.connectToDB(connectionBean)) {
				importationBean
						.setDatabaseTablesList(new LinkedHashMap<Integer, String>());
				model.addAttribute("msg_error",
						"La connexion à la base de données a échoué.");
			} else {
				importationBean.setDatabaseTablesList(dbConnectService
						.getTablesFromDatabase(connectionBean));
			}
		} catch (BusinessLayerException e1) {
			importationBean
					.setDatabaseTablesList(new LinkedHashMap<Integer, String>());
			model.addAttribute("msg_error",
					"Problème de connexion au serveur FTP");
		}
		model.addAttribute("ImportationBean", importationBean);
		return "importation";
	}

	@RequestMapping(value = "/import_start_mapping", method = RequestMethod.POST)
	public String goMapping(
			@ModelAttribute(value = "importationBean") ImportationBean importationBean,
			BindingResult result, ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			Integer db_connection_id = importationBean
					.getSelectedDBConnection();
			Integer ftp_connection_id = importationBean
					.getSelectedFTPConnection();
			Integer data_structure_id = importationBean
					.getSelectedDataStructure();
			Integer selected_local_file = importationBean
					.getSelectedLocalFile();
			Integer selectedDatabaseTable = importationBean
					.getSelectedDatabaseTable();
			Integer selected_ftp_file = importationBean.getSelectedFTPFile();
			Integer selected_table = importationBean.getSelectedDatabaseTable();

			boolean use_local_files = importationBean.isUse_local_files();
			boolean use_ftp_files = importationBean.isUse_ftp_files();
			// verify submitted data
			ConnectionBean connectionBean = dbConnectService
					.getById(db_connection_id);
			if (!dbConnectService.connectToDB(connectionBean)) {
				model.addAttribute("msg_error",
						"La connexion à la base de données a échoué.");
				model.addAttribute("importationBean", importationBean);
				return "importation";
			}
			if (selectedDatabaseTable == null) {
				model.addAttribute("msg_error",
						"Aucun table de base de données n'est sélectionnée.");
				model.addAttribute("importationBean", importationBean);
				return "importation";
			}
			if (use_local_files) {
				if (selected_local_file == null) {
					model.addAttribute("msg_error",
							"Aucun fichier n'est sélectionné.");
					model.addAttribute("importationBean", importationBean);
					return "importation";
				}
			}
			if (use_ftp_files) {
				FTPConnectionBean ftpConnectionBean = ftpConnectionService
						.getById(ftp_connection_id);
				boolean connectedToFTP = false;
				try {
					connectedToFTP = ftpConnectionService
							.testFTPConnection(ftpConnectionBean);
				} catch (BusinessLayerException e) {
					model.addAttribute("msg_error",
							"La connexion au serveur FTP a échoué.");
					model.addAttribute("importationBean", importationBean);
					return "importation";
				}
				if (!connectedToFTP) {
					model.addAttribute("msg_error",
							"La connexion au serveur FTP a échoué.");
					model.addAttribute("importationBean", importationBean);
					return "importation";
				} else {
					if (selected_ftp_file == null) {
						model.addAttribute("msg_error",
								"Aucun fichier n'est sélectionné.");
						model.addAttribute("importationBean", importationBean);
						return "importation";
					}
				}
			}
			DataStructureBean dataStructure = dataStructureService
					.getById(data_structure_id);
			List<ColumnBean> columnsBeanList = dataStructure.getColumnList();
			Collections.sort(columnsBeanList, COMPARATOR);
			importationBean.setColumnsList(columnsBeanList);
			String table = importationBean.getDatabaseTablesList().get(
					selected_table);
			importationBean.setDatabaseTableColumnsList(dbConnectService
					.getColumnsFromDatabaseTable(connectionBean, table));
			model.addAttribute("importationBean", importationBean);
			return "mapping";
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error",
					"Une erreur s'est produite lors du traitement de votre demande.");
			model.addAttribute("importationBean", importationBean);
			return "importation";
		}
	}

	@RequestMapping(value = "/begin_importation", method = RequestMethod.POST)
	public String beginImportationProcess(
			@ModelAttribute(value = "importationBean") ImportationBean importationBean,
			BindingResult result, ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);

		if (importationService.mappingErrors(importationBean)) {
			model.addAttribute(
					"msg_error",
					"Une colonne de la table de la base doit être mappé à une seule colonne du fichier CSV.");
			model.addAttribute("importationBean", importationBean);
			return "mapping";
		}
		try {
			importationService.importToDatabaseTable(importationBean);
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error", e.getMessage());
			model.addAttribute("importationBean", importationBean);
			return "mapping";
		}
		return "importation_success";
	}
}
