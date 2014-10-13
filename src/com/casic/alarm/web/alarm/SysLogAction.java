package com.casic.alarm.web.alarm;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.casic.alarm.domain.SysLog;
import com.casic.alarm.form.SysLogForm;
import com.casic.alarm.manager.SysLogManager;
import com.casic.core.json.JSONTool;
import com.casic.core.struts2.BaseAction;
import com.casic.core.util.StringUtils;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class SysLogAction extends BaseAction implements ModelDriven<SysLog>,
		Preparable {

	private int page;
	private int rows;

	private String person;
	private Date beginTime;
	private Date endTime;

	private SysLog model;

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

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		if (StringUtils.isNotBlank(beginTime)) {
			this.beginTime = format.parse(beginTime);
		} else {
			this.beginTime = null;
		}
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		if (StringUtils.isNotBlank(endTime)) {
			this.endTime = format.parse(endTime);
		} else {
			this.endTime = null;
		}
	}

	public SysLog getModel() {
		return model;
	}

	public void setModel(SysLog model) {
		this.model = model;
	}

	@Resource
	public void setLogManager(SysLogManager logManager) {
		this.logManager = logManager;
	}

	private List<SysLogForm> domainToForm(List<SysLog> list) {
		List<SysLogForm> forms = new ArrayList<SysLogForm>();
		for (SysLog log : list) {
			SysLogForm form = new SysLogForm();
			form.setId(log.getId());
			form.setBusinessName(log.getBusinessName());
			form.setContent(log.getContent());
			form.setCreateTime(log.getCreateTime());
			form.setCreateUser(log.getCreateUser());
			form.setOperationType(log.getOperationType());
			forms.add(form);
		}
		return forms;
	}

	public void prepare() throws Exception {
		this.model = new SysLog();
		page = page <= 0 ? 1 : page;
		rows = rows <= 0 ? 10 : rows;
	}

	@SuppressWarnings("unchecked")
	public void query() {
		try {
			StringBuilder rHql = new StringBuilder();
			StringBuilder cHql = new StringBuilder();
			Map<String, Object> map = new java.util.HashMap<String, Object>();

			rHql.append("from SysLog where 1=1");
			cHql.append("select count(*) from SysLog where 1=1");

			if (StringUtils.isNotBlank(person)) {
				rHql.append(" and createUser=:usr");
				cHql.append(" and createUser=:usr");
				map.put("usr", person);
			}
			if (null != beginTime) {
				rHql.append(" and createTime>=:begin");
				cHql.append(" and createTime>=:begin");
				map.put("begin", beginTime);
			}
			if (null != endTime) {
				rHql.append(" and createTime<=:end");
				cHql.append(" and createTime<=:end");
				map.put("end", endTime);
			}

			rHql.append(" order by createTime desc");

			List<SysLog> list = (List<SysLog>) logManager.pagedQuery(
					rHql.toString(), page, rows, map).getResult();
			int total = logManager.getCount(cHql.toString(), map);

			map.clear();
			map.put("total", total);
			map.put("rows", domainToForm(list));
			JSONTool.writeDataResult(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete() {
		try {
			logManager.removeById(model.getId());
			JSONTool.writeMsgResult(true, "删除成功！");
		} catch (Exception e) {
			try {
				e.printStackTrace();
				JSONTool.writeMsgResult(false, "删除失败！");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

}
