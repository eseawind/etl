package org.intercom.service;

import java.util.List;

import org.intercom.model.UserRightBean;
import org.intercom.service.exceptions.BusinessLayerException;

public interface UserRightService {
	public List<UserRightBean> getUserRightsList()
			throws BusinessLayerException;

	public UserRightBean getById(Integer id) throws BusinessLayerException;

	public UserRightBean prepareUserRightBean(UserRightBean userRightBean)
			throws BusinessLayerException;

	public void saveUserRight(UserRightBean user_right_bean)
			throws BusinessLayerException;

	public void deleteById(Integer id) throws BusinessLayerException;

	public UserRightBean prepareEditableUserRightBean(
			UserRightBean userRightBean) throws BusinessLayerException;

	public void update(UserRightBean user_right_bean)
			throws BusinessLayerException;
}
