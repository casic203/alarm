package com.casic.alarm.scheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.hibernate.criterion.Criterion;
import com.casic.alarm.JSON.AlarmRecordJSON;
import com.casic.alarm.domain.AlarmRecord;
import com.casic.alarm.domain.Device;
import com.casic.alarm.manager.AcceptPersonManager;
import com.casic.alarm.manager.AlarmRecordManager;
import com.casic.core.hibernate.HibernateUtils;
import com.casic.core.hibernate.MatchType;
import com.casic.core.mail.SimpleMailService;
import com.casic.core.mapper.JsonMapper;
import com.casic.core.util.StringUtils;

/**
 * 
 * 系统自动调度类
 * @author liuxin
 *
 */
public class SystemJob {
	
	/**
	 * 邮件服务
	 */
	@Resource
	private SimpleMailService simpleMailService;
	
	@Resource
	private AlarmRecordManager alarmRecordManager;
	
	@Resource
	private AcceptPersonManager acceptPersonManager;
	
//	@Resource
//	private ContactBookManager contactBookManager;
	
	private Map<String, MessageService> messageServiceMap;

	public SystemJob() {
	}
	
	/**
	 * 自动发送邮件
	 */
	public void autoSendMail() {
		/**
		 * 查询所有未发送信息给接警人的报警信息
		 */
		List<Criterion> criterions = new ArrayList<Criterion>();
		criterions.add(HibernateUtils.buildCriterion("messageStatus", 0, MatchType.EQ));
		criterions.add(HibernateUtils.buildCriterion("active", Boolean.TRUE, MatchType.EQ));
		Criterion[] criterionArray = new Criterion[criterions.size()];
		criterionArray = criterions.toArray(criterionArray);
		List<AlarmRecord> queryAlarmRecordList = alarmRecordManager.find(AlarmRecord.class, criterionArray);
		
//		String sql = "select alarm_type.alarmname, contact_book.contact from alarm_contact_book contact_book" +
//				" left join alarm_accept_person accept_person" +
//				" on contact_book.acceptperson_id=accept_person.id" +
//				" left join alarm_alarm_type alarm_type" +
//				" on contact_book.alarmtype_id = alarm_type.id" +
//				" where contact_book.active=1 and contact_book.acceptperson_id=?";
		String updateRecordHql = "update AlarmRecord alarmRecord set alarmRecord.messageStatus=:messageStatus" +
				" where alarmRecord.id=:alarmId";
		for (AlarmRecord alarmRecord : queryAlarmRecordList) {
			Long alarmId = alarmRecord.getId(); //报警id
			String message = alarmRecord.getMessage(); //报警信息
//			Date recordDate = alarmRecord.getRecordDate(); //报警日期
//			Long acceptPersonId = alarmRecord.getAcceptPerson().getId();//接警人id
//			String devCode = alarmRecord.getDevice().getDevCode();
			sendAlarmDataToClient(alarmRecord);
			
			/**
			 * 查询接警人所有通讯方式
			 */
			boolean isSms = false, isMail = false;
			int messageStatus = 0;
			
			Device alarmDevice = alarmRecord.getDevice();
			if(null != alarmDevice) {
			    String telePhone = alarmDevice.getAcceptPerson().getTelePhone();
			    String email = alarmDevice.getAcceptPerson().getEmail();
			    if(StringUtils.isNotBlank(telePhone)) {
				    messageServiceMap.get("sms").sendMsgToAcceptAlarmPerson(telePhone, "报警信息", message);
				    isSms = true;
			    } 
			    if (StringUtils.isNotBlank(email)) {
				    messageServiceMap.get("email").sendMsgToAcceptAlarmPerson(email, "报警信息", message);
				    isMail = true;
			    }
			}
			
			if(isSms && isMail) {
				messageStatus = 3;
			} else if (isMail) {
				messageStatus = 2;
			} else if (isSms) {
				messageStatus = 1;
			} else {
				messageStatus = 4;
			}
			
			/**
			 * 修改消息状态
			 */
			Map<String, Object> updateParamMap = new HashMap<String, Object>();
			updateParamMap.put("messageStatus", messageStatus);
			updateParamMap.put("alarmId", alarmId);
			alarmRecordManager.batchUpdate(updateRecordHql, updateParamMap);
		}
	}
	
