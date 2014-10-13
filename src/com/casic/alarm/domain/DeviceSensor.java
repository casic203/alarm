package com.casic.alarm.domain;
 

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
 * AlarmDeviceSensor entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ALARM_DEVICE_SENSOR")
public class DeviceSensor implements java.io.Serializable {

	// Fields

	private DeviceSensorId id;

	private Device device;
	private SensorType sensorType;

	// Constructors

	/** default constructor */
	public DeviceSensor() {
	}

	/** full constructor */
	public DeviceSensor(DeviceSensorId id, Device device,
			SensorType sensorType) {
		this.id = id;
		this.device = device;
		this.sensorType = sensorType;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "sensorcode", column = @Column(name = "SENSORCODE", nullable = false, length = 4)),
			@AttributeOverride(name = "sensorid", column = @Column(name = "SENSORID", nullable = false)),
			@AttributeOverride(name = "deviceid", column = @Column(name = "DEVICEID", nullable = false, scale = 0)) })
	public DeviceSensorId getId() {
		return this.id;
	}

	public void setId(DeviceSensorId id) {
		this.id = id;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEVICEID", nullable = false, insertable = false, updatable = false)
	public Device getDevice() {
		return this.device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SENSORCODE", nullable = false, insertable = false, updatable = false)
	public SensorType getSensorType() {
		return this.sensorType;
	}

	public void setSensorType(SensorType sensorType) {
		this.sensorType = sensorType;
	}
}