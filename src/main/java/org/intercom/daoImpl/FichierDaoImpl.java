package org.intercom.daoImpl;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.intercom.dao.FichierDao;
import org.intercom.domain.Fichier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class FichierDaoImpl implements FichierDao {

    @PersistenceContext 
    private EntityManager entityManager;
    
	public void save(Fichier fichier) {
		entityManager.persist(fichier);
	}

	public void remove(Fichier fichier) {
		entityManager.remove(fichier);
		
	}

	public List<Fichier> findAll() {
		return entityManager.createQuery("select e from Fichier e", Fichier.class).getResultList();
	}

	public Fichier findById(Long id) {
		return entityManager.find(Fichier.class, id);
	}


}
