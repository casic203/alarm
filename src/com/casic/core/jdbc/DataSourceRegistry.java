 package com.casic.core.jdbc;
 
 import java.util.HashMap;
 import java.util.Map;
 import javax.sql.DataSource;
 
 public class DataSourceRegistry
 {
   private String defaultPrefix;
   private Map<String, DataSource> map = new HashMap();
 
   public DataSource getDataSource() {
     return getDataSource(this.defaultPrefix);
   }
 
   public DataSource getDataSource(String key) {
     assert (key != null) : "key is null";
 
     DataSource dataSource = (DataSource)this.map.get(key);
     assert (dataSource != null) : ("DataSource for [" + key + "] is null");
 
     return dataSource;
   }
 
   public void register(String key, DataSource dataSource) {
     this.map.put(key, dataSource);
   }
 
   public void unregister(String key) {
     this.map.remove(key);
   }
 
   public void clear() {
     this.map.clear();
   }
 
   public void setDefaultPrefix(String defaultPrefix)
   {
     this.defaultPrefix = defaultPrefix;
   }
 }

