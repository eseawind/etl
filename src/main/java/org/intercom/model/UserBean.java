package org.intercom.model;

import java.util.Map;
import java.util.Set;

import org.hibernate.validator.constraints.NotEmpty;

public class UserBean {
	private Integer id;
	@NotEmpty(message = "Nom d'utilisateur invalide.")
	private String name;
	@NotEmpty(message = "Prénom d'utilisateur invalide.")
	private String surname;
	@NotEmpty(message = "Nom utilisateur invalide.")
	private String login;
	private String password;
	private String details_url;
	private String edit_url;
	private String delete_url;
	private Set<Integer> selected_droits_utilisateurs;
	private Map<Integer, String> droits_utilisateurs;
	private Map<Integer, String> display_droits_utilisateurs;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public Set<Integer> getSelected_droits_utilisateurs() {
		return selected_droits_utilisateurs;
	}

	public void setSelected_droits_utilisateurs(
			Set<Integer> selected_droits_utilisateurs) {
		this.selected_droits_utilisateurs = selected_droits_utilisateurs;
	}

	public Map<Integer, String> getDroits_utilisateurs() {
		return droits_utilisateurs;
	}

	public void setDroits_utilisateurs(Map<Integer, String> droits_utilisateurs) {
		this.droits_utilisateurs = droits_utilisateurs;
	}

	public Map<Integer, String> getDisplay_droits_utilisateurs() {
		return display_droits_utilisateurs;
	}

	public void setDisplay_droits_utilisateurs(
			Map<Integer, String> display_droits_utilisateurs) {
		this.display_droits_utilisateurs = display_droits_utilisateurs;
	}

}
