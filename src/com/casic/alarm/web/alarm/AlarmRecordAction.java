package com.casic.alarm.web.alarm;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.dao.DataIntegrityViolationException;

import com.casic.alarm.JSON.AlarmRecordJSON;
import com.casic.alarm.domain.AcceptPerson;
import com.casic.alarm.domain.AlarmRecord;
import com.casic.alarm.domain.AlarmType;
import com.casic.alarm.domain.Device;
import com.casic.alarm.domain.SysLog;
import com.casic.alarm.manager.AcceptPersonManager;
import com.casic.alarm.manager.AlarmRecordManager;
import com.casic.alarm.manager.DeviceManager;
import com.casic.alarm.manager.SysLogManager;
import com.casic.core.json.JSONTool;
import com.casic.core.page.Page;
import com.casic.core.util.StringUtils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class AlarmRecordAction implements ModelDriven<AlarmRecord>, Preparable {

	private Long id;
	private String typeName;
	private String devCodes;
	private String beginTime;
	private String endTime;
	private Long devId;
	private Long typeId;
	private Long personId;
	private Long active;
	private int page;
	private int rows;
	private String scope;
	private AlarmRecord model;
	private Device device;
	private AlarmType alarmType;
	private AcceptPerson acceptPerson;
	private Assertion objUser;
	private AlarmRecordManager alarmRecordManager;
	private AcceptPersonManager acceptPersonManager;
	private DeviceManager deviceManager;
	private SysLogManager logManager;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDevId() {
		return devId;
	}

	public void setDevId(Long devId) {
		this.devId = devId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getDevCodes() {
		return devCodes;
	}

	public void setDevCodes(String devCodes) {
		this.devCodes = devCodes;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public Long getPersonId() {
		return personId;
	}

	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	public Long getActive() {
		return active;
	}

	public void setActive(Long active) {
		this.active = active;
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

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public void setModel(AlarmRecord model) {
		this.model = model;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public AcceptPerson getAcceptPerson() {
		return acceptPerson;
	}

	public void setAcceptPerson(AcceptPerson acceptPerson) {
		this.acceptPerson = acceptPerson;
	}

	public AlarmType getAlarmType() {
		return alarmType;
	}

	public void setAlarmType(AlarmType alarmType) {
		this.alarmType = alarmType;
	}

	@Resource
	public void setAlarmRecordManager(AlarmRecordManager alarmRecordManager) {
		this.alarmRecordManager = alarmRecordManager;
	}

	@Resource
	public void setAcceptPersonManager(AcceptPersonManager acceptPersonManager) {
		this.acceptPersonManager = acceptPersonManager;
	}

	@Resource
	public void setDeviceManager(DeviceManager deviceManager) {
		this.deviceManager = deviceManager;
	}

	@Resource
	public void setLogManager(SysLogManager logManager) {
		this.logManager = logManager;
	}

	public AlarmRecord getModel() {
		return this.model;
	}

	private void objToJSON(List<AlarmRecord> alarmRecords,
			List<AlarmRecordJSON> jsons) {
		for (AlarmRecord obj : alarmRecords) {
			AlarmRecordJSON json = new AlarmRecordJSON();
			json.setId(obj.getId());
			json.setRecordCode(obj.getRecordCode());
			json.setDevice(obj.getDevice().getDevName());
			json.setDeviceType(obj.getDevice().getDeviceType().getTypeName());
			json.setMessage(obj.getMessage());
			json.setRecordDate(obj.getRecordDate());
			json.setCode(obj.getDevice().getDevCode());
			json.setActive(obj.getActive());
			json.setDealResult(json.getDealResult());
			json.setMessageStatus(obj.getMessageStatus());
			if (null != obj.getDevice().getAcceptPerson()) {
				json.setAcceptPerson(obj.getDevice().getAcceptPerson()
						.getPersonName());
			}
			jsons.add(json);
		}
	}

	public void prepare() throws Exception {
		this.model = new AlarmRecord();
		page = page <= 0 ? 1 : page;
		rows = rows <= 0 ? 10 : rows;
		objUser = (Assertion) ActionContext.getContext().getSession()
				.get("_const_cas_assertion_");
		new Page();
	}

	public String execute() {
		return "success";
	}

	@SuppressWarnings("unchecked")
	public void query() {
		try {
			StringBuilder hqlRow = new StringBuilder();
			StringBuilder hqlCount = new StringBuilder();
			Map<String, Object> map = new HashMap<String, Object>();

			hqlRow.append("from AlarmRecord where 1=1");
			hqlCount.append("select count(*) from AlarmRecord where 1=1");

			if (null != active && active == 1) {
				hqlRow.append(" and  active=:active");
				hqlCount.append(" and  active=:active");
				map.put("active", true);
			}

			if (null != active && active == 0) {
				hqlRow.append(" and  active=:active");
				hqlCount.append(" and  active=:active");
				map.put("active", false);
			}

			if (devId != null) {
				hqlRow.append(" and device.id=:did");
				hqlCount.append(" and device.id=:did");
				map.put("did", devId);
			}
			if (typeId != null) {
				hqlRow.append(" and alarmType.id=:tid");
				hqlCount.append(" and alarmType.id=:tid");
				map.put("tid", typeId);
			}
			if (personId != null) {
				hqlRow.append(" and device.acceptPerson.id=:pid");
				hqlCount.append(" and device.acceptPerson.id=:pid");
				map.put("pid", personId);
			}
			hqlRow.append(" order by recordDate");

			List<AlarmRecord> list = (List<AlarmRecord>) alarmRecordManager
					.pagedQuery(hqlRow.toString(), page, rows, map).getResult();
			int total = alarmRecordManager.getCount(hqlCount.toString(), map);

			List<AlarmRecordJSON> jsons = new ArrayList<AlarmRecordJSON>();
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
	public void queryActiveAlarmRecord() {
		try {
			StringBuilder hql = new StringBuilder();
			Map<String, Object> map = new HashMap<String, Object>();
			hql.append("from AlarmRecord where active=true");

			List<AlarmRecord> list = alarmRecordManager.find(hql.toString(),
					map);
			List<AlarmRecordJSON> jsons = new ArrayList<AlarmRecordJSON>();
			objToJSON(list, jsons);

			hql.delete(0, hql.length());
			hql.append("select count(*) from AlarmRecord where active=true");
			int total = alarmRecordManager.getCount(hql.toString(), map);

			map.put("rows", jsons);
			map.put("total", total);
			JSONTool.writeDataResult(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void queryActiveAlarmRecordByDevice() {
		try {
			StringBuilder hql = new StringBuilder();
			Map<String, Object> map = new HashMap<String, Object>();
			hql.append("from AlarmRecord rec where rec.active=true and rec.device.id=:id");
			map.put("id", devId);

			List<AlarmRecord> list = alarmRecordManager.find(hql.toString(),
					map);
			List<AlarmRecordJSON> jsons = new ArrayList<AlarmRecordJSON>();
			objToJSON(list, jsons);
			JSONTool.writeDataResult(jsons);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void queryMsgOfAlarmRecordById() {
		try {
			AlarmRecord alarmRecord = alarmRecordManager.findUniqueBy("id", id);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("data", alarmRecord.getMessage());
			JSONTool.writeDataResult(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void queryVibratingPosition() {
		try {
			String hqlRow = "from AlarmRecord a where a.active=true and a.device.deviceType.typeName='光纤'";
			String hqlCount = "select count(*) from AlarmRecord a where a.active=true and a.device.deviceType.typeName='光纤'";
			Map<String, Object> map = new HashMap<String, Object>();

			List<AlarmRecord> list = (List<AlarmRecord>) alarmRecordManager
					.pagedQuery(hqlRow, page, rows, map).getResult();
			List<AlarmRecordJSON> jsons = new ArrayList<AlarmRecordJSON>();
			objToJSON(list, jsons);

			int total = alarmRecordManager.getCount(hqlCount, map);

			map.clear();
			map.put("rows", jsons);
			map.put("total", total);

			JSONTool.writeDataResult(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void save() throws IOException {
		try {
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy_MM_dd_hh_MM_ss_");
			device = deviceManager.findUniqueBy("id", device.getId());
			model.setRecordCode(device.getDevCode() + "_"
					+ format.format(new Date()));
			model.setDevice(device);
			model.setDeviceCode(device.getDevCode());
			model.setDeviceTypeName(device.getDeviceType().getTypeName());
			model.setRecordDate(new Date());
			alarmRecordManager.save(model);

			SysLog log = new SysLog();
			log.setBusinessName("设备报警");
			log.setContent("新增报警信息：" + model.getRecordCode());
			log.setOperationType("add");
			if (null != objUser) {
				log.setCreateUser(objUser.getPrincipal().getName());
			}
			log.setCreateTime(new Date());
			logManager.save(log);

			JSONTool.writeMsgResult(true, "保存完成！");
		} catch (Exception e) {
			e.printStackTrace();
			JSONTool.writeMsgResult(false, "保存失败！");
		}
	}

	public void edit() {
		device = deviceManager.findUniqueBy("id", device.getId());
		acceptPerson = acceptPersonManager.findUniqueBy("id",
				acceptPerson.getId());
		AlarmRecord alarmRecord = alarmRecordManager.findUniqueBy("id",
				model.getId());
		if (null != device) {
			alarmRecord.setDevice(device);
		}
		alarmRecord.setMessage(model.getMessage());
		alarmRecordManager.save(alarmRecord);

		SysLog log = new SysLog();
		log.setBusinessName("设备报警");
		log.setContent("修改报警信息：" + model.getRecordCode());
		log.setOperationType("add");
		if (null != objUser) {
			log.setCreateUser(objUser.getPrincipal().getName());
		}
		log.setCreateTime(new Date());
		logManager.save(log);
	}

	@SuppressWarnings("unchecked")
	public void queryUnActiveAlarmRecordByTypeAndDt() {
		try {
			String[] codeArray = null;
			if (null != devCodes && devCodes.length() > 0) {
				codeArray = devCodes.split("&");
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			StringBuilder hql = new StringBuilder();
			Map<String, Object> map = new HashMap<String, Object>();

			hql.append("from AlarmRecord where active=false");
			if (StringUtils.isNotBlank(beginTime)) {
				hql.append(" and recordDate>=:begin");
				map.put("begin", dateFormat.parse(beginTime));
			}
			if (StringUtils.isNotBlank(endTime)) {
				hql.append(" and recordDate<=:end");
				map.put("end", dateFormat.parse(endTime));
			}
			if (null != codeArray && codeArray.length > 0) {
				hql.append(" and device.devCode in (:cds)");
				map.put("cds", codeArray);
			}
			if (StringUtils.isNotBlank(typeName)) {
				hql.append(" and device.deviceType.typeName=:tname");
				map.put("tname", typeName);
			}
			page = page <= 0 ? 1 : page;
			rows = rows <= 0 ? 10 : rows;
			List<AlarmRecord> list = (List<AlarmRecord>) alarmRecordManager
					.pagedQuery(hql.toString(), page, rows, map).getResult();
			List<AlarmRecordJSON> jsons = new ArrayList<AlarmRecordJSON>();
			objToJSON(list, jsons);

			hql.delete(0, hql.length());
			map.clear();
			hql.append("select count(*) from AlarmRecord where active=false");
			if (StringUtils.isNotBlank(beginTime)) {
				hql.append(" and recordDate>=:begin");
				map.put("begin", dateFormat.parse(beginTime));
			}
			if (StringUtils.isNotBlank(endTime)) {
				hql.append(" and recordDate<=:end");
				map.put("end", dateFormat.parse(endTime));
			}
			if (null != codeArray && codeArray.length > 0) {
				hql.append(" and device.devCode in (:cds)");
				map.put("cds", codeArray);
			}
			if (StringUtils.isNotBlank(typeName)) {
				hql.append(" and device.deviceType.typeName=:tname");
				map.put("tname", typeName);
			}
			int total = alarmRecordManager.getCount(hql.toString(), map);

			map.clear();
			map.put("total", total);
			map.put("rows", jsons);
			JSONTool.writeDataResult(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void queryActiveAlarmRecordByType() throws IOException {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String hqlRow = "from AlarmRecord where active=true and device.deviceType.typeName=:nam";
			String hqlCount = "select count(*) from AlarmRecord where active=true and device.deviceType.typeName=:nam";
			map.put("nam", typeName);

			List<AlarmRecord> list = (List<AlarmRecord>) alarmRecordManager
					.pagedQuery(hqlRow, page, rows, map).getResult();
			List<AlarmRecordJSON> jsons = new ArrayList<AlarmRecordJSON>();
			objToJSON(list, jsons);

			int total = alarmRecordManager.getCount(hqlCount, map);

			map.clear();
			map.put("total", total);
			map.put("rows", jsons);
			JSONTool.writeDataResult(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void queryActiveAlarmRecordByCode4Type() {
		try {
			String[] codeArray = null;
			if (null != devCodes && devCodes.length() > 0) {
				codeArray = devCodes.split("&");
			}
			StringBuilder hql = new StringBuilder();
			Map<String, Object> map = new HashMap<String, Object>();

			hql.append("from AlarmRecord where active=true");
			if (null != codeArray && codeArray.length > 0) {
				hql.append(" and device.devCode in (:cds)");
				map.put("cds", codeArray);
			}
			if (StringUtils.isNotBlank(typeName)) {
				hql.append(" and device.deviceType.typeName=:tname");
				map.put("tname", typeName);
			}
			page = page <= 0 ? 1 : page;
			rows = rows <= 0 ? 10 : rows;
			List<AlarmRecord> list = (List<AlarmRecord>) alarmRecordManager
					.pagedQuery(hql.toString(), page, rows, map).getResult();
			List<AlarmRecordJSON> jsons = new ArrayList<AlarmRecordJSON>();
			objToJSON(list, jsons);

			hql.delete(0, hql.length());
			map.clear();
			hql.append("select count(*) from AlarmRecord where active=true");
			if (null != codeArray && codeArray.length > 0) {
				hql.append(" and device.devCode in (:cds)");
				map.put("cds", codeArray);
			}
			if (StringUtils.isNotBlank(typeName)) {
				hql.append(" and device.deviceType.typeName=:tname");
				map.put("tname", typeName);
			}
			int total = alarmRecordManager.getCount(hql.toString(), map);

			map.clear();
			map.put("total", total);
			map.put("rows", jsons);
			JSONTool.writeDataResult(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void dealAlarmRecord() {
		try {
			if (null == model || null == model.getId()) {
				JSONTool.writeMsgResult(false, "您还没有选择要处理的报警记录！");
			} else {
				AlarmRecord alarmRecord = alarmRecordManager.findUniqueBy("id",
						model.getId());
				alarmRecord.setActive(false);
				alarmRecordManager.save(alarmRecord);
			}
		} catch (Exception e) {
			try {
				e.printStackTrace();
				JSONTool.writeMsgResult(false, "处理失败！");
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public void delete() {
		try {
			AlarmRecord rec = alarmRecordManager.findUniqueBy("id", id);

			SysLog log = new SysLog();
			log.setBusinessName("设备报警");
			log.setContent("删除报警信息：" + rec.getRecordCode());
			log.setOperationType("delete");
			if (null != objUser) {
				log.setCreateUser(objUser.getPrincipal().getName());
			}
			log.setCreateTime(new Date());

			alarmRecordManager.removeById(id);
			logManager.save(log);

			JSONTool.writeMsgResult(true, "删除成功！");
		} catch (DataIntegrityViolationException e) {
			try {
				JSONTool.writeMsgResult(false, "还有与该报警记录相关联的工单，不能删除!");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (Exception e1) {
			try {
				e1.printStackTrace();
				JSONTool.writeMsgResult(false, "删除失败！");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings({ "unchecked"})
	public void expToExcel() {
		try {

			StringBuilder hqlRow = new StringBuilder();
			Map<String, Object> map = new HashMap<String, Object>();

			hqlRow.append("from AlarmRecord where 1=1");

			if (null != active && active == 1) {
				hqlRow.append(" and  active=:active");
				map.put("active", true);
			}

			if (null != active && active == 0) {
				hqlRow.append(" and  active=:active");
				map.put("active", false);
			}

			if (devId != null) {
				hqlRow.append(" and device.id=:did");
				map.put("did", devId);
			}
			if (personId != null) {
				hqlRow.append(" and device.acceptPerson.id=:pid");
				map.put("pid", personId);
			}
			hqlRow.append(" order by recordDate");

			List<AlarmRecord> list = alarmRecordManager.find(hqlRow.toString(),map);

			doExpExcel(list);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void doExpExcel(List<AlarmRecord> list)
			throws UnsupportedEncodingException, FileNotFoundException,
			IOException {
		int rowIndex = 0;
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("设备告警记录表");
		HSSFRow row = sheet.createRow(rowIndex++);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

		HSSFCell cell = row.createCell(0);
		cell.setCellValue("记录编号");
		cell.setCellStyle(style);

		cell = row.createCell(1);
		cell.setCellValue("设备编号");
		cell.setCellStyle(style);

		cell = row.createCell(2);
		cell.setCellValue("设备名称");
		cell.setCellStyle(style);

		cell = row.createCell(3);
		cell.setCellValue("设备类型");
		cell.setCellStyle(style);

		cell = row.createCell(4);
		cell.setCellValue("告警项目");
		cell.setCellStyle(style);

		cell = row.createCell(5);
		cell.setCellValue("告警值");
		cell.setCellStyle(style);

		cell = row.createCell(6);
		cell.setCellValue("告警信息");
		cell.setCellStyle(style);

		cell = row.createCell(7);
		cell.setCellValue("日期");
		cell.setCellStyle(style);

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		for (AlarmRecord alarm : list) {
			row = sheet.createRow((int) rowIndex++);
			row.createCell(0).setCellValue(alarm.getRecordCode());
			if (null != alarm.getDevice()) {
				row.createCell(1).setCellValue(
						alarm.getDevice().getDevCode());
				row.createCell(2).setCellValue(
						alarm.getDevice().getDevName());
				if (null != alarm.getDevice().getDeviceType()) {
					row.createCell(3)
							.setCellValue(
									alarm.getDevice().getDeviceType()
											.getTypeName());
				}
			}
			row.createCell(4).setCellValue(alarm.getItemName());
			row.createCell(5).setCellValue(alarm.getItemValue());
			row.createCell(6).setCellValue(alarm.getMessage());
			if (null != alarm.getRecordDate()) {
				row.createCell(7).setCellValue(
						format.format(alarm.getRecordDate()));
			}
		}
		
		//String path = ServletActionContext.getRequest().getRealPath("/xls");
		String path=ServletActionContext.getServletContext().getRealPath("/xls");
		String filePath = path + "\\" + URLEncoder.encode("alarm.xls", "UTF-8");
		FileOutputStream fOut = new FileOutputStream(filePath);
		wb.write(fOut);
		fOut.flush();
		fOut.close();
	}
}
