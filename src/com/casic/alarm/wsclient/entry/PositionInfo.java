package com.casic.alarm.wsclient.entry;

import java.io.Serializable;
import java.util.Date;

public class PositionInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5403665731702843740L;

	public PositionInfo() {

	}

	public String ID; // ID
	public String Name; // 监测点名称
	public String Longitude; // 经度
	public String Latitude; // 纬度
	public String BDataPosType; // 监测点类型
	public String SortCode; // 排序码
	public String Comment; // 备注
	public Boolean IsUse = false; // 是否启用
	public String Operator; // 操作人
	public Date OperateTime; // 操作时间

}
