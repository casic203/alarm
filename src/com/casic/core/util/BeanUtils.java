 package com.casic.core.util;
 
 import java.lang.reflect.Field;
 import java.lang.reflect.Method;
 import java.security.AccessController;
 import java.security.PrivilegedAction;
 import java.util.ArrayList;
 import java.util.List;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.util.Assert;
 import org.springframework.util.ReflectionUtils;
 
 public class BeanUtils
 {
   private static Logger logger = LoggerFactory.getLogger(BeanUtils.class);
 
   public static Field getDeclaredField(Object object, String propertyName)
     throws NoSuchFieldException
   {
     Assert.notNull(object);
     Assert.hasText(propertyName);
 
     return getDeclaredField(object.getClass(), propertyName);
   }
 
   public static Field getDeclaredField(Class clazz, String propertyName)
     throws NoSuchFieldException
   {
     Assert.notNull(clazz);
     Assert.hasText(propertyName);
 
     for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
       try
       {
         return superClass.getDeclaredField(propertyName);
       }
       catch (NoSuchFieldException ex) {
         logger.debug(ex.getMessage(), ex);
       }
     }
 
     throw new NoSuchFieldException("No such field: " + clazz.getName() + '.' + propertyName);
   }
 
   public static Object forceGetProperty(final Object object, String propertyName)
     throws NoSuchFieldException
   {
     Assert.notNull(object);
     Assert.hasText(propertyName);
 
     final Field field = getDeclaredField(object, propertyName);
 
     return AccessController.doPrivileged(new PrivilegedAction()
     {
       public Object run() {

    	 boolean accessible = field.isAccessible();
         field.setAccessible(true);

 
         Object result = null;
         try
         {
           result = field.get(object);
         } catch (IllegalAccessException e) {
           BeanUtils.logger.info("error wont happen");
         }
 
         field.setAccessible(accessible);
 
         return result;
       }
     });
   }
 
   public static void forceSetProperty(final Object object, String propertyName, final Object newValue)
     throws NoSuchFieldException
   {
     Assert.notNull(object);
     Assert.hasText(propertyName);
 
     final Field field = getDeclaredField(object, propertyName);
 
     AccessController.doPrivileged(new PrivilegedAction()
     {
       public Object run() {
         boolean accessible = field.isAccessible();
         field.setAccessible(true);
         try
         {
        	 field.set(object, newValue);
         } catch (IllegalAccessException e) {
           BeanUtils.logger.info("Error won't happen");
         }
 
         field.setAccessible(accessible);
 
         return null;
       }
     });
   }
 
   public static Object invokePrivateMethod(final Object object, String methodName, final Object[] params)
     throws NoSuchMethodException
   {
     Assert.notNull(object);
     Assert.hasText(methodName);
 
     Class[] types = new Class[params.length];
 
     for (int i = 0; i < params.length; i++) {
       types[i] = params[i].getClass();
     }
 
     Class clazz = object.getClass();
     Method method = null;
 
     for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
       try
       {
         method = superClass.getDeclaredMethod(methodName, types);
       }
       catch (NoSuchMethodException ex)
       {
         logger.debug(ex.getMessage(), ex);
       }
     }
 
     if (method == null) {
       throw new NoSuchMethodException("No Such Method:" + clazz.getSimpleName() + methodName);
     }
 
     final Method m = method;
 
     return AccessController.doPrivileged(new PrivilegedAction()
     {
       public Object run() {
         boolean accessible = m.isAccessible();
         m.setAccessible(true);
 
         Object result = null;
         try
         {
           result = m.invoke(object, params);
         } catch (Exception e) {
           ReflectionUtils.handleReflectionException(e);
         }
 
         m.setAccessible(accessible);
 
         return result;
       }
     });
   }
 
   public static List<Field> getFieldsByType(Object object, Class type)
   {
     List list = new ArrayList();
     Field[] fields = object.getClass().getDeclaredFields();
 
     for (Field field : fields) {
       if (field.getType().isAssignableFrom(type)) {
         list.add(field);
       }
     }
 
     return list;
   }
 
   public static Class getPropertyType(Class type, String name)
     throws NoSuchFieldException
   {
     return getDeclaredField(type, name).getType();
   }
 
   public static String getGetterName(Class type, String fieldName)
     throws NoSuchFieldException
   {
     Assert.notNull(type, "Type required");
     Assert.hasText(fieldName, "FieldName required");
 
     Class fieldType = getDeclaredField(type, fieldName).getType();
 
     if ((fieldType == Boolean.TYPE) || (fieldType == Boolean.class)) {
       return "is" + StringUtils.capitalize(fieldName);
     }
     return "get" + StringUtils.capitalize(fieldName);
   }
 
   public static Method getGetterMethod(Class type, String fieldName)
   {
     try
     {
       return type.getMethod(getGetterName(type, fieldName), new Class[0]);
     } catch (NoSuchMethodException ex) {
       logger.error(ex.getMessage(), ex);
     } catch (NoSuchFieldException ex) {
       logger.error(ex.getMessage(), ex);
     }
 
     return null;
   }
 }

