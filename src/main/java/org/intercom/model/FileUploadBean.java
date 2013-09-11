package org.intercom.model;

import java.io.File;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class FileUploadBean {

	CommonsMultipartFile uploadedFile;

	public CommonsMultipartFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(CommonsMultipartFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	File uploadfile;

	public File getUploadfile() {
		return uploadfile;
	}

	public void setUploadfile(File uploadfile) {
		this.uploadfile = uploadfile;
	}
	

}
