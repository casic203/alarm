package com.casic.alarm.domain;

import java.io.Serializable;

public class GisAlarmRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5793473899419391944L;

	private String dev;
	private Integer count;

	public String getDev() {
		return dev;
	}

	public void setDev(String dev) {
		this.dev = dev;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
