package org.intercom.dao;

import java.util.List;

import org.intercom.dao.exceptions.DAOLayerException;
import org.intercom.model.FTPConnectionBean;

public interface ConnexionFtpDao {

	public Boolean find(FTPConnectionBean ftpConnection)
			throws DAOLayerException;

	public void saveFTPConnection(FTPConnectionBean ftpConnection)
			throws DAOLayerException;

	public List<FTPConnectionBean> getAll() throws DAOLayerException;

	public void deleteById(Integer id) throws DAOLayerException;

	public FTPConnectionBean getById(Integer id) throws DAOLayerException;

	public void update(FTPConnectionBean ftpConnectionBean)
			throws DAOLayerException;
}
