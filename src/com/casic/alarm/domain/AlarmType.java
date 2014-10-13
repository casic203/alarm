package com.casic.alarm.domain;

import java.io.Serializable;
import java.util.Set;

public class AlarmType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3966425400821437378L;
	private Long id;
	private String alarmCode;
	private String alarmName;
	private Set<AlarmRecord> alarmRecords;
	private Boolean active = true;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAlarmCode() {
		return alarmCode;
	}

	public void setAlarmCode(String alarmCode) {
		this.alarmCode = alarmCode;
	}

	public String getAlarmName() {
		return alarmName;
	}

	public void setAlarmName(String alarmName) {
		this.alarmName = alarmName;
	}

//	@OneToMany(fetch = FetchType.LAZY, mappedBy = "alarmType")
//	public Set<ContactBook> getContactBooks() {
//		return contactBooks;
//	}
//
//	public void setContactBooks(Set<ContactBook> contactBooks) {
//		this.contactBooks = contactBooks;
//	}

	public Set<AlarmRecord> getAlarmRecords() {
		return alarmRecords;
	}

	public void setAlarmRecords(Set<AlarmRecord> alarmRecords) {
		this.alarmRecords = alarmRecords;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

}
