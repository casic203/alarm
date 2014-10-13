 package com.casic.core.util;
 
 import java.lang.reflect.InvocationTargetException;
 import java.util.ArrayList;
 import java.util.Collection;
 import java.util.Date;
 import java.util.Iterator;
 import java.util.List;
 import org.apache.commons.beanutils.PropertyUtils;
 import org.apache.commons.beanutils.converters.DateConverter;
 
 public class ConvertUtils
 {
   public static List convertElementPropertyToList(Collection collection, String propertyName)
   {
     List list = new ArrayList();
     try
     {
       for (Iterator i = collection.iterator(); i.hasNext(); ) 
       { Object obj = i.next();
         list.add(PropertyUtils.getProperty(obj, propertyName));
       }
     }
     catch (NoSuchMethodException e)
     {
    	 
       throw ReflectUtils.convertReflectionExceptionToUnchecked(e);
     } catch (IllegalAccessException e) {
       throw ReflectUtils.convertReflectionExceptionToUnchecked(e);
     } catch (InvocationTargetException e) {
       throw ReflectUtils.convertReflectionExceptionToUnchecked(e);
     }
 
     return list;
   }
 
   public static String convertElementPropertyToString(Collection collection, String propertyName, String separator)
   {
     List list = convertElementPropertyToList(collection, propertyName);
 
     return StringUtils.join(list, separator);
   }
 
   public static Object convertStringToObject(String value, Class<?> toType)
   {
     return org.apache.commons.beanutils.ConvertUtils.convert(value, toType);
   }
 
   private static void registerDateConverter()
   {
     DateConverter dc = new DateConverter();
     dc.setUseLocaleFormat(true);
     dc.setPatterns(new String[] { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss" });
     org.apache.commons.beanutils.ConvertUtils.register(dc, Date.class);
   }
 
   static
   {
     registerDateConverter();
   }
 }

