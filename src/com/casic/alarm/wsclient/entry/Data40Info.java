package com.casic.alarm.wsclient.entry;

import java.io.Serializable;
import java.util.Date;

public class Data40Info implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1042190767802551626L;
	public String ID; // ID
	public String BData_Position; // 监测点
	public String PositionDataNo; // 监测内容编号
	public String EqtNo; // 设备编号
	public String EqtSensorNo; // 设备传感器编号
	public Date RecordTime; // 记录时间
	public Date CollectTime; // 采集时间
	public Double InstantValue = 0.0; // 压力
	public String DataFlag = "0"; // 数据标识

}
