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
@Table(name = "AD_DJ_LIQUID")
@SequenceGenerator(name = "SEQ_AD_DJ_LIQUID", sequenceName = "SEQ_AD_DJ_LIQUID")
public class AdDjLiquid implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6250930880206117358L;
	private Long dbId;
	private String devId;
	private String liquidData;
	private String cell;
	private String signal;
	private String status;
	private Timestamp uptime;
	private Timestamp logtime;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AD_DJ_LIQUID")
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
	
	@Column(name = "LIQUIDDATA")
	public String getLiquidData() {
		return liquidData;
	}
	public void setLiquidData(String liquidData) {
		this.liquidData = liquidData;
	}
	
	@Column(name = "CELL")
	public String getCell() {
		return cell;
	}
	public void setCell(String cell) {
		this.cell = cell;
	}
	
	@Column(name = "SIGNAL")
	public String getSignal() {
		return signal;
	}
	public void setSignal(String signal) {
		this.signal = signal;
	}
	
	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
