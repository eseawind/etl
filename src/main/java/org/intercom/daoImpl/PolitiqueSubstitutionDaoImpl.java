package org.intercom.daoImpl;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.intercom.dao.PolitiqueSubstitusionDao;
import org.intercom.domain.PolitiqueSubstitusion;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class PolitiqueSubstitutionDaoImpl implements PolitiqueSubstitusionDao {

    @PersistenceContext 
    private EntityManager entityManager;
    
	public void save(PolitiqueSubstitusion politiqueSubstitusion) {
		entityManager.persist(politiqueSubstitusion);
	}

	public void remove(PolitiqueSubstitusion politiqueSubstitusion) {
		entityManager.remove(politiqueSubstitusion);
		
	}

	public List<PolitiqueSubstitusion> findAll() {
		return entityManager.createQuery("select e from PolitiqueSubstitusion e", PolitiqueSubstitusion.class).getResultList();
	}

	public PolitiqueSubstitusion findById(Long id) {
		return entityManager.find(PolitiqueSubstitusion.class, id);
	}

}
