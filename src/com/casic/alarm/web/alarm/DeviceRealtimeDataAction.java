package com.casic.alarm.web.alarm;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import com.casic.alarm.JSON.DeviceJSON;
import com.casic.alarm.domain.Device;
import com.casic.alarm.form.DeviceRealTimeDataForm;
import com.casic.alarm.manager.DeviceManager;
import com.casic.alarm.manager.DeviceTypeManager;
import com.casic.alarm.manager.SensorFlowRecordManager;
import com.casic.alarm.manager.SensorNoiseRecordManage;
import com.casic.alarm.manager.SensorNoiseRecordV2Manage;
import com.casic.alarm.manager.SensorPressRecordManage;
import com.casic.alarm.utils.Constants;
import com.casic.core.json.JSONTool;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

import java.sql.Timestamp;
import java.text.ParseException;

import com.casic.alarm.JSON.DeviceConfigJSON;
import com.casic.alarm.JSON.LiquidPipeRecordJSON;
import com.casic.alarm.JSON.LiquidRecordJSON;
import com.casic.alarm.domain.AdDjLiquid;
import com.casic.alarm.domain.AlarmLiquidRecord;
import com.casic.alarm.manager.AdDjLiquidManager;
import com.casic.alarm.manager.AlarmLiquidRecordManager;

/**
 * 获取设备实时数据action
 * 
 * @author liuxin
 * 
 */
