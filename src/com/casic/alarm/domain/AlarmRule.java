package com.casic.alarm.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * AlarmAlarmRule entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ALARM_ALARM_RULE", schema = "SCOTT")
@SequenceGenerator(name = "SEQ_ALARM_RULE_ID", sequenceName = "SEQ_ALARM_RULE_ID")
public class AlarmRule implements java.io.Serializable {

	// Fields

	private Long id;
	private String deviceid;
	private String paramcode;
	private String paramname;
	private String secullval;
	private String seculval;
	private String secuokval;
	private String secuhval;
	private String secuhhval;
	private String sensorcode;

	// Constructors

	/** default constructor */
	public AlarmRule() {
	}

	/** minimal constructor */
	public AlarmRule(Long id) {
		this.id = id;
	}

	/** full constructor */
	public AlarmRule(Long id, String deviceid, String paramcode,
			String paramname, String secullval, String seculval,
			String secuokval, String secuhval, String secuhhval,String sensorcode) {
		this.id = id;
		this.deviceid = deviceid;
		this.paramcode = paramcode;
		this.paramname = paramname;
		this.secullval = secullval;
		this.seculval = seculval;
		this.secuokval = secuokval;
		this.secuhval = secuhval;
		this.secuhhval = secuhhval;
		this.sensorcode = sensorcode;
	}

	// Property accessors
	@Id
	@Column(name = "DBID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ALARM_RULE_ID")
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "DEVICEID")
	public String getDeviceid() {
		return this.deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	@Column(name = "PARAMCODE")
	public String getParamcode() {
		return this.paramcode;
	}

	public void setParamcode(String paramcode) {
		this.paramcode = paramcode;
	}

	@Column(name = "PARAMNAME")
	public String getParamname() {
		return this.paramname;
	}

	public void setParamname(String paramname) {
		this.paramname = paramname;
	}

	@Column(name = "SECULLVAL")
	public String getSecullval() {
		return this.secullval;
	}

	public void setSecullval(String secullval) {
		this.secullval = secullval;
	}

	@Column(name = "SECULVAL")
	public String getSeculval() {
		return this.seculval;
	}

	public void setSeculval(String seculval) {
		this.seculval = seculval;
	}

	@Column(name = "SECUOKVAL")
	public String getSecuokval() {
		return this.secuokval;
	}

	public void setSecuokval(String secuokval) {
		this.secuokval = secuokval;
	}

	@Column(name = "SECUHVAL")
	public String getSecuhval() {
		return this.secuhval;
	}

	public void setSecuhval(String secuhval) {
		this.secuhval = secuhval;
	}

	@Column(name = "SECUHHVAL")
	public String getSecuhhval() {
		return this.secuhhval;
	}

	public void setSecuhhval(String secuhhval) {
		this.secuhhval = secuhhval;
	}
	@Column(name = "SENSORCODE")
	public String getSensorcode() {
		return sensorcode;
	}

	public void setSensorcode(String sensorcode) {
		this.sensorcode = sensorcode;
	}

}