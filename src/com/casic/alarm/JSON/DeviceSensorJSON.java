package com.casic.alarm.JSON;
  

public class DeviceSensorJSON { 
	private String sensorcode;
	private String sensorid; 
	private Long deviceid;
	
	private Boolean isuse; 
	private String devcode;
	private String devname;
	private String sensorname;
	 
	 
	public String getSensorid() {
		return sensorid;
	}
	public void setSensorid(String sensorid) {
		this.sensorid = sensorid;
	}
	public String getDevcode() {
		return devcode;
	}
	public void setDevcode(String devcode) {
		this.devcode = devcode;
	}
	public String getDevname() {
		return devname;
	}
	public void setDevname(String devname) {
		this.devname = devname;
	}
	public Long getDeviceid() {
		return deviceid;
	}
	public void setDeviceid(Long deviceid) {
		this.deviceid = deviceid;
	}
	public Boolean getIsuse() {
		return isuse;
	}
	public void setIsuse(Boolean isuse) {
		this.isuse = isuse;
	}
	  
	public String getSensorcode() {
		return sensorcode;
	}
	public void setSensorcode(String sensorcode) {
		this.sensorcode = sensorcode;
	}
	public String getSensorname() {
		return sensorname;
	}
	public void setSensorname(String sensorname) {
		this.sensorname = sensorname;
	} 
	 
	
}
