package org.intercom.daoImpl;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.intercom.domain.ParametreApplication;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ParametreApplicationDoaImpl {

    @PersistenceContext 
    private EntityManager entityManager;
    
	public void save(ParametreApplication parametreApplication) {
		entityManager.persist(parametreApplication);
	}

	public void remove(ParametreApplication parametreApplication) {
		entityManager.remove(parametreApplication);
		
	}

	public List<ParametreApplication> findAll() {
		return entityManager.createQuery("select e from ParametreApplication e", ParametreApplication.class).getResultList();
	}

	public ParametreApplication findById(Long id) {
		return entityManager.find(ParametreApplication.class, id);
	}

}
