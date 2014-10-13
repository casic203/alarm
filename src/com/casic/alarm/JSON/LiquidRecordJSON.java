////<<<<<<< HEAD
//package com.casic.alarm.JSON;
//
//import java.sql.Timestamp;
//import java.util.Date;
//
//public class LiquidRecordJSON {	 
//	private String devId;
//	private String  liquidData;
//	
//	private String liquidPower;
//	private long upTime;
//	private long diameter;
//	private String time;
////	private Timestamp logTime;
//	
//	public String getLiquidData() {
//		return liquidData;
//	}
//	public void setLiquidData(String liquidData) {
//		this.liquidData = liquidData;
//	}
//	public String getTime() {
//		return time;
//	}
//	public void setTime(String time) {
//		this.time = time;
//	}
//	public long getDiameter() {
//		return diameter;
//	}
//	public void setDiameter(long diameter) {
//		this.diameter = diameter;
//	}
//	public String getDevId() {
//		return devId;
//	}
//	public void setDevId(String devId) {
//		this.devId = devId;
//	}
//	
//	public String getLiquidPower() {
//		return liquidPower;
//	}
//	public void setLiquidPower(String liquidPower) {
//		this.liquidPower = liquidPower;
//	}
//	
//	public long getUpTime() {
//		return upTime;
//	}
//	public void setUpTime(long upTime) {
//		this.upTime = upTime;
//	}
////	public Timestamp getLogTime() {
////		return logTime;
////	}
////	public void setLogTime(Timestamp logTime) {
////		this.logTime = logTime;
////	} 
//}
//=======
package com.casic.alarm.JSON;

import java.sql.Timestamp;
import java.util.Date;

public class LiquidRecordJSON {	 
	private String devId;
	private String  liquidData;	
	private String liquidPower;
	private long upTime;
	private long diameter;
	private String time;
	
	
//	private Timestamp logTime;
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getLiquidData() {
		return liquidData;
	}
	public void setLiquidData(String liquidData) {
		this.liquidData = liquidData;
	}
	public long getDiameter() {
		return diameter;
	}
	public void setDiameter(long diameter) {
		this.diameter = diameter;
	}
	public String getDevId() {
		return devId;
	}
	public void setDevId(String devId) {
		this.devId = devId;
	}
	
	public String getLiquidPower() {
		return liquidPower;
	}
	public void setLiquidPower(String liquidPower) {
		this.liquidPower = liquidPower;
	}
	
	public long getUpTime() {
		return upTime;
	}
	public void setUpTime(long upTime) {
		this.upTime = upTime;
	}


}
//>>>>>>> refs/remotes/origin/master
