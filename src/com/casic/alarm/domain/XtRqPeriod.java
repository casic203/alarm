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
@Table(name = "XT_RQ_PERIOD")
@SequenceGenerator(name = "SEQ_XT_RQ_PERIOD", sequenceName = "SEQ_XT_RQ_PERIOD")
public class XtRqPeriod implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 566089135526318711L;
	private Long dbId;
	private String address; //设备ID
	private String inPress; //进站压力
	private String outPress;//出站压力
	private String flow;    //流量
	private String strength; //浓度
	private String temperature;//温度
	private String cell;       //电池电量
	private Date uptime;       //采集时间
	private Date logtime;      //记录时间
	 
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_XT_RQ_PERIOD")
	@Column(name = "DBID")
	public Long getDbId() {
		return dbId;
	}
	public void setDbId(Long dbId) {
		this.dbId = dbId;
	}
	
	@Column(name = "ADDRESS")
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	@Column(name = "INPRESS")
	public String getInPress() {
		return inPress;
	}
	public void setInPress(String inPress) {
		this.inPress = inPress;
	}
	
	@Column(name = "OUTPRESS")
	public String getOutPress() {
		return outPress;
	}
	public void setOutPress(String outPress) {
		this.outPress = outPress;
	}
	
	@Column(name = "FLOW")
	public String getFlow() {
		return flow;
	}
	public void setFlow(String flow) {
		this.flow = flow;
	}
	
	@Column(name = "STRENGTH")
	public String getStrength() {
		return strength;
	}
	public void setStrength(String strength) {
		this.strength = strength;
	}
	
	@Column(name = "TEMPERATURE")
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	
	@Column(name = "CELL")
	public String getCell() {
		return cell;
	}
	public void setCell(String cell) {
		this.cell = cell;
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
