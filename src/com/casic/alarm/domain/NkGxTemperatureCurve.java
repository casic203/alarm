package com.casic.alarm.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "NK_GX_TEMPERATURE_CURVE")
@SequenceGenerator(name = "SEQ_NK_GX_TEMPERATURE_CURVE", sequenceName = "SEQ_NK_GX_TEMPERATURE_CURVE")
public class NkGxTemperatureCurve implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -218964021739461078L;
	private Long dbId;
	private String devId;
	private String distance;
	private String temperature;
	private Date uptime;
	private Date logtime;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_NK_GX_TEMPERATURE_CURVE")
	@Column(name = "DBID")
	public Long getDbId() {
		return dbId;
	}
	public void setDbId(Long dbId) {
		this.dbId = dbId;
	}
	
	@Column(name = "DEVID")
	public String getDevId() {
		return devId;
	}
	public void setDevId(String devId) {
		this.devId = devId;
	}
	
	@Column(name = "DISTANCE")
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	
	@Column(name = "TEMPERATURE")
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	
	@Column(name = "UPTIME")
	public Date getUptime() {
		return uptime;
	}
	public void setUptime(Date upTime) {
		this.uptime = upTime;
	}
	
	@Column(name = "LOGTIME")
	public Date getLogtime() {
		return logtime;
	}
	public void setLogtime(Date logtime) {
		this.logtime = logtime;
	}
}
