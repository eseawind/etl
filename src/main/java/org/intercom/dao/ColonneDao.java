package org.intercom.dao;

import org.intercom.dao.exceptions.DAOLayerException;
import org.intercom.domain.Colonne;

public interface ColonneDao {
	public void save(Colonne colonne) throws DAOLayerException;

	public void remove(Colonne colonne) throws DAOLayerException;
}
