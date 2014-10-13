package com.casic.alarm.domain;
// default package

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * AlarmDeviceSensorformat entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ALARM_DEVICE_SENSORFORMAT")
public class DeviceSensorformat implements java.io.Serializable {

	// Fields

	private DeviceSensorformatId dsfid;
	private Long sortid;
	private String itemnameCn;
	private Long beginpos;
	private Long itemlen;
	private Long isdatacol;
	private Long ismulitdata;
	private Long isdefaultcol;
	private Long issecucol;
	private String standardcol;
	private String coldatatype;
	private Long collen;
	private String escapecol;
	private Long isshow;
	private Long interfacevaluepx;
	private Long isshow2;
	private String itemvalue;

	// Constructors

	/** default constructor */
	public DeviceSensorformat() {
	}

	/** minimal constructor */
	public DeviceSensorformat(DeviceSensorformatId dsfid) {
		this.dsfid = dsfid;
	}

	/** full constructor */
	public DeviceSensorformat(DeviceSensorformatId dsfid, Long sortid,
			String itemnameCn, Long beginpos, Long itemlen, Long isdatacol,
			Long ismulitdata, Long isdefaultcol, Long issecucol,
			String standardcol, String coldatatype, Long collen,
			String escapecol, Long isshow, Long interfacevaluepx, Long isshow2,String itemvalue) {
		this.dsfid = dsfid;
		this.sortid = sortid;
		this.itemnameCn = itemnameCn;
		this.beginpos = beginpos;
		this.itemlen = itemlen;
		this.isdatacol = isdatacol;
		this.ismulitdata = ismulitdata;
		this.isdefaultcol = isdefaultcol;
		this.issecucol = issecucol;
		this.standardcol = standardcol;
		this.coldatatype = coldatatype;
		this.collen = collen;
		this.escapecol = escapecol;
		this.isshow = isshow;
		this.interfacevaluepx = interfacevaluepx;
		this.isshow2 = isshow2;
		this.itemvalue = itemvalue;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "sensortype", column = @Column(name = "SENSORTYPE", nullable = false, length = 4)),
			@AttributeOverride(name = "itemname", column = @Column(name = "ITEMNAME", nullable = false, length = 30)) })
	public DeviceSensorformatId getDsfid() {
		return dsfid;
	}

	public void setDsfid(DeviceSensorformatId dsfid) {
		this.dsfid = dsfid;
	}

	@Column(name = "SORTID")
	public Long getSortid() {
		return this.sortid;
	}

	public void setSortid(Long sortid) {
		this.sortid = sortid;
	}

	@Column(name = "ITEMNAME_CN")
	public String getItemnameCn() {
		return this.itemnameCn;
	}

	public void setItemnameCn(String itemnameCn) {
		this.itemnameCn = itemnameCn;
	}

	@Column(name = "BEGINPOS")
	public Long getBeginpos() {
		return this.beginpos;
	}

	public void setBeginpos(Long beginpos) {
		this.beginpos = beginpos;
	}

	@Column(name = "ITEMLEN")
	public Long getItemlen() {
		return this.itemlen;
	}

	public void setItemlen(Long itemlen) {
		this.itemlen = itemlen;
	}

	@Column(name = "ISDATACOL")
	public Long getIsdatacol() {
		return this.isdatacol;
	}

	public void setIsdatacol(Long isdatacol) {
		this.isdatacol = isdatacol;
	}

	@Column(name = "ISMULITDATA")
	public Long getIsmulitdata() {
		return this.ismulitdata;
	}

	public void setIsmulitdata(Long ismulitdata) {
		this.ismulitdata = ismulitdata;
	}

	@Column(name = "ISDEFAULTCOL")
	public Long getIsdefaultcol() {
		return this.isdefaultcol;
	}

	public void setIsdefaultcol(Long isdefaultcol) {
		this.isdefaultcol = isdefaultcol;
	}

	@Column(name = "ISSECUCOL")
	public Long getIssecucol() {
		return this.issecucol;
	}

	public void setIssecucol(Long issecucol) {
		this.issecucol = issecucol;
	}

	@Column(name = "STANDARDCOL")
	public String getStandardcol() {
		return this.standardcol;
	}

	public void setStandardcol(String standardcol) {
		this.standardcol = standardcol;
	}

	@Column(name = "COLDATATYPE")
	public String getColdatatype() {
		return this.coldatatype;
	}

	public void setColdatatype(String coldatatype) {
		this.coldatatype = coldatatype;
	}

	@Column(name = "COLLEN")
	public Long getCollen() {
		return this.collen;
	}

	public void setCollen(Long collen) {
		this.collen = collen;
	}

	@Column(name = "ESCAPECOL")
	public String getEscapecol() {
		return this.escapecol;
	}

	public void setEscapecol(String escapecol) {
		this.escapecol = escapecol;
	}

	@Column(name = "ISSHOW")
	public Long getIsshow() {
		return this.isshow;
	}

	public void setIsshow(Long isshow) {
		this.isshow = isshow;
	}

	@Column(name = "INTERFACEVALUEPX")
	public Long getInterfacevaluepx() {
		return this.interfacevaluepx;
	}

	public void setInterfacevaluepx(Long interfacevaluepx) {
		this.interfacevaluepx = interfacevaluepx;
	}

	@Column(name = "ISSHOW2")
	public Long getIsshow2() {
		return this.isshow2;
	}

	public void setIsshow2(Long isshow2) {
		this.isshow2 = isshow2;
	}
	
	@Column(name = "ITEMVALUE")
	public String getItemvalue() {
		return itemvalue;
	}

	public void setItemvalue(String itemvalue) {
		this.itemvalue = itemvalue;
	}

}