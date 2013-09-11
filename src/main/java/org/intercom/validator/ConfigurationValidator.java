package org.intercom.validator;

import org.intercom.model.ConfigurationBean;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ConfigurationValidator implements Validator {
	
	@SuppressWarnings("rawtypes")
	public boolean supports(Class c) {
		return ConfigurationBean.class.isAssignableFrom(c);
	}

	public void validate(Object command, Errors errors) {
		ConfigurationBean configurationBean = (ConfigurationBean) command;
		if ((configurationBean.getLimit_by_line_number())
				&& (configurationBean.getNumber_of_lines() == null))
			errors.reject("line_number.empty",
					"Le nombre de ligne � limiter est vide !");
		Boolean error_sep_lenght = false;
		String field_separator = configurationBean.getDisplay_field_separator();
		Character char_field_separator = field_separator.charAt(0);
		if (char_field_separator == '\\') {
			if (field_separator.length() > 2)
				error_sep_lenght = true;
		} else {
			if (field_separator.length() > 1)
				error_sep_lenght = true;
		}
		if (error_sep_lenght)
			errors.reject(
					"field_separator.lenght",
					"Le s�parateur de ligne doit �tre compos� d'un seul caract�re ou un caract�re �chapp� par le caract�re ' \\ ' (par exemple ' \\t ' ).");
		Boolean error_escape_char = false;
		Integer selected_escape_char = configurationBean
				.getSelected_escape_char();
		if (selected_escape_char != 1) {
			String escape_char = configurationBean.getDisplay_escape_char();
			Character char_escape_char = escape_char.charAt(0);
			if (char_escape_char == '\\') {
				if (escape_char.length() > 2)
					error_escape_char = true;
			} else {
				if (escape_char.length() > 1)
					error_escape_char = true;
			}
			if (error_escape_char)
				errors.reject(
						"escape_char.lenght",
						"Le caract�re d'�chappement doit �tre compos� d'un seul caract�re ou un caract�re �chapp� par le caract�re ' \\ ' (par exemple ' \\t ' ).");
		}
	}
}