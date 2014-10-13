package com.casic.alarm.wsclient.entry;

import java.io.Serializable;
import java.util.Date;

public class SecuInfoInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5641875807360401231L;
	public String ID; // ID
	public String DataID; // 数据ID
	public String BDataSecuType; // 报警类型
	public String BData_SecuDef; // 报警定义
	public String BData_Position; // 监测点
	public String SecuInformation; // 报警内容
	public Date SecuTime; // 报警时间
	public String BDataStatus; // 报警处理状态
	public String Comment; // 备注
	public String Operator; // 操作人
	public String OperateTime; // 操作时间

}
