package org.intercom.service;

import java.util.List;
import java.util.Map;

import org.intercom.model.ColumnBean;
import org.intercom.model.DataStructureBean;
import org.intercom.model.FTPConnectionBean;
import org.intercom.service.exceptions.BusinessLayerException;

public interface DataStructureService {

	public List<ColumnBean> getDataStructureFromLocalFile(String filename,
			Integer selectedConfiguration) throws BusinessLayerException;

	public List<ColumnBean> getDataStructureFromFTPFile(String filename,
			FTPConnectionBean ftpConnection, Integer selectedConfiguration)
			throws BusinessLayerException;

	public void saveDataStructure(DataStructureBean dataStructureBean)
			throws BusinessLayerException;

	public List<DataStructureBean> getDataStructureList()
			throws BusinessLayerException;

	public void deleteById(Integer id) throws BusinessLayerException;

	public DataStructureBean getById(Integer id) throws BusinessLayerException;

	public void updateDataStructureBean(DataStructureBean dataStructureBean)
			throws BusinessLayerException;

	public ColumnBean prepareColumnBean(ColumnBean columnBean)
			throws BusinessLayerException;

	public Map<Integer, String> getDataStructureSelectionList()
			throws BusinessLayerException;
}
