package org.intercom.daoImpl;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.intercom.dao.ParametresDao;
import org.intercom.dao.exceptions.DAOLayerException;
import org.intercom.domain.Parametres;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ParametresDaoImpl implements ParametresDao {

	@PersistenceContext
	private EntityManager entityManager;

	public void save(Parametres parametres) throws DAOLayerException {
		try {
			entityManager.persist(parametres);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	public void remove(Parametres parametres) throws DAOLayerException {
		try {
			entityManager.remove(parametres);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	public List<Parametres> findAll() throws DAOLayerException {
		try {
			return entityManager.createQuery("select p from Parametres p",
					Parametres.class).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	public void deleteById(BigDecimal id) throws DAOLayerException {
		try {
			Parametres parametres = findById(id);
			remove(parametres);
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	private Parametres findById(BigDecimal id) throws DAOLayerException {
		try {
			return entityManager.find(Parametres.class, id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	public Map<Integer, String> getParamList(String param_name)
			throws DAOLayerException {
		try {
			String str_query = "SELECT p FROM Parametres p WHERE p.nom = :param_name ORDER BY p.valeur";
			TypedQuery<Parametres> query = entityManager.createQuery(str_query,
					Parametres.class);
			query.setParameter("param_name", param_name);
			List<Parametres> results = query.getResultList();
			Map<Integer, String> param_list = new LinkedHashMap<Integer, String>();
			for (Parametres parametres : results) {
				param_list.put(parametres.getValeur().intValue(),
						parametres.getLabel());
			}
			return param_list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	public String getLabel(String param_name, BigDecimal value)
			throws DAOLayerException {
		try {
			String str_query = "SELECT p FROM Parametres p WHERE (p.nom = :param_name AND p.valeur = :value)";
			TypedQuery<Parametres> query = entityManager.createQuery(str_query,
					Parametres.class);
			query.setParameter("param_name", param_name);
			query.setParameter("value", value);
			Parametres result = query.getSingleResult();
			return result.getLabel();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}
}
