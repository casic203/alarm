package com.casic.alarm.web.alarm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.casic.alarm.JSON.AlarmTypeJSON;
import com.casic.alarm.domain.AlarmType;
import com.casic.core.json.JSONTool;
import com.casic.core.page.Page;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

@Results({ @Result(name = "reload", location = "alarm-type.do", type = "redirect") })
public class AlarmTypeAction implements ModelDriven<AlarmType>, Preparable {

	private long id;
	private AlarmType model;
	private Page pages;
	private int page;
	private int rows;

	public String execute() {
		return "success";
	}

	public void prepare() throws Exception {
		this.model = new AlarmType();
	}

	public long getId() {
		return id;
	}

	public void objectToJSON(List<AlarmType> list, List<AlarmTypeJSON> jsons) {
		for (AlarmType type : list) {
			AlarmTypeJSON json = new AlarmTypeJSON();
			json.setId(type.getId());
			json.setAlarmCode(type.getAlarmCode());
			json.setAlarmName(type.getAlarmName());
			json.setActive(type.getActive());
			jsons.add(json);
		}
	}

	public void setId(long id) {
		this.id = id;
	}

	public AlarmType getModel() {
		return this.model;
	}

	public Page getPages() {
		return pages;
	}

	public void setPages(Page pages) {
		this.pages = pages;
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

	@SuppressWarnings("unchecked")
	private List<AlarmType> getAlarmTypeFromMem() {
		List<AlarmType> alarmTypeList = (List<AlarmType>)ActionContext.getContext().get("alarmTypeList");
		if(null == alarmTypeList) {
			String alarmCodeArray[] = {"0", "1", "2"};
			String alarmNameArray[] = {"短信", "邮件", "短信+邮件"};
			Long idArray[] = {0l, 1l, 2l};
			alarmTypeList = new ArrayList<AlarmType>();
			for (int i = 0;i < alarmCodeArray.length; i ++) {
				AlarmType telAlarmType = new AlarmType();
				telAlarmType.setActive(true);
				telAlarmType.setAlarmCode(alarmCodeArray[i]);
				telAlarmType.setAlarmName(alarmNameArray[i]);
				telAlarmType.setId(idArray[i]);
				alarmTypeList.add(telAlarmType);
			}
			ActionContext.getContext().put("alarmTypeList", alarmTypeList);
		} 
		return alarmTypeList;
	}
	
	public void queryType() throws IOException {
		List<AlarmType> alarmTypes = getAlarmTypeFromMem();
		List<AlarmTypeJSON> jsons = new ArrayList<AlarmTypeJSON>();
		for (AlarmType type : alarmTypes) {
			objToJSON(jsons, type);
		}
		JSONTool.writeDataResult(jsons);
	}

//	@SuppressWarnings("unchecked")
//	public void save() throws IOException {
//		try {
//			String hql = "from AlarmType where alarmCode=:code or alarmName=:nam";
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("code", model.getAlarmCode());
//			map.put("nam", model.getAlarmName());
//			List<AlarmType> types = manager.find(hql, map);
//			if (null != types && types.size() > 0) {
//				JSONTool.writeMsgResult(false, "报警方式的编号或者名称存在重复！");
//			} else {
//				manager.save(model);
//				JSONTool.writeMsgResult(true, "保存完成！");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			JSONTool.writeMsgResult(false, "保存失败！");
//		}
//	}

//	@SuppressWarnings("unchecked")
//	public void delete() throws IOException {
//		try {
//			Long id = model.getId();
//			String hql = "from ContactBook b where b.active=true and b.alarmType.id=:id";
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("id", id);
//			List<ContactBook> books = bookManager.find(hql, map);
//			if (null != books && books.size() > 0) {
//				JSONTool.writeMsgResult(false, "还有与此报警方式关联的通讯录，无法删除该报警方式！");
//			} else {
//				model = manager.findUniqueBy("id", id);
//				model.setActive(false);
//				manager.save(model);
//				JSONTool.writeMsgResult(true, "删除完成！");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			JSONTool.writeMsgResult(false, "删除失败！");
//		}
//	}

//	public void edit() {
//		try {
//			AlarmType tmp = new AlarmType();
//			tmp = manager.findUniqueBy("id", model.getId());
//			tmp.setAlarmCode(model.getAlarmCode());
//			tmp.setAlarmName(model.getAlarmName());
//			manager.save(tmp);
//			JSONTool.writeMsgResult(true, "编辑完成");
//		} catch (Exception e) {
//			try {
//				e.printStackTrace();
//				JSONTool.writeMsgResult(false, "编辑失败！");
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
//		}
//	}

	private void objToJSON(List<AlarmTypeJSON> jsons, AlarmType type) {
		AlarmTypeJSON json = new AlarmTypeJSON();
		json.setAlarmCode(type.getAlarmCode());
		json.setAlarmName(type.getAlarmName());
		json.setActive(type.getActive());
		json.setId(type.getId());
		jsons.add(json);
	}

	// 获取某个人还没有的通讯方式
//	@SuppressWarnings("unchecked")
//	public void queryUnAllotAlarmTypeByPerson() {
//		try {
//			StringBuilder hql = new StringBuilder();
//			Map<String, Object> map = new HashMap<String, Object>();
//
//			hql.append("from AlarmType a where a.id not in (select b.alarmType.id from ContactBook b where b.acceptPerson.id=:id)");
//			map.put("id", id);
//
//			List<AlarmType> list = manager.find(hql.toString(), map);
//			List<AlarmTypeJSON> jsons = new ArrayList<AlarmTypeJSON>();
//			objectToJSON(list, jsons);
//			JSONTool.writeDataResult(jsons);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

}
