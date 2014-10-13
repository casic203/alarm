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

import com.casic.alarm.JSON.WorkSheetJSON;
import com.casic.alarm.domain.SysLog;
import com.casic.alarm.domain.WorkSheet;
import com.casic.alarm.manager.SysLogManager;
import com.casic.alarm.manager.WorkSheetManager;
import com.casic.core.json.JSONTool;
import com.casic.core.struts2.BaseAction;
import com.casic.core.util.StringUtils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class WorkSheetTakAction extends BaseAction implements ModelDriven<WorkSheet>,
		Preparable {
	private int page;
	private int rows;
	private WorkSheet model;
	private Assertion objUser;
	
	private Long devId;
	private String sheetNo;
	private String beginDate;
	private String endDate;

	private WorkSheetManager workSheetManager;
	private SysLogManager logManager;
	
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	public void prepare() throws Exception {
		page = page <= 0 ? 1 : page;
		rows = rows <= 0 ? 10 : rows;
		model = new WorkSheet();
		objUser = (Assertion) ActionContext.getContext().getSession().get("_const_cas_assertion_");
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

	public Long getDevId() {
		return devId;
	}

	public void setDevId(Long devId) {
		this.devId = devId;
	}

	public WorkSheet getModel() {
		return model;
	}

	public void setModel(WorkSheet model) {
		this.model = model;
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
	public void setWorkSheetManager(WorkSheetManager workSheetManager) {
		this.workSheetManager = workSheetManager;
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
			Map<String, Object> map = new HashMap<String, Object>();

			hqlRow.append("from WorkSheet where sheetStatus='批准' and charger=:cgr");
			hqlCount.append("select count(*) from WorkSheet where sheetStatus='批准' and charger=:cgr");
			map.put("cgr", objUser.getPrincipal().getName());

			if (devId != null) {
				hqlRow.append(" and device.id=:id");
				hqlCount.append(" and device.id=:id");
				map.put("id", devId);
			}
			if (StringUtils.isNotBlank(sheetNo)) {
				hqlRow.append(" and sheetNo like :sno");
				hqlCount.append(" and sheetNo like :sno");
				map.put("sno", "%" + model.getSheetNo() + "%");
			}
			if (StringUtils.isNotBlank(beginDate)) {
				hqlRow.append(" and beginDate=:beg");
				hqlCount.append(" and beginDate=:beg");
				map.put("beg", format.parse(beginDate));
			}
			if (StringUtils.isNotBlank(endDate)) {
				hqlRow.append(" and endDate=:end");
				hqlCount.append(" and endDate=:end");
				map.put("end", format.parse(endDate));
			}

			List<WorkSheet> list = (List<WorkSheet>) workSheetManager
					.pagedQuery(hqlRow.toString(), page, rows, map).getResult();
			List<WorkSheetJSON> jsons = new ArrayList<WorkSheetJSON>();
			objToJSON(list, jsons);
			int total = workSheetManager.getCount(hqlCount.toString(), map);

			map.clear();
			map.put("rows", jsons);
			map.put("total", total);
			JSONTool.writeDataResult(map);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void takeWorkSheet() {
		try {
			if (model.getId() != null) {
				model = workSheetManager.findUniqueBy("id", model.getId());
				model.setSheetStatus("领取");
				workSheetManager.save(model);
				JSONTool.writeMsgResult(true, "工单领取完成！");
				
				SysLog log=new SysLog();
				log.setBusinessName("工单领取操作");
				log.setContent("领取操作："+model.getSheetNo()+"-"+model.getDevice().getDevCode());
				log.setOperationType("add");
				log.setCreateUser(objUser.getPrincipal().getName());
				log.setCreateTime(new Date());
				logManager.save(log);
				
			} else {
				JSONTool.writeMsgResult(false, "您还没有选择要领取的工单！");
			}
		} catch (Exception e) {
			try {
				e.printStackTrace();
				JSONTool.writeMsgResult(false, "工单领取失败！");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

}
