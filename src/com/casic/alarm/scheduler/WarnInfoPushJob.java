package com.casic.alarm.scheduler;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.criterion.Criterion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.casic.alarm.domain.AlarmRecord;
import com.casic.alarm.manager.AlarmRecordManager;
import com.casic.core.hibernate.HibernateUtils;
import com.casic.core.hibernate.MatchType;
import com.casic.core.mapper.JsonMapper;

public class WarnInfoPushJob implements ISystemJob
{

	private static Logger logger = LoggerFactory.getLogger(WarnInfoPushJob.class);

	@Resource
	private WarnInfoPusherProxy warnInfoPusherProxy;
	
	@Resource
	private AlarmRecordManager alarmRecordManager;
	
	public AlarmRecordManager getAlarmRecordManager() 
	{
		return alarmRecordManager;
	}

	public void setAlarmRecordManager(AlarmRecordManager alarmRecordManager) 
	{
		this.alarmRecordManager = alarmRecordManager;
	}

	public WarnInfoPusherProxy getWarnInfoPusherProxy() 
	{
		return warnInfoPusherProxy;
	}

	public void setWarnInfoPusherProxy(WarnInfoPusherProxy warnInfoPusherProxy) 
	{
		this.warnInfoPusherProxy = warnInfoPusherProxy;
	}
	
	public void auotExexcute() {
		
		/*
	    List<Criterion> criterions = new ArrayList<Criterion>();
		criterions.add(HibernateUtils.buildCriterion("isPushed", Boolean.FALSE, MatchType.EQ));
		Criterion[] criterionArray = new Criterion[criterions.size()];
		criterionArray = criterions.toArray(criterionArray);
		List<AlarmRecord> queryAlarmRecordList = alarmRecordManager.find(AlarmRecord.class, criterionArray);
		List<AlarmRecordDTO> results = AlarmRecordConvertor.ConverToDTO(queryAlarmRecordList);
		*/
	//	Date now = new Date();
		
		try {
			List<AlarmRecord> alarms=alarmRecordManager.findBy("isSend", false);
			for(AlarmRecord alarm : alarms){
				//String msg = "{\"data\":{\"id\":"@id",\"active\":\"2\",\"itemname\":\"3\",\"tiemvalue\":\"4\",\"message\":\"5\",\"messageStatus\":\"6\",\"recordcode\":\"7\",\"recorddate\":\"2012-07-26 02:00:00\",\"deviceId\":\"8\"}}";
				String sid=alarm.getId().toString();
				String sActive=(alarm.getActive()==true?"1":"0");
				String sitename=alarm.getItemName();
				String sTimeValue=alarm.getItemValue();
				String smessage=alarm.getMessage();
				String smessageStatus=Integer.toString(alarm.getMessageStatus());
				String srecodecode=alarm.getRecordCode();
				Date date=alarm.getRecordDate();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String sdateString=df.format(new Date());
				String sDevId = "";
				if(alarm.getDevice()!=null){
				 sDevId=alarm.getDevice().getId().toString();
				}
				
				String msg = "{\"data\":{\"id\":\"i1\",\"active\":\"i2\",\"itemname\":\"i3\",\"tiemvalue\":\"i4\",\"message\":\"i5\",\"messageStatus\":\"i6\",\"recordcode\":\"i7\",\"recorddate\":\"i8\",\"deviceId\":\"i9\"}}";
				msg=msg.replaceAll("i1", sid).replaceFirst("i2", sActive)
						.replaceAll("i3", sitename).replaceAll("i4",sTimeValue)
						.replace("i5", smessage).replaceAll("i6", smessageStatus)
						.replaceAll("i7", srecodecode).replaceAll("i8", sdateString)
						.replaceAll("i9", sDevId);
				warnInfoPusherProxy.sendMsg(msg);
				alarm.setIsSend(true);
				alarmRecordManager.save(alarm);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		List<AlarmRecordDTO> alarmRecordDtoList =  new ArrayList();
//		AlarmRecordDTO alarmRecordDto0 = new AlarmRecordDTO();
//	
//		alarmRecordDto0.setActive((short)1);
//		alarmRecordDto0.setDeviceId(new BigDecimal(2));
//		alarmRecordDto0.setId(new BigDecimal(3));
//		alarmRecordDto0.setItemname("燃气");
//		alarmRecordDto0.setTiemvalue("100");
//		alarmRecordDto0.setMessage("燃气浓度超限");
//		alarmRecordDto0.setMessageStatus((long)1);
//		alarmRecordDto0.setRecordcode("001122");
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		alarmRecordDto0.setRecorddate(df.format(new Date()));
//		
//		
//		alarmRecordDtoList.add(alarmRecordDto0);
//		//alarmRecordDtoList.add(alarmRecordDto1);
//		
//		JsonMapper mapper = new JsonMapper();
//		String temp=mapper.toJson(alarmRecordDto0);
//		String result="{data:"+temp+"}";
//		
//		//String result  = mapper.toJson(alarmRecordDtoList); 
//		 //String result=mapper.toJson(alarmRecordDto0);
//		String msg = "{\"data\":{\"id\":2,\"active\":\"2\",\"itemname\":\"3\",\"tiemvalue\":\"4\",\"message\":\"5\",\"messageStatus\":\"6\",\"recordcode\":\"7\",\"recorddate\":\"2012-07-26 02:00:00\",\"deviceId\":\"8\"}}";
//		
//		//System.out.print(result);
//	   warnInfoPusherProxy.sendMsg(msg);
	}
	

}
