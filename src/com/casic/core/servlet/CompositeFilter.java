 package com.casic.core.servlet;
 
 import java.io.IOException;
 import java.util.ArrayList;
 import java.util.List;
 import javax.servlet.Filter;
 import javax.servlet.FilterChain;
 import javax.servlet.FilterConfig;
 import javax.servlet.ServletException;
 import javax.servlet.ServletRequest;
 import javax.servlet.ServletResponse;
 
 public class CompositeFilter
   implements Filter
 {
   private List<? extends Filter> filters;
 
   public CompositeFilter()
   {
     this.filters = new ArrayList();
   }
   public void setFilters(List<? extends Filter> filters) {
     this.filters = new ArrayList(filters);
   }
 
   public void destroy() {
     for (int i = this.filters.size(); i-- > 0; ) {
       Filter filter = (Filter)this.filters.get(i);
       filter.destroy();
     }
   }
 
   public void init(FilterConfig config) throws ServletException {
     for (Filter filter : this.filters)
       filter.init(config);
   }
 
   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
     throws IOException, ServletException
   {
     new VirtualFilterChain(chain, this.filters).doFilter(request, response);
   }
 
   private static final class VirtualFilterChain
     implements FilterChain
   {
     private final FilterChain originalChain;
     private final List<? extends Filter> additionalFilters;
     private int currentPosition = 0;
 
     private VirtualFilterChain(FilterChain chain, List<? extends Filter> additionalFilters)
     {
       this.originalChain = chain;
       this.additionalFilters = additionalFilters;
     }
 
     public void doFilter(ServletRequest request, ServletResponse response)
       throws IOException, ServletException
     {
       if (this.currentPosition == this.additionalFilters.size()) {
         this.originalChain.doFilter(request, response);
       } else {
         this.currentPosition += 1;
 
         Filter nextFilter = (Filter)this.additionalFilters.get(this.currentPosition - 1);
         nextFilter.doFilter(request, response, this);
       }
     }
   }
 }

