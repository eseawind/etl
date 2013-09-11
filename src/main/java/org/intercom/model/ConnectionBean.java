package org.intercom.model;

import java.util.Map;

import org.hibernate.validator.constraints.NotEmpty;

public class ConnectionBean {

	private Integer id;
	private String password;
	private Map<Integer, String> sgbdList;
	private Integer sgbdSelected;
	private String sgbdType;
	private String delete_url;
	private String edit_url;

	@NotEmpty(message = "Nom de la connexion invalide.")
	private String connectionName;

	@NotEmpty(message = "Nom de la base de données invalide.")
	private String databaseName;

	@NotEmpty(message = "Nom d'utilisateur invalide.")
	private String username;

	@NotEmpty(message = "URL de connexion invalide.")
	private String connection_url;

	private String server;
	private String port;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getConnectionName() {
		return connectionName;
	}

	public void setConnectionName(String connectionName) {
		this.connectionName = connectionName;
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

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public Map<Integer, String> getSgbdList() {
		return sgbdList;
	}

	public void setSgbdList(Map<Integer, String> sgbdList) {
		this.sgbdList = sgbdList;
	}

	public Integer getSgbdSelected() {
		return sgbdSelected;
	}

	public void setSgbdSelected(Integer sgbdSelected) {
		this.sgbdSelected = sgbdSelected;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getConnection_url() {
		return connection_url;
	}

	public void setConnection_url(String connection_url) {
		this.connection_url = connection_url;
	}

	public String getSgbdType() {
		return sgbdType;
	}

	public void setSgbdType(String sgbdType) {
		this.sgbdType = sgbdType;
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
