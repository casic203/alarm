//package com.casic.alarm.domain;
//
//import java.io.Serializable;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.SequenceGenerator;
//import javax.persistence.Table;
//
//@Entity
//@Table(name = "ALARM_CONTACT_BOOK")
//@SequenceGenerator(name = "SEQ_ALARM_CONTACT_BOOK_ID", sequenceName = "SEQ_ALARM_CONTACT_BOOK_ID")
//public class ContactBook implements Serializable {
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = -791527359352238718L;
//	private Long id;
//	private String contact;
//	private AcceptPerson acceptPerson;
//	private AlarmType alarmType;
//	private Boolean active = true;
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ALARM_CONTACT_BOOK_ID")
//	@Column(name = "ID")
//	public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}
//
//	@Column(name = "CONTACT", nullable = false)
//	public String getContact() {
//		return contact;
//	}
//
//	public void setContact(String contact) {
//		this.contact = contact;
//	}
//
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "ACCEPTPERSON_ID", nullable = false)
//	public AcceptPerson getAcceptPerson() {
//		return acceptPerson;
//	}
//
//	public void setAcceptPerson(AcceptPerson acceptPerson) {
//		this.acceptPerson = acceptPerson;
//	}
//
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "ALARMTYPE_ID", nullable = false)
//	public AlarmType getAlarmType() {
//		return alarmType;
//	}
//
//	public void setAlarmType(AlarmType alarmType) {
//		this.alarmType = alarmType;
//	}
//
//	@Column(name = "ACTIVE", nullable = false)
//	public Boolean getActive() {
//		return active;
//	}
//
//	public void setActive(Boolean active) {
//		this.active = active;
//	}
//
//}
