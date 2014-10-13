 package com.casic.core.hibernate;
 
 import java.io.Serializable;
 import java.lang.reflect.InvocationTargetException;
 import java.util.List;
 import java.util.Map;
 import org.apache.commons.beanutils.PropertyUtils;
 import org.hibernate.Criteria;
 import org.hibernate.Query;
 import org.hibernate.Session;
 import org.hibernate.SessionFactory;
 import org.hibernate.criterion.Criterion;
 import org.hibernate.criterion.Order;
 import org.hibernate.criterion.Projections;
 import org.hibernate.criterion.Restrictions;
 import org.springframework.transaction.annotation.Transactional;
 import org.springframework.util.Assert;
 import org.springframework.util.ReflectionUtils;
 import org.springframework.util.StringUtils;
 
 public class HibernateGenericDao extends HibernateBasicDao
 {
   public HibernateGenericDao()
   {
   }
 
   public HibernateGenericDao(SessionFactory sessionFactory)
   {
     super(sessionFactory);
   }
 
   public Query createQuery(String hql, Object[] values)
   {
     Assert.hasText(hql, "hql cannot be null");
 
     Query query = getSession().createQuery(hql);
 
     for (int i = 0; i < values.length; i++) {
       query.setParameter(i, values[i]);
     }
 
     return query;
   }
 
   public Query createQuery(String hql, Map<String, Object> map)
   {
     Assert.hasText(hql, "hql cannot be null");
 
     Query query = getSession().createQuery(hql);
 
     if (map != null) {
       query.setProperties(map);
     }
 
     return query;
   }
 
   public Criteria createCriteria(Class entityClass, Criterion[] criterions)
   {
     Criteria criteria = getSession().createCriteria(entityClass);
 
     for (Criterion c : criterions) {
       criteria.add(c);
     }
 
     return criteria;
   }
 
   public <T> Criteria createCriteria(Class<T> entityClass, String orderBy, boolean isAsc, Criterion[] criterions)
   {
     if (StringUtils.hasText(orderBy)) {
       Criteria criteria = createCriteria(entityClass, criterions);
 
       if (isAsc)
         criteria.addOrder(Order.asc(orderBy));
       else {
         criteria.addOrder(Order.desc(orderBy));
       }
 
       return criteria;
     }
     return createCriteria(entityClass, criterions);
   }
 
   @Transactional(readOnly=true)
   public <T> List<T> find(Class<T> entityClass, Criterion[] criterions)
   {
     return createCriteria(entityClass, criterions).list();
   }
 
   @Transactional(readOnly=true)
   public <T> T findUnique(Class<T> entityClass, Criterion[] criterions)
   {
     return (T) createCriteria(entityClass, criterions).uniqueResult();
   }
 
   @Transactional(readOnly=true)
   public List find(String hql, Object[] values)
   {
     return createQuery(hql, values).list();
   }
 
   @Transactional(readOnly=true)
   public List find(String hql, Map<String, Object> map)
   {
	   List list = null;
	   try {
		    list = createQuery(hql, map).list();
	   } catch (Exception e) {
		   e.printStackTrace();
	   }
       return list;
   }
 
   @Transactional(readOnly=true)
   public <T> T findUnique(String hql, Object[] values)
   {
     return (T) createQuery(hql, values).setMaxResults(1).uniqueResult();
   }
 
   @Transactional(readOnly=true)
   public <T> T findUnique(String hql, Map<String, Object> map)
   {
     return (T) createQuery(hql, map).setMaxResults(1).uniqueResult();
   }
 
   @Transactional(readOnly=true)
   public <T> List<T> findBy(Class<T> entityClass, String name, Object value)
   {
     Assert.hasText(name, "property name cannot be null");
 
     return createCriteria(entityClass, new Criterion[] { Restrictions.eq(name, value) }).list();
   }
 
   @Transactional(readOnly=true)
   public <T> List<T> findByLike(Class<T> entityClass, String name, Object value)
   {
     Assert.hasText(name, "property name cannot be null");
 
     return createCriteria(entityClass, new Criterion[] { Restrictions.like(name, value) }).list();
   }
 
   @Transactional(readOnly=true)
   public <T> List<T> findByIds(Class<T> entityClass, List ids)
   {
     Assert.notEmpty(ids);
 
     String idName = getIdName(entityClass);
     Criterion criterion = Restrictions.in(idName, ids);
 
     return find(entityClass, new Criterion[] { criterion });
   }
 
   @Transactional(readOnly=true)
   public <T> T findUniqueBy(Class<T> entityClass, String name, Object value)
   {
     return (T) createCriteria(entityClass, new Criterion[] { Restrictions.eq(name, value) }).setMaxResults(1).uniqueResult();
   }
 
   @Transactional(readOnly=true)
   public <T> boolean isUnique(Class<T> entityClass, T entity, String uniquePropertyNames)
   {
     Assert.hasText(uniquePropertyNames);
 
     Criteria criteria = createCriteria(entityClass, new Criterion[0]).setProjection(Projections.rowCount());
 
     String[] nameList = uniquePropertyNames.split(",");
     try
     {
       for (String name : nameList) {
         criteria.add(Restrictions.eq(name, PropertyUtils.getProperty(entity, name)));
       }
 
       String idName = getIdName(entityClass);
 
       Serializable id = getId(entityClass, entity);
 
       if (id != null)
         criteria.add(Restrictions.not(Restrictions.eq(idName, id)));
     }
     catch (IllegalAccessException e) {
       ReflectionUtils.handleReflectionException(e);
     } catch (NoSuchMethodException e) {
       ReflectionUtils.handleReflectionException(e);
     } catch (InvocationTargetException e) {
       ReflectionUtils.handleReflectionException(e);
     }
 
     Object result = criteria.uniqueResult();
 
     return HibernateUtils.getNumber(result).intValue() == 0;
   }
 
   @Transactional(readOnly=true)
   public <T> Integer getCount(Class<T> entityClass)
   {
     return getCount(createCriteria(entityClass, new Criterion[0]));
   }
 
   @Transactional(readOnly=true)
   public Integer getCount(Criteria criteria)
   {
     Object result = criteria.setProjection(Projections.rowCount()).uniqueResult();
 
     return HibernateUtils.getNumber(result);
   }
 
   @Transactional(readOnly=true)
   public Integer getCount(String hql, Object[] values)
   {
     Object result = createQuery(hql, values).uniqueResult();
 
     return HibernateUtils.getNumber(result);
   }
 
   @Transactional(readOnly=true)
   public Integer getCount(String hql, Map<String, Object> map)
   {
     Object result = createQuery(hql, map).uniqueResult();
 
     return HibernateUtils.getNumber(result);
   }
 
   @Transactional
   public int batchUpdate(String hql, Object[] values)
   {
     return createQuery(hql, values).executeUpdate();
   }
 
   @Transactional
   public int batchUpdate(String hql, Map<String, Object> map)
   {
     return createQuery(hql, map).executeUpdate();
   }
 }

