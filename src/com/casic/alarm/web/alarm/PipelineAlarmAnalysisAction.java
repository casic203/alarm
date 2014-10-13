package com.casic.alarm.web.alarm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.casic.alarm.JSON.AlarmRecordJSON;
import com.casic.alarm.domain.AlarmRecord;
import com.casic.alarm.manager.AlarmRecordManager;
import com.casic.core.json.JSONTool;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 管线开挖报警分析分析
 * @author liuxin
 *
 */
public class PipelineAlarmAnalysisAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7322102236046496254L;

	private String deviceCode;
	
	@Resource
	private AlarmRecordManager alarmRecordManager;

	public void excavationAnalysis() {
		String hql = "from AlarmRecord alarmRecord where alarmRecord.deviceTypeName=:deviceTypeName and alarmRecord.active=:active";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("deviceTypeName", "光纤");
		paramMap.put("active", Boolean.TRUE);
		List<AlarmRecord> alarmRecordList = alarmRecordManager.find(hql, paramMap);
		List<AlarmRecordJSON> alarmRecordJSONs = toJSON(alarmRecordList);
		
		try {
			JSONTool.writeDataResult(alarmRecordJSONs);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private List<AlarmRecordJSON> toJSON(List<AlarmRecord> alarmRecordList) {
		List<AlarmRecordJSON> alarmRecordJSONList = new ArrayList<AlarmRecordJSON>();
		for (AlarmRecord alarmRecord : alarmRecordList) {
			AlarmRecordJSON recordJSON = new AlarmRecordJSON();
			recordJSON.setCode(alarmRecord.getDeviceCode());
			alarmRecordJSONList.add(recordJSON);
		}
		return alarmRecordJSONList;
	}
	
	public AlarmRecordManager getAlarmRecordManager() {
		return alarmRecordManager;
	}

	public void setAlarmRecordManager(AlarmRecordManager alarmRecordManager) {
		this.alarmRecordManager = alarmRecordManager;
	}

	public String getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}
}
