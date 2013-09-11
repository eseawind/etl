package org.intercom.dao;


import java.util.List;

import org.intercom.domain.ParametreApplication;


public interface ParametreApplicationDao {
	public void save(ParametreApplication parametreApplication);
	public void remove(ParametreApplication parametreApplication);
	public List<ParametreApplication> findAll();
	public ParametreApplication findById(Long id);
}
