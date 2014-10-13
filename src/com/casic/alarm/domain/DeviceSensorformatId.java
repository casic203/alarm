package com.casic.alarm.domain;
// default package

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * AlarmDeviceSensorformatId entity. @author MyEclipse Persistence Tools
 */
@Embeddable
public class DeviceSensorformatId implements java.io.Serializable {

	// Fields

	private String sensortype;
	private String itemname;

	// Constructors

	/** default constructor */
	public DeviceSensorformatId() {
	}

	/** full constructor */
	public DeviceSensorformatId(String sensortype, String itemname) {
		this.sensortype = sensortype;
		this.itemname = itemname;
	}

	// Property accessors

	@Column(name = "SENSORTYPE", nullable = false, length = 4)
	public String getSensortype() {
		return this.sensortype;
	}

	public void setSensortype(String sensortype) {
		this.sensortype = sensortype;
	}

	@Column(name = "ITEMNAME", nullable = false, length = 30)
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
		if (!(other instanceof DeviceSensorformatId))
			return false;
		DeviceSensorformatId castOther = (DeviceSensorformatId) other;

		return ((this.getSensortype() == castOther.getSensortype()) || (this
				.getSensortype() != null && castOther.getSensortype() != null && this
				.getSensortype().equals(castOther.getSensortype())))
				&& ((this.getItemname() == castOther.getItemname()) || (this
						.getItemname() != null
						&& castOther.getItemname() != null && this
						.getItemname().equals(castOther.getItemname())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getSensortype() == null ? 0 : this.getSensortype()
						.hashCode());
		result = 37 * result
				+ (getItemname() == null ? 0 : this.getItemname().hashCode());
		return result;
	}

}