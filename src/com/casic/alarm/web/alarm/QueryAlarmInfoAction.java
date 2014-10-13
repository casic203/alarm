package com.casic.alarm.web.alarm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.hibernate.criterion.Criterion;
import com.casic.alarm.JSON.AlarmRecordJSON;
import com.casic.alarm.domain.AlarmRecord;
import com.casic.alarm.manager.AlarmRecordManager;
import com.casic.alarm.scheduler.SystemJob;
import com.casic.core.hibernate.HibernateUtils;
import com.casic.core.hibernate.MatchType;
import com.casic.core.json.JSONTool;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 前台轮询报警记录
 * @author liuxin
 *
 */
public class QueryAlarmInfoAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4585083841335183124L;
	@Resource
	private AlarmRecordManager alarmRecordManager;

	/**
	 * 查询报警记录
	 */
	public void queryAlarmRecord() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		/**
		 * 查询所有未发送信息给接警人的报警信息
		 */
		List<Criterion> criterions = new ArrayList<Criterion>();
		criterions.add(HibernateUtils.buildCriterion("messageStatus", 0, MatchType.EQ));
		criterions.add(HibernateUtils.buildCriterion("active", Boolean.TRUE, MatchType.EQ));
		Criterion[] criterionArray = new Criterion[criterions.size()];
		criterionArray = criterions.toArray(criterionArray);
		List<AlarmRecord> queryAlarmRecordList = alarmRecordManager.find(AlarmRecord.class, criterionArray);
		
		for (AlarmRecord newAlarmRecord : queryAlarmRecordList) {
			
			
			/**
			 * 查询所有未处理的报警信息
			 */
			criterions = new ArrayList<Criterion>();
			criterions.add(HibernateUtils.buildCriterion("active", Boolean.TRUE, MatchType.EQ));
			criterionArray = new Criterion[criterions.size()];
			criterionArray = criterions.toArray(criterionArray);
			List<AlarmRecord> queryUnDealAlarmRecordList = alarmRecordManager.find(AlarmRecord.class, criterionArray);
			
			/**
			 * 封装新的报警信息和历史报警信息
			 */
			resultMap.put("newAlarmMsg", (AlarmRecordJSON)SystemJob.toAlarmRecord(newAlarmRecord, newAlarmRecord.getId()));
			resultMap.put("historyAlarmMsg", (List<AlarmRecordJSON>)SystemJob.toAlarmRecord(queryUnDealAlarmRecordList, newAlarmRecord.getId()));
			
			try {
				JSONTool.writeDataResult(resultMap);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		
		
	}
}
