package com.casic.alarm.wsclient.entry;

import java.io.Serializable;
import java.util.Date;

public class DMAInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9072886829080460101L;

	public DMAInfo() {
	}
	public String ID; // ID
	public String Name; // 分区名称
	public String No; // 分区编码
	public String BDataParent_DMA = "-1";// 上级分区
	public Double UserCount = 0.0; // 用户数量
	public Double NormalWater = 0.0; // 正常夜间用水量
	public Double PipeLeng = 0.0; // 管道总长度
	public Double UserPipeLeng = 0.0; // 户表后管道总长度
	public Double PipeLinks = 0.0; // 管道连接总数
	public Double Icf = 0.0; // ICF参数
	public Double AllowedMinWater = 0.0; // 夜间允许最小流量
	public Double LeakControlRate = 0.0; // 阶段漏损控制目标
	public Double SeriveUsers = 0.0; // 服务区人口
	public Double SeriveArea = 0.0; // 服务区面积
	public Double HighSeaLevel = 0.0; // 区域最高高程
	public Double LowSeaLevel = 0.0; // 区域最低高程
	public Double SaleWater = 0.0; // 日售水量
	public Double NoValueWater = 0.0; // 无计量水量
	public String PipeMaterial; // 管材分类
	public Double PipeUseYears = 0.0; // 管道最长服务年限
	public String GisCenter; // 地图中心
	public String SortCode; // 排序码
	public Boolean IsUse = false; // 是否启用
	public String Comment; // 备注
	public String Operator; // 操作人
	public Date OperateTime; // 操作时间
}
