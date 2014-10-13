package com.casic.alarm.JSON;

public class RegionTreeJSON {
	private Long id;
	private String deviceCode;
	private String elementName;
	private Long _parentId;
	private String iconCls;
	private Boolean isRegion;
	private Boolean checked = true;
//	private String state = "closed";
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getElementName() {
		return elementName;
	}
	public void setElementName(String elementName) {
		this.elementName = elementName;
	}
	public Long get_parentId() {
		return _parentId;
	}
	public void set_parentId(Long _parentId) {
		this._parentId = _parentId;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public Boolean getIsRegion() {
		return isRegion;
	}
	public void setIsRegion(Boolean isRegion) {
		this.isRegion = isRegion;
	}
	public Boolean getChecked() {
		return checked;
	}
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	public String getDeviceCode() {
		return deviceCode;
	}
	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}
}
