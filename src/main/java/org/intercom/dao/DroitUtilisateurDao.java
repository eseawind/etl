package org.intercom.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.intercom.dao.exceptions.DAOLayerException;
import org.intercom.domain.DroitUtilisateur;
import org.intercom.model.UserRightBean;

public interface DroitUtilisateurDao {

	public List<UserRightBean> getAll() throws DAOLayerException;

	public UserRightBean getById(Integer id) throws DAOLayerException;

	public void saveUserRight(UserRightBean user_right_bean)
			throws DAOLayerException;

	public void deleteById(BigDecimal id) throws DAOLayerException;

	public void update(UserRightBean user_right_bean) throws DAOLayerException;

	public Map<Integer, String> getUserRightMap() throws DAOLayerException;

	public DroitUtilisateur findById(BigDecimal id) throws DAOLayerException;

	public Map<Integer, String> getDroitUtilisateurList()
			throws DAOLayerException;
}
