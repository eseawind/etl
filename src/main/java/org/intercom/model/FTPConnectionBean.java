package org.intercom.model;

import org.hibernate.validator.constraints.NotEmpty;

public class FTPConnectionBean {

	@NotEmpty(message = "Nom invalide.")
	String connectionName;

	@NotEmpty(message = "Adresse FTP invalide.")
	String ftpAddress;

	@NotEmpty(message = "Nom d'utilisateur invalide.")
	String username;

	Integer id;
	String password;
	Integer port = 21;
	
	private String delete_url;
	private String edit_url;
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getFtpAddress() {
		return ftpAddress;
	}

	public void setFtpAddress(String ftpAddress) {
		this.ftpAddress = ftpAddress;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getConnectionName() {
		return connectionName;
	}

	public void setConnectionName(String connectionName) {
		this.connectionName = connectionName;
	}

	public String getDelete_url() {
		return delete_url;
	}

	public void setDelete_url(String delete_url) {
		this.delete_url = delete_url;
	}

	public String getEdit_url() {
		return edit_url;
	}

	public void setEdit_url(String edit_url) {
		this.edit_url = edit_url;
	}
}
