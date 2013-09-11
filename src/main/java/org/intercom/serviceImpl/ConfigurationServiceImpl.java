package org.intercom.serviceImpl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.intercom.dao.ConfigurationDao;
import org.intercom.dao.ParametresDao;
import org.intercom.dao.SelectionParIntervalleDao;
import org.intercom.dao.exceptions.DAOLayerException;
import org.intercom.model.ConfigurationBean;
import org.intercom.model.IntervalSelectionBean;
import org.intercom.service.ConfigurationService;
import org.intercom.service.exceptions.BusinessLayerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {

	@Autowired
	SelectionParIntervalleDao IntervalSelectionDao;

	@Autowired
	ConfigurationDao configurationDao;

	@Autowired
	ParametresDao parametresDao;

	public void prepare(ConfigurationBean configurationBean)
			throws BusinessLayerException {
		try {
			Map<Integer, String> encoding_list = new LinkedHashMap<Integer, String>();
			Map<Integer, String> field_separator_list = new LinkedHashMap<Integer, String>();
			Map<Integer, String> escape_char_list = new LinkedHashMap<Integer, String>();
			encoding_list = parametresDao.getParamList("encoding");
			field_separator_list = parametresDao.getParamList("sep_colonne");
			escape_char_list = parametresDao.getParamList("carc_echap");
			configurationBean.setEncoding_list(encoding_list);
			configurationBean.setField_separator_list(field_separator_list);
			configurationBean.setEscape_char_list(escape_char_list);
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	public void saveInterval(IntervalSelectionBean intervalSelectionBean)
			throws BusinessLayerException {
		try {
			IntervalSelectionDao.saveToDB(intervalSelectionBean);
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	public void saveConfigurationBean(ConfigurationBean configurationBean)
			throws BusinessLayerException {
		try {
			setSeparators(configurationBean);
			configurationDao.saveConfiguration(configurationBean);
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	public void update(ConfigurationBean configuration)
			throws BusinessLayerException {
		try {
			setSeparators(configuration);
			configurationDao.update(configuration);
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	public Boolean findSimilarBeans(ConfigurationBean configurationBean)
			throws BusinessLayerException {
		try {
			return configurationDao.findSimlars(configurationBean);
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	public List<ConfigurationBean> getConfigurationsList()
			throws BusinessLayerException {
		try {
			List<ConfigurationBean> configurationBeanList = configurationDao
					.getAll();
			for (ConfigurationBean configurationBean : configurationBeanList)
				setDisplaySeparators(configurationBean);
			return configurationBeanList;
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	public void deleteById(Integer id) throws BusinessLayerException {
		try {
			configurationDao.deleteById(id);
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	public ConfigurationBean getById(Integer id) throws BusinessLayerException {
		try {
			ConfigurationBean configurationBean = configurationDao.getById(id);
			setDisplaySeparators(configurationBean);
			return configurationBean;
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}
	}

	public Map<Integer, String> getConfigurationSelectionList()
			throws BusinessLayerException {
		try {
			return configurationDao.getConfigurationSelectionList();
		} catch (DAOLayerException e) {
			e.printStackTrace();
			throw new BusinessLayerException(e.getMessage());
		}

	}

	private void setSeparators(ConfigurationBean configurationBean) {
		Integer selected_field_separator = configurationBean
				.getSelected_field_separator();
		switch (selected_field_separator) {
		case 1:
			configurationBean.setField_separator(';');
			break;
		case 2:
			configurationBean.setField_separator(',');
			break;
		case 3:
			configurationBean.setField_separator('\t');
			break;
		case 4:
			configurationBean.setField_separator(' ');
			break;
		case 5:
			Character field_separator = configurationBean
					.getDisplay_field_separator().charAt(0);
			configurationBean.setField_separator(field_separator);
			break;
		}
		Integer selected_escape_char = configurationBean
				.getSelected_escape_char();
		switch (selected_escape_char) {
		case 1:
			configurationBean.setEscape_char(null);
			break;
		case 2:
			configurationBean.setEscape_char('\\');
			break;
		case 3:
			configurationBean.setEscape_char('\'');
			break;
		case 4:
			Character escape_char = configurationBean.getDisplay_escape_char()
					.charAt(0);
			configurationBean.setEscape_char(escape_char);
			break;
		}
	}

	private ConfigurationBean setDisplaySeparators(
			ConfigurationBean configurationBean) {
		Character field_separator = configurationBean.getField_separator();
		switch (field_separator) {
		case ';':
			configurationBean.setSelected_field_separator(1);
			configurationBean.setDisplay_field_separator(";");
			break;
		case ',':
			configurationBean.setSelected_field_separator(2);
			configurationBean.setDisplay_field_separator(",");
			break;
		case '\t':
			configurationBean.setSelected_field_separator(3);
			configurationBean.setDisplay_field_separator("\\t");
			break;
		case ' ':
			configurationBean.setSelected_field_separator(4);
			configurationBean.setDisplay_field_separator(" ");
			break;
		default:
			configurationBean.setSelected_field_separator(5);
			configurationBean.setDisplay_field_separator(Character
					.toString(field_separator));
			break;
		}
		Character escape_char = configurationBean.getEscape_char();
		if (escape_char == null) {
			configurationBean.setSelected_escape_char(1);
			configurationBean.setDisplay_escape_char("Aucun");
		} else {
			switch (escape_char) {
			case '\\':
				configurationBean.setSelected_escape_char(2);
				configurationBean.setDisplay_escape_char("\\");
				break;
			case '\'':
				configurationBean.setSelected_escape_char(3);
				configurationBean.setDisplay_escape_char("'");
				break;
			default:
				configurationBean.setSelected_escape_char(4);
				configurationBean.setDisplay_escape_char(Character
						.toString(escape_char));
				break;
			}
		}

		return configurationBean;
	}

}
