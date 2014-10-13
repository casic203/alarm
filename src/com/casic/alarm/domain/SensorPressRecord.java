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
@Table(name = "AD_DJ_PRESS")
@SequenceGenerator(name = "SEQ_AD_DJ_PRESS_ID", sequenceName = "SEQ_AD_DJ_PRESS_ID")
public class SensorPressRecord implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 429994083551525856L;
	private Long dbId;
	private String devId;
	private String pressData;
	private String cell;
	private String signal;
	private String status;
	private Date uptime;
	private Date logtime;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AD_DJ_PRESS_ID")
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
	
	@Column(name = "PRESSDATA")
	public String getPressData() {
		return pressData;
	}
	public void setPressData(String pressData) {
		this.pressData = pressData;
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
	public Date getUptime() {
		return uptime;
	}
	public void setUptime(Date uptime) {
		this.uptime = uptime;
	}
	
	@Column(name = "LOGTIME")
	public Date getLogtime() {
		return logtime;
	}
	public void setLogtime(Date logtime) {
		this.logtime = logtime;
	}
}
