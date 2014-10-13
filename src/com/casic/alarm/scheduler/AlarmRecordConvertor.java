package com.casic.alarm.scheduler;

import java.util.ArrayList;
import java.util.List;

import com.casic.alarm.domain.AlarmRecord;

public class AlarmRecordConvertor 
{
/*
	public static AlarmRecordDTO ConvertToDTO(AlarmRecord alarmRecord)
	{
		AlarmRecordDTO alarmRecordDto=new AlarmRecordDTO();
		
		alarmRecordDto.setActive(alarmRecord.getActive());
		alarmRecordDto.setDeviceCode(alarmRecord.getDeviceCode());
		alarmRecordDto.setDeviceTypeName(alarmRecord.getDeviceTypeName());
		alarmRecordDto.setItemName(alarmRecord.getItemName());
		alarmRecordDto.setItemValue(alarmRecord.getItemValue());
		alarmRecordDto.setMessage(alarmRecord.getMessage());
		
		switch(alarmRecord.getMessageStatus())
		{
			case 0:
				alarmRecordDto.setMessageStatus("待处理");
				break;
			case 1:
				alarmRecordDto.setMessageStatus("仅发短信");
				break;
			case 2:
				alarmRecordDto.setMessageStatus("仅发邮件");
				break;
			case 3:
				alarmRecordDto.setMessageStatus("已发邮件与短信");
				break;
			default:
				alarmRecordDto.setMessage("未处理");
		};
		
		alarmRecordDto.setRecordCode(alarmRecord.getRecordCode());
		alarmRecordDto.setRecordDate(alarmRecord.getRecordDate());
		
		return alarmRecordDto;
	}
	
	public static List<AlarmRecordDTO> ConverToDTO(List<AlarmRecord> alarmRecordList)
	{
		 List dtoList = new ArrayList<AlarmRecordDTO>();
		 for(AlarmRecord item : alarmRecordList)
		 {
			 dtoList.add(ConvertToDTO(item));
		 }
		 return dtoList;
	}
	*/
}
