package com.casic.alarm.DTO;


import java.io.Serializable;

public class AlarmRecordDTO 
{
	public String getDeviceId()
	{
		return deviceId;
	}
	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}
	public String getDeviceType()
	{
		return deviceType;
	}
	public void setDeviceType(String deviceType)
	{
		this.deviceType = deviceType;
	}
	public String getMessage()
	{
		return message;
	}
	public void setMessage(String message)
	{
		this.message = message;
	}
	public String getDealPerson()
	{
		return dealPerson;
	}
	public void setDealPerson(String dealPerson)
	{
		this.dealPerson = dealPerson;
	}
	public String getMessageStatus()
	{
		return messageStatus;
	}
	public void setMessageStatus(String messageStatus)
	{
		this.messageStatus = messageStatus;
	}
	public String getRecordDate()
	{
		return recordDate;
	}
	public void setRecordDate(String recordDate)
	{
		this.recordDate = recordDate;
	}
	private String deviceId;
	private String deviceType;
	private String message;
	private String dealPerson;
	private String messageStatus;
	private String recordDate;
	private String deviceCode;
	private String alarmValue;
	
	public String getAlarmValue()
	{
		return alarmValue;
	}
	public void setAlarmValue(String alarmValue)
	{
		this.alarmValue = alarmValue;
	}
	public String getDeviceCode()
	{
		return deviceCode;
	}
	public void setDeviceCode(String deviceCode)
	{
		this.deviceCode = deviceCode;
	}
	
	
	
	

}
