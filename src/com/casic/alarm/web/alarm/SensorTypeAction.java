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
import com.casic.alarm.JSON.SensorTypeJSON;
import com.casic.alarm.domain.AcceptPerson; 
import com.casic.alarm.domain.DeviceSensorId;
import com.casic.alarm.domain.DeviceSensorformat;
import com.casic.alarm.domain.DeviceSensorformatId;
import com.casic.alarm.domain.DeviceType;
import com.casic.alarm.domain.SensorType;
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
	@org.apache.struts2.convention.annotation.Result(name = "reload", location = "sensor-type.do", type = "redirect"),
	@org.apache.struts2.convention.annotation.Result(name = "json", type = "json", params = {
			"root", "jsonResults" }) }) 
public class SensorTypeAction implements ModelDriven<SensorType>, Preparable{

	private long id;
	private String sensorcode; 
	private int page;
	private int rows;
 
	private SensorType model;
	private Assertion objUser;
	private SensorTypeManager stmanager;   
	private SysLogManager logManager;
	
	public void prepare() throws Exception {
		this.model = new SensorType();
		objUser = (Assertion) ActionContext.getContext().getSession().get("_const_cas_assertion_");
	}

	public SensorType getModel() {
		return this.model;
	}
	
	public String execute() {
		return "success";
	} 

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
 
	public String getSensorcode() {
		return sensorcode;
	}

	public void setSensorcode(String sensorcode) {
		this.sensorcode = sensorcode;
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
 
	@Resource
	public void setStmanager(SensorTypeManager stmanager) {
		this.stmanager = stmanager;
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
			 
			hqlTotal.append("select count(*) from SensorType as dp where 1=1");
			hqlParam.append("from SensorType as dp where 1=1");
		 
			if(model.getIsuse()==null&&model.getSensorcode()==null&&model.getSensorname()==null){
				hqlTotal.append(" and dp.isuse=1");
				hqlParam.append(" and dp.isuse=1");
				
			}
			if(model.getIsuse()!=null){
				hqlTotal.append(" and dp.isuse =:isuse");
				hqlParam.append(" and dp.isuse =:isuse");  
				map.put("isuse", model.getIsuse());    
			} 
		 
			int total=stmanager.getCount(hqlTotal.toString(), map);
			List<SensorType> dpList = (List<SensorType>)stmanager.pagedQuery(hqlParam.toString(), page,rows,map).getResult(); 
			
			List<SensorTypeJSON> pjsons=new ArrayList<SensorTypeJSON>();
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
			String hql = "from SensorType d where d.isuse = 1";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", id);
			List<SensorType> deviceSensors = stmanager.find(hql, map);
			List<SensorTypeJSON> jsons = new ArrayList<SensorTypeJSON>();
			for (SensorType deviceSensor : deviceSensors) {
				SensorTypeJSON json = new SensorTypeJSON();
				 
				json.setSensorcode(deviceSensor.getSensorcode());
				json.setSensorname(deviceSensor.getSensorname());
				jsons.add(json);
			}
			JSONTool.writeDataResult(jsons);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void objToDPJSON(List<SensorType> sensortypes,
			List<SensorTypeJSON> jsons) {  
		for (SensorType t : sensortypes) {
			SensorTypeJSON json = new SensorTypeJSON();  
			json.setSensorcode(t.getSensorcode());
			json.setSensorname(t.getSensorname()); 
			json.setIsuse(t.getIsuse());
			json.setDefaultid(t.getDefaultid());  
			
			jsons.add(json);
		}
	}

	@SuppressWarnings("unchecked")
	public void save() throws IOException {
		try {
			String hql ="from SensorType as st where 1=1 and st.sensorcode='"+sensorcode+"'";
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("sensorcode", sensorcode); 
			
			page = page <= 0 ? 1 : page;
			rows = rows <= 0 ? 10 : rows;
			
			List<SensorType> sensorTypes = (List<SensorType>)stmanager.pagedQuery(hql, page,rows,map).getResult(); 
			if (null != sensorTypes && sensorTypes.size()>0) {
				JSONTool.writeMsgResult(false, "该数据已存在，不能录入！");
			} else {  
				stmanager.save(model);
				
				SensorTypeJSON json=new SensorTypeJSON();  
				json.setSensorcode(model.getSensorcode());
				json.setSensorname(model.getSensorname()); 
				json.setDefaultid(model.getDefaultid());
				json.setIsuse(model.getIsuse());
				
				SysLog log=new SysLog();
				log.setBusinessName("传感器类型维护");
				log.setContent("新增传感器类型："+model.getSensorcode()+"-"+model.getSensorname());
				log.setOperationType("add");
				log.setCreateUser(objUser.getPrincipal().getName());
				log.setCreateTime(new Date());
				logManager.save(log);
				  
				map.put("success", true);
				map.put("msg", "保存完成！");
				map.put("dev", json);

				JSONTool.writeMsgResult(true, "保存完成");
			}
		} catch (Exception e) {
			e.printStackTrace();
			JSONTool.writeMsgResult(false, "保存失败！");
		}
	}
	public void edit() throws IOException {
		try {
			SensorType deviceSensor = new SensorType();
			String hql ="from SensorType as ds where 1=1 and ds.sensorcode='"+sensorcode+"'";
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("sensorcode", sensorcode); 
			
			page = page <= 0 ? 1 : page;
			rows = rows <= 0 ? 10 : rows;
			
			List<SensorType> deviceSensors = (List<SensorType>)stmanager.pagedQuery(hql, page,rows,map).getResult();  
			for(SensorType ds:deviceSensors){
				deviceSensor  = ds;
			} 
			  
			deviceSensor.setSensorname(model.getSensorname());
			deviceSensor.setIsuse(model.getIsuse());
			deviceSensor.setDefaultid(model.getDefaultid());
			 
			stmanager.save(deviceSensor);
			
			SysLog log=new SysLog();
			log.setBusinessName("传感器类型维护");
			log.setContent("修改传感器类型："+model.getSensorcode()+"-"+model.getSensorname());
			log.setOperationType("add");
			log.setCreateUser(objUser.getPrincipal().getName());
			log.setCreateTime(new Date());
			logManager.save(log);
			
			JSONTool.writeMsgResult(true, "编辑成功！");
		} catch (Exception e) {
			e.printStackTrace();
			JSONTool.writeMsgResult(false, "编辑失败！");
		}
	}
	
	public void delete() throws IOException {
		try { 
			SensorType deviceSensor = new SensorType();
			String hql ="from SensorType as ds where 1=1 and ds.sensorcode='"+sensorcode+"'";
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("sensorcode", sensorcode); 
			
			page = page <= 0 ? 1 : page;
			rows = rows <= 0 ? 10 : rows;
			
			List<SensorType> deviceSensors = (List<SensorType>)stmanager.pagedQuery(hql, page,rows,map).getResult();  
			for(SensorType ds:deviceSensors){
				deviceSensor  = ds;
			} 
			 
			deviceSensor.setIsuse(false);
			stmanager.save(deviceSensor);
			
			SysLog log=new SysLog();
			log.setBusinessName("传感器类型维护");
			log.setContent("删除传感器类型："+model.getSensorcode()+"-"+model.getSensorname());
			log.setOperationType("delete");
			log.setCreateUser(objUser.getPrincipal().getName());
			log.setCreateTime(new Date());
			logManager.save(log);
			
			
			JSONTool.writeMsgResult(true, "删除完成！");
			  
		} catch (Exception e) {
			e.printStackTrace();
			JSONTool.writeMsgResult(false, "删除失败！");
		}
	}
}
