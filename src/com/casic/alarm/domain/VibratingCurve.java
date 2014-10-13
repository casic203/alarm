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
@Table(name = "NK_GX_VIBRATING_CURVE")
@SequenceGenerator(name = "SEQ_NK_GX_VIBRATING_CURVE_ID", sequenceName = "SEQ_NK_GX_VIBRATING_CURVE_ID")
public class VibratingCurve implements Serializable {

	private static final long serialVersionUID = 7900928862723586388L;

	private Long dbId;
	private String devId;
	private String distance;
	private String vibrating;
	private String upTime;
	private String logTime;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_NK_GX_VIBRATING_CURVE_ID")
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

	@Column(name = "VIBRATING")
	public String getVibrating() {
		return vibrating;
	}

	public void setVibrating(String vibrating) {
		this.vibrating = vibrating;
	}

	@Column(name = "UPTIME")
	public String getUpTime() {
		return upTime;
	}

	public void setUpTime(String upTime) {
		this.upTime = upTime;
	}

	@Column(name = "LOGTIME")
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
