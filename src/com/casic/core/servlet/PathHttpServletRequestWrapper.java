 package com.casic.core.servlet;
 
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletRequestWrapper;
 
 public class PathHttpServletRequestWrapper extends HttpServletRequestWrapper
 {
   private String servletPath;
   private HttpServletRequest httpServletRequest;
 
   public PathHttpServletRequestWrapper(HttpServletRequest request, String path)
   {
     super(request);
     this.httpServletRequest = request;
 
     if (path.startsWith("/")) {
       if (path.endsWith("*"))
         this.servletPath = path.substring(0, path.length() - 1);
       else
         this.servletPath = path;
     }
     else {
       this.servletPath = "";
     }
 
     if (this.servletPath.endsWith("/"))
       this.servletPath = this.servletPath.substring(0, this.servletPath.length() - 1);
   }
 
   public String getServletPath()
   {
     return this.servletPath;
   }
 
   public String getPathInfo() {
     return this.httpServletRequest.getRequestURI().substring(this.httpServletRequest.getContextPath().length() + this.servletPath.length());
   }
 }

