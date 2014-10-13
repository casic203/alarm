//<<<<<<< HEAD
//package com.casic.alarm.form;
//
//import java.io.Serializable;
//
//public class DeviceRealTimeDataForm implements Serializable {
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = -3664457781065775238L;
//	private String devCode = "";
//	private String dataType = "";
//	private String upTime="";
//	
//	public String getUpTime() {
//		return upTime;
//	}
//
//	public void setUpTime(String upTime) {
//		this.upTime = upTime;
//	}
//
//	public String getEndTime() {
//		return endTime;
//	}
//
//	public void setEndTime(String endTime) {
//		this.endTime = endTime;
//	}
//
//	private String endTime="";
//
//	public String getDevCode() {
//		return devCode;
//	}
//
//	public void setDevCode(String devCode) {
//		this.devCode = devCode;
//	}
//
//	public String getDataType() {
//		return dataType;
//	}
//
//	public void setDataType(String dataType) {
//		this.dataType = dataType;
//	}
//}
//=======
package com.casic.alarm.form;

import java.io.Serializable;

public class DeviceRealTimeDataForm implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3664457781065775238L;
	private String devCode = "";
	private String dataType = "";
	private String dbCord = "";
	private String upTime="";
	private String endTime="";
	
	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getUpTime() {
		return upTime;
	}

	public void setUpTime(String upTime) {
		this.upTime = upTime;
	}

	public String getDevCode() {
		return devCode;
	}

	public void setDevCode(String devCode) {
		this.devCode = devCode;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	public String getDbCord() {
		return dbCord;
	}

	public void setDbCord(String dbCord) {
		this.dbCord = dbCord;
	}
}
//>>>>>>> refs/remotes/origin/master
