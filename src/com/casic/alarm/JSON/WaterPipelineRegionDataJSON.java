package com.casic.alarm.JSON;

import com.casic.alarm.domain.DMAInfo;

public class WaterPipelineRegionDataJSON {
	private Long ID; // ID
	private String Name; // 分区名称
	private String No; // 分区编码
	private String BDataParent_DMA = "-1";// 上级分区
	private Double UserCount = 0.0; // 用户数量
	private Double NormalWater = 0.0; // 正常夜间用水量
	private Double PipeLeng = 0.0; // 管道总长度
	private Double UserPipeLeng = 0.0; // 户表后管道总长度
	private Double PipeLinks = 0.0; // 管道连接总数
	private Double Icf = 0.0; // ICF参数
	private Double LeakControlRate = 0.0; // 阶段漏损控制目标
	private Double SaleWater = 0.0; // 日售水量
	
	public WaterPipelineRegionDataJSON(DMAInfo dmaInfo) {
		this.ID = dmaInfo.getID();
		this.Name = dmaInfo.getName();
		this.No = dmaInfo.getNo();
		this.BDataParent_DMA = dmaInfo.getBDataParent_DMA();
		this.UserCount = dmaInfo.getUserCount();
		this.NormalWater = dmaInfo.getNormalWater();
		this.PipeLeng = dmaInfo.getPipeLeng();
		this.UserPipeLeng = dmaInfo.getUserPipeLeng();
		this.PipeLinks = dmaInfo.getPipeLinks();
		this.Icf = dmaInfo.getIcf();
		this.LeakControlRate = dmaInfo.getLeakControlRate();
		this.SaleWater = dmaInfo.getSaleWater();
	}
	
	public Long getID() {
		return ID;
	}
	public void setID(Long iD) {
		ID = iD;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getNo() {
		return No;
	}
	public void setNo(String no) {
		No = no;
	}
	public String getBDataParent_DMA() {
		return BDataParent_DMA;
	}
	public void setBDataParent_DMA(String bDataParent_DMA) {
		BDataParent_DMA = bDataParent_DMA;
	}
	public Double getUserCount() {
		return UserCount;
	}
	public void setUserCount(Double userCount) {
		UserCount = userCount;
	}
	public Double getNormalWater() {
		return NormalWater;
	}
	public void setNormalWater(Double normalWater) {
		NormalWater = normalWater;
	}
	public Double getPipeLeng() {
		return PipeLeng;
	}
	public void setPipeLeng(Double pipeLeng) {
		PipeLeng = pipeLeng;
	}
	public Double getUserPipeLeng() {
		return UserPipeLeng;
	}
	public void setUserPipeLeng(Double userPipeLeng) {
		UserPipeLeng = userPipeLeng;
	}
	public Double getPipeLinks() {
		return PipeLinks;
	}
	public void setPipeLinks(Double pipeLinks) {
		PipeLinks = pipeLinks;
	}
	public Double getIcf() {
		return Icf;
	}
	public void setIcf(Double icf) {
		Icf = icf;
	}
	public Double getLeakControlRate() {
		return LeakControlRate;
	}
	public void setLeakControlRate(Double leakControlRate) {
		LeakControlRate = leakControlRate;
	}
	public Double getSaleWater() {
		return SaleWater;
	}
	public void setSaleWater(Double saleWater) {
		SaleWater = saleWater;
	}
}
