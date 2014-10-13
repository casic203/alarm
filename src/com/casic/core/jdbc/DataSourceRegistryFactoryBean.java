 package com.casic.core.jdbc;
 
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 import java.util.Properties;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.beans.factory.DisposableBean;
 import org.springframework.beans.factory.FactoryBean;
 import org.springframework.beans.factory.InitializingBean;
 
 public class DataSourceRegistryFactoryBean
   implements FactoryBean, InitializingBean, DisposableBean
 {
   private static Logger logger = LoggerFactory.getLogger(DataSourceRegistryFactoryBean.class);
   private Properties properties;
   private String defaultPrefix = "dbcp.";
   private List<String> prefixes = new ArrayList();
   private DataSourceRegistry dataSourceRegistry;
   private Map<String, DataSourceFactoryBean> dataSourceFactoryBeanMap = new HashMap();
   private boolean log4jdbcEnabled;
 
   public void afterPropertiesSet()
   {
     assert (this.defaultPrefix != null) : "defaultPrefix cannot be null";
     assert (this.defaultPrefix.length() != 0) : "defaultPrefix cannot be empty";
 
     if (this.prefixes == null) {
       this.prefixes = new ArrayList();
     }
 
     if (!this.prefixes.contains(this.defaultPrefix)) {
       this.prefixes.add(this.defaultPrefix);
     }
 
     logger.debug("defaultPrefix : {}", this.defaultPrefix);
     logger.debug("prefixes : {}", this.prefixes);
     this.dataSourceRegistry = new DataSourceRegistry();
     this.dataSourceRegistry.setDefaultPrefix(this.defaultPrefix);
     checkLog4jdbcEnabled();
 
     for (String prefix : this.prefixes) {
       DataSourceFactoryBean dataSourceFactoryBean = new DataSourceFactoryBean();
       dataSourceFactoryBean.setPrefix(prefix);
       dataSourceFactoryBean.setProperties(this.properties);
       dataSourceFactoryBean.setLog4jdbcEnabled(this.log4jdbcEnabled);
       dataSourceFactoryBean.afterPropertiesSet();
       this.dataSourceFactoryBeanMap.put(prefix, dataSourceFactoryBean);
       this.dataSourceRegistry.register(prefix, dataSourceFactoryBean.getObject());
     }
   }
 
   public Object getObject()
   {
     return this.dataSourceRegistry;
   }
 
   public void destroy() {
     for (DataSourceFactoryBean dataSourceFactoryBean : this.dataSourceFactoryBeanMap.values())
     {
       dataSourceFactoryBean.destroy();
     }
   }
 
   public Class getObjectType() {
     return DataSourceRegistry.class;
   }
 
   public boolean isSingleton() {
     return true;
   }
 
   public Map<String, DataSourceFactoryBean> getDataSourceFactoryBeanMap()
   {
     return this.dataSourceFactoryBeanMap;
   }
 
   public void enableLog4jdbc() {
     for (DataSourceFactoryBean dataSourceFactoryBean : this.dataSourceFactoryBeanMap.values())
     {
       dataSourceFactoryBean.enableLog4jdbc();
     }
 
     this.log4jdbcEnabled = true;
   }
 
   public void disableLog4jdbc() {
     for (DataSourceFactoryBean dataSourceFactoryBean : this.dataSourceFactoryBeanMap.values())
     {
       dataSourceFactoryBean.disableLog4jdbc();
     }
 
     this.log4jdbcEnabled = false;
   }
 
   public boolean isLog4jdbcEnabled() {
     return this.log4jdbcEnabled;
   }
 
   private boolean checkLog4jdbcEnabled() {
     this.log4jdbcEnabled = Boolean.parseBoolean(this.properties.getProperty("log4jdbc.enable"));
 
     return this.log4jdbcEnabled;
   }
 
   public void setProperties(Properties properties)
   {
     this.properties = properties;
   }
 
   public void setDefaultPrefix(String defaultPrefix) {
     this.defaultPrefix = defaultPrefix;
   }
 
   public void setPrefixes(List<String> prefixes) {
     this.prefixes = prefixes;
   }
 }

