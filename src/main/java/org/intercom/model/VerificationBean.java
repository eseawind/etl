package org.intercom.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class VerificationBean {

	private Integer selectedConfiguration;
	private Integer selectedDataStructure;
	private Integer selectedLocalFile;
	private Integer selectedFTPConnection;
	private Integer selectedFTPFile;

	private boolean use_local_files;
	private boolean use_ftp_files;

	private Map<Integer, String> configurationList;
	private Map<Integer, String> dataStructureList;
	private Map<Integer, String> localfilesList;
	private Map<Integer, String> ftpConnectionsList;
	private Map<Integer, String> ftpFilesList;
	
	private List<ColumnBean> columnsList;
	private List<ErrorBean> errorList;
	private List<ErrorBean> primatyKeyNullErrors;
	private List<ErrorBean> pkRedundancyErrors;
	private List<ErrorBean> typeMismatchErrors;
	private List<ErrorBean> maximumLenghtErrors;
	private List<ErrorBean> outOfIntervalErrors;

	public VerificationBean() {
		ftpFilesList = new LinkedHashMap<Integer, String>();
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

	public boolean isUse_local_files() {
		return use_local_files;
	}

	public void setUse_local_files(boolean use_local_files) {
		this.use_local_files = use_local_files;
	}

	public boolean isUse_ftp_files() {
		return use_ftp_files;
	}

	public void setUse_ftp_files(boolean use_ftp_files) {
		this.use_ftp_files = use_ftp_files;
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

	public List<ColumnBean> getColumnsList() {
		return columnsList;
	}

	public void setColumnsList(List<ColumnBean> columnsList) {
		this.columnsList = columnsList;
	}

	public List<ErrorBean> getErrorList() {
		return errorList;
	}

	public void setErrorList(List<ErrorBean> errorList) {
		this.errorList = errorList;
	}

	public List<ErrorBean> getPrimatyKeyNullErrors() {
		return primatyKeyNullErrors;
	}

	public void setPrimatyKeyNullErrors(List<ErrorBean> primatyKeyNullErrors) {
		this.primatyKeyNullErrors = primatyKeyNullErrors;
	}

	public List<ErrorBean> getPkRedundancyErrors() {
		return pkRedundancyErrors;
	}

	public void setPkRedundancyErrors(List<ErrorBean> pkRedundancyErrors) {
		this.pkRedundancyErrors = pkRedundancyErrors;
	}

	public List<ErrorBean> getTypeMismatchErrors() {
		return typeMismatchErrors;
	}

	public void setTypeMismatchErrors(List<ErrorBean> typeMismatchErrors) {
		this.typeMismatchErrors = typeMismatchErrors;
	}

	public List<ErrorBean> getMaximumLenghtErrors() {
		return maximumLenghtErrors;
	}

	public void setMaximumLenghtErrors(List<ErrorBean> maximumLenghtErrors) {
		this.maximumLenghtErrors = maximumLenghtErrors;
	}

	public List<ErrorBean> getOutOfIntervalErrors() {
		return outOfIntervalErrors;
	}

	public void setOutOfIntervalErrors(List<ErrorBean> outOfIntervalErrors) {
		this.outOfIntervalErrors = outOfIntervalErrors;
	}
}
