package com.casic.alarm.web.alarm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.casic.alarm.JSON.DMASaleWaterJSON;
import com.casic.alarm.domain.DMASaleWater;
import com.casic.alarm.manager.DMASaleWaterManager;
import com.casic.core.json.JSONTool;
import com.casic.core.struts2.BaseAction;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

/**
 * DMA售水量查看action
 * @author liuxin
 *
 */
public class DmaSaleWaterViewAction extends BaseAction implements ModelDriven<DMASaleWater>, Preparable {

	private Long dmaId;
	
	@Resource
	private DMASaleWaterManager dmaSaleWaterManager;
	
	public void prepare() throws Exception {
	}

	public DMASaleWater getModel() {
		return null;
	}

	@SuppressWarnings("unchecked")
	public void getSaleWaterList() {
		String hql = "from DMASaleWater saleWater where saleWater.dmaInfo.ID=:dmaId and saleWater.deleteFlag=:deleteFlag";
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		List<DMASaleWaterJSON> dmaSaleWaterJSONList = new ArrayList<DMASaleWaterJSON>();
		
		paramMap.put("dmaId", dmaId);
		paramMap.put("deleteFlag", Boolean.FALSE);
		List<DMASaleWater> saleWaterList = dmaSaleWaterManager.find(hql, paramMap);
		for (DMASaleWater dmaSaleWater : saleWaterList) {
			DMASaleWaterJSON dmaSaleWaterJSON = new DMASaleWaterJSON(dmaSaleWater);
			dmaSaleWaterJSONList.add(dmaSaleWaterJSON);
		}
		resultMap.put("total", dmaSaleWaterJSONList.size());
		resultMap.put("rows", dmaSaleWaterJSONList);
		
		try {
			JSONTool.writeDataResult(resultMap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public DMASaleWaterManager getDmaSaleWaterManager() {
		return dmaSaleWaterManager;
	}

	public void setDmaSaleWaterManager(DMASaleWaterManager dmaSaleWaterManager) {
		this.dmaSaleWaterManager = dmaSaleWaterManager;
	}

	public Long getDmaId() {
		return dmaId;
	}

	public void setDmaId(Long dmaId) {
		this.dmaId = dmaId;
	}
}
