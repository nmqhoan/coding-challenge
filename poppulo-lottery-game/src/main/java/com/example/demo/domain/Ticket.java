package com.example.demo.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


@Entity
public class Ticket implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Long id;

	@OneToMany(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
	private List<Line> lines;

	/**
	 * this variable is to indicate if the ticket is checked or not
	 */
	private boolean isChecked = false;

	public Ticket() {
	}

	public Ticket(List<Line> lines) {
		this.lines = lines;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Line> getLines() {
		return lines;
	}

	public void setLines(List<Line> lines) {
		this.lines = lines;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked() {
		this.isChecked = true;
		for(Line line: lines){
			line.calculateResult();
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Ticket)) {
			return false;
		}
		return id != null && id.equals(((Ticket) o).id);
	}

	@Override
	public int hashCode() {
		return 31;
	}

	@Override
	public String toString() {
		return "Ticket{" +
				"ticketId=" + id +
				", lines=" + lines +
				", status=" + isChecked +
				'}';
	}
}