public class DeviceRealtimeDataAction implements
		ModelDriven<DeviceRealTimeDataForm>, Preparable {

	private DeviceTypeManager deviceTypeManager;

	private DeviceManager deviceManager;

	private SensorFlowRecordManager sensorFlowRecordManage;

	private SensorPressRecordManage sensorPressRecordManage;

	private SensorNoiseRecordManage sensorNoiseRecordManage;

	private SensorNoiseRecordV2Manage sensorNoiseRecordV2Manage;

	protected DeviceRealTimeDataForm model = new DeviceRealTimeDataForm();

	private AdDjLiquidManager adDjLiquidManager;

	private AlarmLiquidRecordManager alarmLiquidRecordManage;

	private final int MAX_RECORD = 5;
	
	private SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");

	public String execute() {
		return "success";
	}

	/**
	 * 获取设备的属性
	 */
	public void getDeviceProps() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String devCode = model.getDevCode();
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "select device from Device device where device.devCode=:devCode and device.active=:active";

		if (null != devCode && !"".equals(devCode)) {
			List<DeviceJSON> deviceJSONList = new ArrayList<DeviceJSON>();
			map.put("devCode", devCode);
			map.put("active", Boolean.TRUE);
			List<Device> deviceList = deviceManager.find(hql, map);
			for (Device device : deviceList) {
				DeviceJSON json = new DeviceJSON();
				json.setDevCode(device.getDevCode());
				json.setDevName(device.getDevName());
				json.setTypeName(device.getDeviceType().getTypeName());
				json.setLocation(device.getDeviceType().getLocation());
				json.setLongtitude(device.getLongtitude());
				json.setLatitude(device.getLatitude());
				json.setHeight(device.getHeight());
				if(null != device.getInstallDate()) {
				    json.setInstallDate(dateFormat.format(device.getInstallDate()));
				} else {
					json.setInstallDate("");
				}
				if(null != device.getOutDate()) {
				    json.setOutDate(dateFormat.format(device.getOutDate()));
				} else {
					json.setOutDate("");
				}
				json.setFactory(device.getFactory());
				deviceJSONList.add(json);
			}
			resultMap.put("total", deviceList.size());
			resultMap.put("rows", deviceJSONList);

		}

		try {
			JSONTool.writeDataResult(resultMap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取实时或历史数据
	 */
	@SuppressWarnings("unchecked")
	public void getDeviceData() {
		String devCode = model.getDevCode();
		String dataType = model.getDataType(); // 设备类型 by nn
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String typeName = null;

		/**
		 * 查询设备类型
		 */
		String hql = "select device from Device device where device.devCode=:devCode";
		paramMap = new HashMap<String, Object>();
		paramMap.put("devCode", devCode);
		List<Device> deviceList = deviceManager.find(hql, paramMap);
		for (Device device : deviceList) {
			typeName = device.getDeviceType().getTypeName();
			break;
		}

		/**
		 * 根据设备类型查询记录表
		 */
		String flowHql, pressHql, noiseHql;
		if (Constants.DEVICE_TYPE_DJ.equalsIgnoreCase(typeName)) {
			if (Constants.DATA_TYPE_REALTIME.equals(model.getDataType())) {
				Map<String, Object> dataMap = new HashMap<String, Object>();
				flowHql = "select sensorFlowRecord.insData from SensorFlowRecord sensorFlowRecord"
						+ " where sensorFlowRecord.devId=:devId order by sensorFlowRecord.uptime desc";
				noiseHql = "select sensorNoiseRecord.dData from SensorNoiseRecord sensorNoiseRecord"
						+ " where sensorNoiseRecord.devId=:devId order by sensorNoiseRecord.uptime desc";
				pressHql = "select sensorPressRecord.pressData from SensorPressRecord sensorPressRecord"
						+ " where sensorPressRecord.devId=:devId order by sensorPressRecord.uptime desc";

				paramMap.put("devId", devCode);
				List<Object[]> resFlowObjArrayList = sensorFlowRecordManage
						.createQuery(flowHql, paramMap).setFirstResult(0)
						.setMaxResults(1).list();
				List<Object[]> resPressObjArrayList = sensorPressRecordManage
						.createQuery(pressHql, paramMap).setFirstResult(0)
						.setMaxResults(1).list();
				List<Object[]> resNoiseObjArrayList = sensorNoiseRecordManage
						.createQuery(noiseHql, paramMap).setFirstResult(0)
						.setMaxResults(1).list();
				for (Object sensorFlowRecordData : resFlowObjArrayList) {
					String insData = (String) sensorFlowRecordData;
					if(null != insData && !"".equals(insData)) {
					    dataMap.put("insData", insData);
					} else {
						dataMap.put("insData", "0");
					}
					break;
				}
				for (Object sensorPressRecordData : resPressObjArrayList) {
					String insData = (String) sensorPressRecordData;
					if(null != insData && !"".equals(insData)) {
					    dataMap.put("pressData", insData);
					} else {
						dataMap.put("pressData", "0");
					}
					break;
				}
				for (Object sensorNoiseRecordData : resNoiseObjArrayList) {
					String insData = (String) sensorNoiseRecordData;
					if(null != insData && !"".equals(insData)) {
					    dataMap.put("denseData", insData);
					} else {
						dataMap.put("denseData", "0");
					}
					break;
				}
				resultMap.put("data", dataMap);
			} else if (Constants.DATA_TYPE_HISTORY.equals(model.getDataType())) {
				flowHql = "select sensorFlowRecord.uptime, sensorFlowRecord.insData from SensorFlowRecord sensorFlowRecord"
						+ " where sensorFlowRecord.devId=:devId order by sensorFlowRecord.uptime asc";
				noiseHql = "select sensorNoiseRecord.uptime, sensorNoiseRecord.dData from SensorNoiseRecord sensorNoiseRecord"
						+ " where sensorNoiseRecord.devId=:devId order by sensorNoiseRecord.uptime asc";
				pressHql = "select sensorPressRecord.uptime, sensorPressRecord.pressData from SensorPressRecord sensorPressRecord"
						+ " where sensorPressRecord.devId=:devId order by sensorPressRecord.uptime asc";

				paramMap.put("devId", devCode);
				List<Object[]> flowArrayList = sensorFlowRecordManage
						.createQuery(flowHql, paramMap).setFirstResult(0)
						.setMaxResults(MAX_RECORD).list();
				List<Object[]> pressArrayList = sensorPressRecordManage
						.createQuery(pressHql, paramMap).setFirstResult(0)
						.setMaxResults(MAX_RECORD).list();
				List<Object[]> noiseArrayList = sensorNoiseRecordManage
						.createQuery(noiseHql, paramMap).setFirstResult(0)
						.setMaxResults(MAX_RECORD).list();

				List<Map<String, Object>> dataMapList = new ArrayList<Map<String, Object>>();
				Map<String, Object> flowDataMap = new HashMap<String, Object>();
				List<Map<String, Object>> flowResultList = new ArrayList<Map<String, Object>>();
				if (null != flowArrayList && flowArrayList.size() != 0) {
					for (Object[] sensorFlowRecordData : flowArrayList) {
						Map<String, Object> map = new HashMap<String, Object>();
						Date recordTime = (Date) sensorFlowRecordData[0];
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						if(null != recordTime) {
						    map.put("recordTime", simpleDateFormat.format(recordTime));
						} else {
							map.put("recordTime", "");
						}
						String dataValue = (String) sensorFlowRecordData[1];
						if(null != dataValue && !"".equals(dataValue)) {
						    map.put("dataValue", dataValue);
						} else {
							map.put("dataValue", "0");
						}
						flowResultList.add(map);
					}
					flowDataMap.put("sensorType", "flow");
					flowDataMap.put("data", flowResultList);
					dataMapList.add(flowDataMap);
				}

				Map<String, Object> pressDataMap = new HashMap<String, Object>();
				List<Map<String, Object>> pressResultList = new ArrayList<Map<String, Object>>();
				if (null != pressArrayList && pressArrayList.size() != 0) {
					for (Object[] sensorPressRecordData : pressArrayList) {
						Map<String, Object> map = new HashMap<String, Object>();
						Date recordTime = (Date) sensorPressRecordData[0];
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						if(null != recordTime) {
						    map.put("recordTime", simpleDateFormat.format(recordTime));
						} else {
							map.put("recordTime", "");
						}
						String dataValue = (String) sensorPressRecordData[1];
						if(null != dataValue && !"".equals(dataValue)) {
						    map.put("dataValue", dataValue);
						} else {
							map.put("dataValue", "0");
						}
						pressResultList.add(map);
					}
					pressDataMap.put("sensorType", "press");
					pressDataMap.put("data", pressResultList);
					dataMapList.add(pressDataMap);
				}

				Map<String, Object> noiseDataMap = new HashMap<String, Object>();
				List<Map<String, Object>> noiseResultList = new ArrayList<Map<String, Object>>();
				if (null != noiseArrayList && noiseArrayList.size() != 0) {
					for (Object[] sensorNoiseRecordData : noiseArrayList) {
						Map<String, Object> map = new HashMap<String, Object>();
						Date recordTime = (Date) sensorNoiseRecordData[0];
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						if(null != recordTime) {
						    map.put("recordTime", simpleDateFormat.format(recordTime));
						} else {
							map.put("recordTime", "");
						}
						String dataValue = (String) sensorNoiseRecordData[1];
						if(null != dataValue && !"".equals(dataValue)) {
						    map.put("dataValue", dataValue);
						} else {
							map.put("dataValue", "0");
						}
						noiseResultList.add(map);
					}
					noiseDataMap.put("sensorType", "noise");
					noiseDataMap.put("data", noiseResultList);
					dataMapList.add(noiseDataMap);
				}

				resultMap.put("data", dataMapList);
			} else if (dataType.equals("param")) {// 获取设备配置参数，没有流量、压力 by nn
				noiseHql = "select sensorNoiseRecord.logtime,sensorNoiseRecord.lData, sensorNoiseRecord.dData, sensorNoiseRecord.dBegin, sensorNoiseRecord.dInterval"
						+ ", sensorNoiseRecord.dCount, sensorNoiseRecord.lBegin, sensorNoiseRecord.lInterval, sensorNoiseRecord.lCount"
						+ ", sensorNoiseRecord.warelessOpen, sensorNoiseRecord.warelessClose from SensorNoiseRecord sensorNoiseRecord"
						+ " where sensorNoiseRecord.devId=:devId order by sensorNoiseRecord.logtime desc";
				paramMap.put("devId", devCode);

				List<Object[]> noiseArrayList = sensorNoiseRecordManage.find(
						noiseHql, paramMap);

				List<Map<String, Object>> dataMapList = new ArrayList<Map<String, Object>>();

				Map<String, Object> noiseDataMap = new HashMap<String, Object>();
				List<Map<String, Object>> noiseResultList = new ArrayList<Map<String, Object>>();
				List<DeviceConfigJSON> deviceConfigJSONList = new ArrayList<DeviceConfigJSON>();

				int num = 0;
				if (null != noiseArrayList && noiseArrayList.size() != 0) {
					for (Object[] sensorNoiseRecordData : noiseArrayList) {
						Map<String, Object> map = new HashMap<String, Object>();
						if (num == 0) {
							String recordTime = sensorNoiseRecordData[0]
									.toString();
							map.put("logtime", recordTime);
							String ldataValue = (String) sensorNoiseRecordData[1];
							if (ldataValue != null) {
								map.put("ldataValue",
										Long.parseLong(ldataValue));
							} else {
								map.put("ldataValue", 0L);
								ldataValue = "0";
							}
							String ddataValue = (String) sensorNoiseRecordData[2];
							if (ddataValue != null) {
								map.put("ddataValue",
										Long.parseLong(ddataValue));
							} else {
								map.put("ddataValue", 0L);
								ddataValue = "0";
							}
							String dbeginValue = (String) sensorNoiseRecordData[3];
							if (dbeginValue != null) {
								map.put("dbeginValue",
										Long.parseLong(dbeginValue));
							} else {
								map.put("dbeginValue", 0L);
								dbeginValue = "0";
							}
							String dintervalValue = (String) sensorNoiseRecordData[4];
							if (dintervalValue != null) {
								map.put("dintervalValue",
										Long.parseLong(dintervalValue));
							} else {
								map.put("dintervalValue", 0L);
								dintervalValue = "0";
							}
							String dcountValue = (String) sensorNoiseRecordData[5];
							if (dcountValue != null) {
								map.put("dcountValue",
										Long.parseLong(dcountValue));
							} else {
								map.put("dcountValue", 0L);
								dcountValue = "0";
							}
							String lbeginValue = (String) sensorNoiseRecordData[6];
							if (lbeginValue != null) {
								map.put("lbeginValue",
										Long.parseLong(lbeginValue));
							} else {
								map.put("lbeginValue", 0L);
								lbeginValue = "0";
							}
							String lintervalValue = (String) sensorNoiseRecordData[7];
							if (lintervalValue != null) {
								map.put("lintervalValue",
										Long.parseLong(lintervalValue));
							} else {
								map.put("lintervalValue", 0L);
								lintervalValue = "0";
							}
							String lcountValue = (String) sensorNoiseRecordData[8];
							if (lcountValue != null) {
								map.put("lcountValue",
										Long.parseLong(lcountValue));
							} else {
								map.put("lcountValue", 0L);
								lcountValue = "0";
							}
							String warelessopenValue = (String) sensorNoiseRecordData[9];
							if (warelessopenValue != null) {
								map.put("warelessopenValue",
										Long.parseLong(warelessopenValue));
							} else {
								map.put("warelessopenValue", 0L);
								warelessopenValue = "0";
							}
							String warelesscloseValue = (String) sensorNoiseRecordData[10];
							if (warelesscloseValue != null) {
								map.put("warelesscloseValue",
										Long.parseLong(warelesscloseValue));
							} else {
								map.put("warelesscloseValue", 0L);
								warelesscloseValue = "0";
							}
							// noiseResultList.add(map);

							DeviceConfigJSON json = new DeviceConfigJSON();
							json.setDevCode(devCode);
							json.setRecordTime(recordTime);
							json.setDbegin(dbeginValue);
							json.setDinterval(dintervalValue);
							json.setDcount(dcountValue);
							json.setLbegin(lbeginValue);
							json.setLinterval(lintervalValue);
							json.setLcount(lcountValue);
							json.setWarelessclose(warelesscloseValue);
							json.setWarelessopen(warelessopenValue);
							deviceConfigJSONList.add(json);

							// resultMap.put("data",map);
							resultMap.put("total", noiseArrayList.size());
							resultMap.put("rows", deviceConfigJSONList);
							num++;
						} else {
							break;
						}

					}
				}
			}
		} else if (Constants.DEVICE_TYPE_SL.equalsIgnoreCase(typeName)) {
			if (Constants.DATA_TYPE_REALTIME.equals(model.getDataType())) {
				Map<String, Object> dataMap = new HashMap<String, Object>();
				noiseHql = "select sensorNoiseRecordV2.denseData from SensorNoiseRecordV2 sensorNoiseRecordV2"
						+ " where sensorNoiseRecordV2.srcId=:devId order by sensorNoiseRecordV2.uptime desc";

				paramMap.put("devId", devCode);
				List<Object[]> resNoiseObjArrayList = sensorNoiseRecordV2Manage
						.createQuery(noiseHql, paramMap).setFirstResult(0)
						.setMaxResults(1).list();

				if (null != resNoiseObjArrayList
						&& resNoiseObjArrayList.size() != 0) {
					for (Object sensorNoiseRecordData : resNoiseObjArrayList) {
						String dataValue = (String) sensorNoiseRecordData;
						if(null != dataValue && !"".equals(dataValue)) {
							dataMap.put("denseData", dataValue);
						} else {
							dataMap.put("denseData", "0");
						}
						break;
					}
					resultMap.put("data", dataMap);
				}

			} else if (Constants.DATA_TYPE_HISTORY.equals(model.getDataType())) {
				noiseHql = "select sensorNoiseRecordV2.uptime, sensorNoiseRecordV2.denseData from SensorNoiseRecordV2 sensorNoiseRecordV2"
						+ " where sensorNoiseRecordV2.srcId=:devId order by sensorNoiseRecordV2.uptime asc";

				paramMap.put("devId", devCode);
				List<Map<String, Object>> dataMapList = new ArrayList<Map<String, Object>>();
				Map<String, Object> noiseDataMap = new HashMap<String, Object>();
				List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
				List<Object[]> resNoiseObjArrayList = sensorNoiseRecordV2Manage
						.createQuery(noiseHql, paramMap).setFirstResult(0)
						.setMaxResults(MAX_RECORD).list();
				if (null != resNoiseObjArrayList
						&& resNoiseObjArrayList.size() != 0) {
					for (Object[] sensorNoiseRecordData : resNoiseObjArrayList) {
						Map<String, Object> map = new HashMap<String, Object>();
						Date recordTime = (Date) sensorNoiseRecordData[0];
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						if(null != recordTime) {
						    map.put("recordTime",
								simpleDateFormat.format(recordTime));
						} else {
						    map.put("recordTime",
								"");
						}
						String dataValue = (String) sensorNoiseRecordData[1];
						if(null != dataValue && !"".equals(dataValue)) {
						    map.put("dataValue", dataValue);
						} else {
							map.put("dataValue", "0");
						}
						resultList.add(map);
					}
					noiseDataMap.put("sensorType", "noise");
					noiseDataMap.put("data", resultList);
					dataMapList.add(noiseDataMap);
				}
				resultMap.put("data", dataMapList);
			} else if (dataType.equals("param")) {// 获取设备配置参数，没有流量、压力 by nn
				noiseHql = "select sensorNoiseRecordV2.logtime,sensorNoiseRecordV2.looseData, sensorNoiseRecordV2.denseData, sensorNoiseRecordV2.dBegin"
						+ ", sensorNoiseRecordV2.dInterval, sensorNoiseRecordV2.dCount, sensorNoiseRecordV2.lBegin, sensorNoiseRecordV2.lInterval"
						+ ", sensorNoiseRecordV2.lCount, sensorNoiseRecordV2.wirelessOpen, sensorNoiseRecordV2.wirelessClose "
						+ " from SensorNoiseRecordV2 sensorNoiseRecordV2 where sensorNoiseRecordV2.srcId=:devId order by sensorNoiseRecordV2.logtime desc";
				paramMap.put("devId", devCode);

				List<Object[]> noiseArrayList = sensorNoiseRecordManage.find(
						noiseHql, paramMap);

				List<Map<String, Object>> dataMapList = new ArrayList<Map<String, Object>>();

				Map<String, Object> noiseDataMap = new HashMap<String, Object>();
				List<Map<String, Object>> noiseResultList = new ArrayList<Map<String, Object>>();
				List<DeviceConfigJSON> deviceConfigJSONList = new ArrayList<DeviceConfigJSON>();

				int num = 0;
				if (null != noiseArrayList && noiseArrayList.size() != 0) {
					for (Object[] sensorNoiseRecordData : noiseArrayList) {
						Map<String, Object> map = new HashMap<String, Object>();
						if (num == 0) {
							String recordTime = sensorNoiseRecordData[0]
									.toString();
							map.put("logtime", recordTime);
							String ldataValue = (String) sensorNoiseRecordData[1];
							if (ldataValue != null) {
								map.put("ldataValue",
										Long.parseLong(ldataValue));
							} else {
								map.put("ldataValue", 0L);
								ldataValue = "0";
							}
							String ddataValue = (String) sensorNoiseRecordData[2];
							if (ddataValue != null) {
								map.put("ddataValue",
										Long.parseLong(ddataValue));
							} else {
								map.put("ddataValue", 0L);
								ddataValue = "0";
							}
							String dbeginValue = (String) sensorNoiseRecordData[3];
							if (dbeginValue != null) {
								map.put("dbeginValue",
										Long.parseLong(dbeginValue));
							} else {
								map.put("dbeginValue", 0L);
								dbeginValue = "0";
							}
							String dintervalValue = (String) sensorNoiseRecordData[4];
							if (dintervalValue != null) {
								map.put("dintervalValue",
										Long.parseLong(dintervalValue));
							} else {
								map.put("dintervalValue", 0L);
								dintervalValue = "0";
							}
							String dcountValue = (String) sensorNoiseRecordData[5];
							if (dcountValue != null) {
								map.put("dcountValue",
										Long.parseLong(dcountValue));
							} else {
								map.put("dcountValue", 0L);
								dcountValue = "0";
							}
							String lbeginValue = (String) sensorNoiseRecordData[6];
							if (lbeginValue != null) {
								map.put("lbeginValue",
										Long.parseLong(lbeginValue));
							} else {
								map.put("lbeginValue", 0L);
								lbeginValue = "0";
							}
							String lintervalValue = (String) sensorNoiseRecordData[7];
							if (lintervalValue != null) {
								map.put("lintervalValue",
										Long.parseLong(lintervalValue));
							} else {
								map.put("lintervalValue", 0L);
								lintervalValue = "0";
							}
							String lcountValue = (String) sensorNoiseRecordData[8];
							if (lcountValue != null) {
								map.put("lcountValue",
										Long.parseLong(lcountValue));
							} else {
								map.put("lcountValue", 0L);
								lcountValue = "0";
							}
							String warelessopenValue = (String) sensorNoiseRecordData[9];
							if (warelessopenValue != null) {
								map.put("warelessopenValue",
										Long.parseLong(warelessopenValue));
							} else {
								map.put("warelessopenValue", 0L);
								warelessopenValue = "0";
							}
							String warelesscloseValue = (String) sensorNoiseRecordData[10];
							if (warelesscloseValue != null) {
								map.put("warelesscloseValue",
										Long.parseLong(warelesscloseValue));
							} else {
								map.put("warelesscloseValue", 0L);
								warelesscloseValue = "0";
							}
							// noiseResultList.add(map);

							DeviceConfigJSON json = new DeviceConfigJSON();
							json.setDevCode(devCode);
							json.setRecordTime(recordTime);
							json.setDbegin(dbeginValue);
							json.setDinterval(dintervalValue);
							json.setDcount(dcountValue);
							json.setLbegin(lbeginValue);
							json.setLinterval(lintervalValue);
							json.setLcount(lcountValue);
							json.setWarelessclose(warelesscloseValue);
							json.setWarelessopen(warelessopenValue);
							deviceConfigJSONList.add(json);

							// resultMap.put("data",map);
							resultMap.put("total", noiseArrayList.size());
							resultMap.put("rows", deviceConfigJSONList);
							num++;
						} else {
							break;
						}

					}
				}
			}
		}

		try {
			JSONTool.writeDataResult(resultMap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// /*********************************************雨水监测----wei********************************************//
	/**
	 * 获取液位计的属性 WEI
	 */
	public void getLiquidProps() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// String devCode = model.getDevCode();
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "select device from Device device where device.deviceType.typeName=:typeName and device.active=:active";
		List<DeviceJSON> deviceJSONList = new ArrayList<DeviceJSON>();
		map.put("typeName", "液位监测仪");
		map.put("active", Boolean.TRUE);
		@SuppressWarnings("unchecked")
		List<Device> deviceList = deviceManager.find(hql, map);
		for (Device device : deviceList) {
			DeviceJSON json = new DeviceJSON();
			json.setDevCode(device.getDevCode());
			json.setDevName(device.getDevName());
			json.setTypeName(device.getDeviceType().getTypeName());
			json.setLocation(device.getDeviceType().getLocation());
			json.setLongtitude(device.getLongtitude());
			json.setLatitude(device.getLatitude());
			json.setHeight(device.getHeight());
			if(null!=device&& null!=device.getInstallDate()){
				json.setInstallDate(dateFormat.format(device.getInstallDate()));
			}else{
				json.setInstallDate("");
			}
			if(null!=device&&null!=device.getOutDate()){
				json.setOutDate(dateFormat.format(device.getOutDate()));
			}else{
				json.setOutDate("");
			}
			json.setFactory(device.getFactory());
			deviceJSONList.add(json);
		}
		resultMap.put("total", deviceList.size());
		resultMap.put("rows", deviceJSONList);
		try {
			JSONTool.writeDataResult(resultMap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取液位计实时数据 wei
	 */
	@SuppressWarnings("deprecation")
	public void getLiquidRecord() {
		String devCode = model.getDevCode();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String hql = "select adDjLiquid from AdDjLiquid adDjLiquid "
				+ " where adDjLiquid.devId=:devId order by adDjLiquid.uptime desc";
		List<LiquidRecordJSON> liquidRecordJSONList = new ArrayList<LiquidRecordJSON>();

		paramMap.put("devId", devCode);
		List<AdDjLiquid> resLiquidObjArrayList = adDjLiquidManager
				.createQuery(hql, paramMap).setFirstResult(0).setMaxResults(1)
				.list();
		for (AdDjLiquid sensorLiquidRecordData : resLiquidObjArrayList) {
			LiquidRecordJSON json = new LiquidRecordJSON();
			json.setDevId(sensorLiquidRecordData.getDevId());
			json.setLiquidData(sensorLiquidRecordData.getLiquidData());
			json.setLiquidPower(sensorLiquidRecordData.getCell());
			json.setTime(sensorLiquidRecordData.getUptime().toString());
			liquidRecordJSONList.add(json);
		}
		resultMap.put("total", resLiquidObjArrayList.size());
		resultMap.put("rows", liquidRecordJSONList);
		try {
			JSONTool.writeDataResult(resultMap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查询液位计的属性 wei
	 */
	public void searchLiquidProps() {
		String devCode = model.getDevCode();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// String devCode = model.getDevCode();
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "select device from Device device where device.deviceType.typeName=:typeName"
				+ " and device.devCode=:devCode and device.active=:active";
		List<DeviceJSON> deviceJSONList = new ArrayList<DeviceJSON>();
		map.put("typeName", "液位监测仪");
		map.put("devCode", devCode);
		map.put("active", Boolean.TRUE);
		@SuppressWarnings("unchecked")
		List<Device> deviceList = deviceManager.find(hql, map);
		for (Device device : deviceList) {
			String str = device.getDevCode();
			if (str.equals(devCode)) {
				DeviceJSON json = new DeviceJSON();
				json.setDevCode(device.getDevCode());
				json.setDevName(device.getDevName());
				json.setTypeName(device.getDeviceType().getTypeName());
				json.setLocation(device.getDeviceType().getLocation());
				json.setLongtitude(device.getLongtitude());
				json.setLatitude(device.getLatitude());
				json.setHeight(device.getHeight());
				if(null!=device&&null!=device.getInstallDate()){
					json.setInstallDate(dateFormat.format(device.getInstallDate()));
				}else{
					json.setInstallDate("");
				}
				if(null!=device&&null!=device.getOutDate()){
					json.setOutDate(dateFormat.format(device.getOutDate()));
				}else{
					json.setOutDate("");
				}
				json.setFactory(device.getFactory());
				deviceJSONList.add(json);
			}
		}
		resultMap.put("total", deviceList.size());
		resultMap.put("rows", deviceJSONList);
		try {
			JSONTool.writeDataResult(resultMap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void getAlarmLiquidTransSection() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String hql = "select alarmLiquidRecord from AlarmLiquidRecord alarmLiquidRecord"
				+ " where alarmLiquidRecord.dbcord=:dbCord"
				+ " order by alarmLiquidRecord.upTime desc";
		paramMap.put("dbCord", model.getDbCord());
		@SuppressWarnings("unchecked")
		List<AlarmLiquidRecord> resLiquidObjArrayList = alarmLiquidRecordManage
				.createQuery(hql, paramMap).setFirstResult(0).setMaxResults(1)
				.list();
		resultMap.put("liquid", resLiquidObjArrayList.get(0).getLiquid());
		resultMap.put("diameter", resLiquidObjArrayList.get(0).getDiameter());

		try {
			JSONTool.writeDataResult(resultMap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// /**
	// *
	// * 获取设备历史数据
	// */
	// public void getDeviceData() {
	// String devCode = model.getDevCode();
	// String hql =
	// "select sensorFlowRecord.recordTime, sensorFlowRecord.insData from SensorFlowRecord sensorFlowRecord"
	// +
	// " where sensorFlowRecord.devId=:devId order by sensorFlowRecord.recordTime desc";
	// Map<String, Object> paramMap = new HashMap<String, Object>();
	// List<Map<String, String>> resultList = new ArrayList<Map<String,
	// String>>();
	//
	// if(null != devCode && !"".endsWith(devCode)) {
	// paramMap.put("devId", devCode);
	// List<Object[]> resObjArrayList = sensorFlowRecordManager.find(hql,
	// paramMap);
	// for (Object[] sensorFlowRecordData : resObjArrayList) {
	// Map<String, String> map = new HashMap<String, String>();
	// String recordTime = (String)sensorFlowRecordData[0];
	// map.put("recordTime", recordTime);
	// String insData = (String)sensorFlowRecordData[1];
	// map.put("insData", insData);
	// resultList.add(map);
	// }
	//
	// try {
	// JSONTool.writeDataResult(resultList);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// }

	public void getAlarmLiquidHistory() {
		Timestamp timeNow = new Timestamp(new Date().getTime());

		@SuppressWarnings("deprecation")
		int h = timeNow.getHours() - 1;
		@SuppressWarnings("deprecation")
		Timestamp afterTime = new Timestamp(timeNow.getYear(),
				timeNow.getMonth(), timeNow.getDate(), h, timeNow.getMinutes(),
				timeNow.getSeconds(), timeNow.getNanos());
		List<String> strCode = new ArrayList<String>();
		Map<String, Object> paramMap1 = new HashMap<String, Object>();
		String hql1 = "select alarmLiquidRecord from AlarmLiquidRecord alarmLiquidRecord where alarmLiquidRecord.upTime <:timeNow"
				+ " and alarmLiquidRecord.upTime >:afterTime and alarmLiquidRecord.dbcord=:dbCord";
		paramMap1.put("timeNow", timeNow);
		paramMap1.put("afterTime", afterTime);
		paramMap1.put("dbCord", model.getDbCord());
		@SuppressWarnings("unchecked")
		List<AlarmLiquidRecord> resLiquidObjArrayList1 = alarmLiquidRecordManage
				.find(hql1, paramMap1);
		if (resLiquidObjArrayList1 == null
				|| resLiquidObjArrayList1.size() == 0) {
			try {
				strCode.add("noneRain");
				JSONTool.writeDataResult(strCode);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}

		Map<String, Object> paramMap2 = new HashMap<String, Object>();
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

		String hql2 = "select alarmLiquidRecord from AlarmLiquidRecord alarmLiquidRecord"
				+ " where alarmLiquidRecord.dbcord=:dbCord"
				+ " and alarmLiquidRecord.rainNum="
				+ " (select max(alarmLiquidRecord.rainNum) from AlarmLiquidRecord alarmLiquidRecord"
				+ " where alarmLiquidRecord.dbcord=:dbCord)"
				+ " order by alarmLiquidRecord.upTime asc";
		paramMap2.put("dbCord", model.getDbCord());
		paramMap2.put("dbCord", model.getDbCord());
		@SuppressWarnings("unchecked")
		List<AlarmLiquidRecord> resLiquidObjArrayList2 = alarmLiquidRecordManage
				.createQuery(hql2, paramMap2).list();
		for (AlarmLiquidRecord alarmLiquidRecordData : resLiquidObjArrayList2) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			Date uptime = (Date) alarmLiquidRecordData.getUpTime();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			tempMap.put("liquid", alarmLiquidRecordData.getLiquid());
			if(null!=uptime){
				tempMap.put("uptime", simpleDateFormat.format(uptime));
			}
			dataList.add(tempMap);
		}

		try {
			JSONTool.writeDataResult(dataList);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取雨水管线溢水管线列表 wei
	 */
	public void getAlarmLiquidRecord() {
		Timestamp timeNow = new Timestamp(new Date().getTime());
		@SuppressWarnings("deprecation")
		int h = timeNow.getHours() - 1;
		@SuppressWarnings("deprecation")
		Timestamp afterTime = new Timestamp(timeNow.getYear(),
				timeNow.getMonth(), timeNow.getDate(), h, timeNow.getMinutes(),
				timeNow.getSeconds(), timeNow.getNanos());
		double diameter = 0;
		double liquidData = 0;
		int num = 0;
		List<String> strCode = new ArrayList<String>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String hql = "select alarmLiquidRecord from AlarmLiquidRecord alarmLiquidRecord where alarmLiquidRecord.upTime <:timeNow"
				+ " and alarmLiquidRecord.upTime >:afterTime order by alarmLiquidRecord.upTime desc";
		paramMap.put("timeNow", timeNow);
		paramMap.put("afterTime", afterTime);
		List<AlarmLiquidRecord> resLiquidObjArrayList = alarmLiquidRecordManage
				.find(hql, paramMap);
		if (resLiquidObjArrayList == null || resLiquidObjArrayList.size() == 0) {
			try {
				strCode.add("noneRain");
				JSONTool.writeDataResult(strCode);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		for (AlarmLiquidRecord alarmLiquidRecordData : resLiquidObjArrayList) {
			num++;
			// 管线和管点总数量 传过来
			// if(num>21)
			// {
			// break;
			// }
			liquidData = alarmLiquidRecordData.getLiquid();
			diameter = alarmLiquidRecordData.getDiameter();
			if (liquidData / diameter > 0.75) {
				if (!strCode.contains(alarmLiquidRecordData.getDbcord())) {
					strCode.add(alarmLiquidRecordData.getDbcord());
				}
			}
		}
		num = 0;
		try {
			JSONTool.writeDataResult(strCode);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void getAlarmLiquidRecordList() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<String> strCode = new ArrayList<String>();
		boolean flag2 = false;
		Timestamp timeNow = new Timestamp(new Date().getTime());
		@SuppressWarnings("deprecation")
		int h = timeNow.getHours() - 1;
		@SuppressWarnings("deprecation")
		Timestamp afterTime = new Timestamp(timeNow.getYear(),
				timeNow.getMonth(), timeNow.getDate(), h, timeNow.getMinutes(),
				timeNow.getSeconds(), timeNow.getNanos());
		int num = 0;
		// String
		// hql="select alarmLiquidRecord from AlarmLiquidRecord alarmLiquidRecord where alarmLiquidRecord.upTime <:timeNow"
		// +
		// "and alarmLiquidRecord.upTime >:afterTime order by alarmLiquidRecord.upTime desc";

		String hql = "select alarmLiquidRecord from AlarmLiquidRecord alarmLiquidRecord where alarmLiquidRecord.upTime <:timeNow"
				+ " and alarmLiquidRecord.upTime >:afterTime order by alarmLiquidRecord.upTime desc";

		paramMap.put("timeNow", timeNow);
		paramMap.put("afterTime", afterTime);
		@SuppressWarnings("unchecked")
		List<AlarmLiquidRecord> resLiquidObjArrayList = alarmLiquidRecordManage
				.find(hql, paramMap);
		List<LiquidPipeRecordJSON> liquidRecordJSONList = new ArrayList<LiquidPipeRecordJSON>();
		for (AlarmLiquidRecord alarmLiquidRecordData : resLiquidObjArrayList) {
			num++;
			// 管线和管点总数量
			if (num > 21) {
				break;
			}
			double liquidData = alarmLiquidRecordData.getLiquid();
			double diameter = alarmLiquidRecordData.getDiameter();
			if (liquidData / diameter > 0.75) {
				// String cordStr = alarmLiquidRecordData.getDbcord();
				// for(int i = 0; i < strCode.size(); i++) {
				// if(strCode.get(i)==cordStr){
				// flag2 =true;
				// break;
				// }//
				// }
				LiquidPipeRecordJSON json = new LiquidPipeRecordJSON();
				json.setDbCord(alarmLiquidRecordData.getDbcord());
				json.setLiquid(alarmLiquidRecordData.getLiquid());
				json.setDiameter(alarmLiquidRecordData.getDiameter());
				int flage1 = alarmLiquidRecordData.getCordType();
				if (flage1 == 1)
					json.setType("管井");
				else {
					json.setType("管线");
				}
				json.setTime(alarmLiquidRecordData.getUpTime().toString());
				liquidRecordJSONList.add(json);
			}
		}
		num = 0;
		resultMap.put("total", liquidRecordJSONList.size());
		resultMap.put("rows", liquidRecordJSONList);
		try {
			// JSONTool.writeMsgResult(false, "当前未下雨，没有入流入渗管线！");
			JSONTool.writeDataResult(resultMap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取雨水管充满频次统计 wei
	 * 
	 */
	public class rainPipeCode {
		public String pipeCode;
		public long pipeNum = 0;
		public List<Integer> numList = new ArrayList<Integer>();
		public long rainNum = 0;
		public String t;
		// public long numList = NewArray [];
	}

	public void getAlarmLiquidFrequency() {
		String upTime = model.getUpTime();
		String endTime = model.getEndTime();
		boolean flag = false;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		double diameter = 0;
		double liquidData = 0;
		List<rainPipeCode> strCodeList = new ArrayList<rainPipeCode>();
		try {
			Date dtUp = dateFormat.parse(upTime);
			Timestamp upTimeSt = new Timestamp(dtUp.getTime());
			Date dtEnd = dateFormat.parse(endTime);
			Timestamp endTimeSt = new Timestamp(dtEnd.getTime());

			String hql = "select alarmLiquidRecord from AlarmLiquidRecord alarmLiquidRecord where alarmLiquidRecord.upTime > :upTimeStm "
					+ "and alarmLiquidRecord.upTime < :endTimeStm order by alarmLiquidRecord.upTime asc";

			paramMap.put("upTimeStm", upTimeSt);
			paramMap.put("endTimeStm", endTimeSt);

			List<AlarmLiquidRecord> resLiquidObjArrayList = alarmLiquidRecordManage
					.find(hql, paramMap);
			List<LiquidPipeRecordJSON> liquidRecordJSONList = new ArrayList<LiquidPipeRecordJSON>();
			if (resLiquidObjArrayList == null
					|| resLiquidObjArrayList.size() == 0) {
				return;
			}

			for (AlarmLiquidRecord alarmLiquidRecordData : resLiquidObjArrayList) {
				liquidData = alarmLiquidRecordData.getLiquid();
				diameter = alarmLiquidRecordData.getDiameter();
				String dbCode = alarmLiquidRecordData.getDbcord();
				if (liquidData / diameter > 0.75) {
					for (int i = 0; i < strCodeList.size(); i++) {
						String vidCode = strCodeList.get(i).pipeCode;
						// String dbCode = alarmLiquidRecordData.getDbcord();
						if (vidCode.equals(dbCode)) {
							strCodeList.get(i).pipeNum++;
							flag = true;
							break;
						}
					}
					if (flag == false) {
						rainPipeCode rp = new rainPipeCode();
						rp.pipeCode = alarmLiquidRecordData.getDbcord();
						rp.pipeNum = 1;
						strCodeList.add(rp);
					}
					flag = false;
				}
			}
			for (int j = 0; j < strCodeList.size(); j++) {
				LiquidPipeRecordJSON json = new LiquidPipeRecordJSON();

				json.setDbCord(strCodeList.get(j).pipeCode);
				json.setTimes(strCodeList.get(j).pipeNum);
				liquidRecordJSONList.add(json);
			}
			resultMap.put("total", liquidRecordJSONList.size());
			resultMap.put("rows", liquidRecordJSONList);
			try {
				JSONTool.writeDataResult(resultMap);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取雨水管段次数列 wei
	 * 
	 */
	public void getAlarmLiquidRecordNum() {
		String upTime = model.getUpTime();
		String endTime = model.getEndTime();
		boolean flag = false;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		double diameter = 0;
		double liquidData = 0;
		// long rainNum=0;
		List<rainPipeCode> strCodeList = new ArrayList<rainPipeCode>();
		try {
			Date dtUp = dateFormat.parse(upTime);
			Timestamp upTimeSt = new Timestamp(dtUp.getTime());
			Date dtEnd = dateFormat.parse(endTime);
			Timestamp endTimeSt = new Timestamp(dtEnd.getTime());
			String hql = "select alarmLiquidRecord from AlarmLiquidRecord alarmLiquidRecord where alarmLiquidRecord.upTime > :upTimeStm "
					+ "and alarmLiquidRecord.upTime < :endTimeStm and alarmLiquidRecord.cordType =:cordType order by alarmLiquidRecord.upTime asc";
			paramMap.put("upTimeStm", upTimeSt);
			paramMap.put("endTimeStm", endTimeSt);
			paramMap.put("cordType", 0);
			List<AlarmLiquidRecord> resLiquidObjArrayList = alarmLiquidRecordManage
					.find(hql, paramMap);
			if (resLiquidObjArrayList == null
					|| resLiquidObjArrayList.size() == 0) {
				try {
					JSONTool.writeDataResult("noneRain");
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
			for (AlarmLiquidRecord alarmLiquidRecordData : resLiquidObjArrayList) {
				liquidData = alarmLiquidRecordData.getLiquid();
				diameter = alarmLiquidRecordData.getDiameter();

				double fen = liquidData / diameter;
				int station = 0;
				if (fen > 0.75) {
					station = 3;
				}
				if (fen > 0.5 && fen <= 0.75) {
					station = 2;
				}
				if (fen > 0.25 && fen <= 0.5) {
					station = 1;
				}

				String dbCode = alarmLiquidRecordData.getDbcord();
				long rainNumNow = alarmLiquidRecordData.getRainNum();
				for (int i = 0; i < strCodeList.size(); i++) {
					String vidCode = strCodeList.get(i).pipeCode;
					long rainNum1 = strCodeList.get(i).rainNum;
					// String dbCode = alarmLiquidRecordData.getDbcord();
					if (vidCode.equals(dbCode) && (rainNum1 == rainNumNow)) {
						strCodeList.get(i).numList.add(station);
						flag = true;
						break;
					}
				}
				if (flag == false) {
					rainPipeCode rp = new rainPipeCode();
					rp.pipeCode = alarmLiquidRecordData.getDbcord();
					rp.numList.add(station);
					rp.rainNum = alarmLiquidRecordData.getRainNum();
					rp.t = alarmLiquidRecordData.getUpTime().toString();
					strCodeList.add(rp);
				}
				flag = false;
			}
			try {
				JSONTool.writeDataResult(strCodeList);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取雨水管线溢水管线列表 wei
	 */
	public void getAlarmLiquidRecordOut() {
		Timestamp timeNow = new Timestamp(new Date().getTime());
		@SuppressWarnings("deprecation")
		int h = timeNow.getHours() - 1;
		@SuppressWarnings("deprecation")
		Timestamp afterTime = new Timestamp(timeNow.getYear(),
				timeNow.getMonth(), timeNow.getDate(), h, timeNow.getMinutes(),
				timeNow.getSeconds(), timeNow.getNanos());
		double diameter = 0;
		double liquidData = 0;
		int num = 1;
		List<String> strCode = new ArrayList<String>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String hql = "select alarmLiquidRecord from AlarmLiquidRecord alarmLiquidRecord "
				+ "where alarmLiquidRecord.upTime <:timeNow and alarmLiquidRecord.cordType =:num and alarmLiquidRecord.upTime >:afterTime"
				+ " order by alarmLiquidRecord.upTime desc";
		paramMap.put("timeNow", timeNow);
		paramMap.put("num", num);
		paramMap.put("afterTime", afterTime);
		List<AlarmLiquidRecord> resLiquidObjArrayList = alarmLiquidRecordManage
				.find(hql, paramMap);
		if (resLiquidObjArrayList == null || resLiquidObjArrayList.size() == 0) {
			try {
				strCode.add("noneRain");
				JSONTool.writeDataResult(strCode);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		for (AlarmLiquidRecord alarmLiquidRecordData : resLiquidObjArrayList) {
			num++;
			// 管线和管点总数量传过来
			// if(num>21)
			// {
			// break;
			// }
			liquidData = alarmLiquidRecordData.getLiquid();
			diameter = alarmLiquidRecordData.getDiameter();
			if (liquidData / diameter == 1) {
				if (!strCode.contains(alarmLiquidRecordData.getDbcord())) {
					strCode.add(alarmLiquidRecordData.getDbcord());
				}
			}
		}
		num = 0;
		try {
			JSONTool.writeDataResult(strCode);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取雨水管线可能性溢水管线列表 wei
	 */
	public void getAlarmLiquidForeOut() {
		Timestamp timeNow = new Timestamp(new Date().getTime());
		boolean flag = false;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		double diameter = 0;
		double liquidData = 0;
		List<rainPipeCode> strCodeList = new ArrayList<rainPipeCode>();

		String hql = "select alarmLiquidRecord from AlarmLiquidRecord alarmLiquidRecord where alarmLiquidRecord.upTime > :timeNow and alarmLiquidRecord.cordType = :cordType "
				+ "order by alarmLiquidRecord.upTime asc";

		paramMap.put("timeNow", timeNow);
		paramMap.put("cordType", 0);
		List<AlarmLiquidRecord> resLiquidObjArrayList = alarmLiquidRecordManage
				.find(hql, paramMap);
		if (resLiquidObjArrayList == null || resLiquidObjArrayList.size() == 0) {
			try {
				rainPipeCode rp = new rainPipeCode();
				rp.pipeCode = "noneRain";
				strCodeList.add(rp);
				JSONTool.writeDataResult(strCodeList);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		for (AlarmLiquidRecord alarmLiquidRecordData : resLiquidObjArrayList) {
			liquidData = alarmLiquidRecordData.getLiquid();
			diameter = alarmLiquidRecordData.getDiameter();
			double fen = liquidData / diameter;
			int station = 0;
			if (fen > 0.75) {
				station = 3;
			}
			if (fen > 0.5 && fen <= 0.75) {
				station = 2;
			}
			if (fen > 0.25 && fen <= 0.5) {
				station = 1;
			}

			String dbCode = alarmLiquidRecordData.getDbcord();
			for (int i = 0; i < strCodeList.size(); i++) {
				String vidCode = strCodeList.get(i).pipeCode;
				// String dbCode = alarmLiquidRecordData.getDbcord();
				if (vidCode.equals(dbCode)) {
					strCodeList.get(i).numList.add(station);
					flag = true;
					break;
				}
			}
			if (flag == false) {
				rainPipeCode rp = new rainPipeCode();
				rp.pipeCode = alarmLiquidRecordData.getDbcord();
				rp.numList.add(station);
				rp.t = alarmLiquidRecordData.getUpTime().toString();
				strCodeList.add(rp);
			}
			flag = false;
		}
		try {
			JSONTool.writeDataResult(strCodeList);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void getAlarmLiquidForeOutT() {
		Timestamp timeNow = new Timestamp(new Date().getTime());
		boolean flag = false;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		double diameter = 0;
		double liquidData = 0;
		int num = 1;
		List<rainPipeCode> strCodeList = new ArrayList<rainPipeCode>();
		String hql = "select alarmLiquidRecord from AlarmLiquidRecord alarmLiquidRecord "
				+ "where alarmLiquidRecord.upTime >:timeNow and alarmLiquidRecord.cordType =:num "
				+ " order by alarmLiquidRecord.upTime desc";
		paramMap.put("timeNow", timeNow);
		paramMap.put("num", 1);
		List<AlarmLiquidRecord> resLiquidObjArrayList = alarmLiquidRecordManage
				.find(hql, paramMap);
		if (resLiquidObjArrayList == null || resLiquidObjArrayList.size() == 0) {
			try {
				// JSONTool.writeMsgResult(false, "您还没有选择要处理的报警记录！");
				rainPipeCode rp = new rainPipeCode();
				rp.pipeCode = "noneRain";
				strCodeList.add(rp);
				JSONTool.writeDataResult(strCodeList);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		for (AlarmLiquidRecord alarmLiquidRecordData : resLiquidObjArrayList) {
			liquidData = alarmLiquidRecordData.getLiquid();
			diameter = alarmLiquidRecordData.getDiameter();
			double fen = liquidData / diameter;
			if (fen > 0.9) {
				String dbCode = alarmLiquidRecordData.getDbcord();
				for (int i = 0; i < strCodeList.size(); i++) {
					String vidCode = strCodeList.get(i).pipeCode;
					if (vidCode.equals(dbCode)) {
						flag = true;
						break;
					}
				}
				if (flag == false) {
					rainPipeCode rp = new rainPipeCode();
					rp.pipeCode = alarmLiquidRecordData.getDbcord();
					timeNow = alarmLiquidRecordData.getUpTime();
					rp.t = alarmLiquidRecordData.getUpTime().toString();
					// rp.t = alarmLiquidRecordData.getUpTime();
					strCodeList.add(rp);
				}
				flag = false;
			}
		}
		try {
			JSONTool.writeDataResult(strCodeList);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// /////////////////////////////////////////////以上雨水监测部分/////////////////////////////////////////////

	public DeviceManager getDeviceManager() {
		return deviceManager;
	}

	@Resource
	public void setDeviceManager(DeviceManager deviceManager) {
		this.deviceManager = deviceManager;
	}

	public void prepare() throws Exception {
	}

	public void setModel(DeviceRealTimeDataForm model) {
		this.model = model;
	}

	public DeviceRealTimeDataForm getModel() {
		return this.model;
	}

	public DeviceTypeManager getDeviceTypeManager() {
		return deviceTypeManager;
	}

	@Resource
	public void setDeviceTypeManager(DeviceTypeManager deviceTypeManager) {
		this.deviceTypeManager = deviceTypeManager;
	}

	public SensorFlowRecordManager getSensorFlowRecordManage() {
		return sensorFlowRecordManage;
	}

	@Resource
	public void setSensorFlowRecordManage(
			SensorFlowRecordManager sensorFlowRecordManage) {
		this.sensorFlowRecordManage = sensorFlowRecordManage;
	}

	public SensorPressRecordManage getSensorPressRecordManage() {
		return sensorPressRecordManage;
	}

	@Resource
	public void setSensorPressRecordManage(
			SensorPressRecordManage sensorPressRecordManage) {
		this.sensorPressRecordManage = sensorPressRecordManage;
	}

	public SensorNoiseRecordManage getSensorNoiseRecordManage() {
		return sensorNoiseRecordManage;
	}

	@Resource
	public void setSensorNoiseRecordManage(
			SensorNoiseRecordManage sensorNoiseRecordManage) {
		this.sensorNoiseRecordManage = sensorNoiseRecordManage;
	}

	public SensorNoiseRecordV2Manage getSensorNoiseRecordV2Manage() {
		return sensorNoiseRecordV2Manage;
	}

	@Resource
	public void setSensorNoiseRecordV2Manage(
			SensorNoiseRecordV2Manage sensorNoiseRecordV2Manage) {
		this.sensorNoiseRecordV2Manage = sensorNoiseRecordV2Manage;
	}

	public AdDjLiquidManager getAdDjLiquidManager() {
		return adDjLiquidManager;
	}

	@Resource
	public void setAdDjLiquidManager(AdDjLiquidManager adDjLiquidManager) {
		this.adDjLiquidManager = adDjLiquidManager;
	}

	public AlarmLiquidRecordManager getAlarmLiquidRecordManager() {
		return alarmLiquidRecordManage;
	}

	@Resource
	public void setAlarmLiquidRecordManager(
			AlarmLiquidRecordManager alarmLiquidRecordManage) {
		this.alarmLiquidRecordManage = alarmLiquidRecordManage;
	}

}
