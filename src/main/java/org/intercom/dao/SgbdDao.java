package org.intercom.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.intercom.dao.exceptions.DAOLayerException;
import org.intercom.domain.Sgbd;

public interface SgbdDao {
	public void save(Sgbd sgbd) throws DAOLayerException;

	public void remove(Sgbd sgbd) throws DAOLayerException;

	public List<Sgbd> findAll() throws DAOLayerException;

	public Sgbd findById(BigDecimal id) throws DAOLayerException;

	public Map<Integer, String> getList() throws DAOLayerException;

	public String getclassName(Integer id) throws DAOLayerException;
}
