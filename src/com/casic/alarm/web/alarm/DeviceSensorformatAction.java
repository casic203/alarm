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
  
import com.casic.alarm.JSON.DeviceSensorformatJSON;  
import com.casic.alarm.domain.DeviceSensor;
import com.casic.alarm.domain.DeviceSensorformat;
import com.casic.alarm.domain.DeviceSensorformatId;
import com.casic.alarm.domain.SysLog;
import com.casic.alarm.manager.DeviceSensorManager;
import com.casic.alarm.manager.DeviceSensorformatManager;
import com.casic.alarm.manager.SysLogManager;
import com.casic.core.json.JSONTool;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
@Results({
	@org.apache.struts2.convention.annotation.Result(name = "reload", location = "device-sensorformat.do", type = "redirect"),
	@org.apache.struts2.convention.annotation.Result(name = "json", type = "json", params = {
			"root", "jsonResults" }) }) 
public class DeviceSensorformatAction implements ModelDriven<DeviceSensorformat>, Preparable{
	private long id;
	private String sensortype;
	private String itemname;
	private int page;
	private int rows;

	private DeviceSensorformat model;
	private Assertion objUser;
	private SysLogManager logManager;
	private DeviceSensorformatManager dsfmanager;
	private DeviceSensor deviceSensor;
	private DeviceSensorformatId deviceSensorformatid;
	
	public void prepare() throws Exception {
		this.model = new DeviceSensorformat();
		objUser = (Assertion) ActionContext.getContext().getSession().get("_const_cas_assertion_");
	}

