 package com.casic.core.jdbc;
 
 import java.io.PrintWriter;
 import java.sql.Connection;
 import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;
 
 public class MockDataSource
   implements DataSource
 {
   public Connection getConnection()
     throws SQLException
   {
     return null;
   }
 
   public Connection getConnection(String username, String password)
     throws SQLException
   {
     return null;
   }
 
   public PrintWriter getLogWriter()
     throws SQLException
   {
     return null;
   }
 
   public void setLogWriter(PrintWriter out)
     throws SQLException
   {
   }
 
   public void setLoginTimeout(int seconds)
     throws SQLException
   {
   }
 
   public int getLoginTimeout()
     throws SQLException
   {
     return 0;
   }
 
   public boolean isWrapperFor(Class<?> iface) throws SQLException
   {
     return false;
   }
 
   public <T> T unwrap(Class<T> iface) throws SQLException
   {
     return null;
   }

public Logger getParentLogger() throws SQLFeatureNotSupportedException {
	// TODO Auto-generated method stub
	return null;
}
 }

