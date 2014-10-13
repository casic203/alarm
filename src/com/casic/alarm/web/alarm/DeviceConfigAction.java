package com.casic.alarm.web.alarm;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Results;
import org.jasig.cas.client.validation.Assertion;

import com.casic.alarm.JSON.DeviceConfigJSON;
import com.casic.alarm.JSON.DeviceJSON;
import com.casic.alarm.JSON.DeviceSensorJSON;
import com.casic.alarm.JSON.DeviceSconfigJSON;
import com.casic.alarm.domain.Device; 
import com.casic.alarm.domain.DeviceConfig;
import com.casic.alarm.domain.DeviceSensor;
import com.casic.alarm.domain.DeviceSconfig;
import com.casic.alarm.domain.DeviceSensorformat;
import com.casic.alarm.domain.SensorNoiseRecord;
import com.casic.alarm.domain.SysLog;
import com.casic.alarm.manager.DeviceConfigManager;
import com.casic.alarm.manager.DeviceManager;
import com.casic.alarm.manager.DeviceSensorManager;
import com.casic.alarm.manager.DeviceSensorformatManager;
import com.casic.alarm.manager.SensorFlowRecordManager;
import com.casic.alarm.manager.SensorGasRecordManage;
import com.casic.alarm.manager.SensorNoiseRecordManage;
import com.casic.alarm.manager.SensorNoiseRecordV2Manage;
import com.casic.alarm.manager.SensorPressRecordManage;
import com.casic.alarm.manager.SysLogManager;
import com.casic.alarm.utils.PropertiesUtil;
import com.casic.core.json.JSONTool; 
import com.casic.core.util.StringUtils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
@Results({
	@org.apache.struts2.convention.annotation.Result(name = "reload", location = "device-config.do", type = "redirect"),
	@org.apache.struts2.convention.annotation.Result(name = "json", type = "json", params = {
			"root", "jsonResults" }) }) 
public class DeviceConfigAction implements ModelDriven<Device>, Preparable{
	private long id;
	private int page;
	private int rows;
	
	private Device model; 
	private Assertion objUser;
	private DeviceConfig dcmodel;
	private DeviceManager dmanager;
	private SysLogManager logManager;
	 
	private String flow_cjjg;//cjjg=流量采集间隔
	private String flow_fscs;//fscs=流量发送次数
	private String noise_mjkssj;//mjkssj=噪声密集开始时间
	private String noise_mjjg;//mjjg=噪声密集间隔
	private String noise_mjybs;//mjybs=噪声密集样本数
	private String noise_wxkqsj;//wxkqsj=噪声无线开启时间
	private String noise_wxgbsj;//wxgbsj=噪声无线关闭时间
	private String noise_sskssj;//sskssj=噪声松散开始时间
	private String noise_ssjg;//ssjg=噪声松散间隔
	private String noise_ssybs;// ssybs=噪声松散样本数
	private String press_cjjg;//压力采集间隔
	private String press_fscs;//压力发送次数	
	
	private DeviceSensorformatManager dsfmanager;
	private DeviceConfigManager dcmanager;
	private DeviceSensorManager dsmanager;
	
	private SensorFlowRecordManager sfrmanager;
	private SensorGasRecordManage sgrmanager;
	private SensorNoiseRecordManage snrmanager;
	private SensorNoiseRecordV2Manage snrv2manager;
	private SensorPressRecordManage sprmanager;

	private PropertiesUtil propertiesUtil = null;
	
	public void prepare() throws Exception {
		// TODO Auto-generated method stub
		this.model = new Device();
		objUser = (Assertion) ActionContext.getContext().getSession().get("_const_cas_assertion_");
	}

	public Device getModel() {
		// TODO Auto-generated method stub
		return this.model;
	}
	
	public String execute() {
		return "success";
	}
 
	public String getFlow_cjjg() {
		return flow_cjjg;
	}

