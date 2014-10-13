package com.casic.alarm.wsclient;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.InvalidPropertiesFormatException;
import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.ser.BeanDeserializerFactory;
import org.apache.axis.encoding.ser.BeanSerializerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.casic.alarm.utils.PropertiesUtil;

/**
 * 调用webservice类
 * @author liuxin
 *
 */
@org.springframework.stereotype.Service
public class WebServiceBaseClient {
	
	private static Logger logger = LoggerFactory.getLogger(WebServiceBaseClient.class);
	protected PropertiesUtil propUtil = new PropertiesUtil("application.properties");
	private String endpoint = null;
	private String namespace = null;
	
	/**
	 * 
	 * 调用webservice基础方法
	 * 
	 * @param paramNameArray    参数名
	 * @param paramValueArray   参数值
	 * @param funcName          方法明
	 * @param registerClass     序列号类
	 * @param retClass          返回的类
	 * @return                  返回调用结果
	 */
	protected Object callService(WebServiceAddParamInterface webServiceAddParamInterface, Object[] paramValueArray, String funcName, Class registerClass, Class retClass) {
		if(endpoint == null || namespace == null) {
			try {
				endpoint = propUtil.getProperty("webservice.endpoint");
				namespace = propUtil.getProperty("webservice.namespace");
			} catch (InvalidPropertiesFormatException e) {
				e.printStackTrace();
				logger.debug(e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				logger.debug(e.getMessage());
			}
		}
		
		Service service = new Service();
		Call call;
		QName qn = new QName(namespace, registerClass.getSimpleName());
		try {
			call = (Call)service.createCall();
			call.registerTypeMapping(registerClass, qn, 
					new BeanSerializerFactory(registerClass, qn), 
					new BeanDeserializerFactory(registerClass, qn));
			        call.setUseSOAPAction(true);
			call.setTargetEndpointAddress(new URL(endpoint));    
			call.setOperationName(new QName(namespace, funcName));
			 webServiceAddParamInterface.addWebServiceParam(call);
			 call.setReturnClass(retClass);
			 call.setSOAPActionURI(namespace + funcName);
			 return call.invoke(paramValueArray);
		} catch (ServiceException e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
		} catch (MalformedURLException e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
		} catch (RemoteException e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
		}
		return null;
	}
	
	interface WebServiceAddParamInterface {
		public void addWebServiceParam(Call call);
	}
}
