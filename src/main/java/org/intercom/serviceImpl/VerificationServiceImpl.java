package org.intercom.serviceImpl;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.intercom.model.ColumnBean;
import org.intercom.model.ConfigurationBean;
import org.intercom.model.ErrorBean;
import org.intercom.model.FTPConnectionBean;
import org.intercom.model.IntervalSelectionBean;
import org.intercom.model.VerificationBean;
import org.intercom.service.ConfigurationService;
import org.intercom.service.FTPConnectionService;
import org.intercom.service.LocalUploadService;
import org.intercom.service.VerificationService;
import org.intercom.service.exceptions.BusinessLayerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import au.com.bytecode.opencsv.CSVReader;

@Service
public class VerificationServiceImpl implements VerificationService {

	@Autowired
	private ConfigurationService configurationService;
	@Autowired
	LocalUploadService localUploadService;
	@Autowired
	private FTPConnectionService ftpConnectionService;

	public List<ErrorBean> detectErros(VerificationBean verificationBean)
			throws BusinessLayerException {
		try {
			List<ErrorBean> error_list = new ArrayList<ErrorBean>();
			List<Integer> primary_keys_list = new ArrayList<Integer>();
			List<ColumnBean> columnsBeanList = verificationBean
					.getColumnsList();
			Integer selectedConfiguration = verificationBean
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
			boolean use_local_files = verificationBean.isUse_local_files();
			boolean use_ftp_files = verificationBean.isUse_ftp_files();

			CSVReader reader = null;
			if (use_local_files) {
				Integer selected_local_file = verificationBean
						.getSelectedLocalFile();
				String file_name = verificationBean.getLocalfilesList().get(
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
				Integer ftp_connection_id = verificationBean
						.getSelectedFTPConnection();
				FTPConnectionBean ftpConnection = ftpConnectionService
						.getById(ftp_connection_id);
				String server = ftpConnection.getFtpAddress();
				String username = ftpConnection.getUsername();
				String password = ftpConnection.getPassword();
				Integer port = ftpConnection.getPort();
				FTPClient client = new FTPClient();
				InputStream inputStream = null;
				String filename = verificationBean.getFtpFilesList().get(
						verificationBean.getSelectedFTPFile());
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
			// skip header
			if (get_titles_from_first_line)
				reader.readNext();
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
					int line_lenght = nextLine.length;
					for (Integer column_index = 0; column_index < line_lenght; column_index++) {
						ColumnBean column = findByOrder(columnsBeanList,
								column_index + 1);
						if (column != null) {
							Boolean stop_verification = false;
							// # Verify primary key constraint
							Boolean is_primary_key = column.getPrimary_Key();
							String read_value = nextLine[column_index];
							if (is_primary_key) {
								// # Primary key properties verification
								if (read_value.isEmpty()) {
									// @ Error type 1 --> Primary key value is
									// null
									ErrorBean error = new ErrorBean();
									error.setType(1);
									error.setLine_nb(nb_read_rows);
									error.setColumn_nb(column_index + 1);
									error.setText(nextLine[column_index]);
									String description = "Valeur de la clé primaire est nulle.";
									error.setDescription(description);
									error_list.add(error);
									stop_verification = true;
								}
								if (!stop_verification) {
									// # Verify if primary key is convertible to
									// integer
									// @ Error type 2 --> Primary key is not an
									// integer
									Integer id = null;
									try {
										id = Integer.parseInt(read_value);
									} catch (NumberFormatException e) {
										ErrorBean error = new ErrorBean();
										error.setType(2);
										error.setLine_nb(nb_read_rows);
										error.setColumn_nb(column_index + 1);
										error.setText(nextLine[column_index]);
										String description = "La clé primaire n'est pas de type entier.";
										error.setDescription(description);
										error_list.add(error);
										stop_verification = true;
									}
									if (!stop_verification) {
										// # check for primary key redundancy
										// @ Error type 3 --> Primary key
										// redundancy
										for (Integer key : primary_keys_list) {
											if (id == key) {
												ErrorBean error = new ErrorBean();
												error.setType(3);
												error.setLine_nb(nb_read_rows);
												error.setColumn_nb(column_index + 1);
												error.setText(nextLine[column_index]);
												String description = "Redondance de clé primaire: la clé primaire '"
														+ id + "' existe déja.";
												error.setDescription(description);
												error_list.add(error);
												stop_verification = true;
											}
											if (stop_verification)
												break;
										}
										if (!stop_verification)
											primary_keys_list.add(id);
									}
								}
							}
							if (!stop_verification) {
								// # verify if data can be null
								// @ Error type 4 --> data is null when it's not
								// declared to be allowed to have null value
								Boolean nullable = column.getNullable();
								if (read_value == null && !nullable) {
									ErrorBean error = new ErrorBean();
									error.setType(4);
									error.setLine_nb(nb_read_rows);
									error.setColumn_nb(column_index + 1);
									error.setText(nextLine[column_index]);
									String description = "Cette valeur ne peut pas  être null.";
									error.setDescription(description);
									error_list.add(error);
									stop_verification = true;
								}
							}
							if (!stop_verification) {
								// # Verify data type
								// @ Error type 5 --> Data type mismatch
								int type_value = column.getSelectedType();
								switch (type_value) {
								case 1:
									// String type
									break;
								case 2:
									try {
										new BigDecimal(read_value);
									} catch (NumberFormatException e) {
										ErrorBean error = new ErrorBean();
										error.setType(5);
										error.setLine_nb(nb_read_rows);
										error.setColumn_nb(column_index + 1);
										error.setText(read_value);
										String description = "Type de donnée incompatible. Type de données attendu: BigDecimal";
										error.setDescription(description);
										error_list.add(error);
										stop_verification = true;
									}
									break;
								case 3:
									// Boolean type
									Boolean.valueOf(read_value);
									break;
								case 4:
									// Byte type
									break;
								case 5:
									try {
										Short.valueOf(read_value);
									} catch (NumberFormatException e) {
										ErrorBean error = new ErrorBean();
										error.setType(5);
										error.setLine_nb(nb_read_rows);
										error.setColumn_nb(column_index + 1);
										error.setText(read_value);
										String description = "Type de donnée incompatible. Type de données attendu: Short";
										error.setDescription(description);
										error_list.add(error);
										stop_verification = true;
									}
									break;
								case 6:
									try {
										Integer.parseInt(read_value);
									} catch (NumberFormatException e) {
										ErrorBean error = new ErrorBean();
										error.setType(5);
										error.setLine_nb(nb_read_rows);
										error.setColumn_nb(column_index + 1);
										error.setText(read_value);
										String description = "Type de donnée incompatible. Type de données attendu: Integer";
										error.setDescription(description);
										error_list.add(error);
										stop_verification = true;
									}
									break;
								case 7:
									try {
										Long.parseLong(read_value);
									} catch (NumberFormatException e) {
										ErrorBean error = new ErrorBean();
										error.setType(5);
										error.setLine_nb(nb_read_rows);
										error.setColumn_nb(column_index + 1);
										error.setText(read_value);
										String description = "Type de donnée incompatible. Type de données attendu: Long";
										error.setDescription(description);
										error_list.add(error);
										stop_verification = true;
									}
									break;
								case 8:
									try {
										Float.valueOf(read_value);
									} catch (NumberFormatException e) {
										ErrorBean error = new ErrorBean();
										error.setType(5);
										error.setLine_nb(nb_read_rows);
										error.setColumn_nb(column_index + 1);
										error.setText(read_value);
										String description = "Type de donnée incompatible. Type de données attendu: Float";
										error.setDescription(description);
										error_list.add(error);
										stop_verification = true;
									}
									break;
								case 9:
									try {
										Double.valueOf(read_value);
									} catch (NumberFormatException e) {
										ErrorBean error = new ErrorBean();
										error.setType(5);
										error.setLine_nb(nb_read_rows);
										error.setColumn_nb(column_index + 1);
										error.setText(read_value);
										String description = "Type de donnée incompatible. Type de données attendu: Float";
										error.setDescription(description);
										error_list.add(error);
										stop_verification = true;
									}
									break;
								case 10:
									// Array of bytes type
									break;
								case 11:
									try {
										Date.valueOf(read_value);
									} catch (IllegalArgumentException e) {
										ErrorBean error = new ErrorBean();
										error.setType(5);
										error.setLine_nb(nb_read_rows);
										error.setColumn_nb(column_index + 1);
										error.setText(read_value);
										String description = "Type de donnée incompatible. Type de données attendu: Date ( format \"yyyy-mm-dd\")";
										error.setDescription(description);
										error_list.add(error);
										stop_verification = true;
									}
									break;
								case 12:
									// Time type
									break;
								case 13:
									try {
										Timestamp.valueOf(read_value);
									} catch (IllegalArgumentException e) {
										ErrorBean error = new ErrorBean();
										error.setType(5);
										error.setLine_nb(nb_read_rows);
										error.setColumn_nb(column_index + 1);
										error.setText(read_value);
										String description = "Type de donnée incompatible. Type de données attendu: Timestamp ( format \"yyyy-mm-dd hh:mm:ss\")";
										error.setDescription(description);
										error_list.add(error);
										stop_verification = true;
									}
									break;
								}
							}
							if (!stop_verification) {
								// # Verify data length
								// @ Error type 6 --> Maximum data length
								// exceeded
								Integer maximum_length = null;
								if (column.getLenght() != null) {
									maximum_length = column.getLenght();
									if (read_value.length() > maximum_length) {
										ErrorBean error = new ErrorBean();
										error.setType(6);
										error.setLine_nb(nb_read_rows);
										error.setColumn_nb(column_index + 1);
										error.setText(read_value);
										String description = "La longueur maximale autorisée pour cette colonne ("+ maximum_length + ") a été dépassée.";
										error.setDescription(description);
										error_list.add(error);
										stop_verification = true;
									}
								}
							}
							if (!stop_verification && (column.getMin_value() != null  && column.getMax_value() != null)) {
								// # verify if value is out of the allowed
								// interval
								// @ Error type 7 --> Value out of interval
								Integer min_value = column.getMin_value();
								Integer max_value = column.getMax_value();
								Double temp = Double.valueOf(read_value);
								if (temp > max_value || temp < min_value) {
									ErrorBean error = new ErrorBean();
									error.setType(7);
									error.setLine_nb(nb_read_rows);
									error.setColumn_nb(column_index + 1);
									error.setText(read_value);
									String description = "Valeur dépasse l'intervalle autorisé. La valeur doit être dans l'intervalle [" + min_value +","+  max_value+"]";
									error.setDescription(description);
									error_list.add(error);
									stop_verification = true;
								}
							}
						}
					}
				}
				// exit if number of line limit reached
				if (limit_by_line_number && (nb_lines_to_limit != 0)) {
					if (nb_processed_rows == nb_lines_to_limit)
						stop = true;
				}
			}
			return error_list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
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
	
	public List<ErrorBean> getPrimaryKeyNullErrors(List<ErrorBean> errorList) throws BusinessLayerException {
		try {
			List<ErrorBean> temp_error_list = new ArrayList<ErrorBean>();
			for (ErrorBean error : errorList) {
				if (error.getType() == 1)
					temp_error_list.add(error);
			}
			return temp_error_list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}
	
	public List<ErrorBean> getPKRedundancyErrors(List<ErrorBean> errorList) throws BusinessLayerException {
		try {
			List<ErrorBean> temp_error_list = new ArrayList<ErrorBean>();
			for (ErrorBean error : errorList) {
				if (error.getType() == 3)
					temp_error_list.add(error);
			}
			return temp_error_list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}
	
	public List<ErrorBean> getTypeMismatchErrors(List<ErrorBean> errorList) throws BusinessLayerException {
		try {
			List<ErrorBean> temp_error_list = new ArrayList<ErrorBean>();
			for (ErrorBean error : errorList) {
				if (error.getType() == 5)
					temp_error_list.add(error);
			}
			return temp_error_list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}
	
	public List<ErrorBean> getMaxLenghtErrors(List<ErrorBean> errorList) throws BusinessLayerException {
		try {
			List<ErrorBean> temp_error_list = new ArrayList<ErrorBean>();
			for (ErrorBean error : errorList) {
				if (error.getType() == 6)
					temp_error_list.add(error);
			}
			return temp_error_list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}
	
	public List<ErrorBean> getOutofIntervalErrors(List<ErrorBean> errorList) throws BusinessLayerException {
		try {
			List<ErrorBean> temp_error_list = new ArrayList<ErrorBean>();
			for (ErrorBean error : errorList) {
				if (error.getType() == 7)
					temp_error_list.add(error);
			}
			return temp_error_list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}
}
