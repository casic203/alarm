package com.casic.alarm.wsclient;

import java.io.IOException;
import java.util.Date;
import java.util.InvalidPropertiesFormatException;
import javax.xml.namespace.QName;
import org.apache.axis.client.Call;
import org.springframework.stereotype.Service;

import com.casic.alarm.wsclient.entry.leakageState;

/**
 * 分区状态Webservice
 * @author liuxin
 *
 */
@Service
public class LeakageStateWebService extends WebServiceBaseClient {
	public leakageState[] getLeakageState_RT(String dmaID) {
		String isDebug = "false"; 
		try {
			isDebug = (String)propUtil.getProperty("debug");
		} catch (InvalidPropertiesFormatException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		Object leakageStateObj = null;
		if("false".equals(isDebug)) {
			 leakageStateObj = this.callService(new WebServiceAddParamInterface() {
				public void addWebServiceParam(Call call) {
					String namespace = null;
					try {
						namespace = propUtil.getProperty("webservice.namespace");
				        call.addParameter( new QName(namespace, "dmaID"),  
				                org.apache.axis.encoding.XMLType.XSD_STRING,   
				                javax.xml.rpc.ParameterMode.IN);
					} catch (InvalidPropertiesFormatException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}, new String[]{dmaID}, "RT_LeakageState", leakageState.class, leakageState[].class);
		} else {
			leakageStateObj = getLeakageStateData();
		}
		return (leakageState[])leakageStateObj;
	}
	
	public leakageState[] getLeakageStateData() {
		leakageState leakageState0 = new leakageState();
		leakageState0.BData_DMA = "001";
		leakageState0.ReportDate = new Date();
		leakageState0.LeakRate = 0.12;
		leakageState0.LeakControlRate = 0.56;
		leakageState0.SaleDiffWaterRate = 0.68;
		leakageState0.LeakState = false;
		
		leakageState leakageState1 = new leakageState();
		leakageState1.BData_DMA = "002";
		leakageState1.ReportDate = new Date();
		leakageState1.LeakRate = 0.22;
		leakageState1.LeakControlRate = 0.86;
		leakageState1.SaleDiffWaterRate = 0.28;
		leakageState1.LeakState = false;
		
		leakageState leakageState2 = new leakageState();
		leakageState2.BData_DMA = "003";
		leakageState2.ReportDate = new Date();
		leakageState2.LeakRate = 0.49;
		leakageState2.LeakControlRate = 0.66;
		leakageState2.SaleDiffWaterRate = 0.98;
		leakageState2.LeakState = true;
		
		leakageState leakageState3 = new leakageState();
		leakageState3.BData_DMA = "004";
		leakageState3.ReportDate = new Date();
		leakageState3.LeakRate = 0.82;
		leakageState3.LeakControlRate = 0.56;
		leakageState3.SaleDiffWaterRate = 0.68;
		leakageState3.LeakState = true;
		
		leakageState[] leakageStateArray = {leakageState0, leakageState1, leakageState2, leakageState3};
		return leakageStateArray;
	}
}
