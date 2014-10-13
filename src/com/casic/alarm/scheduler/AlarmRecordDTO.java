package com.casic.alarm.scheduler;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.casic.alarm.domain.Device;

public class AlarmRecordDTO implements Serializable
{
	private static final long serialVersionUID = 1658147150462234453L;

	private BigDecimal id;
	private Short active;
	private String itemname;
	private String tiemvalue;
	public String getTiemvalue() {
		return tiemvalue;
	}
	public void setTiemvalue(String tiemvalue) {
		this.tiemvalue = tiemvalue;
	}
	private String message;
	private Long messageStatus;
	private String recordcode;
	private String recorddate;
	private BigDecimal deviceId;
	
	
	public BigDecimal getId() {
		return id;
	}
	public void setId(BigDecimal id) {
		this.id = id;
	}
	public Short getActive() {
		return active;
	}
	public void setActive(Short active) {
		this.active = active;
	}
	public String getItemname() {
		return itemname;
	}
	public void setItemname(String itemname) {
		this.itemname = itemname;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Long getMessageStatus() {
		return messageStatus;
	}
	public void setMessageStatus(Long messageStatus) {
		this.messageStatus = messageStatus;
	}
	public String getRecordcode() {
		return recordcode;
	}
	public void setRecordcode(String recordcode) {
		this.recordcode = recordcode;
	}
	public String getRecorddate() {
		return recorddate;
	}
	public void setRecorddate(String recorddate) {
		this.recorddate = recorddate;
	}
	public BigDecimal getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(BigDecimal deviceId) {
		this.deviceId = deviceId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