	public void setFlow_cjjg(String flow_cjjg) {
		this.flow_cjjg = flow_cjjg;
	}

	public String getFlow_fscs() {
		return flow_fscs;
	}

	public void setFlow_fscs(String flow_fscs) {
		this.flow_fscs = flow_fscs;
	}

	public String getNoise_mjkssj() {
		return noise_mjkssj;
	}

	public void setNoise_mjkssj(String noise_mjkssj) {
		this.noise_mjkssj = noise_mjkssj;
	}

	public String getNoise_mjjg() {
		return noise_mjjg;
	}

	public void setNoise_mjjg(String noise_mjjg) {
		this.noise_mjjg = noise_mjjg;
	}

	public String getNoise_mjybs() {
		return noise_mjybs;
	}

	public void setNoise_mjybs(String noise_mjybs) {
		this.noise_mjybs = noise_mjybs;
	}

	public String getNoise_wxkqsj() {
		return noise_wxkqsj;
	}

	public void setNoise_wxkqsj(String noise_wxkqsj) {
		this.noise_wxkqsj = noise_wxkqsj;
	}

	public String getNoise_wxgbsj() {
		return noise_wxgbsj;
	}

	public void setNoise_wxgbsj(String noise_wxgbsj) {
		this.noise_wxgbsj = noise_wxgbsj;
	}

	public String getNoise_sskssj() {
		return noise_sskssj;
	}

	public void setNoise_sskssj(String noise_sskssj) {
		this.noise_sskssj = noise_sskssj;
	}

	public String getNoise_ssjg() {
		return noise_ssjg;
	}

	public void setNoise_ssjg(String noise_ssjg) {
		this.noise_ssjg = noise_ssjg;
	}

	public String getNoise_ssybs() {
		return noise_ssybs;
	}

	public void setNoise_ssybs(String noise_ssybs) {
		this.noise_ssybs = noise_ssybs;
	}

	public String getPress_cjjg() {
		return press_cjjg;
	}

	public void setPress_cjjg(String press_cjjg) {
		this.press_cjjg = press_cjjg;
	}

	public String getPress_fscs() {
		return press_fscs;
	}

	public void setPress_fscs(String press_fscs) {
		this.press_fscs = press_fscs;
	}
 
	public DeviceConfig getDcmodel() {
		return dcmodel;
	}
	
