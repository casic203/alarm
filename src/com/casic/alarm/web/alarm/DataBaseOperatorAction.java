package com.casic.alarm.web.alarm;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.casic.alarm.domain.DataBaseOperator;
import com.casic.alarm.manager.DataBaseOperatorManager;
import com.casic.core.json.JSONTool;
import com.casic.core.struts2.BaseAction;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class DataBaseOperatorAction extends BaseAction implements
		ModelDriven<DataBaseOperator>, Preparable {

	private DataBaseOperator model;

	@Resource
	private DataBaseOperatorManager dataBaseOperatorManager;

	public DataBaseOperator getModel() {
		return model;
	}

	public void setModel(DataBaseOperator model) {
		this.model = model;
	}

	public void prepare() throws Exception {
		this.model = new DataBaseOperator();
	}

	@SuppressWarnings("unchecked")
	public void query() {
		try {
			StringBuilder hql = new StringBuilder();
			Map<String, Object> map = new java.util.HashMap<String, Object>();

			hql.append("from DataBaseOperator");
			List<DataBaseOperator> list = dataBaseOperatorManager.find(
					hql.toString(), map);

			hql.delete(0, hql.length());
			hql.append("select count(*) from DataBaseOperator");
			int total = dataBaseOperatorManager.getCount(hql.toString(), map);

			map.put("rows", list);
			map.put("total", total);

			JSONTool.writeDataResult(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void save() {
		try {
			if (null != dataBaseOperatorManager.findUniqueBy("code",
					model.getCode())) {
				JSONTool.writeMsgResult(false, "管理员编号重复：" + model.getCode());
			} else {
				dataBaseOperatorManager.save(model);
				JSONTool.writeMsgResult(true, "添加完成！");
			}
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
			DataBaseOperator operator = dataBaseOperatorManager.findUniqueBy(
					"id", model.getId());
			if (null == operator) {
				JSONTool.writeMsgResult(false, "要修改的管理员不存在！");
			} else {
				operator.setCode(model.getCode());
				operator.setName(model.getName());
				dataBaseOperatorManager.save(operator);
				JSONTool.writeMsgResult(true, "编辑完成！");
			}
		} catch (Exception e) {
			try {
				e.printStackTrace();
				JSONTool.writeMsgResult(false, "编辑失败！");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public void delete() throws IOException {
		try {
			if(model.getId()!=null)
			{
				DataBaseOperator operator=(DataBaseOperator) dataBaseOperatorManager.findUniqueBy("id", model.getId());
				operator.setActive(false);
				dataBaseOperatorManager.save(operator);
				JSONTool.writeMsgResult(true, "删除成功！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			JSONTool.writeMsgResult(true, "删除失败！");
		}
	}
}
