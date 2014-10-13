package com.casic.alarm.form;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SysLogForm {
	private Long id;
	private String businessName;
	private String operationType;
	private String content;
	private String createUser;
	private String createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd hh:MM:ss");
		if(null!=createTime){
			this.createTime = dateFormat.format(createTime);
		}
	}

}
