package org.intercom.model;

import java.util.Map;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class ColumnBean {
	private Integer id;
	@NotNull(message = "Ordre invalide.")
	private Integer order;
	@NotEmpty(message = "Nom invalide.")
	private String name;
	private Integer selectedType;
	private String display_type;
	private Integer lenght;
	private String default_value;
	private Integer min_value;
	private Integer max_value;
	private Boolean nullable;
	private Boolean primary_Key;
	private String edit_url;
	private String delete_url;
	private Integer db_table_column_index;

	private Map<Integer, String> type_list;

	public ColumnBean() {
		db_table_column_index = -1;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getLenght() {
		return lenght;
	}

	public void setLenght(Integer lenght) {
		this.lenght = lenght;
	}

	public Integer getMin_value() {
		return min_value;
	}

	public void setMin_value(Integer min_value) {
		this.min_value = min_value;
	}

	public Integer getMax_value() {
		return max_value;
	}

	public void setMax_value(Integer max_value) {
		this.max_value = max_value;
	}

	public Boolean getNullable() {
		return nullable;
	}

	public void setNullable(Boolean nullable) {
		this.nullable = nullable;
	}

	public Boolean getPrimary_Key() {
		return primary_Key;
	}

	public void setPrimary_Key(Boolean primary_Key) {
		this.primary_Key = primary_Key;
	}

	public String getDefault_value() {
		return default_value;
	}

	public void setDefault_value(String default_value) {
		this.default_value = default_value;
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

	public Map<Integer, String> getType_list() {
		return type_list;
	}

	public void setType_list(Map<Integer, String> type_list) {
		this.type_list = type_list;
	}

	public Integer getSelectedType() {
		return selectedType;
	}

	public void setSelectedType(Integer selectedType) {
		this.selectedType = selectedType;
	}

	public String getDisplay_type() {
		return display_type;
	}

	public void setDisplay_type(String display_type) {
		this.display_type = display_type;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDb_table_column_index() {
		return db_table_column_index;
	}

	public void setDb_table_column_index(Integer db_table_column_index) {
		this.db_table_column_index = db_table_column_index;
	}
}
