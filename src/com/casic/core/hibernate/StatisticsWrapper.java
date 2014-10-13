 package com.casic.core.hibernate;
 
 import org.hibernate.stat.CollectionStatistics;
 import org.hibernate.stat.EntityStatistics;
 import org.hibernate.stat.NaturalIdCacheStatistics;
 import org.hibernate.stat.QueryStatistics;
 import org.hibernate.stat.SecondLevelCacheStatistics;
 import org.hibernate.stat.Statistics;
 
 public class StatisticsWrapper
   implements Statistics
 {
   public void clear()
   {
   }
 
   public EntityStatistics getEntityStatistics(String entityName)
   {
     return null;
   }
 
   public CollectionStatistics getCollectionStatistics(String role) {
     return null;
   }
 
   public SecondLevelCacheStatistics getSecondLevelCacheStatistics(String regionName)
   {
     return null;
   }
 
   public NaturalIdCacheStatistics getNaturalIdCacheStatistics(String regionName)
   {
     return null;
   }
 
   public QueryStatistics getQueryStatistics(String queryString) {
     return null;
   }
 
   public long getEntityDeleteCount() {
     return 0L;
   }
 
   public long getEntityInsertCount() {
     return 0L;
   }
 
   public long getEntityLoadCount() {
     return 0L;
   }
 
   public long getEntityFetchCount() {
     return 0L;
   }
 
   public long getEntityUpdateCount() {
     return 0L;
   }
 
   public long getQueryExecutionCount() {
     return 0L;
   }
 
   public long getQueryExecutionMaxTime() {
     return 0L;
   }
 
   public String getQueryExecutionMaxTimeQueryString() {
     return null;
   }
 
   public long getQueryCacheHitCount() {
     return 0L;
   }
 
   public long getQueryCacheMissCount() {
     return 0L;
   }
 
   public long getQueryCachePutCount() {
     return 0L;
   }
 
   public long getNaturalIdQueryExecutionCount() {
     return 0L;
   }
 
   public long getNaturalIdQueryExecutionMaxTime() {
     return 0L;
   }
 
   public String getNaturalIdQueryExecutionMaxTimeRegion() {
     return null;
   }
 
   public long getNaturalIdCacheHitCount() {
     return 0L;
   }
 
   public long getNaturalIdCacheMissCount() {
     return 0L;
   }
 
   public long getNaturalIdCachePutCount() {
     return 0L;
   }
 
   public long getUpdateTimestampsCacheHitCount() {
     return 0L;
   }
 
   public long getUpdateTimestampsCacheMissCount() {
     return 0L;
   }
 
   public long getUpdateTimestampsCachePutCount() {
     return 0L;
   }
 
   public long getFlushCount() {
     return 0L;
   }
 
   public long getConnectCount() {
     return 0L;
   }
 
   public long getSecondLevelCacheHitCount() {
     return 0L;
   }
 
   public long getSecondLevelCacheMissCount() {
     return 0L;
   }
 
   public long getSecondLevelCachePutCount() {
     return 0L;
   }
 
   public long getSessionCloseCount() {
     return 0L;
   }
 
   public long getSessionOpenCount() {
     return 0L;
   }
 
   public long getCollectionLoadCount() {
     return 0L;
   }
 
   public long getCollectionFetchCount() {
     return 0L;
   }
 
   public long getCollectionUpdateCount() {
     return 0L;
   }
 
   public long getCollectionRemoveCount() {
     return 0L;
   }
 
   public long getCollectionRecreateCount() {
     return 0L;
   }
 
   public long getStartTime() {
     return 0L;
   }
 
   public void logSummary() {
   }
 
   public boolean isStatisticsEnabled() {
     return false;
   }
 
   public void setStatisticsEnabled(boolean b) {
   }
 
   public String[] getQueries() {
     return null;
   }
 
   public String[] getEntityNames() {
     return null;
   }
 
   public String[] getCollectionRoleNames() {
     return null;
   }
 
   public String[] getSecondLevelCacheRegionNames() {
     return null;
   }
 
   public long getSuccessfulTransactionCount() {
     return 0L;
   }
 
   public long getTransactionCount() {
     return 0L;
   }
 
   public long getPrepareStatementCount() {
     return 0L;
   }
 
   public long getCloseStatementCount() {
     return 0L;
   }
 
   public long getOptimisticFailureCount() {
     return 0L;
   }
 }

