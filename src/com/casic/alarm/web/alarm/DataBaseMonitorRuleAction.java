package com.casic.alarm.web.alarm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.jasig.cas.client.validation.Assertion;
import org.springframework.jdbc.core.JdbcTemplate;

import com.casic.alarm.domain.DataBaseMonitorRule;
import com.casic.alarm.domain.DatabaseBackupCycle;
import com.casic.alarm.domain.SysLog;
import com.casic.alarm.form.DataBaseMonitorResult;
import com.casic.alarm.manager.DataBaseMonitorRuleManager;
import com.casic.alarm.manager.DatabaseBackupCycleManager;
import com.casic.alarm.manager.SysLogManager;
import com.casic.core.db.BackupSet;
import com.casic.core.json.JSONTool;
import com.casic.core.struts2.BaseAction;
import com.casic.core.util.StringUtils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class DataBaseMonitorRuleAction extends BaseAction implements
		ModelDriven<DataBaseMonitorRule>, Preparable {

	private int page;
	private int rows;
	private DatabaseBackupCycle backupCycle;
	private DataBaseMonitorRule model;
	private Assertion objUser;

	@Resource
	private DataBaseMonitorRuleManager ruleManager;
	@Resource
	private DatabaseBackupCycleManager cycleManager;
	@Resource
	private SysLogManager logManager;

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

	public DatabaseBackupCycle getBackupCycle() {
		return backupCycle;
	}

	public void setBackupCycle(DatabaseBackupCycle backupCycle) {
		this.backupCycle = backupCycle;
	}

	public DataBaseMonitorRule getModel() {
		return model;
	}

	public void setModel(DataBaseMonitorRule model) {
		this.model = model;
	}

	public void setRuleManager(DataBaseMonitorRuleManager ruleManager) {
		this.ruleManager = ruleManager;
	}
	
	public void setCycleManager(DatabaseBackupCycleManager cycleManager) {
		this.cycleManager = cycleManager;
	}

	public void setLogManager(SysLogManager logManager) {
		this.logManager = logManager;
	}

	public void prepare() throws Exception {
		page = page <= 0 ? 1 : page;
		rows = rows <= 0 ? 10 : rows;
		this.model = new DataBaseMonitorRule();
		this.backupCycle = new DatabaseBackupCycle();
		objUser = (Assertion) ActionContext.getContext().getSession().get("_const_cas_assertion_");
	}

	@SuppressWarnings("unchecked")
	public void query() {
		try {
			StringBuilder hqlRow = new StringBuilder();
			StringBuilder hqlCnt = new StringBuilder();
			Map<String, Object> map = new HashMap<String, Object>();

			hqlRow.append("from DataBaseMonitorRule where active=true");
			hqlCnt.append("select count(*) from DataBaseMonitorRule where active=true");

			if (StringUtils.isNotBlank(model.getName())) {
				hqlRow.append(" and name like :nam");
				hqlCnt.append(" and name like :nam");
				map.put("nam", "%" + model.getName() + "%");
			}

			List<DataBaseMonitorRule> list = (List<DataBaseMonitorRule>) ruleManager
					.pagedQuery(hqlRow.toString(), page, rows, map).getResult();
			int total = ruleManager.getCount(hqlCnt.toString(), map);

			map.clear();
			map.put("total", total);
			map.put("rows", list);
			JSONTool.writeDataResult(map);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void save() {
		try {
			ruleManager.save(model);
			
			SysLog log=new SysLog();
			log.setBusinessName("数据库监控规则维护");
			log.setContent("新增监控规则："+model.getName());
			log.setOperationType("add");
			log.setCreateUser(objUser.getPrincipal().getName());
			log.setCreateTime(new Date());
			logManager.save(log);
			
			JSONTool.writeMsgResult(true, "保存完成！");
		} catch (Exception e) {
			try {
				e.printStackTrace();
				JSONTool.writeMsgResult(false, "保存失败！");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void edit() {
		try {
			DataBaseMonitorRule rule = ruleManager.load(model.getId());
			if (null != rule) {
				rule.setName(model.getName());
				rule.setMax(model.getMax());
				rule.setMin(model.getMin());
				rule.setSql(model.getSql());
				ruleManager.save(rule);
				
				SysLog log=new SysLog();
				log.setBusinessName("数据库监控规则维护");
				log.setContent("修改监控规则："+model.getName());
				log.setOperationType("edit");
				log.setCreateUser(objUser.getPrincipal().getName());
				log.setCreateTime(new Date());
				logManager.save(log);
				
				JSONTool.writeMsgResult(true, "编辑完成！");
			} else {
				JSONTool.writeMsgResult(false, "没有找到要编辑的数据！");
			}
		} catch (IOException e) {
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
			model = ruleManager.load(model.getId());
			model.setActive(false);
			ruleManager.save(model);
			
			SysLog log=new SysLog();
			log.setBusinessName("数据库监控规则维护");
			log.setContent("删除监控规则："+model.getName());
			log.setOperationType("delete");
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

	@SuppressWarnings("unchecked")
	public void getResult() {
		try {
			StringBuilder hqlRow = new StringBuilder();
			StringBuilder hqlCnt = new StringBuilder();
			Map<String, Object> map = new HashMap<String, Object>();
			JdbcTemplate template = ruleManager.getJdbcTemplate();
			List<DataBaseMonitorResult> results = new ArrayList<DataBaseMonitorResult>();

			hqlRow.append("from DataBaseMonitorRule where active=true");
			hqlCnt.append("select count(*) from DataBaseMonitorRule where active=true");

			List<DataBaseMonitorRule> rules = (List<DataBaseMonitorRule>) ruleManager
					.pagedQuery(hqlRow.toString(), page, rows, map).getResult();
			int total = ruleManager.getCount(hqlCnt.toString(), map);

			for (DataBaseMonitorRule rule : rules) {
				double current = template.queryForObject(rule.getSql(),
						new Object[] {}, java.lang.Double.class);
				DataBaseMonitorResult result = new DataBaseMonitorResult();
				result.setName(rule.getName());
				result.setMax(rule.getMax());
				result.setMin(rule.getMin());
				if (current < rule.getMin() || current > rule.getMax()) {
					result.setStatus("异常");
				} else {
					result.setStatus("正常");
				}
				result.setCurrent(current);
				results.add(result);
			}
			
			map.clear();
			map.put("total", total);
			map.put("rows", results);
			JSONTool.writeDataResult(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void doSetCycle() {
		try {
			List<DatabaseBackupCycle> list=cycleManager.getAll();
			if(list.size()>0){
				
			}
			cycleManager.removeAll();
			cycleManager.save(backupCycle);
			BackupSet.setParm4Oracle("AutoBackup",backupCycle.getPath(), backupCycle.getCycle());
			JSONTool.writeMsgResult(true, "设置成功！");
		} catch (Exception e) {
			try {
				e.printStackTrace();
				JSONTool.writeMsgResult(false, "设置失败！");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public void querySetCycle(){
		try {
			List<DatabaseBackupCycle> list=new ArrayList<DatabaseBackupCycle>() ;
			list=cycleManager.getAll();
			Map<String, Object>map=new HashMap<String, Object>();
			if(list.size()>0){
				map.put("_cycle", list.get(0).getCycle());
				map.put("_path", list.get(0).getPath());
			}else{
				map.put("_cycle", "");
				map.put("_path", "");
			}
			JSONTool.writeDataResult(map);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
