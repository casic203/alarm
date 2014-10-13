package com.casic.alarm.web.alarm;

import java.io.IOException;
import java.util.Date;

import javax.annotation.Resource;

import org.jasig.cas.client.validation.Assertion;

import com.casic.alarm.domain.AcceptPerson;
import com.casic.alarm.domain.DMAInfo;
import com.casic.alarm.domain.SysLog;
import com.casic.alarm.form.WaterPipelineRegionForm;
import com.casic.alarm.manager.DMAInfoManage;
import com.casic.alarm.manager.SysLogManager;
import com.casic.core.json.JSONTool;
import com.casic.core.struts2.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 给水管线子分区管理Action
 * @author liuxin
 *
 */
public class WaterPipelineSubRegionManageAction extends BaseAction implements ModelDriven<WaterPipelineRegionForm>{

	private WaterPipelineRegionForm model = new WaterPipelineRegionForm();
	private Assertion objUser;

	@Resource
	private DMAInfoManage dmaInfoManage;
	@Resource
	private SysLogManager logManager;
	
	public WaterPipelineRegionForm getModel() {
		return this.model;
	}

	public void setLogManager(SysLogManager logManager) {
		this.logManager = logManager;
	}



	public String execute() {
		return "success";
	}

	public void prepare() throws Exception {
		this.model = new WaterPipelineRegionForm();
		objUser = (Assertion) ActionContext.getContext().getSession().get("_const_cas_assertion_");
	}
	/**
	 * 添加子分区
	 * @throws IOException 
	 */
	public void addSubDMA() throws IOException {
		if(model != null) {
			DMAInfo dmaInfo = new DMAInfo();
			dmaInfo.setName(model.getName());
			dmaInfo.setNo(model.getNo());
			dmaInfo.setBDataParent_DMA(model.getBDataParent_DMA());
			dmaInfo.setUserCount(model.getUserCount());
			dmaInfo.setNormalWater(model.getNormalWater());
			dmaInfo.setPipeLeng(model.getPipeLeng());
			dmaInfo.setUserPipeLeng(model.getUserPipeLeng());
			dmaInfo.setPipeLinks(model.getPipeLinks());
			dmaInfo.setIcf(model.getIcf());
			dmaInfo.setLeakControlRate(model.getLeakControlRate());
			dmaInfo.setSaleWater(model.getSaleWater());
			dmaInfoManage.save(dmaInfo);
			
			SysLog log=new SysLog();
			log.setBusinessName("给水管线子分区维护");
			log.setContent("新增子分区："+model.getNo()+"-"+model.getName());
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
				JSONTool.writeMsgResult(true, "保存完成！");
			} catch (IOException e) {
				e.printStackTrace();
				JSONTool.writeMsgResult(false, "保存失败！");
			}
			return;
		}
		JSONTool.writeMsgResult(false, "保存失败！");
	}
	
	/**
	 * 修改子分区
	 */
	public void modifySubDMA() {
		
	}
	
	/**
	 * 删除子分区
	 * @throws IOException 
	 */
	public void delSubDMA() throws IOException {
		if(model != null) {
			Long regionID = model.getRegionID();
			DMAInfo dmaInfo = dmaInfoManage.load(regionID);
			dmaInfo.setActive(false);
			dmaInfoManage.save(dmaInfo);
			
			SysLog log=new SysLog();
			log.setBusinessName("给水管线子分区维护");
			log.setContent("删除子分区："+model.getNo()+"-"+model.getName());
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
				JSONTool.writeMsgResult(true, "删除成功！");
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		JSONTool.writeMsgResult(false, "删除失败！");
	}

	public DMAInfoManage getDmaInfoManage() {
		return dmaInfoManage;
	}

	public void setDmaInfoManage(DMAInfoManage dmaInfoManage) {
		this.dmaInfoManage = dmaInfoManage;
	}
}
