 package com.casic.core.mapper;
 
 import org.dozer.DozerBeanMapperSingletonWrapper;
 import org.dozer.Mapper;
 
 public class BeanMapper
 {
   private static Mapper mapper = DozerBeanMapperSingletonWrapper.getInstance();
 
   public void copy(Object src, Object dest)
   {
     mapper.map(src, dest);
   }
 }

