 package com.casic.core.servlet;
 
 import java.util.Collections;
 import java.util.Enumeration;
 import java.util.Map;
 import javax.servlet.ServletConfig;
 import javax.servlet.ServletContext;
 
 public class ProxyServletConfig
   implements ServletConfig
 {
   private String servletName;
   private ServletContext servletContext;
   private Map<String, String> map = Collections.EMPTY_MAP;
 
   public ProxyServletConfig(ServletContext servletContext) {
     this.servletContext = servletContext;
   }
 
   public String getServletName() {
     return this.servletName;
   }
 
   public ServletContext getServletContext() {
     return this.servletContext;
   }
 
   public String getInitParameter(String name) {
     return (String)this.map.get(name);
   }
 
   public Enumeration getInitParameterNames() {
     return Collections.enumeration(this.map.keySet());
   }
 
   public void setServletName(String servletName)
   {
     this.servletName = servletName;
   }
 
   public void setMap(Map<String, String> map) {
     this.map = map;
   }
 }

