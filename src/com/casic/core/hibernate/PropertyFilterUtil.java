 package com.casic.core.hibernate;
 
 import java.util.Collection;
 import java.util.List;
 
 public class PropertyFilterUtil
 {
   public static void buildConfigurations(Collection<PropertyFilter> propertyFilters, StringBuilder buff, List<Object> params)
   {
     buildConfigurations(propertyFilters, buff, params, true);
   }
 
   public static void buildConfigurations(Collection<PropertyFilter> propertyFilters, StringBuilder buff, List<Object> params, boolean checkWhere)
   {
     for (PropertyFilter propertyFilter : propertyFilters)
       buildConfiguration(propertyFilter, buff, params, checkWhere);
   }
 
   public static void buildConfiguration(PropertyFilter propertyFilter, StringBuilder buff, List<Object> params)
   {
     buildConfiguration(propertyFilter, buff, params, true);
   }
 
   public static void buildConfiguration(PropertyFilter propertyFilter, StringBuilder buff, List<Object> params, boolean checkWhere)
   {
     if ((checkWhere) && (buff.indexOf("where") == -1))
       buff.append(" where ");
     else {
       buff.append(" and ");
     }
 
     String propertyName = propertyFilter.getPropertyName();
     Object propertyValue = propertyFilter.getMatchValue();
 
     switch (propertyFilter.getMatchType().ordinal()) {
     case 1:
       buff.append(propertyName).append("=?");
       params.add(propertyValue);
 
       break;
     case 2:
       buff.append(propertyName).append(" like ?");
       params.add("%" + propertyValue + "%");
 
       break;
     case 3:
       buff.append(propertyName).append("<=?");
       params.add(propertyValue);
 
       break;
     case 4:
       buff.append(propertyName).append("<?");
       params.add(propertyValue);
 
       break;
     case 5:
       buff.append(propertyName).append(">=?");
       params.add(propertyValue);
 
       break;
     case 6:
       buff.append(propertyName).append(">?");
       params.add(propertyValue);
 
       break;
     case 7:
       buff.append(propertyName).append("in (?)");
       params.add(propertyValue);
 
       break;
     default:
       buff.append(propertyName).append("=?");
       params.add(propertyValue);
     }
   }
 }

