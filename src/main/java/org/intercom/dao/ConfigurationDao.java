package org.intercom.dao;

import java.util.List;
import java.util.Map;

import org.intercom.dao.exceptions.DAOLayerException;
import org.intercom.model.ConfigurationBean;

public interface ConfigurationDao {
	public void saveConfiguration(ConfigurationBean configurationBean)
			throws DAOLayerException;

	public Boolean findSimlars(ConfigurationBean configurationBean)
			throws DAOLayerException;

	public List<ConfigurationBean> getAll() throws DAOLayerException;

	public void deleteById(Integer id) throws DAOLayerException;

	public ConfigurationBean getById(Integer id) throws DAOLayerException;

	public void update(ConfigurationBean configurationBean)
			throws DAOLayerException;

	public Map<Integer, String> getConfigurationSelectionList()
			throws DAOLayerException;
}
