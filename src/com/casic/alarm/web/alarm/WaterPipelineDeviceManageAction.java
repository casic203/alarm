package com.casic.alarm.web.alarm;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.casic.alarm.JSON.AlarmRecordJSON;
import com.casic.alarm.JSON.DeviceJSON;
import com.casic.alarm.domain.AlarmRecord;
import com.casic.alarm.domain.DevPos;
import com.casic.alarm.domain.Device;
import com.casic.alarm.domain.PositionInfo;
import com.casic.alarm.form.DeviceForm;
import com.casic.alarm.manager.AlarmRecordManager;
import com.casic.alarm.manager.DevPosManage;
import com.casic.alarm.manager.DeviceManager;
import com.casic.alarm.manager.PositionInfoManage;
import com.casic.alarm.utils.Constants;
import com.casic.core.json.JSONTool;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

/**
 * 给水管线设备管理Action
 * @author liuxin
 *
 */
public class WaterPipelineDeviceManageAction implements ModelDriven<DeviceForm>, Preparable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7626964365328715702L;
	
	private DeviceForm model = new DeviceForm();
	
	private Long positionID;           // 监测点ID
	private String sensorType;         //传感器类型 选择输入 [{供水流量：Data3A}；{供水压力：Data40}；{供水噪声：Data02}；{排水液位：Data50}]
	private String pipeMaterial;       //管材 选择输入：球墨铸铁，铸铁，钢/镀锌，铜，钢筋混凝土/水泥，铅，铅银/铜合金，玻璃钢，陶瓷，PVC，PE
	private Integer pipeSize;       //管径  单位毫米
	private Double startTotalValue;    //起始累计流量 流量传感器必填
	private Double lowInstantValue;    //低值报警
	private Double highInstantValue;   //高值报警

	
	@Resource
	private DeviceManager deviceManager;
	
	@Resource
	private PositionInfoManage positionInfoManage;
	
	@Resource
	private DevPosManage devPosManage;
	
	@Resource
	private AlarmRecordManager alarmRecordManager;
	
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	public String execute() {
		return "success";
	}
	
	/**
	 * 增加设备
	 */
	public void addDevice() {
		/**
		 * 修改设备属性，设置埃德尔设备的属性
		 */
		Long deviceId = model.getId();
		Device loadDevice = deviceManager.load(deviceId);
		loadDevice.setNo(model.getNo());
		loadDevice.setInstallPosition(model.getInstallPosition());
		loadDevice.setSimid(model.getSimid());
		loadDevice.setBeginUseTime(model.getBeginUseTime());
		deviceManager.save(loadDevice);
		
		/**
		 * 加载监测点实体
		 */
		 PositionInfo positionInfo = positionInfoManage.load(positionID);
		 
		 /**
		  * 关联监测点和设备
		  */
		 DevPos devPos = new DevPos();
		 devPos.setDevice(loadDevice);
		 devPos.setPositionInfo(positionInfo);
		 devPos.setSensorType(sensorType);
		 devPos.setPipeMaterial(pipeMaterial);
		 devPos.setPipeSize(pipeSize);
		 devPos.setStartTotalValue(startTotalValue);
		 devPos.setLowInstantValue(lowInstantValue);
		 devPos.setHighInstantValue(highInstantValue);
		 devPosManage.save(devPos);
		 
		 try {
			JSONTool.writeMsgResult(true, "添加成功！");
		 } catch (IOException e) {
			e.printStackTrace();
		 }
	}
	
	/**
	 * 删除设备
	 */
	public void deleteDevice() {
		Long localPositionID = positionID;
		Long deviceId = model.getId();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("posId", localPositionID);
		paramMap.put("deviceId", deviceId);

		List<DevPos> devPosList = devPosManage.find("from DevPos devPos where devPos.device.id=:deviceId and devPos.positionInfo.ID=:posId", paramMap);
		for (DevPos devPos : devPosList) {
			devPosManage.remove(devPos);
			break;
		}
		
//		Device loadDevice = deviceManager.get(deviceId);
//		loadDevice.setActive(false);
//		deviceManager.save(loadDevice);
		try {
			JSONTool.writeMsgResult(true, "删除成功！");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询给水管线报警记录
	 */
	public void queryDeviceAlarmRecord() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<AlarmRecordJSON> rowList = new ArrayList<AlarmRecordJSON>();
		String hql = "select alarmRecord from AlarmRecord alarmRecord where alarmRecord.deviceTypeName in :devTypeNameArray and alarmRecord.active=:active";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		
		paramMap.put("devTypeNameArray", Constants.LEAKAGE_DEVICE_COLLECTION);
		paramMap.put("active", Boolean.TRUE);
		List<AlarmRecord> alarmRecordList = alarmRecordManager.find(hql, paramMap);
		resultMap.put("total", alarmRecordList.size());
		for (AlarmRecord alarmRecord : alarmRecordList) {
			AlarmRecordJSON alarmRecordJSON = new AlarmRecordJSON();
			alarmRecordJSON.setCode(alarmRecord.getDeviceCode());
			alarmRecordJSON.setDevice(alarmRecord.getDevice().getDevName()); 
			alarmRecordJSON.setItemName(alarmRecord.getItemName());
			alarmRecordJSON.setItemValue(alarmRecord.getItemValue());
			try{
				alarmRecordJSON.setRecordDate(simpleDateFormat.format(alarmRecord.getRecordDate()));
			}catch(Exception e){
				alarmRecordJSON.setRecordDate("");
			}
			alarmRecordJSON.setMessage(alarmRecord.getMessage());
			rowList.add(alarmRecordJSON);
		}
		resultMap.put("rows", rowList);
		
		try {
			JSONTool.writeDataResult(resultMap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 查询所有给水管线设备
	 */
	public void queryDevice() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<DeviceJSON> deviceJSONList = new ArrayList<DeviceJSON>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String hql = "select device from Device device where device.active=:active and device.deviceType.typeName in :typeNameArray";
		
		paramMap.put("active", Boolean.TRUE);
		paramMap.put("typeNameArray", Constants.LEAKAGE_DEVICE_COLLECTION);
		List<Device> deviceList = deviceManager.find(hql, paramMap);
		for (Device device : deviceList) {
			DeviceJSON deviceJSON = new DeviceJSON(device);
			deviceJSONList.add(deviceJSON);
		}
		resultMap.put("total", deviceJSONList.size());
		resultMap.put("rows", deviceJSONList);
		
		try {
			JSONTool.writeDataResult(resultMap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public DeviceManager getDeviceManager() {
		return deviceManager;
	}

	public void setDeviceManager(DeviceManager deviceManager) {
		this.deviceManager = deviceManager;
	}

	public void prepare() throws Exception {
		
	}

	public DeviceForm getModel() {
		return this.model;
	}

	public Long getPositionID() {
		return positionID;
	}

	public void setPositionID(Long positionID) {
		this.positionID = positionID;
	}

	public PositionInfoManage getPositionInfoManage() {
		return positionInfoManage;
	}

	public void setPositionInfoManage(PositionInfoManage positionInfoManage) {
		this.positionInfoManage = positionInfoManage;
	}
	
	public String getSensorType() {
		return sensorType;
	}

	public void setSensorType(String sensorType) {
		this.sensorType = sensorType;
	}

	public String getPipeMaterial() {
		return pipeMaterial;
	}

	public void setPipeMaterial(String pipeMaterial) {
		this.pipeMaterial = pipeMaterial;
	}

	public int getPipeSize() {
		return pipeSize;
	}

	public void setPipeSize(int pipeSize) {
		this.pipeSize = pipeSize;
	}

	public Double getStartTotalValue() {
		return startTotalValue;
	}

	public void setStartTotalValue(Double startTotalValue) {
		this.startTotalValue = startTotalValue;
	}

	public Double getLowInstantValue() {
		return lowInstantValue;
	}

	public void setLowInstantValue(Double lowInstantValue) {
		this.lowInstantValue = lowInstantValue;
	}

	public Double getHighInstantValue() {
		return highInstantValue;
	}

	public void setHighInstantValue(Double highInstantValue) {
		this.highInstantValue = highInstantValue;
	}

	public DevPosManage getDevPosManage() {
		return devPosManage;
	}

	public void setDevPosManage(DevPosManage devPosManage) {
		this.devPosManage = devPosManage;
	}

	public AlarmRecordManager getAlarmRecordManager() {
		return alarmRecordManager;
	}

	public void setAlarmRecordManager(AlarmRecordManager alarmRecordManager) {
		this.alarmRecordManager = alarmRecordManager;
	}
}