	private void sendAlarmDataToClient(AlarmRecord newAlarmRecord) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
//		/**
//		 * 查询所有未处理的报警信息
//		 */
//		List<Criterion> criterions = new ArrayList<Criterion>();
//		criterions.add(HibernateUtils.buildCriterion("active", Boolean.TRUE, MatchType.EQ));
//		Criterion[] criterionArray = new Criterion[criterions.size()];
//		criterionArray = criterions.toArray(criterionArray);
//		List<AlarmRecord> queryAlarmRecordList = alarmRecordManager.find(AlarmRecord.class, criterionArray);
		
		resultMap.put("newAlarmMsg", (AlarmRecordJSON)toAlarmRecord(newAlarmRecord, newAlarmRecord.getId()));
//		resultMap.put("historyAlarmMsg", (List<AlarmRecordJSON>)toAlarmRecord(queryAlarmRecordList, newAlarmRecord.getId()));
		
		JsonMapper jsonMapper = new JsonMapper();
	}
	
	public static Object toAlarmRecord(Object alarmRecordObj, Long excludeAlarmRecordId) {
		if(alarmRecordObj instanceof AlarmRecord) {
			AlarmRecord alarmRecord = (AlarmRecord)alarmRecordObj;
			AlarmRecordJSON alarmRecordJSON = new AlarmRecordJSON();
			alarmRecordJSON.setMessage(alarmRecord.getMessage());
			alarmRecordJSON.setRecordDate(alarmRecord.getRecordDate());
			alarmRecordJSON.setItemName(alarmRecord.getItemName());
			alarmRecordJSON.setItemValue(alarmRecord.getItemValue());
			Device device = alarmRecord.getDevice();
			if(null != device) {
			    alarmRecordJSON.setDevice(device.getDevName());
			    alarmRecordJSON.setCode(device.getDevCode());
			} else {
			    alarmRecordJSON.setDevice("光纤");
			    alarmRecordJSON.setCode(alarmRecord.getDeviceCode());
			}
			return alarmRecordJSON;
		} else if(alarmRecordObj instanceof List) {
			List<AlarmRecord> alarmRecordList = (List<AlarmRecord>)alarmRecordObj;
			List<AlarmRecordJSON> alarmRecordJSONList = new ArrayList<AlarmRecordJSON>();
			for (AlarmRecord alarmRecord : alarmRecordList) {
				if(excludeAlarmRecordId == alarmRecord.getId()) {
					continue;
				}
				
				AlarmRecordJSON alarmRecordJSON = new AlarmRecordJSON();
				alarmRecordJSON.setMessage(alarmRecord.getMessage());
				alarmRecordJSON.setRecordDate(alarmRecord.getRecordDate());
				alarmRecordJSON.setItemName(alarmRecord.getItemName());
				alarmRecordJSON.setItemValue(alarmRecord.getItemValue());
				Device device = alarmRecord.getDevice();
				if(null != device) {
				    alarmRecordJSON.setDevice(alarmRecord.getDevice().getDevName());
				    alarmRecordJSON.setCode(alarmRecord.getDevice().getDevCode());
				} else {
					alarmRecordJSON.setDevice("光纤");
					alarmRecordJSON.setCode(alarmRecord.getDeviceCode());
				}
				
				alarmRecordJSONList.add(alarmRecordJSON);
			}
//			historyAlarmMsgMap.put("", value)
			return alarmRecordJSONList;
		}
		return null;
	}
	
	
	
	public SimpleMailService getSimpleMailService() {
		return simpleMailService;
	}

	public void setSimpleMailService(SimpleMailService simpleMailService) {
		this.simpleMailService = simpleMailService;
	}
	
	public AlarmRecordManager getAlarmRecordManager() {
		return alarmRecordManager;
	}

	public void setAlarmRecordManager(AlarmRecordManager alarmRecordManager) {
		this.alarmRecordManager = alarmRecordManager;
	}

	public AcceptPersonManager getAcceptPersonManager() {
		return acceptPersonManager;
	}

	public void setAcceptPersonManager(AcceptPersonManager acceptPersonManager) {
		this.acceptPersonManager = acceptPersonManager;
	}

//	public ContactBookManager getContactBookManager() {
//		return contactBookManager;
//	}
//
//	public void setContactBookManager(ContactBookManager contactBookManager) {
//		this.contactBookManager = contactBookManager;
//	}

	public Map<String, MessageService> getMessageServiceMap() {
		return messageServiceMap;
	}

	public void setMessageServiceMap(Map<String, MessageService> messageServiceMap) {
		this.messageServiceMap = messageServiceMap;
	}
	
	public interface MessageService {
		public void sendMsgToAcceptAlarmPerson(String contact, String msgTitle, String msgContent);
	}
	
}


