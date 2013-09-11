package org.intercom.dao;


import java.util.List;

import org.intercom.domain.SauveguardeTable;


public interface SauveguardeTableDao {
	public void save(SauveguardeTable sauveguardeTable);
	public void remove(SauveguardeTable sauveguardeTable);
	public List<SauveguardeTable> findAll();
	public SauveguardeTable findById(Long id);
}
