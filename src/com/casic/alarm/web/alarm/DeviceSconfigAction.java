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
 
import com.casic.alarm.JSON.DeviceJSON;
import com.casic.alarm.JSON.DeviceSconfigJSON;  
import com.casic.alarm.domain.Device;
import com.casic.alarm.domain.DeviceSensor;
import com.casic.alarm.domain.DeviceSconfig; 
import com.casic.alarm.domain.DeviceSproperty;
import com.casic.alarm.domain.SensorType;
import com.casic.alarm.domain.SysLog;
import com.casic.alarm.manager.DeviceSconfigManager;
import com.casic.alarm.manager.SysLogManager;
import com.casic.core.json.JSONTool;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
@Results({
	@org.apache.struts2.convention.annotation.Result(name = "reload", location = "device-sconfig.do", type = "redirect"),
	@org.apache.struts2.convention.annotation.Result(name = "json", type = "json", params = {
			"root", "jsonResults" }) })
public class DeviceSconfigAction  implements ModelDriven<DeviceSconfig>, Preparable {
 
	private int page;
	private String sensorcode; 
	private int rows;
 
	private DeviceSconfig model;
	private Assertion objUser;
	private SysLogManager logManager;
	private DeviceSconfigManager dscmanager; 
	
	public void prepare() throws Exception {
		objUser = (Assertion) ActionContext.getContext().getSession().get("_const_cas_assertion_");
		this.model = new DeviceSconfig();
	} 
	public DeviceSconfig getModel() {
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
	public void setDscmanager(DeviceSconfigManager dscmanager) {
		this.dscmanager = dscmanager;
	}
	
	@Resource
	public void setLogManager(SysLogManager logManager) {
		this.logManager = logManager;
	}
	public void queryParamitem(){
		try {  
			String hqlString="from DeviceSconfig d where 1=1";
			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("id", id);
			List<DeviceSconfig> deviceSconfigs = dscmanager.find(hqlString, map);
			List<DeviceSconfigJSON> jsons = new ArrayList<DeviceSconfigJSON>();
			for (DeviceSconfig deviceSconfig : deviceSconfigs) {
				DeviceSconfigJSON json = new DeviceSconfigJSON();
				 
//				json.setId(deviceSconfig.getId());
//				json.setItemname(deviceSconfig.getItemname());  
				jsons.add(json);
			}
			JSONTool.writeDataResult(jsons);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void query() throws IOException {
		try {  
			StringBuilder hqlTotal=new StringBuilder();
			StringBuilder hqlParam = new StringBuilder();  
			Map<String, Object>map=new HashMap<String, Object>();
			 
			hqlTotal.append("select count(*) from DeviceSconfig as ds where 1=1");
			hqlParam.append("from DeviceSconfig as ds where 1=1");  
		  
			if(getSensorcode()!=null){
				hqlTotal.append(" and ds.dsid.sensorcode =:sensorcode");
				hqlParam.append(" and ds.dsid.sensorcode =:sensorcode");
				map.put("sensorcode", sensorcode);
			}
			hqlParam.append(" order by ds.dsid.sensorcode,ds.itemnum");
			 
			int total=dscmanager.getCount(hqlTotal.toString(), map);
			List<DeviceSconfig> dscList = (List<DeviceSconfig>)dscmanager.pagedQuery(hqlParam.toString(), page,rows,map).getResult(); 
			
			List<DeviceSconfigJSON> dscjsons=new ArrayList<DeviceSconfigJSON>();
			objToDSCJSON(dscList,dscjsons);
			
			map.clear(); 
			map.put("total", total);
			map.put("rows", dscjsons);
			
			JSONTool.writeDataResult(map);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}  
	private void objToDSCJSON(List<DeviceSconfig> deviceSconfig,
			List<DeviceSconfigJSON> jsons) {  
		for (DeviceSconfig t : deviceSconfig) {
			DeviceSconfigJSON json = new DeviceSconfigJSON();
			json.setSensorcode(t.getSensortype().getSensorcode()); 
			json.setSensorname(t.getSensortype().getSensorname());
			json.setItemnum(t.getItemnum());
			json.setItemname(t.getDsid().getItemname());
			json.setItemdatatype(t.getItemdatatype());
			json.setItemvalue(t.getItemvalue());  
			
			jsons.add(json);
		}
	}
	public void save() throws IOException {
		try {
			String hql = "from DeviceSconfig where dsid.sensorcode=:sensorcode and dsid.itemname=:itemname";
			
			Map<String, Object> map = new HashMap<String, Object>(); 
			map.put("sensorcode", model.getDsid().getSensorcode());
			map.put("itemname", model.getDsid().getItemname());   
			List<DeviceSconfig> params = dscmanager.find(hql, map);
						 
			if (null != params && params.size() > 0) {
				JSONTool.writeMsgResult(false, "传感器类型和编码重复！");
			}else {
				dscmanager.save(model);
				JSONTool.writeMsgResult(true, "保存完成");
			}  
			
		} catch (Exception e) {
			e.printStackTrace();
			JSONTool.writeMsgResult(false, "保存失败");
		}finally{
			SysLog log=new SysLog();
			log.setBusinessName("设备配置维护");
			log.setContent("新增配置："+model.getDsid()+"-"+model.getSensortype());
			log.setOperationType("add");
			log.setCreateUser(objUser.getPrincipal().getName());
			log.setCreateTime(new Date());
			logManager.save(log);
		}
	}
	public void edit() throws IOException {
		try {
			DeviceSconfig deviceSconfig = new DeviceSconfig();
			String hql ="from DeviceSconfig as ds where 1=1 and ds.dsid.sensorcode='"+sensorcode+"'";
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("sensorcode", sensorcode); 
			
			page = page <= 0 ? 1 : page;
			rows = rows <= 0 ? 10 : rows;
			
			List<DeviceSconfig> deviceSconfigs = (List<DeviceSconfig>)dscmanager.pagedQuery(hql, page,rows,map).getResult();  
			for(DeviceSconfig ds:deviceSconfigs){
				deviceSconfig  = ds;
			}  
			
			deviceSconfig.setItemvalue(model.getItemvalue());
			 
			dscmanager.save(deviceSconfig);
			JSONTool.writeMsgResult(true, "编辑成功！");
		} catch (Exception e) {
			e.printStackTrace();
			JSONTool.writeMsgResult(false, "编辑失败！");
		}finally{
			SysLog log=new SysLog();
			log.setBusinessName("设备配置维护");
			log.setContent("编辑配置："+model.getDsid()+"-"+model.getSensortype());
			log.setOperationType("edit");
			log.setCreateUser(objUser.getPrincipal().getName());
			log.setCreateTime(new Date());
			logManager.save(log);
		}
	}
	
	public void delete() throws IOException {
		try { 
			DeviceSconfig deviceSconfig = new DeviceSconfig();
			String hql ="from DeviceSconfig as ds where 1=1 and ds.dsid.sensorcode='"+sensorcode+"'";
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("sensorcode", sensorcode); 
			
			page = page <= 0 ? 1 : page;
			rows = rows <= 0 ? 10 : rows;
			
			List<DeviceSconfig> deviceSconfigs = (List<DeviceSconfig>)dscmanager.pagedQuery(hql, page,rows,map).getResult();  
			for(DeviceSconfig ds:deviceSconfigs){
				deviceSconfig  = ds;
			} 
			  
			dscmanager.remove(deviceSconfig);
			JSONTool.writeMsgResult(true, "删除完成！");
			  
		} catch (Exception e) {
			e.printStackTrace();
			JSONTool.writeMsgResult(false, "删除失败！");
		}finally{
			SysLog log=new SysLog();
			log.setBusinessName("设备配置维护");
			log.setContent("删除配置："+model.getDsid()+"-"+model.getSensortype());
			log.setOperationType("delete");
			log.setCreateUser(objUser.getPrincipal().getName());
			log.setCreateTime(new Date());
			logManager.save(log);
		}
		 
	}
}
