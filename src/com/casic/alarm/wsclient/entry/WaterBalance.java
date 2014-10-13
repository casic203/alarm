package com.casic.alarm.wsclient.entry;

import java.io.Serializable;
import java.util.Date;

public class WaterBalance implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2082826116762253521L;
	public String DmaID;         // 分区编号
	public Date AnalysisDate;    // 分析日期
	public Double WaterSupply;   // 供水量
	public Double WaterSale;     // 售水量
	public Double NoValueWater;  //无收益水量
	public Double LR_Leakage;   // 漏失率
	public Double LR_WaterME;    // 表误
	public Double LR_MeterE;     // 计量错误
	public Double LR_Favor;      // 人情水
	public Double LR_Steal;      // 偷水
	public Double LR_Pressure;   // 压力
	public Double SaleDiffWater; //产销差

}
