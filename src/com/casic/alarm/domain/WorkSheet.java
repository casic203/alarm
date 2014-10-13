package com.casic.alarm.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author Administrator
 * 
 */
@Entity
@Table(name = "ALARM_WORK_SHEET")
@SequenceGenerator(name = "SEQ_WORK_SHEET", sequenceName = "SEQ_WORK_SHEET")
public class WorkSheet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3796998948512757250L;
	private Long id;
	private String sheetNo;
	private Device device;
	private AlarmRecord alarmRecord;
	private String charger;
	private String checker;
	private String writer;
	private String task;
	private Date beginDate;
	private Date endDate;
	private Date logDate;
	private Date accDate;
	private String sheetStatus;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WORK_SHEET")
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "SHEETNO")
	public String getSheetNo() {
		return sheetNo;
	}

	public void setSheetNo(String sheetNo) {
		this.sheetNo = sheetNo;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEVICE_ID", nullable = false)
	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ALARM_RECORD_ID", nullable = false)
	public AlarmRecord getAlarmRecord() {
		return alarmRecord;
	}

	public void setAlarmRecord(AlarmRecord alarmRecord) {
		this.alarmRecord = alarmRecord;
	}

	@Column(name="CHARGER")
	public String getCharger() {
		return charger;
	}

	public void setCharger(String charger) {
		this.charger = charger;
	}

	@Column(name="CHECKER")
	public String getChecker() {
		return checker;
	}

	public void setChecker(String checker) {
		this.checker = checker;
	}

	@Column(name="WRITER")
	public String getWriter() {
		return writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

	@Column(name = "TASK")
	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	@Column(name = "BEGINDATE")
	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	@Column(name = "ENDDATE")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name = "LOGDATE")
	public Date getLogDate() {
		return logDate;
	}

	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}

	@Column(name = "ACCDATE")
	public Date getAccDate() {
		return accDate;
	}

	public void setAccDate(Date accDate) {
		this.accDate = accDate;
	}

	@Column(name = "SHEETSTATUS")
	public String getSheetStatus() {
		return sheetStatus;
	}

	public void setSheetStatus(String sheetStatus) {
		this.sheetStatus = sheetStatus;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
