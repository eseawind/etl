package org.intercom.service;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.intercom.model.ConnectionBean;
import org.intercom.service.exceptions.BusinessLayerException;

public interface DBconnectService {
	public Connection getConnection(ConnectionBean connectionBean);

	public void prepareConnectionBean(ConnectionBean connectionBean) throws BusinessLayerException;

	public Boolean connectToDB(ConnectionBean connectionBean);

	public void saveDBConnection(ConnectionBean connectionBean)
			throws BusinessLayerException;

	public Boolean exists(ConnectionBean connectionBean)
			throws BusinessLayerException;

	public List<ConnectionBean> getConnextions() throws BusinessLayerException;

	public void deleteById(Integer id) throws BusinessLayerException;

	public ConnectionBean getById(Integer id) throws BusinessLayerException;

	public void update(ConnectionBean connectionBean)
			throws BusinessLayerException;

	public Map<Integer, String> getDBConnectionList()
			throws BusinessLayerException;

	public Map<Integer, String> getTablesFromDatabase(
			ConnectionBean connectionBean) throws BusinessLayerException;

	public Map<Integer, String> getColumnsFromDatabaseTable(
			ConnectionBean connectionBean, String table)
			throws BusinessLayerException;
}
