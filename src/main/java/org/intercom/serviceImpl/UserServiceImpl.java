package org.intercom.serviceImpl;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.intercom.dao.DroitUtilisateurDao;
import org.intercom.dao.UtilisateurDao;
import org.intercom.dao.exceptions.DAOLayerException;
import org.intercom.model.UserBean;
import org.intercom.service.UserService;
import org.intercom.service.exceptions.BusinessLayerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	UtilisateurDao utilisateurDao;

	@Autowired
	DroitUtilisateurDao droitUtilisateurDao;

	public List<UserBean> getUsersList() throws BusinessLayerException {
		try {

			return utilisateurDao.getAll();
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	public UserBean prepareUserBean(UserBean userBean)
			throws BusinessLayerException {
		try {
			Map<Integer, String> droits_utilisateurs = new LinkedHashMap<Integer, String>();
			droits_utilisateurs = droitUtilisateurDao.getUserRightMap();
			userBean.setDisplay_droits_utilisateurs(droits_utilisateurs);
			return userBean;
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	public UserBean getById(Integer id) throws BusinessLayerException {
		try {
			UserBean userBean = utilisateurDao.getById(id);
			return userBean;
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	public Boolean exists(UserBean userBean) throws BusinessLayerException {
		try {
			return utilisateurDao.find(userBean);
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	public void saveUser(UserBean userBean) throws BusinessLayerException {
		try {
			utilisateurDao.saveUser(userBean);
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	public void deleteById(Integer id) throws BusinessLayerException {
		try {
			utilisateurDao.deleteById(new BigDecimal((double) id));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	public UserBean prepareEditableUserBean(UserBean userBean)
			throws BusinessLayerException {
		try {
			Map<Integer, String> temp_droits_utilisateur = new LinkedHashMap<Integer, String>();
			Map<Integer, String> display_droits_utilisateur = new LinkedHashMap<Integer, String>();
			display_droits_utilisateur = droitUtilisateurDao
					.getDroitUtilisateurList();
			Map<Integer, String> droits_acces = userBean
					.getDroits_utilisateurs();
			for (Map.Entry<Integer, String> entry : display_droits_utilisateur
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
					temp_droits_utilisateur.put(id_entry, label);
				}
			}
			userBean.setDisplay_droits_utilisateurs(temp_droits_utilisateur);
			return userBean;
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	public void update(UserBean userBean) throws BusinessLayerException {
		try {
			utilisateurDao.update(userBean);
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}
}
