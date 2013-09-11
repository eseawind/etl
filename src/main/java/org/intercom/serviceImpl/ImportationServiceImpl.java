package org.intercom.serviceImpl;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.net.ftp.FTPClient;
import org.intercom.model.ColumnBean;
import org.intercom.model.ConfigurationBean;
import org.intercom.model.ConnectionBean;
import org.intercom.model.FTPConnectionBean;
import org.intercom.model.ImportationBean;
import org.intercom.model.IntervalSelectionBean;
import org.intercom.service.ConfigurationService;
import org.intercom.service.DBconnectService;
import org.intercom.service.FTPConnectionService;
import org.intercom.service.ImportationService;
import org.intercom.service.LocalUploadService;
import org.intercom.service.exceptions.BusinessLayerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import au.com.bytecode.opencsv.CSVReader;

@Service
public class ImportationServiceImpl implements ImportationService {

	@Autowired
	private DBconnectService dbConnectService;
	@Autowired
	private ConfigurationService configurationService;
	@Autowired
	LocalUploadService localUploadService;
	@Autowired
	private FTPConnectionService ftpConnectionService;

	public Boolean mappingErrors(ImportationBean importationBean) {
		Map<Integer, String> table_columns_map = importationBean
				.getDatabaseTableColumnsList();
		List<ColumnBean> columnsBeanList = importationBean.getColumnsList();
		for (int index = 1; index < table_columns_map.size(); index++) {
			int mapping_times = 0;
			for (ColumnBean column : columnsBeanList) {
				if (column.getDb_table_column_index() == index)
					mapping_times++;
			}
			if (mapping_times > 1)
				return true;
		}
		return false;
	}

