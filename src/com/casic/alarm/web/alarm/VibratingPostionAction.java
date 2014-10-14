package com.casic.alarm.web.alarm;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.casic.alarm.domain.VibratingPostion4Nk;
import com.casic.alarm.manager.VibratingPostion4NkManager;
import com.casic.core.json.JSONTool;
import com.casic.core.struts2.BaseAction;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class VibratingPostionAction extends BaseAction implements
		ModelDriven<VibratingPostion4Nk>, Preparable {

	private Long devId;
	private Double distance;

	private VibratingPostion4Nk model;

	@Resource
	private VibratingPostion4NkManager postion4NkManager;

	public Long getDevId() {
		return devId;
	}

	public void setDevId(Long devId) {
		this.devId = devId;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public VibratingPostion4Nk getModel() {
		return model;
	}

	public void setModel(VibratingPostion4Nk model) {
		this.model = model;
	}

	public void setPostion4NkManager(
			VibratingPostion4NkManager postion4NkManager) {
		this.postion4NkManager = postion4NkManager;
	}

	public void prepare() throws Exception {
		this.model = new VibratingPostion4Nk();
	}

	public void queryPostionByDevAndDist() {
		try {
			
			if(devId==null){
				devId=Long.MIN_VALUE;
			}
			if(distance==null){
				distance=Double.MIN_VALUE;
			}
			
			String hql = "from VibratingPostion4Nk where abs(:dst-distance)=(select min(abs(:dst-distance)) from VibratingPostion4Nk where devId=:dev) and devId=:dev";
			Map<String, Object>map=new HashMap<String, Object>();
			map.put("dev", devId);
			map.put("dst", distance);
			
			VibratingPostion4Nk postion4Nk=postion4NkManager.findUnique(hql, map);
			JSONTool.writeDataResult(postion4Nk);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
