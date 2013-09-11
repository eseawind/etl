package org.intercom.controller;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.intercom.model.FTPConnectionBean;
import org.intercom.model.FileDetails;
import org.intercom.model.FileUploadBean;
import org.intercom.model.UplaodFTPBean;
import org.intercom.service.FTPConnectionService;
import org.intercom.service.FTPuploadService;
import org.intercom.service.exceptions.BusinessLayerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.google.gson.Gson;

@Controller
public class FTPUploadController {

	@Autowired
	private FTPuploadService uploadService;

	@Autowired
	private FTPConnectionService ftpConnectionService;

	@Autowired
	private ServletContext servletContext = null;

	private Boolean isConnectedToFTP = false;
	private FTPConnectionBean ftpConnection = null;

	@RequestMapping(value = "/upload_ftp", method = RequestMethod.GET)
	public void showForm(ModelMap model, Principal principal)
			throws IOException {
		String name = principal.getName();
		model.addAttribute("username", name);
		FileUploadBean uploadBean = new FileUploadBean();
		model.addAttribute("FileUploadBean", uploadBean);
		UplaodFTPBean uploadFTPBean = new UplaodFTPBean();
		model.addAttribute("UplaodFTPBean", uploadFTPBean);
		isConnectedToFTP = false;
		model.addAttribute("isConnectedToFTP", isConnectedToFTP);
		Map<Integer, String> ftpsSelectList = new LinkedHashMap<Integer, String>();
		try {
			ftpsSelectList = ftpConnectionService.getSelectList();
		} catch (BusinessLayerException e) {
			model.addAttribute("msg_error",
					"Une erreur s'est produite lors du traitement de votre demande.");
		}
		model.addAttribute("ftpConnectionList", ftpsSelectList);
	}

	@RequestMapping(value = "/upload_ftp", method = RequestMethod.POST)
	public void connectFTP(
			@ModelAttribute(value = "UplaodFTPBean") UplaodFTPBean uploadFtpBean,
			BindingResult result, ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		FileUploadBean uploadBean = new FileUploadBean();
		model.addAttribute("FileUploadBean", uploadBean);
		Map<Integer, String> ftpsSelectList = new LinkedHashMap<Integer, String>();
		try {
			ftpsSelectList = ftpConnectionService.getSelectList();
		} catch (BusinessLayerException e) {
			String msg_error = "Erreur à la réucupération de la liste des connexions FTP";
			model.addAttribute("msg_error", msg_error);
		}
		model.addAttribute("ftpConnectionList", ftpsSelectList);
		try {
			Integer connection_id = uploadFtpBean.getSelectedFTPConnection();
			FTPConnectionBean ftpConnectionBean = ftpConnectionService
					.getById(connection_id);
			if (ftpConnectionService.testFTPConnection(ftpConnectionBean)) {
				isConnectedToFTP = true;
				this.ftpConnection = ftpConnectionBean;
			} else {
				isConnectedToFTP = false;
				String msg_error = "Problème de connexion au serveur FTP";
				model.addAttribute("msg_error", msg_error);
			}
		} catch (BusinessLayerException e) {
			isConnectedToFTP = false;
			String msg_error = "Problème de connexion au serveur FTP";
			model.addAttribute("msg_error", msg_error);
		}
		model.addAttribute("isConnectedToFTP", isConnectedToFTP);
	}

	@RequestMapping(value = "/upload_ftp_form", method = RequestMethod.GET)
	public void showExistingFiles(HttpServletResponse response)
			throws IOException {
		if (isConnectedToFTP) {
			Gson gson = new Gson();
			List<FileDetails> uploadList = new ArrayList<FileDetails>();
			try {
				uploadList = uploadService.getExistingFiles(ftpConnection);
			} catch (BusinessLayerException e) {
				// Do nothing
			}
			response.setContentType("text/plain");
			PrintWriter writer = response.getWriter();
			String json = gson.toJson(uploadList);
			writer.write(json);
			writer.close();
		}
	}

	@RequestMapping(value = "/upload_ftp_form", method = RequestMethod.POST)
	public void processForm(
			@ModelAttribute(value = "FileUploadBean") FileUploadBean uploadBean,
			HttpServletResponse response) throws IOException {
		if (isConnectedToFTP) {
			CommonsMultipartFile uploadedFile = uploadBean.getUploadedFile();
			List<FileDetails> uploadList = new ArrayList<FileDetails>();
			FileItem flie = uploadBean.getUploadedFile().getFileItem();
			String fileName = FilenameUtils.getName(flie.getName());
			long size = uploadBean.getUploadedFile().getSize();
			String contentType = flie.getContentType();
			uploadList = uploadService.getResponse(fileName, size, contentType);
			try {
				uploadService.uploadToFtp(uploadedFile, ftpConnection);
			} catch (BusinessLayerException e) {
				// do nothing
			}
			Gson gson = new Gson();
			response.setContentType("text/plain");
			PrintWriter writer = response.getWriter();
			String json = gson.toJson(uploadList);
			writer.write(json);
			writer.close();
		}
	}

	@RequestMapping(value = "/download_ftp", method = RequestMethod.GET)
	public void downloadFile(@RequestParam("file") String fileName,
			HttpServletResponse response) throws IOException {
		String server = ftpConnection.getFtpAddress();
		String username = ftpConnection.getUsername();
		String password = ftpConnection.getPassword();
		Integer port = ftpConnection.getPort();
		FTPClient client = new FTPClient();
		FileInputStream inputStream = null;
		try {
			client.connect(server, port);
			client.login(username, password);
			client.enterLocalPassiveMode();
			client.setFileTransferMode(FTPClient.PASSIVE_REMOTE_DATA_CONNECTION_MODE);
			client.setFileType(FTPClient.BINARY_FILE_TYPE);
			client.setBufferSize(1024);
			String file_name = fileName
					.substring(fileName.lastIndexOf("/") + 1);
			String mimetype = servletContext.getMimeType(fileName);
			int size = uploadService.getFileSize(file_name, ftpConnection);
			response.setContentType(mimetype);
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ file_name + "\"");
			response.setContentLength(size);
			InputStream inStream = client.retrieveFileStream(fileName);
			BufferedInputStream bufferedStream = new BufferedInputStream(
					inStream);
			int bytesRead;
			byte[] buffer = new byte[1024];
			while ((bytesRead = bufferedStream.read(buffer)) != -1) {
				response.getOutputStream().write(buffer, 0, bytesRead);
			}
			inStream.close();
			response.getOutputStream().close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
				client.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@RequestMapping(value = "/delete_ftp", method = RequestMethod.DELETE)
	public void deleteFile(@RequestParam("file") String fileName,
			HttpServletResponse response) throws IOException {
		Boolean result = false;
		try {
			result = uploadService.deleteFromFtp(fileName, ftpConnection);
		} catch (BusinessLayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.setContentType("text/plain");
		PrintWriter writer = response.getWriter();
		Gson gson = new Gson();
		String json = gson.toJson(result);
		writer.write(json);
		writer.close();
	}

}
