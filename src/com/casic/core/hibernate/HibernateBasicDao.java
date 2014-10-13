 package com.casic.core.hibernate;
 
 import java.io.Serializable;
 import java.lang.reflect.InvocationTargetException;
 import java.util.Collection;
 import java.util.Iterator;
 import java.util.List;
 import javax.annotation.Resource;
 import javax.transaction.Synchronization;
 import org.apache.commons.beanutils.PropertyUtils;
 import org.hibernate.Criteria;
 import org.hibernate.Hibernate;
 import org.hibernate.Session;
 import org.hibernate.SessionFactory;
 import org.hibernate.criterion.Order;
 import org.hibernate.metadata.ClassMetadata;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.beans.BeansException;
 import org.springframework.context.ApplicationContext;
 import org.springframework.context.ApplicationContextAware;
 import org.springframework.context.ApplicationEvent;
 import org.springframework.jdbc.core.JdbcTemplate;
 import org.springframework.transaction.annotation.Transactional;
 import org.springframework.transaction.support.TransactionSynchronizationAdapter;
 import org.springframework.transaction.support.TransactionSynchronizationManager;
 import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
 
 @Transactional(rollbackFor={Exception.class})
 public class HibernateBasicDao
   implements ApplicationContextAware
 {
   private static Logger logger = LoggerFactory.getLogger(HibernateBasicDao.class);
   private ApplicationContext applicationContext;
   private SessionFactory sessionFactory;
   private JdbcTemplate jdbcTemplate;
 
   public HibernateBasicDao()
   {
   }
 
   public HibernateBasicDao(SessionFactory sessionFactory)
   {
     this.sessionFactory = sessionFactory;
   }
 
   public SessionFactory getSessionFactory()
   {
     return this.sessionFactory;
   }
 
   public JdbcTemplate getJdbcTemplate()
   {
     return this.jdbcTemplate;
   }
 
   public Session getSession()
   {
     return this.sessionFactory.getCurrentSession();
   }
 
   public void setApplicationContext(ApplicationContext applicationContext)
     throws BeansException
   {
     logger.debug("Autowired applicationContext");
     this.applicationContext = applicationContext;
   }
 
   @Resource
   public void setSessionFactory(SessionFactory sessionFactory)
   {
     this.sessionFactory = sessionFactory;
   }
 
   @Resource
   public void setJdbcTemplate(JdbcTemplate jdbcTemplate)
   {
     this.jdbcTemplate = jdbcTemplate;
   }
 
   @Transactional(readOnly=true)
   public <T> T get(Class<T> entityClass, Serializable id)
   {
     Assert.notNull(id, "Id can not be null.");
 
     return (T) getSession().get(entityClass, id);
   }
 
   @Transactional(readOnly=true)
   public <T> T load(Class<T> entityClass, Serializable id)
   {
     Assert.notNull(id, "Id can not be null.");
 
     return (T) getSession().load(entityClass, id);
   }
 
   @Transactional(readOnly=true)
   public <T> List<T> getAll(Class<T> entityClass)
   {
     return getSession().createCriteria(entityClass).list();
   }
 
   @Transactional(readOnly=true)
   public <T> List<T> getAll(Class<T> entityClass, String orderBy, boolean isAsc)
   {
     if (StringUtils.hasText(orderBy)) {
       Criteria criteria = getSession().createCriteria(entityClass);
 
       if (isAsc)
         criteria.addOrder(Order.asc(orderBy));
       else {
         criteria.addOrder(Order.desc(orderBy));
       }
 
       return criteria.list();
     }
     return getAll(entityClass);
   }
 
   @Transactional
   public void save(Object entity)
   {
     Assert.notNull(entity, "Entity can not be null.");
     getSession().saveOrUpdate(entity);
     logger.debug("save entity: {}", entity);
   }
 
   @Transactional
   public void remove(Object entity)
   {
     Assert.notNull(entity, "Entity can not be null.");
     getSession().delete(entity);
     logger.debug("remove entity: {}", entity);
   }
 
   @Transactional
   public <T> void removeById(Class<T> entityClass, Serializable id)
   {
     Assert.notNull(id, "Id can not be null.");
     remove(load(entityClass, id));
     logger.debug("remove entity by id: {}", id);
   }
 
   @Transactional
   public void removeAll(Collection list)
   {
     Assert.notNull(list, "List can not be null.");
 
     for (Iterator i$ = list.iterator(); i$.hasNext(); ) { Object obj = i$.next();
       remove(obj);
     }
   }
 
   @Transactional
   public <T> void removeAll(Class<T> entityClass)
   {
     removeAll(getAll(entityClass));
   }
 
   public void flush()
   {
     getSession().flush();
   }
 
   public void clear()
   {
     getSession().clear();
   }
 
   public void evict(Object entity)
   {
     Assert.notNull(entity, "Entity cannot be null");
     getSession().evict(entity);
   }
 
   public void initialize(Object object)
   {
     Assert.notNull(object, "Object cannot be null");
     Hibernate.initialize(object);
   }
 
   public <T> Serializable getId(Class<T> entityClass, Object o)
     throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
   {
     Assert.notNull(o);
 
     String idName = getIdName(entityClass);
 
     return (Serializable)PropertyUtils.getProperty(o, idName);
   }
 
   public String getIdName(Class entityClass)
   {
     Assert.notNull(entityClass);
 
     ClassMetadata meta = getSessionFactory().getClassMetadata(entityClass);
 
     Assert.notNull(meta, "Class " + entityClass + " not define in hibernate session factory.");
 
     String idName = meta.getIdentifierPropertyName();
     Assert.hasText(idName, entityClass.getSimpleName() + " has no identifier property define.");
 
     return idName;
   }
 
   public void registerSynchronization(Synchronization synchronization)
   {
     TransactionSynchronizationManager.registerSynchronization(new SynchronizationNotification(synchronization));
   }
 
   public void publishEvent(ApplicationEvent applicationEvent)
   {
     this.applicationContext.publishEvent(applicationEvent);
   }
 
   public static class SynchronizationNotification extends TransactionSynchronizationAdapter
   {
     private Synchronization synchronization;
 
     public SynchronizationNotification(Synchronization synchronization) {
       this.synchronization = synchronization;
     }
 
     public void afterCompletion(int status) {
       this.synchronization.afterCompletion(status);
     }
 
     public void beforeCompletion() {
       this.synchronization.beforeCompletion();
     }
   }
 }

