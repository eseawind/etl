package org.intercom.daoImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.intercom.dao.ColonneDao;
import org.intercom.dao.ParametresDao;
import org.intercom.dao.StructureDonneesDao;
import org.intercom.dao.exceptions.DAOLayerException;
import org.intercom.domain.Colonne;
import org.intercom.domain.StructureDonnees;
import org.intercom.model.ColumnBean;
import org.intercom.model.DataStructureBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class StructureDonneesDaoImpl implements StructureDonneesDao {

	@Autowired
	ColonneDao colonneDao;
	
	@Autowired
	ParametresDao parametresDao;

	@PersistenceContext
	private EntityManager entityManager;

	private void save(StructureDonnees structureDonnees)
			throws DAOLayerException {
		try {
			entityManager.persist(structureDonnees);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	private void remove(StructureDonnees structureDonnees)
			throws DAOLayerException {
		try {
			entityManager.remove(structureDonnees);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}

	}

	private List<StructureDonnees> findAll() throws DAOLayerException {
		try {
			return entityManager.createQuery(
					"select e from StructureDonnees e ORDER BY e.id DESC",
					StructureDonnees.class).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	private StructureDonnees findById(BigDecimal data_structure_id)
			throws DAOLayerException {
		try {
			return entityManager
					.find(StructureDonnees.class, data_structure_id);
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

	private Boolean convertToBool(BigDecimal decimal) {
		int int_value = decimal.intValue();
		if (int_value == 0)
			return false;
		return true;
	}

	public void saveBean(DataStructureBean dataStructureBean)
			throws DAOLayerException {
		try {
			String nom_structure_donnes = dataStructureBean.getName();
			StructureDonnees structureDonnees = new StructureDonnees();
			structureDonnees.setNom(nom_structure_donnes);
			save(structureDonnees);
			List<ColumnBean> ColumnsList = dataStructureBean.getColumnList();
			for (ColumnBean columnBean : ColumnsList) {
				String nom = columnBean.getName();
				BigDecimal typeDonnees = new BigDecimal(
						(double) columnBean.getSelectedType());
				BigDecimal ordreDansFichier = new BigDecimal(
						(double) columnBean.getOrder());
				BigDecimal nullable = convertBoolean(columnBean.getNullable());
				BigDecimal clePrimaire = convertBoolean(columnBean
						.getPrimary_Key());
				Colonne colonne = new Colonne();
				colonne.setNom(nom);
				colonne.setTypeDonnees(typeDonnees);
				colonne.setOrdreDansFichier(ordreDansFichier);
				colonne.setClePrimaire(clePrimaire);
				colonne.setNullable(nullable);
				if (columnBean.getLenght() != null) {
					BigDecimal longueur = new BigDecimal(
							(double) columnBean.getLenght());
					colonne.setLongueur(longueur);
				}
				if (columnBean.getMin_value() != null) {
					BigDecimal valeurMin = new BigDecimal(
							(double) columnBean.getMin_value());
					colonne.setValeurMin(valeurMin);
				}
				if (columnBean.getMax_value() != null) {
					BigDecimal valeurMax = new BigDecimal(
							(double) columnBean.getMax_value());
					colonne.setValeurMax(valeurMax);
				}
				if (columnBean.getDefault_value() != null) {
					String valeurParDefaut = columnBean.getDefault_value();
					colonne.setValeurParDefaut(valeurParDefaut);
				}
				colonne.setStructureDonnees(structureDonnees);
				colonneDao.save(colonne);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	public List<DataStructureBean> getDataStructureList()
			throws DAOLayerException {
		try {
			List<DataStructureBean> dataStructureBeanList = new ArrayList<DataStructureBean>();
			List<StructureDonnees> entityList = findAll();
			for (StructureDonnees structureDonnees : entityList) {
				Integer id = structureDonnees.getIdStructureDonnees()
						.intValue();
				String name = structureDonnees.getNom();
				String details_url = "data_structure_details?id=" + id;
				String edit_url = "data_structure_edit?id=" + id;
				String delete_url = "data_structure_delete?id=" + id;
				DataStructureBean dataStructureBean = new DataStructureBean();
				dataStructureBean.setId(id);
				dataStructureBean.setName(name);
				dataStructureBean.setEdit_url(edit_url);
				dataStructureBean.setDetails_url(details_url);
				dataStructureBean.setDelete_url(delete_url);
				dataStructureBeanList.add(dataStructureBean);
			}
			return dataStructureBeanList;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	public void deleteById(Integer id) throws DAOLayerException {
		try {
			BigDecimal data_structure_id = new BigDecimal((double) id);
			StructureDonnees structureDonnees = findById(data_structure_id);
			Set<Colonne> columnList = structureDonnees.getColonnes();
			for (Colonne colonne : columnList)
				colonneDao.remove(colonne);
			remove(structureDonnees);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	public DataStructureBean getById(Integer id) throws DAOLayerException {
		try {
			BigDecimal data_structure_id = new BigDecimal((double) id);
			StructureDonnees structureDonnees = findById(data_structure_id);
			String name = structureDonnees.getNom();
			List<ColumnBean> columnBeanList = new ArrayList<ColumnBean>();
			Set<Colonne> colonnes = structureDonnees.getColonnes();
			for (Colonne colonne : colonnes) {
				Integer id_colonne = colonne.getIdColonne().intValue();
				Integer order = colonne.getOrdreDansFichier().intValue();
				String column_name = colonne.getNom();
				Integer type = colonne.getTypeDonnees().intValue();
				String default_value = colonne.getValeurParDefaut();
				Boolean nullable = convertToBool(colonne.getNullable());
				Boolean primary_Key = convertToBool(colonne.getClePrimaire());
				String display_type = parametresDao.getLabel("type_colonne",
						colonne.getTypeDonnees());
				String edit_url = "column_edit?order=" + order;
				String delete_url = "column_delete?order=" + order;
				ColumnBean columnBean = new ColumnBean();
				columnBean.setId(id_colonne);
				columnBean.setOrder(order);
				columnBean.setName(column_name);
				columnBean.setSelectedType(type);
				columnBean.setDisplay_type(display_type);
				if (colonne.getLongueur() != null) {
					Integer lenght = colonne.getLongueur().intValue();
					columnBean.setLenght(lenght);
				}
				if (colonne.getValeurMin() != null) {
					Integer min_value = colonne.getValeurMin().intValue();
					columnBean.setMin_value(min_value);
				}
				if (colonne.getValeurMax() != null) {
					Integer max_value = colonne.getValeurMax().intValue();
					columnBean.setMax_value(max_value);
				}
				columnBean.setDefault_value(default_value);
				columnBean.setNullable(nullable);
				columnBean.setPrimary_Key(primary_Key);
				columnBean.setEdit_url(edit_url);
				columnBean.setDelete_url(delete_url);
				columnBeanList.add(columnBean);
			}
			DataStructureBean dataStructureBean = new DataStructureBean();
			dataStructureBean.setId(id);
			dataStructureBean.setName(name);
			dataStructureBean.setColumnList(columnBeanList);
			return dataStructureBean;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}

	public void update(DataStructureBean dataStructureBean)
			throws DAOLayerException {
		BigDecimal id = new BigDecimal((double) dataStructureBean.getId());
		StructureDonnees structureDonnees = findById(id);
		String nom_structure_donnes = dataStructureBean.getName();
		structureDonnees.setNom(nom_structure_donnes);
		Set<Colonne> colonnes = structureDonnees.getColonnes();
		for (Colonne colonne : colonnes)
			colonneDao.remove(colonne);
		List<ColumnBean> columnsBeanList = dataStructureBean.getColumnList();
		for (ColumnBean columnBean : columnsBeanList) {
			String nom = columnBean.getName();
			BigDecimal typeDonnees = new BigDecimal(
					(double) columnBean.getSelectedType());
			BigDecimal ordreDansFichier = new BigDecimal(
					(double) columnBean.getOrder());
			BigDecimal nullable = convertBoolean(columnBean.getNullable());
			BigDecimal clePrimaire = convertBoolean(columnBean.getPrimary_Key());
			Colonne colonne = new Colonne();
			colonne.setNom(nom);
			colonne.setTypeDonnees(typeDonnees);
			colonne.setOrdreDansFichier(ordreDansFichier);
			colonne.setClePrimaire(clePrimaire);
			colonne.setNullable(nullable);
			if (columnBean.getLenght() != null) {
				BigDecimal longueur = new BigDecimal(
						(double) columnBean.getLenght());
				colonne.setLongueur(longueur);
			}
			if (columnBean.getMin_value() != null) {
				BigDecimal valeurMin = new BigDecimal(
						(double) columnBean.getMin_value());
				colonne.setValeurMin(valeurMin);
			}
			if (columnBean.getMax_value() != null) {
				BigDecimal valeurMax = new BigDecimal(
						(double) columnBean.getMax_value());
				colonne.setValeurMax(valeurMax);
			}
			if (columnBean.getDefault_value() != null) {
				String valeurParDefaut = columnBean.getDefault_value();
				colonne.setValeurParDefaut(valeurParDefaut);
			}
			colonne.setStructureDonnees(structureDonnees);
			colonneDao.save(colonne);
		}
	}
	
	public Map<Integer, String> getDataStructureConnectionList()
			throws DAOLayerException {
		try {
			Map<Integer, String> hashMap = new LinkedHashMap<Integer, String>();
			List<StructureDonnees> entityList = findAll();
			for (StructureDonnees structureDonnees : entityList) {
				Integer id = structureDonnees.getIdStructureDonnees().intValue();
				String data_structure_name = structureDonnees.getNom();
				hashMap.put(id, data_structure_name);
			}
			return hashMap;
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new DAOLayerException(e.getMessage());
		}
	}


}
