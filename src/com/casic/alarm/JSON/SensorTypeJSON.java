package com.casic.alarm.JSON;
  

public class SensorTypeJSON {
//	private Long id; 
	private String sensorcode;
	private String sensorname; 
	private Boolean isuse;
	private String defaultid;  
	  
	 
	public Boolean getIsuse() {
		return isuse;
	}
	public void setIsuse(Boolean isuse) {
		this.isuse = isuse;
	}
	public String getDefaultid() {
		return defaultid;
	}
	public void setDefaultid(String defaultid) {
		this.defaultid = defaultid;
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
