package org.intercom.dao;


import java.util.List;

import org.intercom.domain.TableBd;


public interface TableBdDao {
	public void save(TableBd tableBd);
	public void remove(TableBd tableBd);
	public List<TableBd> findAll();
	public TableBd findById(Long id);
}
