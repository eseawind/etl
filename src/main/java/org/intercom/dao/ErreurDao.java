package org.intercom.dao;


import java.util.List;

import org.intercom.domain.Erreur;


public interface ErreurDao {
	public void save(Erreur erreur);
	public void remove(Erreur erreur);
	public List<Erreur> findAll();
	public Erreur findById(Long id);
}
