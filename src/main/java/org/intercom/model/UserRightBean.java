package org.intercom.model;

import java.util.Map;
import java.util.Set;

import org.hibernate.validator.constraints.NotEmpty;

public class UserRightBean {
	private Integer id;
	@NotEmpty(message = "Nom du droit utilisateur invalide.")
	private String label;
	private String details_url;
	private String edit_url;
	private String delete_url;
	private Set<Integer> selected_access_rights;
	private Map<Integer, String> droits_acces;
	private Map<Integer, String> display_droits_acces;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDetails_url() {
		return details_url;
	}

	public void setDetails_url(String details_url) {
		this.details_url = details_url;
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

	public Map<Integer, String> getDroits_acces() {
		return droits_acces;
	}

	public void setDroits_acces(Map<Integer, String> droits_acces) {
		this.droits_acces = droits_acces;
	}

	public Set<Integer> getSelected_access_rights() {
		return selected_access_rights;
	}

	public void setSelected_access_rights(Set<Integer> selected_access_rights) {
		this.selected_access_rights = selected_access_rights;
	}

	public Map<Integer, String> getDisplay_droits_acces() {
		return display_droits_acces;
	}

	public void setDisplay_droits_acces(Map<Integer, String> display_droits_acces) {
		this.display_droits_acces = display_droits_acces;
	}

}