	public void setDcmodel(DeviceConfig dcmodel) {
		this.dcmodel = dcmodel;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	@Resource
	public void setDsfmanager(DeviceSensorformatManager dsfmanager) {
		this.dsfmanager = dsfmanager;
	}

	@Resource
	public void setDmanager(DeviceManager dmanager) {
		this.dmanager = dmanager;
	}
	
	@Resource
	public void setDcmanager(DeviceConfigManager dcmanager) {
		this.dcmanager = dcmanager;
	}
 
	@Resource
	public void setDsmanager(DeviceSensorManager dsmanager) {
		this.dsmanager = dsmanager;
	}

	@Resource
	public void setLogManager(SysLogManager logManager) {
		this.logManager = logManager;
	}

	@SuppressWarnings("unchecked")
	public void query() throws IOException {
		try {
			StringBuilder hqlRow = new StringBuilder();
			StringBuilder hqlCount = new StringBuilder();
			Map<String, Object> map = new HashMap<String, Object>();

			hqlRow.append("from Device where 1=1");
			hqlCount.append("select count(*) from Device as d where 1=1");

			if (model.getDevCode() != null) {
				String devtype = model.getDeviceType().getTypeCode();

				hqlRow.append(" and d.devid =:devid");
				hqlCount.append(" and d.devid =:devid");
				map.put("devid", model.getDevCode());
			} 

			List<Device> list = (List<Device>) dmanager.pagedQuery(hqlRow.toString(), page, rows, map).getResult();
			int total = dmanager.getCount(hqlCount.toString(), map);

			List<DeviceConfigJSON> jsons = new ArrayList<DeviceConfigJSON>();

			objToDJSON(list, jsons);

			map.clear();
			map.put("rows", jsons);
			map.put("total", total);
			JSONTool.writeDataResult(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
	
	private void objToDJSON(List<Device> devices,
			List<DeviceConfigJSON> jsons) {  
		for (Device t : devices) {
			DeviceConfigJSON json = new DeviceConfigJSON(); 
			json.setId(t.getId()); 
			json.setDevCode(t.getDevCode());
			json.setDevName(t.getDevName());
			json.setTypeName(t.getDeviceType().getTypeName());
			json.setActive(t.getActive());
			
			jsons.add(json);
		}
	}
	@SuppressWarnings("unchecked")
	public void sendADData() throws IOException{
		try {
			StringBuilder receiveDataHql = null;
			Map<String, Object> map = new HashMap<String, Object>();
			String[] sensortypes= null;
			if(model.getDevCode()!=null){ //找到该设备有哪些类型的传感器  
				String sensorHql ="select distinct(id.sensorcode) from DeviceSensor where 1=1 and"
									+" device.devCode='" +model.getDevCode()+"'";
					
				List ll = dsmanager.find(sensorHql, map); 
				sensortypes=new String[ll.size()];
				for(int i=0;i<sensortypes.length;i++){ 
					sensortypes[i]=ll.get(i).toString();  
				}
			} 

			for(int i=0;i<sensortypes.length;i++){
				receiveDataHql = new StringBuilder();
				receiveDataHql.append("from DeviceSensorformat where 1=1");
				receiveDataHql.append(" and dsfid.sensortype='"+sensortypes[i]+"'"); 
				receiveDataHql.append(" order by dsfid.sensortype, sortid");
 
				page = page <= 0 ? 1 : page;
				rows = rows <= 0 ? 100 : rows;

				List<DeviceSensorformat> list = (List<DeviceSensorformat>) dsfmanager.pagedQuery(
						receiveDataHql.toString(), page, rows, map).getResult();
				StringBuilder sdata=new StringBuilder();
				 
				for(DeviceSensorformat dsf:list){
					String itemname = dsf.getDsfid().getItemname(); 
					if(itemname.equalsIgnoreCase("FLOW_CJJGSJ")){ 
						if(flow_cjjg.length()>0){ 
							String hsdata = Integer.toHexString(Integer.parseInt(flow_cjjg));
							int len=4-hsdata.length();
							if(len>0){
								for(int j=0;j<len;j++){
									sdata.append("0");
								} 
							}
							sdata.append(hsdata);
							sdata.append(",");
						}else{
							sdata.append("0000,");
						}
					}else if(itemname.equalsIgnoreCase("FLOW_SJFSCS")){
						if(flow_fscs.length()>0){
							String hsdata =Integer.toHexString(Integer.parseInt(flow_fscs));
							int len = 4-hsdata.length();
							if(len>0){
								for(int j=0;j<len;j++){
									sdata.append("0");
								} 
							}
							sdata.append(hsdata); 
							sdata.append(",");
						}else{
							sdata.append("0000,");
						}
					}else if(itemname.equalsIgnoreCase("FLOW_SJJLJGSJ")){
						if(press_cjjg.length()>0){
							String hsdata =Integer.toHexString(Integer.parseInt(press_cjjg));
							int len = 4-hsdata.length();
							if(len>0){
								for(int j=0;j<len;j++){
									sdata.append("0");
								} 
							}
							sdata.append(hsdata); 
							sdata.append(","); 
						}
						else{
							sdata.append("0000,");
						}
					}else if(itemname.equalsIgnoreCase("FLOW_SJFSCS")){
						if(press_fscs.length()>0){
							String hsdata =Integer.toHexString(Integer.parseInt(press_fscs));
							int len = 4-hsdata.length();
							if(len>0){
								for(int j=0;j<len;j++){
									sdata.append("0");
								} 
							}
							sdata.append(hsdata); 
							sdata.append(","); 
						}
						else{
							sdata.append("0000,");
						}
					}else if(itemname.equalsIgnoreCase("NOISE_MJKSSJ")){
						if(noise_mjkssj.length()>0){
							String[] sjdata =noise_mjkssj.split(":"); 
								
							String f = Integer.toHexString(Integer.parseInt(sjdata[0])); 
							String s = Integer.toHexString(Integer.parseInt(sjdata[1]));  
						 
							int flen = 2-f.length();
							if(flen>0){
								for(int j=0;j<flen;j++){
									f="0"+f;
								} 
							}
							int slen = 2-s.length();
							if(slen>0){
								for(int j=0;j<slen;j++){
									s="0"+s;
								} 
							}
						sdata.append(f+s); 
						sdata.append(","); 
						}else{
							sdata.append("0000,");
						}
					}else if(itemname.equalsIgnoreCase("NOISE_MJYBJG")){
						if(noise_mjjg.length()>0){
							String hsdata =(Integer.toHexString(Integer.parseInt(noise_mjjg)));   
							int len = 2-hsdata.length();
							if(len>0){
								for(int j=0;j<len;j++){
									sdata.append("0");
								} 
							}
							sdata.append(hsdata); 
							sdata.append(","); 
						}else{
							sdata.append("FF,");
						}
					}else if(itemname.equalsIgnoreCase("NOISE_MJYBSM")){
						if(noise_mjybs.length()>0){
							String hsdata =(Integer.toHexString(Integer.parseInt(noise_mjybs)));  
							int len = 2-hsdata.length();
							if(len>0){
								for(int j=0;j<len;j++){
									sdata.append("0");
								} 
							}
							sdata.append(hsdata); 
							sdata.append(","); 
						}else{
							sdata.append("FF,");
						}
					}else if(itemname.equalsIgnoreCase("NOISE_WXKQSJ")){
						if(noise_wxkqsj.length()>0){
							String[] sjdata = noise_wxkqsj.split(":");  
							String f = Integer.toHexString(Integer.parseInt(sjdata[0])); 
							String s = Integer.toHexString(Integer.parseInt(sjdata[1]));  
						 
							int flen = 2-f.length();
							if(flen>0){
								for(int j=0;j<flen;j++){
									f="0"+f;
								} 
							}
							int slen = 2-s.length();
							if(slen>0){
								for(int j=0;j<slen;j++){
									s="0"+s;
								} 
							}
							sdata.append(f+s); 
							sdata.append(","); 
						}else{
							sdata.append("0000,");
						}
					}else if(itemname.equalsIgnoreCase("NOISE_WXGBSJ")){
						if(noise_wxgbsj.length()>0){ 
							String[] sjdata = noise_wxgbsj.split(":");  
							String f = Integer.toHexString(Integer.parseInt(sjdata[0])); 
							String s = Integer.toHexString(Integer.parseInt(sjdata[1]));  
						 
							int flen = 2-f.length();
							if(flen>0){
								for(int j=0;j<flen;j++){
									f="0"+f;
								} 
							}
							int slen = 2-s.length();
							if(slen>0){
								for(int j=0;j<slen;j++){
									s="0"+s;
								} 
							}
							sdata.append(f+s); 
							sdata.append(","); 
						}else{
							sdata.append("0000,");
						}
					}else if(itemname.equalsIgnoreCase("NOISE_SSKSSJ")){
						if(noise_sskssj.length()>0){ 
							String[] sjdata = noise_sskssj.split(":");  
							String f = Integer.toHexString(Integer.parseInt(sjdata[0])); 
							String s = Integer.toHexString(Integer.parseInt(sjdata[1]));  
						 
							int flen = 2-f.length();
							if(flen>0){
								for(int j=0;j<flen;j++){
									f="0"+f;
								} 
							}
							int slen = 2-s.length();
							if(slen>0){
								for(int j=0;j<slen;j++){
									s="0"+s;
								} 
							}
							sdata.append(f+s); 
							sdata.append(","); 
						}else{
							sdata.append("0000,");
						}
					}else if(itemname.equalsIgnoreCase("NOISE_SSJG")){
						if(noise_ssjg.length()>0){
							String hsdata =(Integer.toHexString(Integer.parseInt(noise_ssjg))); 
							int len = 2-hsdata.length();
							if(len>0){
								for(int j=0;j<len;j++){
									sdata.append("0");
								} 
							}
							sdata.append(hsdata); 
							sdata.append(","); 
						}else{
							sdata.append("00,");
						}
					}else if(itemname.equalsIgnoreCase("NOISE_SSYBSM")){
						if(noise_ssybs.length()>0){
							String hsdata =(Integer.toHexString(Integer.parseInt(noise_ssybs)));
							int len = 2-hsdata.length();
							if(len>0){
								for(int j=0;j<len;j++){
									sdata.append("0");
								} 
							}
							sdata.append(hsdata); 
							sdata.append(","); 
						}else{
							sdata.append("FF,");
						}
					}else if(itemname.equalsIgnoreCase("JSZJ")){ 
						sdata.append(dsf.getItemvalue()); 
					}else if(itemname.equalsIgnoreCase("JW")){
						sdata.append(dsf.getItemvalue());
					}else if(itemname.equalsIgnoreCase("BT")){
						sdata.append(dsf.getItemvalue());  
					}else{
						sdata.append(dsf.getItemvalue());
						sdata.append(",");
					}
				} 
				//保存 到oracle数据库
				DeviceConfig deviceConfig = new DeviceConfig(); 
				deviceConfig.setDevid(model.getDevCode());
				deviceConfig.setFramecontent(sdata.toString());			
				deviceConfig.setWritetime(new Date());
				deviceConfig.setStatus(false);  
				
				dcmanager.save(deviceConfig);
			} 
			map.put("success", true);
			JSONTool.writeDataResult(map);
			JSONTool.writeMsgResult(true, "保存成功！");
		}catch(Exception e){ 
			e.printStackTrace();
			JSONTool.writeMsgResult(false, "保存失败！");
		}
		
	}
	
	public void sendRQData(){
		Map<String, Object> map=new HashMap<String, Object>();
		String devCode=model.getDevCode();
	
		StringBuilder sendmess= new StringBuilder();
		
		try {
			Socket ss=new Socket("192.168.0.35",6800);
			
			if(ss.isConnected()){
				System.out.println("the server connected"); 
				String senddata= "RQConfig:"+devCode+"/r/n";
				byte[] data=senddata.getBytes();
				
			}
		}catch(Exception e){
			
		}
		
	}
	
	public void edit() throws IOException {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			propertiesUtil = new PropertiesUtil("application.properties");
			
			Device device = new Device();
			device=dmanager.findUniqueBy("id",model.getId()); 
			String devicetypename=device.getDeviceType().getTypeName();
			 
			String tableString ="";
			String[] tables=null;
			if(devicetypename.contains("sl")){//渗漏
				String hqlString="from SensorNoiseRecord2 where devid=:deviceid";
				map.put("deviceid", device.getDevCode());
				List<SensorNoiseRecord> deviceSensors = snrmanager.find(hqlString, map);
			}else if(devicetypename.contains("dj")){//漏损
				tableString =propertiesUtil.getProperty("AD_DJ"); 
				tables=tableString.split("/");
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			JSONTool.writeMsgResult(false, "编辑失败！");
		}finally{
			SysLog log=new SysLog();
			log.setBusinessName("传感器参数维护");
			log.setContent("编辑参数："+model.getDevCode()+"-"+model.getDevName());
			log.setOperationType("add");
			log.setCreateUser(objUser.getPrincipal().getName());
			log.setCreateTime(new Date());
			logManager.save(log);
			JSONTool.writeMsgResult(true, "保存成功！");
		}
	}
	
}
