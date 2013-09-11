package org.intercom.dao;

import java.math.BigDecimal;
import java.util.Map;

import org.intercom.dao.exceptions.DAOLayerException;
import org.intercom.domain.DroitSysteme;

public interface DroitSystemeDao {

	public Map<Integer, String> getDroitSystemeList() throws DAOLayerException;

	public DroitSysteme findById(BigDecimal id) throws DAOLayerException;

	public void remove(DroitSysteme droit_systeme) throws DAOLayerException;

}
