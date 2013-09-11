package org.intercom.serviceImpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.intercom.model.FTPConnectionBean;
import org.intercom.model.FileDetails;
import org.intercom.service.FTPuploadService;
import org.intercom.service.exceptions.BusinessLayerException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Service
public class FTPuploadServiceImpl implements FTPuploadService {

	public List<FileDetails> getResponse(String fileName, long size,
			String contentType) {
		List<FileDetails> uploadList = new ArrayList<FileDetails>();
		FileDetails fileDetails = new FileDetails();
		fileDetails.setName(fileName);
		fileDetails.setSize(size);
		fileDetails.setType(contentType);
		String downloadURL = "download_ftp?file=" + fileName;
		fileDetails.setUrl(downloadURL);
		String deleteURL = "delete_ftp?file=" + fileName;
		fileDetails.setDelete_url(deleteURL);
		fileDetails.setDelete_type("DELETE");
		uploadList.add(fileDetails);
		return uploadList;
	}

	public List<FileDetails> getExistingFiles(FTPConnectionBean ftpConnection)
			throws BusinessLayerException {
		try {
			String server = ftpConnection.getFtpAddress();
			String username = ftpConnection.getUsername();
			String password = ftpConnection.getPassword();
			Integer port = ftpConnection.getPort();
			List<FileDetails> uploadList = new ArrayList<FileDetails>();
			FTPClient client = new FTPClient();
			client.connect(server, port);
			client.login(username, password);
			FTPFile[] ftpFiles = client.listFiles();
			for (FTPFile ftpFile : ftpFiles) {
				if (ftpFile.getType() == FTPFile.FILE_TYPE) {
					System.out.println("FTPFile: " + ftpFile.getName() + "; "
							+ ftpFile.getSize());
					FileDetails fileDetails = new FileDetails();
					String fileName = ftpFile.getName();
					long size = ftpFile.getSize();
					String type = "text/csv";
					fileDetails.setName(fileName);
					fileDetails.setSize(size);
					fileDetails.setType(type);
					String downloadURL = "download_ftp?file=" + fileName;
					fileDetails.setUrl(downloadURL);
					String deleteURL = "delete_ftp?file=" + fileName;
					fileDetails.setDelete_url(deleteURL);
					fileDetails.setDelete_type("DELETE");
					uploadList.add(fileDetails);
				}
			}
			client.logout();
			return uploadList;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	public Boolean uploadToFtp(CommonsMultipartFile uploadedFile,
			FTPConnectionBean ftpConnection) throws BusinessLayerException {
		String server = ftpConnection.getFtpAddress();
		String username = ftpConnection.getUsername();
		String password = ftpConnection.getPassword();
		Integer port = ftpConnection.getPort();
		FTPClient client = new FTPClient();
		InputStream inputStream = null;
		Boolean result = false;
		try {
			client.connect(server, port);
			client.login(username, password);
			String filename = uploadedFile.getOriginalFilename();
			client.setFileType(FTP.BINARY_FILE_TYPE);
			inputStream = uploadedFile.getInputStream();
			result = client.storeFile(filename, inputStream);
			result = true;
			client.logout();
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
				client.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
				throw new BusinessLayerException(e.getMessage());
			}
		}
	}

	public Boolean deleteFromFtp(String fileName,
			FTPConnectionBean ftpConnection) throws BusinessLayerException {
		String server = ftpConnection.getFtpAddress();
		String username = ftpConnection.getUsername();
		String password = ftpConnection.getPassword();
		Integer port = ftpConnection.getPort();
		FTPClient client = new FTPClient();
		Boolean result = false;
		try {
			client.connect(server, port);
			client.login(username, password);
			result = client.deleteFile(fileName);
			client.logout();
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		} finally {
			try {
				client.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
				throw new BusinessLayerException(e.getMessage());
			}
		}
		return result;
	}

	public int getFileSize(String file_name, FTPConnectionBean ftpConnection)
			throws BusinessLayerException {
		String server = ftpConnection.getFtpAddress();
		String username = ftpConnection.getUsername();
		String password = ftpConnection.getPassword();
		Integer port = ftpConnection.getPort();
		FTPClient client = new FTPClient();
		long size = 0;
		try {
			client.connect(server, port);
			client.login(username, password);
			FTPFile[] ftpFiles = client.listFiles();
			for (FTPFile ftpFile : ftpFiles) {

				if ((ftpFile.getType() == FTPFile.FILE_TYPE)
						&& (ftpFile.getName().equals(file_name))) {
					size = ftpFile.getSize();
				}
			}
			client.logout();
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		} finally {
			try {
				client.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
				throw new BusinessLayerException(e.getMessage());
			}
		}
		return (int) size;
	}

}
