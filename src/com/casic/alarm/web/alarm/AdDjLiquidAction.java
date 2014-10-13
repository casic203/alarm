package com.casic.alarm.web.alarm;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import com.casic.alarm.manager.AdDjLiquidManager;
import com.casic.core.json.JSONTool;

/**
 * 液位监测仪Action
 * @author liuxin
 *
 */
public class AdDjLiquidAction extends DeviceRealtimeDataAction {
	
	@Resource
	private AdDjLiquidManager adDjLiquidManager;

	private final int MAX_LIQUID_LEVEL_RECORD = 10;
	
	/**
	 * 
	 * 获取液位监测仪电池电量信息
	 */
	@SuppressWarnings("unchecked")
	public void getDeviceCell() {
		String hql = "select adDjLiquid.cell from AdDjLiquid adDjLiquid" +
				" where adDjLiquid.devId=:devId order by adDjLiquid.uptime desc";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		paramMap.put("devId", model.getDevCode());
		List<Object> adDjLiquidList = adDjLiquidManager.createQuery(hql, paramMap)
		.setFirstResult(0)
		.setMaxResults(1).list();
		for (Object obj : adDjLiquidList) {
			String cell = (String)obj;
			resultMap.put("cell", cell);
			break;
		}
		
		try {
		    JSONTool.writeDataResult(resultMap);
	    } catch (IOException e) {
		    e.printStackTrace();
	    }
	}
	
	/**
	 * 获取液位监测仪液位曲线
	 */
	@SuppressWarnings("unchecked")
	public void getLiquidHistoryData() {
		String hql = "select adDjLiquid.uptime, adDjLiquid.liquidData from AdDjLiquid adDjLiquid" +
				" where adDjLiquid.devId=:devId order by adDjLiquid.uptime asc";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		List<Map<String, Object>> liquidList = new ArrayList<Map<String,Object>>();
		
		paramMap.put("devId", model.getDevCode());
		List<Object[]> adDjLiquidList = adDjLiquidManager.createQuery(hql, paramMap)
				.setFirstResult(0)
				.setMaxResults(MAX_LIQUID_LEVEL_RECORD).list();
		for (Object[] adDjLiquidPropVal : adDjLiquidList) {
			Map<String, Object> propMap = new HashMap<String, Object>();
			Date uptime = (Date)adDjLiquidPropVal[0];
			String liquidData = (String)adDjLiquidPropVal[1];
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(uptime!=null){
				propMap.put("uptime", simpleDateFormat.format(uptime));
			}
			propMap.put("liquidData", Long.parseLong(liquidData));
			liquidList.add(propMap);
		}
		
		try {
		    JSONTool.writeDataResult(liquidList);
	    } catch (IOException e) {
		    e.printStackTrace();
	    }
	}
	
	public AdDjLiquidManager getAdDjLiquidManager() {
		return adDjLiquidManager;
	}

	public void setAdDjLiquidManager(AdDjLiquidManager adDjLiquidManager) {
		this.adDjLiquidManager = adDjLiquidManager;
	}
	
}
