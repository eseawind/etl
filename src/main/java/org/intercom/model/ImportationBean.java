package org.intercom.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ImportationBean {

	private Integer selectedDBConnection;
	private Integer selectedDatabaseTable;
	private Integer selectedConfiguration;
	private Integer selectedDataStructure;
	private Integer selectedLocalFile;
	private Integer selectedFTPConnection;
	private Integer selectedFTPFile;

	private boolean use_local_files;
	private boolean use_ftp_files;

	private List<ColumnBean> columnsList;

	private Map<Integer, String> databaseConnectionList;
	private Map<Integer, String> configurationList;
	private Map<Integer, String> dataStructureList;
	private Map<Integer, String> localfilesList;
	private Map<Integer, String> ftpConnectionsList;
	private Map<Integer, String> ftpFilesList;
	private Map<Integer, String> databaseTablesList;
	private Map<Integer, String> databaseTableColumnsList;

	public ImportationBean() {
		ftpFilesList = new LinkedHashMap<Integer, String>();
		setDatabaseTablesList(new LinkedHashMap<Integer, String>());
		databaseTableColumnsList = new LinkedHashMap<Integer, String>();
		columnsList = new ArrayList<ColumnBean>();
	}

	public Integer getSelectedDBConnection() {
		return selectedDBConnection;
	}

	public void setSelectedDBConnection(Integer selectedDBConnection) {
		this.selectedDBConnection = selectedDBConnection;
	}

	public Integer getSelectedConfiguration() {
		return selectedConfiguration;
	}

	public void setSelectedConfiguration(Integer selectedConfiguration) {
		this.selectedConfiguration = selectedConfiguration;
	}

	public Integer getSelectedDataStructure() {
		return selectedDataStructure;
	}

	public void setSelectedDataStructure(Integer selectedDataStructure) {
		this.selectedDataStructure = selectedDataStructure;
	}

	public Integer getSelectedLocalFile() {
		return selectedLocalFile;
	}

	public void setSelectedLocalFile(Integer selectedLocalFile) {
		this.selectedLocalFile = selectedLocalFile;
	}

	public boolean isUse_local_files() {
		return use_local_files;
	}

	public void setUse_local_files(boolean use_local_files) {
		this.use_local_files = use_local_files;
	}

	public Integer getSelectedFTPConnection() {
		return selectedFTPConnection;
	}

	public void setSelectedFTPConnection(Integer selectedFTPConnection) {
		this.selectedFTPConnection = selectedFTPConnection;
	}

	public boolean isUse_ftp_files() {
		return use_ftp_files;
	}

	public void setUse_ftp_files(boolean use_ftp_files) {
		this.use_ftp_files = use_ftp_files;
	}

	public Integer getSelectedFTPFile() {
		return selectedFTPFile;
	}

	public void setSelectedFTPFile(Integer selectedFTPFile) {
		this.selectedFTPFile = selectedFTPFile;
	}

	public Integer getSelectedDatabaseTable() {
		return selectedDatabaseTable;
	}

	public void setSelectedDatabaseTable(Integer selectedDatabaseTable) {
		this.selectedDatabaseTable = selectedDatabaseTable;
	}

	public List<ColumnBean> getColumnsList() {
		return columnsList;
	}

	public void setColumnsList(List<ColumnBean> columnsList) {
		this.columnsList = columnsList;
	}

	public Map<Integer, String> getDatabaseConnectionList() {
		return databaseConnectionList;
	}

	public void setDatabaseConnectionList(
			Map<Integer, String> databaseConnectionList) {
		this.databaseConnectionList = databaseConnectionList;
	}

	public Map<Integer, String> getConfigurationList() {
		return configurationList;
	}

	public void setConfigurationList(Map<Integer, String> configurationList) {
		this.configurationList = configurationList;
	}

	public Map<Integer, String> getDataStructureList() {
		return dataStructureList;
	}

	public void setDataStructureList(Map<Integer, String> dataStructureList) {
		this.dataStructureList = dataStructureList;
	}

	public Map<Integer, String> getLocalfilesList() {
		return localfilesList;
	}

	public void setLocalfilesList(Map<Integer, String> localfilesList) {
		this.localfilesList = localfilesList;
	}

	public Map<Integer, String> getFtpConnectionsList() {
		return ftpConnectionsList;
	}

	public void setFtpConnectionsList(Map<Integer, String> ftpConnectionsList) {
		this.ftpConnectionsList = ftpConnectionsList;
	}

	public Map<Integer, String> getFtpFilesList() {
		return ftpFilesList;
	}

	public void setFtpFilesList(Map<Integer, String> ftpFilesList) {
		this.ftpFilesList = ftpFilesList;
	}

	public Map<Integer, String> getDatabaseTableColumnsList() {
		return databaseTableColumnsList;
	}

	public void setDatabaseTableColumnsList(
			Map<Integer, String> databaseTableColumnsList) {
		this.databaseTableColumnsList = databaseTableColumnsList;
	}

	public Map<Integer, String> getDatabaseTablesList() {
		return databaseTablesList;
	}

	public void setDatabaseTablesList(Map<Integer, String> databaseTablesList) {
		this.databaseTablesList = databaseTablesList;
	}

}
