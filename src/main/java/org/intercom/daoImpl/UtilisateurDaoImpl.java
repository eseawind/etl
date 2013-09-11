package org.intercom.daoImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.intercom.dao.DroitUtilisateurDao;
import org.intercom.dao.UtilisateurDao;
import org.intercom.dao.exceptions.DAOLayerException;
import org.intercom.domain.DroitUtilisateur;
import org.intercom.domain.Utilisateur;
import org.intercom.model.UserBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class UtilisateurDaoImpl implements UtilisateurDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	DroitUtilisateurDao droiUtilisateurtDao;

	private void save(Utilisateur utilisateur) throws DAOLayerException {
		try {
			entityManager.persist(utilisateur);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	private void remove(Utilisateur utilisateur) throws DAOLayerException {
		try {
			entityManager.remove(utilisateur);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}

	}

	private List<Utilisateur> findAll() throws DAOLayerException {
		try {
			return entityManager.createQuery(
					"select u from Utilisateur u ORDER BY u.id",
					Utilisateur.class).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	private Utilisateur findById(BigDecimal id) throws DAOLayerException {
		try {
			return entityManager.find(Utilisateur.class, id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	@Transactional(readOnly = true)
	public Utilisateur findByUsername(String username) throws DAOLayerException {
		Utilisateur user = null;
		try {
			TypedQuery<Utilisateur> query = entityManager.createQuery(
					"SELECT u FROM Utilisateur u WHERE u.login =:username",
					Utilisateur.class);
			query.setParameter("username", username);
			user = query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
		return user;
	}

	public List<UserBean> getAll() throws DAOLayerException {
		try {
			List<UserBean> userBeanList = new ArrayList<UserBean>();
			List<Utilisateur> entityList = findAll();
			for (Utilisateur utilisateur : entityList) {
				Integer id = utilisateur.getIdUtilisateur().intValue();
				String name = utilisateur.getNom();
				String surname = utilisateur.getPrenom();
				String login = utilisateur.getLogin();
				String password = utilisateur.getMotDePasse();
				String details_url = "user_details?id=" + id;
				String edit_url = "user_edit?id=" + id;
				String delete_url = "user_delete?id=" + id;
				UserBean userbean = new UserBean();
				userbean.setId(id);
				userbean.setName(name);
				userbean.setSurname(surname);
				userbean.setLogin(login);
				userbean.setPassword(password);
				userbean.setDetails_url(details_url);
				userbean.setEdit_url(edit_url);
				userbean.setDelete_url(delete_url);
				userBeanList.add(userbean);
			}
			return userBeanList;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	public Boolean find(UserBean userBean) throws DAOLayerException {
		try {
			String login = userBean.getLogin();
			String str_query = "SELECT c FROM Utilisateur c WHERE c.login = :login";
			TypedQuery<Utilisateur> query = entityManager.createQuery(
					str_query, Utilisateur.class);
			query.setParameter("login", login);
			List<Utilisateur> results = query.getResultList();
			if (results.size() > 0)
				return true;
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	public void saveUser(UserBean userBean) throws DAOLayerException {
		try {
			String nom = userBean.getName();
			String prenom = userBean.getSurname();
			String login = userBean.getLogin();
			String motDePasse = userBean.getPassword();
			Set<Integer> selected_droits_utilisateurs = userBean
					.getSelected_droits_utilisateurs();
			Utilisateur utilisateur = new Utilisateur();
			utilisateur.setLogin(login);
			utilisateur.setMotDePasse(motDePasse);
			utilisateur.setNom(nom);
			utilisateur.setPrenom(prenom);
			if (selected_droits_utilisateurs != null) {
				for (Integer id : selected_droits_utilisateurs) {
					BigDecimal decimal_id = new BigDecimal((double) id);
					DroitUtilisateur droit_utilisateur = droiUtilisateurtDao
							.findById(decimal_id);
					utilisateur.getDroitUtilisateurs().add(droit_utilisateur);
				}
			}
			save(utilisateur);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	public void deleteById(BigDecimal id) throws DAOLayerException {
		try {
			Utilisateur utilisateur = findById(id);
			remove(utilisateur);
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	public UserBean getById(Integer id) throws DAOLayerException {
		try {
			BigDecimal user_id = new BigDecimal((double) id);
			Utilisateur utilisateur = findById(user_id);
			String nom = utilisateur.getNom();
			String prenom = utilisateur.getPrenom();
			String login = utilisateur.getLogin();
			String motDePasse = utilisateur.getMotDePasse();
			Set<DroitUtilisateur> droitUtilisateurs = utilisateur
					.getDroitUtilisateurs();
			Map<Integer, String> droits_utilisateurs = new LinkedHashMap<Integer, String>();
			for (DroitUtilisateur droit_utilisateur : droitUtilisateurs) {
				Integer id_droit = droit_utilisateur.getIdDroitUtilisateur()
						.intValue();
				String label = droit_utilisateur.getLabel();
				droits_utilisateurs.put(id_droit, label);
			}
			String details_url = "user_details?id=" + id;
			String edit_url = "user_edit?id=" + id;
			String delete_url = "use_delete?id=" + id;

			UserBean user_bean = new UserBean();
			user_bean.setName(nom);
			user_bean.setSurname(prenom);
			user_bean.setLogin(login);
			user_bean.setPassword(motDePasse);
			user_bean.setDroits_utilisateurs(droits_utilisateurs);
			user_bean.setDetails_url(details_url);
			user_bean.setEdit_url(edit_url);
			user_bean.setDelete_url(delete_url);
			return user_bean;
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	public void update(UserBean user_bean) throws DAOLayerException {
		try {
			BigDecimal id_droit = new BigDecimal((double) user_bean.getId());
			String nom = user_bean.getName();
			String prenom = user_bean.getSurname();
			String login = user_bean.getLogin();
			String motDePasse = user_bean.getPassword();
			Set<Integer> selected_droit_utilisateurs = user_bean
					.getSelected_droits_utilisateurs();

			Utilisateur utilisateur = findById(id_droit);
			utilisateur.getDroitUtilisateurs().clear();
			utilisateur.setNom(nom);
			utilisateur.setPrenom(prenom);
			utilisateur.setLogin(login);
			utilisateur.setMotDePasse(motDePasse);

			if (selected_droit_utilisateurs != null) {
				for (Integer id : selected_droit_utilisateurs) {
					BigDecimal decimal_id = new BigDecimal((double) id);
					DroitUtilisateur droit_utilisateur = droiUtilisateurtDao
							.findById(decimal_id);
					utilisateur.getDroitUtilisateurs().add(droit_utilisateur);
				}
			}
			save(utilisateur);
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

}
