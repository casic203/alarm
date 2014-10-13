package com.casic.alarm.wsclient.entry;

import java.io.Serializable;
import java.util.Date;

public class EquipmentInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1487910598563689348L;
	public String ID;// ID
	public String No; // 设备编码
	public String BData_Factory; // 厂商
	public String BDataEqtType; // 设备类型
	public String EqtModel; // 设备型号
	public String BData_Position; // 安装位置
	public String BDataEqtStatus; // 运行状态
	public Double EqtYears = 0.0; // 设备使用年限
	public Double PowerYears = 0.0; // 电池使用年限
	public String ValPrecision; // 测量精度
	public String ValScale; // 测量范围
	public String Simid; // SIM卡号
	public Date BeginUseTime; // 开始使用时间
	public Date LastAlarmTime; // 最后报警时间
	public Date LastRecvTime; // 最后数据采集时间
	public Double Power = 0.0; // 电量
	public Double Signal = 0.0; // 信号强度
	public String SortCode; // 排序码
	public String Comment; // 备注
	public Boolean IsUse = false; // 是否启用
	public String Operator; // 操作人
	public Date OperateTime; // 操作时间

}
