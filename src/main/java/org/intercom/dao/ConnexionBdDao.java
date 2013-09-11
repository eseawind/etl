package org.intercom.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.intercom.dao.exceptions.DAOLayerException;
import org.intercom.model.ConnectionBean;

public interface ConnexionBdDao {
	public Boolean find(ConnectionBean connectionBean) throws DAOLayerException;

	public void saveConnection(ConnectionBean connectionBean)
			throws DAOLayerException;

	public List<ConnectionBean> getAll() throws DAOLayerException;

	public void deleteById(BigDecimal id) throws DAOLayerException;

	public ConnectionBean getById(BigDecimal id) throws DAOLayerException;

	public void update(ConnectionBean connectionBean) throws DAOLayerException;

	public Map<Integer, String> getDBConnectionSelectionList()
			throws DAOLayerException;
}