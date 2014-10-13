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
 * AlarmDeviceSproperty entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ALARM_DEVICE_SPROPERTY")
@SequenceGenerator(name = "SEQ_DEVICE_SPROPERTY_ID", sequenceName = "SEQ_DEVICE_SPROPERTY_ID")
public class DeviceSproperty implements java.io.Serializable {

	// Fields

	private Long id;
	private String standardcol;
	private String standardcolname;
	private String coldatatype;
	private Long collen;
	private Long datausetype;
	private Boolean status;

	// Constructors

	/** default constructor */
	public DeviceSproperty() {
	}

	/** minimal constructor */
	public DeviceSproperty(Long id, String standardcol) {
		this.id = id;
		this.standardcol = standardcol;
	}

	/** full constructor */
	public DeviceSproperty(Long id, String standardcol,
			String standardcolname, String coldatatype, Long collen,
			Long datausetype, Boolean status) {
		this.id = id;
		this.standardcol = standardcol;
		this.standardcolname = standardcolname;
		this.coldatatype = coldatatype;
		this.collen = collen;
		this.datausetype = datausetype;
		this.status = status;
	}

	// Property accessors
	@Id
	@Column(name = "DBID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_DEVICE_SPROPERTY_ID")
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "STANDARDCOL")
	public String getStandardcol() {
		return this.standardcol;
	}

	public void setStandardcol(String standardcol) {
		this.standardcol = standardcol;
	}

	@Column(name = "STANDARDCOLNAME")
	public String getStandardcolname() {
		return this.standardcolname;
	}

	public void setStandardcolname(String standardcolname) {
		this.standardcolname = standardcolname;
	}

	@Column(name = "COLDATATYPE")
	public String getColdatatype() {
		return this.coldatatype;
	}

	public void setColdatatype(String coldatatype) {
		this.coldatatype = coldatatype;
	}

	@Column(name = "COLLEN")
	public Long getCollen() {
		return this.collen;
	}

	public void setCollen(Long collen) {
		this.collen = collen;
	}

	@Column(name = "DATAUSETYPE")
	public Long getDatausetype() {
		return this.datausetype;
	}

	public void setDatausetype(Long datausetype) {
		this.datausetype = datausetype;
	}

	@Column(name = "STATUS")
	public Boolean getStatus() {
		return this.status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

}