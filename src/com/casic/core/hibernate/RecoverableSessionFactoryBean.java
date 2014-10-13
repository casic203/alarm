 package com.casic.core.hibernate;
 
 import java.io.IOException;
 import java.util.Properties;
 import javax.sql.DataSource;
 import org.hibernate.SessionFactory;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.beans.factory.DisposableBean;
 import org.springframework.beans.factory.FactoryBean;
 import org.springframework.beans.factory.InitializingBean;
 import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
 
 public class RecoverableSessionFactoryBean
   implements FactoryBean, DisposableBean, InitializingBean
 {
   private static Logger logger = LoggerFactory.getLogger(RecoverableSessionFactoryBean.class);
   private SessionFactoryWrapper sessionFactoryWrapper;
   private DataSource dataSource;
   private Properties hibernateProperties;
   private String[] packagesToScan;
   private LocalSessionFactoryBean localSessionFactoryBean;
 
   public void afterPropertiesSet()
     throws IOException
   {
     this.sessionFactoryWrapper = new SessionFactoryWrapper();
     try
     {
       this.localSessionFactoryBean = new LocalSessionFactoryBean();
       this.localSessionFactoryBean.setDataSource(this.dataSource);
       this.localSessionFactoryBean.setHibernateProperties(this.hibernateProperties);
       this.localSessionFactoryBean.setPackagesToScan(this.packagesToScan);
 
       this.localSessionFactoryBean.afterPropertiesSet();
 
       SessionFactory sessionFactory = this.localSessionFactoryBean.getObject();
       this.sessionFactoryWrapper.setSessionFactory(sessionFactory);
     } catch (Exception ex) {
       logger.error("", ex);
     }
   }
 
   public void destroy() {
     this.sessionFactoryWrapper = null;
     this.localSessionFactoryBean.destroy();
   }
 
   public Object getObject() {
     return this.sessionFactoryWrapper;
   }
 
   public Class getObjectType() {
     return SessionFactory.class;
   }
 
   public boolean isSingleton() {
     return true;
   }
 
   public void setDataSource(DataSource dataSource)
   {
     this.dataSource = dataSource;
   }
 
   public void setHibernateProperties(Properties hibernateProperties) {
     this.hibernateProperties = hibernateProperties;
   }
 
   public void setPackagesToScan(String[] packagesToScan) {
     this.packagesToScan = packagesToScan;
   }
 }

