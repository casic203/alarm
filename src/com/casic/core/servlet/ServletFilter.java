 package com.casic.core.servlet;
 
 import java.io.IOException;
 import java.util.Collections;
 import java.util.HashMap;
 import java.util.Map;
 import java.util.Map.Entry;
 import javax.servlet.FilterChain;
 import javax.servlet.FilterConfig;
 import javax.servlet.Servlet;
 import javax.servlet.ServletException;
 import javax.servlet.ServletRequest;
 import javax.servlet.ServletResponse;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 
 public class ServletFilter extends ProxyFilter
 {
   private Map<UrlPatternMatcher, Servlet> servletMap = Collections.EMPTY_MAP;
 
   public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException
   {
     HttpServletRequest req = (HttpServletRequest)request;
     HttpServletResponse res = (HttpServletResponse)response;
 
     String contextPath = req.getContextPath();
     String requestUri = req.getRequestURI();
     String path = requestUri.substring(contextPath.length());
 
     for (Map.Entry entry : this.servletMap.entrySet())
     {
       UrlPatternMatcher urlPatternMatcher = (UrlPatternMatcher)entry.getKey();
 
       if (urlPatternMatcher.shouldRedirect(path)) {
         res.sendRedirect(contextPath + path + "/");
 
         return;
       }
 
       if (urlPatternMatcher.matches(path)) {
         PathHttpServletRequestWrapper requestWrapper = new PathHttpServletRequestWrapper(req, urlPatternMatcher.getUrlPattern());
 
         Servlet servlet = (Servlet)entry.getValue();
         servlet.service(requestWrapper, response);
 
         return;
       }
     }
 
     filterChain.doFilter(request, response);
   }
 
   public void init(FilterConfig filterConfig) throws ServletException {
     for (Map.Entry entry : this.servletMap.entrySet())
     {
       Servlet servlet = (Servlet)entry.getValue();
       servlet.init(new ProxyServletConfig(filterConfig.getServletContext()));
     }
   }
 
   public void destroy()
   {
     for (Map.Entry entry : this.servletMap.entrySet())
     {
       Servlet servlet = (Servlet)entry.getValue();
       servlet.destroy();
     }
   }
 
   public void setServletMap(Map<String, Servlet> urlPatternMap) {
     this.servletMap = new HashMap();
 
     for (Map.Entry entry : urlPatternMap.entrySet()) {
       UrlPatternMatcher urlPatternMatcher = UrlPatternMatcher.create((String)entry.getKey());
 
       this.servletMap.put(urlPatternMatcher, (Servlet) entry.getValue());
     }
   }
 }

