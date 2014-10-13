package com.casic.alarm.web.alarm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.casic.alarm.JSON.DeviceJSON;
import com.casic.alarm.JSON.RQConfigJSON;
import com.casic.alarm.domain.DeviceSensorformat;
import com.casic.alarm.domain.XtRqPeriod;
import com.casic.alarm.manager.DeviceSensorManager;
import com.casic.alarm.manager.DeviceSensorformatManager;
import com.casic.alarm.manager.XtRqPeriodManager;
import com.casic.core.json.JSONTool;

/**
 * 燃气智能监测终端action
 * @author liuxin
 *
 */
public class XtGasDisplayAction extends DeviceRealtimeDataAction {

	@Resource
	private XtRqPeriodManager xtRqPeriodManager;
	
	private final int MAX_XT_RQ_PERIOD_STRENGTH_HISTORY_RECORD = 10;
	

	private DeviceSensorformatManager dsfmanager;
	private DeviceSensorManager dsmanager;
	
	@Resource
	public void setDsfmanager(DeviceSensorformatManager dsfmanager) {
		this.dsfmanager = dsfmanager;
	}
	@Resource
	public void setDsmanager(DeviceSensorManager dsmanager) {
		this.dsmanager = dsmanager;
	}
	
	/**
	 * 获取燃气智能监测终端实时数据
	 */
	@SuppressWarnings("unchecked")
	public void getDeviceRealtimeData() {
		String hql = "from XtRqPeriod xtRqPeriod where xtRqPeriod.address=:devId order by xtRqPeriod.uptime desc";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		paramMap.put("devId", model.getDevCode());
		List<XtRqPeriod> xtRqPeriodList = xtRqPeriodManager.createQuery(hql, paramMap)
		.setFirstResult(0)
		.setMaxResults(1).list();
		for (XtRqPeriod xtRqPeriod : xtRqPeriodList) {
			resultMap.put("inPress", xtRqPeriod.getInPress());
			resultMap.put("outPress", xtRqPeriod.getOutPress());
			resultMap.put("cell", xtRqPeriod.getCell());
			break;
		}
		
		try {
		    JSONTool.writeDataResult(resultMap);
	    } catch (IOException e) {
		    e.printStackTrace();
	    }
	}
	
	/**
	 * 获取燃气浓度历史曲线数据
	 */
	@SuppressWarnings("unchecked")
	public void getStrengthHistoryData() {
		String hql = "select xtRqPeriod.uptime, xtRqPeriod.strength from XtRqPeriod xtRqPeriod where xtRqPeriod.address=:devId order by xtRqPeriod.uptime desc";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		
		paramMap.put("devId", model.getDevCode());
		List<Object[]> xtRqPeriodObjList = xtRqPeriodManager.createQuery(hql, paramMap)
		.setFirstResult(0)
		.setMaxResults(MAX_XT_RQ_PERIOD_STRENGTH_HISTORY_RECORD).list();
		if(null != xtRqPeriodObjList && xtRqPeriodObjList.size() != 0) {
			for (Object[] xtRqPeriodPropVal : xtRqPeriodObjList) {
				Date uptime = (Date)xtRqPeriodPropVal[0];
				String strength = (String)xtRqPeriodPropVal[1];
				Map<String, Object> itemMap = new HashMap<String, Object>();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				itemMap.put("uptime", simpleDateFormat.format(uptime));
				itemMap.put("strength", Long.parseLong(strength));
				dataList.add(itemMap);
			}
		}
		
		try {
		    JSONTool.writeDataResult(dataList);
	    } catch (IOException e) {
		    e.printStackTrace();
	    }
	}

	public XtRqPeriodManager getXtRqPeriodManager() {
		return xtRqPeriodManager;
	}

	public void setXtRqPeriodManager(XtRqPeriodManager xtRqPeriodManager) {
		this.xtRqPeriodManager = xtRqPeriodManager;
	}
	
	/**
	 * 查看设备在线否
	 */
	public void checkDeviceOnline(){
		Map<String, Object> map=new HashMap<String, Object>();
		String devCode=model.getDevCode();
		/*
		 * 连接服务器
		 */
		try {
			
			Socket ss=new Socket("192.168.0.35",6800);
			
			if(ss.isConnected()){
				System.out.println("the server connected"); 
				String senddata= "RQQuery:"+devCode+"/r/n";
				byte[] data=senddata.getBytes();
				
				OutputStream out =ss.getOutputStream();// 发送服务器
				out.write(data);
				
				InputStream in = ss.getInputStream(); //接收服务器
				int totalBytesRcvd=0;
				
				int bytesReceive;
				bytesReceive = in.read(data);  
				
				String datastr=new String(data,"UTF-8");  
				datastr = "RQQueryResp:{燃气智能监测终端001,1}/r/n";
				Map<String, String> datamap = new HashMap<String,String>();
				if(datastr.startsWith("RQQueryResp:")){
					String datastr2 = datastr.substring(12,datastr.length()-4);
					if(datastr2.contains("},")){
						String[] spdata = datastr2.split("},");
						for(int i=0;i<spdata.length;i++){
							String[] devstatus = spdata[i].split(","); 
							datamap.put(devstatus[0].substring(1), devstatus[1].substring(0,1));
						}
					}else{
						String[] spdata = datastr2.split(",");
						datamap.put(spdata[0].substring(1), spdata[1].substring(0,1));
					}  
				}
				//对每个收到的设备进行配置查询
				String sensortypeSQL = "select id.sensorcode from DeviceSensor where device.devCode='"+devCode+"'" ;
				List sensortypell = dsmanager.find(sensortypeSQL.toString(), map); 
				//for(int i=0;i<sensortypell.size();i++){
				String sensortype=sensortypell.get(0).toString();
				//}
				
				StringBuilder receiveDataHql = null;
				receiveDataHql = new StringBuilder(); 
				receiveDataHql.append("from DeviceSensorformat where 1=1");
				receiveDataHql.append(" and dsfid.sensortype='"+sensortype+"'"); 
				receiveDataHql.append(" order by dsfid.sensortype, sortid");
				List ll = dsfmanager.find(receiveDataHql.toString(), map); 
				 
				String message = "";
				for(int i=0;i<ll.size();i++){
					DeviceSensorformat dsf=(DeviceSensorformat)ll.get(i);
					message+=dsf.getItemvalue();
				}
				String querycomm="RQConfig:{"+devCode+","+message+"}";
				byte[] commdata=querycomm.getBytes();  
				out.write(commdata);
 
				bytesReceive = in.read(commdata); 
				String commdatastr=new String(commdata,"UTF-8");  
				
				if(commdatastr.startsWith("RQConfigResp:")){
					commdatastr = commdatastr.substring(13);
				}
				List<RQConfigJSON> rqJSONList = new ArrayList<RQConfigJSON>();
				RQConfigJSON rqjson = new RQConfigJSON();
				rqjson.setSjcjzq("32");//commdatastr.substring(52,60));
				rqjson.setSjsczq("13");//commdatastr.substring(61,69));
				rqJSONList.add(rqjson);
				
				ss.close(); 
				
				//返回设备在线状态
				map.put("success", true);

				map.put("total", 1);
				map.put("rows", rqJSONList);
			}else{
				map.put("success", false);
				map.put("total",1);
			}
			 
			JSONTool.writeDataResult(map);
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 
	} 
	
}
