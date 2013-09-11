package org.intercom.model;

public class IntervalSelectionBean {
	private Integer id;
	private Integer start_line_nb;
	private Integer end_line_nb;

	public IntervalSelectionBean(Integer id, Integer start_line_nb,
			Integer end_line_nb) {
		this.id = id;
		this.start_line_nb = start_line_nb;
		this.end_line_nb = end_line_nb;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getStart_line_nb() {
		return start_line_nb;
	}

	public void setStart_line_nb(Integer start_line_nb) {
		this.start_line_nb = start_line_nb;
	}

	public Integer getEnd_line_nb() {
		return end_line_nb;
	}

	public void setEnd_line_nb(Integer end_line_nb) {
		this.end_line_nb = end_line_nb;
	}
}
