package org.intercom.model;

public class ErrorBean {

	private Integer line_nb;
	private Integer column_nb;
	private Integer type;
	private String text;
	private String correction;
	private String description;

	public Integer getLine_nb() {
		return line_nb;
	}

	public void setLine_nb(Integer line_nb) {
		this.line_nb = line_nb;
	}

	public Integer getColumn_nb() {
		return column_nb;
	}

	public void setColumn_nb(Integer column_nb) {
		this.column_nb = column_nb;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getCorrection() {
		return correction;
	}

	public void setCorrection(String correction) {
		this.correction = correction;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}
