package com.casic.alarm.wsclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

import javax.xml.namespace.QName;
import org.apache.axis.client.Call;
import org.springframework.stereotype.Service;

import com.casic.alarm.wsclient.entry.leakageEva;

/**
 * 漏损评估webservice
 * @author liuxin
 *
 */
@Service
public class LeakageEvaWebService extends WebServiceBaseClient {
	public leakageEva[] getLeakageEva_RT(String dmaID) {
		String isDebug = "false";
		try {
			isDebug = (String) propUtil.getProperty("debug");
		} catch (InvalidPropertiesFormatException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		Object leakageEvaObj = null;
		if ("false".equals(isDebug)) {
			leakageEvaObj = this.callService(
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
					}, new String[] { dmaID }, "RT_Evaluate", leakageEva.class,
					leakageEva[].class);
		} else {
			leakageEvaObj = getLeakageEva(dmaID);
		}
		return (leakageEva[])leakageEvaObj;
	}

	public leakageEva[] getLeakageEva(String dmaID) {
		leakageEva leakageEva0 = new leakageEva();
		leakageEva0.BData_DMA = "001";
		leakageEva0.ReportDate = new Date();
		leakageEva0.AllowedMinWater = 900.9;
		leakageEva0.MinInstantWater = 345.0;
		leakageEva0.MinInstantWaterTime = new Date();
		leakageEva0.LeakWater = 9.09;
		leakageEva0.SupplyWater = 9098.9;
		leakageEva0.LeakRate = 9.02;
		leakageEva0.LeakControlRate = 0.98;
		leakageEva0.PipeLength = 1000.0;
		leakageEva0.LeakWaterPerPipeLeng = 908.0;
		leakageEva0.MaxInstantWater = 345.9;
		leakageEva0.MaxInstantWaterTime = new Date();
		leakageEva0.AvgInstantWater = 34.5;
		
		leakageEva leakageEva1 = new leakageEva();
		leakageEva1.BData_DMA = "002";
		leakageEva1.ReportDate = new Date();
		leakageEva1.AllowedMinWater = 900.9;
		leakageEva1.MinInstantWater = 345.0;
		leakageEva1.MinInstantWaterTime = new Date();
		leakageEva1.LeakWater = 9.09;
		leakageEva1.SupplyWater = 9098.9;
		leakageEva1.LeakRate = 9.02;
		leakageEva1.LeakControlRate = 0.98;
		leakageEva1.PipeLength = 1000.0;
		leakageEva1.LeakWaterPerPipeLeng = 908.0;
		leakageEva1.MaxInstantWater = 345.9;
		leakageEva1.MaxInstantWaterTime = new Date();
		leakageEva1.AvgInstantWater = 34.5;
		
		leakageEva leakageEva2 = new leakageEva();
		leakageEva2.BData_DMA = "003";
		leakageEva2.ReportDate = new Date();
		leakageEva2.AllowedMinWater = 900.9;
		leakageEva2.MinInstantWater = 345.0;
		leakageEva2.MinInstantWaterTime = new Date();
		leakageEva2.LeakWater = 9.09;
		leakageEva2.SupplyWater = 9098.9;
		leakageEva2.LeakRate = 9.02;
		leakageEva2.LeakControlRate = 0.98;
		leakageEva2.PipeLength = 1000.0;
		leakageEva2.LeakWaterPerPipeLeng = 908.0;
		leakageEva2.MaxInstantWater = 345.9;
		leakageEva2.MaxInstantWaterTime = new Date();
		leakageEva2.AvgInstantWater = 34.5;
		
		leakageEva leakageEva3 = new leakageEva();
		leakageEva3.BData_DMA = "004";
		leakageEva3.ReportDate = new Date();
		leakageEva3.AllowedMinWater = 900.9;
		leakageEva3.MinInstantWater = 345.0;
		leakageEva3.MinInstantWaterTime = new Date();
		leakageEva3.LeakWater = 9.09;
		leakageEva3.SupplyWater = 9098.9;
		leakageEva3.LeakRate = 9.02;
		leakageEva3.LeakControlRate = 0.98;
		leakageEva3.PipeLength = 1000.0;
		leakageEva3.LeakWaterPerPipeLeng = 908.0;
		leakageEva3.MaxInstantWater = 345.9;
		leakageEva3.MaxInstantWaterTime = new Date();
		leakageEva3.AvgInstantWater = 34.5;
		
		leakageEva[] leakageEvaArray = {leakageEva0, leakageEva1, leakageEva2, leakageEva3};
		List<leakageEva> leakageEvaList = new ArrayList<leakageEva>();
		for (leakageEva leakageEva : leakageEvaArray) {
			if(Long.parseLong(dmaID) == Long.parseLong(leakageEva.BData_DMA)) {
				leakageEvaList.add(leakageEva);
				return leakageEvaList.toArray(leakageEvaArray);
			}
		}
		leakageEvaList.add(leakageEva0);
		return leakageEvaList.toArray(leakageEvaArray);
	}
}
