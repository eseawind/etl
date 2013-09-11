package org.intercom.daoImpl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.intercom.dao.ColonneDao;
import org.intercom.dao.exceptions.DAOLayerException;
import org.intercom.domain.Colonne;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ColonneDaoImpl implements ColonneDao {

	@PersistenceContext
	private EntityManager entityManager;

	public void save(Colonne colonne) throws DAOLayerException {
		try {
			entityManager.persist(colonne);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	public void remove(Colonne colonne) throws DAOLayerException {
		try {
			entityManager.remove(colonne);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	private List<Colonne> findAll() throws DAOLayerException {
		try {
			return entityManager.createQuery("select e from Colonne e",
					Colonne.class).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	private Colonne findById(Long id) throws DAOLayerException {
		try {
			return entityManager.find(Colonne.class, id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}
}
