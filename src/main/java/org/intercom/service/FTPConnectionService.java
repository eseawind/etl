package org.intercom.service;

import java.util.List;
import java.util.Map;

import org.intercom.model.FTPConnectionBean;
import org.intercom.service.exceptions.BusinessLayerException;

public interface FTPConnectionService {

	public Boolean testFTPConnection(FTPConnectionBean ftpConnection)
			throws BusinessLayerException;

	public Boolean connectToFTP(FTPConnectionBean ftpConnection)
			throws BusinessLayerException;

	public Boolean exists(FTPConnectionBean ftpConnection)
			throws BusinessLayerException;

	public void saveFTPConnection(FTPConnectionBean ftpConnection)
			throws BusinessLayerException;

	public List<FTPConnectionBean> getConnections()
			throws BusinessLayerException;

	public void deleteById(Integer id) throws BusinessLayerException;

	public FTPConnectionBean getById(Integer id) throws BusinessLayerException;

	public void update(FTPConnectionBean ftpConnection)
			throws BusinessLayerException;
	
	public Map<Integer, String> getSelectList() throws BusinessLayerException;
	
	public Map<Integer, String> getFTPFilesList(FTPConnectionBean ftpConnection)
			throws BusinessLayerException;
}
