package org.intercom.daoImpl;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.intercom.domain.TableBd;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class TableBdDaoImpl {

    @PersistenceContext 
    private EntityManager entityManager;
    
	public void save(TableBd tableBd) {
		entityManager.persist(tableBd);
	}

	public void remove(TableBd tableBd) {
		entityManager.remove(tableBd);
		
	}

	public List<TableBd> findAll() {
		return entityManager.createQuery("select e from TableBd e", TableBd.class).getResultList();
	}

	public TableBd findById(Long id) {
		return entityManager.find(TableBd.class, id);
	}
}
