package com.casic.alarm.web.alarm;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.casic.alarm.domain.GasStrength;
import com.casic.alarm.manager.GasStrengthManager;
import com.casic.core.json.JSONTool;
import com.casic.core.struts2.BaseAction;
import com.casic.core.util.StringUtils;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class GasStrengthAction extends BaseAction implements
		ModelDriven<GasStrength>, Preparable {

	private String devId;
	private String startTime;
	private String endTime;

	private GasStrength model;

	private GasStrengthManager gasStrengthManager;

	public String getDevId() {
		return devId;
	}

	public void setDevId(String devId) {
		this.devId = devId;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public GasStrength getModel() {
		return model;
	}

	public void setModel(GasStrength model) {
		this.model = model;
	}

	@Resource
	public void setGasStrengthManager(GasStrengthManager gasStrengthManager) {
		this.gasStrengthManager = gasStrengthManager;
	}

	public void prepare() throws Exception {
		this.model = new GasStrength();
	}

	@SuppressWarnings("unchecked")
	public void queryStrengthByDevId() {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd");
			String hql = "from GasStrength where devId=:id";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", devId);
			if (StringUtils.isNotBlank(startTime)) {
				hql += " and to_date(upTime,'yyyy-MM-dd HH24:MI:ss')>=:beg";
				map.put("beg", simpleDateFormat.parse(startTime));
			}
			if (StringUtils.isNotBlank(endTime)) {
				hql += " and to_date(upTime,'yyyy-MM-dd HH24:MI:ss')<=:end";
				map.put("end", simpleDateFormat.parse(endTime));
			}
			hql += " order by upTime asc";

			List<GasStrength> list = gasStrengthManager.find(hql, map);

			map.clear();
			map.put("data", list);
			JSONTool.writeDataResult(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
