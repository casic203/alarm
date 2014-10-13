 package com.casic.core.perf4j;
 
 import java.io.IOException;
 import javax.servlet.Filter;
 import javax.servlet.FilterChain;
 import javax.servlet.FilterConfig;
 import javax.servlet.ServletException;
 import javax.servlet.ServletRequest;
 import javax.servlet.ServletResponse;
 import javax.servlet.http.HttpServletRequest;
 import org.perf4j.StopWatch;
 import org.perf4j.slf4j.Slf4JStopWatch;
 
 public class Perf4jFilter
   implements Filter
 {
   public void init(FilterConfig filterConfig)
     throws ServletException
   {
   }
 
   public void destroy()
   {
   }
 
   public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
     throws ServletException, IOException
   {
     StopWatch stopWatch = new Slf4JStopWatch();
     try
     {
       filterChain.doFilter(request, response);
     } finally {
       stopWatch.stop(((HttpServletRequest)request).getRequestURI());
     }
   }
 }

