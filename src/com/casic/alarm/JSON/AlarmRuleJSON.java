package com.casic.alarm.JSON;

public class AlarmRuleJSON {


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
	
	
	public String getSensorcode() {
		return sensorcode;
	}
	public void setSensorcode(String sensorcode) {
		this.sensorcode = sensorcode;
	}
	public String getSecullval() {
		return secullval;
	}
	public void setSecullval(String secullval) {
		this.secullval = secullval;
	}
	public String getSeculval() {
		return seculval;
	}
	public void setSeculval(String seculval) {
		this.seculval = seculval;
	}
	public String getSecuokval() {
		return secuokval;
	}
	public void setSecuokval(String secuokval) {
		this.secuokval = secuokval;
	}
	public String getSecuhval() {
		return secuhval;
	}
	public void setSecuhval(String secuhval) {
		this.secuhval = secuhval;
	}
	public String getSecuhhval() {
		return secuhhval;
	}
	public void setSecuhhval(String secuhhval) {
		this.secuhhval = secuhhval;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDeviceid() {
		return deviceid;
	}
	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}
	public String getParamcode() {
		return paramcode;
	}
	public void setParamcode(String paramcode) {
		this.paramcode = paramcode;
	}
	public String getParamname() {
		return paramname;
	}
	public void setParamname(String paramname) {
		this.paramname = paramname;
	} 
}
