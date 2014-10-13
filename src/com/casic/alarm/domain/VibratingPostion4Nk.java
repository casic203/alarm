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
@Table(name = "NK_VIB_POSITION")
@SequenceGenerator(name = "SEQ_NK_VIB_POSITION_ID", sequenceName = "SEQ_NK_VIB_POSITION_ID")
public class VibratingPostion4Nk implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5602557270122228839L;

	private Long id;
	private Long devId;
	private Double distance;
	private Double longitude;
	private Double latitude;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_NK_VIB_POSITION_ID")
	@Column(name = "DBID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "DEV_ID", nullable = false)
	public Long getDevId() {
		return devId;
	}

	public void setDevId(Long devId) {
		this.devId = devId;
	}

	@Column(name = "DIST", nullable = false)
	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	@Column(name = "LONGITUDE", nullable = false)
	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	@Column(name = "LATITUDE", nullable = false)
	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

}
