package com.casic.alarm.wsclient.entry;

import java.io.Serializable;
import java.util.Date;

public class leakageState implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6268777899909216907L;
	public String BData_DMA;// 分区编号
	public Date ReportDate; //评估日期
	public Double LeakRate;//漏损率
	public Double LeakControlRate;//漏损控制目标
	public Double SaleDiffWaterRate;//产销差量量
	public Boolean LeakState;//是否漏损状态

}
