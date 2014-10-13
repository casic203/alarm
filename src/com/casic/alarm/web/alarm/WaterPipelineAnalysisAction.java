package com.casic.alarm.web.alarm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import com.casic.alarm.domain.DMAInfo;
import com.casic.alarm.form.RegionManageForm;
import com.casic.alarm.manager.DMAInfoManage;
import com.casic.alarm.wsclient.DisLossesWebService;
import com.casic.alarm.wsclient.LeakageEvaWebService;
import com.casic.alarm.wsclient.LeakageStateWebService;
import com.casic.alarm.wsclient.WaterBalanceWebService;
import com.casic.alarm.wsclient.entry.WaterBalance;
import com.casic.alarm.wsclient.entry.disLosses;
import com.casic.alarm.wsclient.entry.leakageEva;
import com.casic.alarm.wsclient.entry.leakageState;
import com.casic.core.json.JSONTool;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

/**
 * 给水管线分析action
 * @author liuxin
 *
 */
public class WaterPipelineAnalysisAction implements ModelDriven<RegionManageForm>, Preparable {

	private RegionManageForm model = new RegionManageForm();
	
	/**
	 * 分区状态 web service
	 */
	@Resource
	private LeakageStateWebService leakageStateWebService;
	
	/**
	 * 漏损评估 web service
	 */
	@Resource
	private LeakageEvaWebService leakageEvaWebService;
	
	/**
	 * 产销差分析web service
	 */
	@Resource
	private DisLossesWebService disLossesWebService;
	
	/**
	 * 水平衡分析web service
	 */
	@Resource
	private WaterBalanceWebService waterBalanceWebService;
	
	/**
	 * 埃德尔分区manage
	 */
	@Resource
	private DMAInfoManage dmaInfoManage;
	
	public String execute() {
		return "success";
	}
	
