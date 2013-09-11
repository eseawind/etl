package org.intercom.model;

import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

public class DataStructureBean {

	private Integer id;
	@NotEmpty(message = "Nom invalide.")
	private String name;
	private Integer selectedLocalFile;
	private Integer selectedFTPFile;
	private Integer selectedFTPConnection;
	private Integer selectedconfiguration;
	private List<ColumnBean> ColumnList;
	private String details_url;
	private String edit_url;
	private String delete_url;

	public Integer getSelectedLocalFile() {
		return selectedLocalFile;
	}

	public void setSelectedLocalFile(Integer selectedLocalFile) {
		this.selectedLocalFile = selectedLocalFile;
	}

	public List<ColumnBean> getColumnList() {
		return ColumnList;
	}

	public void setColumnList(List<ColumnBean> columnList) {
		ColumnList = columnList;
	}

	public Integer getSelectedconfiguration() {
		return selectedconfiguration;
	}

	public void setSelectedconfiguration(Integer selectedconfiguration) {
		this.selectedconfiguration = selectedconfiguration;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDetails_url() {
		return details_url;
	}

	public void setDetails_url(String details_url) {
		this.details_url = details_url;
	}

	public String getEdit_url() {
		return edit_url;
	}

	public void setEdit_url(String edit_url) {
		this.edit_url = edit_url;
	}

	public String getDelete_url() {
		return delete_url;
	}

	public void setDelete_url(String delete_url) {
		this.delete_url = delete_url;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSelectedFTPConnection() {
		return selectedFTPConnection;
	}

	public void setSelectedFTPConnection(Integer selectedFTPConnection) {
		this.selectedFTPConnection = selectedFTPConnection;
	}

	public Integer getSelectedFTPFile() {
		return selectedFTPFile;
	}

	public void setSelectedFTPFile(Integer selectedFTPFile) {
		this.selectedFTPFile = selectedFTPFile;
	}

}
