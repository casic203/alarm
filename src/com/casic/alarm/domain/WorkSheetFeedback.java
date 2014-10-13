package com.casic.alarm.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "ALARM_WORK_SHEET_FEEDBACK")
@SequenceGenerator(name = "SEQ_WORK_SHEET_FEEDBACK", sequenceName = "SEQ_WORK_SHEET_FEEDBACK")
public class WorkSheetFeedback implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 902428464209078734L;

	private Long id;              //ID
	private String alarmReason;   //故障原因
	private String solution;      //解决方法
	private Boolean isLeakage;    //是否漏损
	
	private String leakageReason; //漏损原因
	private Double lossValues;    //漏损量
	private Date feedbackDate;    //反馈时间
	private DMAInfo dmaInfo;      //关联分区
	private WorkSheet workSheet;  //对应的工单
	
	private Date insertDate; 
	private Date updateDate;
	private Boolean deleteFlag;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WORK_SHEET_FEEDBACK")
	@Column(name = "ID")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "ALARMREASON")
	public String getAlarmReason() {
		return alarmReason;
	}
	public void setAlarmReason(String alarmReason) {
		this.alarmReason = alarmReason;
	}
	
	@Column(name = "SOLUTION")
	public String getSolution() {
		return solution;
	}
	public void setSolution(String solution) {
		this.solution = solution;
	}
	
	@Column(name = "ISLEAKAGE")
	public Boolean getIsLeakage() {
		return isLeakage;
	}
	public void setIsLeakage(Boolean isLeakage) {
		this.isLeakage = isLeakage;
	}
	
	@Column(name = "LEAKAGEREASON")
	public String getLeakageReason() {
		return leakageReason;
	}
	public void setLeakageReason(String leakageReason) {
		this.leakageReason = leakageReason;
	}
	
	@Column(name = "LOSSVALUES")
	public Double getLossValues() {
		return lossValues;
	}
	public void setLossValues(Double lossValues) {
		this.lossValues = lossValues;
	}
	
	@Column(name="FEEDBACKDATE")
	public Date getFeedbackDate() {
		return feedbackDate;
	}
	public void setFeedbackDate(Date feedbackDate) {
		this.feedbackDate = feedbackDate;
	}
	
	@ManyToOne(fetch=FetchType.LAZY, targetEntity=DMAInfo.class)
	public DMAInfo getDmaInfo() {
		return dmaInfo;
	}
	public void setDmaInfo(DMAInfo dmaInfo) {
		this.dmaInfo = dmaInfo;
	}
	
	@ManyToOne(fetch=FetchType.LAZY, targetEntity=WorkSheet.class)
	public WorkSheet getWorkSheet() {
		return workSheet;
	}
	public void setWorkSheet(WorkSheet workSheet) {
		this.workSheet = workSheet;
	}
	
	@Column(name="INSERTDATE")
	public Date getInsertDate() {
		return insertDate;
	}
	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}
	
	@Column(name="UPDATEDATE")
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
	@Column(name="DELETEFLAG")
	public Boolean getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(Boolean deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
}
