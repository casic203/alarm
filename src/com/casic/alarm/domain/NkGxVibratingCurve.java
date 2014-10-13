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
@Table(name = "NK_GX_VIBRATING_CURVE")
@SequenceGenerator(name = "SEQ_NK_GX_VIBRATING_CURVE", sequenceName = "SEQ_NK_GX_VIBRATING_CURVE")
public class NkGxVibratingCurve implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6031939658724189949L;
	private Long dbId;
	private String devId;
	private String distance;
	private Timestamp logtime;
	private Timestamp uptime;
	private String vibrating;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_NK_GX_VIBRATING_CURVE")
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
	
	@Column(name = "LOGTIME")
	public Timestamp getLogtime() {
		return logtime;
	}
	public void setLogtime(Timestamp logtime) {
		this.logtime = logtime;
	}
	
	@Column(name = "UPTIME")
	public Timestamp getUptime() {
		return uptime;
	}
	public void setUptime(Timestamp uptime) {
		this.uptime = uptime;
	}
	
	@Column(name = "VIBRATING")
	public String getVibrating() {
		return vibrating;
	}
	public void setVibrating(String vibrating) {
		this.vibrating = vibrating;
	}
}
