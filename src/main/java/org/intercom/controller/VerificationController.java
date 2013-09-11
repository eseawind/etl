package org.intercom.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

import org.intercom.model.ColumnBean;
import org.intercom.model.DataStructureBean;
import org.intercom.model.ErrorBean;
import org.intercom.model.FTPConnectionBean;
import org.intercom.model.VerificationBean;
import org.intercom.service.ConfigurationService;
import org.intercom.service.DataStructureService;
import org.intercom.service.FTPConnectionService;
import org.intercom.service.LocalUploadService;
import org.intercom.service.VerificationService;
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
@SessionAttributes("verificationBean")
public class VerificationController {

	@Autowired
	private VerificationService verificationService;
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

	@RequestMapping(value = "/verification", method = RequestMethod.GET)
	public String goImportation(ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			VerificationBean verificationBean = new VerificationBean();
			verificationBean.setConfigurationList(configurationService
					.getConfigurationSelectionList());
			verificationBean.setDataStructureList(dataStructureService
					.getDataStructureSelectionList());
			verificationBean.setLocalfilesList(localUploadService
					.getLocalFilesList());
			verificationBean.setFtpConnectionsList(ftpConnectionService
					.getSelectList());
			verificationBean.setUse_local_files(true);
			model.addAttribute("verificationBean", verificationBean);
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		return "verification";
	}

	@RequestMapping(value = "/verification_connect_ftp", method = RequestMethod.POST)
	public String connectFTP(
			@ModelAttribute(value = "verificationBean") VerificationBean verificationBean,
			BindingResult result, ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			Integer connection_id = verificationBean.getSelectedFTPConnection();
			FTPConnectionBean ftpConnectionBean = ftpConnectionService
					.getById(connection_id);
			if (ftpConnectionService.testFTPConnection(ftpConnectionBean)) {
				verificationBean.setFtpFilesList(ftpConnectionService
						.getFTPFilesList(ftpConnectionBean));
			} else {
				verificationBean
						.setFtpFilesList(new LinkedHashMap<Integer, String>());
				model.addAttribute("msg_error_ftp",
						"Problème de connexion au serveur FTP.");
			}
		} catch (BusinessLayerException e1) {
			verificationBean
					.setFtpFilesList(new LinkedHashMap<Integer, String>());
			model.addAttribute("msg_error",
					"Problème de connexion au serveur FTP.");
		}
		model.addAttribute("verificationBean", verificationBean);
		return "verification";
	}

	@RequestMapping(value = "/verfication_detect_errors", method = RequestMethod.POST)
	public String errorDetection(
			ModelMap model,
			@ModelAttribute(value = "verificationBean") VerificationBean verificationBean,
			Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		try {
			Integer data_structure_id = verificationBean
					.getSelectedDataStructure();
			DataStructureBean dataStructure = dataStructureService
					.getById(data_structure_id);
			List<ColumnBean> columnsBeanList = dataStructure.getColumnList();
			Collections.sort(columnsBeanList, COMPARATOR);
			verificationBean.setColumnsList(columnsBeanList);
			List<ErrorBean> errorList = verificationService.detectErros(verificationBean);
			
			List<ErrorBean> primatyKeyNullErrors = verificationService.getPrimaryKeyNullErrors(errorList);
			verificationBean.setPrimatyKeyNullErrors(primatyKeyNullErrors);
			Integer nb_primary_key_errors = primatyKeyNullErrors.size();
			model.addAttribute("nb_primary_key_errors", nb_primary_key_errors);

			List<ErrorBean> pkRedundancyErrors = verificationService.getPKRedundancyErrors(errorList);
			verificationBean.setPkRedundancyErrors(pkRedundancyErrors);
			Integer nb_pk_redanduncy_errors = pkRedundancyErrors.size();
			model.addAttribute("nb_pk_redanduncy_errors", nb_pk_redanduncy_errors);

			List<ErrorBean> typeMismatchErrors = verificationService.getTypeMismatchErrors(errorList);
			verificationBean.setTypeMismatchErrors(typeMismatchErrors);
			Integer nb_type_mismatch_errors = typeMismatchErrors.size();
			model.addAttribute("nb_type_mismatch_errors", nb_type_mismatch_errors);

			List<ErrorBean> maximumLenghtErrors = verificationService.getMaxLenghtErrors(errorList);
			verificationBean.setMaximumLenghtErrors(maximumLenghtErrors);
			Integer nb_max_lenght_errors = maximumLenghtErrors.size();
			model.addAttribute("nb_max_lenght_errors", nb_max_lenght_errors);

			List<ErrorBean> outOfIntervalErrors = verificationService.getOutofIntervalErrors(errorList);
			verificationBean.setOutOfIntervalErrors(outOfIntervalErrors);
			Integer nb_out_of_interval_errors = outOfIntervalErrors.size();
			model.addAttribute("nb_out_of_interval_errors", nb_out_of_interval_errors);
			
			verificationBean.setErrorList(errorList);
			model.addAttribute("verificationBean", verificationBean);
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error",
					"Une erreur s'est produite lors du traitement de votre demande.");
			return "verification";
		}
		return "verfication_detect_errors";
	}
}
