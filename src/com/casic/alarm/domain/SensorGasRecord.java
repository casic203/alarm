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
@Table(name = "JG_RQ_GAS")
@SequenceGenerator(name = "SEQ_JG_RQ_GAS_ID", sequenceName = "SEQ_JG_RQ_GAS_ID")
public class SensorGasRecord implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8677020890600953095L;
	private Long dbId;
	private String devId;
	private String leakData;
	private String inPress;
	private String outPress;
	private String tempGas;
	private String tempRoom;
	private String cellPower;
	private String recordTime;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_JG_RQ_GAS_ID")
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
	
	@Column(name = "LEAKDATA")
	public String getLeakData() {
		return leakData;
	}
	public void setLeakData(String leakData) {
		this.leakData = leakData;
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
	
	@Column(name = "TEMPGAS")
	public String getTempGas() {
		return tempGas;
	}
	public void setTempGas(String tempGas) {
		this.tempGas = tempGas;
	}
	
	@Column(name = "TEMPROOM")
	public String getTempRoom() {
		return tempRoom;
	}
	public void setTempRoom(String tempRoom) {
		this.tempRoom = tempRoom;
	}
	
	@Column(name = "CELLPOWER")
	public String getCellPower() {
		return cellPower;
	}
	public void setCellPower(String cellPower) {
		this.cellPower = cellPower;
	}
	
	@Column(name = "RECORDTIME")
	public String getRecordTime() {
		return recordTime;
	}
	public void setRecordTime(String recordTime) {
		this.recordTime = recordTime;
	}
}
