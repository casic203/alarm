package com.casic.alarm.domain;
// default package

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * AlarmDeviceConfig entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ALARM_DEVICE_CONFIG")
@SequenceGenerator(name = "SEQ_DEVICE_CONFIG_DBID", sequenceName = "SEQ_DEVICE_CONFIG_DBID")
public class DeviceConfig implements java.io.Serializable {

	// Fields
 
	private Long dbid;
	private String devid;
	private String sensorid;
	private Date writetime;
	private String framecontent;
	private Boolean status;
	private Date sendtime;

	// Constructors

	/** default constructor */
	public DeviceConfig() {
	}

	/** minimal constructor */
	public DeviceConfig(Long dbid) {
		this.dbid = dbid;
	}

	/** full constructor */
	public DeviceConfig(Long dbid, String devid, String sensorid,
			Date writetime, String framecontent, Boolean status,
			Date sendtime) {
		this.dbid = dbid;
		this.devid = devid;
		this.sensorid = sensorid;
		this.writetime = writetime;
		this.framecontent = framecontent;
		this.status = status;
		this.sendtime = sendtime;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_DEVICE_CONFIG_DBID")
	@Column(name = "DBID")   
	public Long getDbid() {
		return this.dbid;
	}

	public void setDbid(Long dbid) {
		this.dbid = dbid;
	}
 
	@Column(name = "DEVID")
	public String getDevid() {
		return this.devid;
	}

	public void setDevid(String devid) {
		this.devid = devid;
	}

	@Column(name = "SENSORID")
	public String getSensorid() {
		return this.sensorid;
	}

	public void setSensorid(String sensorid) {
		this.sensorid = sensorid;
	}

 
	@Column(name = "WRITETIME")
	public Date getWritetime() {
		return this.writetime;
	}

	public void setWritetime(Date writetime) {
		this.writetime = writetime;
	}

	@Column(name = "FRAMECONTENT")
	public String getFramecontent() {
		return this.framecontent;
	}

	public void setFramecontent(String framecontent) {
		this.framecontent = framecontent;
	}

	@Column(name = "STATUS")
	public Boolean getStatus() {
		return this.status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	@Column(name = "SENDTIME")
	public Date getSendtime() {
		return this.sendtime;
	}

	public void setSendtime(Date sendtime) {
		this.sendtime = sendtime;
	}
}