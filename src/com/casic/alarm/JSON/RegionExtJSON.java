package com.casic.alarm.JSON;

public class RegionExtJSON {
	private Long id;
	private String regionName;
	private String regionArea;
	private Long _parentId;
	private String parentName;
	private String deviceIds;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public Long get_parentId() {
		return _parentId;
	}
	public void set_parentId(Long _parentId) {
		this._parentId = _parentId;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public String getDeviceIds() {
		return deviceIds;
	}
	public void setDeviceIds(String deviceIds) {
		this.deviceIds = deviceIds;
	}
}
