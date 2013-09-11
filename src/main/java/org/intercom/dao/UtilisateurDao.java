package org.intercom.dao;

import java.math.BigDecimal;
import java.util.List;

import org.intercom.dao.exceptions.DAOLayerException;
import org.intercom.domain.Utilisateur;
import org.intercom.model.UserBean;

public interface UtilisateurDao {
	public Utilisateur findByUsername(String username) throws DAOLayerException;

	public List<UserBean> getAll() throws DAOLayerException;

	public Boolean find(UserBean userBean) throws DAOLayerException;

	public void saveUser(UserBean userBean) throws DAOLayerException;

	public void deleteById(BigDecimal id) throws DAOLayerException;

	public UserBean getById(Integer id) throws DAOLayerException;

	public void update(UserBean user_bean) throws DAOLayerException;
}
