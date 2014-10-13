package com.casic.alarm.domain;
// default package

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * AlarmDeviceSconfig entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ALARM_DEVICE_SCONFIG")
public class DeviceSconfig implements java.io.Serializable {

	// Fields

	private DeviceSconfigId dsid;
	
	private SensorType sensortype;
	private Long itemnum;
	private String itemdatatype;
	private String itemvalue;

	// Constructors

	/** default constructor */
	public DeviceSconfig() {
	}

	/** minimal constructor */
	public DeviceSconfig(DeviceSconfigId id, SensorType sensortype,
			Long itemnum) {
		this.dsid = id;
		this.sensortype = sensortype;
		this.itemnum = itemnum;
	}

	/** full constructor */
	public DeviceSconfig(DeviceSconfigId id, SensorType sensortype,
			Long itemnum, String itemdatatype, String itemvalue) {
		this.dsid = id;
		this.sensortype = sensortype;
		this.itemnum = itemnum;
		this.itemdatatype = itemdatatype;
		this.itemvalue = itemvalue;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "sensorcode", column = @Column(name = "SENSORCODE", nullable = false, length = 4)),
			@AttributeOverride(name = "itemname", column = @Column(name = "ITEMNAME", nullable = false, length = 32)) })
	public DeviceSconfigId getDsid() {
		return this.dsid;
	}

	public void setDsid(DeviceSconfigId id) {
		this.dsid = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SENSORCODE", nullable = false, insertable = false, updatable = false)
	public SensorType getSensortype() {
		return this.sensortype;
	}

	public void setSensortype(SensorType sensortype) {
		this.sensortype = sensortype;
	}

	@Column(name = "ITEMNUM")
	public Long getItemnum() {
		return this.itemnum;
	}

	public void setItemnum(Long itemnum) {
		this.itemnum = itemnum;
	}

	@Column(name = "ITEMDATATYPE")
	public String getItemdatatype() {
		return this.itemdatatype;
	}

	public void setItemdatatype(String itemdatatype) {
		this.itemdatatype = itemdatatype;
	}

	@Column(name = "ITEMVALUE")
	public String getItemvalue() {
		return this.itemvalue;
	}

	public void setItemvalue(String itemvalue) {
		this.itemvalue = itemvalue;
	}

}