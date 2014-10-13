package com.casic.alarm.JSON;

import java.sql.Timestamp;
import java.util.Date;

public class LiquidPipeRecordJSON {
  private String dbCord;
  
  private double liquid;
  
  private long diameter;
  
  private long times;
  
  private String type; 
  
  private String time;

public String getTime() {
	return time;
}

public void setTime(String time) {
	this.time = time;
}

public String getType() {
	return type;
}

public void setType(String type) {
	this.type = type;
}

public long getTimes() {
	return times;
}

public void setTimes(long times) {
	this.times = times;
}

public long getDiameter() {
	return diameter;
}

public void setDiameter(long diameter) {
	this.diameter = diameter;
}

public double getLiquid() {
	return liquid;
}

public void setLiquid(double liquid) {
	this.liquid = liquid;
}

public String getDbCord() {
	return dbCord;
}

public void setDbCord(String dbCord) {
	this.dbCord = dbCord;
}

}
