package com.casic.core.prop;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

public class PropertiesTool {
	public Properties properties;
	
	public  PropertiesTool() {
		properties=new Properties();
	}

	public Object getProperty(String key)
			throws InvalidPropertiesFormatException, IOException {
		InputStream in = this.getClass().getClassLoader()
				.getResourceAsStream("/db_back_up_cfg.properties");
		properties.load(in);
		in.close();
		return properties.get(key);
	}

	public void setProperty(String key, String value) throws IOException {
		OutputStream out = new FileOutputStream(this.getClass()
				.getResource("/db_back_up_cfg.properties").getPath());
		properties.setProperty(key, value);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		properties.store(out, format.format(new Date()));
	}
}
