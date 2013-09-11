package org.intercom.dao;

import org.intercom.dao.exceptions.DAOLayerException;
import org.intercom.domain.SelectionParIntervalle;
import org.intercom.model.IntervalSelectionBean;

public interface SelectionParIntervalleDao {
	public void saveToDB(IntervalSelectionBean intervalSelectionBean)
			throws DAOLayerException;

	public void save(SelectionParIntervalle selectionParIntervalle)
			throws DAOLayerException;

	public void remove(SelectionParIntervalle selectionParIntervalle)
			throws DAOLayerException;
}
