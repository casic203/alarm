 package com.casic.core.util;
 
 import java.lang.reflect.ParameterizedType;
 import java.lang.reflect.Type;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 
 public class GenericsUtils
 {
   private static final Logger LOGGER = LoggerFactory.getLogger(GenericsUtils.class);
 
   public static Class getSuperClassGenricType(Class clazz)
   {
     return getSuperClassGenricType(clazz, 0);
   }
 
   public static Class getSuperClassGenricType(Class clazz, int index)
   {
     Type genType = clazz.getGenericSuperclass();
 
     if ((clazz.getSimpleName().indexOf("$$EnhancerByCGLIB$$") != -1) && ((genType instanceof Class)))
     {
       genType = ((Class)genType).getGenericSuperclass();
     }
 
     if (!(genType instanceof ParameterizedType)) {
       LOGGER.warn("{}'s superclass not ParameterizedType", clazz.getSimpleName());
 
       return Object.class;
     }
 
     Type[] params = ((ParameterizedType)genType).getActualTypeArguments();
 
     if ((index >= params.length) || (index < 0)) {
       LOGGER.warn("Index: {}, Size of {}'s Parameterized Type: {}", new Object[] { Integer.valueOf(index), clazz.getSimpleName(), Integer.valueOf(params.length) });
 
       return Object.class;
     }
 
     if (!(params[index] instanceof Class)) {
       LOGGER.warn("{} not set the actual class on superclass generic parameter", clazz.getSimpleName());
 
       return Object.class;
     }
 
     return (Class)params[index];
   }
 }

