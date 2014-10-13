package com.casic.alarm.domain;
// default package

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * AlarmDeviceSconfigId entity. @author MyEclipse Persistence Tools
 */
@Embeddable
public class DeviceSconfigId implements java.io.Serializable {

	// Fields

	private String sensorcode;
	private String itemname;

	// Constructors

	/** default constructor */
	public DeviceSconfigId() {
	}

	/** full constructor */
	public DeviceSconfigId(String sensorcode, String itemname) {
		this.sensorcode = sensorcode;
		this.itemname = itemname;
	}

	// Property accessors

	@Column(name = "SENSORCODE")
	public String getSensorcode() {
		return this.sensorcode;
	}

	public void setSensorcode(String sensorcode) {
		this.sensorcode = sensorcode;
	}

	@Column(name = "ITEMNAME")
	public String getItemname() {
		return this.itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof DeviceSconfigId))
			return false;
		DeviceSconfigId castOther = (DeviceSconfigId) other;

		return ((this.getSensorcode() == castOther.getSensorcode()) || (this
				.getSensorcode() != null && castOther.getSensorcode() != null && this
				.getSensorcode().equals(castOther.getSensorcode())))
				&& ((this.getItemname() == castOther.getItemname()) || (this
						.getItemname() != null
						&& castOther.getItemname() != null && this
						.getItemname().equals(castOther.getItemname())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getSensorcode() == null ? 0 : this.getSensorcode()
						.hashCode());
		result = 37 * result
				+ (getItemname() == null ? 0 : this.getItemname().hashCode());
		return result;
	}

}