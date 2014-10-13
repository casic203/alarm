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
@Table(name = "AD_SL_NOISE")
@SequenceGenerator(name = "SEQ_AD_SL_NOISE_ID", sequenceName = "SEQ_AD_SL_NOISE_ID")
public class SensorNoiseRecordV2 implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8736214079553329858L;
	private Long dbId;
	private String srcId;
	private String denseData;
	private String tagId;
	private String looseData;
	private String dBegin;
	private String dInterval;
	private String dCount;
	private String lBegin;
	private String lInterval;
	private String lCount;
	private String wirelessOpen;
	private String wirelessClose;
	private Date uptime;
	private Date logtime;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AD_SL_NOISE_ID")
	@Column(name = "DBID")
	public Long getDbId() {
		return dbId;
	}
	public void setDbId(Long dbId) {
		this.dbId = dbId;
	}
	
	@Column(name = "LOOSEDATA")
	public String getLooseData() {
		return looseData;
	}
	public void setLooseData(String looseData) {
		this.looseData = looseData;
	}
	
	@Column(name = "DENSEDATA")
	public String getDenseData() {
		return denseData;
	}
	public void setDenseData(String denseData) {
		this.denseData = denseData;
	}
	
	@Column(name = "SRCID")
	public String getSrcId() {
		return srcId;
	}
	public void setSrcId(String srcId) {
		this.srcId = srcId;
	}
	
	@Column(name = "TAGID")
	public String getTagId() {
		return tagId;
	}
	public void setTagId(String tagId) {
		this.tagId = tagId;
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
	
	@Column(name = "WIRELESSOPEN")
	public String getWirelessOpen() {
		return wirelessOpen;
	}
	public void setWirelessOpen(String wirelessOpen) {
		this.wirelessOpen = wirelessOpen;
	}
	
	@Column(name = "WIRELESSCLOSE")
	public String getWirelessClose() {
		return wirelessClose;
	}
	public void setWirelessClose(String wirelessClose) {
		this.wirelessClose = wirelessClose;
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
