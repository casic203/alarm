 package com.casic.core.hibernate;
 
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.transaction.PlatformTransactionManager;
 import org.springframework.transaction.TransactionDefinition;
 import org.springframework.transaction.TransactionException;
 import org.springframework.transaction.TransactionStatus;
 
 public class ReadOnlyTransactionManager
   implements PlatformTransactionManager
 {
   private static Logger logger = LoggerFactory.getLogger(ReadOnlyTransactionManager.class);
   private PlatformTransactionManager platformTransactionManager;
   private boolean readOnly;
 
   public TransactionStatus getTransaction(TransactionDefinition definition)
     throws TransactionException
   {
     if (this.readOnly) {
       TransactionDefinition readOnlyTransactionDefinition = new ReadOnlyTransactionDefinition(definition);
 
       return this.platformTransactionManager.getTransaction(readOnlyTransactionDefinition);
     }
 
     return this.platformTransactionManager.getTransaction(definition);
   }
 
   public void commit(TransactionStatus status) throws TransactionException
   {
     this.platformTransactionManager.commit(status);
   }
 
   public void rollback(TransactionStatus status) throws TransactionException {
     logger.info("transaction rollback at : {}", status);
     this.platformTransactionManager.rollback(status);
   }
 
   public void setPlatformTransactionManager(PlatformTransactionManager platformTransactionManager)
   {
     this.platformTransactionManager = platformTransactionManager;
   }
 
   public void setReadOnly(boolean readOnly) {
     this.readOnly = readOnly;
   }
 
   public boolean isReadOnly() {
     return this.readOnly;
   }
 }

