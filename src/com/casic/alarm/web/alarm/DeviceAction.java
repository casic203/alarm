package com.casic.alarm.web.alarm;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.dao.DataIntegrityViolationException;

import com.casic.alarm.JSON.AcceptPersonJSON;
import com.casic.alarm.JSON.DeviceJSON;
import com.casic.alarm.domain.AcceptPerson;
import com.casic.alarm.domain.Device;
import com.casic.alarm.domain.DeviceType;
import com.casic.alarm.domain.SysLog;
import com.casic.alarm.manager.AcceptPersonManager;
import com.casic.alarm.manager.AlarmRecordManager;
import com.casic.alarm.manager.DeviceManager;
import com.casic.alarm.manager.DeviceTypeManager;
import com.casic.alarm.manager.SysLogManager;
import com.casic.core.json.JSONTool;
import com.casic.core.util.StringUtils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

@Results({ @Result(name = "reload", location = "device.do", type = "redirect") })
public class DeviceAction implements ModelDriven<Device>, Preparable {

	private long id;
	private long typeId;
	private long personId;
	private int page;
	private int rows;
	private String beginUseTime;
	private Device model;
	private Assertion objUser;
	private AcceptPerson acceptPerson;
	private DeviceType deviceType;
	private DeviceManager deviceManager;
	private DeviceTypeManager deviceTypeManager;
	private AcceptPersonManager acceptPersonManager;
	private AlarmRecordManager alarmRecordManager;
	private SysLogManager logManager;
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public long getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		if(StringUtils.isNotBlank(personId)){
			this.personId = Long.parseLong(personId);
		}
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

	public String getBeginUseTime() {
		return beginUseTime;
	}

	public void setBeginUseTime(String beginUseTime) {
		this.beginUseTime = beginUseTime;
	}

	public Device getModel() {
		return this.model;
	}
	
	public void setModel(Device model){
		this.model=model;
	}

	public AcceptPerson getAcceptPerson() {
		return acceptPerson;
	}

	public void setAcceptPerson(AcceptPerson acceptPerson) {
		this.acceptPerson = acceptPerson;
	}

