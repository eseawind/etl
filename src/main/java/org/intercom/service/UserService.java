package org.intercom.service;

import java.util.List;

import org.intercom.model.UserBean;
import org.intercom.service.exceptions.BusinessLayerException;

public interface UserService {
	public List<UserBean> getUsersList() throws BusinessLayerException;

	public Boolean exists(UserBean userBean) throws BusinessLayerException;

	public void saveUser(UserBean userBean) throws BusinessLayerException;

	public void deleteById(Integer id) throws BusinessLayerException;

	public UserBean prepareUserBean(UserBean userBean)
			throws BusinessLayerException;

	public UserBean getById(Integer id) throws BusinessLayerException;

	public UserBean prepareEditableUserBean(UserBean userBean)
			throws BusinessLayerException;

	public void update(UserBean userBean) throws BusinessLayerException;
}
