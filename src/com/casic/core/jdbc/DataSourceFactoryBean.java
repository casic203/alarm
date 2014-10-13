 package com.casic.core.jdbc;
 
 import java.lang.reflect.InvocationTargetException;
 import java.lang.reflect.Method;
 import java.sql.Connection;
 import java.util.Locale;
 import java.util.Map.Entry;
 import java.util.Properties;
 import javax.sql.DataSource;
 import net.sf.log4jdbc.Log4jdbcProxyDataSource;
 import org.apache.commons.dbcp.BasicDataSource;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.beans.factory.DisposableBean;
 import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
 
 public class DataSourceFactoryBean
   implements FactoryBean<DataSource>, DisposableBean, InitializingBean
 {
   private static Logger logger = LoggerFactory.getLogger(DataSourceFactoryBean.class);
   private BasicDataSource basicDataSource;
   private Log4jdbcProxyDataSource log4jdbcProxyDataSource;
   private DataSourceWrapper dataSourceWrapper = new DataSourceWrapper();
   private Properties properties;
   private String prefix = "dbcp.";
   private boolean log4jdbcEnabled;
   private Throwable throwable;
 
   public void afterPropertiesSet()
   {
     this.basicDataSource = new BasicDataSource();
 
     tryToSetProperties();
 
     checkProperties();
     checkConnection();
 
     this.log4jdbcProxyDataSource = new Log4jdbcProxyDataSource(this.basicDataSource);
 
     if (this.log4jdbcEnabled) {
       logger.debug("enable log4jdbc");
       this.dataSourceWrapper.setCurrentDataSource(this.log4jdbcProxyDataSource);
     }
     else {
       logger.debug("disable log4jdbc");
       this.dataSourceWrapper.setCurrentDataSource(this.basicDataSource);
     }
   }
 
   public DataSource getObject() {
     if ((this.basicDataSource == null) && (this.log4jdbcProxyDataSource == null)) {
       throw new RuntimeException("both log4jdbcProxyDataSource and basicDataSource are null, need initialize first.");
     }
 
     return this.dataSourceWrapper;
   }
 
   public void destroy() {
     if (this.basicDataSource != null) {
       try {
         this.basicDataSource.close();
         this.basicDataSource = null;
       } catch (Exception ex) {
         logger.warn("close dbcp error", ex);
       }
     }
 
     if (this.log4jdbcProxyDataSource != null)
       this.log4jdbcProxyDataSource = null;
   }
 
   public Class getObjectType()
   {
     return DataSource.class;
   }
 
   public boolean isSingleton() {
     return true;
   }
 
   public void setProperties(Properties properties)
   {
     this.properties = properties;
   }
 
   public void setPrefix(String prefix) {
     this.prefix = prefix;
   }
 
   public BasicDataSource getBasicDataSource() {
     return this.basicDataSource;
   }
 
   public Throwable getThrowable() {
     return this.throwable;
   }
 
   public void enableLog4jdbc()
   {
     if (!this.log4jdbcEnabled) {
       this.log4jdbcEnabled = true;
       this.dataSourceWrapper.setCurrentDataSource(this.log4jdbcProxyDataSource);
 
       logger.info("enable log4jdbc");
     } else {
       logger.info("log4jdbc already enabled");
     }
   }
 
   public void disableLog4jdbc() {
     if (this.log4jdbcEnabled) {
       this.log4jdbcEnabled = false;
       this.dataSourceWrapper.setCurrentDataSource(this.basicDataSource);
       logger.info("disable log4jdbc");
     } else {
       logger.info("log4jdbc already disabled");
     }
   }
 
   public boolean isLog4jdbcEnabled() {
     return this.log4jdbcEnabled;
   }
 
   public void setLog4jdbcEnabled(boolean log4jdbcEnabled) {
     this.log4jdbcEnabled = log4jdbcEnabled;
   }
 
   public void checkConnection()
   {
     Connection conn = null;
     try
     {
       conn = this.basicDataSource.getConnection();
     } catch (Exception ex) {
       this.throwable = ex;
       logger.warn("error open connection", ex);
     } finally {
       try {
         if (conn != null)
           conn.close();
       }
       catch (Exception ex) {
         logger.info("error on close connection", ex);
       }
     }
   }
 
   private void tryToSetProperties() {
     if (this.properties == null) {
       throw new IllegalArgumentException("there is no dbcp properties setting.");
     }
 
     logger.debug("prefix : {}", this.prefix);
 
     for (Entry<Object, Object> entry : this.properties.entrySet()) {
       String key = (String)entry.getKey();
       String value = (String)entry.getValue();
 
       if (key.startsWith(this.prefix))
       {
         String propertyName = key.substring(this.prefix.length());
         try
         {
           tryToSetProperty(propertyName, value);
         } catch (Exception ex) {
           logger.info("error to set property : key : " + key + ", value : " + value, ex);
         }
       }
     }
   }
 
   private void checkProperties()
   {
   }
 
   protected void tryToSetProperty(String propertyName, String propertyValue) throws Exception
   {
     String setterName = "set" + propertyName.substring(0, 1).toUpperCase(Locale.ENGLISH) + propertyName.substring(1);
 
     Method[] methods = BasicDataSource.class.getMethods();
 
     for (Method method : methods)
       if (method.getName().equals(setterName))
       {
         Class[] parameterTypes = method.getParameterTypes();
 
         if (parameterTypes.length == 1)
         {
           invokeMethod(method, parameterTypes[0], propertyValue);
         }
       }
   }
 
   private void invokeMethod(Method method, Class parameterType, String propertyValue) throws IllegalAccessException, InvocationTargetException
   {
     logger.debug("match method : {}, {}", method, propertyValue);
 
     if (parameterType == String.class)
       method.invoke(this.basicDataSource, new Object[] { propertyValue });
     else if ((parameterType == Integer.class) || (parameterType == Integer.TYPE))
     {
       method.invoke(this.basicDataSource, new Object[] { Integer.valueOf(Integer.parseInt(propertyValue)) });
     } else if ((parameterType == Long.class) || (parameterType == Long.TYPE))
     {
       method.invoke(this.basicDataSource, new Object[] { Long.valueOf(Long.parseLong(propertyValue)) });
     } else if ((parameterType == Boolean.class) || (parameterType == Boolean.TYPE))
     {
       method.invoke(this.basicDataSource, new Object[] { Boolean.valueOf(propertyValue) });
     }
     else logger.info("cannot process parameterType : [" + parameterType + "]");
   }
 }

