package org.intercom.service;

import java.io.IOException;
import java.util.List;

import org.intercom.model.FTPConnectionBean;
import org.intercom.model.FileDetails;
import org.intercom.service.exceptions.BusinessLayerException;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

public interface FTPuploadService {
	public List<FileDetails> getExistingFiles(FTPConnectionBean ftpConnection)
			throws IOException, BusinessLayerException;

	public Boolean uploadToFtp(CommonsMultipartFile uploadedFile,
			FTPConnectionBean ftpConnection) throws BusinessLayerException;

	public List<FileDetails> getResponse(String fileName, long size,
			String contentType);

	public Boolean deleteFromFtp(String fileName,
			FTPConnectionBean ftpConnection) throws BusinessLayerException;

	public int getFileSize(String file_name, FTPConnectionBean ftpConnection)
			throws BusinessLayerException;

}
