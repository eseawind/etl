package org.intercom.serviceImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletContext;

import org.intercom.model.FileDetails;
import org.intercom.service.LocalUploadService;
import org.intercom.service.exceptions.BusinessLayerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocalUploadServiceImpl implements LocalUploadService {

	@Autowired
	ServletContext servletContext = null;

	public List<FileDetails> getResponse(String fileName, long size,
			String contentType) {
		List<FileDetails> uploadList = new ArrayList<FileDetails>();
		FileDetails fileDetails = new FileDetails();
		fileDetails.setName(fileName);
		fileDetails.setSize(size);
		fileDetails.setType(contentType);
		String downloadURL = "download?file=" + fileName;
		fileDetails.setUrl(downloadURL);
		String deleteURL = "delete?file=" + fileName;
		fileDetails.setDelete_url(deleteURL);
		fileDetails.setDelete_type("DELETE");
		uploadList.add(fileDetails);
		return uploadList;
	}

	public File getDownloadFile(String fileName) throws BusinessLayerException {
		try {
			String upload_directory = servletContext.getRealPath("/uploads")
					+ System.getProperty("file.separator");
			String filePath = upload_directory + fileName;
			return new File(filePath);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	public String getFullPath(String filename) throws BusinessLayerException {
		try {
			String upload_directory = servletContext.getRealPath("/uploads")
					+ System.getProperty("file.separator");
			String filePath = upload_directory + filename;
			return filePath;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	public List<FileDetails> getExistingFiles() throws BusinessLayerException {
		List<FileDetails> uploadList = new ArrayList<FileDetails>();
		try {
			String upload_directory = servletContext.getRealPath("/uploads")
					+ System.getProperty("file.separator");
			File uploadFolder = new File(upload_directory);
			File[] fileList = uploadFolder.listFiles();
			for (File file : fileList) {
				FileDetails fileDetails = new FileDetails();
				String fileName = file.getName();
				long size = file.length();
				String contentType = new MimetypesFileTypeMap()
						.getContentType(file);
				fileDetails.setName(fileName);
				fileDetails.setSize(size);
				fileDetails.setType(contentType);
				String downloadURL = "download?file=" + fileName;
				fileDetails.setUrl(downloadURL);
				String deleteURL = "delete?file=" + fileName;
				fileDetails.setDelete_url(deleteURL);
				fileDetails.setDelete_type("DELETE");
				uploadList.add(fileDetails);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
		return uploadList;
	}

	public Map<Integer, String> getLocalFilesList()
			throws BusinessLayerException {
		Map<Integer, String> hashMap = new LinkedHashMap<Integer, String>();
		try {
			String upload_directory = servletContext.getRealPath("/uploads")
					+ System.getProperty("file.separator");
			File uploadFolder = new File(upload_directory);
			File[] fileList = uploadFolder.listFiles();
			int i = 0;
			for (File file : fileList) {
				i++;
				String fileName = file.getName();
				hashMap.put(i, fileName);
			}
			return hashMap;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	public Boolean deleteFile(String fileName) throws BusinessLayerException {
		try {
			String upload_directory = servletContext.getRealPath("/uploads")
					+ System.getProperty("file.separator");
			String filePath = upload_directory + fileName;
			File file = new File(filePath);
			return file.delete();
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

}
