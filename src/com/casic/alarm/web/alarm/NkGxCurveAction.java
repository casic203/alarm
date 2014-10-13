package com.casic.alarm.web.alarm;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.casic.alarm.manager.NkGxStressCurveManager;
import com.casic.alarm.manager.NkGxTemperatureCurveManager;
import com.casic.alarm.manager.NkGxVibratingCurveManager;
import com.casic.core.json.JSONTool;

/**
 * 光纤设备Action
 * @author liuxin
 *
 */
public class NkGxCurveAction extends DeviceRealtimeDataAction {
	
	/**
	 * 压力
	 */
	@Resource
	private NkGxStressCurveManager nkGxStressCurveManager;
	
	/**
	 * 温度
	 */
	@Resource
	private NkGxTemperatureCurveManager nkGxTemperatureCurveManager;
	
	/**
	 * 振动
	 */
	@Resource
	private NkGxVibratingCurveManager nkGxVibratingCurveManager;
	
	private final int MAX_NK_GX_HISTORY_RECORD = 1;
	
	
	/**
	 * 获取光纤最新数据
	 */
	@SuppressWarnings("unchecked")
	public void getDeviceRealtimeData() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String stressHql = "select nkGxStressCurve.distance, nkGxStressCurve.stress from NkGxStressCurve nkGxStressCurve" +
				" where nkGxStressCurve.devId=:devId order by nkGxStressCurve.uptime asc";
	    String temperatureHql = "select nkGxTemperatureCurve.distance, nkGxTemperatureCurve.temperature from NkGxTemperatureCurve nkGxTemperatureCurve" +
	    		" where nkGxTemperatureCurve.devId=:devId order by nkGxTemperatureCurve.uptime asc";
	    String vibratingHql = "select nkGxVibratingCurve.distance, nkGxVibratingCurve.vibrating from NkGxVibratingCurve nkGxVibratingCurve" +
	    		" where nkGxVibratingCurve.devId=:devId order by nkGxVibratingCurve.uptime asc";
	    Map<String, Object> resultMap = new HashMap<String, Object>();
	    
	    paramMap.put("devId", model.getDevCode());
	    List<Object[]> nkGxStressCurveList = nkGxStressCurveManager.createQuery(stressHql, paramMap)
	    		.setFirstResult(0)
	    		.setMaxResults(MAX_NK_GX_HISTORY_RECORD).list();
	    List<Object[]> nkGxTemperatureList = nkGxTemperatureCurveManager.createQuery(temperatureHql, paramMap)
	    		.setFirstResult(0)
	    		.setMaxResults(MAX_NK_GX_HISTORY_RECORD).list();;
	    List<Object[]> nkGxVibratingList = nkGxVibratingCurveManager.createQuery(vibratingHql, paramMap)
	    		.setFirstResult(0)
	    		.setMaxResults(MAX_NK_GX_HISTORY_RECORD).list();
	    Map<String, Object> streeMap = new HashMap<String, Object>();
	    for (Object[] nkGxStressObjArray : nkGxStressCurveList) {
			String distance = (String)nkGxStressObjArray[0];
			String stress = (String)nkGxStressObjArray[1];
			streeMap.put("distance", distance);
			streeMap.put("data", stress);
			break;
		}
	    resultMap.put("stress", streeMap);
	    Map<String, Object> temperatureMap = new HashMap<String, Object>();
	    for (Object[] nkGxTemperatureObjArray : nkGxTemperatureList) {
	    	String distance = (String)nkGxTemperatureObjArray[0];
			String temperature = (String)nkGxTemperatureObjArray[1];
			temperatureMap.put("distance", distance);
			temperatureMap.put("data", temperature);
			break;
		}
	    resultMap.put("temperature", temperatureMap);
	    Map<String, Object> vibratingMap = new HashMap<String, Object>();
	    for (Object[] nkGxVibratingObjArray : nkGxVibratingList) {
	    	String distance = (String)nkGxVibratingObjArray[0];
			String vibrating = (String)nkGxVibratingObjArray[1];
			vibratingMap.put("distance", distance);
			vibratingMap.put("data", vibrating);
			break;
		}
	    resultMap.put("vibrating", vibratingMap);
	    
		try {
		    JSONTool.writeDataResult(resultMap);
	    } catch (IOException e) {
		    e.printStackTrace();
	    }
	}
	
	public NkGxStressCurveManager getNkGxStressCurveManager() {
		return nkGxStressCurveManager;
	}
	public void setNkGxStressCurveManager(
			NkGxStressCurveManager nkGxStressCurveManager) {
		this.nkGxStressCurveManager = nkGxStressCurveManager;
	}
	public NkGxTemperatureCurveManager getNkGxTemperatureCurveManager() {
		return nkGxTemperatureCurveManager;
	}
	public void setNkGxTemperatureCurveManager(
			NkGxTemperatureCurveManager nkGxTemperatureCurveManager) {
		this.nkGxTemperatureCurveManager = nkGxTemperatureCurveManager;
	}
	public NkGxVibratingCurveManager getNkGxVibratingCurveManager() {
		return nkGxVibratingCurveManager;
	}
	public void setNkGxVibratingCurveManager(
			NkGxVibratingCurveManager nkGxVibratingCurveManager) {
		this.nkGxVibratingCurveManager = nkGxVibratingCurveManager;
	}
}
