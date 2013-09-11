package org.intercom.daoImpl;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.intercom.dao.DroitSystemeDao;
import org.intercom.dao.exceptions.DAOLayerException;
import org.intercom.domain.DroitSysteme;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class DroitSystemeDaoImpl implements DroitSystemeDao {

	@PersistenceContext
	private EntityManager entityManager;

	private List<DroitSysteme> findAll() throws DAOLayerException {
		try {
			return entityManager.createQuery(
					"select u from DroitSysteme u ORDER BY u.id",
					DroitSysteme.class).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	public Map<Integer, String> getDroitSystemeList() throws DAOLayerException {
		Map<Integer, String> map_droits_acces = new LinkedHashMap<Integer, String>();
		try {
			List<DroitSysteme> list_droits_acces = findAll();
			for (DroitSysteme droits_systeme : list_droits_acces) {
				map_droits_acces.put(droits_systeme.getIdDroitSysteme()
						.intValue(), droits_systeme.getLabel());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
		return map_droits_acces;
	}

	public DroitSysteme findById(BigDecimal id) throws DAOLayerException {
		try {
			return entityManager.find(DroitSysteme.class, id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	public void remove(DroitSysteme droit_systeme) throws DAOLayerException {
		try {
			entityManager.remove(droit_systeme);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}
}
