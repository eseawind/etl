package org.intercom.daoImpl;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.intercom.dao.SgbdDao;
import org.intercom.dao.exceptions.DAOLayerException;
import org.intercom.domain.Sgbd;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class SgbdDaoImpl implements SgbdDao {

	@PersistenceContext
	private EntityManager entityManager;

	public void save(Sgbd sgbd) throws DAOLayerException {
		try {
			entityManager.persist(sgbd);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	public void remove(Sgbd sgbd) throws DAOLayerException {
		try {
			entityManager.remove(sgbd);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	public List<Sgbd> findAll() throws DAOLayerException {
		try {
			return entityManager.createQuery("select e from Sgbd e", Sgbd.class)
					.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	public Sgbd findById(BigDecimal id) throws DAOLayerException {
		try {
			return entityManager.find(Sgbd.class, id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	public Map<Integer, String> getList() throws DAOLayerException {
		try {
			Map<Integer, String> hashMap = new LinkedHashMap<Integer, String>();
			List<Sgbd> list = entityManager.createQuery("select e from Sgbd e",
					Sgbd.class).getResultList();
			for (Sgbd item : list) {
				Integer id = item.getIdSgbd().intValue();
				String type = item.getType();
				hashMap.put(id, type);
			}
			return hashMap;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	public String getclassName(Integer id) throws DAOLayerException {
		try {
			BigDecimal sgbd_id = new BigDecimal((double) id);
			Sgbd sgbd = findById(sgbd_id);
			return sgbd.getDriverClassName();
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}
}
