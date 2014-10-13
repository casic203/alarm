 package com.casic.core.jdbc;
 
 import java.io.PrintWriter;
 import java.sql.Connection;
 import java.sql.SQLException;
 import javax.sql.DataSource;
 
 public class DataSourceWrapper extends MockDataSource
 {
   protected DataSource currentDataSource;
 
   public Connection getConnection()
     throws SQLException
   {
     return this.currentDataSource.getConnection();
   }
 
   public Connection getConnection(String username, String password)
     throws SQLException
   {
     return this.currentDataSource.getConnection(username, password);
   }
 
   public PrintWriter getLogWriter()
     throws SQLException
   {
     return this.currentDataSource.getLogWriter();
   }
 
   public void setLogWriter(PrintWriter out)
     throws SQLException
   {
     this.currentDataSource.setLogWriter(out);
   }
 
   public void setLoginTimeout(int seconds)
     throws SQLException
   {
     this.currentDataSource.setLoginTimeout(seconds);
   }
 
   public int getLoginTimeout()
     throws SQLException
   {
     return this.currentDataSource.getLoginTimeout();
   }
 
   public boolean isWrapperFor(Class<?> iface) throws SQLException
   {
     return this.currentDataSource.isWrapperFor(iface);
   }
 
   public <T> T unwrap(Class<T> iface) throws SQLException
   {
     return this.currentDataSource.unwrap(iface);
   }
 
   public DataSource getCurrentDataSource()
   {
     return this.currentDataSource;
   }
 
   public void setCurrentDataSource(DataSource currentDataSource) {
     this.currentDataSource = currentDataSource;
   }
 }

