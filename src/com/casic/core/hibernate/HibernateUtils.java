 package com.casic.core.hibernate;
 
 import com.casic.core.util.BeanUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.CriteriaImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
 
 public class HibernateUtils
 {
   private static Logger logger = LoggerFactory.getLogger(HibernateUtils.class);
 
   public static Integer getNumber(Object result)
   {
     if (result == null) {
       return Integer.valueOf(0);
     }
     return Integer.valueOf(((Number)result).intValue());
   }
 
   public static String removeSelect(String hql)
   {
     Assert.hasText(hql);
 
     if (hql.toLowerCase(Locale.ENGLISH).indexOf("distinct") != -1) {
       logger.warn("there is a distinct in paged query hql : [{}], this maybe cause an unexpected result", hql);
     }
 
     int beginPos = hql.toLowerCase(Locale.CHINA).indexOf("from");
     Assert.isTrue(beginPos != -1, " hql : " + hql + " must has a keyword 'from'");
 
     return hql.substring(beginPos);
   }
 
   public static String removeOrders(String hql)
   {
     Assert.hasText(hql);
 
     Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", 2);
 
     Matcher m = p.matcher(hql);
     StringBuffer sb = new StringBuffer();
 
     while (m.find()) {
       m.appendReplacement(sb, "");
     }
 
     m.appendTail(sb);
 
     return sb.toString();
   }
 
   public static Query distinct(Query query)
   {
     query.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
 
     return query;
   }
 
   public static Criteria distinct(Criteria criteria)
   {
     criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
 
     return criteria;
   }
 
   public static Projection findProjection(Criteria criteria)
   {
     if ((criteria instanceof CriteriaImpl)) {
       return ((CriteriaImpl)criteria).getProjection();
     }
     throw new IllegalArgumentException(criteria + " is not a CriteriaImpl");
   }
 
   public static List findOrderEntries(Criteria criteria)
   {
     try
     {
       return (List)BeanUtils.forceGetProperty(criteria, "orderEntries");
     } catch (Exception e) {
       throw new RuntimeException(" Runtime Exception impossibility throw ", e);
     }
   }
 
   public static void setOrderEntries(Criteria criteria, List orderEntries)
   {
     try
     {
       BeanUtils.forceSetProperty(criteria, "orderEntries", orderEntries);
     } catch (Exception e) {
       throw new RuntimeException(" Runtime Exception impossibility throw ", e);
     }
   }
 
   public static Criterion buildCriterion(String propertyName, Object propertyValue, MatchType matchType)
   {
     Assert.hasText(propertyName, "propertyName����Ϊ��");
 
     Criterion criterion = null;
 
     switch (matchType.ordinal()) {
     case 1:
       criterion = Restrictions.eq(propertyName, propertyValue);
 
       break;
     case 2:
       criterion = Restrictions.like(propertyName, (String)propertyValue, MatchMode.ANYWHERE);
 
       break;
     case 3:
       criterion = Restrictions.le(propertyName, propertyValue);
 
       break;
     case 4:
       criterion = Restrictions.lt(propertyName, propertyValue);
 
       break;
     case 5:
       criterion = Restrictions.ge(propertyName, propertyValue);
 
       break;
     case 6:
       criterion = Restrictions.gt(propertyName, propertyValue);
 
       break;
     case 7:
       criterion = Restrictions.in(propertyName, (Collection)propertyValue);
 
       break;
     default:
       criterion = Restrictions.eq(propertyName, propertyValue);
     }
 
     return criterion;
   }
 
   public static Criterion[] buildCriterion(List<PropertyFilter> filters)
   {
     List criterionList = new ArrayList();
 
     for (PropertyFilter filter : filters)
     {
       if (!filter.hasMultiProperties()) {
         Criterion criterion = buildCriterion(filter.getPropertyName(), filter.getMatchValue(), filter.getMatchType());
 
         criterionList.add(criterion);
       }
       else {
         Disjunction disjunction = Restrictions.disjunction();
 
         for (String param : filter.getPropertyNames()) {
           Criterion criterion = buildCriterion(param, filter.getMatchValue(), filter.getMatchType());
 
           disjunction.add(criterion);
         }
 
         criterionList.add(disjunction);
       }
     }
 
     return (Criterion[])criterionList.toArray(new Criterion[criterionList.size()]);
   }
 
   public static void buildQuery(StringBuffer buff, PropertyFilter propertyFilter)
   {
     if (buff.indexOf("where") == -1)
       buff.append(" where ");
     else {
       buff.append(" and ");
     }
 
     buff.append(propertyFilter.getPropertyName());
 
     switch (propertyFilter.getMatchType().ordinal()) {
     case 1:
       buff.append(" =:");
 
       break;
     case 2:
       buff.append(" like:");
 
       break;
     case 3:
       buff.append(" <=:");
 
       break;
     case 4:
       buff.append(" <:");
 
       break;
     case 5:
       buff.append(" >=:");
 
       break;
     case 6:
       buff.append(" >:");
 
       break;
     case 7:
       buff.append(" in :");
 
       break;
     default:
       buff.append(" =:");
     }
 
     buff.append(propertyFilter.getPropertyName().replaceAll("\\.", "_"));
   }
 }

