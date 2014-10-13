package com.casic.alarm.wsclient;

import java.io.IOException;
import java.util.Date;
import java.util.InvalidPropertiesFormatException;
import javax.xml.namespace.QName;
import org.apache.axis.client.Call;
import org.springframework.stereotype.Service;

import com.casic.alarm.wsclient.entry.WaterBalance;

/**
 * 水平衡分区web service
 * @author liuxin
 *
 */
@Service
public class WaterBalanceWebService extends WebServiceBaseClient {
	public WaterBalance[] getWaterBalance_RT(String dmaID) {
		String isDebug = "false"; 
		try {
			isDebug = (String)propUtil.getProperty("debug");
		} catch (InvalidPropertiesFormatException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		Object waterBalanceObj = null;
		if("false".equals(isDebug)) {
			waterBalanceObj = this.callService(new WebServiceAddParamInterface() {
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
			}, new String[]{dmaID}, "RT_WaterBalance", WaterBalance.class, WaterBalance[].class);
		} else {
			waterBalanceObj = getWaterBalanceTestData();
		}
		return (WaterBalance[])waterBalanceObj;
	}
	
	public WaterBalance[] getWaterBalanceTestData() {
		WaterBalance waterBalance0 = new WaterBalance();
		waterBalance0.DmaID = "001";
		waterBalance0.AnalysisDate = new Date();
		waterBalance0.WaterSupply = 100.87;
		waterBalance0.WaterSale = 234.9;
		waterBalance0.NoValueWater = 98.2;
		waterBalance0.LR_Leakage = 0.02;
		waterBalance0.LR_WaterME = 0.2;
		waterBalance0.LR_MeterE = 0.4;
		waterBalance0.LR_Favor = 90.8;
		waterBalance0.LR_Steal = 90.8;
		waterBalance0.LR_Pressure = 90.6;
		waterBalance0.SaleDiffWater = 45.9;
		
		WaterBalance waterBalance1 = new WaterBalance();
		waterBalance1.DmaID = "002";
		waterBalance1.AnalysisDate = new Date();
		waterBalance1.WaterSupply = 100.87;
		waterBalance1.WaterSale = 234.9;
		waterBalance1.NoValueWater = 98.2;
		waterBalance1.LR_Leakage = 0.02;
		waterBalance1.LR_WaterME = 0.2;
		waterBalance1.LR_MeterE = 0.4;
		waterBalance1.LR_Favor = 90.8;
		waterBalance1.LR_Steal = 90.8;
		waterBalance1.LR_Pressure = 90.6;
		waterBalance1.SaleDiffWater = 45.9;
		
		WaterBalance waterBalance2 = new WaterBalance();
		waterBalance2.DmaID = "003";
		waterBalance2.AnalysisDate = new Date();
		waterBalance2.WaterSupply = 100.87;
		waterBalance2.WaterSale = 234.9;
		waterBalance2.NoValueWater = 98.2;
		waterBalance2.LR_Leakage = 0.02;
		waterBalance2.LR_WaterME = 0.2;
		waterBalance2.LR_MeterE = 0.4;
		waterBalance2.LR_Favor = 90.8;
		waterBalance2.LR_Steal = 90.8;
		waterBalance2.LR_Pressure = 90.6;
		waterBalance2.SaleDiffWater = 45.9;
		
		WaterBalance waterBalance3 = new WaterBalance();
		waterBalance3.DmaID = "004";
		waterBalance3.AnalysisDate = new Date();
		waterBalance3.WaterSupply = 100.87;
		waterBalance3.WaterSale = 234.9;
		waterBalance3.NoValueWater = 98.2;
		waterBalance3.LR_Leakage = 0.02;
		waterBalance3.LR_WaterME = 0.2;
		waterBalance3.LR_MeterE = 0.4;
		waterBalance3.LR_Favor = 90.8;
		waterBalance3.LR_Steal = 90.8;
		waterBalance3.LR_Pressure = 90.6;
		waterBalance3.SaleDiffWater = 45.9;
		
		WaterBalance[] waterBalanceArray = {waterBalance0, waterBalance1, waterBalance2, waterBalance3};
		return waterBalanceArray;
	}
}
