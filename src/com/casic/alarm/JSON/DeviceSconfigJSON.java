package com.casic.alarm.JSON;
 

public class DeviceSconfigJSON { 
	
//	private Long id;
	private String sensorcode; 
	private String sensorname;
	
	private String itemid;
	private Long itemnum;
	private String itemname;
	private String itemdatatype;
	private String itemvalue;
	
	public String getSensorname() {
		return sensorname;
	}
	public void setSensorname(String sensorname) {
		this.sensorname = sensorname;
	}
//	public Long getId() {
//		return id;
//	}
//	public void setId(Long id) {
//		this.id = id;
//	}
	public String getItemvalue() {
		return itemvalue;
	}
	public void setItemvalue(String itemvalue) {
		this.itemvalue = itemvalue;
	}
	public String getSensorcode() {
		return sensorcode;
	}
	public void setSensorcode(String sensorcode) {
		this.sensorcode = sensorcode;
	}
	public String getItemid() {
		return itemid;
	}
	public void setItemid(String itemid) {
		this.itemid = itemid;
	} 
	public Long getItemnum() {
		return itemnum;
	}
	public void setItemnum(Long itemnum) {
		this.itemnum = itemnum;
	}
	public String getItemname() {
		return itemname;
	}
	public void setItemname(String itemname) {
		this.itemname = itemname;
	}
	public String getItemdatatype() {
		return itemdatatype;
	}
	public void setItemdatatype(String itemdatatype) {
		this.itemdatatype = itemdatatype;
	} 

}
