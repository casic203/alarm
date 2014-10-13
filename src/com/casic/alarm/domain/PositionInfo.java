package com.casic.alarm.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "POSITIONINFO")
@SequenceGenerator(name = "SEQ_POSITIONINFO_ID", sequenceName = "SEQ_POSITIONINFO_ID")
public class PositionInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5403665731702843740L;

	public PositionInfo() {

	}

	private Long ID; // ID
	private String Name; // 监测点名称
	private String Longitude; // 经度
	private String Latitude; // 纬度
	private String BDataPosType; // 监测点类型
	private String SortCode; // 排序码
	private String Comment; // 备注
	private Boolean IsUse; // 是否启用
	private String Operator; // 操作人
	private Date OperateTime; // 操作时间
	private Set<PosDma> equipmentInfoSet = new HashSet<PosDma>();
	private Set<DevPos> eqtInPosSet = new HashSet<DevPos>();
	private Boolean active = true;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_POSITIONINFO_ID")
	@Column(name = "ID")
	public Long getID() {
		return ID;
	}
	public void setID(Long iD) {
		ID = iD;
	}
	
	@Column(name = "NAME")
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	
	@Column(name = "LONGITUDE")
	public String getLongitude() {
		return Longitude;
	}
	public void setLongitude(String longitude) {
		Longitude = longitude;
	}
	
	@Column(name = "LATITUDE")
	public String getLatitude() {
		return Latitude;
	}
	public void setLatitude(String latitude) {
		Latitude = latitude;
	}
	
	@Column(name = "BDATAPOSTYPE")
	public String getBDataPosType() {
		return BDataPosType;
	}
	public void setBDataPosType(String bDataPosType) {
		BDataPosType = bDataPosType;
	}
	
	@Column(name = "SORTCODE")
	public String getSortCode() {
		return SortCode;
	}
	public void setSortCode(String sortCode) {
		SortCode = sortCode;
	}
	
	@Column(name = "POS_COMMENT")
	public String getComment() {
		return Comment;
	}
	public void setComment(String comment) {
		Comment = comment;
	}
	
	@Column(name = "ISUSE")
	public Boolean getIsUse() {
		return IsUse;
	}
	public void setIsUse(Boolean isUse) {
		IsUse = isUse;
	}
	
	@Column(name = "OPERATOR")
	public String getOperator() {
		return Operator;
	}
	public void setOperator(String operator) {
		Operator = operator;
	}
	
	@Column(name = "OPERATETIME")
	public Date getOperateTime() {
		return OperateTime;
	}
	public void setOperateTime(Date operateTime) {
		OperateTime = operateTime;
	}
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="positionInfo", targetEntity=PosDma.class)
	public Set<PosDma> getEquipmentInfoSet() {
		return equipmentInfoSet;
	}
	public void setEquipmentInfoSet(Set<PosDma> equipmentInfoList) {
		this.equipmentInfoSet = equipmentInfoList;
	}
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="positionInfo", targetEntity=DevPos.class)
	public Set<DevPos> getEqtInPosSet() {
		return eqtInPosSet;
	}
	public void setEqtInPosSet(Set<DevPos> eqtInPosSet) {
		this.eqtInPosSet = eqtInPosSet;
	}
	
	@Column(name = "ACTIVE")
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
}