	public DeviceSensorformat getModel() {
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
  
	public String getSensortype() {
		return sensortype;
	}

	public void setSensortype(String sensortype) {
		this.sensortype = sensortype;
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
	
	public DeviceSensor getDeviceSensor() {
		return deviceSensor;
	}

	public void setDeviceSensor(DeviceSensor deviceSensor) {
		this.deviceSensor = deviceSensor;
	}

	public String getItemname() {
		return itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}

	public DeviceSensorformatId getDeviceSensorformatid() {
		return deviceSensorformatid;
	}

	public void setDeviceSensorformatid(DeviceSensorformatId deviceSensorformatid) {
		this.deviceSensorformatid = deviceSensorformatid;
	}

	@Resource
	public void setDsfmanager(DeviceSensorformatManager dsfmanager) {
		this.dsfmanager = dsfmanager;
	}
	@Resource
	public void setDsmanager(DeviceSensorManager dsmanager) {
	}

	@Resource
	public void setLogManager(SysLogManager logManager) {
		this.logManager = logManager;
	}

	@SuppressWarnings("unchecked")
	public void query() throws IOException {
		try {  
			StringBuilder hqlTotal=new StringBuilder();
			StringBuilder hqlParam = new StringBuilder(); 
			Map<String, Object>map=new HashMap<String, Object>();
			 
			hqlTotal.append("select count(*) from DeviceSensorformat as dsf where 1=1");
			hqlParam.append("from DeviceSensorformat as dsf where 1=1");
			 
			if(sensortype!=null){
				hqlTotal.append(" and dsf.dsfid.sensortype=:sensortype");
				hqlParam.append(" and dsf.dsfid.sensortype=:sensortype");
				map.put("sensortype", sensortype);
			}
			if(itemname!=null){
				hqlTotal.append(" and dsf.dsfid.itemname =:itemname");
				hqlParam.append(" and dsf.dsfid.itemname =:itemname");
				map.put("itemname", itemname);
			}
			
			hqlTotal.append(" order by dsf.sortid");
			hqlParam.append(" order by dsf.sortid");
			
			int total=dsfmanager.getCount(hqlTotal.toString(), map);
			List<DeviceSensorformat> dfsList = (List<DeviceSensorformat>)dsfmanager.pagedQuery(hqlParam.toString(), page,rows,map).getResult(); 
			
			List<DeviceSensorformatJSON> pjsons=new ArrayList<DeviceSensorformatJSON>();
			objToDFSAllJSON(dfsList,pjsons);
			
			map.clear(); 
			map.put("total", total);
			map.put("rows", pjsons);
			
			JSONTool.writeDataResult(map);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void objToDFSAllJSON(List<DeviceSensorformat> dfsList,
			List<DeviceSensorformatJSON> pjsons) {
		for(DeviceSensorformat dsf:dfsList){
			DeviceSensorformatJSON dsfjson = new DeviceSensorformatJSON();
			dsfjson.setSensortype(dsf.getDsfid().getSensortype());
			dsfjson.setItemname(dsf.getDsfid().getItemname());  
			dsfjson.setSortid(dsf.getSortid());
			dsfjson.setItemnameCn(dsf.getItemnameCn());
			 
			dsfjson.setBeginpos(dsf.getBeginpos());
			dsfjson.setItemlen(dsf.getItemlen());
			dsfjson.setIsdatacol(dsf.getIsdatacol());
			dsfjson.setIsmulitdata(dsf.getIsmulitdata());
			dsfjson.setIsdefaultcol(dsf.getIsdefaultcol());
			dsfjson.setIssecucol(dsf.getIssecucol());
			dsfjson.setStandardcol(dsf.getStandardcol());
			dsfjson.setColdatatype(dsf.getColdatatype());
			dsfjson.setCollen(dsf.getCollen());
			dsfjson.setEscapecol(dsf.getEscapecol());
			dsfjson.setIsshow(dsf.getIsshow());
			dsfjson.setInterfacevaluepx(dsf.getInterfacevaluepx());
			dsfjson.setIsshow2(dsf.getIsshow2());
			dsfjson.setItemvalue(dsf.getItemvalue());
			 
			pjsons.add(dsfjson);
		}
	}
	@SuppressWarnings("unchecked")
	public void save() throws IOException {
		try {  
			String hql ="from DeviceSensorformat as dsf where 1=1 and dsf.dsfid.sensortype='"+sensortype+"' and dsf.dsfid.itemname='"+itemname+"'";
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("sensortype", sensortype);
			map.put("itemname", itemname);
			
			page = page <= 0 ? 1 : page;
			rows = rows <= 0 ? 10 : rows;
			
			List<DeviceSensorformat> deviceSensorformats = (List<DeviceSensorformat>)dsfmanager.pagedQuery(hql, page,rows,map).getResult(); 
			  
			if (null != deviceSensorformats && deviceSensorformats.size()>0) {
				JSONTool.writeMsgResult(false, "该数据帧已存在，不能录入！");
			} else { 
				deviceSensorformatid = new DeviceSensorformatId(sensortype,itemname);
				model.setDsfid(deviceSensorformatid);
				dsfmanager.save(model);
				
				DeviceSensorformatJSON json=new DeviceSensorformatJSON();
				json.setSensortype(sensortype);
				json.setItemname(itemname);  
				json.setSortid(model.getSortid());
				json.setItemnameCn(model.getItemnameCn());
				json.setItemvalue(model.getItemvalue());
				 
				json.setBeginpos(model.getBeginpos());
				json.setItemlen(model.getItemlen());
				json.setIsdatacol(model.getIsdatacol());
				json.setIsmulitdata(model.getIsmulitdata());
				json.setIsdefaultcol(model.getIsdefaultcol());
				json.setIssecucol(model.getIssecucol());
				json.setStandardcol(model.getStandardcol());
				json.setColdatatype(model.getColdatatype());
				json.setCollen(model.getCollen());
				json.setEscapecol(model.getEscapecol());
				json.setIsshow(model.getIsshow());
				json.setInterfacevaluepx(model.getInterfacevaluepx());
				json.setIsshow2(model.getIsshow2());
				  
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
			log.setBusinessName("设备传感器格式维护");
			log.setContent("新增传感器格式："+model.getDsfid()+"-"+model.getStandardcol());
			log.setOperationType("add");
			log.setCreateUser(objUser.getPrincipal().getName());
			log.setCreateTime(new Date());
			logManager.save(log);
		}
	}
	@SuppressWarnings("unchecked")
	public void edit() throws IOException {
		try {
			DeviceSensorformat deviceSensorformat = new DeviceSensorformat();
			String hql ="from DeviceSensorformat as dsf where 1=1 and dsf.dsfid.sensortype='"+sensortype+"' and dsf.dsfid.itemname='"+itemname+"'";
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("sensortype",sensortype);
			map.put("itemname", itemname);

			page = page <= 0 ? 1 : page;
			rows = rows <= 0 ? 10 : rows;
			
			List<DeviceSensorformat> deviceSensorformats = (List<DeviceSensorformat>)dsfmanager.pagedQuery(hql, page,rows,map).getResult(); 
			for(DeviceSensorformat dsf:deviceSensorformats){
				deviceSensorformat  = dsf;
			} 
			deviceSensorformat.setItemvalue(model.getItemvalue());
			deviceSensorformat.setSortid(model.getSortid());
			 
			dsfmanager.save(deviceSensorformat);
			JSONTool.writeMsgResult(true, "编辑成功！");
		}catch (Exception e) {
			e.printStackTrace();
			JSONTool.writeMsgResult(false, "编辑失败！");
		}finally{
			SysLog log=new SysLog();
			log.setBusinessName("设备传感器格式维护");
			log.setContent("修改传感器格式："+model.getDsfid()+"-"+model.getStandardcol());
			log.setOperationType("edit");
			log.setCreateUser(objUser.getPrincipal().getName());
			log.setCreateTime(new Date());
			logManager.save(log);
		}
	}
	@SuppressWarnings("unchecked")
	public void delete() throws IOException {
		try {   
			DeviceSensorformat deviceSensorformat = new DeviceSensorformat();
			String hql ="from DeviceSensorformat as dsf where 1=1 and dsf.dsfid.sensortype='"+sensortype+"' and dsf.dsfid.itemname='"+itemname+"'";
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("sensortype", sensortype);
			map.put("itemname", itemname);

			page = page <= 0 ? 1 : page;
			rows = rows <= 0 ? 10 : rows;
			
			List<DeviceSensorformat> deviceSensorformats = (List<DeviceSensorformat>)dsfmanager.pagedQuery(hql, page,rows,map).getResult(); 
			for(DeviceSensorformat dsf:deviceSensorformats){
				deviceSensorformat  = dsf;
			} 
			
			dsfmanager.remove(deviceSensorformat);
			JSONTool.writeMsgResult(true, "删除完成！");
			  
		} catch (Exception e) {
			e.printStackTrace();
			JSONTool.writeMsgResult(false, "删除失败！");
		}finally{
			SysLog log=new SysLog();
			log.setBusinessName("设备传感器格式维护");
			log.setContent("删除传感器格式："+model.getDsfid()+"-"+model.getStandardcol());
			log.setOperationType("delete");
			log.setCreateUser(objUser.getPrincipal().getName());
			log.setCreateTime(new Date());
			logManager.save(log);
		}
	}

}
