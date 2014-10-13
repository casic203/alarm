package com.casic.alarm.web.alarm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import com.casic.alarm.JSON.DeviceJSON;
import com.casic.alarm.JSON.PositionInfoJSON;
import com.casic.alarm.JSON.WaterPipelineRegionDataJSON;
import com.casic.alarm.JSON.WaterPipelineRegionTreeJSON;
import com.casic.alarm.domain.DMAInfo;
import com.casic.alarm.domain.DevPos;
import com.casic.alarm.domain.Device;
import com.casic.alarm.domain.PosDma;
import com.casic.alarm.domain.PositionInfo;
import com.casic.alarm.form.WaterPipelineRegionForm;
import com.casic.alarm.manager.DMAInfoManage;
import com.casic.alarm.manager.DeviceManager;
import com.casic.alarm.manager.PositionInfoManage;
import com.casic.core.json.JSONTool;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

/**
 * 给水管线分区浏览Action
 * @author liuxin
 *
 */
@Results({ @Result(name = "success", location = "/index.jsp", type = "redirect") })
public class WaterPipelineRegionScanAction implements ModelDriven<WaterPipelineRegionForm>, Preparable  {

	@Resource
	private DMAInfoManage dmaInfoManage;
	
	@Resource
	private PositionInfoManage positionInfoManage;
	
	@Resource
	private DeviceManager deviceManager;
	
	private WaterPipelineRegionForm model = new WaterPipelineRegionForm();
	
	public void prepare() throws Exception {
		
	}

	public WaterPipelineRegionForm getModel() {
		return this.model;
	}

	public String execute() {
		return "success";
	}
	
	/**
	 * 查询所有给水管线分区树型结构数据
	 */
    public void getWaterPipelineRegionTreeData() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<WaterPipelineRegionTreeJSON> regionTreeJSONList = new ArrayList<WaterPipelineRegionTreeJSON>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String hql = "from DMAInfo dmaInfo where dmaInfo.active=:active";
		
		paramMap.put("active", true);
		List<DMAInfo> dmaInfoList = dmaInfoManage.find(hql, paramMap);
		for (DMAInfo dmaInfo : dmaInfoList) {
			WaterPipelineRegionTreeJSON dmaTreeJSON = new WaterPipelineRegionTreeJSON();
			dmaTreeJSON.setId(dmaInfo.getID());
			dmaTreeJSON.setElementName(dmaInfo.getName());
			dmaTreeJSON.setIconCls(null);
			if(dmaInfo.getBDataParent_DMA() != null && !"-1".equals(dmaInfo.getBDataParent_DMA())) {
				dmaTreeJSON.set_parentId(Long.parseLong(dmaInfo.getBDataParent_DMA()));
			}
			dmaTreeJSON.setIsRegion(true);
			dmaTreeJSON.setIsPosition(false);
			dmaTreeJSON.setRegionID(dmaInfo.getID());
			regionTreeJSONList.add(dmaTreeJSON);
			
			List<PosDma> posInDmaSet = dmaInfo.getPosInDmaList();
			for (PosDma posInDma : posInDmaSet) {
				if(!posInDma.getPositionInfo().getActive()) {
					continue;
				}
				
				WaterPipelineRegionTreeJSON posTreeJSON = new WaterPipelineRegionTreeJSON();
				Date date = new Date();
				posTreeJSON.setId(date.getTime() + dmaInfo.getID() + posInDma.getPositionInfo().getID());
				posTreeJSON.setElementName(posInDma.getPositionInfo().getName());
				posTreeJSON.setIconCls("icon-tip");
				posTreeJSON.set_parentId(dmaInfo.getID());
				posTreeJSON.setIsRegion(false);
				posTreeJSON.setIsPosition(true);
				posTreeJSON.setPositionID(posInDma.getPositionInfo().getID());
				regionTreeJSONList.add(posTreeJSON);
				
			}
		}
		
