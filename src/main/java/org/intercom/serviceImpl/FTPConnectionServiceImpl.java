package org.intercom.serviceImpl;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.intercom.dao.ConnexionFtpDao;
import org.intercom.dao.exceptions.DAOLayerException;
import org.intercom.model.FTPConnectionBean;
import org.intercom.service.FTPConnectionService;
import org.intercom.service.exceptions.BusinessLayerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FTPConnectionServiceImpl implements FTPConnectionService {

	@Autowired
	ConnexionFtpDao connexionFtpDao;

	public Boolean testFTPConnection(FTPConnectionBean ftpConnection)
			throws BusinessLayerException {
		String server = ftpConnection.getFtpAddress();
		String username = ftpConnection.getUsername();
		String password = ftpConnection.getPassword();
		Integer port = ftpConnection.getPort();
		Boolean error = false;
		FTPClient ftp = new FTPClient();
		try {
			ftp.connect(server, port);
			int reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				error = true;
				ftp.disconnect();
				throw new BusinessLayerException(
						"Le serveur FTP a refusé la connexion.");
			} else {
				if (!ftp.login(username, password)) {
					ftp.logout();
					error = true;
					throw new BusinessLayerException("Authentification échoué.");
				}
			}
		} catch (IOException e) {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException f) {
					f.printStackTrace();
					throw new BusinessLayerException(f.getMessage());
				}
			}
			error = true;
			e.printStackTrace();
			throw new BusinessLayerException("Problème dans la connexion FTP.");
		}
		return !error;
	}

	public Boolean connectToFTP(FTPConnectionBean ftpConnection)
			throws BusinessLayerException {
		String server = ftpConnection.getFtpAddress();
		String username = ftpConnection.getUsername();
		String password = ftpConnection.getPassword();
		Integer port = ftpConnection.getPort();

		FTPClient client = new FTPClient();
		Boolean result = false;
		try {
			client.connect(server, port);
			result = client.login(username, password);
			client.logout();
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessLayerException("Problème dans la connexion FTP");
		} finally {
			try {
				client.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
				throw new BusinessLayerException(
						"Problème dans la connexion FTP");
			}
		}
		return result;
	}

	public Boolean exists(FTPConnectionBean ftpConnection)
			throws BusinessLayerException {
		try {
			return connexionFtpDao.find(ftpConnection);
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	public void saveFTPConnection(FTPConnectionBean ftpConnection)
			throws BusinessLayerException {
		try {
			connexionFtpDao.saveFTPConnection(ftpConnection);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	public List<FTPConnectionBean> getConnections()
			throws BusinessLayerException {
		try {
			return connexionFtpDao.getAll();
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	public void deleteById(Integer id) throws BusinessLayerException {
		try {
			connexionFtpDao.deleteById(id);
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	public FTPConnectionBean getById(Integer id) throws BusinessLayerException {
		try {
			return connexionFtpDao.getById(id);
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	public void update(FTPConnectionBean ftpConnection)
			throws BusinessLayerException {
		try {
			connexionFtpDao.update(ftpConnection);
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	public Map<Integer, String> getSelectList() throws BusinessLayerException {
		try {
			Map<Integer, String> hashMap = new LinkedHashMap<Integer, String>();
			List<FTPConnectionBean> ftpConnectionList = getConnections();
			for (FTPConnectionBean item : ftpConnectionList) {
				Integer id = item.getId();
				String name = item.getConnectionName();
				hashMap.put(id, name);
			}
			return hashMap;
		} catch (BusinessLayerException e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}
	

	public Map<Integer, String> getFTPFilesList(FTPConnectionBean ftpConnection)
			throws BusinessLayerException {
		try {
			String server = ftpConnection.getFtpAddress();
			String username = ftpConnection.getUsername();
			String password = ftpConnection.getPassword();
			Integer port = ftpConnection.getPort();
			Map<Integer, String> ftpFilesList = new LinkedHashMap<Integer, String>();
			FTPClient client = new FTPClient();
			client.connect(server, port);
			client.login(username, password);
			FTPFile[] ftpFiles = client.listFiles();
			int i = 0;
			for (FTPFile ftpFile : ftpFiles) {
				if (ftpFile.getType() == FTPFile.FILE_TYPE) {
					i++;
					String fileName = ftpFile.getName();
					ftpFilesList.put(i, fileName);
				}
			}
			client.logout();
			return ftpFilesList;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}


}
