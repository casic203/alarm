 package com.casic.core.export;
 
 import com.casic.core.util.ReflectUtils;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
 public class TableModel
 {
   private static Logger logger = LoggerFactory.getLogger(TableModel.class);
   private String name;
   private List<String> headers = new ArrayList();
   private List data;
 
   public String getName()
   {
     return this.name;
   }
 
   public void setName(String name) {
     this.name = name;
   }
 
   public void addHeaders(String[] header) {
     if (header == null) {
       return;
     }
 
     for (String text : header)
       this.headers.add(text);
   }
 
   public void setData(List data)
   {
     this.data = data;
   }
 
   public int getHeaderCount() {
     return this.headers.size();
   }
 
   public int getDataCount() {
     return this.data.size();
   }
 
   public String getHeader(int index) {
     return (String)this.headers.get(index);
   }
 
   public String getValue(int i, int j) {
     try {
       String header = getHeader(j);
       Object object = this.data.get(i);
       String methodName = ReflectUtils.getGetterMethodName(object, header);
 
       Object value = ReflectUtils.getMethodValue(object, methodName);
 
       return value == null ? "" : value.toString();
     } catch (Exception ex) {
       logger.info("error", ex);
     }
     return "";
   }
 }

