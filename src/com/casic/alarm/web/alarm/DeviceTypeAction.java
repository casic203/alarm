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
import org.springframework.dao.DataIntegrityViolationException;

import com.casic.alarm.JSON.DeviceTypeJSON;
import com.casic.alarm.domain.Device;
import com.casic.alarm.domain.DeviceType;
import com.casic.alarm.domain.SysLog;
import com.casic.alarm.manager.DeviceManager;
import com.casic.alarm.manager.DeviceTypeManager;
import com.casic.alarm.manager.SysLogManager;
import com.casic.core.json.JSONTool;
import com.casic.core.util.StringUtils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

@Results({
		@org.apache.struts2.convention.annotation.Result(name = "reload", location = "device-type.do?operationMode=RETRIEVE", type = "redirect"),
		@org.apache.struts2.convention.annotation.Result(name = "json", type = "json", params = {
				"root", "jsonResults" }) })
public class DeviceTypeAction implements ModelDriven<DeviceType>, Preparable {

	private long id;
	private int page;
	private int rows;
	private DeviceType model;
	private Assertion objUser;

	private DeviceTypeManager manager;
	private DeviceManager deviceManager;
	private SysLogManager logManager;
	
	public String execute() {
		return "success";
	}

	public void prepare() throws Exception {
		this.model = new DeviceType();
		objUser = (Assertion) ActionContext.getContext().getSession().get("_const_cas_assertion_");
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

	public DeviceType getModel() {
		return this.model;
	}

	public DeviceTypeManager getManager() {
		return manager;
	}

	@Resource
	public void setManager(DeviceTypeManager manager) {
		this.manager = manager;
	}

	public DeviceManager getDeviceManager() {
		return deviceManager;
	}

	@Resource
	public void setDeviceManager(DeviceManager deviceManager) {
		this.deviceManager = deviceManager;
	}

	@Resource
	public void setLogManager(SysLogManager logManager) {
		this.logManager = logManager;
	}

	@SuppressWarnings("unchecked")
	public void query() throws IOException {
		try {
			StringBuilder hqlRow = new StringBuilder();
			StringBuilder hqlTotal = new StringBuilder();
			Map<String, Object> map = new HashMap<String, Object>();

			hqlRow.append("from DeviceType where 1=1");
			hqlTotal.append("select count(*) from DeviceType where 1=1");

			if (StringUtils.isNotBlank(model.getTypeCode())) {
				hqlRow.append(" and typeCode=:code");
				hqlTotal.append(" and typeCode=:code");
				map.put("code", model.getTypeCode());
			}
			if (StringUtils.isNotBlank(model.getTypeName())) {
				hqlRow.append(" and typeName like :name");
				hqlTotal.append(" and typeName like :name");
				map.put("name", "%" + model.getTypeName() + "%");
			}
			
			hqlRow.append(" order by id");

			List<DeviceType> list = (List<DeviceType>) deviceManager
					.pagedQuery(hqlRow.toString(), page, rows, map).getResult();
			List<DeviceTypeJSON> jsons = new ArrayList<DeviceTypeJSON>();
			objToJSON(list, jsons);
			int total = deviceManager.getCount(hqlTotal.toString(), map);

			map.clear();
			map.put("rows", jsons);
			map.put("total", total);

			JSONTool.writeDataResult(map);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void queryType() throws IOException {
		List<DeviceType> deviceTypes = manager.findBy("active", true);
		List<DeviceTypeJSON> jsons = new ArrayList<DeviceTypeJSON>();
		objToJSON(deviceTypes, jsons);
		JSONTool.writeDataResult(jsons);
	}

	private void objToJSON(List<DeviceType> deviceTypes,
			List<DeviceTypeJSON> jsons) {
		for (DeviceType t : deviceTypes) {
			DeviceTypeJSON json = new DeviceTypeJSON();
			json.setTypeCode(t.getTypeCode());
			json.setTypeName(t.getTypeName());
			json.setLocation(t.getLocation());
			json.setActive(t.getActive());
			json.setId(t.getId());
			jsons.add(json);
		}
	}

	@SuppressWarnings("unchecked")
	public void save() throws IOException {
		try {
			String hql = "from DeviceType where typeCode=:code or typeName=:nam";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("code", model.getTypeCode());
			map.put("nam", model.getTypeName());
			List<DeviceType> types = manager.find(hql, map);
			if (null != types && types.size() > 0) {
				JSONTool.writeMsgResult(false, "设备类型和编号重复！");
			} else {
				manager.save(model);
				
				SysLog log=new SysLog();
				log.setBusinessName("设备类型维护");
				log.setContent("新增设备类型："+model.getTypeCode()+"-"+model.getTypeName());
				log.setOperationType("add");
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

	public void edit() throws IOException {
		try {
			DeviceType deviceType = manager.load(model.getId());
			String src = deviceType.getLocation();
			String dst = model.getLocation();
			
			deviceType.setTypeCode(model.getTypeCode());
			deviceType.setTypeName(model.getTypeName());
			deviceType.setLocation(model.getLocation());
			
			manager.save(deviceType);
			
			SysLog log=new SysLog();
			log.setBusinessName("设备类型维护");
			log.setContent("修改设备类型："+model.getTypeCode()+"-"+model.getTypeName());
			log.setOperationType("edit");
			log.setCreateUser(objUser.getPrincipal().getName());
			log.setCreateTime(new Date());
			logManager.save(log);
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", true);
			map.put("msg", "编辑完成！");
			map.put("src", src);
			map.put("dst", dst);
			map.put("nam", deviceType.getTypeName());
			
			JSONTool.writeDataResult(map);
		} catch (Exception e) {
			e.printStackTrace();
			JSONTool.writeMsgResult(false, "编辑失败！");
		}
	}

	public void delete() throws IOException {
		try {
			manager.removeById(id);
			
			SysLog log=new SysLog();
			log.setBusinessName("设备类型维护");
			log.setContent("删除设备类型："+model.getTypeCode()+"-"+model.getTypeName());
			log.setOperationType("edit");
			log.setCreateUser(objUser.getPrincipal().getName());
			log.setCreateTime(new Date());
			logManager.save(log);
			
			JSONTool.writeMsgResult(true, "删除完成！");
		}catch(DataIntegrityViolationException e1){
			JSONTool.writeMsgResult(false, "还有与此设备类型关联的设备，因此不能删除该设备类型！");
		}catch (Exception e2) {
			e2.printStackTrace();
			JSONTool.writeMsgResult(false, "删除失败！");
		}
	}
}
