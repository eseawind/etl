package org.intercom.daoImpl;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.intercom.domain.Erreur;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ErreurDaoImpl {

    @PersistenceContext 
    private EntityManager entityManager;
    
	public void save(Erreur erreur) {
		entityManager.persist(erreur);
	}

	public void remove(Erreur erreur) {
		entityManager.remove(erreur);
		
	}

	public List<Erreur> findAll() {
		return entityManager.createQuery("select e from Erreur e", Erreur.class).getResultList();
	}

	public Erreur findById(Long id) {
		return entityManager.find(Erreur.class, id);
	}
}
