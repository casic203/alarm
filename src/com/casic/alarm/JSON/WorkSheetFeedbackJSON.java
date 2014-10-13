package com.casic.alarm.JSON;

import java.util.Date;

import com.casic.alarm.domain.WorkSheetFeedback;

public class WorkSheetFeedbackJSON {
	private Long id;              //ID
	private String alarmReason;   //故障原因
	private String solution;      //解决方法
	private Boolean isLeakage;    //是否漏损
	
	private String leakageReason; //漏损原因
	private Double lossValues;    //漏损量
	private Date feedbackDate;    //反馈时间
	private Long workSheetId;  //对应的工单
	private Long dmaId;        //DMA
	private String dmaName;
	
	public WorkSheetFeedbackJSON() {
		
	}
	
	public WorkSheetFeedbackJSON(WorkSheetFeedback workSheetFeedback) {
		this.id = workSheetFeedback.getId();
		this.alarmReason = workSheetFeedback.getAlarmReason();
		this.solution = workSheetFeedback.getSolution();
		this.isLeakage = workSheetFeedback.getIsLeakage();
		this.leakageReason = workSheetFeedback.getLeakageReason();
		this.lossValues = workSheetFeedback.getLossValues();
		this.feedbackDate = workSheetFeedback.getFeedbackDate();
		this.workSheetId = workSheetFeedback.getWorkSheet().getId();
		if(null != workSheetFeedback.getDmaInfo()) {
		    this.dmaId = workSheetFeedback.getDmaInfo().getID();
		    this.dmaName = workSheetFeedback.getDmaInfo().getName();
		}
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAlarmReason() {
		return alarmReason;
	}
	public void setAlarmReason(String alarmReason) {
		this.alarmReason = alarmReason;
	}
	public String getSolution() {
		return solution;
	}
	public void setSolution(String solution) {
		this.solution = solution;
	}
	public Boolean getIsLeakage() {
		return isLeakage;
	}
	public void setIsLeakage(Boolean isLeakage) {
		this.isLeakage = isLeakage;
	}
	public String getLeakageReason() {
		return leakageReason;
	}
	public void setLeakageReason(String leakageReason) {
		this.leakageReason = leakageReason;
	}
	public Double getLossValues() {
		return lossValues;
	}
	public void setLossValues(Double lossValues) {
		this.lossValues = lossValues;
	}
	public Date getFeedbackDate() {
		return feedbackDate;
	}
	public void setFeedbackDate(Date feedbackDate) {
		this.feedbackDate = feedbackDate;
	}
	public Long getWorkSheetId() {
		return workSheetId;
	}
	public void setWorkSheetId(Long workSheetId) {
		this.workSheetId = workSheetId;
	}
	public Long getDmaId() {
		return dmaId;
	}
	public void setDmaId(Long dmaId) {
		this.dmaId = dmaId;
	}
	public String getDmaName() {
		return dmaName;
	}
	public void setDmaName(String dmaName) {
		this.dmaName = dmaName;
	}
}
