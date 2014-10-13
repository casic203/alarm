 package com.casic.core.servlet;
 
 import java.io.IOException;
 import java.util.Collections;
 import java.util.Map;
 import javax.servlet.Servlet;
 import javax.servlet.ServletConfig;
 import javax.servlet.ServletException;
 import javax.servlet.ServletRequest;
 import javax.servlet.ServletResponse;
 import javax.servlet.http.HttpServletResponse;
 
 public class ProxyServlet
   implements Servlet
 {
   private final String name;
   private final Servlet servlet;
   private final Map<String, String> map;
   private final boolean enable;
 
   public ProxyServlet(String name, Servlet servlet)
   {
     this(name, servlet, Collections.EMPTY_MAP, true);
   }
 
   public ProxyServlet(String name, Servlet servlet, Map<String, String> map) {
     this(name, servlet, map, true);
   }
 
   public ProxyServlet(String name, Servlet servlet, boolean enable) {
     this(name, servlet, Collections.EMPTY_MAP, enable);
   }
 
   public ProxyServlet(String name, Servlet servlet, Map<String, String> map, boolean enable)
   {
     this.name = name;
     this.servlet = servlet;
     this.map = map;
     this.enable = enable;
   }
 
   public void init(ServletConfig config) throws ServletException {
     if (this.enable) {
       ProxyServletConfig proxyServletConfig = new ProxyServletConfig(config.getServletContext());
 
       proxyServletConfig.setServletName(this.name);
       proxyServletConfig.setMap(this.map);
       this.servlet.init(proxyServletConfig);
     }
   }
 
   public ServletConfig getServletConfig() {
     return this.servlet.getServletConfig();
   }
 
   public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException
   {
     if (this.enable)
       this.servlet.service(req, res);
     else
       ((HttpServletResponse)res).sendError(404);
   }
 
   public String getServletInfo()
   {
     return this.name;
   }
 
   public void destroy() {
     if (this.enable)
       this.servlet.destroy();
   }
 
   public String getName()
   {
     return this.name;
   }
 }

