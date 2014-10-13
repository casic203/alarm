package com.casic.alarm.wsclient;

import java.io.IOException;
import java.util.Date;
import java.util.InvalidPropertiesFormatException;
import javax.xml.namespace.QName;
import org.apache.axis.client.Call;
import org.springframework.stereotype.Service;

import com.casic.alarm.wsclient.entry.disLosses;

/**
 * 产销差分析webservice
 * @author liuxin
 *
 */
@Service
public class DisLossesWebService extends WebServiceBaseClient {
	public disLosses[] getDislosses_RT(String dmaID) {
		String isDebug = "false";
		try {
			isDebug = (String) propUtil.getProperty("debug");
		} catch (InvalidPropertiesFormatException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		Object disLossesObj = null;
		if ("false".equals(isDebug)) { //读取真实数据
			disLossesObj = this.callService(
					new WebServiceAddParamInterface() {
						public void addWebServiceParam(Call call) {
							String namespace = null;
							try {
								namespace = propUtil
										.getProperty("webservice.namespace");
								call.addParameter(
										new QName(namespace, "dmaID"),
										org.apache.axis.encoding.XMLType.XSD_STRING,
										javax.xml.rpc.ParameterMode.IN);
							} catch (InvalidPropertiesFormatException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}, new String[] { dmaID }, "RT_DisLosses", disLosses.class,
					disLosses[].class);
		} else { 
			disLossesObj = getDisLossesData();
		}
		return (disLosses[]) disLossesObj;
	}
	
	public disLosses[] getDisLossesData() {
		disLosses dLosses0 = new disLosses();
		dLosses0.BData_DMA = "001";
		dLosses0.ReportDate = new Date();
		dLosses0.SupplyWater = 900.0;
		dLosses0.SaleWater = 800.0;
		dLosses0.SaleDiffWater = 100.0;
		dLosses0.SaleDiffWaterRate = 0.55;
		
		disLosses dLosses1 = new disLosses();
		dLosses1.BData_DMA = "002";
		dLosses1.ReportDate = new Date();
		dLosses1.SupplyWater = 900.0;
		dLosses1.SaleWater = 800.0;
		dLosses1.SaleDiffWater = 100.0;
		dLosses1.SaleDiffWaterRate = 0.55;
		
		disLosses dLosses2 = new disLosses();
		dLosses2.BData_DMA = "003";
		dLosses2.ReportDate = new Date();
		dLosses2.SupplyWater = 900.0;
		dLosses2.SaleWater = 800.0;
		dLosses2.SaleDiffWater = 100.0;
		dLosses2.SaleDiffWaterRate = 0.55;
		
		disLosses dLosses3 = new disLosses();
		dLosses3.BData_DMA = "004";
		dLosses3.ReportDate = new Date();
		dLosses3.SupplyWater = 900.0;
		dLosses3.SaleWater = 800.0;
		dLosses3.SaleDiffWater = 100.0;
		dLosses3.SaleDiffWaterRate = 0.55;
		
		disLosses[] dLossesArray = {dLosses0, dLosses1, dLosses2, dLosses3};
		return dLossesArray;
	}
}
