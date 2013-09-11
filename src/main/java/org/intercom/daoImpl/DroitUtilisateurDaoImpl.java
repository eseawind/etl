package org.intercom.daoImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.intercom.dao.DroitSystemeDao;
import org.intercom.dao.DroitUtilisateurDao;
import org.intercom.dao.exceptions.DAOLayerException;
import org.intercom.domain.DroitSysteme;
import org.intercom.domain.DroitUtilisateur;
import org.intercom.model.UserRightBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class DroitUtilisateurDaoImpl implements DroitUtilisateurDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	DroitSystemeDao droitSystemeDao;

	private static Comparator<DroitSysteme> COMPARATOR = new Comparator<DroitSysteme>() {
		public int compare(DroitSysteme droit_systeme_1,
				DroitSysteme droit_systeme_2) {
			return droit_systeme_1.getIdDroitSysteme().intValue()
					- droit_systeme_2.getIdDroitSysteme().intValue();
		}
	};

	private void save(DroitUtilisateur droit_utilisateur)
			throws DAOLayerException {
		try {
			entityManager.persist(droit_utilisateur);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	private void remove(DroitUtilisateur droit_utilisateur)
			throws DAOLayerException {
		try {
			entityManager.remove(droit_utilisateur);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	public UserRightBean getById(Integer id) throws DAOLayerException {
		try {
			BigDecimal user_right_id = new BigDecimal((double) id);
			DroitUtilisateur droit_utilisateur = findById(user_right_id);
			String label = droit_utilisateur.getLabel();
			Set<DroitSysteme> set_droit_systeme = droit_utilisateur
					.getDroitSystemes();
			List<DroitSysteme> list_droit_systeme = new ArrayList<DroitSysteme>();
			for (DroitSysteme droit_systeme : set_droit_systeme) {
				list_droit_systeme.add(droit_systeme);
			}
			Collections.sort(list_droit_systeme, COMPARATOR);
			String details_url = "user_right_details?id=" + id;
			String edit_url = "user_right_edit?id=" + id;
			String delete_url = "user_right_delete?id=" + id;

			UserRightBean user_right_bean = new UserRightBean();
			user_right_bean.setId(id);
			user_right_bean.setLabel(label);
			Map<Integer, String> hashMap = new LinkedHashMap<Integer, String>();
			for (DroitSysteme droit_systeme : list_droit_systeme) {
				Integer id_droit_systeme = droit_systeme.getIdDroitSysteme()
						.intValue();
				String label_droit_systeme = droit_systeme.getLabel();
				hashMap.put(id_droit_systeme, label_droit_systeme);
			}
			user_right_bean.setDroits_acces(hashMap);
			user_right_bean.setDetails_url(details_url);
			user_right_bean.setEdit_url(edit_url);
			user_right_bean.setDelete_url(delete_url);
			return user_right_bean;
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	private List<DroitUtilisateur> findAll() throws DAOLayerException {
		try {
			return entityManager.createQuery(
					"select u from DroitUtilisateur u ORDER BY u.id",
					DroitUtilisateur.class).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	public DroitUtilisateur findById(BigDecimal id) throws DAOLayerException {
		try {
			return entityManager.find(DroitUtilisateur.class, id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	public List<UserRightBean> getAll() throws DAOLayerException {
		try {
			List<UserRightBean> user_rights_list = new ArrayList<UserRightBean>();
			List<DroitUtilisateur> entityList = findAll();
			for (DroitUtilisateur droit_utilisateur : entityList) {
				Integer id = droit_utilisateur.getIdDroitUtilisateur()
						.intValue();
				String label = droit_utilisateur.getLabel();
				Set<DroitSysteme> liste_droit_systeme = droit_utilisateur
						.getDroitSystemes();
				String details_url = "user_right_details?id=" + id;
				String edit_url = "user_right_edit?id=" + id;
				String delete_url = "user_right_delete?id=" + id;
				UserRightBean user_right_bean = new UserRightBean();
				user_right_bean.setId(id);
				user_right_bean.setLabel(label);
				Map<Integer, String> hashMap = new LinkedHashMap<Integer, String>();
				for (DroitSysteme droit_systeme : liste_droit_systeme) {
					Integer id_droit_systeme = droit_systeme
							.getIdDroitSysteme().intValue();
					String label_droit_systeme = droit_systeme.getLabel();
					hashMap.put(id_droit_systeme, label_droit_systeme);
				}
				user_right_bean.setDroits_acces(hashMap);
				user_right_bean.setDetails_url(details_url);
				user_right_bean.setEdit_url(edit_url);
				user_right_bean.setDelete_url(delete_url);
				user_rights_list.add(user_right_bean);
			}
			return user_rights_list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}


	public Map<Integer, String> getUserRightMap() throws DAOLayerException {
		try {
			Map<Integer, String> map_droits_utilisateurs = new LinkedHashMap<Integer, String>();
			List<DroitUtilisateur> entityList = findAll();
			for (DroitUtilisateur droit_utilisateur : entityList) {
				Integer id = droit_utilisateur.getIdDroitUtilisateur()
						.intValue();
				String label = droit_utilisateur.getLabel();
				map_droits_utilisateurs.put(id, label);
			}
			return map_droits_utilisateurs;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}
	
	public void saveUserRight(UserRightBean user_right_bean)
			throws DAOLayerException {
		try {
			String label = user_right_bean.getLabel();
			Set<Integer> selected_access_rights = user_right_bean
					.getSelected_access_rights();
			DroitUtilisateur droit_utilisateur = new DroitUtilisateur();
			droit_utilisateur.setLabel(label);
			if (selected_access_rights != null) {
				for (Integer id : selected_access_rights) {
					BigDecimal decimal_id = new BigDecimal((double) id);
					DroitSysteme droit_systeme = droitSystemeDao
							.findById(decimal_id);
					droit_utilisateur.getDroitSystemes().add(droit_systeme);
				}
			}
			save(droit_utilisateur);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	public void update(UserRightBean user_right_bean) throws DAOLayerException {
		try {
			BigDecimal id_droit = new BigDecimal(
					(double) user_right_bean.getId());
			String label = user_right_bean.getLabel();
			Set<Integer> selected_access_rights = user_right_bean
					.getSelected_access_rights();

			DroitUtilisateur droit_utilisateur = findById(id_droit);
			droit_utilisateur.getDroitSystemes().clear();
			droit_utilisateur.setLabel(label);
			if (selected_access_rights != null) {
				for (Integer id : selected_access_rights) {
					BigDecimal decimal_id = new BigDecimal((double) id);
					DroitSysteme droit_systeme = droitSystemeDao
							.findById(decimal_id);
					droit_utilisateur.getDroitSystemes().add(droit_systeme);
				}
			}
			save(droit_utilisateur);

		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	public void deleteById(BigDecimal id) throws DAOLayerException {
		try {
			DroitUtilisateur droit_utilisateur = findById(id);
			remove(droit_utilisateur);
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	public Map<Integer, String> getDroitUtilisateurList() throws DAOLayerException {
		Map<Integer, String> map_droits_acces = new LinkedHashMap<Integer, String>();
		try {
			List<DroitUtilisateur> list_droits_utilisateurs = findAll();
			for (DroitUtilisateur droits_utilisateur : list_droits_utilisateurs) {
				map_droits_acces.put(droits_utilisateur.getIdDroitUtilisateur()
						.intValue(), droits_utilisateur.getLabel());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
		return map_droits_acces;
	}

}
