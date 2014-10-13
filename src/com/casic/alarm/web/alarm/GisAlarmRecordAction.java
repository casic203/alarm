package com.casic.alarm.web.alarm;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import com.casic.alarm.domain.AlarmRecord;
import com.casic.alarm.domain.GisAlarmRecord;
import com.casic.alarm.manager.AlarmRecordManager;
import com.casic.core.json.JSONTool;
import com.casic.core.struts2.BaseAction;
import com.casic.core.util.StringUtils;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class GisAlarmRecordAction extends BaseAction implements
		ModelDriven<GisAlarmRecord>, Preparable {

	private GisAlarmRecord model;
	private String devCodes;
	private String devType;
	private String startTime;
	private String endTime;
	private AlarmRecordManager alarmRecordManager;

	public GisAlarmRecord getModel() {
		return model;
	}

	public void setModel(GisAlarmRecord model) {
		this.model = model;
	}

	public String getDevCodes() {
		return devCodes;
	}

	public void setDevCodes(String devCodes) {
		this.devCodes = devCodes;
	}

	public String getDevType() {
		return devType;
	}

	public void setDevType(String devType) {
		this.devType = devType;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public AlarmRecordManager getAlarmRecordManager() {
		return alarmRecordManager;
	}

	@Resource
	public void setAlarmRecordManager(AlarmRecordManager alarmRecordManager) {
		this.alarmRecordManager = alarmRecordManager;
	}

	public void prepare() throws Exception {
		this.model = new GisAlarmRecord();
	}

	public String execute() {
		return "success";
	}

	// 统计在一段时间内所有设备发生故障的次数
	@SuppressWarnings("unchecked")
	public void queryAlarmRecord4AllActive() throws IOException {
		try {
			Integer count = 0;
			StringBuilder hql = new StringBuilder();
			Map<String, Object> map = new HashMap<String, Object>();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

			List<String> nameList = new ArrayList<String>();
			List<GisAlarmRecord> gisAlarmRecords = new ArrayList<GisAlarmRecord>();

			hql.delete(0, hql.length());
			map.clear();
			hql.append("select distinct typeName from DeviceType");
			nameList = alarmRecordManager.find(hql.toString(), map);

			hql.delete(0, hql.length());
			hql.append("select count(*) from AlarmRecord where active=true and device.deviceType.typeName=:nam");
			if (StringUtils.isNotBlank(startTime)) {
				hql.append(" and recordDate>=:start");
			}
			if (StringUtils.isNotBlank(endTime)) {
				hql.append("and recordDate<=:end");
			}
			for (String nam : nameList) {
				map.clear();
				map.put("nam", nam);
				if (StringUtils.isNotBlank(startTime)) {
					map.put("start", dateFormat.parse(startTime));
				}
				if (StringUtils.isNotBlank(endTime)) {
					map.put("end", dateFormat.parse(endTime));
				}
				count = alarmRecordManager.getCount(hql.toString(), map);

				if (0 < count) {
					GisAlarmRecord gisAlarmRecord = new GisAlarmRecord();
					gisAlarmRecord.setDev(nam);
					gisAlarmRecord.setCount(count);
					gisAlarmRecords.add(gisAlarmRecord);
				}
			}

			StringBuilder xBuilder = new StringBuilder("[");
			StringBuilder yBuilder = new StringBuilder("[");
			if (gisAlarmRecords.size() > 0) {
				for (GisAlarmRecord gisAlarmRecord : gisAlarmRecords) {
					xBuilder.append("'").append(gisAlarmRecord.getDev())
							.append("',");
					yBuilder.append(gisAlarmRecord.getCount()).append(",");
				}
				xBuilder.deleteCharAt(xBuilder.length() - 1).append("]");
				yBuilder.deleteCharAt(yBuilder.length() - 1).append("]");
			} else {
				xBuilder.append("]");
				yBuilder.append("]");
			}
			map.clear();
			map.put("xx", xBuilder.toString());
			map.put("yy", yBuilder.toString());
			JSONTool.writeDataResult(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 统计在一段时间内所有设备发生故障的次数
	@SuppressWarnings("unchecked")
	public void queryAlarmRecord4AllUnActive() throws IOException {
		try {
			Integer count = 0;
			StringBuilder hql = new StringBuilder();
			Map<String, Object> map = new HashMap<String, Object>();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

			List<String> nameList = new ArrayList<String>();
			List<GisAlarmRecord> gisAlarmRecords = new ArrayList<GisAlarmRecord>();

			hql.delete(0, hql.length());
			map.clear();
			hql.append("select distinct typeName from DeviceType");
			nameList = alarmRecordManager.find(hql.toString(), map);

			hql.delete(0, hql.length());
			hql.append("select count(*) from AlarmRecord where active=false and device.deviceType.typeName=:nam");
			if (StringUtils.isNotBlank(startTime)) {
				hql.append(" and recordDate>=:start");
			}
			if (StringUtils.isNotBlank(endTime)) {
				hql.append(" and recordDate<=:end");
			}
			for (String nam : nameList) {
				map.clear();
				map.put("nam", nam);
				if (StringUtils.isNotBlank(startTime)) {
					map.put("start", dateFormat.parse(startTime));
				}
				if (StringUtils.isNotBlank(endTime)) {
					map.put("end", dateFormat.parse(endTime));
				}
				count = alarmRecordManager.getCount(hql.toString(), map);

				if (0 < count) {
					GisAlarmRecord gisAlarmRecord = new GisAlarmRecord();
					gisAlarmRecord.setDev(nam);
					gisAlarmRecord.setCount(count);
					gisAlarmRecords.add(gisAlarmRecord);
				}
			}

			StringBuilder xBuilder = new StringBuilder("[");
			StringBuilder yBuilder = new StringBuilder("[");
			if (gisAlarmRecords.size() > 0) {
				for (GisAlarmRecord gisAlarmRecord : gisAlarmRecords) {
					xBuilder.append("'").append(gisAlarmRecord.getDev())
							.append("',");
					yBuilder.append(gisAlarmRecord.getCount()).append(",");
				}
				xBuilder.deleteCharAt(xBuilder.length() - 1).append("]");
				yBuilder.deleteCharAt(yBuilder.length() - 1).append("]");
			} else {
				xBuilder.append("]");
				yBuilder.append("]");
			}
			map.clear();
			map.put("xx", xBuilder.toString());
			map.put("yy", yBuilder.toString());
			JSONTool.writeDataResult(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void queryAlarmRecord4PartActive() {
		try {
			String[] codeArray = devCodes.split("&");
			
			StringBuilder hql = new StringBuilder();
			Map<String, Object> hqlMap = new HashMap<String, Object>();
			
			hql.append("from AlarmRecord where active=true and device.devCode in (:cds)");
			hqlMap.put("cds", codeArray);
			List<AlarmRecord> alarmRecords = alarmRecordManager.find(hql.toString(), hqlMap);
			
			Map<String, Object>resultMap=new HashMap<String, Object>();
			for(AlarmRecord alarmRecord : alarmRecords){
				String typeName = alarmRecord.getDevice().getDeviceType().getTypeName();
				if (resultMap.containsKey(typeName)) {
					int count = (Integer) resultMap.get(typeName);
					count++;
					resultMap.put(typeName, count);
				} else {
					resultMap.put(typeName, 1);
				}
			}


			List<GisAlarmRecord> gisAlarmRecords = new ArrayList<GisAlarmRecord>();
			if (resultMap.size() > 0) {
				for (Entry<String, Object> ent : resultMap.entrySet()) {
					GisAlarmRecord gisAlarmRecord = new GisAlarmRecord();
					gisAlarmRecord.setDev(ent.getKey());
					gisAlarmRecord.setCount((Integer) ent.getValue());
					gisAlarmRecords.add(gisAlarmRecord);
				}
			}

			StringBuilder xBuilder = new StringBuilder("[");
			StringBuilder yBuilder = new StringBuilder("[");
			if (gisAlarmRecords.size() > 0) {
				for (GisAlarmRecord gisAlarmRecord : gisAlarmRecords) {
					xBuilder.append("'").append(gisAlarmRecord.getDev())
							.append("',");
					yBuilder.append(gisAlarmRecord.getCount()).append(",");
				}
				xBuilder.deleteCharAt(xBuilder.length() - 1).append("]");
				yBuilder.deleteCharAt(yBuilder.length() - 1).append("]");
			} else {
				xBuilder.append("]");
				yBuilder.append("]");
			}
			hqlMap.clear();
			hqlMap.put("xx", xBuilder.toString());
			hqlMap.put("yy", yBuilder.toString());
			JSONTool.writeDataResult(hqlMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void queryAlarmRecord4PartUnActive() {
		try {
			String[] codeArray = devCodes.split("&");
			
			StringBuilder hql = new StringBuilder();
			Map<String, Object> hqlMap = new HashMap<String, Object>();
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

			hql.append("from AlarmRecord where active=false and device.devCode in (:cds)");
			hqlMap.put("cds", codeArray);
			if (StringUtils.isNotBlank(startTime)) {
				hql.append(" and recordDate>=:start");
				hqlMap.put("start", dateFormat.parse(startTime));
			}

			if (StringUtils.isNotBlank(endTime)) {
				hql.append(" and recordDate<=:end");
				hqlMap.put("end", dateFormat.parse(endTime));
			}
			List<AlarmRecord> alarmRecords = alarmRecordManager.find(hql.toString(), hqlMap);
			
			Map<String, Object>resultMap=new HashMap<String, Object>();
			for(AlarmRecord alarmRecord : alarmRecords){
				String typeName = alarmRecord.getDevice().getDeviceType().getTypeName();
				if (resultMap.containsKey(typeName)) {
					int count = (Integer) resultMap.get(typeName);
					count++;
					resultMap.put(typeName, count);
				} else {
					resultMap.put(typeName, 1);
				}
			}


			List<GisAlarmRecord> gisAlarmRecords = new ArrayList<GisAlarmRecord>();
			if (resultMap.size() > 0) {
				for (Entry<String, Object> ent : resultMap.entrySet()) {
					GisAlarmRecord gisAlarmRecord = new GisAlarmRecord();
					gisAlarmRecord.setDev(ent.getKey());
					gisAlarmRecord.setCount((Integer) ent.getValue());
					gisAlarmRecords.add(gisAlarmRecord);
				}
			}

			StringBuilder xBuilder = new StringBuilder("[");
			StringBuilder yBuilder = new StringBuilder("[");
			if (gisAlarmRecords.size() > 0) {
				for (GisAlarmRecord gisAlarmRecord : gisAlarmRecords) {
					xBuilder.append("'").append(gisAlarmRecord.getDev())
							.append("',");
					yBuilder.append(gisAlarmRecord.getCount()).append(",");
				}
				xBuilder.deleteCharAt(xBuilder.length() - 1).append("]");
				yBuilder.deleteCharAt(yBuilder.length() - 1).append("]");
			} else {
				xBuilder.append("]");
				yBuilder.append("]");
			}
			hqlMap.clear();
			hqlMap.put("xx", xBuilder.toString());
			hqlMap.put("yy", yBuilder.toString());
			JSONTool.writeDataResult(hqlMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
