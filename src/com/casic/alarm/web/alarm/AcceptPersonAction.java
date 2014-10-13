package com.casic.alarm.web.alarm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.json.annotations.JSON;
import org.jasig.cas.client.validation.Assertion;

import com.casic.alarm.JSON.AcceptPersonJSON;
import com.casic.alarm.domain.AcceptPerson;
import com.casic.alarm.domain.Device;
import com.casic.alarm.domain.SysLog;
import com.casic.alarm.manager.AcceptPersonManager;
import com.casic.alarm.manager.DeviceManager;
import com.casic.alarm.manager.SysLogManager;
import com.casic.core.json.JSONTool;
import com.casic.core.struts2.BaseAction;
import com.casic.core.util.StringUtils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

@Results({
		@org.apache.struts2.convention.annotation.Result(name = "reload", location = "accept-person.do?operationMode=RETRIEVE", type = "redirect"),
		@org.apache.struts2.convention.annotation.Result(name = "json", type = "json", params = {
				"root", "jsonResults" }) })
public class AcceptPersonAction extends BaseAction implements
		ModelDriven<AcceptPerson>, Preparable {

	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = -2152313913008792904L;
	private long id;
	private int page;
	private int rows;
	private String devs;
	private String jsonResults;

	private AcceptPerson model;
	private Assertion objUser;

	private AcceptPersonManager acceptPersonManager;
	private DeviceManager deviceManager;
	private SysLogManager logManager;
	
	public void prepare() throws Exception {
		this.model = new AcceptPerson();
		page = page <= 0 ? 1 : page;
		rows = rows <= 0 ? 10 : rows;
		objUser = (Assertion) ActionContext.getContext().getSession().get("_const_cas_assertion_");
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public AcceptPerson getModel() {
		return this.model;
	}

	public void setModel(AcceptPerson model) {
		this.model = model;
	}

	public AcceptPersonManager getManager() {
		return acceptPersonManager;
	}

	@Resource
	public void setManager(AcceptPersonManager manager) {
		this.acceptPersonManager = manager;
	}

	@Resource
	public void setDeviceManager(DeviceManager deviceManager) {
		this.deviceManager = deviceManager;
	}
	
	@Resource
	public void setLogManager(SysLogManager logManager) {
		this.logManager = logManager;
	}

	@JSON
	public String getJsonResults() {
		return jsonResults;
	}

	public void setJsonResults(String jsonResults) {
		this.jsonResults = jsonResults;
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

	public String getDevs() {
		return devs;
	}

	public void setDevs(String devices) {
		this.devs = devices;
	}

	public String execute() {
		return "success";
	}

	private void objToJSON(List<AcceptPerson> acceptPersons,
			List<AcceptPersonJSON> jsons) {
		for (AcceptPerson person : acceptPersons) {
			AcceptPersonJSON json = new AcceptPersonJSON();
			json.setId(person.getId());
			json.setActive(person.getActive());
			json.setPersonCode(person.getPersonCode());
			json.setPersonName(person.getPersonName());
			json.setTelePhone(person.getTelePhone());
			json.setEmail(person.getEmail());
			jsons.add(json);
		}
	}

	@SuppressWarnings("unchecked")
	public void query() throws IOException {
		try {
			StringBuilder hqlRow = new StringBuilder();
			StringBuilder hqlCount = new StringBuilder();
			Map<String, Object> map = new HashMap<String, Object>();

			hqlRow.append("from AcceptPerson where active=true");
			hqlCount.append("select count(*) from AcceptPerson where active=true");

			if (StringUtils.isNotBlank(model.getPersonCode())) {
				hqlRow.append(" and personCode=:code");
				hqlCount.append(" and personCode=:code");
				map.put("code", model.getPersonCode());
			}
			if (StringUtils.isNotBlank(model.getPersonName())) {
				hqlRow.append(" and personName like :name");
				hqlCount.append(" and personName like :name");
				map.put("name", "%" + model.getPersonName() + "%");
			}
			hqlRow.append(" order by personCode");

			List<AcceptPerson> acceptPersons = (List<AcceptPerson>) acceptPersonManager
					.pagedQuery(hqlRow.toString(), page, rows, map).getResult();
			int total = acceptPersonManager.getCount(hqlCount.toString(), map);

			List<AcceptPersonJSON> jsons = new ArrayList<AcceptPersonJSON>();
			objToJSON(acceptPersons, jsons);

			map.clear();
			map.put("total", total);
			map.put("rows", jsons);
			JSONTool.writeDataResult(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void queryPerson() throws IOException {
		try {
			List<AcceptPerson> acceptPersons = acceptPersonManager.findBy("active", true);
			List<AcceptPersonJSON> jsons = new ArrayList<AcceptPersonJSON>();
			objToJSON(acceptPersons, jsons);
			JSONTool.writeDataResult(jsons);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void save() throws IOException {
		try {
			if (null != acceptPersonManager.findUniqueBy("personCode",model.getPersonCode())) {
				JSONTool.writeMsgResult(false,	"接警人编号重复：" + model.getPersonCode());
			} else {
				acceptPersonManager.save(model);
				
				SysLog log=new SysLog();
				log.setBusinessName("接警人维护");
				log.setContent("新增接警人："+model.getPersonCode()+"-"+model.getPersonName());
				log.setOperationType("add");
				log.setCreateUser(objUser.getPrincipal().getName());
				log.setCreateTime(new Date());
				logManager.save(log);
				
				JSONTool.writeMsgResult(true, "保存成功！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			JSONTool.writeMsgResult(false, "保存失败！");
		}
	}

	public void edit() {
		try {
			AcceptPerson person = acceptPersonManager.findUniqueBy("id",model.getId());
			if (null != person) {
				person.setPersonCode(model.getPersonCode());
				person.setPersonName(model.getPersonName());
				person.setTelePhone(model.getTelePhone());
				person.setEmail(model.getEmail());
				acceptPersonManager.save(person);
				
				SysLog log=new SysLog();
				log.setBusinessName("接警人维护");
				log.setContent("修改接警人："+model.getPersonCode()+"-"+model.getPersonName());
				log.setOperationType("edit");
				log.setCreateUser(objUser.getPrincipal().getName());
				log.setCreateTime(new Date());
				logManager.save(log);
			}
			JSONTool.writeMsgResult(true, "修改完成！");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				JSONTool.writeMsgResult(false, "修改失败！");
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void delete() throws IOException {
		try {
			Long id = model.getId();
			String hql = "from Device d where d.active=true and d.acceptPerson.id=:id";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", id);
			List<Device> devices = deviceManager.find(hql, map);
			if (null != devices && devices.size() > 0) {
				JSONTool.writeMsgResult(false, "此人还有关联的设备，请解除关联的设备在删除！");
			} else {
				acceptPersonManager.removeById(id);
				
				SysLog log=new SysLog();
				log.setBusinessName("接警人维护");
				log.setContent("删除接警人："+model.getPersonCode()+"-"+model.getPersonName());
				log.setOperationType("delete");
				log.setCreateUser(objUser.getPrincipal().getName());
				log.setCreateTime(new Date());
				logManager.save(log);
				
				JSONTool.writeMsgResult(true, "删除成功！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			JSONTool.writeMsgResult(true, "删除失败！");
		}
	}
}
