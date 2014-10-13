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

import com.casic.alarm.JSON.DeviceSensorJSON;
import com.casic.alarm.JSON.DeviceSpropertyJSON; 
import com.casic.alarm.domain.DeviceSensor;
import com.casic.alarm.domain.DeviceSproperty;
import com.casic.alarm.domain.SysLog;
import com.casic.alarm.manager.DeviceSpropertyManager; 
import com.casic.alarm.manager.SysLogManager;
import com.casic.core.json.JSONTool;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
@Results({
	@org.apache.struts2.convention.annotation.Result(name = "reload", location = "device-sproperty.do", type = "redirect"),
	@org.apache.struts2.convention.annotation.Result(name = "json", type = "json", params = {
			"root", "jsonResults" }) })
public class DeviceSpropertyAction  implements ModelDriven<DeviceSproperty>, Preparable {
	private long id;
	private int page;
	private int rows;
 
	private DeviceSproperty model;
	private Assertion objUser;

	private DeviceSpropertyManager dpcmanager; 
	private SysLogManager logManager;

	
	public void prepare() throws Exception {
		this.model = new DeviceSproperty();
		objUser = (Assertion) ActionContext.getContext().getSession().get("_const_cas_assertion_");
	}

	public DeviceSproperty getModel() {
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
	public void setDpcmanager(DeviceSpropertyManager dpcmanager) {
		this.dpcmanager = dpcmanager;
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
			 
			hqlTotal.append("select count(*) from DeviceSproperty as dpc where 1=1");
			hqlParam.append("from DeviceSproperty as dpc where 1=1"); 
			
			if(model.getColdatatype()==null&&model.getCollen()==null&&model.getDatausetype()==null&&model.getStandardcol()==null
					&&model.getStandardcolname()==null&&model.getStatus()==null){
				hqlTotal.append(" and dpc.status=1");
				hqlParam.append(" and dpc.status=1"); 
			}
			if(model.getStatus()!=null){
				hqlTotal.append(" and dpc.status =:status");
				hqlParam.append(" and dpc.status =:status");
				map.put("status", model.getStatus());
			}
			int total=dpcmanager.getCount(hqlTotal.toString(), map);
			List<DeviceSproperty> dscList = (List<DeviceSproperty>)dpcmanager.pagedQuery(hqlParam.toString(), page,rows,map).getResult(); 
			
			List<DeviceSpropertyJSON> dscjsons=new ArrayList<DeviceSpropertyJSON>();
			objToDPCJSON(dscList,dscjsons);
			
			map.clear(); 
			map.put("total", total);
			map.put("rows", dscjsons);
			
			JSONTool.writeDataResult(map);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}  
	
	private void objToDPCJSON(List<DeviceSproperty> deviceSproperty,
			List<DeviceSpropertyJSON> jsons) {  
		for (DeviceSproperty t : deviceSproperty) {
			DeviceSpropertyJSON json = new DeviceSpropertyJSON();
			json.setId(t.getId());
			json.setStandardcol(t.getStandardcol());
			json.setStandardcolname(t.getStandardcolname());
			json.setDatausetype(t.getDatausetype());
			json.setCollen(t.getCollen());
			json.setColdatatype(t.getColdatatype());
			json.setStatus(t.getStatus());
			  
			jsons.add(json);
		}
	}
	public void save() throws IOException {
		try {
			String hql = "from DeviceSproperty where standardcol=:standardcol and status = 1";
			Map<String, Object> map = new HashMap<String, Object>(); 
			map.put("standardcol", model.getStandardcol());   
			model.setStatus(true);
			
			List<DeviceSproperty> params = dpcmanager.find(hql, map);
			
			if (null != params && params.size() > 0) {
				JSONTool.writeMsgResult(false, "该参数已存在！");
			}else {
				dpcmanager.save(model);
				JSONTool.writeMsgResult(true, "保存完成");
			} 
			
		} catch (Exception e) {
			e.printStackTrace();
			JSONTool.writeMsgResult(false, "保存失败");
		}finally{
			SysLog log=new SysLog();
			log.setBusinessName("设备属性维护");
			log.setContent("新增设备属性："+model.getStandardcolname()+"-"+model.getColdatatype());
			log.setOperationType("add");
			log.setCreateUser(objUser.getPrincipal().getName());
			log.setCreateTime(new Date());
			logManager.save(log);
		}
	}
	public void edit() throws IOException {
		try {
			DeviceSproperty devicePropertyconfig = new DeviceSproperty();
			devicePropertyconfig=dpcmanager.findUniqueBy("id",model.getId());
			
			devicePropertyconfig.setStandardcolname(model.getStandardcolname()); 
			devicePropertyconfig.setColdatatype(model.getColdatatype());
			devicePropertyconfig.setCollen(model.getCollen()); 
			
			dpcmanager.save(devicePropertyconfig);
			JSONTool.writeMsgResult(true, "编辑成功！");
		} catch (Exception e) {
			e.printStackTrace();
			JSONTool.writeMsgResult(false, "编辑失败！");
		}finally{
			SysLog log=new SysLog();
			log.setBusinessName("设备属性维护");
			log.setContent("修改设备属性："+model.getStandardcolname()+"-"+model.getColdatatype());
			log.setOperationType("edit");
			log.setCreateUser(objUser.getPrincipal().getName());
			log.setCreateTime(new Date());
			logManager.save(log);
		}
	}
	public void delete() throws IOException {
		try {   
			Long id=model.getId();

			model=dpcmanager.findUniqueBy("id",id); 
			model.setStatus(false);
			
			dpcmanager.save(model); 
			JSONTool.writeMsgResult(true, "删除完成！");
			  
		} catch (Exception e) {
			e.printStackTrace();
			JSONTool.writeMsgResult(false, "删除失败！");
		}finally{
			SysLog log=new SysLog();
			log.setBusinessName("设备属性维护");
			log.setContent("删除设备属性："+model.getStandardcolname()+"-"+model.getColdatatype());
			log.setOperationType("delete");
			log.setCreateUser(objUser.getPrincipal().getName());
			log.setCreateTime(new Date());
			logManager.save(log);
		}
	}
	
	public void queryStandardCol() throws IOException {
		try {
			String hql = "from DeviceSproperty d where d.status = 1";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", id);
			List<DeviceSproperty> deviceSpropertys = dpcmanager.find(hql, map);
			List<DeviceSpropertyJSON> jsons = new ArrayList<DeviceSpropertyJSON>();
			for (DeviceSproperty deviceSproperty : deviceSpropertys) {
				DeviceSpropertyJSON json = new DeviceSpropertyJSON();
				 
				json.setId(deviceSproperty.getId());
				json.setStandardcol(deviceSproperty.getStandardcol());
				json.setStandardcolname(deviceSproperty.getStandardcolname());
				json.setColdatatype(deviceSproperty.getColdatatype());
				jsons.add(json);
			}
			JSONTool.writeDataResult(jsons);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
