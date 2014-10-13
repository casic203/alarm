package com.casic.alarm.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;
import java.util.Properties;

public class PropertiesUtil {
	private Properties properties;
	private String propName;
	private Map<String, String> memoryPropMap = new HashMap<String, String>();

	public PropertiesUtil(String propFileName) {
		properties=new Properties();
		this.propName = propFileName;
	}

	public String getProperty(String key)
			throws InvalidPropertiesFormatException, IOException {
		String retVal = null;
		String memVal = memoryPropMap.get(key);
		if(null == memVal) {
		    InputStream in = this.getClass().getClassLoader()
				.getResourceAsStream("/" + propName);
		    properties.load(in);
		    in.close();
		    retVal = (String)properties.get(key);
		    memoryPropMap.put(key, retVal);
		} else {
			retVal = memVal;
		}
		return retVal;
	}
}
