 package com.casic.core.hibernate;
 
 import com.casic.core.util.ConvertUtils;
import com.casic.core.util.ServletUtils;
import com.casic.core.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import org.springframework.util.Assert;
 
 public class PropertyFilter
 {
   public static final String OR_SEPARATOR = "_OR_";
   private MatchType matchType;
   private Object matchValue;
   private Class<?> propertyClass;
   private String[] propertyNames;
 
   public PropertyFilter()
   {
   }
 
   public PropertyFilter(String filterName, String value)
   {
     String firstPart = StringUtils.substringBefore(filterName, "_");
     String matchTypeCode = StringUtils.substring(firstPart, 0, firstPart.length() - 1);
 
     String propertyTypeCode = StringUtils.substring(firstPart, firstPart.length() - 1, firstPart.length());
     try
     {
       this.matchType = ((MatchType)Enum.valueOf(MatchType.class, matchTypeCode));
     } catch (RuntimeException e) {
       throw new IllegalArgumentException("filter鍚嶇О" + filterName + "娌℃湁鎸夎鍒欑紪鍐�鏃犳硶寰楀埌灞炴�姣旇緝绫诲瀷.", e);
     }
 
     try
     {
       this.propertyClass = ((PropertyType)Enum.valueOf(PropertyType.class, propertyTypeCode)).getValue();
     }
     catch (RuntimeException e) {
       throw new IllegalArgumentException("filter鍚嶇О" + filterName + "娌℃湁鎸夎鍒欑紪鍐�鏃犳硶寰楀埌灞炴�鍊肩被鍨�", e);
     }
 
     String propertyNameStr = StringUtils.substringAfter(filterName, "_");
     Assert.isTrue(StringUtils.isNotBlank(propertyNameStr), "filter鍚嶇О" + filterName + "娌℃湁鎸夎鍒欑紪鍐�鏃犳硶寰楀埌灞炴�鍚嶇О.");
 
     this.propertyNames = StringUtils.splitByWholeSeparator(propertyNameStr, "_OR_");
 
     if (this.matchType == MatchType.IN)
       this.matchValue = convertStringToCollection(value, this.propertyClass);
     else
       this.matchValue = ConvertUtils.convertStringToObject(value, this.propertyClass);
   }
 
   private <T> Collection<T> convertStringToCollection(String text, Class<T> propertyClass)
   {
     List list = new ArrayList();
 
     for (String value : text.split(",")) {
       list.add(ConvertUtils.convertStringToObject(value, propertyClass));
     }
 
     return list;
   }
 
   public static List<PropertyFilter> buildFromHttpRequest(HttpServletRequest request)
   {
     return buildFromHttpRequest(request, "filter");
   }
 
   public static List<PropertyFilter> buildFromHttpRequest(HttpServletRequest request, String filterPrefix)
   {
     List filterList = new ArrayList();
 
     Map<String, Object> filterParamMap = (Map<String, Object>)ServletUtils.getParametersStartingWith(request, filterPrefix + "_");
 
     for (Map.Entry entry : filterParamMap.entrySet()) {
       String filterName = (String)entry.getKey();
       String value = (String)entry.getValue();
 
       if (StringUtils.isNotBlank(value)) {
         PropertyFilter filter = new PropertyFilter(filterName, value);
         filterList.add(filter);
       }
     }
 
     return filterList;
   }
 
   public Class<?> getPropertyClass()
   {
     return this.propertyClass;
   }
 
   public MatchType getMatchType()
   {
     return this.matchType;
   }
 
   public Object getMatchValue()
   {
     return this.matchValue;
   }
 
   public String[] getPropertyNames()
   {
     return this.propertyNames;
   }
 
   public String getPropertyName()
   {
     Assert.isTrue(this.propertyNames.length == 1, "There are not only one property in this filter.");
 
     return this.propertyNames[0];
   }
 
   public boolean hasMultiProperties()
   {
     return this.propertyNames.length > 1;
   }
 }

