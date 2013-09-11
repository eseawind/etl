package org.intercom.dao;

import java.util.List;
import java.util.Map;

import org.intercom.dao.exceptions.DAOLayerException;
import org.intercom.model.DataStructureBean;

public interface StructureDonneesDao {

	public void saveBean(DataStructureBean dataStructureBean)
			throws DAOLayerException;

	public List<DataStructureBean> getDataStructureList()
			throws DAOLayerException;

	public void deleteById(Integer id) throws DAOLayerException;

	public DataStructureBean getById(Integer id) throws DAOLayerException;

	public void update(DataStructureBean dataStructureBean)
			throws DAOLayerException;
	
	public Map<Integer, String> getDataStructureConnectionList()
			throws DAOLayerException;
}
