package org.intercom.serviceImpl;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.net.ftp.FTPClient;
import org.intercom.dao.ParametresDao;
import org.intercom.dao.StructureDonneesDao;
import org.intercom.dao.exceptions.DAOLayerException;
import org.intercom.model.ColumnBean;
import org.intercom.model.ConfigurationBean;
import org.intercom.model.DataStructureBean;
import org.intercom.model.FTPConnectionBean;
import org.intercom.service.ConfigurationService;
import org.intercom.service.DataStructureService;
import org.intercom.service.LocalUploadService;
import org.intercom.service.exceptions.BusinessLayerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import au.com.bytecode.opencsv.CSVReader;

@Service
public class DataStructureServiceImpl implements DataStructureService {

	@Autowired
	ConfigurationService configurationService;

	@Autowired
	StructureDonneesDao structureDonneesDao;

	@Autowired
	LocalUploadService localUploadService;

	@Autowired
	ParametresDao parametresDao;

	public List<ColumnBean> getDataStructureFromLocalFile(String filename,
			Integer selectedConfiguration) throws BusinessLayerException {
		try {
			Boolean use_escape_char = false;
			Character char_secape = null;
			ConfigurationBean configurationBean = configurationService
					.getById(selectedConfiguration);
			Boolean get_titles_from_first_line = configurationBean
					.getGet_titles_from_first_line();
			String encoding = configurationBean.getEncoding();
			Character char_separator = configurationBean.getField_separator();
			if (configurationBean.getSelected_escape_char() != 1) {
				use_escape_char = true;
				char_secape = configurationBean.getEscape_char();
			}
			String file_path = localUploadService.getFullPath(filename);
			InputStreamReader sreamReader = null;
			if (!encoding.isEmpty())
				sreamReader = new InputStreamReader(new FileInputStream(
						file_path), encoding);
			else
				sreamReader = new InputStreamReader(new FileInputStream(
						file_path));
			BufferedReader bufferedReader = new BufferedReader(sreamReader);
			CSVReader reader = null;
			if (use_escape_char)
				reader = new CSVReader(bufferedReader, char_separator,
						char_secape);
			else
				reader = new CSVReader(bufferedReader, char_separator);
			String[] firstLine = reader.readNext();
			List<ColumnBean> columnsList = new ArrayList<ColumnBean>();
			for (int i = 0; i < firstLine.length; i++) {
				Integer index = i + 1;
				ColumnBean column = new ColumnBean();
				String column_name = "Colonne " + index;
				if (get_titles_from_first_line)
					column_name = firstLine[i];
				Integer column_lenght = column_name.length();
				String edit_url = "column_local_edit?order=" + index;
				String delete_url = "column_local_delete?order=" + index;
				column.setOrder(index);
				column.setName(column_name);
				column.setSelectedType(1);
				BigDecimal selected_type = new BigDecimal((double) 1);
				String display_type = parametresDao.getLabel("type_colonne",
						selected_type);
				column.setDisplay_type(display_type);
				column.setLenght(column_lenght);
				column.setNullable(true);
				column.setPrimary_Key(false);
				column.setEdit_url(edit_url);
				column.setDelete_url(delete_url);
				columnsList.add(column);
			}
			return columnsList;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	public List<ColumnBean> getDataStructureFromFTPFile(String filename,
			FTPConnectionBean ftpConnection, Integer selectedConfiguration)
			throws BusinessLayerException {
		String server = ftpConnection.getFtpAddress();
		String username = ftpConnection.getUsername();
		String password = ftpConnection.getPassword();
		Integer port = ftpConnection.getPort();
		FTPClient client = new FTPClient();
		InputStream inputStream = null;
		try {
			client.connect(server, port);
			client.login(username, password);
			client.enterLocalPassiveMode();
			client.setFileTransferMode(FTPClient.PASSIVE_REMOTE_DATA_CONNECTION_MODE);
			client.setFileType(FTPClient.BINARY_FILE_TYPE);
			inputStream = client.retrieveFileStream(filename);
			BufferedInputStream bufferedStream = new BufferedInputStream(
					inputStream);
			Boolean use_escape_char = false;
			Character char_secape = null;
			ConfigurationBean configurationBean = configurationService
					.getById(selectedConfiguration);
			Boolean get_titles_from_first_line = configurationBean
					.getGet_titles_from_first_line();
			String encoding = configurationBean.getEncoding();
			Character char_separator = configurationBean.getField_separator();
			if (configurationBean.getSelected_escape_char() != 1) {
				use_escape_char = true;
				char_secape = configurationBean.getEscape_char();
			}
			InputStreamReader sreamReader = null;
			if (!encoding.isEmpty())
				sreamReader = new InputStreamReader(bufferedStream, encoding);
			else
				sreamReader = new InputStreamReader(bufferedStream);
			BufferedReader bufferedReader = new BufferedReader(sreamReader);
			CSVReader reader = null;
			if (use_escape_char)
				reader = new CSVReader(bufferedReader, char_separator,
						char_secape);
			else
				reader = new CSVReader(bufferedReader, char_separator);
			String[] firstLine = reader.readNext();
			List<ColumnBean> columnsList = new ArrayList<ColumnBean>();
			for (int i = 0; i < firstLine.length; i++) {
				Integer index = i + 1;
				ColumnBean column = new ColumnBean();
				String column_name = "Colonne " + index;
				if (get_titles_from_first_line)
					column_name = firstLine[i];
				Integer column_lenght = column_name.length();
				String edit_url = "column_ftp_edit?order=" + index;
				String delete_url = "column_ftp_delete?order=" + index;
				column.setOrder(index);
				column.setName(column_name);
				column.setSelectedType(1);
				BigDecimal selected_type = new BigDecimal((double) 1);
				String display_type = parametresDao.getLabel("type_colonne",
						selected_type);
				column.setDisplay_type(display_type);
				column.setLenght(column_lenght);
				column.setNullable(true);
				column.setPrimary_Key(false);
				column.setEdit_url(edit_url);
				column.setDelete_url(delete_url);
				columnsList.add(column);
			}
			inputStream.close();
			return columnsList;
		} catch (Exception e) {
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

	public void saveDataStructure(DataStructureBean dataStructureBean)
			throws BusinessLayerException {
		try {
			structureDonneesDao.saveBean(dataStructureBean);
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	public List<DataStructureBean> getDataStructureList()
			throws BusinessLayerException {
		try {
			return structureDonneesDao.getDataStructureList();
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	public void deleteById(Integer id) throws BusinessLayerException {
		try {
			structureDonneesDao.deleteById(id);
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	public DataStructureBean getById(Integer id) throws BusinessLayerException {
		try {
			return structureDonneesDao.getById(id);
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	public void updateDataStructureBean(DataStructureBean dataStructureBean)
			throws BusinessLayerException {
		try {
			structureDonneesDao.update(dataStructureBean);
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	public ColumnBean prepareColumnBean(ColumnBean columnBean)
			throws BusinessLayerException {
		try {
			Map<Integer, String> hashMap = parametresDao
					.getParamList("type_colonne");
			columnBean.setType_list(hashMap);
			if (columnBean.getSelectedType() != null) {
				BigDecimal selected_type = new BigDecimal(
						(double) columnBean.getSelectedType());
				String display_type = parametresDao.getLabel("type_colonne",
						selected_type);
				columnBean.setDisplay_type(display_type);
			}
			return columnBean;
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	public Map<Integer, String> getDataStructureSelectionList()
			throws BusinessLayerException {
		try {
			return structureDonneesDao.getDataStructureConnectionList();
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

}
