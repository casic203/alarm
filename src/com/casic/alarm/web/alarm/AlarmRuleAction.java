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

import com.casic.alarm.JSON.AlarmRuleJSON;
import com.casic.alarm.JSON.DeviceSensorJSON;
import com.casic.alarm.domain.AlarmRule;
import com.casic.alarm.domain.DeviceSconfig;
import com.casic.alarm.domain.DeviceSensor;
import com.casic.alarm.domain.DeviceSproperty;
import com.casic.alarm.domain.SysLog;
import com.casic.alarm.manager.AlarmRuleManager;
import com.casic.alarm.manager.DeviceManager;
import com.casic.alarm.manager.DeviceSensorManager;
import com.casic.alarm.manager.DeviceTypeManager;
import com.casic.alarm.manager.SysLogManager;
import com.casic.core.json.JSONTool;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
@Results({
	@org.apache.struts2.convention.annotation.Result(name = "reload", location = "alarm-rule.do", type = "redirect"),
	@org.apache.struts2.convention.annotation.Result(name = "json", type = "json", params = {
			"root", "jsonResults" }) }) 
public class AlarmRuleAction implements ModelDriven<AlarmRule>, Preparable{

	private long id; 
	private int page;
	private int rows;

	private AlarmRule model;
	private Assertion objUser;
	private DeviceManager deviceManager;
	private AlarmRuleManager arManager;
	private SysLogManager logManager;
	
	public void prepare() throws Exception {
		this.model = new AlarmRule();
		objUser = (Assertion) ActionContext.getContext().getSession().get("_const_cas_assertion_");
	}

	public AlarmRule getModel() {
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
	public void setDeviceManager(DeviceManager deviceManager) {
		this.deviceManager = deviceManager;
	}   
	@Resource
	public void setArManager(AlarmRuleManager arManager) {
		this.arManager = arManager;
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
			 
			hqlTotal.append("select count(*) from AlarmRule as ar where 1=1");
			hqlParam.append("from AlarmRule as ar where 1=1");
			
			hqlParam.append(" order by ar.deviceid ");
			int total=arManager.getCount(hqlTotal.toString(), map);
			List<AlarmRule> dpList = (List<AlarmRule>)arManager.pagedQuery(hqlParam.toString(), page,rows,map).getResult(); 
			
			List<AlarmRuleJSON> arjsons=new ArrayList<AlarmRuleJSON>();
			objToARJSON(dpList,arjsons);

			map.clear();
			map.put("total", total);
			map.put("rows", arjsons);

			JSONTool.writeDataResult(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void objToARJSON(List<AlarmRule> alarmRules,
			List<AlarmRuleJSON> jsons) {  
		for (AlarmRule t : alarmRules) {
			AlarmRuleJSON json = new AlarmRuleJSON(); 
			
			json.setId(t.getId());
			json.setDeviceid(t.getDeviceid());
			json.setParamcode(t.getParamcode());
			json.setParamname(t.getParamname());
			json.setSecullval(t.getSecullval());
			json.setSeculval(t.getSeculval());
			json.setSecuokval(t.getSecuokval());
			json.setSecuhval(t.getSecuhval());
			json.setSecuhhval(t.getSecuhhval());
			json.setSensorcode(t.getSensorcode());
			 
			jsons.add(json);
		}
	}
	public void save() throws IOException {
		try {
			String hql = "from AlarmRule where deviceid=:deviceid and sensorcode=:sensorcode and paramcode=:paramcode";
			
			Map<String, Object> map = new HashMap<String, Object>(); 
			map.put("deviceid", model.getDeviceid());
			map.put("sensorcode", model.getSensorcode());  
			map.put("paramcode", model.getParamcode());   
			map.put("paramname", model.getParamname());
			map.put("secullval", model.getSecullval());
			map.put("seculval", model.getSeculval());
			map.put("secuokval", model.getSecuokval());
			map.put("secuhval", model.getSecuhval());
			map.put("secuhhval", model.getSecuhhval());
			
			List<AlarmRule> params = arManager.find(hql, map);
			   
			if (null != params && params.size() > 0) {
				JSONTool.writeMsgResult(false, "已有警限！");
			}else {
				arManager.save(model);
				
				SysLog log=new SysLog();
				log.setBusinessName("报警规格维护");
				log.setContent("新增报警规则："+model.getParamcode()+"-"+model.getParamname());
				log.setOperationType("delete");
				log.setCreateUser(objUser.getPrincipal().getName());
				log.setCreateTime(new Date());
				logManager.save(log);
				
				JSONTool.writeMsgResult(true, "保存完成");
			}  
			
		} catch (Exception e) {
			e.printStackTrace();
			JSONTool.writeMsgResult(false, "保存失败");
		}
	}
	public void delete() throws IOException {
		try { 
			Long id=model.getId();

			model=arManager.findUniqueBy("id", id); 
			
			arManager.remove(model);
			
			SysLog log=new SysLog();
			log.setBusinessName("报警规格维护");
			log.setContent("删除报警规则："+model.getParamcode()+"-"+model.getParamname());
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
	public void edit() throws IOException {
		try {
			AlarmRule alarmRule = new AlarmRule();
			alarmRule=arManager.findUniqueBy("id",model.getId()); 
			alarmRule.setSecullval(model.getSecullval());
			alarmRule.setSeculval(model.getSeculval());
			alarmRule.setSecuokval(model.getSecuokval());
			alarmRule.setSecuhval(model.getSecuhval());
			alarmRule.setSecuhhval(model.getSecuhhval()); 
			 
			arManager.save(alarmRule);
			
			SysLog log=new SysLog();
			log.setBusinessName("报警规格维护");
			log.setContent("编辑报警规则："+model.getParamcode()+"-"+model.getParamname());
			log.setOperationType("delete");
			log.setCreateUser(objUser.getPrincipal().getName());
			log.setCreateTime(new Date());
			logManager.save(log);
			
			JSONTool.writeMsgResult(true, "编辑成功！");
		} catch (Exception e) {
			e.printStackTrace();
			JSONTool.writeMsgResult(false, "编辑失败！");
		}
	}
}
