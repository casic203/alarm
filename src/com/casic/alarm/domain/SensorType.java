package com.casic.alarm.domain;
// default package

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * AlarmSensor entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ALARM_SENSOR", schema = "SCOTT")
public class SensorType implements java.io.Serializable {

	// Fields

	private String sensorcode;
	private String sensorname;
	private Boolean isuse;
	private String defaultid;

	// Constructors

	/** default constructor */
	public SensorType() {
	}

	/** minimal constructor */
	public SensorType(String sensorcode) {
		this.sensorcode = sensorcode;
	}

	/** full constructor */
	public SensorType(String sensorcode, String sensorname, Boolean isuse,
			String defaultid) {
		this.sensorcode = sensorcode;
		this.sensorname = sensorname;
		this.isuse = isuse;
		this.defaultid = defaultid;
	}

	// Property accessors
	@Id
	@Column(name = "SENSORCODE")
	public String getSensorcode() {
		return this.sensorcode;
	}

	public void setSensorcode(String sensorcode) {
		this.sensorcode = sensorcode;
	}

	@Column(name = "SENSORNAME")
	public String getSensorname() {
		return this.sensorname;
	}

	public void setSensorname(String sensorname) {
		this.sensorname = sensorname;
	}

	@Column(name = "ISUSE", precision = 1, scale = 0)
	public Boolean getIsuse() {
		return this.isuse;
	}

	public void setIsuse(Boolean isuse) {
		this.isuse = isuse;
	}

	@Column(name = "DEFAULTID")
	public String getDefaultid() {
		return this.defaultid;
	}

	public void setDefaultid(String defaultid) {
		this.defaultid = defaultid;
	}

}