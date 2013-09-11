package org.intercom.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

public class ConfigurationBean {

	private Integer id;
	@NotEmpty(message = "Nom de la configuration invalide.")
	private String config_name;

	private Integer selected_encoding;
	private Integer selected_field_separator;
	private Integer selected_escape_char;

	private String encoding;
	private Character field_separator;
	private Character escape_char;
	
	@NotBlank(message = "Séparateur de colonne invalide.")
	private String display_field_separator;	
	
	private String display_escape_char;

	private Boolean ignore_empty_lines;
	private Boolean get_titles_from_first_line;

	private Boolean limit_by_line_number;
	private Integer number_of_lines;

	private Boolean limit_by_interval;

	private Map<Integer, String> encoding_list;
	private Map<Integer, String> field_separator_list;
	private Map<Integer, String> escape_char_list;

	private Map<Integer, String> intervalSelectItems;

	private List<IntervalSelectionBean> intervalList;

	private String details_url;
	private String edit_url;
	private String delete_url;

	public ConfigurationBean() {
		limit_by_interval = false;
		limit_by_line_number = false;
		get_titles_from_first_line = false;
		ignore_empty_lines = true;
		intervalList =  new ArrayList<IntervalSelectionBean>();
		intervalSelectItems = new LinkedHashMap<Integer, String>();
	}

	public String getConfig_name() {
		return config_name;
	}

	public void setConfig_name(String config_name) {
		this.config_name = config_name;
	}

	public Map<Integer, String> getEncoding_list() {
		return encoding_list;
	}

	public void setEncoding_list(Map<Integer, String> encoding_list) {
		this.encoding_list = encoding_list;
	}

	public Boolean getIgnore_empty_lines() {
		return ignore_empty_lines;
	}

	public void setIgnore_empty_lines(Boolean ignore_empty_lines) {
		this.ignore_empty_lines = ignore_empty_lines;
	}

	public Boolean getGet_titles_from_first_line() {
		return get_titles_from_first_line;
	}

	public void setGet_titles_from_first_line(Boolean get_titles_from_first_line) {
		this.get_titles_from_first_line = get_titles_from_first_line;
	}

	public Map<Integer, String> getField_separator_list() {
		return field_separator_list;
	}

	public void setField_separator_list(
			Map<Integer, String> field_separator_list) {
		this.field_separator_list = field_separator_list;
	}

	public Map<Integer, String> getEscape_char_list() {
		return escape_char_list;
	}

	public void setEscape_char_list(Map<Integer, String> escape_char_list) {
		this.escape_char_list = escape_char_list;
	}

	public Integer getNumber_of_lines() {
		return number_of_lines;
	}

	public void setNumber_of_lines(Integer number_of_lines) {
		this.number_of_lines = number_of_lines;
	}

	public Boolean getLimit_by_line_number() {
		return limit_by_line_number;
	}

	public void setLimit_by_line_number(Boolean limit_by_line_number) {
		this.limit_by_line_number = limit_by_line_number;
	}

	public boolean isLimit_by_interval() {
		return limit_by_interval;
	}

	public void setLimit_by_interval(boolean limit_by_interval) {
		this.limit_by_interval = limit_by_interval;
	}

	public Map<Integer, String> getIntervalSelectItems() {
		return intervalSelectItems;
	}

	public void setIntervalSelectItems(Map<Integer, String> intervalSelectItems) {
		this.intervalSelectItems = intervalSelectItems;
	}

	public Integer getSelected_field_separator() {
		return selected_field_separator;
	}

	public void setSelected_field_separator(Integer selected_field_separator) {
		this.selected_field_separator = selected_field_separator;
	}

	public Integer getSelected_escape_char() {
		return selected_escape_char;
	}

	public void setSelected_escape_char(Integer selected_escape_char) {
		this.selected_escape_char = selected_escape_char;
	}

	public List<IntervalSelectionBean> getIntervalList() {
		return intervalList;
	}

	public void setIntervalList(List<IntervalSelectionBean> intervalList) {
		this.intervalList = intervalList;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEdit_url() {
		return edit_url;
	}

	public void setEdit_url(String edit_url) {
		this.edit_url = edit_url;
	}

	public String getDelete_url() {
		return delete_url;
	}

	public void setDelete_url(String delete_url) {
		this.delete_url = delete_url;
	}

	public Integer getSelected_encoding() {
		return selected_encoding;
	}

	public void setSelected_encoding(Integer selected_encoding) {
		this.selected_encoding = selected_encoding;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getDetails_url() {
		return details_url;
	}

	public void setDetails_url(String details_url) {
		this.details_url = details_url;
	}

	public Character getEscape_char() {
		return escape_char;
	}

	public void setEscape_char(Character escape_char) {
		this.escape_char = escape_char;
	}

	public Character getField_separator() {
		return field_separator;
	}

	public void setField_separator(Character field_separator) {
		this.field_separator = field_separator;
	}

	public String getDisplay_field_separator() {
		return display_field_separator;
	}

	public void setDisplay_field_separator(String display_field_separator) {
		this.display_field_separator = display_field_separator;
	}

	public String getDisplay_escape_char() {
		return display_escape_char;
	}

	public void setDisplay_escape_char(String display_escape_char) {
		this.display_escape_char = display_escape_char;
	}
}
