 package com.casic.core.hibernate;
 
 import com.casic.core.page.Page;
import com.casic.core.util.GenericsUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
 
 public class HibernateEntityDao<T> extends HibernatePagingDao
 {
   private static Logger logger = LoggerFactory.getLogger(HibernateEntityDao.class);
   private Class<T> entityClass;
 
   public HibernateEntityDao()
   {
     this.entityClass = GenericsUtils.getSuperClassGenricType(getClass());
   }
 
   public HibernateEntityDao(SessionFactory sessionFactory)
   {
     this();
     super.setSessionFactory(sessionFactory);
   }
 
   public HibernateEntityDao(SessionFactory sessionFactory, Class<T> entityClass)
   {
     super(sessionFactory);
     this.entityClass = entityClass;
   }
 
   public Class<T> getEntityClass()
   {
     return this.entityClass;
   }
 
   public void setEntityClass(Class<T> entityClass)
   {
     this.entityClass = entityClass;
   }
 
   @Transactional(readOnly=true)
   public T get(Serializable id)
   {
     return get(this.entityClass, id);
   }
 
   @Transactional(readOnly=true)
   public T load(Serializable id)
   {
     return load(this.entityClass, id);
   }
 
   @Transactional(readOnly=true)
   public List<T> getAll()
   {
     return getAll(this.entityClass);
   }
 
   @Transactional(readOnly=true)
   public List<T> getAll(String orderBy, boolean isAsc)
   {
     return getAll(this.entityClass, orderBy, isAsc);
   }
 
   @Transactional
   public void removeById(Serializable id)
   {
     remove(get(id));
   }
 
   @Transactional
   public void removeAll()
   {
     removeAll(getAll());
   }
 
   @Transactional
   public void save(Object entity)
   {
     try {
       Object o = entity;
       boolean isCreated = getId(this.entityClass, o) == null;
       super.save(entity);
 
       if (isCreated)
         publishEvent(new EntityCreatedEvent(o));
       else
         publishEvent(new EntityUpdatedEvent(o));
     }
     catch (NoSuchMethodException ex) {
       logger.warn(ex.getMessage(), ex);
       super.save(entity);
     } catch (IllegalAccessException ex) {
       logger.warn(ex.getMessage(), ex);
       super.save(entity);
     } catch (InvocationTargetException ex) {
       logger.warn(ex.getMessage(), ex);
       super.save(entity);
     }
   }
 
   @Transactional
   public void remove(Object entity)
   {
     Object o = entity;
     super.remove(entity);
     publishEvent(new EntityRemovedEvent(o));
   }
 
   public Criteria createCriteria(Criterion[] criterions)
   {
     return createCriteria(this.entityClass, criterions);
   }
 
   public Criteria createCriteria(String orderBy, boolean isAsc, Criterion[] criterions)
   {
     return createCriteria(this.entityClass, orderBy, isAsc, criterions);
   }
 
   @Transactional(readOnly=true)
   public List<T> findBy(String name, Object value)
   {
     return findBy(this.entityClass, name, value);
   }
 
   @Transactional(readOnly=true)
   public List<T> findByIds(List ids)
   {
     return findByIds(this.entityClass, ids);
   }
 
   @Transactional(readOnly=true)
   public List<T> findByLike(String name, Object value)
   {
     return findByLike(this.entityClass, name, value);
   }
 
   @Transactional(readOnly=true)
   public T findUniqueBy(String name, Object value)
   {
     return findUniqueBy(this.entityClass, name, value);
   }
 
   @Transactional(readOnly=true)
   public Integer getCount()
   {
     return getCount(this.entityClass);
   }
 
   @Transactional(readOnly=true)
   public Page pagedQuery(int pageNo, int pageSize, Criterion[] criterions)
   {
     return pagedQuery(this.entityClass, pageNo, pageSize, criterions);
   }
 
   @Transactional(readOnly=true)
   public Page pagedQuery(int pageNo, int pageSize, String orderBy, boolean isAsc, Criterion[] criterions)
   {
     logger.debug("start");
 
     return pagedQuery(this.entityClass, pageNo, pageSize, orderBy, isAsc, criterions);
   }
 
   public List<T> findBy(String propertyName, Object propertyValue, MatchType matchType)
   {
     return find(this.entityClass, new Criterion[] { HibernateUtils.buildCriterion(propertyName, propertyValue, matchType) });
   }
 
   public List<T> find(List<PropertyFilter> propertyFilters)
   {
     return find(this.entityClass, HibernateUtils.buildCriterion(propertyFilters));
   }
 
   public Page pagedQuery(int pageNo, int pageSize, List<PropertyFilter> propertyFilters)
   {
     return pagedQuery(this.entityClass, pageNo, pageSize, HibernateUtils.buildCriterion(propertyFilters));
   }
 
   public Page pagedQuery(Page page, List<PropertyFilter> propertyFilters)
   {
     return pagedQuery(this.entityClass, page, HibernateUtils.buildCriterion(propertyFilters));
   }
 
   public Page pagedQuery(String hql, Page page, List<PropertyFilter> propertyFilters)
   {
     return pagedQuery(hql, page.getPageNo(), page.getPageSize(), propertyFilters);
   }
 
   public Page pagedQuery(String hql, int pageNo, int pageSize, List<PropertyFilter> propertyFilters)
   {
     StringBuffer buff = new StringBuffer(hql);
     Map map = new HashMap();
 
     for (PropertyFilter propertyFilter : propertyFilters) {
       HibernateUtils.buildQuery(buff, propertyFilter);
 
       String key = propertyFilter.getPropertyName().replaceAll("\\.", "_");
 
       map.put(key, propertyFilter.getMatchValue());
     }
 
     return pagedQuery(buff.toString(), pageNo, pageSize, map);
   }
 }

