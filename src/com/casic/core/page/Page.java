 package com.casic.core.page;
 
 import java.util.ArrayList;
 import java.util.Collection;
 import java.util.Locale;
 
 public class Page
 {
   public static final String ASC = "ASC";
   public static final String DESC = "DESC";
   public static final int DEFAULT_PAGE_SIZE = 10;
   private int pageNo = 1;
 
   private int pageSize = 10;
   private String orderBy;
   private String order = "ASC";
   private Object result;
   private int totalCount = -1;
   private boolean autoCount;
   private int start;
   private int pageCount = -1;
 
   public Page()
   {
     this.totalCount = 0;
     this.result = new ArrayList();
   }
 
   public Page(Object result, int totalCount)
   {
     this.result = result;
     this.totalCount = totalCount;
   }
 
   public Page(int pageNo, int pageSize, String orderBy, String order)
   {
     this.pageNo = pageNo;
     this.pageSize = pageSize;
     this.orderBy = orderBy;
     checkAndSetOrder(order);
     calculateStart();
   }
 
   public boolean isAsc()
   {
     return !"DESC".equalsIgnoreCase(this.order);
   }
 
   public String getInverseOrder()
   {
     if ("DESC".equalsIgnoreCase(this.order)) {
       return "ASC";
     }
     return "DESC";
   }
 
   public boolean isPageSizeEnabled()
   {
     return this.pageSize > 0;
   }
 
   public boolean isStartEnabled()
   {
     return this.start >= 0;
   }
 
   public boolean isOrderEnabled()
   {
     return (this.orderBy != null) && (this.orderBy.trim().length() != 0);
   }
 
   public boolean isPreviousEnabled()
   {
     return this.pageNo > 1;
   }
 
   public boolean isNextEnabled()
   {
     return this.pageNo < this.pageCount;
   }
 
   public boolean isPageCountEnabled()
   {
     return this.pageCount >= 0;
   }
 
   private void calculateStart()
   {
     if ((this.pageNo < 1) || (this.pageSize < 1))
       this.start = -1;
     else
       this.start = ((this.pageNo - 1) * this.pageSize);
   }
 
   private void calculatePageCount()
   {
     if ((this.totalCount < 0) || (this.pageSize < 1))
       this.pageCount = -1;
     else
       this.pageCount = ((this.totalCount - 1) / this.pageSize + 1);
   }
 
   public int getPageNo()
   {
     return this.pageNo;
   }
 
   public void setPageNo(int pageNo)
   {
     this.pageNo = pageNo;
     calculateStart();
   }
 
   public int getPageSize()
   {
     return this.pageSize;
   }
 
   public void setPageSize(int pageSize)
   {
     this.pageSize = pageSize;
     calculateStart();
     calculatePageCount();
   }
 
   public String getOrderBy()
   {
     return this.orderBy;
   }
 
   public void setOrderBy(String orderBy)
   {
     this.orderBy = orderBy;
   }
 
   public String getOrder()
   {
     return this.order;
   }
 
   public void setOrder(String order)
   {
     checkAndSetOrder(order);
   }
 
   public Object getResult()
   {
     return this.result;
   }
 
   public void setResult(Object result)
   {
     this.result = result;
   }
 
   public int getTotalCount()
   {
     return this.totalCount;
   }
 
   public void setTotalCount(int totalCount)
   {
     this.totalCount = totalCount;
     calculatePageCount();
   }
 
   public boolean isAutoCount()
   {
     return this.autoCount;
   }
 
   public void setAutoCount(boolean autoCount)
   {
     this.autoCount = autoCount;
   }
 
   public int getStart()
   {
     return this.start;
   }
 
   public int getPageCount()
   {
     return this.pageCount;
   }
 
   public int getResultSize() {
     if ((this.result instanceof Collection)) {
       return ((Collection)this.result).size();
     }
     return 0;
   }
 
   private void checkAndSetOrder(String text)
   {
     if (("ASC".equalsIgnoreCase(text)) || ("DESC".equalsIgnoreCase(text)))
       this.order = text.toUpperCase(Locale.CHINA);
     else
       throw new IllegalArgumentException("order should be 'DESC' or 'ASC'");
   }
 }