	public void importToDatabaseTable(ImportationBean importationBean)
			throws BusinessLayerException {
		Connection db_connection = null;
		PreparedStatement prepared_statement = null;
		// get database connection
		try {
			Integer db_connection_id = importationBean
					.getSelectedDBConnection();
			ConnectionBean connectionBean = dbConnectService
					.getById(db_connection_id);
			db_connection = dbConnectService.getConnection(connectionBean);
			if (db_connection == null)
				throw new BusinessLayerException(
						"La connexion à la base de données a échoué.");
			db_connection.setAutoCommit(false);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessLayerException(
					"La connexion à la base de données a échoué.");
		}
		// generate insert SQL query
		Integer selected_table = importationBean.getSelectedDatabaseTable();
		String table = importationBean.getDatabaseTablesList().get(
				selected_table);
		List<ColumnBean> columnsBeanList = importationBean.getColumnsList();
		Map<Integer, String> table_columns_map = new LinkedHashMap<Integer, String>();
		Map<Integer, String> table_columns_list = importationBean
				.getDatabaseTableColumnsList();
		int columns_map_index = 0;
		for (ColumnBean column : columnsBeanList) {
			if (column != null) {
				columns_map_index++;
				int index = column.getDb_table_column_index();
				String db_column_name = table_columns_list.get(index);
				table_columns_map.put(columns_map_index, db_column_name);
			}
		}
		// form insert SQL query
		Integer table_columns_nbr = table_columns_map.size();
		String sql = "INSERT INTO " + table + "(" + table_columns_map.get(1);
		for (int i = 2; i <= table_columns_nbr; i++)
			sql += ", " + table_columns_map.get(i);
		sql += ") VALUES";
		sql += "(?";
		for (int i = 2; i <= table_columns_nbr; i++)
			sql += ",?";
		sql += ")";
		// create prepared statement and batch
		try {
			prepared_statement = db_connection.prepareStatement(sql);
			// configure importation using configuration data
			Integer selectedConfiguration = importationBean
					.getSelectedConfiguration();
			ConfigurationBean configurationBean = configurationService
					.getById(selectedConfiguration);
			Boolean use_escape_char = false;
			Character char_secape = null;
			Boolean get_titles_from_first_line = configurationBean
					.getGet_titles_from_first_line();
			String encoding = configurationBean.getEncoding();
			Character char_separator = configurationBean.getField_separator();
			if (configurationBean.getSelected_escape_char() != 1) {
				use_escape_char = true;
				char_secape = configurationBean.getEscape_char();
			}
			boolean ignore_empty_lines = configurationBean
					.getIgnore_empty_lines();
			boolean limit_by_line_number = configurationBean
					.getLimit_by_line_number();
			int nb_lines_to_limit = 0;
			if (limit_by_line_number) {
				nb_lines_to_limit = configurationBean.getNumber_of_lines();
			}
			boolean limit_by_interval = configurationBean.isLimit_by_interval();
			List<IntervalSelectionBean> intervalList = null;
			if (limit_by_interval) {
				intervalList = configurationBean.getIntervalList();
			}
			boolean use_local_files = importationBean.isUse_local_files();
			boolean use_ftp_files = importationBean.isUse_ftp_files();
			CSVReader reader = null;
			if (use_local_files) {
				Integer selected_local_file = importationBean
						.getSelectedLocalFile();
				String file_name = importationBean.getLocalfilesList().get(
						selected_local_file);
				String file_path = localUploadService.getFullPath(file_name);
				InputStreamReader sreamReader = null;
				if (!encoding.isEmpty())
					sreamReader = new InputStreamReader(new FileInputStream(
							file_path), encoding);
				else
					sreamReader = new InputStreamReader(new FileInputStream(
							file_path));
				BufferedReader bufferedReader = new BufferedReader(sreamReader);
				if (use_escape_char)
					reader = new CSVReader(bufferedReader, char_separator,
							char_secape);
				else
					reader = new CSVReader(bufferedReader, char_separator);
			}
			if (use_ftp_files) {
				Integer ftp_connection_id = importationBean
						.getSelectedFTPConnection();
				FTPConnectionBean ftpConnection = ftpConnectionService
						.getById(ftp_connection_id);
				String server = ftpConnection.getFtpAddress();
				String username = ftpConnection.getUsername();
				String password = ftpConnection.getPassword();
				Integer port = ftpConnection.getPort();
				FTPClient client = new FTPClient();
				InputStream inputStream = null;
				String filename = importationBean.getFtpFilesList().get(
						importationBean.getSelectedFTPFile());
				client.connect(server, port);
				client.login(username, password);
				client.enterLocalPassiveMode();
				client.setFileTransferMode(FTPClient.PASSIVE_REMOTE_DATA_CONNECTION_MODE);
				client.setFileType(FTPClient.BINARY_FILE_TYPE);
				inputStream = client.retrieveFileStream(filename);
				BufferedInputStream bufferedStream = new BufferedInputStream(
						inputStream);
				InputStreamReader sreamReader = null;
				if (!encoding.isEmpty())
					sreamReader = new InputStreamReader(bufferedStream,
							encoding);
				else
					sreamReader = new InputStreamReader(bufferedStream);
				BufferedReader bufferedReader = new BufferedReader(sreamReader);
				if (use_escape_char)
					reader = new CSVReader(bufferedReader, char_separator,
							char_secape);
				else
					reader = new CSVReader(bufferedReader, char_separator);
			}
			// skip header if titles are in the first line
			if (get_titles_from_first_line)
				reader.readNext();
			int BATCH_SIZE = 500;
			int nb_read_rows = 0;
			int nb_processed_rows = 0;
			String[] nextLine;
			boolean stop = false;
			while (((nextLine = reader.readNext()) != null) && !stop) {
				nb_read_rows++;
				boolean skip_empty = false;
				if (ignore_empty_lines) {
					boolean is_empty = true;
					for (int i = 0; i < nextLine.length; i++) {
						if ((!nextLine[i].equals("")) && (nextLine[i] != null))
							is_empty = false;
					}
					if (is_empty)
						skip_empty = true;
				}
				boolean skip_limit = false;
				if (limit_by_interval) {
					skip_limit = true;
					boolean getout = false;
					for (IntervalSelectionBean interval : intervalList) {
						int start_line_nb = interval.getStart_line_nb();
						int end_line_nb = interval.getEnd_line_nb();
						if ((nb_read_rows >= start_line_nb)
								&& (nb_read_rows <= end_line_nb)) {
							skip_limit = false;
							getout = true;
							break;
						}
						if (getout)
							break;
					}
				}
				if (!(skip_empty || skip_limit)) {
					nb_processed_rows++;
					int statement_index = 0;
					int line_lenght = nextLine.length;
					for (Integer column_index = 0; column_index < line_lenght; column_index++) {
						ColumnBean column = findByOrder(columnsBeanList,
								column_index + 1);
						if (column != null) {
							statement_index++;
							int type_value = column.getSelectedType();
							switch (type_value) {
							case 1:
								prepared_statement.setString(statement_index,
										nextLine[column_index]);
								break;
							case 2:
								prepared_statement.setBigDecimal(
										statement_index, new BigDecimal(
												nextLine[column_index]));
								break;
							case 3:
								prepared_statement
										.setBoolean(
												statement_index,
												Boolean.valueOf(nextLine[column_index]));
								break;
							case 4:
								prepared_statement.setByte(statement_index,
										nextLine[column_index].getBytes()[0]);
								break;
							case 5:
								prepared_statement.setShort(statement_index,
										Short.valueOf(nextLine[column_index]));
								break;
							case 6:
								prepared_statement
										.setInt(statement_index,
												Integer.parseInt(nextLine[column_index]));
								break;
							case 7:
								prepared_statement.setLong(statement_index,
										Long.parseLong(nextLine[column_index]));
								break;
							case 8:
								prepared_statement.setFloat(statement_index,
										Float.valueOf(nextLine[column_index]));
								break;
							case 9:
								prepared_statement.setDouble(statement_index,
										Double.valueOf(nextLine[column_index]));
								break;
							case 10:
								prepared_statement.setBytes(statement_index,
										nextLine[column_index].getBytes());
								break;
							case 11:
								prepared_statement.setDate(statement_index,
										Date.valueOf(nextLine[column_index]));
								break;
							case 12:
								prepared_statement.setTime(statement_index,
										Time.valueOf(nextLine[column_index]));
								break;
							case 13:
								prepared_statement
										.setTimestamp(
												statement_index,
												Timestamp
														.valueOf(nextLine[column_index]));
								break;
							}
						}
					}
					if (line_lenght < table_columns_nbr) {
						// Erreur Longueur de ligne dépassé
					}
					
					prepared_statement.addBatch();
					if ((nb_processed_rows + 1) % BATCH_SIZE == 0)
						prepared_statement.executeBatch();
				}
				if (nb_processed_rows % BATCH_SIZE != 0)
					prepared_statement.executeBatch();
				// exit if number of line limit reached
				if (limit_by_line_number && (nb_lines_to_limit != 0)) {
					if (nb_processed_rows == nb_lines_to_limit)
						stop = true;
				}
			}
			db_connection.commit();
		} catch (Exception e) {
			try {
				db_connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				throw new BusinessLayerException(
						"Une erreur s'est produite lors du traitement de votre demande.");
			}
			e.printStackTrace();
			throw new BusinessLayerException(
					"Une erreur s'est produite lors du traitement de votre demande.");
		} finally {
			if (prepared_statement != null) {
				try {
					prepared_statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
					throw new BusinessLayerException(
							"Une erreur s'est produite lors du traitement de votre demande.");
				}
			}
		}
	}

	private ColumnBean findByOrder(List<ColumnBean> columnsBeanList,
			Integer order) {
		for (ColumnBean column : columnsBeanList) {
			if (column.getOrder() == order)
				return column;
		}
		return null;
	}
}