	/**
	 * 获取所有的漏损分区
	 */
	public void allLeakRegion() {
		String dmaIds = "";
		
		/**
		 * 查询所有分区ID
		 */
		List<DMAInfo> dmaInfos = dmaInfoManage.getAll();
		for (DMAInfo dmaInfo : dmaInfos) {
			if(!"-1".equals(dmaInfo.getBDataParent_DMA()) || !"".equals(dmaInfo.getBDataParent_DMA())) {
			    dmaIds += (dmaInfo.getID() + ", ");
			}
		}
		dmaIds = dmaIds.substring(0, dmaIds.lastIndexOf(","));
		
		/**
		 * 通过分区ID集合查询所有分区漏损情况
		 */
		leakageState[] leakEvaluates = leakageStateWebService.getLeakageState_RT(dmaIds);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<leakageState> leakageStateList = new ArrayList<leakageState>();
		for (leakageState dmaLeakEvaluate : leakEvaluates) {
			Boolean LeakState = dmaLeakEvaluate.LeakState;
			if(!LeakState) {
				continue;
			}
			leakageStateList.add(dmaLeakEvaluate);
		}
		resultMap.put("total", leakageStateList.size());
		resultMap.put("rows", leakageStateList);
		
		try {
			JSONTool.writeDataResult(resultMap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取某个漏损分区的漏损信息
	 */
	public void getLeakInfoByRegionId() {
		Long regionId = model.getRegionId();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> itemList = new ArrayList<Map<String,Object>>();
		
		leakageEva[] leakageEva = leakageEvaWebService.getLeakageEva_RT(regionId + "");
		leakageEva leakageEva0 = leakageEva[0];
		String[] propNameArray = {"分区编号", "评估日期", "夜间允许最小流量", 
				"夜间允许最小流量", "监测最小流量时间", "日漏失水量", 
				"日供水量", "日漏失率", "阶段漏损控制目标", 
				"管道总长度", "单位管长漏失水量", "监测最大流量（瞬时）",
				"监测最大流量时间", "日均流量（瞬时)"};
		String[] propValueArray = {leakageEva0.BData_DMA, leakageEva0.ReportDate + "", leakageEva0.AllowedMinWater + "", 
				leakageEva0.MinInstantWater + "", leakageEva0.MinInstantWaterTime + "", leakageEva0.LeakWater + "", 
				leakageEva0.SupplyWater + "", leakageEva0.LeakRate + "", leakageEva0.LeakControlRate + "", 
				leakageEva0.PipeLength + "", leakageEva0.LeakWaterPerPipeLeng + "", leakageEva0.MaxInstantWater + "",
				leakageEva0.MaxInstantWaterTime + "", leakageEva0.AvgInstantWater + ""};
		for (int i = 0; i < propValueArray.length; i++) {
			Map<String, Object> itemMap = new HashMap<String, Object>();
			itemMap.put("propName", propNameArray[i]);
			itemMap.put("propValue", propValueArray[i]);
			itemList.add(itemMap);
		}
		resultMap.put("total", itemList.size());
		resultMap.put("rows", itemList);
		
		try {
			JSONTool.writeDataResult(resultMap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * 产销差分析
	 * 
	 */
	public void getAllRegionDisLosses() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String dmaIds = "";
		
		/**
		 * 查询所有分区ID
		 */
		List<DMAInfo> dmaInfos = dmaInfoManage.getAll();
		for (DMAInfo dmaInfo : dmaInfos) {
			if(!"-1".equals(dmaInfo.getBDataParent_DMA()) || !"".equals(dmaInfo.getBDataParent_DMA())) {
			    dmaIds += (dmaInfo.getID() + ", ");
			}
		}
		dmaIds = dmaIds.substring(0, dmaIds.lastIndexOf(","));
		
		leakageState[] leakEvaluates = leakageStateWebService.getLeakageState_RT(dmaIds);
		for (leakageState dmaLeakEvaluate : leakEvaluates) {
			dmaIds += (dmaLeakEvaluate.BData_DMA + ", ");
		}
		dmaIds = dmaIds.substring(0, dmaIds.lastIndexOf(","));
		
		disLosses[] disLossesArray = disLossesWebService.getDislosses_RT(dmaIds);
		resultMap.put("total", disLossesArray.length);
		resultMap.put("rows", disLossesArray);
		
		try {
			JSONTool.writeDataResult(resultMap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 水平衡分析
	 * @return
	 */
	public void waterBalance() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String dmaIds = "";
		
		/**
		 * 查询所有分区ID
		 */
		List<DMAInfo> dmaInfos = dmaInfoManage.getAll();
		for (DMAInfo dmaInfo : dmaInfos) {
			if(!"-1".equals(dmaInfo.getBDataParent_DMA()) || !"".equals(dmaInfo.getBDataParent_DMA())) {
			    dmaIds += (dmaInfo.getID() + ", ");
			}
		}
		dmaIds = dmaIds.substring(0, dmaIds.lastIndexOf(","));
		
		/**
		 * 查询水平衡分析数据
		 */
		WaterBalance[] waterBalanceArray = waterBalanceWebService.getWaterBalance_RT(dmaIds);
		resultMap.put("total", waterBalanceArray.length);
		resultMap.put("rows", waterBalanceArray);
		
		try {
			JSONTool.writeDataResult(resultMap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public LeakageStateWebService getLeakageStateWebService() {
		return leakageStateWebService;
	}

	public void setLeakageStateWebService(
			LeakageStateWebService leakageStateWebService) {
		this.leakageStateWebService = leakageStateWebService;
	}

	public DMAInfoManage getDmaInfoManage() {
		return dmaInfoManage;
	}

	public void setDmaInfoManage(DMAInfoManage dmaInfoManage) {
		this.dmaInfoManage = dmaInfoManage;
	}
	
	public LeakageEvaWebService getLeakageEvaWebService() {
		return leakageEvaWebService;
	}

	public void setLeakageEvaWebService(LeakageEvaWebService leakageEvaWebService) {
		this.leakageEvaWebService = leakageEvaWebService;
	}

	public DisLossesWebService getDisLossesWebService() {
		return disLossesWebService;
	}

	public void setDisLossesWebService(DisLossesWebService disLossesWebService) {
		this.disLossesWebService = disLossesWebService;
	}

	public void prepare() throws Exception {
		
	}

	public RegionManageForm getModel() {
		return this.model;
	}

	public WaterBalanceWebService getWaterBalanceWebService() {
		return waterBalanceWebService;
	}

	public void setWaterBalanceWebService(
			WaterBalanceWebService waterBalanceWebService) {
		this.waterBalanceWebService = waterBalanceWebService;
	}
}
