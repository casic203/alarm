package com.casic.alarm.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "BACK_UP_CYCLE")
@SequenceGenerator(name = "SEQ_BACK_UP_CYCLE_ID", sequenceName = "SEQ_BACK_UP_CYCLE_ID")
public class DatabaseBackupCycle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 436522981106276596L;

	private long id;
	private String cycle;
	private String path;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BACK_UP_CYCLE_ID")
	@Column(name = "ID")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name="CYCLE")
	public String getCycle() {
		return cycle;
	}

	public void setCycle(String cycle) {
		this.cycle = cycle;
	}

	@Column(name="PATH")
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
