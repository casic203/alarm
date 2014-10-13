package com.casic.alarm.web.alarm;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.jasig.cas.client.validation.Assertion;

import com.casic.alarm.domain.DatabaseMonitor;
import com.casic.alarm.manager.DatabaseMonitorManager;
import com.casic.alarm.manager.SysLogManager;
import com.casic.core.json.JSONTool;
import com.casic.core.struts2.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class DatabaseMonitorAction extends BaseAction implements
		ModelDriven<DatabaseMonitor>, Preparable {

	private int page;
	private int rows;

	private DatabaseMonitor model;
	private Assertion objUser;

	@Resource
	private DatabaseMonitorManager databaseMonitorManager;
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

	public void setModel(DatabaseMonitor model) {
		this.model = model;
	}

	public DatabaseMonitor getModel() {
		return this.model;
	}

	public void prepare() throws Exception {
		this.model = new DatabaseMonitor();
		objUser = (Assertion) ActionContext.getContext().getSession().get("_const_cas_assertion_");
	}

	public void setLogManager(SysLogManager logManager) {
		this.logManager = logManager;
	}

	public void query() {
		try {
			List<DatabaseMonitor> list = databaseMonitorManager.getAll();
			int total = databaseMonitorManager.getCount();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("total", total);
			map.put("rows", list);
			JSONTool.writeDataResult(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void save() {
		try {
			if (null == model) {
				JSONTool.writeMsgResult(false, "新增记录不能为空！");
			}
			databaseMonitorManager.save(model);
			JSONTool.writeMsgResult(true, "新增完成！");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				JSONTool.writeMsgResult(false, "新增失败！");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void edit() {
		try {
			if (null == model) {
				JSONTool.writeMsgResult(false, "编辑后的记录不能为空！");
			}
			DatabaseMonitor databaseMonitor = databaseMonitorManager
					.findUniqueBy("id", model.getId());
			databaseMonitor.setActive(model.getActive());
			databaseMonitor.setCurrent(model.getCurrent());
			databaseMonitor.setItem(model.getItem());
			databaseMonitor.setMail(model.getMail());
			databaseMonitor.setMaxValue(model.getMaxValue());
			databaseMonitor.setMinValue(model.getMinValue());
			databaseMonitor.setPersonName(model.getPersonName());
			databaseMonitor.setPhone(model.getPhone());
			databaseMonitor.setSqlString(model.getSqlString());
			databaseMonitorManager.save(model);
			JSONTool.writeMsgResult(true, "新增完成！");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				JSONTool.writeMsgResult(false, "新增失败！");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

}
