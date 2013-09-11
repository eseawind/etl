package org.intercom.daoImpl;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.intercom.dao.SelectionParIntervalleDao;
import org.intercom.dao.exceptions.DAOLayerException;
import org.intercom.domain.SelectionParIntervalle;
import org.intercom.model.IntervalSelectionBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class SelectionParIntervalleDaoImpl implements SelectionParIntervalleDao {

	@PersistenceContext
	private EntityManager entityManager;

	public void saveToDB(IntervalSelectionBean intervalSelectionBean)
			throws DAOLayerException {
		BigDecimal numLigneDepart = new BigDecimal(
				(double) intervalSelectionBean.getStart_line_nb());
		BigDecimal numLigneFin = new BigDecimal(
				(double) intervalSelectionBean.getEnd_line_nb());
		SelectionParIntervalle selectionParIntervalle = new SelectionParIntervalle();
		selectionParIntervalle.setNumLigneDepart(numLigneDepart);
		selectionParIntervalle.setNumLigneFin(numLigneFin);
		try {
			save(selectionParIntervalle);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	public void save(SelectionParIntervalle selectionParIntervalle)
			throws DAOLayerException {
		try {
			entityManager.persist(selectionParIntervalle);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	public void remove(SelectionParIntervalle selectionParIntervalle)
			throws DAOLayerException {
		try {
			entityManager.remove(selectionParIntervalle);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	private List<SelectionParIntervalle> findAll() throws DAOLayerException {
		try {
			return entityManager.createQuery(
					"select e from SelectionParIntervalle e",
					SelectionParIntervalle.class).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	private SelectionParIntervalle findById(Long id) throws DAOLayerException {
		try {
			return entityManager.find(SelectionParIntervalle.class, id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

}
