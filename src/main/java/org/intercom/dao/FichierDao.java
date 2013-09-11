package org.intercom.dao;


import java.util.List;

import org.intercom.domain.Fichier;


public interface FichierDao {
	public void save(Fichier fichier);
	public void remove(Fichier fichier);
	public List<Fichier> findAll();
	public Fichier findById(Long id);
}
