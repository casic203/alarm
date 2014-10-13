 package com.casic.core.hibernate;
 
 import java.io.Serializable;
 import java.sql.Connection;
 import java.util.Map;
 import java.util.Set;
 import javax.naming.NamingException;
 import javax.naming.Reference;
 import org.hibernate.Cache;
 import org.hibernate.HibernateException;
 import org.hibernate.Session;
 import org.hibernate.SessionBuilder;
 import org.hibernate.SessionFactory;
 import org.hibernate.SessionFactory.SessionFactoryOptions;
 import org.hibernate.StatelessSession;
 import org.hibernate.StatelessSessionBuilder;
 import org.hibernate.TypeHelper;
 import org.hibernate.engine.spi.FilterDefinition;
 import org.hibernate.metadata.ClassMetadata;
 import org.hibernate.metadata.CollectionMetadata;
 import org.hibernate.stat.Statistics;
 
 public class SessionFactoryWrapper
   implements SessionFactory
 {
   private SessionFactory sessionFactory;
   private SpringSessionContext springSessionContext;
 
   public SessionFactoryWrapper()
   {
     this.springSessionContext = new SpringSessionContext(this);
   }
 
   public void setSessionFactory(SessionFactory sessionFactory) {
     this.sessionFactory = sessionFactory;
   }
 
   public SessionFactory.SessionFactoryOptions getSessionFactoryOptions()
   {
     return this.sessionFactory.getSessionFactoryOptions();
   }
 
   public SessionBuilder withOptions() {
     return this.sessionFactory.withOptions();
   }
 
   public Session openSession() throws HibernateException {
     return this.sessionFactory.openSession();
   }
 
   public Session getCurrentSession()
     throws HibernateException
   {
     return this.springSessionContext.currentSession();
   }
 
   public StatelessSessionBuilder withStatelessOptions() {
     return this.sessionFactory.withStatelessOptions();
   }
 
   public StatelessSession openStatelessSession() {
     return this.sessionFactory.openStatelessSession();
   }
 
   public StatelessSession openStatelessSession(Connection connection) {
     return this.sessionFactory.openStatelessSession(connection);
   }
 
   public ClassMetadata getClassMetadata(Class entityClass) {
     return this.sessionFactory.getClassMetadata(entityClass);
   }
 
   public ClassMetadata getClassMetadata(String entityName) {
     return this.sessionFactory.getClassMetadata(entityName);
   }
 
   public CollectionMetadata getCollectionMetadata(String roleName) {
     return this.sessionFactory.getCollectionMetadata(roleName);
   }
 
   public Map<String, ClassMetadata> getAllClassMetadata() {
     return this.sessionFactory.getAllClassMetadata();
   }
 
   public Map getAllCollectionMetadata() {
     return this.sessionFactory.getAllCollectionMetadata();
   }
 
   public Statistics getStatistics()
   {
     return new StatisticsWrapper();
   }
 
   public void close() throws HibernateException {
     this.sessionFactory.close();
   }
 
   public boolean isClosed() {
     return this.sessionFactory.isClosed();
   }
 
   public Cache getCache() {
     return this.sessionFactory.getCache();
   }
 
   public void evict(Class persistentClass) throws HibernateException {
     throw new UnsupportedOperationException();
   }
 
   public void evict(Class persistentClass, Serializable id) throws HibernateException
   {
     throw new UnsupportedOperationException();
   }
 
   public void evictEntity(String entityName) throws HibernateException {
     throw new UnsupportedOperationException();
   }
 
   public void evictEntity(String entityName, Serializable id) throws HibernateException
   {
     throw new UnsupportedOperationException();
   }
 
   public void evictCollection(String roleName) throws HibernateException {
     throw new UnsupportedOperationException();
   }
 
   public void evictCollection(String roleName, Serializable id) throws HibernateException
   {
     throw new UnsupportedOperationException();
   }
 
   public void evictQueries(String cacheRegion) throws HibernateException {
     throw new UnsupportedOperationException();
   }
 
   public void evictQueries() throws HibernateException {
     throw new UnsupportedOperationException();
   }
 
   public Set getDefinedFilterNames() {
     return this.sessionFactory.getDefinedFilterNames();
   }
 
   public FilterDefinition getFilterDefinition(String filterName) throws HibernateException
   {
     return this.sessionFactory.getFilterDefinition(filterName);
   }
 
   public boolean containsFetchProfileDefinition(String name) {
     return this.sessionFactory.containsFetchProfileDefinition(name);
   }
 
   public TypeHelper getTypeHelper() {
     return this.sessionFactory.getTypeHelper();
   }
 
   public Reference getReference() throws NamingException
   {
     return this.sessionFactory.getReference();
   }
 }

