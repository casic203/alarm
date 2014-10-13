package com.casic.alarm.JSON;
  
public class DeviceConfigJSON {
	private Long id;
	private String devCode;
	private String devName; 
	private String typeName;
	private Boolean active;
	  
	private String recordTime;
	private String Dbegin;
	private String Dinterval;
	private String Dcount;
	private String Lbegin;
	private String Linterval;
	private String Lcount;
	private String Warelessopen;
	private String Warelessclose; 
	 
	public String getRecordTime() {
		return recordTime;
	}
	public void setRecordTime(String recordTime) {
		this.recordTime = recordTime;
	}
	public String getDbegin() {
		return Dbegin;
	}
	public void setDbegin(String dbegin) {
		Dbegin = dbegin;
	}
	public String getDinterval() {
		return Dinterval;
	}
	public void setDinterval(String dinterval) {
		Dinterval = dinterval;
	}
	public String getDcount() {
		return Dcount;
	}
	public void setDcount(String dcount) {
		Dcount = dcount;
	}
	public String getLbegin() {
		return Lbegin;
	}
	public void setLbegin(String lbegin) {
		Lbegin = lbegin;
	}
	public String getLinterval() {
		return Linterval;
	}
	public void setLinterval(String linterval) {
		Linterval = linterval;
	}
	public String getLcount() {
		return Lcount;
	}
	public void setLcount(String lcount) {
		Lcount = lcount;
	}
	public String getWarelessopen() {
		return Warelessopen;
	}
	public void setWarelessopen(String warelessopen) {
		Warelessopen = warelessopen;
	}
	public String getWarelessclose() {
		return Warelessclose;
	}
	public void setWarelessclose(String warelessclose) {
		Warelessclose = warelessclose;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDevCode() {
		return devCode;
	}
	public void setDevCode(String devCode) {
		this.devCode = devCode;
	}
	public String getDevName() {
		return devName;
	}
	public void setDevName(String devName) {
		this.devName = devName;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	} 
	
	
}
