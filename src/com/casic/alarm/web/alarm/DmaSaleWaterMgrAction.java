package com.casic.alarm.web.alarm;

import java.io.IOException;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.Resource;

import org.jasig.cas.client.validation.Assertion;

import com.casic.alarm.domain.AcceptPerson;
import com.casic.alarm.domain.DMAInfo;
import com.casic.alarm.domain.DMASaleWater;
import com.casic.alarm.domain.SysLog;
import com.casic.alarm.manager.DMAInfoManage;
import com.casic.alarm.manager.DMASaleWaterManager;
import com.casic.alarm.manager.SysLogManager;
import com.casic.core.json.JSONTool;
import com.casic.core.struts2.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * DMA售水量管理Action
 * @author liuxin
 *
 */
public class DmaSaleWaterMgrAction extends BaseAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5699705866372334707L;
	private Long dmaId;
	private String beginDate;
	private String endDate;
	private String water;
	private String noValueWater;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	private DMASaleWater model = new DMASaleWater();
	private Assertion objUser;
	
	@Resource
	private DMASaleWaterManager dmaSaleWaterManager;
	
	@Resource
	private DMAInfoManage dmaInfoManage;
	
	@Resource
	private SysLogManager logManager;

	public DMASaleWater getModel() {
		return this.model;
	}
	
	public void prepare() throws Exception {
		this.model = new DMASaleWater();
		objUser = (Assertion) ActionContext.getContext().getSession().get("_const_cas_assertion_");
	}
	
	public void add() {
		try {
			model.setStartDate(dateFormat.parse(beginDate));
			model.setEndDate(dateFormat.parse(endDate));
			model.setSaleWater(Double.parseDouble(water));
			model.setNoValueWater(Double.parseDouble(noValueWater));
			model.setInsertDate(new Date());
			model.setUpdateDate(new Date());
			model.setDeleteFlag(Boolean.FALSE);
			
			DMAInfo dmaInfo = dmaInfoManage.get(dmaId);
			model.setDmaInfo(dmaInfo);
			dmaSaleWaterManager.save(model);
			
			SysLog log=new SysLog();
			log.setBusinessName("分区售水维护");
			log.setContent("新增售水："+model.getDmaInfo().getName()+"-"+model.getSaleWater());
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
		} catch (Exception e) {
			try {
				JSONTool.writeMsgResult(true, "添加失败！");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		}
		
		try {
			JSONTool.writeMsgResult(true, "添加成功！");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void delete() {
		try {
			Long saleWaterId = model.getId();
			DMASaleWater dmaSaleWater = dmaSaleWaterManager.get(saleWaterId);
			if(null != dmaSaleWater) {
				dmaSaleWaterManager.remove(dmaSaleWater);
				SysLog log=new SysLog();
				log.setBusinessName("分区售水维护");
				log.setContent("删除售水："+model.getDmaInfo().getName()+"-"+model.getSaleWater());
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
			}
		} catch (Exception e) {
			try {
				JSONTool.writeMsgResult(true, "删除失败！");
				return;
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		
		try {
			JSONTool.writeMsgResult(true, "删除成功！");
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

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}


	public String getWater() {
		return water;
	}

	public void setWater(String water) {
		this.water = water;
	}

	public String getNoValueWater() {
		return noValueWater;
	}

	public void setNoValueWater(String noValueWater) {
		this.noValueWater = noValueWater;
	}

	public DMAInfoManage getDmaInfoManage() {
		return dmaInfoManage;
	}

	public void setDmaInfoManage(DMAInfoManage dmaInfoManage) {
		this.dmaInfoManage = dmaInfoManage;
	}

	public void setLogManager(SysLogManager logManager) {
		this.logManager = logManager;
	}
	
}
