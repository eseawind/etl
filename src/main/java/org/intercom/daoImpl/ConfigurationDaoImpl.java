package org.intercom.daoImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.intercom.dao.ConfigurationDao;
import org.intercom.dao.ParametresDao;
import org.intercom.dao.SelectionParIntervalleDao;
import org.intercom.dao.exceptions.DAOLayerException;
import org.intercom.domain.Configuration;
import org.intercom.domain.SelectionParIntervalle;
import org.intercom.model.ConfigurationBean;
import org.intercom.model.IntervalSelectionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ConfigurationDaoImpl implements ConfigurationDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	SelectionParIntervalleDao selectionParIntervalleDao;
	@Autowired
	ParametresDao parametresDao;

	private void save(Configuration configuration) throws DAOLayerException {
		try {
			entityManager.persist(configuration);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	private void remove(Configuration configuration) throws DAOLayerException {
		try {
			entityManager.remove(configuration);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	private List<Configuration> findAll() throws DAOLayerException {
		try {
			return entityManager.createQuery(
					"select e from Configuration e ORDER BY e.id DESC",
					Configuration.class).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	private Configuration findById(BigDecimal id) throws DAOLayerException {
		try {
			return entityManager.find(Configuration.class, id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	private BigDecimal convertBoolean(Boolean bool) {
		if (bool)
			return new BigDecimal((double) 1);
		return new BigDecimal((double) 0);
	}

	public void saveConfiguration(ConfigurationBean configurationBean)
			throws DAOLayerException {
		try {
			String nom = configurationBean.getConfig_name();
			BigDecimal encodageDeCaractere = new BigDecimal(
					(double) configurationBean.getSelected_encoding());
			Character separateurDeColonne = configurationBean
					.getField_separator();
			Character caractereEchappement = configurationBean.getEscape_char();
			BigDecimal ignorerLignesVides = convertBoolean(configurationBean
					.getIgnore_empty_lines());
			BigDecimal utiliserNomPremiereLigne = convertBoolean(configurationBean
					.getGet_titles_from_first_line());

			BigDecimal limiterNbLignes = new BigDecimal((double) 0);
			Boolean limit_by_line_number = configurationBean
					.getLimit_by_line_number();
			if (limit_by_line_number) {
				Integer nb_lignes_limite = configurationBean
						.getNumber_of_lines();
				limiterNbLignes = new BigDecimal((double) nb_lignes_limite);
			}
			Configuration configuration = new Configuration();
			configuration.setNom(nom);
			configuration.setEncodageDeCaractere(encodageDeCaractere);
			configuration.setCaractereEchappement(caractereEchappement);
			configuration.setSeparateurDeColonne(separateurDeColonne);
			configuration.setIgnorerLignesVides(ignorerLignesVides);
			configuration.setNbLignesALimiter(limiterNbLignes);
			configuration.setUtiliserNomPremiereLigne(utiliserNomPremiereLigne);
			save(configuration);
			Boolean limit_by_interval = configurationBean.isLimit_by_interval();
			if (limit_by_interval) {
				List<IntervalSelectionBean> intervalList = configurationBean
						.getIntervalList();
				Set<SelectionParIntervalle> selectionParIntervalleList = new HashSet<SelectionParIntervalle>();
				for (IntervalSelectionBean intervalSelectionBean : intervalList) {
					SelectionParIntervalle selectionParIntervalle = new SelectionParIntervalle();
					BigDecimal numLigneDepart = new BigDecimal(
							(double) intervalSelectionBean.getStart_line_nb());
					BigDecimal numLigneFin = new BigDecimal(
							(double) intervalSelectionBean.getEnd_line_nb());
					selectionParIntervalle.setNumLigneDepart(numLigneDepart);
					selectionParIntervalle.setNumLigneFin(numLigneFin);
					selectionParIntervalleList.add(selectionParIntervalle);
				}
				for (SelectionParIntervalle selectionParIntervalle : selectionParIntervalleList) {
					selectionParIntervalle.setConfiguration(configuration);
					selectionParIntervalleDao.save(selectionParIntervalle);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	@Transactional(readOnly = true)
	public Boolean findSimlars(ConfigurationBean configurationBean)
			throws DAOLayerException {
		try {
			String str_query = "SELECT c FROM Configuration c WHERE c.nom = :config_name ";
			TypedQuery<Configuration> query = entityManager.createQuery(
					str_query, Configuration.class);
			query.setParameter("config_name",
					configurationBean.getConfig_name());
			List<Configuration> results = query.getResultList();
			if (results.size() > 0)
				return true;
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	public List<ConfigurationBean> getAll() throws DAOLayerException {
		try {
			List<ConfigurationBean> configurationBeanList = new ArrayList<ConfigurationBean>();
			List<Configuration> entityList = findAll();
			for (Configuration configuration : entityList) {
				Integer id = configuration.getIdConfiguration().intValue();
				String config_name = configuration.getNom();
				BigDecimal selected_encoding = configuration
						.getEncodageDeCaractere();
				String encoding = parametresDao.getLabel("encoding",
						selected_encoding);
				Character field_separator = configuration
						.getSeparateurDeColonne();
				Character escape_char = configuration.getCaractereEchappement();
				String details_url = "config_details?id=" + id;
				String edit_url = "config_edit?id=" + id;
				String delete_url = "config_delete?id=" + id;
				ConfigurationBean configurationBean = new ConfigurationBean();
				configurationBean.setId(id);
				configurationBean.setConfig_name(config_name);
				configurationBean.setField_separator(field_separator);
				configurationBean.setEscape_char(escape_char);
				configurationBean.setDetails_url(details_url);
				configurationBean.setEdit_url(edit_url);
				configurationBean.setDelete_url(delete_url);
				configurationBean.setEncoding(encoding);
				configurationBeanList.add(configurationBean);
			}
			return configurationBeanList;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	public Map<Integer, String> getConfigurationSelectionList()
			throws DAOLayerException {
		try {
			Map<Integer, String> hashMap = new LinkedHashMap<Integer, String>();
			List<Configuration> entityList = findAll();
			for (Configuration configuration : entityList) {
				Integer id = configuration.getIdConfiguration().intValue();
				String config_name = configuration.getNom();
				hashMap.put(id, config_name);
			}
			return hashMap;
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	public void deleteById(Integer id) throws DAOLayerException {
		try {
			BigDecimal configuration_id = new BigDecimal((double) id);
			Configuration configuration = findById(configuration_id);
			Set<SelectionParIntervalle> intervalList = configuration
					.getSelectionParIntervalles();
			for (SelectionParIntervalle interval : intervalList)
				selectionParIntervalleDao.remove(interval);
			remove(configuration);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	private Boolean convertToBool(BigDecimal decimal) {
		int int_value = decimal.intValue();
		if (int_value == 0)
			return false;
		return true;
	}

	public ConfigurationBean getById(Integer id) throws DAOLayerException {
		BigDecimal configuration_id = new BigDecimal((double) id);
		try {
			Configuration configuration = findById(configuration_id);
			String config_name = configuration.getNom();
			BigDecimal selected_encoding = configuration
					.getEncodageDeCaractere();
			String encoding = parametresDao.getLabel("encoding",
					selected_encoding);
			Integer select_encoding = selected_encoding.intValue();
			Character field_separator = configuration.getSeparateurDeColonne();
			Character escape_char = configuration.getCaractereEchappement();
			Boolean ignore_empty_lines = convertToBool(configuration
					.getIgnorerLignesVides());
			Boolean get_titles_from_first_line = convertToBool(configuration
					.getUtiliserNomPremiereLigne());
			Integer nbLignesALimiter = configuration.getNbLignesALimiter()
					.intValue();
			String details_url = "config_details?id=" + id;
			String edit_url = "config_edit?id=" + id;
			String delete_url = "config_delete?id=" + id;
			
			ConfigurationBean configurationBean = new ConfigurationBean();
			configurationBean.setId(id);
			configurationBean.setConfig_name(config_name);
			configurationBean.setField_separator(field_separator);
			configurationBean.setEscape_char(escape_char);
			configurationBean.setSelected_encoding(select_encoding);
			configurationBean.setEncoding(encoding);
			configurationBean.setIgnore_empty_lines(ignore_empty_lines);
			configurationBean
					.setGet_titles_from_first_line(get_titles_from_first_line);
			configurationBean.setDetails_url(details_url);
			configurationBean.setEdit_url(edit_url);
			configurationBean.setDelete_url(delete_url);
			if (nbLignesALimiter > 0) {
				configurationBean.setLimit_by_line_number(true);
				configurationBean.setNumber_of_lines(nbLignesALimiter);
			}
			Set<SelectionParIntervalle> selectionParIntervalles = configuration
					.getSelectionParIntervalles();
			if (!selectionParIntervalles.isEmpty()) {
				List<IntervalSelectionBean> intervalList = new ArrayList<IntervalSelectionBean>();
				for (SelectionParIntervalle selectionIntervalle : selectionParIntervalles) {
					Integer id_intervalle = selectionIntervalle.getIdSelection()
							.intValue();
					Integer start_line_nb = selectionIntervalle
							.getNumLigneDepart().intValue();
					Integer end_line_nb = selectionIntervalle.getNumLigneFin()
							.intValue();
					IntervalSelectionBean inteval = new IntervalSelectionBean(
							id_intervalle, start_line_nb, end_line_nb);
					intervalList.add(inteval);
				}
				int i = 0;
				String interval_name;
				Map<Integer, String> intervalSelectItems = new LinkedHashMap<Integer, String>();
				for (IntervalSelectionBean intervalSelectionBean : intervalList) {
					interval_name = intervalSelectionBean.getStart_line_nb()
							+ " - " + intervalSelectionBean.getEnd_line_nb();
					intervalSelectItems.put(i++, interval_name);
				}
				configurationBean.setIntervalSelectItems(intervalSelectItems);
				configurationBean.setLimit_by_interval(true);
				configurationBean.setIntervalList(intervalList);
			}
			return configurationBean;
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	public void update(ConfigurationBean configurationBean)
			throws DAOLayerException {
		try {
			BigDecimal id = new BigDecimal((double) configurationBean.getId());
			Configuration configuration = findById(id);
			// clean up intervals and limit by line number
			Set<SelectionParIntervalle> tempIntervalList = configuration
					.getSelectionParIntervalles();
			for (SelectionParIntervalle interval : tempIntervalList)
				selectionParIntervalleDao.remove(interval);
			configuration.setNbLignesALimiter(new BigDecimal((double) 0));

			String nom = configurationBean.getConfig_name();
			BigDecimal encodageDeCaractere = new BigDecimal(
					(double) configurationBean.getSelected_encoding());
			Character separateurDeColonne = configurationBean
					.getField_separator();
			Character caractereEchappement = configurationBean.getEscape_char();
			BigDecimal ignorerLignesVides = convertBoolean(configurationBean
					.getIgnore_empty_lines());
			BigDecimal utiliserNomPremiereLigne = convertBoolean(configurationBean
					.getGet_titles_from_first_line());

			BigDecimal limiterNbLignes = new BigDecimal((double) 0);
			Boolean limit_by_line_number = configurationBean
					.getLimit_by_line_number();
			if (limit_by_line_number) {
				Integer nb_lignes_limite = configurationBean
						.getNumber_of_lines();
				limiterNbLignes = new BigDecimal((double) nb_lignes_limite);
				configurationBean
						.setIntervalList(new ArrayList<IntervalSelectionBean>());
			}
			configuration.setCaractereEchappement(caractereEchappement);
			configuration.setEncodageDeCaractere(encodageDeCaractere);
			configuration.setIgnorerLignesVides(ignorerLignesVides);
			configuration.setNbLignesALimiter(limiterNbLignes);
			configuration.setNom(nom);
			configuration.setSeparateurDeColonne(separateurDeColonne);
			configuration.setUtiliserNomPremiereLigne(utiliserNomPremiereLigne);
			Boolean limit_by_interval = configurationBean.isLimit_by_interval();
			if (limit_by_interval) {
				List<IntervalSelectionBean> intervalList = configurationBean
						.getIntervalList();
				Set<SelectionParIntervalle> selectionParIntervalleList = new HashSet<SelectionParIntervalle>();
				for (IntervalSelectionBean intervalSelectionBean : intervalList) {
					SelectionParIntervalle selectionParIntervalle = new SelectionParIntervalle();
					BigDecimal numLigneDepart = new BigDecimal(
							(double) intervalSelectionBean.getStart_line_nb());
					BigDecimal numLigneFin = new BigDecimal(
							(double) intervalSelectionBean.getEnd_line_nb());
					selectionParIntervalle.setNumLigneDepart(numLigneDepart);
					selectionParIntervalle.setNumLigneFin(numLigneFin);
					selectionParIntervalleList.add(selectionParIntervalle);
				}
				for (SelectionParIntervalle selectionParIntervalle : selectionParIntervalleList) {
					selectionParIntervalle.setConfiguration(configuration);
					selectionParIntervalleDao.save(selectionParIntervalle);
				}
			}
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

}
