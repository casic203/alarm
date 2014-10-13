package com.casic.alarm.web.alarm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Results;
import org.jasig.cas.client.validation.Assertion;
  
import com.casic.alarm.JSON.AcceptPersonJSON;
import com.casic.alarm.JSON.DeviceJSON;
import com.casic.alarm.JSON.DeviceSensorJSON;
import com.casic.alarm.JSON.DeviceTypeJSON;
import com.casic.alarm.domain.AcceptPerson;
import com.casic.alarm.domain.Device;
import com.casic.alarm.domain.DeviceSensor;  
import com.casic.alarm.domain.DeviceSensorId;
import com.casic.alarm.domain.DeviceSensorformat;
import com.casic.alarm.domain.DeviceSensorformatId;
import com.casic.alarm.domain.DeviceType;
import com.casic.alarm.domain.SysLog;
import com.casic.alarm.manager.DeviceManager;
import com.casic.alarm.manager.DeviceSensorManager; 
import com.casic.alarm.manager.DeviceTypeManager;
import com.casic.alarm.manager.SensorTypeManager;
import com.casic.alarm.manager.SysLogManager;
import com.casic.core.json.JSONTool;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
@Results({
	@org.apache.struts2.convention.annotation.Result(name = "reload", location = "device-sensor.do", type = "redirect"),
	@org.apache.struts2.convention.annotation.Result(name = "json", type = "json", params = {
			"root", "jsonResults" }) }) 
public class DeviceSensorAction implements ModelDriven<DeviceSensor>, Preparable{
 
	private String sensorcode;
	private Long deviceid = 0L;
	private String sensorid;
	private int page;
	private int rows;
 
	private DeviceSensor model;
	private Assertion objUser;

	private DeviceSensorManager dpmanager; 
	private SysLogManager logManager;
	
	private Device device;
	private DeviceManager deviceManager;
	private SensorTypeManager sensorManager;
	private DeviceSensorId deviceSensorid;
	
	
	public void prepare() throws Exception {
		// TODO Auto-generated method stub
		this.model = new DeviceSensor();
		objUser = (Assertion) ActionContext.getContext().getSession().get("_const_cas_assertion_");
	}

	public DeviceSensor getModel() {
		// TODO Auto-generated method stub
		return this.model;
	}
	
	public String execute() {
		return "success";
	} 
  
	public String getSensorcode() {
		return sensorcode;
	}

	public void setSensorcode(String sensorcode) {
		this.sensorcode = sensorcode;
	}

	public String getSensorid() {
		return sensorid;
	}

	public void setSensorid(String sensorid) {
		this.sensorid = sensorid;
	}

	public Long getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(Long deviceid) {
		this.deviceid = deviceid;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	} 

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	@Resource
	public void setDpmanager(DeviceSensorManager dpmanager) {
		this.dpmanager = dpmanager;
	}   

	@Resource
	public void setSensorManager(SensorTypeManager sensorTypeManager) {
		this.sensorManager = sensorTypeManager;
	}
	
	@Resource 
	public void setLogManager(SysLogManager logManager) {
		this.logManager = logManager;
	}

