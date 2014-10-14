package com.casic.alarm.web.alarm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.hibernate.criterion.Criterion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.casic.alarm.DTO.AlarmRecordConvertor;
import com.casic.alarm.DTO.AlarmRecordDTO;
import com.casic.alarm.JSON.AlarmRecordJSON;
import com.casic.alarm.domain.AlarmRecord;
import com.casic.alarm.manager.AlarmRecordManager;
import com.casic.alarm.scheduler.SystemJob;
import com.casic.core.hibernate.HibernateUtils;
import com.casic.core.hibernate.MatchType;
import com.casic.core.json.JSONTool;
import com.casic.core.mapper.JsonMapper;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 前台轮询报警记录
 * @author liuxin
 *
 */
public class QueryAlarmInfoAction extends ActionSupport {

	private static Logger logger = LoggerFactory.getLogger(QueryAlarmInfoAction.class);
	/**
	 * 查询报警记录
	 */
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

	public void queryAlarmRecord() throws IOException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		/**
		 * 查询所有未发送信息给接警人的报警信息
		 */
		logger.info("打印输出进入action");
		String hql="from AlarmRecord as a where a.active=true order by a.id desc";
	
		List<AlarmRecord> records = (List<AlarmRecord>) this.alarmRecordManager.
				getSession().
				createQuery(hql).
				setMaxResults(10).list();
		
		List<AlarmRecordDTO> recorddto=AlarmRecordConvertor.ConvertToDto(records);
		JsonMapper mapper = new JsonMapper();
		
		//resultMap.put("latestAlarms",mapper.toJson(recorddto));
		logger.info("json数据显示"+mapper.toJson(recorddto));
		
		JSONTool.writeDataResult(recorddto);
		
	}
}
