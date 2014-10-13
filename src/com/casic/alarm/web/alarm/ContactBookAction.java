//package com.casic.alarm.web.alarm;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.annotation.Resource;
//
//import org.apache.struts2.ServletActionContext;
//import org.apache.struts2.convention.annotation.Result;
//import org.apache.struts2.convention.annotation.Results;
//
//import com.casic.alarm.JSON.AlarmTypeJSON;
//import com.casic.alarm.JSON.ContactBookJSON;
//import com.casic.alarm.domain.AcceptPerson;
//import com.casic.alarm.domain.AlarmType;
//import com.casic.alarm.domain.ContactBook;
//import com.casic.alarm.manager.AcceptPersonManager;
//import com.casic.alarm.manager.AlarmTypeManager;
//import com.casic.alarm.manager.ContactBookManager;
//import com.casic.core.json.JSONTool;
//import com.casic.core.mapper.JsonMapper;
//import com.opensymphony.xwork2.ModelDriven;
//import com.opensymphony.xwork2.Preparable;
//
//@Results({
//		@Result(name = "reload", location = "contact-book.do?operationMode=RETRIEVE", type = "redirect"),
//		@Result(name = "new", location = "contact-book-new?operationMode=NEW", type = "redirect"),
//		@Result(name = "edit", location = "contact-book-eidt.do?operationMode=EDIT&id=${id}", type = "redirect") })
//public class ContactBookAction implements ModelDriven<ContactBook>, Preparable {
//
//	private long id;
//	private long personId;
//	private long typeId;
//	private int page;
//	private int rows;
//	private ContactBook model;
//	private AcceptPerson acceptPerson;
//	private AlarmType alarmType;
//	private ContactBookManager manager;
//	private AcceptPersonManager acceptPersonManager;
//	private AlarmTypeManager alarmTypeManager;
//
//	public String execute() {
//		return "success";
//	}
//
//	public void prepare() throws Exception {
//		this.model = new ContactBook();
//	}
//
//	public long getId() {
//		return id;
//	}
//
//	public void setId(long id) {
//		this.id = id;
//	}
//
//	public long getPersonId() {
//		return personId;
//	}
//
//	public void setPersonId(long personId) {
//		this.personId = personId;
//	}
//
//	public long getTypeId() {
//		return typeId;
//	}
//
//	public void setTypeId(long typeId) {
//		this.typeId = typeId;
//	}
//
//	public int getPage() {
//		return page;
//	}
//
//	public void setPage(int page) {
//		this.page = page;
//	}
//
//	public int getRows() {
//		return rows;
//	}
//
//	public void setRows(int rows) {
//		this.rows = rows;
//	}
//
//	public ContactBook getModel() {
//		return this.model;
//	}
//
//	public AcceptPerson getAcceptPerson() {
//		return acceptPerson;
//	}
//
//	public void setAcceptPerson(AcceptPerson acceptPerson) {
//		this.acceptPerson = acceptPerson;
//	}
//
//	public AlarmType getAlarmType() {
//		return alarmType;
//	}
//
//	public void setAlarmType(AlarmType alarmType) {
//		this.alarmType = alarmType;
//	}
//
//	@Resource
//	public void setManager(ContactBookManager manager) {
//		this.manager = manager;
//	}
//
//	@Resource
//	public void setAcceptPersonManager(AcceptPersonManager acceptPersonManager) {
//		this.acceptPersonManager = acceptPersonManager;
//	}
//
//	@Resource
//	public void setAlarmTypeManager(AlarmTypeManager alarmTypeManager) {
//		this.alarmTypeManager = alarmTypeManager;
//	}
//
//	private void objToJSON(List<ContactBook> objs, List<ContactBookJSON> jsons) {
//		for (ContactBook obj : objs) {
//			ContactBookJSON json = new ContactBookJSON();
//			json.setId(obj.getId());
//			json.setAcceptPerson(obj.getAcceptPerson().getPersonName());
//			json.setActive(obj.getActive());
//			json.setAlarmType(obj.getAlarmType().getAlarmName());
//			json.setContact(obj.getContact());
//			jsons.add(json);
//		}
//	}
//
//	@SuppressWarnings("unchecked")
//	public void query() throws IOException {
//		try {
//			StringBuilder hqlRow=new StringBuilder();
//			StringBuilder hqlCount=new StringBuilder();
//			
//			hqlRow.append("from ContactBook where 1=1");
//			hqlCount.append("select count(*) from ContactBook where 1=1");
//			Map<String, Object> map=new HashMap<String, Object>();
//			
//			if(id>0){
//				hqlRow.append(" and acceptPerson.id=:id");
//				hqlCount.append(" and acceptPerson.id=:id");
//				map.put("id", id);
//			}
//			hqlRow.append(" order by acceptPerson.personCode");
//
//			page = page <= 0 ? 1 : page;
//			rows = rows <= 0 ? 10 : rows;
//			
//			List<ContactBook> contactBooks = (List<ContactBook>) manager
//					.pagedQuery(hqlRow.toString(), page, rows, map).getResult();
//			List<ContactBookJSON> jsons = new ArrayList<ContactBookJSON>();
//			objToJSON(contactBooks, jsons);
//			int total=manager.getCount(hqlCount.toString(), map);
//			
//			map.clear();
//			map.put("total", total);
//			map.put("rows", jsons);
//			
//			JSONTool.writeDataResult(map);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	@SuppressWarnings("unchecked")
//	public void queyAlarmTypeByPerson() throws IOException {
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("id", id);
//		List<AlarmType> alarmTypes = manager
//				.find("select b.alarmType from ContactBook b where b.acceptPerson.id=:id and b.active=true",
//						map);
//		List<AlarmTypeJSON> jsons = new ArrayList<AlarmTypeJSON>();
//		for (AlarmType type : alarmTypes) {
//			AlarmTypeJSON json = new AlarmTypeJSON();
//			json.setActive(type.getActive());
//			json.setAlarmCode(type.getAlarmCode());
//			json.setAlarmName(type.getAlarmName());
//			json.setId(type.getId());
//			jsons.add(json);
//		}
//		JsonMapper jsonMapper = new JsonMapper();
//		PrintWriter writer = ServletActionContext.getResponse().getWriter();
//		writer.write(jsonMapper.toJson(jsons));
//		writer.flush();
//		writer.close();
//	}
//
//	@SuppressWarnings("unchecked")
//	public void save() throws IOException {
//		try {
//			acceptPerson = acceptPersonManager.findUniqueBy("id",
//					acceptPerson.getId());
//			alarmType = alarmTypeManager.findUniqueBy("id", alarmType.getId());
//
//			 String hql =
//			 "from ContactBook b where b.acceptPerson.id=:pid and b.alarmType.id=:tid";
//			 Map<String, Object> map = new HashMap<String, Object>();
//			 map.put("pid", acceptPerson.getId());
//			 map.put("tid", alarmType.getId());
//			 List<ContactBook> books = manager.find(hql, map);
//			 if (null != books && books.size() > 0) {
//			 JSONTool.writeMsgResult(false, acceptPerson.getPersonName()
//			 + "的通讯方式有重复！");
//			 } else {
//			 model.setAcceptPerson(acceptPerson);
//			 model.setAlarmType(alarmType);
//			 manager.save(model);
//			 JSONTool.writeMsgResult(true, "保存完成！");
//			 }
//		} catch (Exception e) {
//			e.printStackTrace();
//			JSONTool.writeMsgResult(false, "保存失败！");
//		}
//	}
//
//	public void delete() throws IOException {
//		try {
//			model = manager.findUniqueBy("id", model.getId());
//			model.setActive(false);
//			manager.save(model);
//			JSONTool.writeMsgResult(true, "删除完成！");
//		} catch (Exception e) {
//			JSONTool.writeMsgResult(true, "删除失败！");
//		}
//	}
//
//	public void edit() {
//		try {
//			acceptPerson = acceptPersonManager.findUniqueBy("id", acceptPerson.getId());
//			alarmType = alarmTypeManager.findUniqueBy("id", alarmType.getId());
//			ContactBook tmp = new ContactBook();
//			tmp = manager.findUniqueBy("id", model.getId());
//			tmp.setAcceptPerson(acceptPerson);
//			tmp.setAlarmType(alarmType);
//			tmp.setContact(model.getContact());
//			manager.save(tmp);
//			JSONTool.writeMsgResult(true, "修改完成！");
//		} catch (Exception e) {
//			e.printStackTrace();
//			try {
//				JSONTool.writeMsgResult(false, "修改失败！");
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
//		}
//	}
//}
