 package com.casic.core.util;
 
 import java.lang.reflect.Field;
 import java.lang.reflect.InvocationTargetException;
 import java.lang.reflect.Method;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 
 public class ReflectUtils
 {
   private static Logger logger = LoggerFactory.getLogger(ReflectUtils.class);
 
   public static String getGetterMethodName(Object target, String fieldName)
     throws Exception
   {
     String methodName = "get" + StringUtils.capitalize(fieldName);
     try
     {
       target.getClass().getDeclaredMethod(methodName, new Class[0]);
     } catch (NoSuchMethodException ex) {
       methodName = "is" + StringUtils.capitalize(fieldName);
 
       target.getClass().getDeclaredMethod(methodName, new Class[0]);
     }
 
     return methodName;
   }
 
   public static Object getMethodValue(Object target, String methodName)
     throws Exception
   {
     Method method = target.getClass().getDeclaredMethod(methodName, new Class[0]);
 
     return method.invoke(target, new Object[0]);
   }
 
   public static void setMethodValue(Object target, String methodName, Object methodValue)
     throws Exception
   {
     if (methodValue != null)
       try {
         Method method = target.getClass().getDeclaredMethod(methodName, new Class[] { methodValue.getClass() });
 
         method.invoke(target, new Object[] { methodValue });
       } catch (NoSuchMethodException ex) {
         logger.debug(ex.getMessage(), ex);
       } catch (IllegalAccessException ex) {
         logger.debug(ex.getMessage(), ex);
       } catch (InvocationTargetException ex) {
         logger.debug(ex.getMessage(), ex);
       }
   }
 
   public static Object getFieldValue(Object target, String fieldName)
     throws Exception
   {
     Field field = target.getClass().getDeclaredField(fieldName);
     field.setAccessible(true);
 
     return field.get(target);
   }
 
   public static RuntimeException convertReflectionExceptionToUnchecked(Exception e)
   {
     if (((e instanceof IllegalAccessException)) || ((e instanceof IllegalArgumentException)) || ((e instanceof NoSuchMethodException)))
     {
       return new IllegalArgumentException("Reflection Exception.", e);
     }if ((e instanceof InvocationTargetException)) {
       return new RuntimeException("Reflection Exception.", ((InvocationTargetException)e).getTargetException());
     }
     if ((e instanceof RuntimeException)) {
       return (RuntimeException)e;
     }
 
     return new RuntimeException("Unexpected Checked Exception.", e);
   }
 }

