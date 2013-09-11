package org.intercom.serviceImpl;

import java.math.BigDecimal;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.intercom.dao.ConnexionBdDao;
import org.intercom.dao.SgbdDao;
import org.intercom.dao.exceptions.DAOLayerException;
import org.intercom.model.ConnectionBean;
import org.intercom.service.DBconnectService;
import org.intercom.service.exceptions.BusinessLayerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysql.jdbc.Driver;

@Service
public class DBconnectServiceImpl implements DBconnectService {

	@Autowired
	ConnexionBdDao connexionBdDao;
	@Autowired
	SgbdDao sgbdDao;

	private Map<Integer, String> sgbdList = new LinkedHashMap<Integer, String>();

	public void prepareConnectionBean(ConnectionBean connectionBean) throws BusinessLayerException {
		try {
			sgbdList = sgbdDao.getList();
			connectionBean.setSgbdList(sgbdList);
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}
	
	public Connection getConnection(ConnectionBean connectionBean) {
		Connection connection = null;
		String conUrl = connectionBean.getConnection_url();
		String username = connectionBean.getUsername();
		String password = connectionBean.getPassword();
		try {
			 connection = DriverManager.getConnection(conUrl, username, password);
			return connection;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Boolean connectToDB(ConnectionBean connectionBean) {
		Connection connection = null;
		String conUrl = connectionBean.getConnection_url();
		String username = connectionBean.getUsername();
		String password = connectionBean.getPassword();
		try {
			 connection = DriverManager.getConnection(conUrl, username, password);
			if (connection == null)
				return false;
			connection.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void saveDBConnection(ConnectionBean connectionBean)
			throws BusinessLayerException {
		try {
			connexionBdDao.saveConnection(connectionBean);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	public Boolean exists(ConnectionBean connectionBean)
			throws BusinessLayerException {
		try {
			return connexionBdDao.find(connectionBean);
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	public List<ConnectionBean> getConnextions() throws BusinessLayerException {
		try {
			return connexionBdDao.getAll();
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	public void deleteById(Integer id) throws BusinessLayerException {
		try {
			connexionBdDao.deleteById(new BigDecimal((double) id));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	public ConnectionBean getById(Integer id) throws BusinessLayerException {
		try {
			ConnectionBean connection = connexionBdDao.getById(new BigDecimal(
					(double) id));
			prepareConnectionBean(connection);
			return connection;
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	public void update(ConnectionBean connectionBean)
			throws BusinessLayerException {
		try {
			connexionBdDao.update(connectionBean);
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	public Map<Integer, String> getDBConnectionList()
			throws BusinessLayerException {
		try {
			return connexionBdDao.getDBConnectionSelectionList();
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	public Map<Integer, String> getTablesFromDatabase(
			ConnectionBean connectionBean) throws BusinessLayerException {
		Connection connection = null;
		String conUrl = connectionBean.getConnection_url();
		String username = connectionBean.getUsername();
		String password = connectionBean.getPassword();
		Map<Integer, String> hashMap = new LinkedHashMap<Integer, String>();
		try {
			 connection = DriverManager.getConnection(conUrl, username, password);
			if (!(connection == null)) {
				DatabaseMetaData metadata = connection.getMetaData();
				String[] TABLE_TYPES = { "TABLE" };
				ResultSet tables = metadata.getTables(null, null, null,
						TABLE_TYPES);
				int i = 0;
				while (tables.next()) {
					i++;
					String table_name = tables.getString("TABLE_NAME");
					hashMap.put(i, table_name);
				}
				connection.close();
			}
			return hashMap;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	public Map<Integer, String> getColumnsFromDatabaseTable(
			ConnectionBean connectionBean, String table)
			throws BusinessLayerException {
		Connection connection = null;
		String conUrl = connectionBean.getConnection_url();
		String username = connectionBean.getUsername();
		String password = connectionBean.getPassword();
		Map<Integer, String> hashMap = new LinkedHashMap<Integer, String>();
		try {
			 connection = DriverManager.getConnection(conUrl, username, password);
			if (!(connection == null)) {
				DatabaseMetaData metadata = connection.getMetaData();
				ResultSet resultSet = metadata.getColumns(null, null, table,
						null);
				int i = 0;
				while (resultSet.next()) {
					i++;
					String column_name = resultSet.getString("COLUMN_NAME");
					hashMap.put(i, column_name);
				}
				connection.close();
			}
			return hashMap;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	// ********************** Driver File Functions

	public void connectToDBDynamicly(URL FileUrl, String conUrl, String dbname,
			String username, String password) {
		Connection connection = null;
		try {
			// String tempURL = "jar:file:/"+FileUrl+"!/";
			URLClassLoader ucl = new URLClassLoader(new URL[] { FileUrl });

			String classname = "com.mysql.jdbc.Driver";
			Driver d = (Driver) Class.forName(classname, true, ucl)
					.newInstance();

			DriverManager.registerDriver(d);
			connection = DriverManager
					.getConnection("jdbc:mysql://localhost:3307/hibernate_db1",
							"root", "root");
			System.out.println("Connected to the database");

			DatabaseMetaData metadata = connection.getMetaData();
			ResultSet resultSet = metadata.getColumns(null, null, "personne",
					null);
			while (resultSet.next()) {
				String name = resultSet.getString("COLUMN_NAME");
				String type = resultSet.getString("TYPE_NAME");
				int size = resultSet.getInt("COLUMN_SIZE");

				System.out.println("Column name: [" + name + "]; type: ["
						+ type + "]; size: [" + size + "]");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println("Disconnected from database");
		}
	}
	// *************************************

}