	public void query() throws IOException {
		try {  
			StringBuilder hqlTotal=new StringBuilder();
			StringBuilder hqlParam = new StringBuilder(); 
			Map<String, Object>map=new HashMap<String, Object>();
			 
			hqlTotal.append("select count(*) from DeviceSensor as dp where 1=1");
			hqlParam.append("from DeviceSensor as dp where 1=1");
			//hqlParam.append("select dt.typeCode,dt.typeName,dp.paramcode,dp.paramname from DeviceParameter as dp,DeviceType as dt where 1=1 and dp.typecode = dt.typeCode");
		   
			if (deviceid>0&&deviceid!=null) {
				hqlTotal.append(" and dp.id.deviceid=:did");
				hqlParam.append(" and dp.id.deviceid=:did");
				map.put("did", deviceid);
			}
			int total=dpmanager.getCount(hqlTotal.toString(), map);
			List<DeviceSensor> dpList = (List<DeviceSensor>)dpmanager.pagedQuery(hqlParam.toString(), page,rows,map).getResult(); 
			
			List<DeviceSensorJSON> pjsons=new ArrayList<DeviceSensorJSON>();
			objToDPJSON(dpList,pjsons);
			
			map.clear(); 
			map.put("total", total);
			map.put("rows", pjsons);
			
			JSONTool.writeDataResult(map);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}   
	public void querySensorCode() throws IOException {
		try {
			String hql = "from DeviceSensor d where d.isuse = 1";
			Map<String, Object> map = new HashMap<String, Object>(); 
			List<DeviceSensor> deviceSensors = dpmanager.find(hql, map);
			List<DeviceSensorJSON> jsons = new ArrayList<DeviceSensorJSON>();
			for (DeviceSensor deviceSensor : deviceSensors) {
				DeviceSensorJSON json = new DeviceSensorJSON();
				 
				json.setSensorname(deviceSensor.getSensorType().getSensorname());
				json.setSensorcode(deviceSensor.getId().getSensorcode()); 
				jsons.add(json);
			}
			JSONTool.writeDataResult(jsons);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void querySensorId() throws IOException {
		try {
			String hql = "from DeviceSensor d ";
			Map<String, Object> map = new HashMap<String, Object>(); 
			List<DeviceSensor> deviceSensors = dpmanager.find(hql, map);
			List<DeviceSensorJSON> jsons = new ArrayList<DeviceSensorJSON>();
			for (DeviceSensor deviceSensor : deviceSensors) {
				DeviceSensorJSON json = new DeviceSensorJSON();
				 
				json.setSensorname(deviceSensor.getId().getSensorid());
				json.setSensorid(deviceSensor.getId().getSensorid()); 
				jsons.add(json);
			}
			JSONTool.writeDataResult(jsons);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void objToDPJSON(List<DeviceSensor> deviceSensors,
			List<DeviceSensorJSON> jsons) {  
		for (DeviceSensor t : deviceSensors) {
			DeviceSensorJSON json = new DeviceSensorJSON();  
			json.setSensorcode(t.getId().getSensorcode());  
			json.setDeviceid(t.getDevice().getId());
			json.setSensorid(t.getId().getSensorid());
			
			json.setDevname(t.getDevice().getDevName());
			json.setSensorname(t.getSensorType().getSensorname());
			
			jsons.add(json);
		}
	}
 
	public void save() throws IOException {
		try {
			String hql ="from DeviceSensor as ds where 1=1 and ds.id.sensorcode='"+sensorcode+"' and ds.id.deviceid="+deviceid
					+" and ds.id.sensorid='"+sensorid+"'";
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("sensorcode", sensorcode);
			map.put("sensorid",sensorid);
			map.put("deviceid",deviceid);
			
			page = page <= 0 ? 1 : page;
			rows = rows <= 0 ? 10 : rows;
			
			List<DeviceSensor> deviceSensors = (List<DeviceSensor>)dpmanager.pagedQuery(hql, page,rows,map).getResult(); 
			if (null != deviceSensors && deviceSensors.size()>0) {
				JSONTool.writeMsgResult(false, "该数据已存在，不能录入！");
			} else { 
				deviceSensorid = new DeviceSensorId(sensorcode,sensorid,deviceid);
				model.setId(deviceSensorid);
				dpmanager.save(model);
				
				DeviceSensorJSON json=new DeviceSensorJSON();
				json.setDeviceid(model.getId().getDeviceid());
				json.setSensorid(model.getId().getSensorid());
				json.setSensorcode(model.getId().getSensorcode());   
				  
				map.put("success", true);
				map.put("msg", "保存完成！");
				map.put("dev", json);

				JSONTool.writeMsgResult(true, "保存完成");
			}
		} catch (Exception e) {
			e.printStackTrace();
			JSONTool.writeMsgResult(false, "保存失败！");
		}finally{
			SysLog log=new SysLog();
			log.setBusinessName("设备传感器维护");
			log.setContent("新增传感器："+model.getDevice().getDevCode()+"-"+model.getSensorType().getSensorname());
			log.setOperationType("add");
			log.setCreateUser(objUser.getPrincipal().getName());
			log.setCreateTime(new Date());
			logManager.save(log);
		}
//	 
	}
	public void edit() throws IOException {
		try {
			DeviceSensor deviceSensor = new DeviceSensor(); 
			DeviceSensorId dsi=null;
			String hql ="from DeviceSensor as ds where 1=1 and ds.id.sensorcode='"+sensorcode+"' and ds.id.deviceid="+deviceid;
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("sensorcode", sensorcode);
			//map.put("sensorid",sensorid);
			map.put("deviceid",deviceid); 
			
			page = page <= 0 ? 1 : page;
			rows = rows <= 0 ? 10 : rows;
			
			List<DeviceSensor> deviceSensors = (List<DeviceSensor>)dpmanager.pagedQuery(hql, page,rows,map).getResult();  
			for(DeviceSensor ds:deviceSensors){
				deviceSensor  = ds;
			}   
			dsi=new DeviceSensorId(sensorcode,sensorid,deviceid);  
			deviceSensor.setId(dsi);
			
			dpmanager.save(deviceSensor);
			JSONTool.writeMsgResult(true, "编辑成功！");
		} catch (Exception e) {
			e.printStackTrace();
			JSONTool.writeMsgResult(false, "编辑失败！");
		}finally{
			SysLog log=new SysLog();
			log.setBusinessName("设备传感器维护");
			log.setContent("修改传感器："+model.getDevice().getDevCode()+"-"+model.getSensorType().getSensorname());
			log.setOperationType("edit");
			log.setCreateUser(objUser.getPrincipal().getName());
			log.setCreateTime(new Date());
			logManager.save(log);
		}
	}
	
	public void delete() throws IOException {
		try { 
			DeviceSensor deviceSensor = new DeviceSensor(); 
			String hql ="from DeviceSensor as ds where 1=1 and ds.id.sensorcode='"+sensorcode+"' and ds.id.deviceid="+deviceid
					+" and ds.id.sensorid='"+sensorid+"'";
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("sensorcode", sensorcode);
			map.put("sensorid",sensorid);
			map.put("deviceid",deviceid);
			
			page = page <= 0 ? 1 : page;
			rows = rows <= 0 ? 10 : rows;
			
			List<DeviceSensor> deviceSensors = (List<DeviceSensor>)dpmanager.pagedQuery(hql, page,rows,map).getResult();  
			for(DeviceSensor ds:deviceSensors){
				deviceSensor  = ds;
			} 
			  
			dpmanager.remove(deviceSensor);
			JSONTool.writeMsgResult(true, "删除完成！");
			  
		} catch (Exception e) {
			e.printStackTrace();
			JSONTool.writeMsgResult(false, "删除失败！");
		}finally{
			SysLog log=new SysLog();
			log.setBusinessName("设备传感器维护");
			log.setContent("删除传感器："+model.getDevice().getDevCode()+"-"+model.getSensorType().getSensorname());
			log.setOperationType("delete");
			log.setCreateUser(objUser.getPrincipal().getName());
			log.setCreateTime(new Date());
			logManager.save(log);
		}
	}
}
