package com.casic.alarm.form;

public class RegionManageForm {
	
	private Long regionId;
	private String regionName;
	private String regionArea;
	private Long _parentRegionId;
	private String deviceIds;
	
	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getRegionArea() {
		return regionArea;
	}

	public void setRegionArea(String regionArea) {
		this.regionArea = regionArea;
	}

	public Long get_parentRegionId() {
		return _parentRegionId;
	}

	public void set_parentRegionId(Long _parentRegionId) {
		this._parentRegionId = _parentRegionId;
	}

	public String getDeviceIds() {
		return deviceIds;
	}

	public void setDeviceIds(String deviceIds) {
		this.deviceIds = deviceIds;
	}
}
