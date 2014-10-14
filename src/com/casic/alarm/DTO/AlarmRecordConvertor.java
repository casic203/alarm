package com.casic.alarm.DTO;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.casic.alarm.domain.AlarmRecord;

public class AlarmRecordConvertor
{
	public static AlarmRecordDTO ConvertToDto(AlarmRecord record)
	{
		AlarmRecordDTO result = new AlarmRecordDTO();
		result.setDealPerson(record.getDevice().getAcceptPerson().getPersonName());
		result.setDeviceId(record.getDevice().getId().toString());
		result.setDeviceType(record.getDevice().getDeviceType().getTypeName());
		result.setMessage(record.getMessage());
		int status = record.getMessageStatus();
		result.setDeviceCode(record.getDeviceCode());
		result.setAlarmValue(record.getItemValue());
		String messageStatus;
		switch(record.getMessageStatus())
		{
			case 1:
				messageStatus="已短信报警";
			case 2:
				messageStatus="已邮件报警";
			case 3:
				messageStatus="短信邮件已报警";
			default:
				messageStatus="未处理";
		}
		result.setMessageStatus(messageStatus);
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		result.setRecordDate(df.format(record.getRecordDate()));
		
		return result;
		
	}

	public static List<AlarmRecordDTO> ConvertToDto(List<AlarmRecord> records)
	{
		List<AlarmRecordDTO> results=new ArrayList<AlarmRecordDTO>();
		for(AlarmRecord record : records)
		{
			results.add(ConvertToDto(record));
		}
		return results;
	}
 }
