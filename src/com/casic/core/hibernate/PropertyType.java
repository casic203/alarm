 package com.casic.core.hibernate;
 
 import java.util.Date;
 
 public enum PropertyType
 {
   S(String.class), 
 
   I(Integer.class), 
 
   L(Long.class), 
 
   N(Double.class), 
 
   D(Date.class), 
 
   B(Boolean.class);
 
   private Class<?> clazz;
 
   private PropertyType(Class<?> clazz)
   {
     this.clazz = clazz;
   }
 
   public Class<?> getValue()
   {
     return this.clazz;
   }
 }

