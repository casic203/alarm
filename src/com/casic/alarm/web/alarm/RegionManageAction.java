package com.casic.alarm.web.alarm;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.jasig.cas.client.validation.Assertion;

import com.casic.alarm.JSON.DeviceJSON;
import com.casic.alarm.JSON.RegionExtJSON;
import com.casic.alarm.JSON.RegionJSON;
import com.casic.alarm.JSON.RegionTreeJSON;
import com.casic.alarm.domain.Device;
import com.casic.alarm.domain.Region;
import com.casic.alarm.domain.SysLog;
import com.casic.alarm.form.RegionManageForm;
import com.casic.alarm.manager.DeviceManager;
import com.casic.alarm.manager.RegionManage;
import com.casic.alarm.manager.SysLogManager;
import com.casic.core.json.JSONTool;
import com.casic.core.struts2.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

/**
 * 分区管理Action
 * @author liuxin
 *
 */
@Results({ @Result(name = "success", location = "/index.jsp", type = "redirect") })
public class RegionManageAction extends BaseAction implements ModelDriven<RegionManageForm>, Preparable {

	private String flag;
	
	/**
	 * 分区Manage
	 */
	@Resource
	private RegionManage regionManage;
	
	/**
	 * 设备Manage
	 */
	@Resource
    private DeviceManager deviceManager;
	@Resource
	private SysLogManager logManager;
	
	private RegionManageForm model = new RegionManageForm();
	private Assertion objUser;
	
	public void prepare() throws Exception {
		objUser = (Assertion) ActionContext.getContext().getSession().get("_const_cas_assertion_");
	}

	public RegionManageForm getModel() {
		return this.model;
	}
	
	public void setLogManager(SysLogManager logManager) {
		this.logManager = logManager;
	}

	public String execute() {
		return "success";
	}

	/**
	 * 查询所有分区树型结构数据
	 */
    public void queryRegionTreeData() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<RegionTreeJSON> regionTreeJSONList = new ArrayList<RegionTreeJSON>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String hql = "from Region r where r.active=:active";
		
