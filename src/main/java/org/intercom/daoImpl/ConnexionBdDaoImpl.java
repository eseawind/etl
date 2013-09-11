package org.intercom.daoImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.intercom.dao.ConnexionBdDao;
import org.intercom.dao.SgbdDao;
import org.intercom.dao.exceptions.DAOLayerException;
import org.intercom.domain.ConnexionBd;
import org.intercom.domain.Sgbd;
import org.intercom.model.ConnectionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ConnexionBdDaoImpl implements ConnexionBdDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	SgbdDao sgbdDao;

	@Transactional(readOnly = true)
	public Boolean find(ConnectionBean connectionBean) throws DAOLayerException {
		try {
			String nomConnexionBd = connectionBean.getConnectionName();
			String nomBaseDonnees = connectionBean.getDatabaseName();
			String nomUtilisateur = connectionBean.getUsername();
			String motDePasse = connectionBean.getPassword();
			String urlConnexion = connectionBean.getConnection_url();
			Integer sgbdId = connectionBean.getSgbdSelected();

			BigDecimal id = new BigDecimal((double) sgbdId);
			Sgbd sgbd = sgbdDao.findById(id);
			String str_query = "SELECT c FROM ConnexionBd c WHERE (";
			str_query += "(c.nomConnexionBd = :nomConnexionBd AND c.nomBaseDonnees = :nomBaseDonnees)";
			str_query += " AND (c.nomUtilisateur = :nomUtilisateur AND c.motDePasse = :motDePasse)";
			str_query += " AND (c.urlConnexion = :urlConnexion  AND c.sgbd = :sgbd)";
			str_query += " )";
			TypedQuery<ConnexionBd> query = entityManager.createQuery(
					str_query, ConnexionBd.class);
			query.setParameter("nomConnexionBd", nomConnexionBd);
			query.setParameter("nomBaseDonnees", nomBaseDonnees);
			query.setParameter("nomUtilisateur", nomUtilisateur);
				query.setParameter("motDePasse", motDePasse);
			query.setParameter("urlConnexion", urlConnexion);
			query.setParameter("sgbd", sgbd);
			List<ConnexionBd> results = query.getResultList();
			if (results.size() > 0)
				return true;
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	public void saveConnection(ConnectionBean connectionBean)
			throws DAOLayerException {
		try {
			String nomConnexionBd = connectionBean.getConnectionName();
			String nomBaseDonnees = connectionBean.getDatabaseName();
			String nomUtilisateur = connectionBean.getUsername();
			String motDePasse = connectionBean.getPassword();
			String urlConnexion = connectionBean.getConnection_url();
			Integer sgbdId = connectionBean.getSgbdSelected();
			BigDecimal id = new BigDecimal((double) sgbdId);
			Sgbd sgbd = sgbdDao.findById(id);
			ConnexionBd connexionBd = new ConnexionBd();
			connexionBd.setNomConnexionBd(nomConnexionBd);
			connexionBd.setNomBaseDonnees(nomBaseDonnees);
			connexionBd.setNomUtilisateur(nomUtilisateur);
			connexionBd.setMotDePasse(motDePasse);
			connexionBd.setUrlConnexion(urlConnexion);
			connexionBd.setSgbd(sgbd);
			save(connexionBd);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	public void save(ConnexionBd connexionBd) throws DAOLayerException {
		try {
			entityManager.persist(connexionBd);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	private void remove(ConnexionBd connexionBd) throws DAOLayerException {
		try {
			entityManager.remove(connexionBd);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	private List<ConnexionBd> findAll() throws DAOLayerException {
		try {
			return entityManager.createQuery("select e from ConnexionBd e",
					ConnexionBd.class).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	public List<ConnectionBean> getAll() throws DAOLayerException {
		try {
			List<ConnectionBean> dbConnectionList = new ArrayList<ConnectionBean>();
			List<ConnexionBd> connectionList = findAll();
			for (ConnexionBd connection : connectionList) {
				ConnectionBean connectionBean = new ConnectionBean();
				String connectionName = connection.getNomConnexionBd();
				String databaseName = connection.getNomBaseDonnees();
				String sgbdType = connection.getSgbd().getType();
				String username = connection.getNomUtilisateur();
				String password = connection.getMotDePasse();
				String connection_url = connection.getUrlConnexion();
				String delete_url = "db_delete?id="
						+ connection.getIdConnexionBd();
				String edit_url = "connection_edit?id="
						+ connection.getIdConnexionBd();
				connectionBean.setConnection_url(connection_url);
				connectionBean.setConnectionName(connectionName);
				connectionBean.setDatabaseName(databaseName);
				connectionBean.setDelete_url(delete_url);
				connectionBean.setEdit_url(edit_url);
				connectionBean.setPassword(password);
				connectionBean.setSgbdType(sgbdType);
				connectionBean.setUsername(username);
				dbConnectionList.add(connectionBean);
			}
			return dbConnectionList;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	public void deleteById(BigDecimal id) throws DAOLayerException {
		try {
			ConnexionBd connection = findById(id);
			remove(connection);
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	public ConnectionBean getById(BigDecimal id) throws DAOLayerException {
		try {
			ConnexionBd db_connection = findById(id);
			ConnectionBean connection = new ConnectionBean();
			Integer id_conneciton = db_connection.getIdConnexionBd().intValue();
			String connectionName = db_connection.getNomConnexionBd();
			String databaseName = db_connection.getNomBaseDonnees();
			String username = db_connection.getNomUtilisateur();
			String connection_url = db_connection.getUrlConnexion();
			Integer sgbdSelected = db_connection.getSgbd().getIdSgbd()
					.intValue();
			String password = db_connection.getMotDePasse();
			if (password != null)
				connection.setPassword(password);
			connection.setPassword(password);
			connection.setId(id_conneciton);
			connection.setConnection_url(connection_url);
			connection.setConnectionName(connectionName);
			connection.setDatabaseName(databaseName);
			connection.setId(id_conneciton);
			connection.setSgbdSelected(sgbdSelected);
			connection.setUsername(username);
			return connection;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	private ConnexionBd findById(BigDecimal id) throws DAOLayerException {
		try {
			return entityManager.find(ConnexionBd.class, id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	public void update(ConnectionBean connection) throws DAOLayerException {
		try {
			BigDecimal id = new BigDecimal((double) connection.getId());
			ConnexionBd connectionEntity = findById(id);
			String nomConnexionBd = connection.getConnectionName();
			String nomBaseDonnees = connection.getDatabaseName();
			String nomUtilisateur = connection.getUsername();
			String motDePasse = connection.getPassword();
			String urlConnexion = connection.getConnection_url();
			Integer sgbdId = connection.getSgbdSelected();
			Sgbd sgbd = sgbdDao.findById(new BigDecimal((double) sgbdId));
			connectionEntity.setMotDePasse(motDePasse);
			connectionEntity.setNomBaseDonnees(nomBaseDonnees);
			connectionEntity.setNomConnexionBd(nomConnexionBd);
			connectionEntity.setNomUtilisateur(nomUtilisateur);
			connectionEntity.setSgbd(sgbd);
			connectionEntity.setUrlConnexion(urlConnexion);
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	public Map<Integer, String> getDBConnectionSelectionList()
			throws DAOLayerException {
		try {
			Map<Integer, String> hashMap = new LinkedHashMap<Integer, String>();
			List<ConnexionBd> entityList = findAll();
			for (ConnexionBd connexionBd : entityList) {
				Integer id = connexionBd.getIdConnexionBd().intValue();
				String connection_name = connexionBd.getNomConnexionBd();
				hashMap.put(id, connection_name);
			}
			return hashMap;
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

}
