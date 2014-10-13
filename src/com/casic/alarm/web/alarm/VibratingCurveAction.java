package com.casic.alarm.web.alarm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.casic.alarm.domain.VibratingCurve;
import com.casic.alarm.manager.VibratingCurveManager;
import com.casic.core.json.JSONTool;
import com.casic.core.struts2.BaseAction;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class VibratingCurveAction extends BaseAction implements
		ModelDriven<VibratingCurve>, Preparable {

	private String devId;

	private VibratingCurve model;

	private VibratingCurveManager vibratingCurveManager;

	public String getDevId() {
		return devId;
	}

	public void setDevId(String devId) {
		this.devId = devId;
	}

	public VibratingCurve getModel() {
		return model;
	}

	public void setModel(VibratingCurve model) {
		this.model = model;
	}

	@Resource
	public void setVibratingCurveManager(
			VibratingCurveManager vibratingCurveManager) {
		this.vibratingCurveManager = vibratingCurveManager;
	}

	public void prepare() throws Exception {
		this.model = new VibratingCurve();
	}

	@SuppressWarnings("unchecked")
	public void queryVibratingCurve() {
		try {
			String hql = "from VibratingCurve where devId=:id order by dbId desc";
			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put("id", devId);			
			List<VibratingCurve> list = vibratingCurveManager.find(hql, map);
			
			map.clear();
			map.put("data", list.get(0));
			JSONTool.writeDataResult(list.get(0));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
