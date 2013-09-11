package org.intercom.service;

import java.util.List;
import java.util.Map;

import org.intercom.model.ConfigurationBean;
import org.intercom.model.IntervalSelectionBean;
import org.intercom.service.exceptions.BusinessLayerException;

public interface ConfigurationService {
	public void saveInterval(IntervalSelectionBean intervalBean)
			throws BusinessLayerException;

	public void prepare(ConfigurationBean configurationBean)
			throws BusinessLayerException;

	public void saveConfigurationBean(ConfigurationBean configurationBean)
			throws BusinessLayerException;

	public Boolean findSimilarBeans(ConfigurationBean configurationBean)
			throws BusinessLayerException;

	public List<ConfigurationBean> getConfigurationsList()
			throws BusinessLayerException;

	public void deleteById(Integer id) throws BusinessLayerException;

	public ConfigurationBean getById(Integer id) throws BusinessLayerException;

	public void update(ConfigurationBean configuration)
			throws BusinessLayerException;

	public Map<Integer, String> getConfigurationSelectionList()
			throws BusinessLayerException;
}
