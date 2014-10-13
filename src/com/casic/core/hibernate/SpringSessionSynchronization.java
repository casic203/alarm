 package com.casic.core.hibernate;
 
 import org.hibernate.FlushMode;
 import org.hibernate.HibernateException;
 import org.hibernate.Session;
 import org.hibernate.SessionFactory;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.core.Ordered;
 import org.springframework.dao.DataAccessException;
 import org.springframework.orm.hibernate4.SessionFactoryUtils;
 import org.springframework.orm.hibernate4.SessionHolder;
 import org.springframework.transaction.support.TransactionSynchronization;
 import org.springframework.transaction.support.TransactionSynchronizationManager;
 
 class SpringSessionSynchronization
   implements TransactionSynchronization, Ordered
 {
   private static Logger logger = LoggerFactory.getLogger(SpringSessionSynchronization.class);
   private final SessionHolder sessionHolder;
   private final SessionFactory sessionFactory;
   private boolean holderActive = true;
 
   public SpringSessionSynchronization(SessionHolder sessionHolder, SessionFactory sessionFactory)
   {
     this.sessionHolder = sessionHolder;
     this.sessionFactory = sessionFactory;
   }
 
   private Session getCurrentSession() {
     return this.sessionHolder.getSession();
   }
 
   public int getOrder() {
     return 900;
   }
 
   public void suspend() {
     if (this.holderActive) {
       TransactionSynchronizationManager.unbindResource(this.sessionFactory);
 
       getCurrentSession().disconnect();
     }
   }
 
   public void resume() {
     if (this.holderActive)
       TransactionSynchronizationManager.bindResource(this.sessionFactory, this.sessionHolder);
   }
 
   public void flush()
   {
     try
     {
       logger.debug("Flushing Hibernate Session on explicit request");
       getCurrentSession().flush();
     } catch (HibernateException ex) {
       throw SessionFactoryUtils.convertHibernateAccessException(ex);
     }
   }
 
   public void beforeCommit(boolean readOnly) throws DataAccessException {
     if (!readOnly) {
       Session session = getCurrentSession();
 
       if (!FlushMode.isManualFlushMode(session.getFlushMode()))
         try {
           logger.debug("Flushing Hibernate Session on transaction synchronization");
 
           session.flush();
         } catch (HibernateException ex) {
           throw SessionFactoryUtils.convertHibernateAccessException(ex);
         }
     }
   }
 
   public void beforeCompletion()
   {
     Session session = this.sessionHolder.getSession();
 
     if (this.sessionHolder.getPreviousFlushMode() != null)
     {
       session.setFlushMode(this.sessionHolder.getPreviousFlushMode());
     }
 
     session.disconnect();
   }
 
   public void afterCommit() {
   }
 
   public void afterCompletion(int status) {
     try {
       if (status != 0)
       {
         this.sessionHolder.getSession().clear();
       }
     } finally {
       this.sessionHolder.setSynchronizedWithTransaction(false);
     }
   }
 }

