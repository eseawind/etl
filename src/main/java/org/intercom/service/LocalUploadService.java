package org.intercom.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.intercom.model.FileDetails;
import org.intercom.service.exceptions.BusinessLayerException;

public interface LocalUploadService {

	public List<FileDetails> getResponse(String fileName, long size,
			String contentType);

	public File getDownloadFile(String fileName) throws BusinessLayerException;

	public List<FileDetails> getExistingFiles() throws BusinessLayerException;

	public Boolean deleteFile(String fileName) throws BusinessLayerException;

	public Map<Integer, String> getLocalFilesList()
			throws BusinessLayerException;

	public String getFullPath(String filename) throws BusinessLayerException;

}
