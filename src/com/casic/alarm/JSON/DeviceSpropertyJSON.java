package com.casic.alarm.JSON;

import javax.persistence.Column;

public class DeviceSpropertyJSON {
	private Long id;
	private String standardcol;
	private String standardcolname;
	private String coldatatype;
	private Long collen;
	private Long datausetype;
	private Boolean status;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getStandardcol() {
		return standardcol;
	}
	public void setStandardcol(String standardcol) {
		this.standardcol = standardcol;
	}
	public String getStandardcolname() {
		return standardcolname;
	}
	public void setStandardcolname(String standardcolname) {
		this.standardcolname = standardcolname;
	}
	public String getColdatatype() {
		return coldatatype;
	}
	public void setColdatatype(String coldatatype) {
		this.coldatatype = coldatatype;
	}
	public Long getCollen() {
		return collen;
	}
	public void setCollen(Long collen) {
		this.collen = collen;
	}
	public Long getDatausetype() {
		return datausetype;
	}
	public void setDatausetype(Long datausetype) {
		this.datausetype = datausetype;
	} 
	public Boolean getStatus() {
		return this.status;
	} 
	public void setStatus(Boolean status) {
		this.status = status;
	}
}
