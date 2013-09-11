package org.intercom.daoImpl;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.intercom.dao.CorrectionDao;
import org.intercom.domain.Correction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class CorrectionDaoImpl implements CorrectionDao {

    @PersistenceContext 
    private EntityManager entityManager;
    
	public void save(Correction correction) {
		entityManager.persist(correction);
	}

	public void remove(Correction correction) {
		entityManager.remove(correction);
		
	}

	public List<Correction> findAll() {
		return entityManager.createQuery("select e from Correction e", Correction.class).getResultList();
	}

	public Correction findById(Long id) {
		return entityManager.find(Correction.class, id);
	}

}
