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
@Table(name = "AD_DJ_NOISE")
@SequenceGenerator(name = "SEQ_AD_DJ_NOISE_ID", sequenceName = "SEQ_AD_DJ_NOISE_ID")
public class SensorNoiseRecord implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8736214079553329858L;
	private Long dbId;
	private String devId;
	private String lData;
	private String dData;
	private String dBegin;
	private String dInterval;
	private String dCount;
	private String lBegin;
	private String lInterval;
	private String lCount;
	private String warelessOpen;
	private String warelessClose;
	private String cell;
	private String signal;
	private String status;
	private Date uptime;
	private Date logtime;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AD_DJ_NOISE_ID")
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
	
	@Column(name = "LDATA")
	public String getlData() {
		return lData;
	}
	public void setlData(String lData) {
		this.lData = lData;
	}
	
	@Column(name = "DDATA")
	public String getdData() {
		return dData;
	}
	public void setdData(String dData) {
		this.dData = dData;
	}
	
	@Column(name = "DBEGIN")
	public String getdBegin() {
		return dBegin;
	}
	public void setdBegin(String dBegin) {
		this.dBegin = dBegin;
	}
	
	@Column(name = "DINTERVAL")
	public String getdInterval() {
		return dInterval;
	}
	public void setdInterval(String dInterval) {
		this.dInterval = dInterval;
	}
	
	@Column(name = "DCOUNT")
	public String getdCount() {
		return dCount;
	}
	public void setdCount(String dCount) {
		this.dCount = dCount;
	}
	
	@Column(name = "LBEGIN")
	public String getlBegin() {
		return lBegin;
	}
	public void setlBegin(String lBegin) {
		this.lBegin = lBegin;
	}
	
	@Column(name = "LINTERVAL")
	public String getlInterval() {
		return lInterval;
	}
	public void setlInterval(String lInterval) {
		this.lInterval = lInterval;
	}
	
	@Column(name = "LCOUNT")
	public String getlCount() {
		return lCount;
	}
	public void setlCount(String lCount) {
		this.lCount = lCount;
	}
	
	@Column(name = "WARELESSOPEN")
	public String getWarelessOpen() {
		return warelessOpen;
	}
	public void setWarelessOpen(String warelessOpen) {
		this.warelessOpen = warelessOpen;
	}
	
	@Column(name = "WARELESSCLOSE")
	public String getWarelessClose() {
		return warelessClose;
	}
	public void setWarelessClose(String warelessClose) {
		this.warelessClose = warelessClose;
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
