package org.intercom.daoImpl;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.intercom.domain.SauveguardeTable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class SauveguardeTableDaoImpl {

    @PersistenceContext 
    private EntityManager entityManager;
    
	public void save(SauveguardeTable sauveguardeTable) {
		entityManager.persist(sauveguardeTable);
	}

	public void remove(SauveguardeTable sauveguardeTable) {
		entityManager.remove(sauveguardeTable);
		
	}

	public List<SauveguardeTable> findAll() {
		return entityManager.createQuery("select e from SauveguardeTable e", SauveguardeTable.class).getResultList();
	}

	public SauveguardeTable findById(Long id) {
		return entityManager.find(SauveguardeTable.class, id);
	}
}
