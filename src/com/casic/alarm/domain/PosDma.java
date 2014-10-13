package com.casic.alarm.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "POSDMA")
@SequenceGenerator(name = "SEQ_POSDMA_ID", sequenceName = "SEQ_POSDMA_ID")
public class PosDma implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 8784794374182869670L;
	private Long ID; //ID     
	private DMAInfo dmaInfo;            //分区ID
	private PositionInfo positionInfo;  //监测点ID
    private String Direction;  //流向     选择输入[{流入：1}，{流出：-1} ]
    
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_POSDMA_ID")
	@Column(name = "ID")
	public Long getID() {
		return ID;
	}
	public void setID(Long iD) {
		ID = iD;
	}

	@ManyToOne(fetch=FetchType.EAGER, targetEntity=DMAInfo.class)
	public DMAInfo getDmaInfo() {
		return dmaInfo;
	}
	public void setDmaInfo(DMAInfo dmaInfo) {
		this.dmaInfo = dmaInfo;
	}
	
	@ManyToOne(fetch=FetchType.EAGER, targetEntity=PositionInfo.class)
	public PositionInfo getPositionInfo() {
		return positionInfo;
	}
	public void setPositionInfo(PositionInfo positionInfo) {
		this.positionInfo = positionInfo;
	}
	
	@Column(name="DIRECTION")
	public String getDirection() {
		return Direction;
	}
	public void setDirection(String direction) {
		Direction = direction;
	}
}
