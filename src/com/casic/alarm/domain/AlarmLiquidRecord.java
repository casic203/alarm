//<<<<<<< HEAD
//package com.casic.alarm.domain;
//
//import java.io.Serializable;
//import java.sql.Timestamp;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.SequenceGenerator;
//import javax.persistence.Table;
//
//@Entity
//@Table(name = "ALARM_LIQUID_RECORD")
//@SequenceGenerator(name = "SEQ_ALARM_LIQUID_RECORD_ID", sequenceName = "SEQ_ALARM_LIQUID_RECORD_ID")
//public class AlarmLiquidRecord implements Serializable{
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = -2800192556432667090L;
//	private Long dbId;	
//	private String dbcord; 
//	private double liquid; 	 
//	private Timestamp  upTime;
//	private long diameter;
//	private long rainnum;
//	private int cordType;
//	
//	@Column(name = "CORDTYPE")
//	public int getCordType() {
//		return cordType;
//	}
//	public void setCordType(int cordType) {
//		this.cordType = cordType;
//	}
//	@Column(name = "RAINNUM")
//	public long getRainnum() {
//		return rainnum;
//	}
//	public void setRainnum(long rainnum) {
//		this.rainnum = rainnum;
//	}
//	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ALARM_LIQUID_RECORD_ID")
//	@Column(name = "DBID")
//	public Long getDbId() {
//		return dbId;
//	}
//	public void setDbId(Long dbId) {
//		this.dbId = dbId;
//	}
//	@Column(name = "DBCORD")
//	public String getDbcord() {
//		return dbcord;
//	}
//	public void setDbcord(String dbcord) {
//		this.dbcord = dbcord;
//	}
//	@Column(name = "LIQUID")
//	public double getLiquid() {
//		return liquid;
//	}
//	public void setLiquid(double liquid) {
//		this.liquid = liquid;
//	}
//	@Column(name = "UPTIME")
//	public Timestamp getUpTime() {
//		return upTime;
//	}
//	public void setUpTime(Timestamp upTime) {
//		this.upTime = upTime;
//	}
//	@Column(name = "DIAMETER")
//	public long getDiameter() {
//		return diameter;
//	}
//	public void setDiameter(long diameter) {
//		this.diameter = diameter;
//	}	
//
//}
//======= 
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
@Table(name = "ALARM_LIQUID_RECORD")
@SequenceGenerator(name = "SEQ_ALARM_LIQUID_RECORD_ID", sequenceName = "SEQ_ALARM_LIQUID_RECORD_ID")
public class AlarmLiquidRecord implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2800192556432667090L;

	private Long dbId;	
	private String dbcord; 
	private double liquid; 	 
	private Timestamp  upTime;
	private long diameter;
	private int rainNum;
	private int cordType;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ALARM_LIQUID_RECORD_ID")
	@Column(name = "DBID")
	public Long getDbId() {
		return dbId;
	}
	public void setDbId(Long dbId) {
		this.dbId = dbId;
	}
	@Column(name = "DBCORD")
	public String getDbcord() {
		return dbcord;
	}
	public void setDbcord(String dbcord) {
		this.dbcord = dbcord;
	}
	@Column(name = "LIQUID")
	public double getLiquid() {
		return liquid;
	}
	public void setLiquid(double liquid) {
		this.liquid = liquid;
	}
	@Column(name = "UPTIME")
	public Timestamp getUpTime() {
		return upTime;
	}
	public void setUpTime(Timestamp upTime) {
		this.upTime = upTime;
	}
	@Column(name = "DIAMETER")
	public long getDiameter() {
		return diameter;
	}
	public void setDiameter(long diameter) {
		this.diameter = diameter;
	}
	@Column(name = "RAINNUM")
	public int getRainNum() {
		return rainNum;
	}
	public void setRainNum(int rainNum) {
		this.rainNum = rainNum;
	}
	@Column(name = "CORDTYPE")
	public int getCordType() {
		return cordType;
	}
	public void setCordType(int cordType) {
		this.cordType = cordType;
	}
}

