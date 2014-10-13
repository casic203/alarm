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
@Table(name = "JG_GAS_STRENGTH")
@SequenceGenerator(name = "SEQ_JG_GAS_STRENGTH_ID", sequenceName = "SEQ_JG_GAS_STRENGTH_ID")
public class GasStrength implements Serializable {

	private static final long serialVersionUID = -8425961051943168649L;

	private Long dbId;
	private String devId;
	private String strength;
	private String upTime;
	private String logTime;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_JG_GAS_STRENGTH_ID")
	@Column(name = "DBID")
	public Long getDbId() {
		return dbId;
	}

	public void setDbId(Long dbId) {
		this.dbId = dbId;
	}

	@Column(name="DEVID")
	public String getDevId() {
		return devId;
	}

	public void setDevId(String devId) {
		this.devId = devId;
	}

	@Column(name="STRENGTH")
	public String getStrength() {
		return strength;
	}

	public void setStrength(String strength) {
		this.strength = strength;
	}

	@Column(name="UPTIME")
	public String getUpTime() {
		return upTime;
	}

	public void setUpTime(String upTime) {
		this.upTime = upTime;
	}

	@Column(name="LOGTIME")
	public String getLogTime() {
		return logTime;
	}

	public void setLogTime(String logTime) {
		this.logTime = logTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
