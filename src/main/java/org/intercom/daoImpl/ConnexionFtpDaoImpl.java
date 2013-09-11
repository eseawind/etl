package org.intercom.daoImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.intercom.dao.ConnexionFtpDao;
import org.intercom.dao.exceptions.DAOLayerException;
import org.intercom.domain.ConnexionFtp;
import org.intercom.model.FTPConnectionBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ConnexionFtpDaoImpl implements ConnexionFtpDao {

	@PersistenceContext
	private EntityManager entityManager;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ConnexionFtpDao.class);

	private void save(ConnexionFtp ftpConnection) throws DAOLayerException {
		try {
			entityManager.persist(ftpConnection);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	private void remove(ConnexionFtp ftpConnection) throws DAOLayerException {
		try {
			entityManager.remove(ftpConnection);
		} catch (Exception e) {
			LOGGER.error("Error in deleting entity");
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	private ConnexionFtp findById(BigDecimal id) throws DAOLayerException {
		try {
			return entityManager.find(ConnexionFtp.class, id);
		} catch (Exception e) {
			LOGGER.error("Error in getting entity by id");
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	private List<ConnexionFtp> findAll() throws DAOLayerException {
		try {
			return entityManager.createQuery("select c from ConnexionFtp c",
					ConnexionFtp.class).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	@Transactional(readOnly = true)
	public Boolean find(FTPConnectionBean ftpConnection)
			throws DAOLayerException {
		try {
			String nomConnexionFtp = ftpConnection.getConnectionName();
			String adresseServeurFtp = ftpConnection.getFtpAddress();
			String nomUtilisateur = ftpConnection.getUsername();
			String motDePasse = ftpConnection.getPassword();
			BigDecimal port_number = new BigDecimal(
					(double) ftpConnection.getPort());

			String str_query = "SELECT c FROM ConnexionFtp c WHERE (";
			str_query += "(c.nomConnexionFtp = :nomConnexionFtp AND c.adresseServeurFtp = :adresseServeurFtp)";
			str_query += " AND (c.nomUtilisateur = :nomUtilisateur AND c.motDePasse = :motDePasse)";
			str_query += " AND (c.numeroPort = :numeroPort)";
			str_query += " )";
			TypedQuery<ConnexionFtp> query = entityManager.createQuery(
					str_query, ConnexionFtp.class);
			query.setParameter("nomConnexionFtp", nomConnexionFtp);
			query.setParameter("adresseServeurFtp", adresseServeurFtp);
			query.setParameter("nomUtilisateur", nomUtilisateur);
			if (motDePasse.isEmpty())
				query.setParameter("motDePasse", " ");
			else
				query.setParameter("motDePasse", motDePasse);
			query.setParameter("numeroPort", port_number);
			List<ConnexionFtp> results = query.getResultList();
			if (results.size() > 0)
				return true;
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	public void saveFTPConnection(FTPConnectionBean ftpConnection)
			throws DAOLayerException {
		try {
			String nomConnexionFtp = ftpConnection.getConnectionName();
			String adresseServeurFtp = ftpConnection.getFtpAddress();
			String nomUtilisateur = ftpConnection.getUsername();
			String motDePasse = ftpConnection.getPassword();
			BigDecimal port_number = new BigDecimal(
					(double) ftpConnection.getPort());

			ConnexionFtp connection = new ConnexionFtp(nomConnexionFtp,
					adresseServeurFtp, nomUtilisateur, motDePasse, port_number);
			save(connection);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	public List<FTPConnectionBean> getAll() throws DAOLayerException {
		try {
			List<FTPConnectionBean> ftpConnectionList = new ArrayList<FTPConnectionBean>();
			List<ConnexionFtp> entityList = findAll();
			for (ConnexionFtp connection : entityList) {
				FTPConnectionBean connectionBean = new FTPConnectionBean();
				Integer id = connection.getId().intValue();
				String connectionName = connection.getNomConnexionFtp();
				String ftpAddress = connection.getAdresseServeurFtp();
				String username = connection.getNomUtilisateur();
				String password = connection.getMotDePasse();
				Integer port = connection.getNumeroPort().intValue();
				String delete_url = "ftp_delete?id=" + connection.getId();
				String edit_url = "ftp_edit?id=" + connection.getId();
				
				connectionBean.setId(id);
				connectionBean.setConnectionName(connectionName);
				connectionBean.setFtpAddress(ftpAddress);
				connectionBean.setUsername(username);
				connectionBean.setPassword(password);
				connectionBean.setPort(port);
				connectionBean.setDelete_url(delete_url);
				connectionBean.setEdit_url(edit_url);
				ftpConnectionList.add(connectionBean);
			}
			return ftpConnectionList;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	public void deleteById(Integer id) throws DAOLayerException {
		try {
			BigDecimal connection_id = new BigDecimal((double) id);
			ConnexionFtp ftpconnection = findById(connection_id);
			remove(ftpconnection);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	public FTPConnectionBean getById(Integer id) throws DAOLayerException {
		BigDecimal connection_id = new BigDecimal((double) id);
		try {
			ConnexionFtp ftpConnection = findById(connection_id);
			FTPConnectionBean ftpConnectionBean = new FTPConnectionBean();
			ftpConnectionBean.setId(id);
			ftpConnectionBean.setConnectionName(ftpConnection
					.getNomConnexionFtp());
			ftpConnectionBean.setFtpAddress(ftpConnection
					.getAdresseServeurFtp());
			ftpConnectionBean.setUsername(ftpConnection.getNomUtilisateur());

			String password = ftpConnection.getMotDePasse();
			if (!(password == null))
				ftpConnectionBean.setPassword(password);
			ftpConnectionBean.setPort(ftpConnection.getNumeroPort().intValue());
			return ftpConnectionBean;
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	public void update(FTPConnectionBean ftpConnectionBean)
			throws DAOLayerException {
		try {
			BigDecimal id = new BigDecimal((double) ftpConnectionBean.getId());
			ConnexionFtp connectionEntity = findById(id);
			connectionEntity.setNomConnexionFtp(ftpConnectionBean
					.getConnectionName());
			connectionEntity.setAdresseServeurFtp(ftpConnectionBean
					.getFtpAddress());
			connectionEntity.setNomUtilisateur(ftpConnectionBean.getUsername());
			connectionEntity.setMotDePasse(ftpConnectionBean.getPassword());
			BigDecimal numero_port = new BigDecimal(
					(double) ftpConnectionBean.getPort());
			connectionEntity.setNumeroPort(numero_port);
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

}