		resultMap.put("total", regionTreeJSONList.size());
		resultMap.put("rows", regionTreeJSONList);
		try {
			JSONTool.writeDataResult(resultMap);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 根据分区ID查找子分区
     */
    public void querySubDMAByID() {
    	Long regionId = model.getRegionID();
    	Map<String, Object> paramMap = new HashMap<String, Object>();
    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	List<WaterPipelineRegionDataJSON> waterPipelineRegionDataJSONList = new ArrayList<WaterPipelineRegionDataJSON>();
    	String hql = "from DMAInfo dmaInfo where dmaInfo.BDataParent_DMA=:parentRegionID and dmaInfo.active=:active";
    	
    	paramMap.put("parentRegionID", regionId + "");
    	paramMap.put("active", true);
    	List<DMAInfo> dmaInfoList = dmaInfoManage.find(hql, paramMap);
    	for (DMAInfo dmaInfo : dmaInfoList) {
    		WaterPipelineRegionDataJSON regionDataJSON = new WaterPipelineRegionDataJSON(dmaInfo);
    		waterPipelineRegionDataJSONList.add(regionDataJSON);
		}
    	resultMap.put("total", waterPipelineRegionDataJSONList.size());
    	resultMap.put("rows", waterPipelineRegionDataJSONList);
    	
		try {
			JSONTool.writeDataResult(resultMap);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 根据分区ID查找改分区下的监测点
     */
    public void queryPositionInfoByID() {
    	Long regionId = model.getRegionID();
    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	List<PositionInfoJSON> positionInfoJSONList = new ArrayList<PositionInfoJSON>();
    	Map<String, Object> paramMap = new HashMap<String, Object>();
    	String hql = "from DMAInfo dmaInfo where dmaInfo.ID=:regionId and dmaInfo.active=:active";
    	
    	paramMap.put("regionId", regionId);
    	paramMap.put("active", true);
    	List<DMAInfo> dmaInfoList = dmaInfoManage.find(hql, paramMap);
    	for (DMAInfo dmaInfo : dmaInfoList) {
    		List<PosDma> posDmaList = dmaInfo.getPosInDmaList();
    		
    		for (PosDma posDma : posDmaList) {
    			PositionInfo positionInfo = posDma.getPositionInfo();
    			if(!positionInfo.getActive()) {
    				continue;
    			}
    			
    			PositionInfoJSON positionInfoJSON = new PositionInfoJSON(positionInfo);
    			positionInfoJSONList.add(positionInfoJSON);
			}
    		break;
		}
    	resultMap.put("total", positionInfoJSONList.size());
    	resultMap.put("rows", positionInfoJSONList);
    	
		try {
			JSONTool.writeDataResult(resultMap);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 根据监测点ID查找所有设备
     */
    public void queryEquipmentInfoByID() {
    	Long positionID = model.getPositionID();
    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	List<DeviceJSON> deviceJSONList = new ArrayList<DeviceJSON>();
    	Map<String, Object> paramMap = new HashMap<String, Object>();
    	String hql = "from PositionInfo positionInfo where positionInfo.ID=:positionID and positionInfo.active=:active";
    	
    	paramMap.put("positionID", positionID);
    	paramMap.put("active", true);
    	List<PositionInfo> positionInfoList = positionInfoManage.find(hql, paramMap);
    	for (PositionInfo positionInfo : positionInfoList) {
    		Set<DevPos> devPosSet = positionInfo.getEqtInPosSet();
    		for (DevPos devPos : devPosSet) {
    			Device device = devPos.getDevice();
    			Boolean isActive = device.getActive();
    			if(!isActive) {
    				continue;
    			}
    			
    			DeviceJSON deviceJSON = new DeviceJSON(device);
    			deviceJSONList.add(deviceJSON);
			}
    		break;
		}
    	resultMap.put("total", deviceJSONList.size());
    	resultMap.put("rows", deviceJSONList);
    	
		try {
			JSONTool.writeDataResult(resultMap);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    /**
     * 查询所有监测点
     */
    public void queryPosition() {
    	Map<String, Object> paramMap = new HashMap<String, Object>();
    	List<PositionInfoJSON> positionInfoJSONList = new ArrayList<PositionInfoJSON>();
    	Set<Long> excludePosIdSet = new HashSet<Long>();
    	
    	String hql = "from DMAInfo dmaInfo where dmaInfo.ID=:regionId and dmaInfo.active=:active";
    	paramMap.put("regionId", model.getRegionID());
    	paramMap.put("active", true);
    	List<DMAInfo> dmaInfoList = dmaInfoManage.find(hql, paramMap);
    	for (DMAInfo dmaInfo : dmaInfoList) {
    		List<PosDma> posDmaList = dmaInfo.getPosInDmaList();
    		
    		for (PosDma posDma : posDmaList) {
    			PositionInfo positionInfo = posDma.getPositionInfo();
    			if(!positionInfo.getActive()) {
    				continue;
    			}
    			
    			excludePosIdSet.add(positionInfo.getID());
			}
    		break;
		}
    	
    	paramMap.clear();
    	paramMap.put("active", true);
    	List<PositionInfo> positionInfoList = positionInfoManage.find("from PositionInfo pos where pos.active=:active", paramMap);
    	
    	for (PositionInfo positionInfo : positionInfoList) {
			PositionInfoJSON positionInfoJSON = new PositionInfoJSON(positionInfo);
			if(!excludePosIdSet.contains(positionInfo.getID())) {
				positionInfoJSONList.add(positionInfoJSON);
			}
		}
    	
		try {
			JSONTool.writeDataResult(positionInfoJSONList);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 查询所有设备
     */
    public void queryAllDevice() {
    	Map<String, Object> paramMap = new HashMap<String, Object>();
    	List<DeviceJSON> deviceJSONList = new ArrayList<DeviceJSON>();
    	
    	paramMap.put("active", true);
    	List<Device> deviceList = deviceManager.find("from Device dev where dev.active=:active", paramMap);
    	for (Device device : deviceList) {
			DeviceJSON deviceJSON = new DeviceJSON(device);
			deviceJSONList.add(deviceJSON);
		}
    	
		try {
			JSONTool.writeDataResult(deviceJSONList);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
	public DMAInfoManage getDmaInfoManage() {
		return dmaInfoManage;
	}

	public void setDmaInfoManage(DMAInfoManage dmaInfoManage) {
		this.dmaInfoManage = dmaInfoManage;
	}

	public PositionInfoManage getPositionInfoManage() {
		return positionInfoManage;
	}

	public void setPositionInfoManage(PositionInfoManage positionInfoManage) {
		this.positionInfoManage = positionInfoManage;
	}

	public DeviceManager getDeviceManager() {
		return deviceManager;
	}

	public void setDeviceManager(DeviceManager deviceManager) {
		this.deviceManager = deviceManager;
	}
}
