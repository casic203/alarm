package com.casic.alarm.web.alarm;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.jasig.cas.client.validation.Assertion;

import com.casic.alarm.domain.DMAInfo;
import com.casic.alarm.domain.PosDma;
import com.casic.alarm.domain.PositionInfo;
import com.casic.alarm.domain.SysLog;
import com.casic.alarm.form.PositionForm;
import com.casic.alarm.manager.DMAInfoManage;
import com.casic.alarm.manager.PosDmaManager;
import com.casic.alarm.manager.PositionInfoManage;
import com.casic.alarm.manager.SysLogManager;
import com.casic.core.json.JSONTool;
import com.casic.core.struts2.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

/**
 * 给水管线监测点管理Action
 * @author liuxin
 *
 */
public class WaterPipelinePositionManageAction extends BaseAction implements ModelDriven<PositionForm>, Preparable{

	@Resource
	private PosDmaManager posDmaManager;
	
	@Resource
	private DMAInfoManage dmaInfoManage;
	
	@Resource
	private PositionInfoManage positionInfoManage;
	
	@Resource
	private SysLogManager logManager;
	
	private Long dmaID;
	
	private String direction;
	
	private PositionForm model = new PositionForm();
	private Assertion objUser;

	public void prepare() throws Exception {
		objUser = (Assertion) ActionContext.getContext().getSession().get("_const_cas_assertion_");
	}

	public String execute() {
		return "success";
	}
	
	public PositionForm getModel() {
		return this.model;
	}

	public void setLogManager(SysLogManager logManager) {
		this.logManager = logManager;
	}

	/**
	 * 新建一个监测点并关联到分区
	 * @throws IOException
	 */
	public void addPosition() throws IOException {
		if(model != null) {
			PositionInfo positionInfo = new PositionInfo();
			positionInfo.setName(model.getName());
			positionInfo.setLongitude(model.getLongitude());
			positionInfo.setLatitude(model.getLatitude());
			positionInfo.setBDataPosType(model.getBDataPosType());
			positionInfo.setSortCode(model.getSortCode());
			positionInfo.setComment(model.getComment());
			positionInfo.setIsUse(true);
			positionInfo.setOperator(model.getOperator());
			positionInfo.setOperateTime(new Date());
			positionInfoManage.save(positionInfo);
			
			DMAInfo loadDMAInfo = dmaInfoManage.load(dmaID);
			PosDma posDma = new PosDma();
		    posDma.setDmaInfo(loadDMAInfo);
		    posDma.setPositionInfo(positionInfo);
		    posDma.setDirection(direction);
		    posDmaManager.save(posDma);
		    
		    SysLog log=new SysLog();
			log.setBusinessName("给水管线位置维护");
			log.setContent("新增位置："+model.getName()+"-"+model.getBDataPosType());
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
				return;
			} catch (IOException e) {
				e.printStackTrace();
				JSONTool.writeMsgResult(false, "保存失败！");
			}
		}
		JSONTool.writeMsgResult(false, "保存失败！");
	}
	
	/**
	 * 关联一个监测点到分区
	 * @throws IOException
	 */
	public void addExistsPosition() {

		/**
		 * 加载监测点和分区实体
		 */
		Long positionID = model.getID();
		Long localDmaID = dmaID;
		PositionInfo positionInfo = positionInfoManage.load(positionID);
		DMAInfo dmaInfo = dmaInfoManage.load(localDmaID);
		
		/**
		 * 关联监测点和分区
		 */
		PosDma posDma = new PosDma();
	    posDma.setDmaInfo(dmaInfo);
	    posDma.setPositionInfo(positionInfo);
	    posDma.setDirection(direction);
	    posDmaManager.save(posDma);
	    
	    SysLog log=new SysLog();
		log.setBusinessName("给水管线位置维护");
		log.setContent("现有位置新增："+model.getName()+"-"+model.getBDataPosType());
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
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void deletePosition() throws IOException {
		if(model != null) {
			Long localDmaID = dmaID;
			Long positionID = model.getID();
			/**
			 * 删除分区和监测点关联实体
			 */
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("dmaID", localDmaID);
			paramMap.put("posID", positionID);
			List<PosDma> posDmaList = posDmaManager.find("from PosDma posDma where posDma.dmaInfo.ID=:dmaID and posDma.positionInfo.ID=:posID", paramMap);
			for (PosDma posDma : posDmaList) {
				posDmaManager.remove(posDma);
				break;
			}
			
			SysLog log=new SysLog();
			log.setBusinessName("给水管线位置维护");
			log.setContent("删除位置："+model.getName()+"-"+model.getBDataPosType());
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
			
			JSONTool.writeMsgResult(true, "删除成功！"); 
			return;
		}
		JSONTool.writeMsgResult(false, "删除失败！");
	}

	public PositionInfoManage getPositionInfoManage() {
		return positionInfoManage;
	}

	public void setPositionInfoManage(PositionInfoManage positionInfoManage) {
		this.positionInfoManage = positionInfoManage;
	}

	public Long getDmaID() {
		return dmaID;
	}

	public void setDmaID(Long dmaID) {
		this.dmaID = dmaID;
	}

	public PosDmaManager getPosDmaManager() {
		return posDmaManager;
	}

	public void setPosDmaManager(PosDmaManager posDmaManager) {
		this.posDmaManager = posDmaManager;
	}

	public DMAInfoManage getDmaInfoManage() {
		return dmaInfoManage;
	}

	public void setDmaInfoManage(DMAInfoManage dmaInfoManage) {
		this.dmaInfoManage = dmaInfoManage;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}
	
	
}
