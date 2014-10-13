 package com.casic.core.hibernate;
 
 import com.casic.core.page.Page;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projection;
import org.hibernate.internal.CriteriaImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
 
 public class HibernatePagingDao extends HibernateGenericDao
 {
   public HibernatePagingDao()
   {
   }
 
   public HibernatePagingDao(SessionFactory sessionFactory)
   {
     super(sessionFactory);
   }
 
   @Transactional(readOnly=true)
   public Page pagedQuery(String hql, int pageNo, int pageSize, Object[] values)
   {
     Assert.hasText(hql);
     Assert.isTrue(pageNo >= 1, "pageNo should be eg 1");
 
     String countQueryString = "select count (*) " + HibernateUtils.removeSelect(HibernateUtils.removeOrders(hql));
 
     Integer totalCount = getCount(countQueryString, values);
 
     if (totalCount.intValue() < 1) {
       return new Page();
     }
 
     Query query = createQuery(hql, values);
     int start = (pageNo - 1) * pageSize;
     List result = query.setFirstResult(start).setMaxResults(pageSize).list();
 
     Page page = new Page(result, totalCount.intValue());
     page.setPageNo(pageNo);
     page.setPageSize(pageSize);
 
     return page;
   }
 
   @Transactional(readOnly=true)
   public Page pagedQuery(String hql, int pageNo, int pageSize, Map<String, Object> map)
   {
     Assert.hasText(hql);
     Assert.isTrue(pageNo >= 1, "pageNo should be eg 1");
 
     String countQueryString = "select count (*) " + HibernateUtils.removeSelect(HibernateUtils.removeOrders(hql));
 
     Integer totalCount = getCount(countQueryString, map);
 
     if (totalCount.intValue() < 1) {
       return new Page();
     }
 
     Query query = createQuery(hql, map);
     int start = (pageNo - 1) * pageSize;
     List result = query.setFirstResult(start).setMaxResults(pageSize).list();
 
     Page page = new Page(result, totalCount.intValue());
     page.setPageNo(pageNo);
     page.setPageSize(pageSize);
 
     return page;
   }
 
   @Transactional(readOnly=true)
   public Page pagedQuery(Criteria criteria, int pageNo, int pageSize)
   {
     Assert.notNull(criteria);
     Assert.isTrue(pageNo >= 1, "pageNo should be eg 1");
     Assert.isTrue(criteria instanceof CriteriaImpl);
 
     Projection projection = HibernateUtils.findProjection(criteria);
 
     List orderEntries = HibernateUtils.findOrderEntries(criteria);
     HibernateUtils.setOrderEntries(criteria, Collections.EMPTY_LIST);
 
     Integer totalCount = getCount(criteria);
 
     criteria.setProjection(projection);
 
     if (projection == null) {
       criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
     }
 
     HibernateUtils.setOrderEntries(criteria, orderEntries);
 
     if (totalCount.intValue() < 1) {
       return new Page();
     }
 
     int start = (pageNo - 1) * pageSize;
     List result = criteria.setFirstResult(start).setMaxResults(pageSize).list();
 
     Page page = new Page(result, totalCount.intValue());
     page.setPageNo(pageNo);
     page.setPageSize(pageSize);
 
     return page;
   }
 
   @Transactional(readOnly=true)
   public <T> Page pagedQuery(Class<T> entityClass, int pageNo, int pageSize, Criterion[] criterions)
   {
     Criteria criteria = createCriteria(entityClass, criterions);
 
     return pagedQuery(criteria, pageNo, pageSize);
   }
 
   @Transactional(readOnly=true)
   public <T> Page pagedQuery(Class<T> entityClass, int pageNo, int pageSize, String orderBy, boolean isAsc, Criterion[] criterions)
   {
     Criteria criteria = createCriteria(entityClass, orderBy, isAsc, criterions);
 
     Page page = pagedQuery(criteria, pageNo, pageSize);
     page.setOrderBy(orderBy);
     page.setOrder(isAsc ? "ASC" : "DESC");
 
     return page;
   }
 
   @Transactional(readOnly=true)
   public <T> Page pagedQuery(Class<T> entityClass, Page page)
   {
     String orderBy = page.getOrderBy();
     String order = page.getOrder();
 
     Criteria criteria = null;
 
     if (page.isOrderEnabled())
       criteria = createCriteria(entityClass, orderBy, "ASC".equals(order), new Criterion[0]);
     else {
       criteria = createCriteria(entityClass, new Criterion[0]);
     }
 
     Page resultPage = pagedQuery(criteria, page.getPageNo(), page.getPageSize());
 
     resultPage.setOrderBy(orderBy);
     resultPage.setOrder(order);
 
     return resultPage;
   }
 
   @Transactional(readOnly=true)
   public <T> Page pagedQuery(Class<T> entityClass, Page page, Criterion[] criterions)
   {
     String orderBy = page.getOrderBy();
     String order = page.getOrder();
 
     Criteria criteria = null;
 
     if (page.isOrderEnabled()) {
       criteria = createCriteria(entityClass, orderBy, "ASC".equals(order), criterions);
     }
     else {
       criteria = createCriteria(entityClass, criterions);
     }
 
     Page resultPage = pagedQuery(criteria, page.getPageNo(), page.getPageSize());
 
     resultPage.setOrderBy(orderBy);
     resultPage.setOrder(order);
 
     return resultPage;
   }
 }

