 package com.casic.core.spring;
 
 import java.io.ByteArrayOutputStream;
 import java.io.IOException;
 import java.io.InputStream;
 import java.io.UnsupportedEncodingException;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 import java.util.Properties;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.beans.factory.FactoryBean;
 import org.springframework.beans.factory.InitializingBean;
 import org.springframework.core.io.DefaultResourceLoader;
 import org.springframework.core.io.Resource;
 import org.springframework.core.io.ResourceLoader;
 import org.springframework.core.io.support.PropertiesLoaderSupport;
 
 public class ApplicationPropertiesFactoryBean extends PropertiesLoaderSupport
   implements FactoryBean<Properties>, InitializingBean
 {
   private static final int DEFAULT_BUFFER_SIZE = 1024;
   private static Logger logger = LoggerFactory.getLogger(ApplicationPropertiesFactoryBean.class);
 
   private ResourceLoader resourceLoader = new DefaultResourceLoader();
   private Properties properties;
 
   public boolean isSingleton()
   {
     return true;
   }
 
   public void afterPropertiesSet() throws IOException
   {
     readProperties();
 
     setIgnoreResourceNotFound(true);
 
     this.properties = mergeProperties();
   }
 
   public Properties getObject() throws IOException {
     return this.properties;
   }
 
   public Class<Properties> getObjectType() {
     return Properties.class;
   }
 
   protected void readProperties() throws UnsupportedEncodingException {
     Resource propertiesListResource = this.resourceLoader.getResource("classpath:/properties.lst");
 
     List resources = new ArrayList();
 
     if (propertiesListResource == null) {
       logger.info("use default properties");
       resources.add(this.resourceLoader.getResource("classpath:/application.properties"));
 
       resources.add(this.resourceLoader.getResource("classpath:/application.local.properties"));
 
       resources.add(this.resourceLoader.getResource("classpath:/application.server.properties"));
     }
     else {
       ByteArrayOutputStream baos = new ByteArrayOutputStream();
       byte[] b = new byte[1024];
       int len = 0;
       InputStream is = null;
       try
       {
         is = propertiesListResource.getInputStream();
 
         while ((len = is.read(b, 0, 1024)) != -1)
           baos.write(b, 0, len);
       }
       catch (Exception ex) {
         logger.error("", ex);
       } finally {
         try {
           is.close();
         } catch (Exception ex) {
           logger.error("", ex);
         }
       }
 
       String text = new String(baos.toByteArray(), "UTF-8");
 
       for (String str : text.split("\n")) {
         str = str.trim();
 
         if (str.length() != 0)
         {
           resources.add(this.resourceLoader.getResource(str));
         }
       }
     }
     setLocations((Resource[])resources.toArray(new Resource[0]));
   }
 
   public Map<String, Object> getMap() {
     Map map = new HashMap();
 
     for (String key : this.properties.stringPropertyNames()) {
       map.put(key, this.properties.get(key));
     }
 
     return map;
   }
 }

