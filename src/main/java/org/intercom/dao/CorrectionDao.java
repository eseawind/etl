package org.intercom.dao;


import java.util.List;

import org.intercom.domain.Correction;


public interface CorrectionDao {
	public void save(Correction correction);
	public void remove(Correction correction);
	public List<Correction> findAll();
	public Correction findById(Long id);
}
