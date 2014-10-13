package com.casic.alarm.wsclient.entry;

import java.io.Serializable;
import java.util.Date;

public class disLosses implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2793797187330390986L;
	public String BData_DMA;// 分区编号
	public Date ReportDate;//评估日期
	public Double SupplyWater;//日供水量
	public Double SaleWater;// 售水量
	public Double SaleDiffWater;// 产销差量量
	public Double SaleDiffWaterRate;//产销差率
}
