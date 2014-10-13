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
@Table(name = "ALARM_DATA_BASE_MONITOR_RULE")
@SequenceGenerator(name = "SEQ_DATA_BASE_MONITOR_RULE_ID", sequenceName = "SEQ_DATA_BASE_MONITOR_RULE_ID")
public class DataBaseMonitorRule implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8111640561304408785L;
	private Long id;
	private String name;
	private String sql;
	private double max;
	private double min;
	private Boolean active = true;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_DATA_BASE_MONITOR_RULE_ID")
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name="SQL")
	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	@Column(name="MAX")
	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	@Column(name="MIN")
	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	@Column(name="ACTIVE")
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
