package com.casic.alarm.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "NK_GX_STRESS_CURVE")
@SequenceGenerator(name = "SEQ_NK_GX_STRESS_CURVE", sequenceName = "SEQ_NK_GX_STRESS_CURVE")
public class NkGxStressCurve implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 290960151628066195L;
	private Long dbId;
	private String devId;
	private String distance;
	private String stress;
	private Timestamp uptime;
	private Timestamp logtime;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_NK_GX_STRESS_CURVE")
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
	
	@Column(name = "STRESS")
	public String getStress() {
		return stress;
	}
	public void setStress(String stress) {
		this.stress = stress;
	}
	
	@Column(name = "UPTIME")
	public Timestamp getUptime() {
		return uptime;
	}
	public void setUptime(Timestamp uptime) {
		this.uptime = uptime;
	}
	
	@Column(name = "LOGTIME")
	public Timestamp getLogtime() {
		return logtime;
	}
	public void setLogtime(Timestamp logtime) {
		this.logtime = logtime;
	}
}
