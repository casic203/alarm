package com.casic.alarm.web.alarm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.jasig.cas.client.validation.Assertion;

import com.casic.alarm.JSON.WorkSheetFeedbackJSON;
import com.casic.alarm.JSON.WorkSheetJSON;
import com.casic.alarm.domain.DevPos;
import com.casic.alarm.domain.PosDma;
import com.casic.alarm.domain.SysLog;
import com.casic.alarm.domain.WorkSheet;
import com.casic.alarm.domain.WorkSheetFeedback;
import com.casic.alarm.manager.DMAInfoManage;
import com.casic.alarm.manager.DevPosManage;
import com.casic.alarm.manager.PosDmaManager;
import com.casic.alarm.manager.SysLogManager;
import com.casic.alarm.manager.WorkSheetFeedbackManager;
import com.casic.alarm.manager.WorkSheetManager;
import com.casic.alarm.utils.Constants;
import com.casic.core.json.JSONTool;
import com.casic.core.struts2.BaseAction;
import com.casic.core.util.StringUtils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

/**
 * 维修反馈action
 * @author liuxin
 *
 */
public class WorkSheetFeedbackAction extends BaseAction implements ModelDriven<WorkSheet>,
Preparable {
	
	private int page;
	private int rows;
	private Long devId;
	private WorkSheet model = new WorkSheet();;
	
	private Long deviceId;
	private Assertion objUser;
	private String alarmReason;
	private String lossValues;
	private Long dmaId;
	private Long posId;
	private String solution;
	private int isAder;
	private Long workSheetId;
    private Long workSheetFeedbackId;
	
	@Resource
	private WorkSheetFeedbackManager workSheetFeedbackManager;

	@Resource
	private WorkSheetManager workSheetManager;
	
	@Resource
	private DevPosManage devPosManage;
	
	@Resource
	private PosDmaManager posDmaManager;
	
	@Resource
	private DMAInfoManage dmaInfoManage;
	
	@Resource
	private SysLogManager logManager;

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

			hqlRow.append("from WorkSheet where (sheetStatus='领取' or sheetStatus='已反馈') and charger=:chg");
			hqlCount.append("select count(*) from WorkSheet where (sheetStatus='领取' or sheetStatus='已反馈') and charger=:chg");
			map.put("chg", objUser.getPrincipal().getName());

			if (devId != null) {
				hqlRow.append(" and device.id=:id");
				hqlCount.append(" and device.id=:id");
				map.put("id", devId);
			}
			if (StringUtils.isNotBlank(model.getSheetNo())) {
				hqlRow.append(" and sheetNo like :sno");
				hqlCount.append(" and sheetNo like :sno");
				map.put("sno", "%" + model.getSheetNo() + "%");
			}
			if (model.getBeginDate() != null) {
				hqlRow.append(" and beginDate>=:beg");
				hqlCount.append(" and beginDate>=:beg");
				map.put("beg", model.getBeginDate());
			}
			if (model.getEndDate() != null) {
				hqlRow.append(" and endDate<=:end");
				hqlCount.append(" and endDate<=:end");
				map.put("end", model.getEndDate());
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
	
	/**
	 * 获取反馈信息
	 */
	@SuppressWarnings("unchecked")
	public void queryFeedback() {
		Long workSheetId = model.getId();
		String hql = "from WorkSheetFeedback feedback" +
				" where feedback.workSheet.id=:workSheetId" +
				" and feedback.deleteFlag=:deleteFlag";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Boolean isLeakage = false;
		
		/**
		 * 查询此工单是否是管道漏损
		 */
		WorkSheet workSheet = (WorkSheet)workSheetManager.get(workSheetId);
		Long deviceId = 0l;
		String deviceTypeName = workSheet.getDevice().getDeviceType().getTypeName();
		for (String leakageDevice : Constants.LEAKAGE_DEVICE_COLLECTION) {
			if(deviceTypeName.equals(leakageDevice)) {
				isLeakage = true;
			}
		}
		deviceId = workSheet.getDevice().getId();
		resultMap.put("deviceId", deviceId);
		resultMap.put("isLeakage", isLeakage);
		
		/**
		 * 查询反馈信息
		 */
		paramMap.put("workSheetId", workSheetId);
		paramMap.put("deleteFlag", Boolean.FALSE);
		List<WorkSheetFeedback> workSheetFeedbackList = workSheetFeedbackManager.find(hql, paramMap);
		if(workSheetFeedbackList.size() != 0) {
		    for (WorkSheetFeedback workSheetFeedback : workSheetFeedbackList) {
			    WorkSheetFeedbackJSON feedbackJSON = new WorkSheetFeedbackJSON(workSheetFeedback);
				resultMap.put("data", feedbackJSON);
				resultMap.put("havaData", true);
			    break;
		    }
		} else {
			resultMap.put("havaData", false);
		}

		try {
			JSONTool.writeDataResult(resultMap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 通过设备ID查询对应监测点
	 */
	@SuppressWarnings("unchecked")
	public void getPosByDeviceId() {
		String hql = "from DevPos devPos where devPos.device.id=:deviceId";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		
		paramMap.put("deviceId", deviceId);
		List<DevPos> devPosList = devPosManage.find(hql, paramMap);
		for (DevPos devPos : devPosList) {
			Map<String, Object> itemMap = new HashMap<String, Object>();
			itemMap.put("id", devPos.getPositionInfo().getID());
			itemMap.put("elementName", devPos.getPositionInfo().getName());
			data.add(itemMap);
		}
		
		try {
			JSONTool.writeDataResult(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 通过监测点ID查询对应分区
	 */
	public void getDMAByPosId() {
		String hql = "from PosDma posDma where posDma.positionInfo.ID=:posId";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		
		paramMap.put("posId", posId);
		List<PosDma> posDmaList = posDmaManager.find(hql, paramMap);
		for (PosDma posDma : posDmaList) {
			Map<String, Object> itemMap = new HashMap<String, Object>();
			itemMap.put("id", posDma.getDmaInfo().getID());
			itemMap.put("elementName", posDma.getDmaInfo().getName());
			data.add(itemMap);
		}
		
		try {
			JSONTool.writeDataResult(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 提交维修反馈信息
	 */
	public void submitFeedback() {
		WorkSheetFeedback workSheetFeedback = null;
        if(null != workSheetFeedbackId && !"".equals(workSheetFeedbackId)) {
        	workSheetFeedback = workSheetFeedbackManager.load(workSheetFeedbackId);
        } else {
        	workSheetFeedback = new WorkSheetFeedback();
		}
		
		try {
			if(isAder == 1) { //埃德尔设备
				workSheetFeedback.setIsLeakage(Boolean.TRUE);        //是否漏损(用于埃德尔)
				workSheetFeedback.setLeakageReason(alarmReason);  
				workSheetFeedback.setLossValues(Double.parseDouble(lossValues));
				workSheetFeedback.setFeedbackDate(new Date());
				workSheetFeedback.setDmaInfo(dmaInfoManage.load(dmaId));
			} else {          //非埃德尔设备
				workSheetFeedback.setIsLeakage(Boolean.FALSE);
			}
			
			Long sheetNo = workSheetId;
			WorkSheet workSheet = workSheetManager.load(sheetNo);
			workSheet.setSheetStatus("已反馈");
			workSheetManager.save(workSheet);
			workSheetManager.flush();
			
			workSheetFeedback.setWorkSheet(workSheet);
			workSheetFeedback.setAlarmReason(alarmReason);
	        workSheetFeedback.setSolution(solution);
	        workSheetFeedback.setInsertDate(new Date());
	        workSheetFeedback.setUpdateDate(new Date());
	        workSheetFeedback.setDeleteFlag(Boolean.FALSE);
	        

	        workSheetFeedbackManager.save(workSheetFeedback);
	        
	        SysLog log=new SysLog();
			log.setBusinessName("工单反馈");
			log.setContent("反馈工单："+model.getSheetNo()+"-"+model.getDevice().getDevCode());
			log.setOperationType("edit");
			try {
				if(null == objUser) {
					objUser = (Assertion) ActionContext.getContext().getSession().get("_const_cas_assertion_");
				}
			    log.setCreateUser(objUser.getPrincipal().getName());
			} catch(Exception e) {
				log.setCreateUser("");
			}
			log.setCreateTime(new Date());
			logManager.save(log);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        try {
			JSONTool.writeMsgResult(true, "反馈成功！");
		} catch (IOException e) {
			e.printStackTrace();
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
	
	public void prepare() throws Exception {
		page = page <= 0 ? 1 : page;
		rows = rows <= 0 ? 10 : rows;
		objUser = (Assertion) ActionContext.getContext().getSession().get("_const_cas_assertion_");
	}

	public void setWorkSheetFeedbackManager(
			WorkSheetFeedbackManager workSheetFeedbackManager) {
		this.workSheetFeedbackManager = workSheetFeedbackManager;
	}

	public void setWorkSheetManager(WorkSheetManager workSheetManager) {
		this.workSheetManager = workSheetManager;
	}

	public String getAlarmReason() {
		return alarmReason;
	}

	public void setAlarmReason(String alarmReason) {
		this.alarmReason = alarmReason;
	}

	public String getLossValues() {
		return lossValues;
	}

	public void setLossValues(String lossValues) {
		this.lossValues = lossValues;
	}

	public String getSolution() {
		return solution;
	}

	public void setSolution(String solution) {
		this.solution = solution;
	}

	public Long getDmaId() {
		return dmaId;
	}

	public void setDmaId(Long dmaId) {
		this.dmaId = dmaId;
	}

	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	public Long getPosId() {
		return posId;
	}

	public void setPosId(Long posId) {
		this.posId = posId;
	}

	public DevPosManage getDevPosManage() {
		return devPosManage;
	}

	public void setDevPosManage(DevPosManage devPosManage) {
		this.devPosManage = devPosManage;
	}

	public PosDmaManager getPosDmaManager() {
		return posDmaManager;
	}

	public void setPosDmaManager(PosDmaManager posDmaManager) {
		this.posDmaManager = posDmaManager;
	}

	public int getIsAder() {
		return isAder;
	}

	public void setIsAder(int isAder) {
		this.isAder = isAder;
	}

	public DMAInfoManage getDmaInfoManage() {
		return dmaInfoManage;
	}

	public void setDmaInfoManage(DMAInfoManage dmaInfoManage) {
		this.dmaInfoManage = dmaInfoManage;
	}

	public Long getWorkSheetId() {
		return workSheetId;
	}

	public void setWorkSheetId(Long workSheetId) {
		this.workSheetId = workSheetId;
	}

	public Long getWorkSheetFeedbackId() {
		return workSheetFeedbackId;
	}

	public void setWorkSheetFeedbackId(Long workSheetFeedbackId) {
		this.workSheetFeedbackId = workSheetFeedbackId;
	}

	public void setLogManager(SysLogManager logManager) {
		this.logManager = logManager;
	}
	
}
