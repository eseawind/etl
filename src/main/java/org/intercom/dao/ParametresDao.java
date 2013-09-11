package org.intercom.dao;

import java.math.BigDecimal;
import java.util.Map;

import org.intercom.dao.exceptions.DAOLayerException;

public interface ParametresDao {
	public Map<Integer, String> getParamList(String param_name)
			throws DAOLayerException;

	public String getLabel(String param_name, BigDecimal value)
			throws DAOLayerException;
}
