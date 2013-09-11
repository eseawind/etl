package org.intercom.serviceImpl;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.intercom.dao.DroitSystemeDao;
import org.intercom.dao.DroitUtilisateurDao;
import org.intercom.dao.exceptions.DAOLayerException;
import org.intercom.model.UserRightBean;
import org.intercom.service.UserRightService;
import org.intercom.service.exceptions.BusinessLayerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRightServiceImpl implements UserRightService {

	@Autowired
	DroitUtilisateurDao droitUtilisateurDao;
	@Autowired
	DroitSystemeDao droiSystemeDao;

	public List<UserRightBean> getUserRightsList()
			throws BusinessLayerException {
		try {

			return droitUtilisateurDao.getAll();
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	public UserRightBean getById(Integer id) throws BusinessLayerException {
		try {
			UserRightBean userRightBean = droitUtilisateurDao.getById(id);
			return userRightBean;
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	public UserRightBean prepareUserRightBean(UserRightBean userRightBean)
			throws BusinessLayerException {
		try {
			Map<Integer, String> droits_acces = new LinkedHashMap<Integer, String>();
			droits_acces = droiSystemeDao.getDroitSystemeList();
			userRightBean.setDisplay_droits_acces(droits_acces);
			return userRightBean;
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	public UserRightBean prepareEditableUserRightBean(
			UserRightBean userRightBean) throws BusinessLayerException {
		try {
			Map<Integer, String> temp_droits_acces = new LinkedHashMap<Integer, String>();
			Map<Integer, String> display_droits_acces = new LinkedHashMap<Integer, String>();
			display_droits_acces = droiSystemeDao.getDroitSystemeList();
			Map<Integer, String> droits_acces = userRightBean.getDroits_acces();

			for (Map.Entry<Integer, String> entry : display_droits_acces
					.entrySet()) {
				Integer id_entry = entry.getKey();
				boolean exists = false;
				for (Map.Entry<Integer, String> droit : droits_acces.entrySet()) {
					Integer id_droit = droit.getKey();
					if (id_entry == id_droit) {
						exists = true;
						break;
					}
				}
				if (!exists) {
					String label = entry.getValue();
					temp_droits_acces.put(id_entry, label);
				}
			}
			userRightBean.setDisplay_droits_acces(temp_droits_acces);
			return userRightBean;
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	public void saveUserRight(UserRightBean user_right_bean)
			throws BusinessLayerException {
		try {
			droitUtilisateurDao.saveUserRight(user_right_bean);
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	public void deleteById(Integer id) throws BusinessLayerException {
		try {
			droitUtilisateurDao.deleteById(new BigDecimal((double) id));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	public void update(UserRightBean user_right_bean)
			throws BusinessLayerException {
		try {
			droitUtilisateurDao.update(user_right_bean);
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}
}
