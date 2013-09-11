package org.intercom.dao;


import java.util.List;

import org.intercom.domain.PolitiqueSubstitusion;


public interface PolitiqueSubstitusionDao {
	public void save(PolitiqueSubstitusion politiqueSubstitusion);
	public void remove(PolitiqueSubstitusion politiqueSubstitusion);
	public List<PolitiqueSubstitusion> findAll();
	public PolitiqueSubstitusion findById(Long id);
}