		paramMap.put("active", Boolean.TRUE);
		List<Region> regionList = regionManage.find(hql, paramMap);
		for (Region region : regionList) {
			RegionTreeJSON regionTreeJSON = new RegionTreeJSON();
			regionTreeJSON.setId(region.getId());
			regionTreeJSON.setElementName(region.getRegionName());
			regionTreeJSON.setIconCls(null);
			if(region.getParent() != null) {
				regionTreeJSON.set_parentId(region.getParent().getId());
			}
			regionTreeJSON.setIsRegion(true);
			regionTreeJSONList.add(regionTreeJSON);
			
			Set<Device> deviceSet = region.getDevices();
			for (Device device : deviceSet) {
				if(!device.getActive()) {
					continue;
				}
				
				RegionTreeJSON deviceTreeJSON = new RegionTreeJSON();
				Long time = new Date().getTime();
				deviceTreeJSON.setId(time + device.getId());
				deviceTreeJSON.setElementName(device.getDevName());
				deviceTreeJSON.setIconCls("icon-menm-item");
				deviceTreeJSON.set_parentId(region.getId());
				deviceTreeJSON.setIsRegion(false);
				deviceTreeJSON.setDeviceCode(device.getDevCode());
				regionTreeJSONList.add(deviceTreeJSON);
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
	 * 查询所有分区信息
	 */
	public void queryRegion() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<RegionJSON> regionJSONList = new ArrayList<RegionJSON>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String queryKeyRegionName = model.getRegionName();
		String hql = null;
		boolean isKeyQuery = false;
		
		if(null == queryKeyRegionName || "".equals(queryKeyRegionName)) {
	        hql = "from Region r where r.active=:active";
	        isKeyQuery = false;
		} else {
			hql = "from Region r where r.active=:active and r.regionName like :regionName";
			paramMap.put("regionName", "%" + queryKeyRegionName + "%");
			isKeyQuery = true;
		}
		paramMap.put("active", Boolean.TRUE);
		List<Region> regionList = regionManage.find(hql, paramMap);
		toJSONArray(regionList, regionJSONList, isKeyQuery);
		resultMap.put("total", regionJSONList.size());
		resultMap.put("rows", regionJSONList);
		try {
			JSONTool.writeDataResult(resultMap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据分区id查询所有设备
	 */
	public void queryDevicesByRegionId() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String hql = "from Device device where device.region.id=:regionId and device.active=:active";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		List<DeviceJSON> deviceJSONList = new ArrayList<DeviceJSON>();
		
		paramMap.put("regionId", model.getRegionId());
		paramMap.put("active", Boolean.TRUE);
		List<Device> deviceList = deviceManager.find(hql, paramMap);
		toDeviceJSONArray(deviceList, deviceJSONList);
		resultMap.put("total", deviceJSONList.size());
		resultMap.put("rows", deviceJSONList);
		try {
			JSONTool.writeDataResult(resultMap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 创建分区
	 */
	public void createRegion() {
		Region region = new Region();
		Long parentRegionId = model.get_parentRegionId();
		String deviceIds = model.getDeviceIds();
		String[] deviceIdArray = deviceIds.split("-");
		
		Region parentRegion = null;
		if(null != parentRegionId) { /** 选择父分区 */
			parentRegion = regionManage.get(parentRegionId);
		} 
	    region.setRegionName(model.getRegionName());
	    region.setRegionArea(model.getRegionArea());
	    region.setActive(Boolean.TRUE);
	    region.setParent(parentRegion);
	    Set<Device> addDevice = region.getDevices();
	    if(null != deviceIds && !"".equals(deviceIds)) {
	        for (String deviceIdStr : deviceIdArray) {
	    	    Long deviceId = Long.parseLong(deviceIdStr);
	    	    Device curDevice = deviceManager.get(deviceId);
	    	    curDevice.setRegion(region);
	    	    addDevice.add(curDevice);
		    }
	    }
	    regionManage.save(region);
	    
	    SysLog log=new SysLog();
		log.setBusinessName("分区管理");
		log.setContent("创建分区："+model.getRegionName()+"-"+model.getRegionArea());
		log.setOperationType("add");
		try {
			if(null == objUser) {
				objUser = (Assertion) ActionContext.getContext().getSession().get("_const_cas_assertion_");
			}
		    log.setCreateUser(objUser.getPrincipal().getName());
		} catch(Exception e) {
			log.setCreateUser("");
		}
		log.setCreateTime(new Date());
		logManager.save(log);
	    
		try {
			JSONTool.writeMsgResult(true, "创建分区成功!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除分区
	 */
	public void deleteRegion() {
		Long delRegionId = model.getRegionId();
		Region delRegion = regionManage.get(delRegionId);
		delRegion.setActive(Boolean.FALSE);
		Set<Device> regionDevice = delRegion.getDevices();
		for (Device device : regionDevice) {
			device.setRegion(null);
			deviceManager.save(device);
		}
		regionManage.save(delRegion);
		
		SysLog log=new SysLog();
		log.setBusinessName("分区管理");
		log.setContent("删除分区："+model.getRegionName()+"-"+model.getRegionArea());
		log.setOperationType("delete");
		try {
			if(null == objUser) {
				objUser = (Assertion) ActionContext.getContext().getSession().get("_const_cas_assertion_");
			}
		    log.setCreateUser(objUser.getPrincipal().getName());
		} catch(Exception e) {
			log.setCreateUser("");
		}
		log.setCreateTime(new Date());
		logManager.save(log);
		
		try {
			JSONTool.writeMsgResult(true, "删除分区成功!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 选择没有加入分区的设备
	 */
	public void queryNoRegionDevice() {
		Long currentModifyRegionId = model.getRegionId();
		String hql = null;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if(null == currentModifyRegionId) { //获取设备列表用于创建分区
			 hql = "from Device device where device.region is null and device.active=:active";
			 paramMap.put("regionId", model.getRegionId());
			 paramMap.put("active", Boolean.TRUE);
		} else {//获取设备列表用于修改分区
			 hql = "from Device device where (device.region is null or device.region.id=:curRegionId) and device.active=:active";
			 paramMap.put("regionId", model.getRegionId());
			 paramMap.put("active", Boolean.TRUE);
			 paramMap.put("curRegionId", currentModifyRegionId);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<DeviceJSON> deviceJSONList = new ArrayList<DeviceJSON>();

		List<Device> deviceList = deviceManager.find(hql, paramMap);
		toDeviceJSONArray(deviceList, deviceJSONList);
		resultMap.put("total", deviceJSONList.size());
		resultMap.put("rows", deviceJSONList);
		try {
			JSONTool.writeDataResult(resultMap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 开始修改分区
	 */
	public void beginModifyRegion() {
		Long regionId = model.getRegionId();
		String regionName = null;
		Long parentRegionId = null;
		String parentRegionName = null;
		String regionArea = null;
		String deviceIds = null;
		
		Region region = regionManage.get(regionId);
		if(null != region) {
			regionName = region.getRegionName();
			Region parentRegion = region.getParent();
			if(null != parentRegion) {
				parentRegionId = parentRegion.getId();
				parentRegionName = parentRegion.getRegionName();
			}
			regionArea = region.getRegionArea();
			Set<Device> deviceSet = region.getDevices();
			if(null != deviceSet && deviceSet.size() != 0) {
				deviceIds = "";
				for (Device device : deviceSet) {
					deviceIds += (device.getId() + "-");
				}
				deviceIds = deviceIds.substring(0, deviceIds.length() - 1);
			}
			
			RegionExtJSON regionExtJSON = new RegionExtJSON();
			regionExtJSON.setId(regionId);
			regionExtJSON.setRegionName(regionName);
			regionExtJSON.set_parentId(parentRegionId);
			regionExtJSON.setParentName(parentRegionName);
			regionExtJSON.setRegionArea(regionArea);
			regionExtJSON.setDeviceIds(deviceIds);
			
			try {
				JSONTool.writeDataResult(regionExtJSON);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 */
	public void confirmModifyRegion() {
		Region oldRegion = regionManage.get(model.getRegionId());
		Long parentRegionId = model.get_parentRegionId();
		String regionName = model.getRegionName();
		String regionArea = model.getRegionArea();
		String deviceIds = model.getDeviceIds();
		String[] deviceIdArray = null;
		if(null != deviceIds && !"".equals(deviceIds)) {
			deviceIdArray = deviceIds.split("-");
		}
		
		Region parentRegion = null;
		if(null != parentRegionId) { /** 选择父分区 */
			parentRegion = regionManage.get(parentRegionId);
		} 
		oldRegion.setRegionName(regionName);
		oldRegion.setRegionArea(regionArea);
		oldRegion.setParent(parentRegion);
	    Set<Device> addDevice = oldRegion.getDevices();
	    /**
	     *  清除和分区关联的设备对象
	     */
	    for (Device device : addDevice) { 
	    	device.setRegion(null);
	    	deviceManager.save(device);
		}
	    /**
	     * 关联新的对象
	     */
	    if(null != deviceIdArray) {
	        for (String deviceIdStr : deviceIdArray) {
	    	    Long deviceId = Long.parseLong(deviceIdStr);
	    	    Device curDevice = deviceManager.get(deviceId);
	    	    curDevice.setRegion(oldRegion);
	    	    addDevice.add(curDevice);
		    }
	    }
	    regionManage.save(oldRegion);

		try {
			JSONTool.writeMsgResult(true, "修改分区成功");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 转化适合前台显示的对象
	 * @param regionList      分区对象链表	
	 * @param regionJSONList  用于前台显示的分区对象链表
	 */
	private void toJSONArray(List<Region> regionList, List<RegionJSON> regionJSONList, boolean isKeyQuery) {
		for (Region region : regionList) {
			RegionJSON regionJSON = new RegionJSON();
			regionJSON.setId(region.getId());
			regionJSON.setRegionName(region.getRegionName());
			regionJSON.setRegionArea(region.getRegionArea());
			if(region.getParent() != null && !isKeyQuery) {
			    regionJSON.set_parentId(region.getParent().getId());
			}
			regionJSONList.add(regionJSON);
		}
	}
	
	/**
	 * 转化适合前台显示的对象
	 * @param deviceList      设备对象链表
	 * @param deviceJSONList  用于前台显示的设备对象链表
	 */
	private void toDeviceJSONArray(List<Device> deviceList, List<DeviceJSON> deviceJSONList) {
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		for (Device device : deviceList) {
			DeviceJSON deviceJSON = new DeviceJSON();
			deviceJSON.setId(device.getId());
			deviceJSON.setDevCode(device.getDevCode());
			if(null != device.getDeviceType()) {
			    deviceJSON.setTypeName(device.getDeviceType().getTypeName());
			}
			deviceJSON.setDevName(device.getDevName());
			deviceJSON.setFactory(device.getFactory());
			try {
			    deviceJSON.setInstallDate(dateFormat.format(device.getInstallDate()));
			} catch(Exception e) {
				deviceJSON.setInstallDate("");
			}
			try {
			    deviceJSON.setOutDate(dateFormat.format(device.getOutDate()));
			} catch(Exception e) {
				deviceJSON.setOutDate("");
			}
			
			if(null != device.getAcceptPerson()) {
			    deviceJSON.setPersonName(device.getAcceptPerson().getPersonName());
			}
			deviceJSONList.add(deviceJSON);
		}
	}
	
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public RegionManage getRegionManage() {
		return regionManage;
	}

	public void setRegionManage(RegionManage regionManage) {
		this.regionManage = regionManage;
	}
	
	public DeviceManager getDeviceManager() {
		return deviceManager;
	}

	public void setDeviceManager(DeviceManager deviceManager) {
		this.deviceManager = deviceManager;
	}

	public void setModel(RegionManageForm model) {
		this.model = model;
	}
}
