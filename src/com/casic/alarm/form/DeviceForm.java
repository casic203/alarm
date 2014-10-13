package com.casic.alarm.form;

import java.util.Date;

public class DeviceForm {
	private Long id;
	private String no; // 设备编码
	private String installPosition; // 安装位置
	private String simid; // SIM卡号
	private Date beginUseTime; // 开始使用时间

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getInstallPosition() {
		return installPosition;
	}

	public void setInstallPosition(String installPosition) {
		this.installPosition = installPosition;
	}

	public String getSimid() {
		return simid;
	}

	public void setSimid(String simid) {
		this.simid = simid;
	}

	public Date getBeginUseTime() {
		return beginUseTime;
	}

	public void setBeginUseTime(Date beginUseTime) {
		this.beginUseTime = beginUseTime;
	}

}