	public DeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}

	@Resource
	public void setDeviceManager(DeviceManager deviceManager) {
		this.deviceManager = deviceManager;
	}

	@Resource
	public void setDeviceTypeManager(DeviceTypeManager deviceTypeManager) {
		this.deviceTypeManager = deviceTypeManager;
	}

	@Resource
	public void setAcceptPersonManager(AcceptPersonManager acceptPersonManager) {
		this.acceptPersonManager = acceptPersonManager;
	}

	@Resource
	public void setAlarmRecordManager(AlarmRecordManager alarmRecordManager) {
		this.alarmRecordManager = alarmRecordManager;
	}
	
	@Resource
	public void setLogManager(SysLogManager logManager) {
		this.logManager = logManager;
	}

	private void objToJSON(List<Device> devices, List<DeviceJSON> jsons)
			throws IOException {
		for (Device device : devices) {
			DeviceJSON json = new DeviceJSON();
			json.setId(device.getId());
			json.setActive(device.getActive());
			json.setDevCode(device.getDevCode());
			json.setDevName(device.getDevName());
			json.setTypeId(device.getDeviceType().getId());
			json.setTypeName(device.getDeviceType().getTypeName());
			json.setLatitude(device.getLatitude());
			json.setLongtitude(device.getLongtitude());
			json.setHeight(device.getHeight());
			json.setTurnX(device.getTurnX());
			json.setTurnY(device.getTurnY());
			json.setTurnZ(device.getTurnZ());
			json.setZoomX(device.getZoomX());
			json.setZoomY(device.getZoomY());
			json.setZoomZ(device.getZoomZ());
			json.setGaocheng(device.getGaocheng());
			json.setFactory(device.getFactory());

			json.setInstallDate(device.getInstallDate() != null ? simpleDateFormat
					.format(device.getInstallDate()) : "");
			json.setOutDate(device.getOutDate() != null ? simpleDateFormat
					.format(device.getOutDate()) : "");

			json.setSimid(device.getSimid());
			json.setInstallPosition(device.getInstallPosition());
			json.setBeginUseTime(device.getBeginUseTime());
			json.setNo(device.getNo());

			if (null != device.getAcceptPerson()) {
				json.setPersonName(device.getAcceptPerson().getPersonName());
				json.setPersonId(device.getAcceptPerson().getId());
			}
			json.setTypeName(device.getDeviceType().getTypeName());
			jsons.add(json);
		}
	}

	public void prepare() throws Exception {
		this.model = new Device();
		page = page <= 0 ? 1 : page;
		rows = rows <= 0 ? 10 : rows;
		objUser = (Assertion) ActionContext.getContext().getSession().get("_const_cas_assertion_");
	}

	public String execute() {
		return "success";
	}

	@SuppressWarnings("unchecked")
	public void query() throws IOException {
		StringBuilder hqlRow = new StringBuilder();
		StringBuilder hqlCount = new StringBuilder();
		Map<String, Object> map = new HashMap<String, Object>();

		hqlRow.append("from Device where active=true");
		hqlCount.append("select count(*) from Device where active=true");

		if (StringUtils.isNotBlank(model.getDevCode())) {
			hqlRow.append(" and devCode=:devcode");
			hqlCount.append(" and devCode=:devcode");
			map.put("devcode", model.getDevCode());
		}
		if (StringUtils.isNotBlank(model.getDevName())) {
			hqlRow.append(" and devName like :devname");
			hqlCount.append(" and devName like :devname");
			map.put("devname", "%" + model.getDevName() + "%");
		}
		if (personId > 0) {
			hqlRow.append(" and acceptPerson.id=:pid");
			hqlCount.append(" and acceptPerson.id=:pid");
			map.put("pid", personId);
		}
		if (typeId > 0) {
			hqlRow.append(" and deviceType.id=:did");
			hqlCount.append(" and deviceType.id=:did");
			map.put("did", typeId);
		}
		try {
			page = page <= 0 ? 1 : page;
			rows = rows <= 0 ? 10 : rows;

			List<Device> list = (List<Device>) deviceManager.pagedQuery(
					hqlRow.toString(), page, rows, map).getResult();
			int total = deviceManager.getCount(hqlCount.toString(), map);

			List<DeviceJSON> jsons = new ArrayList<DeviceJSON>();

			objToJSON(list, jsons);

			map.clear();
			map.put("rows", jsons);
			map.put("total", total);
			JSONTool.writeDataResult(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void queryDeviceid() {
		try {
			String hql = "from Device d where 1=1";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", id);
			List<Device> devices = deviceManager.find(hql, map);
			List<DeviceJSON> jsons = new ArrayList<DeviceJSON>();
			for (Device device : devices) {
				DeviceJSON json = new DeviceJSON();

				json.setId(device.getId());
				json.setDevCode(device.getDevCode());
				json.setDevName(device.getDevName());
				jsons.add(json);
			}
			JSONTool.writeDataResult(jsons);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void queryUnAllotDeviceByPage() {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			StringBuilder hql = new StringBuilder();

			hql.delete(0, hql.length());
			map.clear();

			hql.append("from Device d where d.acceptPerson.id is null and d.active=true");
			List<Device> devices = (List<Device>) deviceManager.pagedQuery(
					hql.toString(), page, rows, map).getResult();
			List<DeviceJSON> jsons = new ArrayList<DeviceJSON>();
			objToJSON(devices, jsons);

			hql.delete(0, hql.length());
			map.clear();
			hql.append("select count(*) from Device d where d.acceptPerson.id is null and d.active=true");
			int total = deviceManager.getCount(hql.toString(), map);

			map.clear();
			map.put("rows", jsons);
			map.put("total", total);
			JSONTool.writeDataResult(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void queryDeviceByPageForEdit() {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			StringBuilder hql = new StringBuilder();

			hql.delete(0, hql.length());
			map.clear();

			hql.append("from Device d where (d.acceptPerson.id is null or d.acceptPerson.id=:id) and d.active=true");
			map.put("id", personId);
			List<Device> devices = (List<Device>) deviceManager.pagedQuery(
					hql.toString(), page, rows, map).getResult();
			List<DeviceJSON> jsons = new ArrayList<DeviceJSON>();
			objToJSON(devices, jsons);

			hql.delete(0, hql.length());
			map.clear();
			hql.append("select count(*) from Device d where (d.acceptPerson.id is null or d.acceptPerson.id=:id) and d.active=true");
			map.put("id", personId);
			int total = deviceManager.getCount(hql.toString(), map);

			map.clear();
			map.put("rows", jsons);
			map.put("total", total);
			JSONTool.writeDataResult(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void queryActive() {
		try {
			List<Device> devices = deviceManager.findBy("active", true);
			List<DeviceJSON> jsons = new ArrayList<DeviceJSON>();
			objToJSON(devices, jsons);
			JSONTool.writeDataResult(jsons);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 获取还没有分配到监测点可用设备
	 */
	@SuppressWarnings("unchecked")
	public void queryWithoutPosition() {
		try {
			String hql = "from Device where id not in (select device.id from DevPos)";
			List<Device> list = deviceManager.find(hql,
					new HashMap<String, Object>());
			List<DeviceJSON> jsons = new ArrayList<DeviceJSON>();
			objToJSON(list, jsons);
			JSONTool.writeDataResult(jsons);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void queryPersonByDevice() {
		try {
			String hql = "select d.acceptPerson from Device d where d.id=:id";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", id);
			List<AcceptPerson> acceptPersons = deviceManager.find(hql, map);
			List<AcceptPersonJSON> jsons = new ArrayList<AcceptPersonJSON>();
			for (AcceptPerson acceptPerson : acceptPersons) {
				AcceptPersonJSON json = new AcceptPersonJSON();
				json.setId(acceptPerson.getId());
				json.setPersonName(acceptPerson.getPersonName());
				jsons.add(json);
			}
			JSONTool.writeDataResult(jsons);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void queryDeviceTypeById() {
		try {
			String hql = "select deviceType from Device where id=:id";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", id);
			DeviceType type = deviceManager.findUnique(hql, map);
			map.clear();
			map.put("data", type);
			JSONTool.writeDataResult(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void queryDeviceWithUndealAlarmRecord() {
		try {
			String hql = "select rec.device from AlarmRecord rec where rec.active=true";
			Map<String, Object> map = new HashMap<String, Object>();
			List<Device> list = alarmRecordManager.find(hql, map);
			List<DeviceJSON> jsons = new ArrayList<DeviceJSON>();
			objToJSON(list, jsons);
			JSONTool.writeDataResult(jsons);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void save() throws IOException {
		try {
			if (null != deviceManager.findUniqueBy("devCode",model.getDevCode())) {
				JSONTool.writeMsgResult(false, "该设备的编号重复，不能录入！");
			} else {
				deviceType = deviceTypeManager.findUniqueBy("id",deviceType.getId());
				acceptPerson = acceptPersonManager.findUniqueBy("id",acceptPerson.getId());
				model.setDeviceType(deviceType);
				model.setAcceptPerson(acceptPerson);
				deviceManager.save(model);

				DeviceJSON json = new DeviceJSON();
				json.setDevCode(model.getDevCode());
				json.setDevName(model.getDevName());
				json.setTypeName(deviceType.getTypeName());
				json.setLocation(model.getDeviceType().getLocation());
				json.setLongtitude(model.getLongtitude());
				json.setLatitude(model.getLatitude());
				json.setHeight(model.getHeight());
				
				SysLog log=new SysLog();
				log.setBusinessName("设备维护");
				log.setContent("新增设备："+model.getDevCode()+"-"+model.getDevName());
				log.setOperationType("add");
				if(null!=objUser){
					log.setCreateUser(objUser.getPrincipal().getName());
				}
				log.setCreateTime(new Date());
				logManager.save(log);

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("success", true);
				map.put("msg", "保存完成！");
				map.put("dev", json);

				JSONTool.writeDataResult(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JSONTool.writeMsgResult(false, "保存失败！");
		}
	}

	public void delete() throws IOException {
		try {
			model = deviceManager.load(model.getId());
			deviceManager.removeById(model.getId());

			DeviceJSON json = new DeviceJSON();
			json.setDevCode(model.getDevCode());
			json.setDevName(model.getDevName());
			json.setTypeName(model.getDeviceType().getTypeName());
			json.setLocation(model.getDeviceType().getLocation());
			json.setLongtitude(model.getLongtitude());
			json.setLatitude(model.getLatitude());
			json.setHeight(model.getHeight());
			
			SysLog log=new SysLog();
			log.setBusinessName("设备维护");
			log.setContent("删除设备："+model.getDevCode()+"-"+model.getDevName());
			log.setOperationType("delete");
			if(null!=objUser){
				log.setCreateUser(objUser.getPrincipal().getName());
			}
			log.setCreateTime(new Date());
			logManager.save(log);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", true);
			map.put("msg", "删除设备完成！");
			map.put("dev", json);

			JSONTool.writeDataResult(map);

		} catch (DataIntegrityViolationException dve) {
			JSONTool.writeMsgResult(false, "还有与该设备关联的报警记录或工单，请先删除这些信息后在删除该设备！");
		} catch (Exception e) {
			e.printStackTrace();
			JSONTool.writeMsgResult(false, "删除设备失败！");
		}
	}

	public void edit() throws ParseException, IOException {
		try {
			deviceType = deviceTypeManager.findUniqueBy("id",typeId);
			acceptPerson = acceptPersonManager.findUniqueBy("id",personId);
			Device device = deviceManager.findUniqueBy("id", model.getId());
			device.setDeviceType(deviceType);
			device.setAcceptPerson(acceptPerson);
			device.setFactory(model.getFactory());
			device.setOutDate(model.getOutDate());
			device.setInstallDate(model.getInstallDate());
			device.setDevCode(model.getDevCode());
			device.setDevName(model.getDevName());
			device.setLongtitude(model.getLongtitude());
			device.setLatitude(model.getLatitude());
			device.setHeight(model.getHeight());
			device.setTurnX(model.getTurnX());
			device.setTurnY(model.getTurnY());
			device.setTurnZ(model.getTurnZ());
			device.setZoomX(model.getZoomX());
			device.setZoomY(model.getZoomY());
			device.setZoomZ(model.getZoomZ());
			device.setGaocheng(model.getGaocheng());

			device.setNo(model.getNo());
			device.setSimid(model.getSimid());
			device.setInstallPosition(model.getInstallPosition());
			if(StringUtils.isNotBlank(beginUseTime)){
				device.setBeginUseTime(simpleDateFormat.parse(beginUseTime));
			}

			deviceManager.save(device);

			DeviceJSON json = new DeviceJSON();
			json.setDevCode(device.getDevCode());
			json.setDevName(device.getDevName());
			json.setTypeName(device.getDeviceType().getTypeName());
			json.setLocation(device.getDeviceType().getLocation());
			json.setLongtitude(device.getLongtitude());
			json.setLatitude(device.getLatitude());
			json.setHeight(device.getHeight());
			json.setLocation(deviceType.getLocation());
			
			SysLog log=new SysLog();
			log.setBusinessName("设备维护");
			log.setContent("编辑设备："+model.getDevCode()+"-"+model.getDevName());
			log.setOperationType("edit");
			if(null!=objUser){
				log.setCreateUser(objUser.getPrincipal().getName());
			}
			log.setCreateTime(new Date());
			logManager.save(log);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", true);
			map.put("msg", "编辑完成！");
			map.put("dev", json);

			JSONTool.writeDataResult(map);
		} catch (Exception e) {
			e.printStackTrace();
			JSONTool.writeMsgResult(false, "编辑失败！");
		}
	}
}
