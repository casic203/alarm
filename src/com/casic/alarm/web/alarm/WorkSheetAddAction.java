package com.casic.alarm.web.alarm;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.jasig.cas.client.validation.Assertion;

import com.casic.alarm.JSON.AcceptPersonJSON;
import com.casic.alarm.JSON.WorkSheetJSON;
import com.casic.alarm.domain.AlarmRecord;
import com.casic.alarm.domain.Device;
import com.casic.alarm.domain.SysLog;
import com.casic.alarm.domain.WorkSheet;
import com.casic.alarm.manager.AlarmRecordManager;
import com.casic.alarm.manager.DeviceManager;
import com.casic.alarm.manager.SysLogManager;
import com.casic.alarm.manager.WorkSheetManager;
import com.casic.alarm.permission.UsersHttpUrlSourceFetcher;
import com.casic.core.json.JSONTool;
import com.casic.core.struts2.BaseAction;
import com.casic.core.util.StringUtils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class WorkSheetAddAction extends BaseAction implements
		ModelDriven<WorkSheet>, Preparable {

	private Long id;
	
	private int page;
	private int rows;
	
	private String sheetNo;
	private String chargerId;
	private Long devId;
	private String beginDate;
	private String endDate;

	private WorkSheet model;
	private Assertion objUser;
	private Device device;
	private AlarmRecord alarmRecord;
	private String charger;
	private String checker;

	private WorkSheetManager workSheetManager;
	private DeviceManager deviceManager;
	private AlarmRecordManager alarmRecordManager;
	private UsersHttpUrlSourceFetcher userSourceFetcher;
	private SysLogManager logManager;
	
	private SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");

	public void prepare() throws Exception {
		model = new WorkSheet();
		objUser = (Assertion) ActionContext.getContext().getSession().get("_const_cas_assertion_");
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getChargerId() {
		return chargerId;
	}

	public void setChargerId(String chargerId) {
		this.chargerId = chargerId;
	}

	public Long getDevId() {
		return devId;
	}

	public void setDevId(Long devId) {
		this.devId = devId;
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

	public WorkSheet getModel() {
		return model;
	}

	public void setModel(WorkSheet model) {
		this.model = model;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public AlarmRecord getAlarmRecord() {
		return alarmRecord;
	}

	public void setAlarmRecord(AlarmRecord alarmRecord) {
		this.alarmRecord = alarmRecord;
	}

	public String getCharger() {
		return charger;
	}

	public void setCharger(String charger) {
		this.charger = charger;
	}

	public String getChecker() {
		return checker;
	}

	public void setChecker(String checker) {
		this.checker = checker;
	}

	public void setWorkSheetManager(WorkSheetManager workSheetManager) {
		this.workSheetManager = workSheetManager;
	}
	
	public String getSheetNo() {
		return sheetNo;
	}

	public void setSheetNo(String sheetNo) {
		this.sheetNo = sheetNo;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	@Resource
	public void setDeviceManager(DeviceManager deviceManager) {
		this.deviceManager = deviceManager;
	}

	@Resource
	public void setAlarmRecordManager(AlarmRecordManager alarmRecordManager) {
		this.alarmRecordManager = alarmRecordManager;
	}

	@Resource
	public void setUserSourceFetcher(UsersHttpUrlSourceFetcher userSourceFetcher) {
		this.userSourceFetcher = userSourceFetcher;
	}
	
	@Resource
	public void setLogManager(SysLogManager logManager) {
		this.logManager = logManager;
	}

	private void objToJSON(List<WorkSheet> list, List<WorkSheetJSON> jsons) {
		WorkSheetJSON json;
		for (WorkSheet sheet : list) {
			json = new WorkSheetJSON();
			json.setId(sheet.getId());
			json.setAlarmRecord(sheet.getAlarmRecord().getMessage());
			json.setBeginDate(sheet.getBeginDate());
			json.setCharger(sheet.getCharger());
			json.setChecker(sheet.getChecker());
			json.setWriter(sheet.getWriter());
			json.setDevice(sheet.getDevice().getDevName());
			json.setEndDate(sheet.getEndDate());
			json.setLogDate(sheet.getLogDate());
			if (null != sheet.getAccDate()) {
				json.setAccDate(sheet.getAccDate());
			}
			json.setSheetStatus(sheet.getSheetStatus());
			json.setSheetNo(sheet.getSheetNo());
			json.setTask(sheet.getTask());
			jsons.add(json);
		}
	}

	@SuppressWarnings("unchecked")
	public void query() {
		try {
			StringBuilder hqlRow = new StringBuilder();
			StringBuilder hqlCount = new StringBuilder();
			Map<String, Object> map = new java.util.HashMap<String, Object>();

			hqlRow.append("from WorkSheet where sheetStatus='待审' and writer=:wt");
			hqlCount.append("select count(*) from WorkSheet where sheetStatus='待审' and writer=:wt");
			map.put("wt", objUser.getPrincipal().getName());

			if (StringUtils.isNotBlank(sheetNo)) {
				hqlRow.append(" and sheetNo like :sheet");
				hqlCount.append(" and sheetNo like :sheet");
				map.put("sheet", "%" + model.getSheetNo() + "%");
			}

			if (null != devId) {
				hqlRow.append(" and device.id=:devId");
				hqlCount.append(" and device.id=:devId");
				map.put("devId", devId);
			}

			if (StringUtils.isNotBlank(chargerId)) {
				hqlRow.append(" and charger=:perId");
				hqlCount.append(" and charger=:perId");
				map.put("perId", chargerId);
			}

			if (StringUtils.isNotBlank(beginDate)) {
				hqlRow.append(" and beginDate=:begin");
				hqlCount.append(" and beginDate=:begin");
				map.put("begin", format.parseObject(beginDate));
			}

			if (StringUtils.isNotBlank(endDate)) {
				hqlRow.append(" and endDate=:end");
				hqlCount.append(" and endDate=:end");
				map.put("endDate", format.parseObject(endDate));
			}

			page = page <= 0 ? 1 : page;
			rows = rows <= 0 ? 10 : rows;

			List<WorkSheet> list = (List<WorkSheet>) workSheetManager
					.pagedQuery(hqlRow.toString(), page, rows, map).getResult();
			int total = workSheetManager.getCount(hqlCount.toString(), map);

			List<WorkSheetJSON> jsons = new ArrayList<WorkSheetJSON>();

			objToJSON(list, jsons);

			map.clear();
			map.put("rows", jsons);
			map.put("total", total);
			JSONTool.writeDataResult(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void queryChargerFromWorkSheetById() {
		try {
			WorkSheet workSheet = workSheetManager.findUniqueBy("id", id);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", workSheet.getCharger());
			JSONTool.writeDataResult(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void save() {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy_MM_dd_HH_ss_mm");
			device = deviceManager.findUniqueBy("id", device.getId());
			alarmRecord = alarmRecordManager.findUniqueBy("id",alarmRecord.getId());
			alarmRecord.setActive(false);
			String sheetNo = device.getDevCode() + "_"
					+ simpleDateFormat.format(new Date());
			model.setSheetNo(sheetNo);
			model.setDevice(device);
			model.setAlarmRecord(alarmRecord);
			model.setCharger(charger);
			model.setLogDate(new Date());
			model.setSheetStatus("待审");
			model.setWriter(objUser.getPrincipal().getName());
			workSheetManager.save(model);
			alarmRecordManager.save(alarmRecord);
			
			SysLog log=new SysLog();
			log.setBusinessName("工单管理");
			log.setContent("新建工单："+model.getSheetNo()+"-"+model.getDevice().getDevCode());
			log.setOperationType("add");
			log.setCreateUser(objUser.getPrincipal().getName());
			log.setCreateTime(new Date());
			logManager.save(log);
			
			JSONTool.writeMsgResult(true, "添加成功！");

		} catch (Exception e) {
			try {
				e.printStackTrace();
				JSONTool.writeMsgResult(false, "添加失败！");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void edit() {
		try {
			WorkSheet sheet=workSheetManager.findUniqueBy("id", model.getId());
			sheet.setCharger(charger);
			sheet.setSheetStatus("待审");
			sheet.setTask(model.getTask());
			sheet.setBeginDate(model.getBeginDate());
			sheet.setEndDate(model.getEndDate());
			workSheetManager.save(sheet);
			
			SysLog log=new SysLog();
			log.setBusinessName("工单管理");
			log.setContent("修改工单："+sheet.getSheetNo()+"-"+sheet.getDevice().getDevCode());
			log.setOperationType("edit");
			log.setCreateUser(objUser.getPrincipal().getName());
			log.setCreateTime(new Date());
			logManager.save(log);
			
			JSONTool.writeMsgResult(true, "编辑成功！");

		} catch (Exception e) {
			try {
				e.printStackTrace();
				JSONTool.writeMsgResult(false, "编辑失败！");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void delete() {
		try {
			model = workSheetManager.findUniqueBy("id", model.getId());
			alarmRecord = model.getAlarmRecord();
			alarmRecord.setActive(true);
			alarmRecordManager.save(alarmRecord);
			workSheetManager.remove(model);
			
			SysLog log=new SysLog();
			log.setBusinessName("工单管理");
			log.setContent("删除工单："+model.getSheetNo()+"-"+model.getDevice().getDevCode());
			log.setOperationType("add");
			log.setCreateUser(objUser.getPrincipal().getName());
			log.setCreateTime(new Date());
			logManager.save(log);
			
			JSONTool.writeMsgResult(true, "删除完成！");
		} catch (Exception e) {
			try {
				e.printStackTrace();
				JSONTool.writeMsgResult(false, "删除失败！");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void queryPerson() {
		try {
			List<String> list = userSourceFetcher.getSource("");
			List<AcceptPersonJSON> jsons = new ArrayList<AcceptPersonJSON>();

			if (null != list && list.size() > 0) {
				for (String str : list) {
					AcceptPersonJSON json = new AcceptPersonJSON();
					json.setPersonName(str);
					jsons.add(json);
				}
				JSONTool.writeDataResult(jsons);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
